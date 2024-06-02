package com.example.bestyou.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bestyou.R;
import com.example.bestyou.adapters.MyCartAdapter;
import com.example.bestyou.models.MyCartModel;
import com.example.bestyou.models.PopularWorkoutsModel;
import com.example.bestyou.models.ShowAllWorkoutsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity implements MyCartAdapter.OnItemCheckedChangeListener {

    RecyclerView recyclerView;
    List<MyCartModel> myCartModelList;
    MyCartAdapter myCartAdapter;
    MyCartModel MyCartModel = null;
    ShowAllWorkoutsModel showAllWorkoutsModel = null;
    //Popular Products
    PopularWorkoutsModel popularWorkoutsModel = null;
    private FirebaseAuth auth;
    Toolbar toolbar;
    FirebaseFirestore fireStore;
    Button doneButton, deleteButton;
    private String dayPlanned, name, bodyPart, sets, reps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        auth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.my_cart_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dayPlanned = getIntent().getStringExtra("dayPlanned");
        name = getIntent().getStringExtra("name");
        bodyPart = getIntent().getStringExtra("bodyPart");
        sets = getIntent().getStringExtra("sets");
        reps = getIntent().getStringExtra("reps");

        fireStore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.show_all_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myCartModelList = new ArrayList<>();
        myCartAdapter = new MyCartAdapter(this, myCartModelList, this);
        recyclerView.setAdapter(myCartAdapter);

        final List<MyCartModel> allItems = new ArrayList<>();


        if (dayPlanned == null || dayPlanned.isEmpty()) {
            loadDataForWeek(allItems);
        } else {
            loadDataForDay(dayPlanned, allItems);
        }

        deleteButton = findViewById(R.id.btn_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCartAdapter.removeCheckedItems(fireStore, auth);
            }
        });

        doneButton = findViewById(R.id.btn_done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCartAdapter.setDoneClicked(true);
                addToWorkoutComplete();
            }
        });
    }

    @Override
    public void onCheckedChanged(int position, boolean isChecked) {
        if (isChecked) {
            doneButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
        } else {
            doneButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
        }
    }

    private void addToWorkoutComplete() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateandTime = sdf.format(Calendar.getInstance().getTime());

        for (MyCartModel cartItem : myCartModelList) {
            if (cartItem.isChecked()) {
                HashMap<String, Object> workoutData = new HashMap<>();
                workoutData.put("img_url", cartItem.getImg_url());
                workoutData.put("workoutName", cartItem.getWorkoutName());
                workoutData.put("bodyPart", cartItem.getBodyPart());
                workoutData.put("numberOfReps", cartItem.getNumberOfReps());
                workoutData.put("numberOfSets", cartItem.getNumberOfSets());
                workoutData.put("timestamp", currentDateandTime);

                fireStore.collection("WorkoutDone").document(auth.getCurrentUser().getUid())
                        .collection("User").add(workoutData).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                Toast.makeText(CartActivity.this, "Workout Done!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }

    private void loadDataForWeek(final List<MyCartModel> allItems) {
        String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

        for (String day : daysOfWeek) {
            loadDataForDay(day, allItems);
        }
    }

    private void loadDataForDay(String day, final List<MyCartModel> allItems) {
        fireStore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("User").whereEqualTo("dayPlanned", day)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            List<MyCartModel> tempList = new ArrayList<>();
                            for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                MyCartModel myCartModel = doc.toObject(MyCartModel.class);
                                myCartModel.setDocumentId(doc.getId());
                                tempList.add(myCartModel);
                            }

                            // Sort the list by date in descending order (latest first)
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            Collections.sort(tempList, new Comparator<MyCartModel>() {
                                @Override
                                public int compare(MyCartModel o1, MyCartModel o2) {
                                    try {
                                        Date date1 = dateFormat.parse(o1.getCurrentDate());
                                        Date date2 = dateFormat.parse(o2.getCurrentDate());
                                        // Reverse the comparison to sort in descending order
                                        return date2.compareTo(date1);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                        return 0;
                                    }
                                }
                            });

                            // Add headers and items to the final list
                            String lastProcessedDate = null;
                            for (MyCartModel myCartModel : tempList) {
                                String currentDate = myCartModel.getCurrentDate();
                                if (currentDate != null && !currentDate.equals(lastProcessedDate)) {
                                    MyCartModel dateHeader = new MyCartModel();
                                    dateHeader.setDateHeader(true);
                                    dateHeader.setCurrentDate(currentDate);
                                    myCartModelList.add(dateHeader);
                                    lastProcessedDate = currentDate;
                                }
                                myCartModelList.add(myCartModel);
                            }

                            myCartAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}