package com.cproz.pantomath.StudentProfile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cproz.pantomath.About.About;
import com.cproz.pantomath.AdditionalInfo.AdditionalInfo;
import com.cproz.pantomath.R;

import java.util.Objects;

public class Settings extends AppCompatActivity {


    Button UpdatePlan, PersonalInfo, InviteFriends, Feedback, Help, About, Logout;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        Initialisation();


        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);


        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogoutPopUp log = new LogoutPopUp();
                log.show(getSupportFragmentManager(), "logout");
            }
        });


        Feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, com.cproz.pantomath.Feedback.Feedback.class));
            }
        });

        PersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, AdditionalInfo.class);
                intent.putExtra("Settings", "Settings");
                startActivity(intent);
            }
        });

        Help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.cproz.net");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        About.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, com.cproz.pantomath.About.About.class));
            }
        });


    }

    public void Initialisation(){

        PersonalInfo = findViewById(R.id.PersonalInfo);
        InviteFriends = findViewById(R.id.InviteFriends);
        Feedback = findViewById(R.id.feedback);
        Help = findViewById(R.id.Help);
        About = findViewById(R.id.about);
        Logout = findViewById(R.id.logout);
        toolbar = findViewById(R.id.SettingsToolBar);
    }

}