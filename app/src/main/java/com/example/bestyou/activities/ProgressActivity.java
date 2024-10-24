package com.example.bestyou.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.bestyou.R;
import com.example.bestyou.models.UserModel;
import com.example.bestyou.utils.AndroidUtil;
import com.example.bestyou.utils.FirebaseUtil;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProgressActivity extends AppCompatActivity {

    Toolbar toolbar;
    FirebaseAuth auth;
    ImageView profilePic;
    TextView username, currentWeight, initialWeight, targetWeight, completedSessions;
    UserModel currentUserModel;
    CardView currentWeightCard, initialWeightCard;
    /*private LineChart weightChart;*/
    private BarChart weightChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        toolbar = findViewById(R.id.progress_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Set the back arrow color to white
        if (toolbar.getNavigationIcon() != null) {
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        // status bar color to black
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.black));

        // navigation bar color to black
        window.setNavigationBarColor(getResources().getColor(R.color.black));

        toolbar.setNavigationOnClickListener(view -> finish());

        weightChart = findViewById(R.id.weightChart);

        weightChart.getXAxis().setTextColor(Color.WHITE);
        weightChart.getAxisLeft().setTextColor(Color.WHITE);
        weightChart.getAxisRight().setTextColor(Color.WHITE);

        // Set text color for legend
        weightChart.getLegend().setTextColor(Color.WHITE);
        weightChart.getDescription().setTextColor(Color.WHITE);
        setupChart();

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser(); //getting the authorized user

        //error handling if user is not found, to login the user again
        if (currentUser == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        profilePic = findViewById(R.id.profilePic);
        username = findViewById(R.id.userName);
        currentWeight = findViewById(R.id.users_current_weight);
        initialWeight = findViewById(R.id.users_initial_weight);
        targetWeight = findViewById(R.id.users_target_weight);
        completedSessions = findViewById(R.id.users_completed_sessions);

        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    currentUserModel = task.getResult().toObject(UserModel.class);
                    if (currentUserModel != null) {
                        username.setText(currentUserModel.getUsername());
                        currentWeight.setText(currentUserModel.getCurrentWeight());
                        initialWeight.setText(currentUserModel.getInitialWeight());
                        targetWeight.setText(currentUserModel.getTargetWeight());

                        // Update the chart with weight history
                        updateChart(currentUserModel.getWeightHistoryAsMap(), currentUserModel.getTargetWeight(), currentUserModel.getInitialWeight());

                        // Fetch and display the session count
                        fetchSessionCountFromFirebase();
                    }
                }
            }
        });

        FirebaseUtil.getCurrentProfilePicStorageRef().getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Uri uri = task.getResult();
                        AndroidUtil.setProfilePic(this, uri, profilePic);
                    }
                });

        currentWeightCard = findViewById(R.id.current_weight);
        initialWeightCard = findViewById(R.id.initial_weight);

        currentWeightCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProgressActivity.this, EditProfileActivity.class));
            }
        });

        initialWeightCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProgressActivity.this, EditProfileActivity.class));
            }
        });

    }

    //getting number of sessions
    private void fetchSessionCountFromFirebase() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance().collection("Users")
                .document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        Long sessionCount = document.getLong("sessionCount");
                        if (sessionCount != null) {
                            completedSessions.setText(String.valueOf(sessionCount));
                        } else {
                            completedSessions.setText("0");
                        }
                    } else {
                        completedSessions.setText("0");
                    }
                });
    }

    private void updateChart(List<Map<String, Object>> weightHistory, String targetWeight, String initialWeight) {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        int index = 0;

        for (Map<String, Object> entry : weightHistory) {
            // Retrieve weight and convert to float
            String weightStr = (String) entry.get("weight");
            if (weightStr != null) {
                float weight = Float.parseFloat(weightStr);

                // Add entry to barEntries
                barEntries.add(new BarEntry(index, weight));
                index++;
            }
        }

        // Create BarDataSet with entries and label
        BarDataSet barDataSet = new BarDataSet(barEntries, "Weight Progress");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.WHITE); // Text color for values
        barDataSet.setValueTextSize(10f); // Text size for values

        // Set up BarData and attach to chart
        BarData barData = new BarData(barDataSet);
        weightChart.setFitBars(true); // Ensure the bars fit into the chart view
        weightChart.setData(barData);
        weightChart.getDescription().setText("Weight Progress Over Time");
        weightChart.animateY(2000); // Animate Y-axis

        // Add target weight line
        float targetWeightFloat = Float.parseFloat(targetWeight);
        LimitLine targetWeightLine = new LimitLine(targetWeightFloat, "Target");
        targetWeightLine.setLineWidth(2f);
        targetWeightLine.setLineColor(getResources().getColor(R.color.white)); // graph color
        targetWeightLine.setTextSize(10f);
        targetWeightLine.setTextColor(Color.WHITE);

        // Add initial weight line with dotted style
        float initialWeightFloat = Float.parseFloat(initialWeight);
        LimitLine initialWeightLine = new LimitLine(initialWeightFloat, "Initial");
        initialWeightLine.setLineWidth(2f);
        initialWeightLine.setLineColor(Color.LTGRAY); // Color for initial weight
        initialWeightLine.setTextSize(10f);
        initialWeightLine.setTextColor(Color.LTGRAY);
        initialWeightLine.enableDashedLine(10f, 10f, 0f); // Set dashed line style

        // Get the Y-axis to add the LimitLine
        YAxis leftAxis = weightChart.getAxisLeft();
        leftAxis.addLimitLine(targetWeightLine); // Add target weight line to the Y-axis
        leftAxis.addLimitLine(initialWeightLine); // Add initial weight line

        // Refresh the chart
        weightChart.invalidate();
    }

    private void setupChart() {
        weightChart.setDrawBarShadow(false);
        weightChart.setDrawValueAboveBar(true);
        weightChart.setMaxVisibleValueCount(60);
        weightChart.setPinchZoom(false);
        weightChart.setDrawGridBackground(false);
    }
}