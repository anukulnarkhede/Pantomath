package com.cproz.pantomath.StudentProfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cproz.pantomath.Home.Home;
import com.cproz.pantomath.Home.HomeDoubtData;
import com.cproz.pantomath.Home.HomeFragment;
import com.cproz.pantomath.R;
import com.cproz.pantomath.Signup.NewAccount;
import com.cproz.pantomath.Signup.Password;
import com.cproz.pantomath.Signup.ProfilePictureSignup;
import com.cproz.pantomath.Signup.SignupInfoCarrier;
import com.cproz.pantomath.Upload.DoubtCarrier;
import com.cproz.pantomath.Upload.UploadFragment;
import com.cproz.pantomath.Upload.UploadImagePage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jackandphantom.circularimageview.CircleImage;
import com.jackandphantom.circularimageview.RoundedImage;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Feedback extends AppCompatActivity {


    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    String email = user != null ? user.getEmail() : null;
    private DocumentReference ref = firebaseFirestore.collection("Users/Students/StudentsInfo/" ).document(String.valueOf(email));
    String StudentClass = null, StudentBoard = null;
    public static String SUBJECT = UploadFragment.SUBJECT, CHAPTER = UploadFragment.CHAPTER, NAME = UploadFragment.NAME, EMAIL = UploadFragment.EMAIL;
    Toolbar toolbar;
    Uri mCropImageUri, uriImg1, uriImg2;
    String decision;
    private FirebaseFirestore db;
    EditText feedBackText;
    Button Addfeedback, FeedbackImageCancle;
    RoundedImage feedbackImage;
    ProgressBar progressBar;
    TextView addPhoto;


    FirebaseStorage firebaseStorage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
        toolbar = findViewById(R.id.FeedbackToolBar);
        feedBackText = findViewById(R.id.FeedbackText);
        Addfeedback = findViewById(R.id.AddFeedback);
        feedbackImage = findViewById(R.id.FeedbackImage);
        FeedbackImageCancle = findViewById(R.id.FeedbackImageCancle);
        progressBar = findViewById(R.id.progressBarFeedback);
        addPhoto = findViewById(R.id.addPhoto);

        progressBar.setVisibility(View.GONE);
        FeedbackImageCancle.setVisibility(View.GONE);









        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);



        feedbackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectImageClick(v);
            }
        });


        FeedbackImageCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                feedbackImage.setImageResource(R.drawable.image_view_bg);
                //doubtImage2.setVisibility(View.GONE);
                addPhoto.setVisibility(View.VISIBLE);

                //imageCancel1.setVisibility(View.GONE);
                FeedbackImageCancle.setVisibility(View.GONE);


                uriImg1 = null;



            }
        });


        Addfeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Feedback.this, Home.class));

            }
        });




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
        CropImage.activity().start(Feedback.this);


    }




    @Override
    @SuppressLint({"NewApi", "SetTextI18n"})
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of pick image chooser
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(Objects.requireNonNull(Feedback.this), data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(Feedback.this, imageUri)) {
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

                feedbackImage.setImageURI(mCropImageUri);
                addPhoto.setVisibility(View.GONE);
                FeedbackImageCancle.setVisibility(View.VISIBLE);
                uriImg1 = mCropImageUri;
                //profilePicture.setImageURI(mCropImageUri);
                //SelectPhotoText.setText("Change Photo");











            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(Feedback.this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // required permissions granted, start crop image activity
            startCropImageActivity(mCropImageUri);
        }
        else {
            Toast.makeText(Feedback.this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }


    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(Feedback.this);
    }


   /*public void AddFeedback(){
       final String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();

       //profile picture upload........\
       StorageReference storageReference = firebaseStorage.getReference();

       final StorageReference reference = storageReference.child("Feedback/" + userId + "/feedBackScrsht.jpg");


       Bitmap bitmap = null;
       try {
           bitmap = MediaStore.Images.Media.getBitmap(Feedback.this.getContentResolver(), mCropImageUri);
       } catch (IOException e) {
           e.printStackTrace();
       }
       ByteArrayOutputStream baos = new ByteArrayOutputStream();
       assert bitmap != null;
       bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
       byte[] data = baos.toByteArray();

       reference.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
           @Override
           public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                   @Override
                   public void onSuccess(Uri uri) {
                       String ProfileURL = uri.toString();
                       SignupInfoCarrier signupInfoCarrier = new SignupInfoCarrier(
                               NewAccount.NAME, NewAccount.EMAIL, "","","Student","","", userId, ProfileURL
                       );
                       firebaseFirestore.document("Users/Students/StudentsInfo/" + userId ).set(signupInfoCarrier).addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {



                               //startActivity(new Intent(ProfilePictureSignup.this, Home.class));
                           }
                       })
                               .addOnFailureListener(new OnFailureListener() {
                                   @Override
                                   public void onFailure(@NonNull Exception e) {
                                       Toast.makeText(Feedback.this, "Signup Failed", Toast.LENGTH_SHORT).show();
                                       System.out.println("Document upload failed");
                                      Addfeedback.setEnabled(true);
                                   }
                               });
                   }
               });
           }
       });
   }*/



}