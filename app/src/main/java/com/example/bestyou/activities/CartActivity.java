package com.example.bestyou.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.bestyou.R;
import com.example.bestyou.adapters.MyCartAdapter;
import com.example.bestyou.models.MyCartModel;
import com.example.bestyou.models.ShowAllWorkoutsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements MyCartAdapter.OnItemCheckedChangeListener {

    RecyclerView recyclerView;
    List<MyCartModel> myCartModelList;
    MyCartAdapter myCartAdapter;
    private FirebaseAuth auth;
    Toolbar toolbar;
    FirebaseFirestore fireStore;
    Button doneButton, deleteButton;

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

        String dayPlanned = getIntent().getStringExtra("dayPlanned");

        fireStore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.show_all_rec);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        myCartModelList = new ArrayList<>();
        /*myCartAdapter = new MyCartAdapter(this, myCartModelList);*/
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
        doneButton = findViewById(R.id.btn_done);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the removeCheckedItems method in your adapter
                myCartAdapter.removeCheckedItems(fireStore, auth);
                /* myCartAdapter.removeCheckedItems();*/
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

    }
