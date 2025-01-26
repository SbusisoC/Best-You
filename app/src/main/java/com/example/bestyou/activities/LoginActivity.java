package com.example.bestyou.activities;

import static com.example.bestyou.utils.AndroidUtil.checkGooglePlayServices;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;

import com.example.bestyou.R;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    FirebaseAuth auth;
    CardView btnSignInWithGoogle;
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    String TAG = "LEO";
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // status bar color to black
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.black));

        // navigation bar color to black
        window.setNavigationBarColor(getResources().getColor(R.color.black));

        auth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnSignInWithGoogle = findViewById(R.id.btnSignInWithGoogleLeo);

        btnSignInWithGoogle.setOnClickListener(v -> {
            try {
                Log.d(TAG, "onCreate: click on google btn");
                signInUsingGoogle(LoginActivity.this);
            } catch (NoSuchAlgorithmException e) {
                Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });


       /* GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.btnSignInWithGoogle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });*/
    }

    public void signIn(View view) {

        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();

        if (TextUtils.isEmpty(userEmail)) {
            Toast.makeText(this, "Enter Email Address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(userPassword)) {
            Toast.makeText(this, "Enter Password!", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //instead call successfulLoggedIn();
                            Toast.makeText(LoginActivity.this, "Login Successful.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();


                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }

    public void register(View view) {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    private void signInUsingGoogle(Context context) throws NoSuchAlgorithmException {
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
            //handle cancellation if needed
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
                            Log.e("TAG", "Unexpected error");
                        }
                    } else {
                        Toast.makeText(context, "Sorry this way of logging in is not supported yet", Toast.LENGTH_LONG).show();
                    }


                }

                @Override
                public void onError(@NonNull GetCredentialException e) {
                    e.printStackTrace();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void successfulLoggedIn() {
        Toast.makeText(LoginActivity.this, "Login Successful.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}

   /* private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    String idToken = account.getIdToken();
                    firebaseAuthWithGoogle(idToken);
                } else {
                    Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show();
                }
            } catch (ApiException e) {
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}*/