package com.cproz.pantomath.Signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cproz.pantomath.R;

import java.util.Objects;
import java.util.regex.Pattern;

public class Password extends AppCompatActivity {


    Toolbar toolbar;
    Button next;
    EditText Password, ConfirmPassword;
    TextView errorText;
    final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");

    public static String PASSWORD = com.cproz.pantomath.Signup.Password.PASSWORD, CONFIRMPASSWORD = com.cproz.pantomath.Signup.Password.CONFIRMPASSWORD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_signup);

        initialization();

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);

        errorText.setVisibility(View.GONE);

        Password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Password.setBackgroundResource(R.drawable.text_view_bg);
                ConfirmPassword.setBackgroundResource(R.drawable.text_view_bg);
                errorText.setVisibility(View.GONE);
            }
        });

        ConfirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Password.setBackgroundResource(R.drawable.text_view_bg);
                ConfirmPassword.setBackgroundResource(R.drawable.text_view_bg);
                errorText.setVisibility(View.GONE);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validation();
                //startActivity(new Intent(Password.this, ProfilePictureSignup.class));
                //overridePendingTransition(10,10);
            }
        });


    }

    private void initialization() {
        toolbar = findViewById(R.id.PasswordToolBar);
        next = findViewById(R.id.NextPassword);
        errorText = findViewById(R.id.errorPassword);
        Password = findViewById(R.id.Password);
        ConfirmPassword = findViewById(R.id.ConfirmPassword);


    }

    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @SuppressLint("SetTextI18n")
    public void validation(){

        com.cproz.pantomath.Signup.Password.PASSWORD = Password.getText().toString();
        com.cproz.pantomath.Signup.Password.CONFIRMPASSWORD = ConfirmPassword.getText().toString();

        if (PASSWORD.isEmpty()&&CONFIRMPASSWORD.isEmpty()){
            errorText.setVisibility(View.VISIBLE);
            errorText.setText("All fields are required");
            Password.setBackgroundResource(R.drawable.error_text_field_bg);
            Password.requestFocus();
            ConfirmPassword.setBackgroundResource(R.drawable.error_text_field_bg);
        }

        else if (!PASSWORD_PATTERN.matcher(PASSWORD).matches()) {
            errorText.setVisibility(View.VISIBLE);
            errorText.setText("Password must contain at least 8 characters one capital letter and a special character");
            Password.setBackgroundResource(R.drawable.error_text_field_bg);
            Password.requestFocus();
            ConfirmPassword.setBackgroundResource(R.drawable.error_text_field_bg);
        }

        else if (PASSWORD.isEmpty()){
            errorText.setVisibility(View.VISIBLE);
            errorText.setText("Please enter the password");
            Password.setBackgroundResource(R.drawable.error_text_field_bg);
            Password.requestFocus();
            ConfirmPassword.setBackgroundResource(R.drawable.text_view_bg);
        }

        else if (CONFIRMPASSWORD.isEmpty()){
            errorText.setVisibility(View.VISIBLE);
            errorText.setText("Please confirm password");
            ConfirmPassword.setBackgroundResource(R.drawable.error_text_field_bg);
            ConfirmPassword.requestFocus();
            Password.setBackgroundResource(R.drawable.text_view_bg);
        }
        else if (!PASSWORD.equals(CONFIRMPASSWORD)){
            errorText.setVisibility(View.VISIBLE);
            errorText.setText("Password does not match");
            Password.setBackgroundResource(R.drawable.error_text_field_bg);
            ConfirmPassword.requestFocus();
            ConfirmPassword.setBackgroundResource(R.drawable.error_text_field_bg);
        }
        else
        {
            startActivity(new Intent(Password.this, ProfilePictureSignup.class));
            overridePendingTransition(10,10);
        }



    }
}