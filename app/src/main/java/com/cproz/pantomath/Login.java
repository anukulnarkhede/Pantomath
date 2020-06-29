package com.cproz.pantomath;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class Login extends AppCompatActivity {

    final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
    EditText Email, Password;
    TextView CreateAccount, ForgetPassword, ErrorText;
    String EmailString, PasswordString;
    Button Login, LoginWithGoogle;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Initialization of variables
        Initialization();
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
            @Override
            public void onClick(View v) {
                validation();
            }
        });






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
    }
    public void validation(){



        EmailString = Email.getText().toString().trim();
        PasswordString = Password.getText().toString().trim();

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
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(EmailString).matches()){
            ErrorText.setVisibility(View.VISIBLE);
            ErrorText.setText("Invalid Format of email");
            Email.setBackgroundResource(R.drawable.error_text_field_bg);
            Email.requestFocus();

        }
        else if (PasswordString.isEmpty()){
            ErrorText.setVisibility(View.VISIBLE);
            Password.setBackgroundResource(R.drawable.error_text_field_bg);
            Password.requestFocus();
        }
        else
        {
            Authentication();

        }
        /*else if (!PASSWORD_PATTERN.matcher(PasswordString).matches()){
            ErrorText.setVisibility(View.VISIBLE);
            Password.setBackgroundResource(R.drawable.error_text_field_bg);
            Password.requestFocus();
        }*/








    }

    private void Authentication() {

    }

}