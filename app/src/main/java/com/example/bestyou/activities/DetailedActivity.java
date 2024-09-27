package com.example.bestyou.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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
    TextView name, bodyPart, dayTextView, textTime, Time;
    Button addToPlan;
    Toolbar toolbar;

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
    private String selectedTime;
    private Calendar selectedDate;

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

        // Initialize Views
        textTime = findViewById(R.id.textTime);
        Time = findViewById(R.id.Time);
        recyclerView = findViewById(R.id.recylerView);
        detailedImg = findViewById(R.id.detailed_img);
        name = findViewById(R.id.detailed_name);
        bodyPart = findViewById(R.id.part);
        dayTextView = findViewById(R.id.dayTextView);
        leftArrow = findViewById(R.id.leftArrow);
        rightArrow = findViewById(R.id.rightArrow);
        addToPlan = findViewById(R.id.add_to_plan);
        sets = findViewById(R.id.editTextSets);
        reps = findViewById(R.id.editTextReps);

        // Initialize RecyclerView and Adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setEntryAdapter = new SetEntryAdapter(0); // Initially 0 sets
        recyclerView.setAdapter(setEntryAdapter);

        final Object obj = getIntent().getSerializableExtra("detailed");
        if (obj instanceof PopularWorkoutsModel) {
            popularWorkoutsModel = (PopularWorkoutsModel) obj;
        } else if (obj instanceof ShowAllWorkoutsModel) {
            showAllWorkoutsModel = (ShowAllWorkoutsModel) obj;
        }

        if (showAllWorkoutsModel != null && "cardio".equalsIgnoreCase(showAllWorkoutsModel.getType())) {
            // Hide reps, sets, and recycler view
            findViewById(R.id.textReps).setVisibility(View.GONE);
            findViewById(R.id.editTextReps).setVisibility(View.GONE);
            findViewById(R.id.textSets).setVisibility(View.GONE);
            findViewById(R.id.editTextSets).setVisibility(View.GONE);
            recyclerView.setVisibility(View.INVISIBLE);

            // Show time-related views
            textTime.setVisibility(View.VISIBLE);
            Time.setVisibility(View.VISIBLE);
        } else {
            // Default case: Show reps, sets, and recycler view for non-cardio workouts
            findViewById(R.id.textReps).setVisibility(View.VISIBLE);
            findViewById(R.id.editTextReps).setVisibility(View.VISIBLE);
            findViewById(R.id.textSets).setVisibility(View.VISIBLE);
            findViewById(R.id.editTextSets).setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);

            // Hide time-related views
            textTime.setVisibility(View.GONE);
            Time.setVisibility(View.GONE);
        }

        if (popularWorkoutsModel != null) {
            Glide.with(getApplicationContext()).asGif().load(popularWorkoutsModel.getImg_url()).into(detailedImg);
            name.setText(popularWorkoutsModel.getName());
            bodyPart.setText(popularWorkoutsModel.getBodyPart());

        }

        if (showAllWorkoutsModel != null) {
            Glide.with(getApplicationContext()).asGif().load(showAllWorkoutsModel.getImg_url()).into(detailedImg);
            Glide.with(getApplicationContext()).load(showAllWorkoutsModel.getImg_url()).into(detailedImg);
            name.setText(showAllWorkoutsModel.getName());
            bodyPart.setText(showAllWorkoutsModel.getBodyPart());

        }

        Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

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
                showDatePickerDialog();
                /*decrementDay();
                updateDay();*/
            }
        });

        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
                /*incrementDay();
                updateDay();*/
            }
        });

    }

      private void addtoPlan() {
          String saveCurrentTime;
          Calendar calForDate = Calendar.getInstance();

          // Use selectedDate from date picker if it is set, otherwise use current date
          SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
          String saveCurrentDate = (selectedDate != null) ? dateFormat.format(selectedDate.getTime()) : dateFormat.format(calForDate.getTime());

          SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
          saveCurrentTime = currentTime.format(calForDate.getTime());

        final HashMap<String, Object> cartMap = new HashMap<>();

        int numberOfReps = Integer.parseInt(reps.getText().toString());


        // Check which model is not null and get the image URL accordingly
        String imageUrl = "";
        String workoutType = "";
        if (popularWorkoutsModel != null) {
            imageUrl = popularWorkoutsModel.getImg_url();
            workoutType = popularWorkoutsModel.getType();
        } else if (showAllWorkoutsModel != null) {
            imageUrl = showAllWorkoutsModel.getImg_url();
            workoutType = showAllWorkoutsModel.getType();
        }

        cartMap.put("img_url", imageUrl);
        cartMap.put("workoutName", name.getText().toString());
        cartMap.put("bodyPart", bodyPart.getText().toString());
        cartMap.put("currentTime", saveCurrentTime);
        cartMap.put("currentDate", saveCurrentDate);
        cartMap.put("time",System.currentTimeMillis());
        cartMap.put("numberOfSets", sets.getText().toString());
        cartMap.put("numberOfReps", reps.getText().toString());
        cartMap.put("dayPlanned", dayTextView.getText().toString());
        cartMap.put("type", workoutType);

          // Add the selected workout time (if it's a cardio workout)
          if (selectedTime != null && !selectedTime.isEmpty()) {
              cartMap.put("workoutTime", selectedTime);
          }

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

    private void showTimePickerDialog() {
        // Create an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailedActivity.this);
        LayoutInflater inflater = getLayoutInflater();

        // Inflate the custom layout for the time picker
        View dialogView = inflater.inflate(R.layout.dialog_time_picker, null);
        builder.setView(dialogView);

        // Initialize NumberPickers for hours, minutes, and seconds
        final NumberPicker hoursPicker = dialogView.findViewById(R.id.hours_picker);
        final NumberPicker minutesPicker = dialogView.findViewById(R.id.minutes_picker);
        final NumberPicker secondsPicker = dialogView.findViewById(R.id.seconds_picker);

        // Set NumberPicker ranges
        hoursPicker.setMinValue(0);
        hoursPicker.setMaxValue(23);
        minutesPicker.setMinValue(0);
        minutesPicker.setMaxValue(59);
        secondsPicker.setMinValue(0);
        secondsPicker.setMaxValue(59);

        // Handle OK button click
        builder.setPositiveButton("OK", (dialog, which) -> {
            int hours = hoursPicker.getValue();
            int minutes = minutesPicker.getValue();
            int seconds = secondsPicker.getValue();

            // Store selected time in the format "HH:mm:ss.SSS"
            selectedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);

            // Update the Time TextView with selected time
            Time.setText(selectedTime);
        });

        // Handle Cancel button click
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Show the dialog
        builder.create().show();
    }
    private void showDatePickerDialog() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        // Update the TextView with the selected date
                        selectedDate = Calendar.getInstance();
                        selectedDate.set(selectedYear, selectedMonth, selectedDay);

                        SimpleDateFormat sdf = new SimpleDateFormat("EEE"); // Format the date to display the day of the week
                        String dayOfWeek = sdf.format(selectedDate.getTime());
                        dayTextView.setText(dayOfWeek); // Update the dayTextView with the selected day
                    }
                },
                year, month, day
        );

        // Show the DatePickerDialog
        datePickerDialog.show();
    }
}

