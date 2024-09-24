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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
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
    private LineChart weightChart;


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

        // Set text color for Y-axis
        weightChart.getAxisLeft().setTextColor(Color.WHITE);
        weightChart.getAxisRight().setTextColor(Color.WHITE);

        // Set text color for legend
        weightChart.getLegend().setTextColor(Color.WHITE);

        // Set text color for descriptions
        weightChart.getDescription().setTextColor(Color.WHITE);
        setupChart();

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser(); //getting the authorized user

        //error handling if user is not found, to login the user again
        if(currentUser == null){
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
                        /*updateChart(currentUserModel.getWeightHistory());*/
                        updateChart(currentUserModel.getWeightHistoryAsMap(), currentUserModel.getInitialWeight(), currentUserModel.getTargetWeight());

                        // Fetch and display the session count
                        fetchSessionCount();
                    }
                }
            }
        });

        FirebaseUtil.getCurrentProfilePicStorageRef().getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Uri uri  = task.getResult();
                        AndroidUtil.setProfilePic(this,uri,profilePic);
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

    private void fetchSessionCount() {
        String userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        FirebaseFirestore.getInstance().collection("Users")
                .document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        Long sessionCount = task.getResult().getLong("sessionCount");
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
    private void setupChart() {
        weightChart.getDescription().setEnabled(false);
        weightChart.setTouchEnabled(true);
        weightChart.setDragEnabled(true);
        weightChart.setScaleEnabled(true);
        weightChart.setDrawGridBackground(false);
        weightChart.setPinchZoom(true);

        XAxis xAxis = weightChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);  // Ensure granularity is enabled
        xAxis.setValueFormatter(new ValueFormatter() {
            private final SimpleDateFormat mFormat = new SimpleDateFormat("MM/dd/yy");

            @Override
            public String getFormattedValue(float value) {
                return mFormat.format(new Date((long) value));
            }
        });

        YAxis leftAxis = weightChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        weightChart.getAxisRight().setEnabled(false);
    }

    private void updateChart(List<Map<String, Object>> weightHistory, String initialWeight, String targetWeight) {
        if (weightHistory == null || weightHistory.isEmpty() || initialWeight == null || targetWeight == null) {
            // Clear existing data
            weightChart.clear();
            return;
        }

        List<Entry> entries = new ArrayList<>();

        // Add initial weight point
        entries.add(new Entry(0, Float.parseFloat(initialWeight)));

        // Add subsequent weight update points
        long timeOffset = 0;
        for (Map<String, Object> entry : weightHistory) {
            Timestamp timestamp = (Timestamp) entry.get("timestamp");
            String weight = (String) entry.get("weight");
            if (timestamp != null && weight != null) {
                entries.add(new Entry(timestamp.toDate().getTime() + timeOffset, Float.parseFloat(weight)));
                timeOffset += 1000; // Add a small time offset to prevent overlapping points
            }
        }

        LineDataSet dataSet = new LineDataSet(entries, "My Weight Progress Time");
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet.setLineWidth(2f);
        LineData lineData = new LineData(dataSet);

        weightChart.setData(lineData);

        // Add target weight line
        float targetWeightFloat = Float.parseFloat(targetWeight);
        LimitLine targetWeightLine = new LimitLine(targetWeightFloat, "Target Weight");
        targetWeightLine.setLineWidth(2f);
        targetWeightLine.setLineColor(getResources().getColor(R.color.red)); // graph color
        targetWeightLine.setTextSize(8f);

        YAxis leftAxis = weightChart.getAxisLeft();
        dataSet.setValueTextColor(Color.WHITE);
        leftAxis.removeAllLimitLines(); // Clear previous limit lines
        leftAxis.addLimitLine(targetWeightLine);

        weightChart.invalidate();
    }
}