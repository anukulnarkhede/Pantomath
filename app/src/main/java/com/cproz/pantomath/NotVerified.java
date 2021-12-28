package com.cproz.pantomath;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.mbms.MbmsErrors;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cproz.pantomath.Home.Home;
import com.cproz.pantomath.Signup.EmailVerificationPopUp;
import com.cproz.pantomath.Signup.PackageSelection;
import com.cproz.pantomath.Signup.RetryPopup;
import com.cproz.pantomath.Signup.VerificationPopup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class NotVerified extends AppCompatActivity {


    ImageView UnderVerification;
    Button requestAgain;
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    String email = user != null ? user.getEmail() : null;
    TextView VerificationText, UnpaidText, timer;

    @SuppressLint("SetTextI18n")
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
                requestAgain.setVisibility(View.GONE);
                UnderVerification.setImageResource(R.drawable.under);
                UnpaidText.setVisibility(View.GONE);
                VerificationText.setVisibility(View.VISIBLE);
            }else if (Objects.equals(bundle.getString("UserStatus"), "Suspended")){
                requestAgain.setVisibility(View.GONE);
                UnderVerification.setImageResource(R.drawable.suspended_vector);
                VerificationText.setVisibility(View.GONE);
                UnpaidText.setVisibility(View.VISIBLE);
                UnpaidText.setText("Please contact your Litmus Academy Branch Admin");
            }else if (Objects.equals(bundle.getString("UserStatus"), "Deleted")){
                //UnderVerification.setImageResource(R.drawable.suspended_vector);
                requestAgain.setText("Request Again");
                UnderVerification.setImageResource(R.drawable.request_denied);
                UnpaidText.setVisibility(View.GONE);
                VerificationText.setVisibility(View.GONE);
                requestAgain.setVisibility(View.VISIBLE);
                requestAgain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Date date = new Date();


                        requestAgain.setEnabled(false);
                        firebaseFirestore.collection("Users/Students/StudentsInfo/" )
                                .document(String.valueOf(email))
                                .update("User", "Not Verified", "SignupTime", date)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        startActivity(new Intent(NotVerified.this, Home.class));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {

                            @Override
                            public void onFailure(@NonNull Exception e) {
                                requestAgain.setEnabled(true);
                                Toast.makeText(NotVerified.this, "Request was unsuccessful!", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                });


            }else if(Objects.equals(bundle.getString("UserStatus"), "Unpaid")){
                UnderVerification.setVisibility(View.GONE);
                requestAgain.setVisibility(View.GONE);
                VerificationText.setVisibility(View.GONE);
                UnpaidText.setVisibility(View.VISIBLE);

            }



//        else {
//
//
//            UnderVerification.setImageResource(R.drawable.email_verification_png);
//            requestAgain.setVisibility(View.VISIBLE);
//            requestAgain.setText("Resend Verification Email");
//
//            requestAgain.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    requestAgain.setEnabled(false);
//                    firebaseAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            VerificationPopup verificationPopup = new VerificationPopup();
//                            verificationPopup.show(getSupportFragmentManager(), "asa");
//                            requestAgain.setEnabled(true);
//                            timer.setVisibility(View.VISIBLE);
//                            timer.setVisibility(View.VISIBLE);
//                            long duration = TimeUnit.MINUTES.toMillis(1);
//                            new CountDownTimer(duration, 1000) {
//                                @Override
//                                public void onTick(long millisUntilFinished) {
//                                    String sDuration = String.format(Locale.ENGLISH, "%02d : %02d",
//                                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished),
//                                            TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
//                                    requestAgain.setEnabled(false);
//                                    requestAgain.setTextColor(Color.parseColor("#999999"));
//                                    timer.setText("Resend verification email in " + sDuration);
//                                }
//
//                                @Override
//                                public void onFinish() {
//                                    requestAgain.setTextColor(Color.parseColor("#0476D9"));
//                                    timer.setVisibility(View.GONE);
//                                    requestAgain.setEnabled(true);
//                                }
//                            }.start();
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(NotVerified.this, "Request failed, Tap resend button", Toast.LENGTH_SHORT).show();
//                            requestAgain.setEnabled(true);
//                        }
//                    });
//                }
//            });
//        }











    }

    public void Initialization(){
        UnderVerification = findViewById(R.id.UnderVerification);
        requestAgain = findViewById(R.id.requestAgain);
        VerificationText = findViewById(R.id.VerificationText);
        UnpaidText = findViewById(R.id.UnpaidText);
        timer = findViewById(R.id.timer);
    }

    @Override
    public void onBackPressed() {

    }
}