package com.example.bestyou.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.bestyou.R;
import com.example.bestyou.adapters.MyCartAdapter;
import com.example.bestyou.adapters.ShowAllWorkoutsAdapter;
import com.example.bestyou.models.MyCartModel;
import com.example.bestyou.models.ShowAllWorkoutsModel;
import com.example.bestyou.models.WorkoutPlansModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShowAllWorkoutsActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    ShowAllWorkoutsAdapter showAllWorkoutsAdapter;
    List<ShowAllWorkoutsModel> showAllWorkoutsModelList;
    MyCartAdapter myCartAdapter;
    List<MyCartModel> myCartModelList;

    Toolbar toolbar;

    FirebaseFirestore fireStore;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_workouts);


        toolbar = findViewById(R.id.show_all_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set the back arrow color to white
        if (toolbar.getNavigationIcon() != null) {
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.black));

        // navigation bar color to black
        window.setNavigationBarColor(getResources().getColor(R.color.black));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String type = getIntent().getStringExtra("type");

        fireStore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.show_all_rec);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        showAllWorkoutsModelList = new ArrayList<>();
        showAllWorkoutsAdapter = new ShowAllWorkoutsAdapter(this, showAllWorkoutsModelList);
        recyclerView.setAdapter(showAllWorkoutsAdapter);


        if (type == null || type.isEmpty()) {
            fireStore.collection("ShowAllWorkouts")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                for (DocumentSnapshot doc : task.getResult().getDocuments()) {

                                    ShowAllWorkoutsModel showAllWorkoutsModel = doc.toObject(ShowAllWorkoutsModel.class);
                                    showAllWorkoutsModelList.add(showAllWorkoutsModel);
                                    showAllWorkoutsAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });

        }

        if (type != null && type.equalsIgnoreCase("chest")) {
            fireStore.collection("ShowAllWorkouts").whereEqualTo("type", "chest")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                for (DocumentSnapshot doc : task.getResult().getDocuments()) {

                                    ShowAllWorkoutsModel showAllWorkoutsModel = doc.toObject(ShowAllWorkoutsModel.class);
                                    showAllWorkoutsModelList.add(showAllWorkoutsModel);
                                    showAllWorkoutsAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });

        }

        if (type != null && type.equalsIgnoreCase("arms")) {
            fireStore.collection("ShowAllWorkouts").whereEqualTo("type", "arms")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                for (DocumentSnapshot doc : task.getResult().getDocuments()) {

                                    ShowAllWorkoutsModel showAllWorkoutsModel = doc.toObject(ShowAllWorkoutsModel.class);
                                    showAllWorkoutsModelList.add(showAllWorkoutsModel);
                                    showAllWorkoutsAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });

        }

        if (type != null && type.equalsIgnoreCase("shoulders")) {
            fireStore.collection("ShowAllWorkouts").whereEqualTo("type", "shoulders")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                for (DocumentSnapshot doc : task.getResult().getDocuments()) {

                                    ShowAllWorkoutsModel showAllWorkoutsModel = doc.toObject(ShowAllWorkoutsModel.class);
                                    showAllWorkoutsModelList.add(showAllWorkoutsModel);
                                    showAllWorkoutsAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });

        }

        if (type != null && type.equalsIgnoreCase("legs")) {
            fireStore.collection("ShowAllWorkouts").whereEqualTo("type", "legs")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                for (DocumentSnapshot doc : task.getResult().getDocuments()) {

                                    ShowAllWorkoutsModel showAllWorkoutsModel = doc.toObject(ShowAllWorkoutsModel.class);
                                    showAllWorkoutsModelList.add(showAllWorkoutsModel);
                                    showAllWorkoutsAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
        }

        if (type != null && type.equalsIgnoreCase("back")) {
            fireStore.collection("ShowAllWorkouts").whereEqualTo("type", "back")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                for (DocumentSnapshot doc : task.getResult().getDocuments()) {

                                    ShowAllWorkoutsModel showAllWorkoutsModel = doc.toObject(ShowAllWorkoutsModel.class);
                                    showAllWorkoutsModelList.add(showAllWorkoutsModel);
                                    showAllWorkoutsAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });

        }

        if (type != null && type.equalsIgnoreCase("cardio")) {
            fireStore.collection("ShowAllWorkouts").whereEqualTo("type", "cardio")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                for (DocumentSnapshot doc : task.getResult().getDocuments()) {

                                    ShowAllWorkoutsModel showAllWorkoutsModel = doc.toObject(ShowAllWorkoutsModel.class);
                                    showAllWorkoutsModelList.add(showAllWorkoutsModel);
                                    showAllWorkoutsAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });

        }

    }
}