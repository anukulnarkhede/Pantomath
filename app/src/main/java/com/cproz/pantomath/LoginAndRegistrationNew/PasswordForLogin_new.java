package com.cproz.pantomath.LoginAndRegistrationNew;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cproz.pantomath.Home.Home;
import com.cproz.pantomath.Login.Login;
import com.cproz.pantomath.Login.ResetPassword;
import com.cproz.pantomath.R;
import com.cproz.pantomath.Signup.PackageSelection;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class PasswordForLogin_new extends AppCompatActivity {

    TextView ForgotPassword, HeadText;
    String Email = "", PasswordStr, headTextStr;
    Toolbar toolbar;
    EditText Password;
    Button Continue;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_for_login_new);
        Initialisation();

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow_rounded);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null){
            Email = bundle.getString("Email");
            headTextStr = bundle.getString("HeadText");
            HeadText.setText(headTextStr);
        }





        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PasswordStr = Password.getText().toString();
                Login(Email, PasswordStr);
            }
        });




        ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PasswordForLogin_new.this, ResetPassword.class);
                intent.putExtra("Email", Email);
                startActivity(intent);
            }
        });
    }

    private void Login(String email, String passwordStr) {
        if (PasswordStr.isEmpty()){
            Password.setError("Please enter your Password.");
        }else{
            firebaseAuth.signInWithEmailAndPassword(email, passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        DocumentReference reference =
                                firebaseFirestore.collection("Users/Students/StudentsInfo/" )
                                        .document(Objects.requireNonNull(firebaseAuth.getCurrentUser().getEmail()));

                        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                String Class = documentSnapshot.getString("Class");
                                String Board = documentSnapshot.getString("Board");
                                String Institute = documentSnapshot.getString("Institute");
                                String Name = documentSnapshot.getString("Name");
                                String Number = documentSnapshot.getString("Number");


                                if (Objects.equals(Name, "") || Objects.equals(Number, "")){
                                    startActivity(new Intent(PasswordForLogin_new.this, AccountSetup.class));
                                }
                                else if (Objects.equals(Class, "") || Objects.equals(Board, "") || Objects.equals(Institute, "")){
                                    startActivity(new Intent(PasswordForLogin_new.this, AcademicInformation.class));
                                }
                                else {
                                    startActivity(new Intent(PasswordForLogin_new.this, Home.class));
                                }

                            }
                        });

                    }else{
                        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                        assert connectivityManager != null;
                        @SuppressLint("MissingPermission") Network networkInfo = connectivityManager.getActiveNetwork();
                        if (networkInfo == null){
                            Toast.makeText(PasswordForLogin_new.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(PasswordForLogin_new.this, "Invalid Credentials", Toast.LENGTH_SHORT).show(); //toast for invalid entries;;
                        }
                    }
                }
            });
        }
    }

    private void Initialisation() {
        ForgotPassword = findViewById(R.id.forgotPassword);
        HeadText = findViewById(R.id.HeadText);
        toolbar = findViewById(R.id.AccPassToolbar);
        Password = findViewById(R.id.Password);
        Continue = findViewById(R.id.Continue);
    }


}