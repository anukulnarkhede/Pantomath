package com.cproz.pantomath.About;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.mbms.MbmsErrors;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cproz.pantomath.BuildConfig;
import com.cproz.pantomath.R;

import java.util.Objects;

public class About extends AppCompatActivity {

    Button TermsButt, PrivacyPolicyButt;
    TextView buildNumberAct;

    Toolbar toolbar;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        Initialization();



        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(Color.parseColor("#5f6368"), PorterDuff.Mode.SRC_ATOP);


        PrivacyPolicyButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.cproz.net/d-solveprivacypolicy");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        TermsButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.cproz.net/d-solvetermsofservice");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        buildNumberAct.setText(BuildConfig.VERSION_NAME + " - Beta");





    }


    private void Initialization() {
        TermsButt = findViewById(R.id.TermsButt);
        PrivacyPolicyButt  = findViewById(R.id.PrivacyPolicyButt);
        toolbar = findViewById(R.id.AboutToolbar);
        buildNumberAct = findViewById(R.id.buildNumberAct);
    }


}