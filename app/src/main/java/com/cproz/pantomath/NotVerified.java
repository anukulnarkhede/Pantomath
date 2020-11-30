package com.cproz.pantomath;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.mbms.MbmsErrors;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cproz.pantomath.Home.Home;
import com.cproz.pantomath.Signup.PackageSelection;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.Objects;

public class NotVerified extends AppCompatActivity {


    ImageView UnderVerification;
    Button requestAgain;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    String email = user != null ? user.getEmail() : null;
    TextView VerificationText, UnpaidText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.not_verified);
        Initialization();
        final Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        requestAgain.setVisibility(View.GONE);
        VerificationText.setVisibility(View.GONE);
        UnpaidText.setVisibility(View.GONE);

        if (Objects.equals(bundle.getString("UserStatus"), "Not Verified")){
            UnderVerification.setImageResource(R.drawable.under);
            UnpaidText.setVisibility(View.GONE);
            VerificationText.setVisibility(View.VISIBLE);
        }else if (Objects.equals(bundle.getString("UserStatus"), "Suspended")){
            UnderVerification.setImageResource(R.drawable.suspended_vector);
            VerificationText.setVisibility(View.GONE);
            UnpaidText.setVisibility(View.GONE);
        }else if (Objects.equals(bundle.getString("UserStatus"), "Deleted")){
            //UnderVerification.setImageResource(R.drawable.suspended_vector);
            UnderVerification.setImageResource(R.drawable.request_denied);
            UnpaidText.setVisibility(View.GONE);
            VerificationText.setVisibility(View.GONE);
            requestAgain.setVisibility(View.VISIBLE);

        }else if(Objects.equals(bundle.getString("UserStatus"), "Unpaid")){
            UnderVerification.setVisibility(View.GONE);
            requestAgain.setVisibility(View.GONE);
            VerificationText.setVisibility(View.GONE);
            UnpaidText.setVisibility(View.VISIBLE);

        }


        requestAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();


                requestAgain.setEnabled(false);
//                firebaseFirestore.collection("Users/Students/StudentsInfo/" ).document(String.valueOf(email)).update("User", "Not Verified", "SignupTime", date).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        startActivity(new Intent(NotVerified.this, Home.class));
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        requestAgain.setEnabled(true);
//                        Toast.makeText(NotVerified.this, "Request was unsuccessful!", Toast.LENGTH_SHORT).show();
//                    }
//                });

                startActivity(new Intent(NotVerified.this, PackageSelection.class));
            }
        });






    }

    public void Initialization(){
        UnderVerification = findViewById(R.id.UnderVerification);
        requestAgain = findViewById(R.id.requestAgain);
        VerificationText = findViewById(R.id.VerificationText);
        UnpaidText = findViewById(R.id.UnpaidText);
    }

    @Override
    public void onBackPressed() {

    }
}