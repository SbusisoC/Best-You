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
import android.content.SharedPreferences;

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
    private static final String PREFS_NAME = "CartPrefs";
    private static final String IS_DONE_CLICKED_KEY = "isDoneClicked";

    @Override
    protected void onPause() {
        super.onPause();
        saveCheckedStates();
        saveDoneClickedState();
    }
    @Override
    protected void onResume() {
        super.onResume();
        restoreCheckedStates();
        restoreDoneClickedState();
    }

    private void saveCheckedStates() {
        SharedPreferences sharedPreferences = getSharedPreferences("CartPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        for (MyCartModel item : myCartModelList) {
            editor.putBoolean(item.getDocumentId(), item.isChecked());
        }

        editor.apply();
    }

    private void restoreCheckedStates() {
        SharedPreferences sharedPreferences = getSharedPreferences("CartPrefs", MODE_PRIVATE);

        for (MyCartModel item : myCartModelList) {
            item.setChecked(sharedPreferences.getBoolean(item.getDocumentId(), false));
        }

        myCartAdapter.notifyDataSetChanged();
    }

    private void saveDoneClickedState() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_DONE_CLICKED_KEY, myCartAdapter.isDoneClicked());
        editor.apply();
    }

    private void restoreDoneClickedState() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isDoneClicked = sharedPreferences.getBoolean(IS_DONE_CLICKED_KEY, false);
        myCartAdapter.setDoneClicked(isDoneClicked);
    }

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

        restoreCheckedStates();


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
                        .collection("User")
                        .whereEqualTo("img_url", cartItem.getImg_url())
                        .whereEqualTo("workoutName", cartItem.getWorkoutName())
                        .whereEqualTo("bodyPart", cartItem.getBodyPart())
                        .whereEqualTo("numberOfReps", cartItem.getNumberOfReps())
                        .whereEqualTo("numberOfSets", cartItem.getNumberOfSets())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().isEmpty()) {
                                        // Item does not exist, add it to the collection
                                        fireStore.collection("WorkoutDone").document(auth.getCurrentUser().getUid())
                                                .collection("User").add(workoutData).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(CartActivity.this, "Workout Done!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    } else {
                                        // Item already exists, show a message or handle accordingly
                                        /*Toast.makeText(CartActivity.this, "Workout already marked as done!", Toast.LENGTH_SHORT).show();*/
                                    }
                                } else {
                                    /*Toast.makeText(CartActivity.this, "Failed to check workout status", Toast.LENGTH_SHORT).show();*/
                                }
                            }
                        });

                /*fireStore.collection("WorkoutDone").document(auth.getCurrentUser().getUid())
                        .collection("User").add(workoutData).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                Toast.makeText(CartActivity.this, "Workout Done!", Toast.LENGTH_SHORT).show();
                            }
                        });*/
            }
        }
    }
    public void removeFromWorkoutComplete(MyCartModel cartItem) {
        fireStore.collection("WorkoutDone").document(auth.getCurrentUser().getUid())
                .collection("User")
                .whereEqualTo("img_url", cartItem.getImg_url())
                .whereEqualTo("workoutName", cartItem.getWorkoutName())
                .whereEqualTo("bodyPart", cartItem.getBodyPart())
                .whereEqualTo("numberOfReps", cartItem.getNumberOfReps())
                .whereEqualTo("numberOfSets", cartItem.getNumberOfSets())
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        for (DocumentSnapshot document : task.getResult().getDocuments()) {
                            document.getReference().delete().addOnCompleteListener(deleteTask -> {
                                if (deleteTask.isSuccessful()) {
                                    Toast.makeText(CartActivity.this, "Workout unmarked as done", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(CartActivity.this, "Failed to unmark workout as done", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
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
                            tempList.sort((o1, o2) -> {
                                Long date1 = o1.getTime();
                                Long date2 = o2.getTime();
                                // Reverse the comparison to sort in descending order
                                return date2.compareTo(date1);
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
                            restoreCheckedStates();

                            myCartAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}
