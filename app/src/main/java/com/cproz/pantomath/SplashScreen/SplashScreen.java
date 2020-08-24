package com.cproz.pantomath.SplashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cproz.pantomath.Home.Home;
import com.cproz.pantomath.Home.HomeFragment;
import com.cproz.pantomath.Login.Login;
import com.cproz.pantomath.R;
import com.cproz.pantomath.Signup.EmailVerificationPopUp;
import com.cproz.pantomath.Signup.PackageSelection;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SplashScreen extends AppCompatActivity {

    public static String BOARD = HomeFragment.BOARD, CLASS = HomeFragment.CLASS;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    String email = user != null ? user.getEmail() : null;
    private DocumentReference ref = firebaseFirestore.collection("Users/Students/StudentsInfo/" ).document(String.valueOf(email));

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null){

                if (user.isEmailVerified()){
                    startActivity(new Intent(this, Home.class));
                }else{
                    startActivity(new Intent(this, Login.class));

                }



        }
        else {
            startActivity(new Intent(this, Login.class));
        }

    }
}
