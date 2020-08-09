package com.cproz.pantomath.Login;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.cproz.pantomath.Signup.NewAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class Login extends AppCompatActivity {

    final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
    EditText Email, Password;
    TextView CreateAccount, ForgetPassword, ErrorText;
    String EmailString, PasswordString;
    Button Login, LoginWithGoogle;
    private FirebaseAuth firebaseAuth;
    ProgressBar progressBar;



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

                validation();
            }
        });

        CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, NewAccount.class) );
            }
        });






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
        progressBar.setProgress(100, true);

        EmailString = Email.getText().toString().trim();
        PasswordString = Password.getText().toString();

        if (EmailString.isEmpty()&&PasswordString.isEmpty()){
                ErrorText.setVisibility(View.VISIBLE);
                Email.setBackgroundResource(R.drawable.error_text_field_bg);
                Password.setBackgroundResource(R.drawable.error_text_field_bg);
                Email.requestFocus();

        }
        else if (EmailString.isEmpty()){
            ErrorText.setVisibility(View.VISIBLE);
            Email.setBackgroundResource(R.drawable.error_text_field_bg);
            Email.requestFocus();
            Password.setBackgroundResource(R.drawable.text_view_bg);
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(EmailString).matches()){
            ErrorText.setVisibility(View.VISIBLE);
            Password.setBackgroundResource(R.drawable.text_view_bg);
            ErrorText.setText("Make sure your email is correct.");
            Email.setBackgroundResource(R.drawable.error_text_field_bg);
            Email.requestFocus();

        }
        else if (PasswordString.isEmpty()){
            ErrorText.setVisibility(View.VISIBLE);
            ErrorText.setText("Please enter the password");
            Email.setBackgroundResource(R.drawable.text_view_bg);
            Password.setBackgroundResource(R.drawable.error_text_field_bg);
            Password.requestFocus();
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
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    startActivity(new Intent(Login.this, Home.class));//new intent for change activity

                }
                else{
                    Login.setEnabled(true);
                    Toast.makeText(Login.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();//toast for invalid entries;;
                }
            }
        });//login_Student_auth function end

    }

}