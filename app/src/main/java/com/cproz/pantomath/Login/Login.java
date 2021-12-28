package com.cproz.pantomath.Login;

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
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.cproz.pantomath.Home.Home;
import com.cproz.pantomath.R;
import com.cproz.pantomath.Signup.EmailVerificationPopUp;
import com.cproz.pantomath.Signup.NewAccount;
import com.cproz.pantomath.Signup.PackageSelection;
import com.cproz.pantomath.Signup.ProfilePictureSignup;
import com.cproz.pantomath.Signup.RetryPopup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {
    EditText Email, Password;
    TextView CreateAccount, ForgetPassword, ErrorText;
    String EmailString, PasswordString;
    Button Login, LoginWithGoogle;
    private FirebaseAuth firebaseAuth;
    Button progressBar;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


    String Class , Board ;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);




        //Initialization of variables
        Initialization();

        progressBar.setVisibility(View.GONE);
        firebaseAuth = FirebaseAuth.getInstance();
        //Initialization of variables
        Login.setEnabled(true);
        ErrorText.setVisibility(View.GONE);


        ForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ForgetPasswordPopup forgetPasswordPopup = new ForgetPasswordPopup();
                //forgetPasswordPopup.show(getSupportFragmentManager(), "awa");
                startActivity(new Intent(Login.this, ResetPassword.class));
            }
        });

        Email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ErrorText.setVisibility(View.GONE);
                Email.setBackgroundResource(R.drawable.text_view_bg);
                Password.setBackgroundResource(R.drawable.text_view_bg);
            }
        });

        Password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ErrorText.setVisibility(View.GONE);
                Email.setBackgroundResource(R.drawable.text_view_bg);
                Password.setBackgroundResource(R.drawable.text_view_bg);
            }
        });


        Login.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                Login.setEnabled(false);
                validation();
            }
        });

        CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, NewAccount.class) );
            }
        });



        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

            assert connectivityManager != null;
            Network networkInfo = connectivityManager.getActiveNetwork();
            if (networkInfo == null){
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            System.out.println(e);
        }



    }

    @Override
    public void onBackPressed() {
        
    }

    //Initialization of variables
    public void Initialization(){
        Email = findViewById(R.id.loginEmail);
        Password = findViewById(R.id.loginPassword);
        CreateAccount = findViewById(R.id.CreatAccLoginText);
        ForgetPassword = findViewById(R.id.forgetPassword);
        Login = findViewById(R.id.LoginButt);
        LoginWithGoogle = findViewById(R.id.googleLogin_butt);
        ErrorText = findViewById(R.id.errorTextLogin);
       progressBar = findViewById(R.id.progressBarLogin);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    public void validation(){



       progressBar.setVisibility(View.VISIBLE);
//        progressBar.setProgress(100, true);

        EmailString = Email.getText().toString().trim();
        PasswordString = Password.getText().toString();

        if (EmailString.isEmpty()&&PasswordString.isEmpty()){
                ErrorText.setVisibility(View.VISIBLE);
                Email.setBackgroundResource(R.drawable.error_text_field_bg);
            progressBar.setVisibility(View.GONE);
                Password.setBackgroundResource(R.drawable.error_text_field_bg);
                Email.requestFocus();
                Login.setEnabled(true);

        }
        else if (EmailString.isEmpty()){
            ErrorText.setVisibility(View.VISIBLE);
            Email.setBackgroundResource(R.drawable.error_text_field_bg);
           progressBar.setVisibility(View.GONE);
            Email.requestFocus();
            Password.setBackgroundResource(R.drawable.text_view_bg);
            Login.setEnabled(true);
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(EmailString).matches()){
            ErrorText.setVisibility(View.VISIBLE);
            Password.setBackgroundResource(R.drawable.text_view_bg);
            ErrorText.setText("Make sure your email is correct.");
           progressBar.setVisibility(View.GONE);
            Email.setBackgroundResource(R.drawable.error_text_field_bg);
            Email.requestFocus();
            Login.setEnabled(true);

        }
        else if (PasswordString.isEmpty()){
            ErrorText.setVisibility(View.VISIBLE);
            ErrorText.setText("Please enter the password");
           progressBar.setVisibility(View.GONE);
            Email.setBackgroundResource(R.drawable.text_view_bg);
            Password.setBackgroundResource(R.drawable.error_text_field_bg);
            Password.requestFocus();
            Login.setEnabled(true);
        }
        else
        {
            Login.setEnabled(false);
            Authentication(EmailString, PasswordString);

        }
        /*else if (!PASSWORD_PATTERN.matcher(PasswordString).matches()){
            ErrorText.setVisibility(View.VISIBLE);
            Password.setBackgroundResource(R.drawable.error_text_field_bg);
            Password.requestFocus();
        }*/








    }

    private void Authentication(String Email, String Password) {






        firebaseAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    isEmailVerifiedx();

                   // startActivity(new Intent(Login.this, Home.class));//new intent for change activity

                }
                else{
                    Login.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                    //Toast.makeText(Login.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();//toast for invalid entries;;
                    ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                    assert connectivityManager != null;
                    Network networkInfo = connectivityManager.getActiveNetwork();
                    if (networkInfo == null){
                        Toast.makeText(Login.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(Login.this, "Invalid Credentials", Toast.LENGTH_SHORT).show(); //toast for invalid entries;;
                    }



                }
            }
        });//login_Student_auth function end

    }

    public void isEmailVerifiedx(){
        FirebaseUser user = firebaseAuth.getCurrentUser();

        String email = user.getEmail();
        DocumentReference ref = firebaseFirestore.collection("Users/Students/StudentsInfo/" ).document(String.valueOf(email));

        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Class = documentSnapshot.getString("Class");
                Board = documentSnapshot.getString("Board");


                if (Class.equals("") || Objects.equals(Board, "")){
                    startActivity(new Intent(Login.this, PackageSelection.class));
                }
                else {
                    startActivity(new Intent(Login.this, Home.class));
                }

            }
        });



//        assert user != null;
//        if (user.isEmailVerified()){
//
//
//            String email = user.getEmail();
//            DocumentReference ref = firebaseFirestore.collection("Users/Students/StudentsInfo/" ).document(String.valueOf(email));
//
//            ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                @Override
//                public void onSuccess(DocumentSnapshot documentSnapshot) {
//                    Class = documentSnapshot.getString("Class");
//                    Board = documentSnapshot.getString("Board");
//
//
//                    if (Class.equals("") || Objects.equals(Board, "")){
//                        startActivity(new Intent(Login.this, PackageSelection.class));
//                    }
//                    else {
//                        startActivity(new Intent(Login.this, Home.class));
//                    }
//
//                }
//            });
//            //startActivity(new Intent(Login.this, Home.class));//new intent for change activity
//        }
//        else{
//            user = FirebaseAuth.getInstance().getCurrentUser();
//            assert user != null;
//            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void aVoid) {
//                    EmailVerificationPopUp emailVerificationPopUp = new EmailVerificationPopUp();
//                    emailVerificationPopUp.show(getSupportFragmentManager(), "asa");
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Login.setEnabled(true);
//                    progressBar.setEnabled(false);
//                    Toast.makeText(Login.this, "Please try again", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//        }
    }

}