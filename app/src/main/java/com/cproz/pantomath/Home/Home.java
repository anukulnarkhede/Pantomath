package com.cproz.pantomath.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.cproz.pantomath.NotVerified;
import com.cproz.pantomath.R;
import com.cproz.pantomath.Notifications.NotificationsFragments;
import com.cproz.pantomath.Signup.PackageSelection;
import com.cproz.pantomath.SplashScreen.SplashScreen;
import com.cproz.pantomath.Upload.UploadFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.DateTime;

import java.util.Objects;

public class Home extends AppCompatActivity {


    FrameLayout fragmentContainer;
    BottomNavigationView bottomNav;
    final Fragment fragment1 = new HomeFragment();
    final Fragment fragment2 = new UploadFragment();
    final Fragment fragment3 = new NotificationsFragments();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;
    public static String BOARD = HomeFragment.BOARD, CLASS = HomeFragment.CLASS;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    String email = user != null ? user.getEmail() : null;
    private DocumentReference ref = firebaseFirestore.collection("Users/Students/StudentsInfo/" ).document(String.valueOf(email));
    String User;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        initialisation();

        bottomNav.setOnNavigationItemSelectedListener(navListener);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        assert firebaseUser != null;
        if(firebaseUser.isEmailVerified()){

            ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (Objects.equals(documentSnapshot.getString("Class"), "") || Objects.equals(documentSnapshot.getString("Board"), "")){
                        startActivity(new Intent(Home.this, PackageSelection.class));
                    }
                    else {
                        startActivity(new Intent(Home.this, Home.class));
                    }
                }
            });



        }

        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

            User = documentSnapshot.getString("User");

                assert User != null;
                if (User.equals("Not Verified")){
                    Intent intent = new Intent(Home.this, NotVerified.class);
                    intent.putExtra("UserStatus", "Not Verified");
                    startActivity(intent);
            }else if (User.equals("Verified")){

                startActivity(new Intent(Home.this, Home.class));
            }else if (User.equals("Blocked")){
                    Intent intent = new Intent(Home.this, NotVerified.class);
                    intent.putExtra("UserStatus", "Blocked");
                    startActivity(intent);
                }


            }
        });


        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();



        fm.beginTransaction().add(R.id.fragment_container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.fragment_container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.fragment_container,fragment1, "1").commit();









    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {


            switch (item.getItemId()){

                case R.id.home:
                    fm.beginTransaction().hide(active).show(fragment1).commit();
                    active = fragment1;
                    break;
                case R.id.upload:
                    fm.beginTransaction().hide(active).show(fragment2).commit();
                    active = fragment2;
                    break;
                case R.id.saved:
                    fm.beginTransaction().hide(active).show(fragment3).commit();
                    active = fragment3;
                    break;


            }




            return true;
        }
    };

    public void initialisation(){
        fragmentContainer = findViewById(R.id.fragment_container);
        bottomNav = findViewById(R.id.bottomNavStdAppStart);
    }

    public void onBackPressed() {

        /*if (active == fragment1){
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
        else if (active == fragment2){
            fm.beginTransaction().hide(active).show(fragment1).commit();
            bottomNav.getMenu().getItem(0).setChecked(true);
            active = fragment1;
        }else if (active == fragment3){
            fm.beginTransaction().hide(active).show(fragment2).commit();
            bottomNav.getMenu().getItem(1).setChecked(true);
            active = fragment2;
        }*/





        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavStdAppStart);
        int seletedItemId = bottomNavigationView.getSelectedItemId();
        if (R.id.home != seletedItemId) {
            setHomeItem(Home.this);
        }
        else if (active == fragment1){
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }

        else {
        super.onBackPressed();
    }





    }

    public static void setHomeItem(Activity activity) {
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                activity.findViewById(R.id.bottomNavStdAppStart);
        bottomNavigationView.setSelectedItemId(R.id.home);
    }




}