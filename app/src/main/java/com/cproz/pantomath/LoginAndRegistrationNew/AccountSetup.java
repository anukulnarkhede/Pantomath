package com.cproz.pantomath.LoginAndRegistrationNew;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.NestedScrollingChild;

import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cproz.pantomath.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.Objects;

public class AccountSetup extends AppCompatActivity {

    Toolbar toolbar;
    EditText FirstName, LastName, PhoneNumber;
    Button Next;
    String FullName, phoneNumberStr;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_setup);
        Initialisation();

        String Email = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();
        if (Email != null){
            firebaseFirestore.collection("Users/Students/StudentsInfo/")
                    .document(Email)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String[] newStr = Objects.requireNonNull(documentSnapshot.getString("Name")).split("\\s+");
                            if (newStr.length > 1){
                                FirstName.setText(newStr[0]);
                                LastName.setText(newStr[1]);
                            }
                            PhoneNumber.setText(documentSnapshot.getString("Number"));


                        }
                    });
        }


//        setSupportActionBar(toolbar);
//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow_rounded);

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FirstName.getText().toString().isEmpty() ||
                        LastName.getText().toString().isEmpty() ||
                        PhoneNumber.getText().toString().isEmpty()){
                    Toast.makeText(AccountSetup.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }else if (PhoneNumber.getText().length() < 10){
                    PhoneNumber.setError("Invalid phone number");
                    PhoneNumber.requestFocus();
                }else{
                    FullName = FirstName.getText().toString() + " " +LastName.getText().toString();
                    FullName = toTitleCase(FullName);
                    phoneNumberStr = PhoneNumber.getText().toString();
                    Intent intent = new Intent(AccountSetup.this, AcademicInformation.class);
                    intent.putExtra("Name", FullName);
                    intent.putExtra("PhoneNumber", phoneNumberStr);
                    startActivity(intent);
                }


            }
        });




    }

    private void Initialisation() {

        toolbar = findViewById(R.id.AccSetUpToolbar);
        FirstName = findViewById(R.id.FirstName);
        LastName = findViewById(R.id.LastName);
        PhoneNumber = findViewById(R.id.PhoneNumber);
        Next = findViewById(R.id.NextAccSetUp);
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
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
}