package com.cproz.pantomath.Signup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.cproz.pantomath.R;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilePictureSignup extends AppCompatActivity {

    Toolbar toolbar;
    CircleImageView profilePicture;
    RelativeLayout SelectPhotoButton;
    Button ContinueButt;
    TextView IWillDoItLater;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;
    Button progressBar;
    public String decision;
    private Uri mCropImageUri = null;
    TextView SelectPhotoText;
    ImageView CamIcon;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_picture_signup);

        Initialization();

      progressBar = findViewById(R.id.progressBar);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
      progressBar.setVisibility(View.GONE);
        firebaseStorage = FirebaseStorage.getInstance();

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);



        SelectPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectImageClick(v);

            }
        });





        ContinueButt.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if (mCropImageUri == null){
                    Toast.makeText(ProfilePictureSignup.this, "Please select profile picture", Toast.LENGTH_SHORT).show();
                }
                else{
                    ContinueButt.setEnabled(false);
                   progressBar.setVisibility(View.VISIBLE);
//                    progressBar.setProgress(100, true);
                    signup();

                }

            }
        });

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

    private void signup() {

        firebaseAuth.createUserWithEmailAndPassword(NewAccount.EMAIL, Password.PASSWORD)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    final String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();

                    //profile picture upload........\
                    StorageReference storageReference = firebaseStorage.getReference();

                    final StorageReference reference = storageReference.child("ProfilePictures/" + userId + "/profile.jpg");


                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(ProfilePictureSignup.this.getContentResolver(), mCropImageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    assert bitmap != null;
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] data = baos.toByteArray();


                    final Date SignupTime = new Date();


                    reference.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String ProfileURL = uri.toString();
                                    SignupInfoCarrier signupInfoCarrier = new SignupInfoCarrier(
                                            toTitleCase(NewAccount.NAME), NewAccount.EMAIL.toLowerCase().trim(), "","","","","", userId, ProfileURL
                                    , SignupTime,0, "","","",""
                                    );
                                    firebaseFirestore.document("Users/Students/StudentsInfo/" + userId )
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
                                                    Toast.makeText(ProfilePictureSignup.this, "Signup Failed", Toast.LENGTH_SHORT).show();
                                                    System.out.println("Document upload failed");
                                                    ContinueButt.setEnabled(true);
                                                }
                                            });
                                }
                            });
                        }
                    });







                }
                else {
                    Toast.makeText(ProfilePictureSignup.this, "Signup Failed", Toast.LENGTH_SHORT).show();
                    System.out.println("password login failed");
                    ContinueButt.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }

    private void Initialization() {
        toolbar = findViewById(R.id.ProfilePictureToolBar);
        ContinueButt = findViewById(R.id.Continue_Butt_ProfilePicture);
        SelectPhotoButton = findViewById(R.id.ProfilePicSelectButt);
        profilePicture = findViewById(R.id.ProfilePicSelection);
        SelectPhotoText = findViewById(R.id.buttTextPPselector);
        CamIcon = findViewById(R.id.iconPPselector);
        IWillDoItLater = findViewById(R.id.textView);

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
                SelectPhotoText.setText("Change Photo");








            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
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
            Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }


    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
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

















    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }


}