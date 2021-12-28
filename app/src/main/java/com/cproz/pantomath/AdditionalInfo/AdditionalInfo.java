package com.cproz.pantomath.AdditionalInfo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.mbms.MbmsErrors;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cproz.pantomath.Home.Home;
import com.cproz.pantomath.R;
import com.cproz.pantomath.Signup.EmailVerificationPopUp;
import com.cproz.pantomath.Signup.NewAccount;
import com.cproz.pantomath.Signup.PackageSelection;
import com.cproz.pantomath.Signup.Password;
import com.cproz.pantomath.Signup.ProfilePictureSignup;
import com.cproz.pantomath.Signup.RetryPopup;
import com.cproz.pantomath.Signup.SignupInfoCarrier;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class AdditionalInfo extends AppCompatActivity {


    EditText PhoneNumber;
    Spinner CitySpinner, InstituteSeletor, SelectBranch;
    Toolbar toolbar;
    String SelectedCity = "", SelectedInstitute = "";
//    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
//    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//    FirebaseUser user = firebaseAuth.getCurrentUser();
//    String email = Objects.requireNonNull(user).getEmail();
    public static String SELECTEDCITY = AdditionalInfo.SELECTEDCITY = "",
        SELECTEDINSTITUTE =  AdditionalInfo.SELECTEDINSTITUTE = "",
        SELECTEDBRANCH = AdditionalInfo.SELECTEDBRANCH = "";

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    Button UpdateInfo, progressBar;
    CheckBox OtherStudents;
    TextView ErrorText;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_info);
        Initialization();
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(Color.parseColor("#5f6368"), PorterDuff.Mode.SRC_ATOP);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
//        Bundle bundle = getIntent().getExtras();
//        assert bundle != null;
//        if (Objects.equals(bundle.getString("Settings"), "Settings")){
//           OtherStudents.setVisibility(View.GONE);
//           UpdateInfo.setText("Save Changes");
//        }else{
//            OtherStudents.setVisibility(View.VISIBLE);
//        }



//        final String[] City = {"Select City",
//                "Aurangabad",
//                "Nashik",
//                "Ahmednagar",
//                "Akola",
//                "Amravati",
//                "Bhandara",
//                "Beed",
//                "Buldhana",
//                "Chandrapur",
//                "Dhule",
//                "Gadchiroli",
//                "Gondia",
//                "Hingoli",
//                "Jalgaon",
//                "Jalna",
//                "Kolhapur",
//                "Latur",
//                "Mumbai City",
//                "Mumbai suburban",
//                "Nandurbar",
//                "Nanded",
//                "Nagpur",
//                "Osmanabad",
//                "Parbhani",
//                "Pune",
//                "Raigad",
//                "Ratnagiri",
//                "Sindhudurg",
//                "Sangli",
//                "Solapur",
//                "Satara",
//                "Thane",
//                "Wardha",
//                "Washim",
//                "Yavatmal"};

        final String[] City = {
                "Select City",
                "Aurangabad",
                "Nashik",
                };

        final ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(City));

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.style_spinner, arrayList);

        CitySpinner.setAdapter(arrayAdapter);

        CitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SELECTEDCITY = City[position];
                //Toast.makeText(AddTeacher.this, SUBJECT, Toast.LENGTH_SHORT).show();


                if (SELECTEDCITY.equals("Select City")){
                    InstituteSeletor.setEnabled(false);
                    OtherStudents.setEnabled(false);
                    final String[] Institute = {"Select Institute", "Litmus Academy"};
                    OtherStudents.setChecked(false);
                    Institute(Institute);
                }else
                if (SELECTEDCITY.equals("Aurangabad")||SELECTEDCITY.equals("Nashik")){
                    InstituteSeletor.setEnabled(true);
                    OtherStudents.setEnabled(false);
                    final String[] Institute = {"Select Institute", "Litmus Academy"};
                    OtherStudents.setChecked(false);
                    Institute(Institute);
                }else {
                    OtherStudents.setEnabled(false);
                    InstituteSeletor.setEnabled(false);
                    final String[] Institute = {"Select Institute", "Litmus Academy"};
                    Institute(Institute);
                    OtherStudents.setChecked(false);
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                SELECTEDCITY = "";
            }
        });


       // Toast.makeText(this, "", Toast.LENGTH_SHORT).show();








        final String[] Institute = {"Select Institute", "Litmus Academy"};

        Institute(Institute);

//        InstituteSeletor.setEnabled(false);
        final String[] Branches = {"Select Branch", "Cidco", "Hudco", "Nirala Bazar", "Deolai", "Jyoti Nagar"};

        Branch(Branches);




        UpdateInfo.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                String number = PhoneNumber.getText().toString();
                String city = SELECTEDCITY;
                String Institute = SELECTEDINSTITUTE;
                String Board = PackageSelection.SELECTEDBOARD;
                String STD = PackageSelection.SELECTEDCLASS;
                String Branch = SELECTEDBRANCH;





                if (PhoneNumber.getText().toString().isEmpty()){
                    PhoneNumber.requestFocus();
                    ErrorText.setTextColor(Color.parseColor("#FF2829"));
                    ErrorText.setText("Phone Number is Compulsory");

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
                else if (OtherStudents.isChecked()){
                    Institute = "Litmus Academy";
                    Branch = "NA";
                    InstituteSeletor.setEnabled(false);
                    SelectBranch.setEnabled(false);

                    ErrorText.setTextColor(Color.parseColor("#999999"));

                    Bundle bundle = getIntent().getExtras();
                    assert bundle != null;
                    if (Objects.equals(bundle.getString("Settings"), "Settings")){
                        //UpdateSettings(number, city, Institute, Branch);
                    }else{
                        //UpdateCity(number,city,Institute,Board,STD, Branch,"Unpaid");
                    }
                }
                else if (Institute.equals("Select Institute")){

                    ErrorText.setText("Select Institute");
                    ErrorText.setTextColor(Color.parseColor("#FF2829"));
                }
                else if (SELECTEDBRANCH.equals("Select Branch")){
                    ErrorText.setText("Select Branch");
                    ErrorText.setTextColor(Color.parseColor("#FF2829"));
                }
                else{

                    UpdateInfo.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);

                    signup(number,city,Board, STD, Institute, Branch );

//                    ErrorText.setTextColor(Color.parseColor("#999999"));
//
//                    Bundle bundle = getIntent().getExtras();
//                    assert bundle != null;
//                    if (Objects.equals(bundle.getString("Settings"), "Settings")){
//                        //UpdateSettings(number, city, Institute, Branch);
//                    }else{
//                        //UpdateCity(number,city,Institute,Board,STD, Branch,"Not Verified");
//                    }
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
        SelectBranch = findViewById(R.id.SelectBranch);
        OtherStudents = findViewById(R.id.OtherStudents);
        progressBar = findViewById(R.id.progressBar);


    }

//    public void UpdateCity(String number, String city, String Institute, String Board, String STD, String Branch,String UserStatus){
//        firebaseFirestore.collection("Users/Students/StudentsInfo/").document(email).
//                update("Number", number, "Address", city, "Institute", Institute, "Board", Board, "Class", STD, "User", UserStatus, "Branch", Branch).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                startActivity(new Intent(AdditionalInfo.this, Home.class));
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });
//    }
//
//
//    public void UpdateSettings(String number, String city, String Institute, String Branch){
//        firebaseFirestore.collection("Users/Students/StudentsInfo/").document(email).
//                update("Number", number, "Address", city, "Institute", Institute, "Branch", Branch).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                startActivity(new Intent(AdditionalInfo.this, Home.class));
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });
//    }

    public void Institute(final String[] Institute){
        final ArrayList<String> arrayList1 = new ArrayList<>(Arrays.asList(Institute));

        final ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(this, R.layout.style_spinner, arrayList1);


        InstituteSeletor.setAdapter(arrayAdapter1);

        InstituteSeletor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SELECTEDINSTITUTE = Institute[position];

                if (SELECTEDINSTITUTE.equals("Select Institute")||SELECTEDCITY.equals("Select City")){
                    SelectBranch.setEnabled(false);
                    final String[] Branches = {"Select Branch", "Cidco", "Hudco", "Nirala Bazar", "Deolai", "Jyoti Nagar"};

                    Branch(Branches);
                }else
                if (SELECTEDCITY.equals("Aurangabad")&&SELECTEDINSTITUTE.equals("Litmus Academy")){
                    SelectBranch.setEnabled(true);
                    final String[] Branches = {"Select Branch", "Cidco", "Hudco", "Nirala Bazar", "Deolai", "Jyoti Nagar"};

                    Branch(Branches);
                }else
                    if (SELECTEDCITY.equals("Nashik")&&SELECTEDINSTITUTE.equals("Litmus Academy")){
                        SelectBranch.setEnabled(true);
                        final String[] Branches = {"Select Branch", "Indira Nagar", "Jehan Circle", "Ashok Stambh", "Trimurti Chauk", "Ashoka Marg", "Amrutdham", "Deolali Camp"};

                        Branch(Branches);
                    }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void Branch(final String[] Branches){
        final ArrayList<String> arrayList1 = new ArrayList<>(Arrays.asList(Branches));

        final ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(this, R.layout.style_spinner, arrayList1);


        SelectBranch.setAdapter(arrayAdapter1);

        SelectBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SELECTEDBRANCH = Branches[position];


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void signup(final String Number, final String addressCity, final String Board, final String Class, final String Institute, final String Branch) {

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(NewAccount.EMAIL, NewAccount.PASSWORD)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){







                            final Date SignupTime = new Date();
                            String Email = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();

                            SignupInfoCarrier signupInfoCarrier = new SignupInfoCarrier(
                                    toTitleCase(NewAccount.NAME), NewAccount.EMAIL.toLowerCase().trim(), Number,addressCity,"Not Verified",Board,Class, Email , ""
                                    , SignupTime,0, "","",Institute, Branch

                            );
                            firebaseFirestore.document("Users/Students/StudentsInfo/" + Email )
                                    .set(signupInfoCarrier)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            FirebaseUser user = firebaseAuth.getCurrentUser();
                                            verification(user);
                                            //startActivity(new Intent(ProfilePictureSignup.this, Home.class));
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(AdditionalInfo.this, "Signup Failed", Toast.LENGTH_SHORT).show();
                                            System.out.println("Document upload failed");
                                            UpdateInfo.setEnabled(true);
                                        }
                                    });



                        }
                        else {
                            Toast.makeText(AdditionalInfo.this, "Signup Failed", Toast.LENGTH_SHORT).show();
                            System.out.println("password login failed");
                            UpdateInfo.setEnabled(true);
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

    }


    public static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder(input.length());
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }



    public void verification(FirebaseUser user){
        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                EmailVerificationPopUp emailVerificationPopUp = new EmailVerificationPopUp();
                emailVerificationPopUp.show(getSupportFragmentManager(), "asa");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                RetryPopup retryPopup = new RetryPopup();
                retryPopup.show(getSupportFragmentManager(), "axa");
            }
        });
    }


}




