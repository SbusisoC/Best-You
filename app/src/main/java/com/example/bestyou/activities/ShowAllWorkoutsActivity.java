package com.example.bestyou.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.bestyou.R;
import com.example.bestyou.adapters.ShowAllWorkoutsAdapter;
import com.example.bestyou.models.ShowAllWorkoutsModel;
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

    Toolbar toolbar;

    FirebaseFirestore fireStore;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_workouts);

        toolbar = findViewById(R.id.show_all_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
    }
}