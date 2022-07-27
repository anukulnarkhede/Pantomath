package com.cproz.pantomath.LoginAndRegistrationNew;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.fonts.FontFamily;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cproz.pantomath.AdditionalInfo.AdditionalInfo;
import com.cproz.pantomath.R;
import com.cproz.pantomath.Signup.EmailVerificationPopUp;
import com.cproz.pantomath.Signup.NewAccount;
import com.cproz.pantomath.Signup.RetryPopup;
import com.cproz.pantomath.Signup.SignupInfoCarrier;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.Objects;

public class CreateAccPassword extends AppCompatActivity {

    Toolbar toolbar;
    TextView TermsAndPrivacy;
    Button CreateAccButt, progressBar;
    String Password, ConfirmPassword, Email;
    EditText password, confirmPassword;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_acc_password);
        initialisation();
        final Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            Email = bundle.getString("Email");
        }

        //-----------------------------------------------------------------------------------------------------------------------------------------
        //toolbar

        progressBar.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow_rounded);

        //-----------------------------------------------------------------------------------------------------------------------------------------
        //termsText

        String termsAndPrivacyText = "By creating an account, you are agreeing to our\nTerms of service and Privacy policy.";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            TermsAndPrivacy.setLineHeight(54);
        }

        SpannableString spannableString = new SpannableString(termsAndPrivacyText);

        ClickableSpan clickableSpan1 = new ClickableSpan() {

            public void updateDrawState(TextPaint ds) {
                ds.setColor(ds.linkColor);    // you can use custom color
                ds.setUnderlineText(false);    // this remove the underline
            }

            @Override
            public void onClick(@NonNull View view) {
                Uri uri = Uri.parse("http://www.google.com");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        };

        ClickableSpan clickableSpan2 = new ClickableSpan() {

            public void updateDrawState(TextPaint ds) {
                ds.setColor(ds.linkColor);    // you can use custom color
                ds.setUnderlineText(false);    // this remove the underline
            }

            @Override
            public void onClick(@NonNull View view) {
                Uri uri = Uri.parse("http://www.google.com");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        };

        ForegroundColorSpan fcBlue = new ForegroundColorSpan(Color.parseColor("#006AFF"));
        ForegroundColorSpan fcBlueX = new ForegroundColorSpan(Color.parseColor("#006AFF"));
        spannableString.setSpan(fcBlue, 48, 65, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(fcBlueX, 69, 83, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(clickableSpan1, 48, 65, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(clickableSpan2, 69, 83, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        TermsAndPrivacy.setText(spannableString);
        TermsAndPrivacy.setMovementMethod(LinkMovementMethod.getInstance());

        //-----------------------------------------------------------------------------------------------------------------------------------------
        //create account button

        CreateAccButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                String Passwd = password.getText().toString();
//                Intent intent = new Intent(CreateAccPassword.this, AccountSetup.class);
//                intent.putExtra("Password",Passwd);
//                intent.putExtra("Email", Email);
//                startActivity(intent);

                Password = password.getText().toString();
                ConfirmPassword = confirmPassword.getText().toString();
                validation(Email, Password, ConfirmPassword);
            }
        });


    }

    private void validation(String email, String pswd, String confirmPswd) {

        if (pswd.isEmpty() && confirmPswd.isEmpty()){
            password.setError("All fields are compulsory");
        } else if (pswd.isEmpty()){
            password.setError("Password can not be empty");
            password.requestFocus();
        }else if (confirmPswd.isEmpty()){
            confirmPassword.requestFocus();
            confirmPassword.setError("Confirm your password");
        }else if (!pswd.equals(confirmPswd)){
            confirmPassword.requestFocus();
            confirmPassword.setError("Your password does not match");
        }else if (pswd.length() < 8){
            confirmPassword.requestFocus();
            confirmPassword.setError("Your password must contain at least 8 characters.");
        }
        else {
            progressBar.setVisibility(View.VISIBLE);
            signup(email, pswd);
        }
    }

    private void signup(final String email, String pswd) {

        firebaseAuth.createUserWithEmailAndPassword(email, pswd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){







                            final Date SignupTime = new Date();
                            String Email = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();

                            SignupInfoCarrier signupInfoCarrier = new SignupInfoCarrier(
                                    "",
                                    Email,
                                    "",
                                    "",
                                    "Not Verified",
                                    "",
                                    "",
                                    Email ,
                                    "",
                                    SignupTime,
                                    0,
                                    "",
                                    "",
                                    "",
                                    ""

                            );
                            firebaseFirestore.document("Users/Students/StudentsInfo/" + Email )
                                    .set(signupInfoCarrier)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            FirebaseUser user = firebaseAuth.getCurrentUser();
                                            verification(user);
                                            progressBar.setVisibility(View.GONE);
                                            //startActivity(new Intent(ProfilePictureSignup.this, Home.class));
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(CreateAccPassword.this, "Signup Failed", Toast.LENGTH_SHORT).show();
//                                            System.out.println("Document upload failed");
//                                            UpdateInfo.setEnabled(true);
                                        }
                                    });



                        }
                        else {
                            Toast.makeText(CreateAccPassword.this, "Signup Failed", Toast.LENGTH_SHORT).show();
                            System.out.println("password login failed");
//                            UpdateInfo.setEnabled(true);
                              progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }


    public void initialisation(){
        toolbar = findViewById(R.id.AccPassToolbar);
        TermsAndPrivacy = findViewById(R.id.TermsAndPrivacy);
        CreateAccButt = findViewById(R.id.CreateAccButt);
        password = findViewById(R.id.Password);
        confirmPassword = findViewById(R.id.confirmPassword);
        progressBar = findViewById(R.id.progressBar);
    }

    public void verification(FirebaseUser user){
        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startActivity(new Intent(CreateAccPassword.this, AccountSetup.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                RetryPopup retryPopup = new RetryPopup();
                retryPopup.show(getSupportFragmentManager(), "axa");
            }
        });
    }


}