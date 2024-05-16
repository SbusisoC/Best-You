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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

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
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        myCartModelList = new ArrayList<>();
        myCartAdapter = new MyCartAdapter(this, myCartModelList, this);
        recyclerView.setAdapter(myCartAdapter);

        if (dayPlanned == null || dayPlanned.isEmpty()) {
            fireStore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                    .collection("User")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                for (DocumentSnapshot doc : task.getResult().getDocuments()) {

                                    MyCartModel myCartModel = doc.toObject(MyCartModel.class);
                                    myCartModelList.add(myCartModel);
                                    myCartModel.setDocumentId(doc.getId());
                                    myCartAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });

        }

        if (dayPlanned != null && dayPlanned.equalsIgnoreCase("Sun")) {
            fireStore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                    .collection("User").whereEqualTo("dayPlanned", "Sun")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                for (DocumentSnapshot doc : task.getResult().getDocuments()) {

                                    String documentId = doc.getId();

                                    MyCartModel myCartModel = doc.toObject(MyCartModel.class);

                                    myCartModel.setDocumentId(documentId);
                                    myCartModelList.add(myCartModel);
                                    myCartModel.setDocumentId(doc.getId());
                                    myCartAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });

        }

        if (dayPlanned != null && dayPlanned.equalsIgnoreCase("Mon")) {
            fireStore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                    .collection("User").whereEqualTo("dayPlanned", "Mon")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                for (DocumentSnapshot doc : task.getResult().getDocuments()) {

                                    MyCartModel myCartModel = doc.toObject(MyCartModel.class);
                                    myCartModelList.add(myCartModel);
                                    myCartModel.setDocumentId(doc.getId());
                                    myCartAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });

        }

        if (dayPlanned != null && dayPlanned.equalsIgnoreCase("Tue")) {
            fireStore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                    .collection("User").whereEqualTo("dayPlanned", "Tue")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                for (DocumentSnapshot doc : task.getResult().getDocuments()) {

                                    MyCartModel myCartModel = doc.toObject(MyCartModel.class);
                                    myCartModelList.add(myCartModel);
                                    myCartModel.setDocumentId(doc.getId());
                                    myCartAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });

        }

        if (dayPlanned != null && dayPlanned.equalsIgnoreCase("Wed")) {
            fireStore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                    .collection("User").whereEqualTo("dayPlanned", "Wed")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                for (DocumentSnapshot doc : task.getResult().getDocuments()) {

                                    MyCartModel myCartModel = doc.toObject(MyCartModel.class);
                                    myCartModelList.add(myCartModel);
                                    myCartModel.setDocumentId(doc.getId());
                                    myCartAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
        }

        if (dayPlanned != null && dayPlanned.equalsIgnoreCase("Thu")) {
            fireStore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                    .collection("User").whereEqualTo("dayPlanned", "Thu")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                for (DocumentSnapshot doc : task.getResult().getDocuments()) {

                                    MyCartModel myCartModel = doc.toObject(MyCartModel.class);
                                    myCartModelList.add(myCartModel);
                                    myCartModel.setDocumentId(doc.getId());
                                    myCartAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });

        }

        if (dayPlanned != null && dayPlanned.equalsIgnoreCase("Fri")) {
            fireStore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                    .collection("User").whereEqualTo("dayPlanned", "Fri")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                for (DocumentSnapshot doc : task.getResult().getDocuments()) {

                                    MyCartModel myCartModel = doc.toObject(MyCartModel.class);
                                    myCartModelList.add(myCartModel);
                                    myCartModel.setDocumentId(doc.getId());
                                    myCartAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });

        }

        if (dayPlanned != null && dayPlanned.equalsIgnoreCase("Sat")) {
            fireStore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                    .collection("User").whereEqualTo("dayPlanned", "Sat")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                for (DocumentSnapshot doc : task.getResult().getDocuments()) {

                                    MyCartModel myCartModel = doc.toObject(MyCartModel.class);
                                    myCartModelList.add(myCartModel);
                                    myCartModel.setDocumentId(doc.getId());
                                    myCartAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
        }

        deleteButton = findViewById(R.id.btn_delete);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the removeCheckedItems method in your adapter
                myCartAdapter.removeCheckedItems(fireStore, auth);
                /* myCartAdapter.removeCheckedItems();*/
            }
        });

        doneButton = findViewById(R.id.btn_done);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set the isDoneClicked flag to true when the "Done" button is clicked
                myCartAdapter.setDoneClicked(true);
                addToWorkoutComplete();
            }
        });
    }

    @Override
    public void onCheckedChanged(int position, boolean isChecked) {
        // Update the visibility of buttons based on the checkbox state
        if (isChecked) {
            doneButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
        } else {
            doneButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
        }
    }

    private void addToWorkoutComplete() {
        // Get current date and time
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateandTime = sdf.format(Calendar.getInstance().getTime());

        // Iterate through myCartModelList to check for checked items
        for (MyCartModel cartItem : myCartModelList) {
            if (cartItem.isChecked()) {
                // Create a HashMap to hold workout details
                HashMap<String, Object> workoutData = new HashMap<>();
                workoutData.put("img_url", cartItem.getImg_url());
                workoutData.put("workoutName", cartItem.getWorkoutName());
                workoutData.put("bodyPart", cartItem.getBodyPart());
                workoutData.put("numberOfReps", cartItem.getNumberOfReps());
                workoutData.put("numberOfSets", cartItem.getNumberOfSets());
                workoutData.put("timestamp", currentDateandTime); // Add timestamp

                // Add the workoutData to the "workoutDone" collection
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
}
