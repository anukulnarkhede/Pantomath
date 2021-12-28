package com.cproz.pantomath.Signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.cproz.pantomath.AdditionalInfo.AdditionalInfo;
import com.cproz.pantomath.Home.Home;
import com.cproz.pantomath.R;
import com.cproz.pantomath.StudentProfile.StudentProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class PackageSelection extends AppCompatActivity {

    RadioGroup selectClass, selectBoard;
    RadioButton selectclass, selectboard;
    Button createAccount;
    String SelectedClass, SelectedBoard;

    public static  String SELECTEDBOARD = PackageSelection.SELECTEDBOARD = "", SELECTEDCLASS = PackageSelection.SELECTEDCLASS = "";


    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    String email = user != null ? user.getEmail() : null;
    private final DocumentReference ref = firebaseFirestore.collection("Users/Students/StudentsInfo/" ).document(String.valueOf(email));


    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.package_selection);
        Initialisation();




        int radioIdClass = selectClass.getCheckedRadioButtonId(), radioIdBoard = selectBoard.getCheckedRadioButtonId();

        selectClass.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case(R.id.class9th):
                        SELECTEDCLASS = "9th";
                        break;
                    case(R.id.class10th):
                        SELECTEDCLASS = "10th";
                        break;
                }
            }
        });


        selectBoard.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case(R.id.SSC):
                        SELECTEDBOARD = "SSC";
                        break;

                    case(R.id.CBSE):
                        SELECTEDBOARD = "CBSE";
                        break;

                    case(R.id.ICSE):
                        SELECTEDBOARD = "ICSE";
                }
            }
        });



        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (SELECTEDBOARD.equals("") || SelectedClass.equals("")){
                    Toast.makeText(PackageSelection.this, "Select class and board", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(PackageSelection.this, AdditionalInfo.class);
                    intent.putExtra("Board", SELECTEDBOARD);
                    intent.putExtra("Class", SELECTEDCLASS);
                    startActivity(intent);
//                Toast.makeText(PackageSelection.this, SelectedBoard + "  " + SelectedClass, Toast.LENGTH_SHORT).show();
//                UpdateClassBoards();
                }


            }
        });



    }

    public void Initialisation(){

        selectClass = findViewById(R.id.selectClass);
        selectBoard = findViewById(R.id.selectBoard);
        createAccount = findViewById(R.id.createAcc);
    }

//    public void UpdateClassBoards(){
//
//
//
//        firebaseFirestore.collection("Users/Students/StudentsInfo/"  ).document( email).update("Class", SelectedClass, "Board",SelectedBoard).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//
//                startActivity(new Intent(PackageSelection.this, Home.class));
//            }
//        })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                        System.out.println("Profile Picture was unable to update");
//                        //progressBar.setVisibility(View.GONE);
//
//                    }
//                });
//    }



}