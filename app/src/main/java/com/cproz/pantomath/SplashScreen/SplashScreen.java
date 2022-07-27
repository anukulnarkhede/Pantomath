package com.cproz.pantomath.SplashScreen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.cproz.pantomath.Home.Home;
import com.cproz.pantomath.Home.HomeFragment;
import com.cproz.pantomath.Login.Login;
import com.cproz.pantomath.LoginAndRegistrationNew.AcademicInformation;
import com.cproz.pantomath.LoginAndRegistrationNew.AccountSetup;
import com.cproz.pantomath.LoginAndRegistrationNew.Login_new;
import com.cproz.pantomath.LoginAndRegistrationNew.PasswordForLogin_new;
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        if (firebaseUser != null){

            ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

            assert connectivityManager != null;
            @SuppressLint("MissingPermission") Network networkInfo = connectivityManager.getActiveNetwork();
            if (networkInfo == null){
                Toast.makeText(SplashScreen.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }

            firebaseFirestore.collection("Users/Students/StudentsInfo/" )
                    .document(Objects.requireNonNull(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail()))
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {



                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String Class = documentSnapshot.getString("Class");
                            String Board = documentSnapshot.getString("Board");
                            String Institute = documentSnapshot.getString("Institute");
                            String Name = documentSnapshot.getString("Name");
                            String Number = documentSnapshot.getString("Number");


                            if (Objects.equals(Name, "") ||
                                    Objects.equals(Number, "") ||
                                    Objects.equals(Class, "") ||
                                    Objects.equals(Board, "") ||
                                    Objects.equals(Institute, "")){
                                startActivity(new Intent(SplashScreen.this, AccountSetup.class));
                            }
                            else {
                                startActivity(new Intent(SplashScreen.this, Home.class));
                            }
                        }
                    });





        }
        else {
            startActivity(new Intent(this, Login_new.class));
        }

    }

}
