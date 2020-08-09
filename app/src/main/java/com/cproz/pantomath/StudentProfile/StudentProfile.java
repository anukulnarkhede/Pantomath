package com.cproz.pantomath.StudentProfile;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cproz.pantomath.Home.Home;
import com.cproz.pantomath.Home.HomeDoubtData;
import com.cproz.pantomath.Home.HomeFragment;
import com.cproz.pantomath.R;
import com.cproz.pantomath.Signup.NewAccount;
import com.cproz.pantomath.Signup.ProfilePictureSignup;
import com.cproz.pantomath.Signup.SignupInfoCarrier;
import com.cproz.pantomath.Upload.SmartSuggestionAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentProfile extends AppCompatActivity {


    Toolbar toolbar;
    CircleImageView profilePicture, editPhotoButt;
    TextView  NoOfDoubts,NoOfSolved,NoOfUnsolved,UserName, BoardClass;
    RecyclerView recyclerView;
    ImageView setting;
    Button Algebra, Geometry, Physics, Chemistry, Biology, History, Geography, Languages, AllSubject;
    static  String SUBJECT = StudentProfile.SUBJECT;
    public String Board, Class;
    private FirebaseFirestore db;
    private List<HomeDoubtData> DoubtList1;
    private HomeDoubtData homeDoubtData;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    String email = user != null ? user.getEmail() : null;
    private DocumentReference ref = firebaseFirestore.collection("Users/Students/StudentsInfo/" ).document(String.valueOf(email));
    List <HomeDoubtData> DoubtList2;
    String decision;
    Uri mCropImageUri;
    FirebaseStorage firebaseStorage;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_profile);

        Initialisation();

        db = FirebaseFirestore.getInstance();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        DoubtList1 = new ArrayList<>();
        DoubtList2 = new ArrayList<>();

        firebaseStorage = FirebaseStorage.getInstance();
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);

        progressBar = findViewById(R.id.progressBarProfile);
        progressBar.setVisibility(View.GONE);



        Algebra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoubtList1.clear();
                LoadDataFromFirebaseSubFilter("Algebra");
            }
        });

        AllSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoubtList1.clear();
                LoadDataFromFirebase();

            }
        });

        Geometry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoubtList1.clear();
                LoadDataFromFirebaseSubFilter("Geometry");
            }
        });

        Physics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoubtList1.clear();
                LoadDataFromFirebaseSubFilter("Physics");
            }
        });

        Chemistry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoubtList1.clear();
                LoadDataFromFirebaseSubFilter("Chemistry");
            }
        });

        Biology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoubtList1.clear();
                LoadDataFromFirebaseSubFilter("Biology");
            }
        });

        History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoubtList1.clear();
                LoadDataFromFirebaseSubFilter("History");
            }
        });


        Geography.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoubtList1.clear();
                LoadDataFromFirebaseSubFilter("Geography");
            }
        });

        Languages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoubtList1.clear();
                LoadDataFromFirebaseSubFilter("Languages");
            }
        });

        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){


                    Board = documentSnapshot.getString("Board");
                    Class = documentSnapshot.getString("STD");

                    String boardAndclass = documentSnapshot.getString("Class") + " " + Objects.requireNonNull(documentSnapshot.getString("Board")).toUpperCase();
                    BoardClass.setText(boardAndclass);
                    UserName.setText(toTitleCase(Objects.requireNonNull(documentSnapshot.getString("Name"))));

                    if (Objects.equals(documentSnapshot.getString("profileURL"), "")){
                        profilePicture.setImageResource(R.drawable.defprofileimage);
                    }
                    else{
                        Picasso.get().load(documentSnapshot.getString("profileURL")).into(profilePicture);
                    }




                    LoadDataFromFirebase();


                    LoadDataFromFirebaseNoOFUnsolved();










                }
                else{
                    System.out.println("does not exist");
                }
            }
        });









        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentProfile.this, Settings.class));
            }
        });





        profilePicture.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                onSelectImageClick(v);

            }
        });





    }




    public void Initialisation(){
        toolbar = findViewById(R.id.StudentProfileToolBar);
        profilePicture = findViewById(R.id.ProfilePicture_profile);
        editPhotoButt = findViewById(R.id.editProfilePicture_profile);
        NoOfDoubts = findViewById(R.id.noOfDoubts_profile);
        NoOfSolved = findViewById(R.id.noOfSolvedDoubts_profile);
        NoOfUnsolved = findViewById(R.id.noOfUnSolvedDoubts_profile);
        recyclerView = findViewById(R.id.RecyclerViewProfile);
        UserName = findViewById(R.id.UserNameProfile);
        BoardClass = findViewById(R.id.ClassAndBoard_Profile);
        setting = findViewById(R.id.settingsProfile);
        Algebra = findViewById(R.id.AlgebraProfile);
        Geometry = findViewById(R.id.GeometryProfile);
        Physics = findViewById(R.id.PhysicsProfile);
        Chemistry = findViewById(R.id.ChemistryProfile);
        Biology = findViewById(R.id.BiologyProfile);
        History = findViewById(R.id.HistoryProfile);
        Geography = findViewById(R.id.GeographyProfile);
        Languages = findViewById(R.id.LanguagesProfile);
        AllSubject = findViewById(R.id.AllProfile);

    }



    private void LoadDataFromFirebase(){
        db.collection("Doubts").whereEqualTo("Board", HomeFragment.BOARD).whereEqualTo("STD",HomeFragment.CLASS).whereEqualTo("Email", email).orderBy("DateTime", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot querySnapshot : Objects.requireNonNull(task.getResult())){


                    //Date date = new Date();











                    homeDoubtData = new HomeDoubtData(querySnapshot.getString("AnsPhotoUrl1"), querySnapshot.getString("AnsPhotoUrl2"), querySnapshot.getString("AnsText"),
                            querySnapshot.getString("AudioUrl"), querySnapshot.getString("Board"), querySnapshot.getString("Chapter"),
                            querySnapshot.getString("Email"), querySnapshot.getString("FileUrl"), querySnapshot.getString("Link"),
                            querySnapshot.getString("Name"), querySnapshot.getString("Photo1url"), querySnapshot.getString("Photo2url"),
                            querySnapshot.getString("ProfileImageURL"), querySnapshot.getString("QText"), querySnapshot.getString("STD"),
                            querySnapshot.getString("Status"), querySnapshot.getString("Subject"), querySnapshot.getString("Teacher"), querySnapshot.getString("Uid")
                            , querySnapshot.getDate("DateTime"),"");

                    DoubtList1.add(homeDoubtData);

                    //HomeStuDoubtCardAdapter homeStuDoubtCardAdapter = new HomeStuDoubtCardAdapter(getContext(), DoubtList1);


                    SmartSuggestionAdapter smartSuggestionAdapter = new SmartSuggestionAdapter(StudentProfile.this,DoubtList1);

                    NoOfDoubts.setText(String.valueOf(DoubtList1.size()));






                    recyclerView.setItemViewCacheSize(40);

                    recyclerView.setAdapter(smartSuggestionAdapter);

                }
            }
        });
    }


    private void LoadDataFromFirebaseNoOFUnsolved(){
        db.collection("Doubts").whereEqualTo("Board", HomeFragment.BOARD).whereEqualTo("STD",HomeFragment.CLASS).whereEqualTo("Email", email).whereEqualTo("Status", "Unsolved").orderBy("DateTime", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot querySnapshot : Objects.requireNonNull(task.getResult())){


                    //Date date = new Date();













                    homeDoubtData = new HomeDoubtData(querySnapshot.getString("AnsPhotoUrl1"), querySnapshot.getString("AnsPhotoUrl2"), querySnapshot.getString("AnsText"),
                            querySnapshot.getString("AudioUrl"), querySnapshot.getString("Board"), querySnapshot.getString("Chapter"),
                            querySnapshot.getString("Email"), querySnapshot.getString("FileUrl"), querySnapshot.getString("Link"),
                            querySnapshot.getString("Name"), querySnapshot.getString("Photo1url"), querySnapshot.getString("Photo2url"),
                            querySnapshot.getString("ProfileImageURL"), querySnapshot.getString("QText"), querySnapshot.getString("STD"),
                            querySnapshot.getString("Status"), querySnapshot.getString("Subject"), querySnapshot.getString("Teacher"), querySnapshot.getString("Uid")
                            , querySnapshot.getDate("DateTime"),"");

                    DoubtList2.add(homeDoubtData);






                    NoOfUnsolved.setText(String.valueOf(DoubtList2.size()));

                    int x = DoubtList1.size() - DoubtList2.size();

                    NoOfSolved.setText(String.valueOf(x));








                }
            }
        });
    }


    private void LoadDataFromFirebaseSubFilter(String Subjectx){
        db.collection("Doubts").whereEqualTo("Board", HomeFragment.BOARD).whereEqualTo("STD", HomeFragment.CLASS).whereEqualTo("Email", email).whereEqualTo("Subject", Subjectx).orderBy("DateTime", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot querySnapshot : Objects.requireNonNull(task.getResult())){


                    //Date date = new Date();









                    homeDoubtData = new HomeDoubtData(querySnapshot.getString("AnsPhotoUrl1"), querySnapshot.getString("AnsPhotoUrl2"), querySnapshot.getString("AnsText"),
                            querySnapshot.getString("AudioUrl"), querySnapshot.getString("Board"), querySnapshot.getString("Chapter"),
                            querySnapshot.getString("Email"), querySnapshot.getString("FileUrl"), querySnapshot.getString("Link"),
                            querySnapshot.getString("Name"), querySnapshot.getString("Photo1url"), querySnapshot.getString("Photo2url"),
                            querySnapshot.getString("ProfileImageURL"), querySnapshot.getString("QText"), querySnapshot.getString("STD"),
                            querySnapshot.getString("Status"), querySnapshot.getString("Subject"), querySnapshot.getString("Teacher"), querySnapshot.getString("Uid")
                            , querySnapshot.getDate("DateTime"),"");

                    DoubtList1.add(homeDoubtData);

                    //HomeStuDoubtCardAdapter homeStuDoubtCardAdapter = new HomeStuDoubtCardAdapter(getContext(), DoubtList1);


                    SmartSuggestionAdapter smartSuggestionAdapter = new SmartSuggestionAdapter(StudentProfile.this,DoubtList1);








                    recyclerView.setItemViewCacheSize(25);

                    recyclerView.setAdapter(smartSuggestionAdapter);

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


    @SuppressLint("SetTextI18n")
    public void GalleryOrCamera(String actionx) {
        String action;
        action = actionx;

        if (action == null) {
            System.out.println("Wait");
        } else if (action.equals("gallery")) {
            decision = "gallery";


        } else if (action.equals("camera")) {
            decision = "camera";
        }
    }



    public void onSelectImageClick(View view) {
        //CropImage.startPickImageActivity(this);
        CropImage.activity().start(this);


    }




    @Override
    @SuppressLint({"NewApi", "SetTextI18n"})
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of pick image chooser
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mCropImageUri = result.getUri();

                profilePicture.setImageURI(mCropImageUri);

                UpdateProfilePic(mCropImageUri);








            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // required permissions granted, start crop image activity
            startCropImageActivity(mCropImageUri);
        }
        else {
            Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }


    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void UpdateProfilePic( Uri mCropImageUri){

        StorageReference storageReference = firebaseStorage.getReference();

        final StorageReference reference = storageReference.child("ProfilePictures/" + email + "/profile.jpg");

        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(100, true);



        reference.putFile(mCropImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String ProfileURL = uri.toString();

                        firebaseFirestore.collection("Users/Students/StudentsInfo/"  ).document( email).update("profileURL", ProfileURL).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                Toast.makeText(StudentProfile.this, "Profile Picture Updated", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        System.out.println("Profile Picture was unable to update");
                                        progressBar.setVisibility(View.GONE);

                                    }
                                });
                    }
                });
            }
        });

    }




    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

    }
}