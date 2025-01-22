package com.example.bestyou.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;

import com.example.bestyou.R;
import com.example.bestyou.models.UserModel;
import com.example.bestyou.utils.AndroidUtil;
import com.example.bestyou.utils.FirebaseUtil;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (FirebaseUtil.isLoggedIn() && getIntent().getExtras() != null) {
            //from notification pop up to chat
            String userId = getIntent().getExtras().getString("userId");
            if(userId != null && !userId.isEmpty()) {
                FirebaseUtil.allUserCollectionReference().document(userId).get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                //to open to the user that sent notification
                                UserModel model = task.getResult().toObject(UserModel.class);

                                Intent mainIntent = new Intent(this, MainActivity.class);
                                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(mainIntent);

                                Intent intent = new Intent(this, ChatActivity.class);
                                AndroidUtil.passUserModelAsIntent(intent, model);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

                            }else {

                                navigateToDefaultScreen();
                            }
                        }).addOnFailureListener(e -> {
                            // Handle errors
                            e.printStackTrace();
                            navigateToDefaultScreen();
                        });
            }else {
                // If userId is null, navigate to the default screen
                navigateToDefaultScreen();
            }
        } else {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (FirebaseUtil.isLoggedIn()) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this, RegisterActivity.class));
                    }
                    finish();

                }

            }, 1000);
        }
        StrictMode. ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private void navigateToDefaultScreen() {
        if (FirebaseUtil.isLoggedIn()) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, RegisterActivity.class));
        }
        finish();
    }
}