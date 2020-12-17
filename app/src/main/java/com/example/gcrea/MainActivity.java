package com.example.gcrea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity{

    private static final String TAGWORD = "Google Sign In Activity";
    private static final int REQUEST_SIGN_IN = 101;
    public static final String INPUT_EMAIL = "EMAIL";
    public static final String REQUEST_LOG_OUT = "LOGOUT";
    public static final int LOGOUT = 999;

    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;
    private boolean buttonPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonPressed = false;

        configureSignIn();

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
        TextView title = findViewById(R.id.appName);

        // Add green colour to the word "Green"
        String text = "Swinburne Sarawak<br/><font color=#056738>Green</font> Club";
        title.setText(Html.fromHtml(text));

        Button button = findViewById(R.id.signInButton);
        // Button on click to sign in
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getExtras() != null) {
                int request = intent.getExtras().getInt(REQUEST_LOG_OUT);
                if (request == 1) {
                    signOut();
                }
            }
        }
    }

    private void configureSignIn() {
        //Configure Google Sign In
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (buttonPressed) {
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            updateUIStatus(currentUser);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == REQUEST_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAGWORD, "firebaseAuthWithGoogle:" + account.getId());
                Authenticate(account.getIdToken());
                buttonPressed = true;
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAGWORD, "Google sign in failed", e);
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_LONG).show();
            }
        }
    }



    private void updateUIStatus(FirebaseUser firebaseUser) {
        String email = "";
        String userID = "";
        if (firebaseUser != null) {
            // Get email
            email = firebaseUser.getEmail();
            Intent intent = new Intent(this, MainMenu.class);
            intent.putExtra(INPUT_EMAIL, email);
            startActivity(intent);
        }
    }

    public void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQUEST_SIGN_IN);
    }

    public void signOut() {
        // Firebase sign out
        firebaseAuth.signOut();

        // Google sign out
        googleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUIStatus(null);
                    }
                });
    }

    private void Authenticate(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAGWORD, "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUIStatus(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAGWORD, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getParent().getApplicationContext(), "Authentication Failed.", Toast.LENGTH_LONG).show();
                            updateUIStatus(null);
                        }
                    }
                });
    }
}