package com.example.bestyou.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.example.bestyou.R;
import com.example.bestyou.models.UserModel;
import com.example.bestyou.utils.AndroidUtil;
import com.example.bestyou.utils.FirebaseUtil;
import com.example.bestyou.models.WeightEntry;
import com.github.mikephil.charting.charts.LineChart;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    FirebaseAuth auth;
    Button LogoutBtn, editProfile, updateWeight;
    ImageView profilePic;
    TextView username, currentWeight, initialWeight;
    UserModel currentUserModel;
    CardView currentWeightCard, initialWeightCard;
    private LineChart weightChart;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(view -> finish());

        weightChart = findViewById(R.id.weightChart);
        setupChart();

        auth = FirebaseAuth.getInstance();
        LogoutBtn = findViewById(R.id.logoutBtn);
        FirebaseUser currentUser = auth.getCurrentUser(); //getting the authorized user

        //error handling if user is not found, to login the user again
        if(currentUser == null){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        LogoutBtn.setOnClickListener(view -> {
            //signing out
            FirebaseAuth.getInstance().signOut();
            //sending user to login
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            //starting the login activity
            startActivity(intent);
            //finish everything in the main activity
            finish();

        });

        editProfile = findViewById(R.id.profile_edit_btn);
        profilePic = findViewById(R.id.profilePic);
        username = findViewById(R.id.userName);
        currentWeight = findViewById(R.id.users_current_weight);
        initialWeight = findViewById(R.id.users_initial_weight);

        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    currentUserModel = task.getResult().toObject(UserModel.class);
                    if (currentUserModel != null) {
                        username.setText(currentUserModel.getUsername());
                        currentWeight.setText(currentUserModel.getCurrentWeight());
                        initialWeight.setText(currentUserModel.getInitialWeight());

                        // Update the chart with weight history
                        /*updateChart(currentUserModel.getWeightHistory());*/
                        updateChart(currentUserModel.getWeightHistoryAsMap(), currentUserModel.getInitialWeight());
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

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
                finish();
            }
        });

        updateWeight = findViewById(R.id.update_weight_btn);
        updateWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
                finish();
            }
        });

        currentWeightCard = findViewById(R.id.current_weight);
        initialWeightCard = findViewById(R.id.initial_weight);

        currentWeightCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
            }
        });

        initialWeightCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
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

    private void updateChart(List<Map<String, Object>> weightHistory, String initialWeight) {
        if (weightHistory == null || weightHistory.isEmpty() || initialWeight == null) {
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
        LineData lineData = new LineData(dataSet);

        weightChart.setData(lineData);
        weightChart.invalidate();
    }
}