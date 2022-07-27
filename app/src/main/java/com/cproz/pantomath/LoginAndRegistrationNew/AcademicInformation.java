package com.cproz.pantomath.LoginAndRegistrationNew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.cproz.pantomath.Home.Home;
import com.cproz.pantomath.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class AcademicInformation extends AppCompatActivity {

    Toolbar toolbar;
    Button Submit, progressBar;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    DocumentReference reference = firebaseFirestore.collection("Variables/").document("All Variables");
    List<String> Cities = new ArrayList<>(), Classes = new ArrayList<>(), Boards = new ArrayList<>(), Institutes = new ArrayList<>();
    ArrayList<String> CitiesList = new ArrayList<>(), ClassesList = new ArrayList<>() , BoardsList = new ArrayList<>(), InstitutesList = new ArrayList<>();
    Spinner ClassSpinner, BoardSpinner, CitySpinner, InstituteSpinner;
    String selectedCity = "Select your city", selectedClass = "Select your class", selectedBoard = "Select your board", selectedInstitute = "Select your institute";
    String Name, PhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academic_information);
        Initialisation();

        progressBar.setVisibility(View.GONE);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow_rounded);



        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            Name = bundle.getString("Name");
            PhoneNumber = bundle.getString("PhoneNumber");
        }


        CitiesList.add("Select your city");
        ClassesList.add("Select your class");
        BoardsList.add("Select your board");
        InstitutesList.add("Select your institute");



        final ArrayAdapter<String> arrayAdapterForCities =
                new ArrayAdapter<>(AcademicInformation.this, R.layout.style_spinner, CitiesList);
        CitySpinner.setAdapter(arrayAdapterForCities);

        final ArrayAdapter<String> arrayAdapterForClass =
                new ArrayAdapter<>(AcademicInformation.this, R.layout.style_spinner, ClassesList);
        ClassSpinner.setAdapter(arrayAdapterForClass);

        final ArrayAdapter<String> arrayAdapterForBoard =
                new ArrayAdapter<>(AcademicInformation.this, R.layout.style_spinner, BoardsList);
        BoardSpinner.setAdapter(arrayAdapterForBoard);

        final ArrayAdapter<String> arrayAdapterForInstitutes =
                new ArrayAdapter<>(AcademicInformation.this, R.layout.style_spinner, InstitutesList);
        InstituteSpinner.setAdapter(arrayAdapterForInstitutes);




        reference
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Cities = (List<String>) documentSnapshot.get("Cities");
                        Classes = (List<String>) documentSnapshot.get("Classes");
                        Boards = (List<String>) documentSnapshot.get("Boards");

                        CitiesList = (ArrayList<String>) Cities;
                        ClassesList = (ArrayList<String>) Classes;
                        BoardsList = (ArrayList<String>) Boards;





//-------------------------------------------------------------------------------------------------------------------------------------------------------
                        //City spinner
                        final ArrayAdapter<String> arrayAdapterForCities =
                                new ArrayAdapter<>(AcademicInformation.this, R.layout.style_spinner, CitiesList);
                        CitySpinner.setAdapter(arrayAdapterForCities);
                        CitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                selectedCity = CitiesList.get(position);

                                //-------------------------------------------------------------------------------------------------------------------------------------------------------
                                //Institute spinner

                                InstitutesList.clear();
                                InstitutesList.add("Select your institute");
                                if (selectedCity != null){
                                    firebaseFirestore.collection("Institutes/")
                                            .whereArrayContains("Cities", selectedCity)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                    for (QueryDocumentSnapshot querySnapshot : Objects.requireNonNull(task.getResult())){
                                                        InstitutesList.add(querySnapshot.getString("Name"));
                                                    }
                                                    final ArrayAdapter<String> arrayAdapterForInstitute =
                                                            new ArrayAdapter<>(AcademicInformation.this, R.layout.style_spinner, InstitutesList);
                                                    InstituteSpinner.setAdapter(arrayAdapterForInstitute);
                                                    InstituteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                        @Override
                                                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                                            selectedInstitute = InstitutesList.get(position);

                                                            Submit.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View view) {
                                                                    uploadDataToFirebase(selectedInstitute, selectedCity, selectedClass, selectedBoard);
                                                                }
                                                            });



                                                        }

                                                        @Override
                                                        public void onNothingSelected(AdapterView<?> adapterView) {
                                                            selectedInstitute = InstitutesList.get(0);

                                                        }
                                                    });



                                                }

                                            });

                                }


                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                selectedCity = "Select your city";

                            }
                        });

                        //-------------------------------------------------------------------------------------------------------------------------------------------------------
                        //Class spinner
                        final ArrayAdapter<String> arrayAdapterForClass =
                                new ArrayAdapter<>(AcademicInformation.this, R.layout.style_spinner, ClassesList);
                        ClassSpinner.setAdapter(arrayAdapterForClass);
                        ClassSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                selectedClass = ClassesList.get(position);

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                selectedClass = ClassesList.get(0);

                            }
                        });


//-------------------------------------------------------------------------------------------------------------------------------------------------------
                        //Board spinner
                        final ArrayAdapter<String> arrayAdapterForBoard =
                                new ArrayAdapter<>(AcademicInformation.this, R.layout.style_spinner, BoardsList);
                        BoardSpinner.setAdapter(arrayAdapterForBoard);
                        BoardSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                selectedBoard = BoardsList.get(position);

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                selectedBoard = BoardsList.get(0);

                            }
                        });









                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });





    }

    private void uploadDataToFirebase(String selectedInstitute, String selectedCity, String selectedClass, String selectedBoard) {

        if (selectedClass.equals("Select your class")
                || selectedBoard.equals("Select your board")
                || selectedCity.equals("Select your city")
                || selectedInstitute.equals("Select your institute")){
            Toast.makeText(AcademicInformation.this, "All fields are compulsory.", Toast.LENGTH_SHORT).show();
        }else{
            RelayToFirebase(Name, PhoneNumber, selectedClass, selectedBoard, selectedCity, selectedInstitute);
        }


    }

    private void RelayToFirebase(String name, String phoneNumber, String selectedClass, String selectedBoard, String selectedCity, String selectedInstitute) {


            progressBar.setVisibility(View.VISIBLE);
            String Email = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();

        assert Email != null;
        firebaseFirestore.collection("Users/Students/StudentsInfo/")
                .document(Email)
                .update("Address", selectedCity,
                        "Board", selectedBoard,
                        "Class", selectedClass,
                        "Number", phoneNumber,
                        "Name", name,
                        "Institute", selectedInstitute)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                startActivity(new Intent(AcademicInformation.this, Home.class));
                progressBar.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
            }
        });


    }

    private void Initialisation() {
        toolbar = findViewById(R.id.AcademicInfoToolbar);
        ClassSpinner = findViewById(R.id.selectClass);
        BoardSpinner = findViewById(R.id.selectBoard);
        CitySpinner = findViewById(R.id.selectCity);
        InstituteSpinner = findViewById(R.id.selectInstitute);
        Submit = findViewById(R.id.Submit);
        progressBar = findViewById(R.id.progressBar);
    }


}