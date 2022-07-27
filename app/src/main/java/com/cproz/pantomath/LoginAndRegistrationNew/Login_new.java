package com.cproz.pantomath.LoginAndRegistrationNew;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cproz.pantomath.Home.Home;
import com.cproz.pantomath.R;
import com.cproz.pantomath.Signup.SignupInfoCarrier;
import com.cproz.pantomath.SplashScreen.SplashScreen;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.Date;
import java.util.Objects;
import java.util.regex.Pattern;

public class Login_new extends AppCompatActivity {

    private static final int RC_SIGN_IN = 12;
    private static final String TAG = "";
    EditText LoginEmail;
    Button LoginContinue, SignInWithGoogle, progressBarLogin;
    String email;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    GoogleSignInClient googleSignInClient;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_new);
        Initialisation();

        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        assert connectivityManager != null;
        @SuppressLint("MissingPermission") Network networkInfo = connectivityManager.getActiveNetwork();
        if (networkInfo == null){
            Toast.makeText(Login_new.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        LoginContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = LoginEmail.getText().toString();
                validation();
            }
        });

        createRequest();

        SignInWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarLogin.setVisibility(View.VISIBLE);
                signIn();
            }
        });



    }

    private void createRequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    private void Initialisation() {
        LoginEmail = findViewById(R.id.NewLoginEmail);
        LoginContinue = findViewById(R.id.continue_home);
        SignInWithGoogle = findViewById(R.id.google_signup);
        progressBarLogin = findViewById(R.id.progressBarLogin);
    }

    public void validation(){
        if (email.isEmpty()){
            LoginEmail.setError("Enter your email to continue.");
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            LoginEmail.setError("Enter a valid email to continue.");
        }
        else {
            progressBarLogin.setVisibility(View.VISIBLE);
            checkEmail();
        }
    }

    public void checkEmail(){
        auth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        boolean check = !Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getSignInMethods()).isEmpty();

                        final Intent intentToPass;
                        if (!check){
                            intentToPass = new Intent(Login_new.this, CreateAccPassword.class);
                            intentToPass.putExtra("Email", email);
                            startActivity(intentToPass);
                            progressBarLogin.setVisibility(View.GONE);

                        }
                        else{

                            intentToPass = new Intent(Login_new.this, PasswordForLogin_new.class);
                            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                            firebaseFirestore.collection("Users/Students/StudentsInfo/")
                                    .document(email)
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @SuppressLint("SetTextI18n")
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            String[] newStr = Objects.requireNonNull(documentSnapshot.getString("Name")).split("\\s+");
                                            if (newStr.length > 1){
                                                String HeadText = "Welcome back, " + newStr[0];
                                                intentToPass.putExtra("HeadText", HeadText);
                                                intentToPass.putExtra("Email", email);
                                                startActivity(intentToPass);
                                                progressBarLogin.setVisibility(View.GONE);
                                            }

                                        }
                                    });


                        }




                    }
                }).addOnFailureListener(new OnFailureListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onFailure(@NonNull Exception e) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                assert connectivityManager != null;
                @SuppressLint("MissingPermission") Network networkInfo = connectivityManager.getActiveNetwork();
                if (networkInfo == null){
                    Toast.makeText(Login_new.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
                progressBarLogin.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (task.isSuccessful()){
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                    firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    Log.w(TAG, "Google sign in failed", e);
                }
            }else{
                progressBarLogin.setVisibility(View.GONE);
            }

        }
    }


    private void firebaseAuthWithGoogle(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //google signin done
                            final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(Login_new.this);
                            FirebaseUser user = auth.getCurrentUser();
                            assert user != null;
                            DocumentReference reference = firebaseFirestore.collection("Users/Students/StudentsInfo/")
                                    .document(Objects.requireNonNull(user.getEmail()));

                            reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @RequiresApi(api = Build.VERSION_CODES.M)
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            //if user document exists
                                            if (Objects.equals(document.getString("Name"), "")
                                                    || Objects.equals(document.getString("Number"), "")
                                                    || Objects.equals(document.getString("Class"), "")
                                                    || Objects.equals(document.getString("Board"), "")
                                                    || Objects.equals(document.getString("Address"), "")
                                                    || Objects.equals(document.getString("Institute"), "")){
                                                Intent intent = new Intent(Login_new.this, AccountSetup.class);
                                                assert acct != null;
                                                intent.putExtra("FirstName", acct.getGivenName());
                                                intent.putExtra("LastName", acct.getFamilyName());
                                                startActivity(intent);
                                            }else{
                                                startActivity(new Intent(Login_new.this, Home.class));
                                            }

                                        } else {

                                            //if user documents does not exists;
                                            final Date SignupTime = new Date();
                                            String Email = Objects.requireNonNull(auth.getCurrentUser()).getEmail();

                                            assert acct != null;
                                            String photoUrl = "";
                                            if (acct.getPhotoUrl() != null){
                                                photoUrl = Objects.requireNonNull(acct.getPhotoUrl()).toString();
                                            }
                                            SignupInfoCarrier signupInfoCarrier = new SignupInfoCarrier(
                                                    "",
                                                    Email,
                                                    "",
                                                    "",
                                                    "Not Verified",
                                                    "",
                                                    "",
                                                    Email ,
                                                    photoUrl,
                                                    SignupTime,
                                                    0,
                                                    "",
                                                    "",
                                                    "",
                                                    ""

                                            );
                                            //create user document
                                            firebaseFirestore.document("Users/Students/StudentsInfo/" + Email )
                                                    .set(signupInfoCarrier)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {


                                                            progressBarLogin.setVisibility(View.GONE);
                                                            startActivity(new Intent(Login_new.this, AccountSetup.class));
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            progressBarLogin.setVisibility(View.GONE);
                                                            Toast.makeText(Login_new.this, "Signup Failed", Toast.LENGTH_SHORT).show();
//                                            System.out.println("Document upload failed");
//                                            UpdateInfo.setEnabled(true);
                                                        }
                                                    });
                                        }
                                    } else {
                                        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                                        assert connectivityManager != null;
                                        @SuppressLint("MissingPermission") Network networkInfo = connectivityManager.getActiveNetwork();
                                        if (networkInfo == null){
                                            Toast.makeText(Login_new.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });


                        } else {



                            ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                            assert connectivityManager != null;
                            @SuppressLint("MissingPermission") Network networkInfo = connectivityManager.getActiveNetwork();
                            if (networkInfo == null){
                                Toast.makeText(Login_new.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(Login_new.this, "Google signIn failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }


    @Override
    public void onBackPressed() {

    }
}