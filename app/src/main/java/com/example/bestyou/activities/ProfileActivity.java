package com.example.bestyou.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.bestyou.R;
import com.example.bestyou.models.UserModel;
import com.example.bestyou.utils.AndroidUtil;
import com.example.bestyou.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    FirebaseAuth auth;
    Button LogoutBtn, editProfile;

    ImageView profilePic;
    TextView username, userEmail;

    UserModel currentUserModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(view -> finish());

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
        /*userEmail = findViewById(R.id.userEmail);*/

        /*username.setText(currentUser.getDisplayName());*/

        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    currentUserModel = task.getResult().toObject(UserModel.class);
                    if (currentUserModel != null) {
                        username.setText(currentUserModel.getUsername());
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
    }
}