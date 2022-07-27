package com.cproz.pantomath.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cproz.pantomath.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ResetPassword extends AppCompatActivity {

    EditText resetEmail;
    Button submit;
    String email, Email;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);

        toolbar = findViewById(R.id.forgetPasswordToolBar);
        resetEmail = findViewById(R.id.resetEmail);
        submit = findViewById(R.id.reset_submit_butt);

        email = resetEmail.getText().toString();


        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow_rounded);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            Email = bundle.getString("Email");
            resetEmail.setText(Email);
        }



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resetEmail.getText().toString().isEmpty()){
                    resetEmail.requestFocus();
                    resetEmail.setError("To reset password, email is required");
                }else{
                    firebaseAuth.sendPasswordResetEmail(resetEmail.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            ForgetPopup forgetPopup = new ForgetPopup();
                            forgetPopup.show(getSupportFragmentManager(), "sws");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ResetPassword.this, "Failed to reset password", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


    }
}