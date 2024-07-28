package com.example.bestyou.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bestyou.R;
/*import com.example.bestyou.models.NewProductsModel;*/
import com.example.bestyou.adapters.SetEntryAdapter;
import com.example.bestyou.models.PopularWorkoutsModel;
import com.example.bestyou.models.ShowAllWorkoutsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DetailedActivity extends AppCompatActivity {

    ImageView detailedImg, leftArrow, rightArrow;
    TextView name, bodyPart, dayTextView;
    Button addToPlan;
    /*ImageView addItems, removeItems;*/

    Toolbar toolbar;

    /*int totalQuantity = 1;
    int totalPrice = 0;*/

    //New Products
    /*NewProductsModel newProductsModel = null;*/

    //Show All
    ShowAllWorkoutsModel showAllWorkoutsModel = null;
    //Popular Products
    PopularWorkoutsModel popularWorkoutsModel = null;
    FirebaseAuth auth;
    private FirebaseFirestore fireStore;
    private int currentDayIndex = 0;
    private String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    private EditText sets, reps;
    private Spinner weightSpinner;
    private RecyclerView recyclerView;
    private SetEntryAdapter setEntryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        toolbar = findViewById(R.id.detailed_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fireStore = FirebaseFirestore.getInstance();

        auth = FirebaseAuth.getInstance();

        final Object obj = getIntent().getSerializableExtra("detailed");


        if (obj instanceof PopularWorkoutsModel) {
            popularWorkoutsModel = (PopularWorkoutsModel) obj;
        } else if (obj instanceof ShowAllWorkoutsModel) {
            showAllWorkoutsModel = (ShowAllWorkoutsModel) obj;
        }

        detailedImg = findViewById(R.id.detailed_img);
        name = findViewById(R.id.detailed_name);
        bodyPart = findViewById(R.id.part);

        dayTextView = findViewById(R.id.dayTextView);
        leftArrow = findViewById(R.id.leftArrow);
        rightArrow = findViewById(R.id.rightArrow);
        addToPlan = findViewById(R.id.add_to_plan);

        // Initialize RecyclerView and Adapter
        recyclerView = findViewById(R.id.recylerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setEntryAdapter = new SetEntryAdapter(0); // Initially 0 sets
        recyclerView.setAdapter(setEntryAdapter);

        sets = findViewById(R.id.editTextSets);
        reps = findViewById(R.id.editTextReps);

        if (popularWorkoutsModel != null) {
            Glide.with(getApplicationContext()).load(popularWorkoutsModel.getImg_url()).into(detailedImg);
            name.setText(popularWorkoutsModel.getName());
            bodyPart.setText(popularWorkoutsModel.getBodyPart());

        }

        if (showAllWorkoutsModel != null) {
            Glide.with(getApplicationContext()).load(showAllWorkoutsModel.getImg_url()).into(detailedImg);
            name.setText(showAllWorkoutsModel.getName());
            bodyPart.setText(showAllWorkoutsModel.getBodyPart());

        }

        sets.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No action needed here
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    int numberOfSets = Integer.parseInt(s.toString());
                    setEntryAdapter.updateData(numberOfSets);
                }
            }
        });

        addToPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addtoPlan();
            }
        });
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementDay();
                updateDay();
            }
        });

        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementDay();
                updateDay();
            }
        });

    }

      private void addtoPlan() {

        String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd, MM, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final HashMap<String, Object> cartMap = new HashMap<>();

        int numberOfReps = Integer.parseInt(reps.getText().toString());


        // Check which model is not null and get the image URL accordingly
        String imageUrl = "";
        if (popularWorkoutsModel != null) {
            imageUrl = popularWorkoutsModel.getImg_url();
        } else if (showAllWorkoutsModel != null) {
            imageUrl = showAllWorkoutsModel.getImg_url();
        }

        cartMap.put("img_url", imageUrl);
        cartMap.put("workoutName", name.getText().toString());
        cartMap.put("bodyPart", bodyPart.getText().toString());
        cartMap.put("currentTime", saveCurrentTime);
        cartMap.put("currentDate", saveCurrentDate);

        //
        cartMap.put("time",System.currentTimeMillis());
        //

        cartMap.put("numberOfSets", sets.getText().toString());
        cartMap.put("numberOfReps", reps.getText().toString());
        cartMap.put("dayPlanned", dayTextView.getText().toString());

          // Collecting the sets data
          int numberOfSets = Integer.parseInt(sets.getText().toString());
          for (int i = 0; i < numberOfSets; i++) {
              View view = recyclerView.getLayoutManager().findViewByPosition(i);
              if (view != null) {
                  EditText repsEditText = view.findViewById(R.id.editTextReps);
                  EditText weightEditText = view.findViewById(R.id.weightEntry);
                  String reps = repsEditText.getText().toString();
                  String weight = weightEditText.getText().toString();
                  cartMap.put("set_" + (i + 1) + "_reps", reps);
                  cartMap.put("set_" + (i + 1) + "_weight", weight);
              }
          }

        fireStore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("User").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(DetailedActivity.this, "Added To A Plan", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });


    }
    private void decrementDay() {
        currentDayIndex = (currentDayIndex - 1 + daysOfWeek.length) % daysOfWeek.length;
    }

    private void incrementDay() {

        currentDayIndex = (currentDayIndex + 1) % daysOfWeek.length;
    }

    private void updateDay() {

        dayTextView.setText(daysOfWeek[currentDayIndex]);
    }
}

