package com.example.bestyou.activities;

import static com.example.bestyou.utils.AndroidUtil.checkGooglePlayServices;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bestyou.R;
import com.example.bestyou.models.UserModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.UUID;

@SuppressWarnings("deprecation")
public class RegisterActivity extends AppCompatActivity {

    EditText name, email, password;
    FirebaseAuth auth;
    ProgressBar progressBar;
    String TAG = "LEO";

    SharedPreferences sharedPreferences;
    GoogleSignInClient googleSignInClient;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                try {
                    GoogleSignInAccount signInAccount = accountTask.getResult(ApiException.class);
                    AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
                    auth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                auth = FirebaseAuth.getInstance();
                                /*Glide.with(RegisterActivity.this).load(Objects.requireNonNull(auth.getCurrentUser()).getPhotoUrl()).into(imageView);*/
                                name.setText(auth.getCurrentUser().getDisplayName());
                                /*mail.setText(auth.getCurrentUser().getEmail());*/
                                Toast.makeText(RegisterActivity.this, "Signed in successfully!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Failed to sign in: " + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // status bar color to black
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.black));

        // navigation bar color to black
        window.setNavigationBarColor(getResources().getColor(R.color.black));

        auth = FirebaseAuth.getInstance();
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        /*trainerPassword = findViewById(R.id.trainer_password);*/

        //for first time boarding
        sharedPreferences = getSharedPreferences("BoardingScreen", MODE_PRIVATE);

        boolean isFirstTime = sharedPreferences.getBoolean("firstTime",true);
        if(isFirstTime) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("firstTime", false);
            editor.commit();

            /*Intent intent = new Intent(RegisterActivity.this, BoardingActivity.class);
            startActivity(intent);
            finish();*/
        }
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(RegisterActivity.this, options);

        auth = FirebaseAuth.getInstance();

        CardView signInButton = findViewById(R.id.btnSignInWithGoogle);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    registerUsingGoogle(RegisterActivity.this);
                } catch (NoSuchAlgorithmException e) {
                    Toast.makeText(RegisterActivity.this,"Error "+e.getMessage(),Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    public void signUp(View view) {

        String userName = name.getText().toString();
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();
        /*String userTrainerPassword = trainerPassword.getText().toString();*/

        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(this, "Enter Name!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(userEmail)) {
            Toast.makeText(this, "Enter Email Address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(userPassword)) {
            Toast.makeText(this, "Create Password!", Toast.LENGTH_SHORT).show();
            return;
        }
       /* if (TextUtils.isEmpty(userTrainerPassword)) {
            Toast.makeText(this, "Create Password For Your Trainer", Toast.LENGTH_SHORT).show();
            return;
        }*/

        if (userPassword.length() < 6) {
            Toast.makeText(this, "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        /*progressBar.setVisibility(View.GONE);*/
                        if (task.isSuccessful()) {
                            String userName = name.getText().toString();

                            // Update the user's profile with the provided name
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(userName)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // Save additional user data in Firestore
                                                saveUserInFirestore(user, userName);
                                            } else {
                                                Toast.makeText(RegisterActivity.this, "Profile update failed.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "Authentication failed. Try again.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }

    //todo implement same for register using google
    private void saveUserInFirestore(FirebaseUser user, String userName) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        UserModel userModel = new UserModel();
        userModel.setUsername(userName);
        userModel.setUserId(user.getUid());

        db.collection("users").document(user.getUid())
                .set(userModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Account created.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Firestore update failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signIn(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }


    private void registerUsingGoogle(Context context) throws NoSuchAlgorithmException {
        if (!checkGooglePlayServices(context)) return;
        CredentialManager credentialManager = CredentialManager.create(context);
        String rawNonce = UUID.randomUUID().toString();
        byte[] bytes = rawNonce.getBytes(StandardCharsets.UTF_8);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(bytes);
        StringBuilder hashedNonce = new StringBuilder();
        for (byte b : digest) {
            hashedNonce.append(String.format("%02x", b));
        }
        GetSignInWithGoogleOption getSignInWithGoogleOption = new GetSignInWithGoogleOption.Builder(context.getString(R.string.client_id))
                .setNonce(String.valueOf(hashedNonce))
                .build();
        GetCredentialRequest googleSignRequest = new GetCredentialRequest.Builder()
                .addCredentialOption(getSignInWithGoogleOption)
                .build();
        CancellationSignal cancellationSignal = new CancellationSignal();
        cancellationSignal.setOnCancelListener(() -> {
            Log.d(TAG, "signInUsingGoogle: cancelling logging in");
        });
        try {
            credentialManager.getCredentialAsync(context, googleSignRequest, cancellationSignal,Runnable::run,new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                @Override
                public void onResult(GetCredentialResponse getCredentialResponse) {
                    Log.d(TAG, "onResult: on result credential manager");
                    Credential credential = getCredentialResponse.getCredential();
                    // GoogleIdToken credential
                    if (credential.getType() == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        Log.d(TAG, "onResult: inside cred type google");
                        try {
                            GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential
                                    .createFrom(credential.getData());
                            String googleIdToken = googleIdTokenCredential.getIdToken();
                            AuthCredential authCredential = GoogleAuthProvider.getCredential(googleIdToken, null);
                            auth.signInWithCredential(authCredential).addOnSuccessListener(authResult -> {
                                Log.d(TAG, "onResult: sign with firebase success");
                                successfulLoggedIn();
                            }).addOnFailureListener(e -> {
                                Toast.makeText(context,"Error "+e.getMessage(),Toast.LENGTH_LONG).show();
                            });
                        } catch (Exception e) {
                            Toast.makeText(context,"Error "+e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context, "Sorry this way of logging in is not supported yet", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onError(@NonNull GetCredentialException e) {
                    Toast.makeText(context,"Error "+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(context,"Error "+e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void successfulLoggedIn() {
        Toast.makeText(RegisterActivity.this, "Login Successful.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}