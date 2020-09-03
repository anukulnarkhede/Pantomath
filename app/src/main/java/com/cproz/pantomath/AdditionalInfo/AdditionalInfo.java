package com.cproz.pantomath.AdditionalInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.telephony.mbms.MbmsErrors;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cproz.pantomath.Home.Home;
import com.cproz.pantomath.R;
import com.cproz.pantomath.Signup.PackageSelection;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class AdditionalInfo extends AppCompatActivity {

    EditText PhoneNumber;
    Spinner CitySpinner, InstituteSeletor;
    Toolbar toolbar;
    String SelectedCity = "", SelectedInstitute = "";
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    String email = Objects.requireNonNull(user).getEmail();
    public static String SELECTEDCITY = AdditionalInfo.SELECTEDCITY = "",SELECTEDINSTITUTE =  AdditionalInfo.SELECTEDINSTITUTE = "";
    Button UpdateInfo;
    TextView ErrorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_info);
        Initialization();
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);




        final String[] City = {"Select City", "Aurangabad", "Nashik"};

        final ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(City));

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.style_spinner, arrayList);

        CitySpinner.setAdapter(arrayAdapter);

        CitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SELECTEDCITY = City[position];
                //Toast.makeText(AddTeacher.this, SUBJECT, Toast.LENGTH_SHORT).show();




            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                SELECTEDCITY = "";
            }
        });


       // Toast.makeText(this, "", Toast.LENGTH_SHORT).show();






        final String[] Institute = {"Select Institute", "Litmus Academy, A-Cidco", "Litmus Academy, A-Hudco", "Litmus Academy, A-Jyoti Nagar",
                "Litmus Academy, A-Deolai", "Litmus Academy, A-Nirala Bazar", "Litmus Academy, N-Ashok Stambh", "Litmus Academy, N-Jehan Circle",
                "Litmus Academy, N-Indira Nagar",
                "Litmus Academy, N-Trimurti Chauk", "Litmus Academy, N-Ashoka Marg", "Litmus Academy, N-Amrutdham", "Litmus Academy, N-Deolali Camp"};

        final ArrayList<String> arrayList1 = new ArrayList<>(Arrays.asList(Institute));

        final ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(this, R.layout.style_spinner, arrayList1);


        InstituteSeletor.setAdapter(arrayAdapter1);

        InstituteSeletor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SELECTEDINSTITUTE = Institute[position];


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        UpdateInfo.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                String number = PhoneNumber.getText().toString();
                String city = SELECTEDCITY;
                String Institute = SELECTEDINSTITUTE;
                String Board = PackageSelection.SELECTEDBOARD;
                String STD = PackageSelection.SELECTEDCLASS;



                if (PhoneNumber.getText().toString().isEmpty()){
                    PhoneNumber.requestFocus();
                    ErrorText.setTextColor(Color.parseColor("#FF2829"));

                }

                else if (PhoneNumber.getText().toString().length() < 10 || PhoneNumber.getText().toString().length() >10 ){
                    PhoneNumber.requestFocus();

                    ErrorText.setText("Phone number must be of 10 digits");
                    ErrorText.setTextColor(Color.parseColor("#FF2829"));
                }
                else if (city.equals("Select City")){

                    ErrorText.setText("Select City");
                    ErrorText.setTextColor(Color.parseColor("#FF2829"));
                }
                else if (Institute.equals("Select Institute")){

                    ErrorText.setText("Select Institute");
                    ErrorText.setTextColor(Color.parseColor("#FF2829"));
                }
                else{

                    ErrorText.setTextColor(Color.parseColor("#999999"));

                    Bundle bundle = getIntent().getExtras();
                    assert bundle != null;
                    if (Objects.equals(bundle.getString("Settings"), "Settings")){
                        UpdateSettings(number, city, Institute);
                    }else{
                        UpdateCity(number,city,Institute,Board,STD);
                    }
                }


            }
        });





    }

    private void Initialization() {

        PhoneNumber = findViewById(R.id.PhoneNumber);
        CitySpinner = findViewById(R.id.City);
        InstituteSeletor = findViewById(R.id.SelectInstitute);
        toolbar = findViewById(R.id.AdditionalInfoToolbar);
        UpdateInfo = findViewById(R.id.UpdateInfo);
        ErrorText = findViewById(R.id.errorText);

    }

    public void UpdateCity(String number, String city, String Institute, String Board, String STD){
        firebaseFirestore.collection("Users/Students/StudentsInfo/").document(email).
                update("Number", number, "Address", city, "Institute", Institute, "Board", Board, "Class", STD).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                startActivity(new Intent(AdditionalInfo.this, Home.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    public void UpdateSettings(String number, String city, String Institute){
        firebaseFirestore.collection("Users/Students/StudentsInfo/").document(email).
                update("Number", number, "Address", city).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                startActivity(new Intent(AdditionalInfo.this, Home.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}