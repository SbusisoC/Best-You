package com.example.bestyou.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.bestyou.R;
import com.example.bestyou.fragments.HomeFragment;
import com.example.bestyou.utils.FirebaseUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    Fragment homeFragment;
    FirebaseAuth auth;

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser == null){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the status bar color to black
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.black));

        // Set the navigation bar color to black
        window.setNavigationBarColor(getResources().getColor(R.color.black));

        //Get access
        auth = FirebaseAuth.getInstance();

        homeFragment = new HomeFragment();
        loadFragment(homeFragment);
        bottomNavigation();

        getFCMToken();
    }

    void getFCMToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    String token = task.getResult();
                    FirebaseUtil.currentUserDetails().update("fcmToken",token);

                }
        });
    }

    private void loadFragment(Fragment homeFragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.home_container, homeFragment);
        transaction.commit();
    }

    private void bottomNavigation(){
        /*LinearLayout homeBtn = findViewById(R.id.homeBtn);*/
        LinearLayout progressBtn = findViewById(R.id.progressBtn);
        LinearLayout notificationBtn = findViewById(R.id.notificationBtn);
        LinearLayout profileBtn = findViewById(R.id.profileBtn);

        /*homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        });*/

        progressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ProgressActivity.class));
            }
        });

        notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MessagesActivity.class));
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }
        });

    }
}