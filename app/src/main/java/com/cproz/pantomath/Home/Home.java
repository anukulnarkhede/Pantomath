package com.cproz.pantomath.Home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.cproz.pantomath.NotVerified;
import com.cproz.pantomath.R;
import com.cproz.pantomath.Notifications.NotificationsFragments;
import com.cproz.pantomath.Signup.PackageSelection;
import com.cproz.pantomath.SplashScreen.SplashScreen;
import com.cproz.pantomath.Upload.UploadFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
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

    private int REQUEST_CODE = 11;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        initialisation();

        bottomNav.setOnNavigationItemSelectedListener(navListener);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful()){
                    final String Token = Objects.requireNonNull(task.getResult()).getToken();

                    ref.update("Token", Token).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            System.out.println(Token);
                        }
                    });
                }
            }
        });


        // TODO : Add city, Branch, Academic Year


        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

            User = documentSnapshot.getString("User");


                assert User != null;
                switch (User) {

                    case "Active":

                        startActivity(new Intent(Home.this, Home.class));
                        assert firebaseUser != null;
                        if(firebaseUser.isEmailVerified()){

                            String Board = documentSnapshot.getString("Board");
                            String Class = documentSnapshot.getString("Class");

                            if (Objects.equals(Class, "") || Objects.equals(Board, "")){
                                startActivity(new Intent(Home.this, PackageSelection.class));
                            }
                            else{
                                startActivity(new Intent(Home.this, Home.class));
                            }

//                            ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                @Override
//                                public void onSuccess(DocumentSnapshot documentSnapshot) {
//
//                                    String Board = documentSnapshot.getString("Board");
//                                    String Class = documentSnapshot.getString("Class");
//
//                                    if (Objects.equals(Class, "") || Objects.equals(Board, "")){
//                                        startActivity(new Intent(Home.this, PackageSelection.class));
//                                    }
//                                    else {
//                                        startActivity(new Intent(Home.this, Home.class));
//                                    }
//                                }
//                            });



                        }
                        break;
                    case "Not Verified": {
                        Intent intent = new Intent(Home.this, NotVerified.class);
                        intent.putExtra("UserStatus", User);
                        startActivity(intent);
                        break;
                    }
                    case "Suspended": {
                        Intent intent = new Intent(Home.this, NotVerified.class);
                        intent.putExtra("UserStatus", User);
                        startActivity(intent);
                        break;
                    }
                    case "Deleted": {
                        Intent intent = new Intent(Home.this, NotVerified.class);
                        intent.putExtra("UserStatus", User);
                        startActivity(intent);
                        break;
                    }
                    case "Unpaid": {
                        Intent intent = new Intent(Home.this, NotVerified.class);
                        intent.putExtra("UserStatus", User);
                        startActivity(intent);
                        break;
                    }

                    case "":{
                        Intent intent = new Intent(Home.this, PackageSelection.class);
                        intent.putExtra("UserStatus", User);
                        startActivity(intent);
                        break;
                    }


                }


            }
        });


        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();



        fm.beginTransaction().add(R.id.fragment_container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.fragment_container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.fragment_container,fragment1, "1").commit();


        final AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(Home.this);
        com.google.android.play.core.tasks.Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(new com.google.android.play.core.tasks.OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {

                if (result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)){

                    try {
                        appUpdateManager.startUpdateFlowForResult(result, AppUpdateType.IMMEDIATE, Home.this, REQUEST_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }

                }

            }
        });





    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE){
            Toast.makeText(this, "Starting Download", Toast.LENGTH_SHORT).show();

            if (resultCode != RESULT_OK){
                Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show();
            }
        }


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