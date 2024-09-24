package com.example.bestyou.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bestyou.R;
import com.example.bestyou.adapters.CartSetsAdapter;
import com.example.bestyou.models.MyCartModel;
import com.example.bestyou.models.SetModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CartDetailedActivity extends AppCompatActivity {

    Toolbar toolbar;
    FirebaseAuth auth;
    private FirebaseFirestore fireStore;
    MyCartModel myCartModel = null;
    ImageView detailedImg, startTimerButton, pauseTimerButton, restartTimerButton;
    TextView name, bodyPart, cartNumberOfReps, cartNumberOfSets, cartTextReps, cartTextSets, timerText, Time;
    private RecyclerView recyclerView;
    /*private CartSetsAdapter cartSetsAdapter;*/
    private TextView sets, reps;
    private CartSetsAdapter cartSetsAdapter;
    private CountDownTimer timer;
    private long timeRemaining;

    private MediaPlayer alarmSound;
    private Button stopAlarmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_detailed);

        toolbar = findViewById(R.id.cartDetailed_toolbar);
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

        final Object obj = getIntent().getSerializableExtra("detailedCart");


        if (obj instanceof MyCartModel) {
            myCartModel = (MyCartModel) obj;
        }

        detailedImg = findViewById(R.id.cartDetailed_img);
        name = findViewById(R.id.cartDetailed_name);
        bodyPart = findViewById(R.id.cartPart);
        cartNumberOfReps = findViewById(R.id.cartEditTextReps);
        cartNumberOfSets = findViewById(R.id.cartEditTextSets);
        cartTextReps = findViewById(R.id.cartTextReps);
        cartTextSets = findViewById(R.id.cartTextSets);
        timerText = findViewById(R.id.cartTextTime);
        Time= findViewById(R.id.cartTime);
        startTimerButton = findViewById(R.id.cartStart);
        pauseTimerButton = findViewById(R.id.cartPause);
        restartTimerButton = findViewById(R.id.cartRestart);
        alarmSound = MediaPlayer.create(this, R.raw.alarm_sound);  // Ensure sound file is in res/raw

        // Initialize Stop Alarm Button
        stopAlarmButton = findViewById(R.id.stopAlarmButton);

        stopAlarmButton.setOnClickListener(v -> {
            if (alarmSound != null && alarmSound.isPlaying()) {
                alarmSound.stop();  // Stop the alarm sound
                stopAlarmButton.setVisibility(View.GONE);  // Hide Stop Alarm button
            }
        });

        // Initialize RecyclerView and Adapter
        recyclerView = findViewById(R.id.recylerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<SetModel> setList = new ArrayList<>();
        if (myCartModel != null) {
            Glide.with(getApplicationContext()).asGif().load(myCartModel.getImg_url()).into(detailedImg);
            name.setText(myCartModel.getWorkoutName());
            bodyPart.setText(myCartModel.getBodyPart());
            Time.setText(myCartModel.getWorkoutTime());
            cartNumberOfSets.setText(myCartModel.getNumberOfSets());
            cartNumberOfReps.setText(myCartModel.getNumberOfReps());

            if ("cardio".equalsIgnoreCase(myCartModel.getType())) {
                // Hide sets and reps, show the timer
                cartNumberOfSets.setVisibility(View.GONE);
                cartNumberOfReps.setVisibility(View.GONE);
                cartTextReps.setVisibility(View.GONE);
                cartTextSets.setVisibility(View.GONE);
                startTimerButton.setVisibility(View.VISIBLE);
                pauseTimerButton.setVisibility(View.GONE); // Initially hidden
                restartTimerButton.setVisibility(View.VISIBLE);
                timerText.setVisibility(View.VISIBLE);
                Time.setVisibility(View.VISIBLE);

                // Extract the workout time from the model (assume it's in format "HH:mm:ss")
                String workoutTime = myCartModel.getWorkoutTime(); // e.g., "00:05:00" for 5 minutes
                String[] timeParts = workoutTime.split(":");
                int hours = Integer.parseInt(timeParts[0]);
                int minutes = Integer.parseInt(timeParts[1]);
                int seconds = Integer.parseInt(timeParts[2]);

                // Convert the time to milliseconds
                long totalTimeInMillis = (hours * 3600 + minutes * 60 + seconds) * 1000;
                timeRemaining = totalTimeInMillis;

                // Set up the timer functionality
                startTimerButton.setOnClickListener(v -> {
                    startTimer(totalTimeInMillis);
                });

                pauseTimerButton.setOnClickListener(v -> {
                    pauseTimer();
                });

                restartTimerButton.setOnClickListener(v -> {
                    restartTimer(totalTimeInMillis);
                });
            } else {
                // Show sets and reps for non-cardio workouts
                cartNumberOfSets.setVisibility(View.VISIBLE);
                cartNumberOfReps.setVisibility(View.VISIBLE);
                startTimerButton.setVisibility(View.GONE);
                timerText.setVisibility(View.GONE);
                Time.setVisibility(View.GONE);

                // Extract sets information
                int numberOfSets = Integer.parseInt(myCartModel.getNumberOfSets());
                for (int i = 1; i <= numberOfSets; i++) {
                    String reps = myCartModel.getSetReps(i);
                    String weight = myCartModel.getSetWeight(i);
                    setList.add(new SetModel(i, Integer.parseInt(reps), Integer.parseInt(weight)));
                }
            }
        }

        cartSetsAdapter = new CartSetsAdapter(setList);
        recyclerView.setAdapter(cartSetsAdapter);
    }

    // Function to start the timer
    private void startTimer(long duration) {
        startTimerButton.setVisibility(View.GONE); // Hide start button
        pauseTimerButton.setVisibility(View.VISIBLE); // Show pause button

        if (timer != null) {
            timer.cancel(); // Cancel any ongoing timer
        }

        // Create a new timer based on the workout time or remaining time
        timer = new CountDownTimer(timeRemaining, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemaining = millisUntilFinished; // Update remaining time
                long hoursRemaining = (millisUntilFinished / 3600000) % 24;
                long minutesRemaining = (millisUntilFinished / 60000) % 60;
                long secondsRemaining = (millisUntilFinished / 1000) % 60;

                // Update the timer text with formatted remaining time
                timerText.setText(String.format("%02d:%02d:%02d", hoursRemaining, minutesRemaining, secondsRemaining));
            }

            @Override
            public void onFinish() {
                Time.setText("Workout complete!");
                pauseTimerButton.setVisibility(View.GONE); // Hide pause button when finished

                // Play the alarm sound when the workout is complete
                if (alarmSound != null) {
                    alarmSound.start();  // Start the alarm sound
                    stopAlarmButton.setVisibility(View.VISIBLE);  // Show the Stop Alarm button
                }
            }
        };

        timer.start();
    }

    // Function to pause the timer
    private void pauseTimer() {
        if (timer != null) {
            timer.cancel(); // Stop the timer
            startTimerButton.setVisibility(View.VISIBLE); // Show start button to resume
            pauseTimerButton.setVisibility(View.GONE); // Hide pause button
        }
    }

    // Function to restart the timer
    private void restartTimer(long duration) {
        timeRemaining = duration; // Reset to the full workout duration
        startTimer(duration); // Restart the timer with full duration
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel(); // Ensure the timer stops if the activity is destroyed
        }

        // Release MediaPlayer resources when done
        if (alarmSound != null) {
            alarmSound.release();
            alarmSound = null;
        }
    }
}