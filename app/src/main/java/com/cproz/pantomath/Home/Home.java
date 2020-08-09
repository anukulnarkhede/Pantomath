package com.cproz.pantomath.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.cproz.pantomath.R;
import com.cproz.pantomath.Saved.SavedFragment;
import com.cproz.pantomath.Upload.UploadFragment;
import com.cproz.pantomath.Upload.UploadImagePage;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends AppCompatActivity {


    FrameLayout fragmentContainer;
    BottomNavigationView bottomNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        initialisation();

        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();



    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()){

                case R.id.home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.upload:
                    selectedFragment = new UploadFragment();
                    break;
                case R.id.saved:
                    selectedFragment = new SavedFragment();
                    break;


            }

            assert selectedFragment != null;
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

            return true;
        }
    };

    public void initialisation(){
        fragmentContainer = findViewById(R.id.fragment_container);
        bottomNav = findViewById(R.id.bottomNavStdAppStart);
    }

    public void onBackPressed() {

    }

    /*@Override
    protected void onResume() {
        Fragment selectedFragment;
        if (UploadImagePage.FLAG == 1) {

            selectedFragment = new UploadFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

        }

        super.onResume();
    }*/


}