package com.cproz.pantomath.Signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.cproz.pantomath.R;

import java.util.Objects;

public class NewAccount extends AppCompatActivity {



    Toolbar toolbar;
    Button nextButt;
    EditText Name, Email, LastName;
    TextView errorText;


    public static String NAME = NewAccount.NAME, EMAIL = NewAccount.EMAIL, LASTNAME = NewAccount.LASTNAME;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_account);

        initialization();

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        errorText.setVisibility(View.GONE);

        Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);


        Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Name.setBackgroundResource(R.drawable.text_view_bg);
                Email.setBackgroundResource(R.drawable.text_view_bg);
                errorText.setVisibility(View.GONE);
            }
        });

        Email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Name.setBackgroundResource(R.drawable.text_view_bg);
                Email.setBackgroundResource(R.drawable.text_view_bg);
                errorText.setVisibility(View.GONE);
            }
        });


        nextButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Validation();
                //startActivity(new Intent(NewAccount.this, Password.class));
                //overridePendingTransition(10,10);
            }
        });





    }

    private void initialization() {

        toolbar = findViewById(R.id.NewAccToolBar);
        nextButt = findViewById(R.id.NextNewAcc);
        errorText = findViewById(R.id.errorNewAcc);
        Email = findViewById(R.id.NewAccEmail);
        Name = findViewById(R.id.NewAccName);
        LastName = findViewById(R.id.NewAccLastName);


    }

    @SuppressLint("SetTextI18n")
    public void Validation(){

        NewAccount.NAME = Name.getText().toString() + " " + LastName.getText().toString();
        NewAccount.LASTNAME = LastName.getText().toString();
        NewAccount.EMAIL = toTitleCase(Email.getText().toString().toLowerCase().trim());

        if (NAME.isEmpty()&&EMAIL.isEmpty()&&LASTNAME.isEmpty()){

            errorText.setVisibility(View.VISIBLE);
            errorText.setText("All fields are required");
            Name.setBackgroundResource(R.drawable.error_text_field_bg);
            Name.requestFocus();
            Email.setBackgroundResource(R.drawable.error_text_field_bg);


        }
        else if (Name.getText().toString().isEmpty()){

            errorText.setVisibility(View.VISIBLE);
            errorText.setText("Please enter your name");
            Name.setBackgroundResource(R.drawable.error_text_field_bg);
            Name.requestFocus();
            Email.setBackgroundResource(R.drawable.text_view_bg);

        }
        else if (EMAIL.isEmpty()){
            errorText.setVisibility(View.VISIBLE);
            errorText.setText("Please enter your email");
            Email.setBackgroundResource(R.drawable.error_text_field_bg);
            Email.requestFocus();
            Name.setBackgroundResource(R.drawable.text_view_bg);
        }
        else if (LASTNAME.isEmpty()){
            errorText.setVisibility(View.VISIBLE);
            errorText.setText("Please enter your last name");
            Email.setBackgroundResource(R.drawable.error_text_field_bg);
            Email.requestFocus();
            Name.setBackgroundResource(R.drawable.text_view_bg);
        }

        else if (!Patterns.EMAIL_ADDRESS.matcher(EMAIL).matches()){
            errorText.setVisibility(View.VISIBLE);
            Name.setBackgroundResource(R.drawable.text_view_bg);
            errorText.setText("Make sure your email is correct.");
            Email.setBackgroundResource(R.drawable.error_text_field_bg);
            Email.requestFocus();

        }
        else
        {
            startActivity(new Intent(NewAccount.this, Password.class));
            overridePendingTransition(10,10);
        }

    }


    public static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder(input.length());
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }



}