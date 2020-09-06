package com.cproz.pantomath.Upload;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cproz.pantomath.Home.Home;
import com.cproz.pantomath.Home.HomeFragment;
import com.cproz.pantomath.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jackandphantom.circularimageview.RoundedImage;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class UploadImagePage extends AppCompatActivity {


    public static int FLAG = UploadImagePage.FLAG;
    Button  SubjTag, ConfirmDoubt, CancelImg1, CancelImg2;
    TextView AddPhoto1, AddPhoto2;
    EditText QuestionText;
    RoundedImage Image1, Image2;
    String decision;
    Uri mCropImageUri, uriImg1, uriImg2;
    int fl1 = 0, fl2 = 0;
    View root;
    ConstraintLayout constraintLayout;
    Toolbar toolbar;
    ProgressBar progressBar;

    public String Name, Email, AnsPhotoUrl1 = "", AnsPhotoUrl2 = "", AnsText = "", AudioUrl = "", Chapter, FileUrl = "", Link = "",
            Photo1url = "", Photo2url = "", ProfileImageURL = "", Status,
            Text, Board, STD, Uid, Subject, Teacher = "", Created, DateTime;

    public FirebaseStorage storage;
    public StorageReference storageReference;

    private FirebaseFirestore fireDB;
    ConstraintLayout constFullscreen;
    LinearLayout FullScreenBgButt;
    Button crossProf;

    public DocumentReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_image_page);

        Initialise();


        constFullscreen = findViewById(R.id.constFullscreen);
        FullScreenBgButt = findViewById(R.id.FullScreenBgButt);


        constFullscreen.setVisibility(View.GONE);



        progressBar.setVisibility(View.GONE);

        ConfirmDoubt.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                ConfirmDoubt.setEnabled(false);

                validations();
            }
        });

        //Toast.makeText(this, UploadFragment.SUBJECT, Toast.LENGTH_SHORT).show();

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);

        SubjTag.setText(UploadFragment.SUBJECT);

        switch (UploadFragment.SUBJECT){
            case "Algebra":

                colorChange(R.drawable.doubt_card_bg, R.drawable.subject_button_bg, Color.parseColor("#FF2829"));
                break;

            case "Geometry":
                colorChange(R.drawable.doubt_card_bg_geom, R.drawable.subject_button_bg_geom, Color.parseColor("#9A0D91"));
                break;

            case "Physics":
                colorChange(R.drawable.doubt_card_bg_physics, R.drawable.subject_button_bg_phy, Color.parseColor("#0078FF"));
                break;


            case "Chemistry":
                colorChange(R.drawable.doubt_card_bg_chem, R.drawable.subject_button_bg_chem, Color.parseColor("#FF9B00"));
                break;


            case "Biology":
                colorChange(R.drawable.doubt_card_bg_bio, R.drawable.subject_button_bg_bio, Color.parseColor("#FF1ADD"));
                break;


            case "History":
                colorChange(R.drawable.doubt_card_bg_his, R.drawable.subject_button_bg_his, Color.parseColor("#813912"));
                break;


            case "Geography":
                colorChange(R.drawable.doubt_card_bg_geog, R.drawable.subject_button_bg_geog, Color.parseColor("#009F37"));
                break;


            case "English":
                SubjTag.setText(UploadFragment.SUBJECT);
                colorChange(R.drawable.doubt_card_bg_lang, R.drawable.subject_button_bg_lang, Color.parseColor("#5550B6"));
                break;

            case "Hindi":
                SubjTag.setText(UploadFragment.SUBJECT);
                colorChange(R.drawable.doubt_card_bg_lang, R.drawable.subject_button_bg_lang, Color.parseColor("#5550B6"));
                break;

            case "Marathi":
                SubjTag.setText(UploadFragment.SUBJECT);
                colorChange(R.drawable.doubt_card_bg_lang, R.drawable.subject_button_bg_lang, Color.parseColor("#5550B6"));
                break;


            case "Sanskrit":
                SubjTag.setText(UploadFragment.SUBJECT);
                colorChange(R.drawable.doubt_card_bg_lang, R.drawable.subject_button_bg_lang, Color.parseColor("#5550B6"));
                break;


            case "French":
                SubjTag.setText(UploadFragment.SUBJECT);
                colorChange(R.drawable.doubt_card_bg_lang, R.drawable.subject_button_bg_lang, Color.parseColor("#5550B6"));
                break;


        }




        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        FLAG = 0;

        CancelImg1.setVisibility(View.GONE);
        CancelImg2.setVisibility(View.GONE);




        Image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fl1 = 1;
                fl2 = 0;
                onSelectImageClick(v);
                uriImg1 = mCropImageUri;

            }
        });

        Image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (uriImg1==null){
                    Toast.makeText(UploadImagePage.this, "Add First Image", Toast.LENGTH_SHORT).show();
                }else{
                    fl2 = 1;
                    fl1 = 0;
                    onSelectImageClick(v);
                    uriImg2 = mCropImageUri;
                }

            }
        });



        CancelImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (AddPhoto1.getVisibility() == View.GONE && AddPhoto2.getVisibility() == View.GONE){




                    uriImg1 = uriImg2;
                    uriImg2 = null;
                    Image1.setImageURI(uriImg1);
                    AddPhoto2.setVisibility(View.VISIBLE);


                    Image2.setImageResource(R.drawable.image_view_bg);
                    AddPhoto2.setVisibility(View.VISIBLE);

                    CancelImg2.setVisibility(View.GONE);

                }

                else if (AddPhoto1.getVisibility() == View.GONE && AddPhoto2.getVisibility() == View.VISIBLE){
                    Image1.setImageResource(R.drawable.image_view_bg);

                    AddPhoto2.setVisibility(View.VISIBLE);

                    CancelImg1.setVisibility(View.GONE);

                    AddPhoto1.setVisibility(View.VISIBLE);
                    uriImg1 = null;
                    uriImg2 = null;
                }




            }
        });

        CancelImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Image2.setImageResource(R.drawable.image_view_bg);
                //doubtImage2.setVisibility(View.GONE);
                AddPhoto2.setVisibility(View.VISIBLE);

                //imageCancel1.setVisibility(View.GONE);
                CancelImg2.setVisibility(View.GONE);


                uriImg2 = null;



            }
        });


    }

    public void colorChange(int consBG, int buttBg, int textColor){
        constraintLayout.setBackgroundResource(consBG);
        SubjTag.setBackgroundResource(buttBg);
        SubjTag.setTextColor(textColor);
    }


    public void Initialise(){


        SubjTag = findViewById(R.id.SubjectTagUpload);
        ConfirmDoubt = findViewById(R.id.ConfirmDoubtUpload);
        CancelImg1 = findViewById(R.id.Image1cancel_button);
        CancelImg2 = findViewById(R.id.Image2cancel_button);
        AddPhoto1 = findViewById(R.id.addPhoto1);
        AddPhoto2 = findViewById(R.id.addPhoto2);
        QuestionText = findViewById(R.id.QuestionUploadText);
        Image1 = findViewById(R.id.DoubtUploadImage1);
        Image2 = findViewById(R.id.DoubtUploadImage2);
        constraintLayout = findViewById(R.id.UploadImageCard);
        progressBar = findViewById(R.id.progressBarUpload);
        toolbar = findViewById(R.id.AddDoubtToolBar);

    }


    // Image ka lochyaa


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
        CropImage.activity().start(UploadImagePage.this);


    }




    @Override
    @SuppressLint({"NewApi", "SetTextI18n"})
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of pick image chooser
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(Objects.requireNonNull(UploadImagePage.this), data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(UploadImagePage.this, imageUri)) {
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

                //profilePicture.setImageURI(mCropImageUri);
                //SelectPhotoText.setText("Change Photo");



                if (fl1 == 1 && fl2 == 0) {

                    uriImg1 = mCropImageUri;
                    Image1.setImageURI(mCropImageUri);
                    AddPhoto1.setVisibility(View.GONE);
                    System.out.println(mCropImageUri);

                    //Image1.setBackgroundColor(Color.parseColor("#00FFFFFF"));


                    CancelImg1.setVisibility(View.VISIBLE);





                } else if (fl2 == 1 && fl1 == 0) {


                    uriImg2 = mCropImageUri;
                    Image2.setImageURI(uriImg2);
                    AddPhoto2.setVisibility(View.GONE);

                    //Image2.setBackgroundColor(Color.parseColor("#00FFFFFF"));

                    System.out.println(mCropImageUri);
                    CancelImg2.setVisibility(View.VISIBLE);






                }







            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(UploadImagePage.this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
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
            Toast.makeText(UploadImagePage.this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }


    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(UploadImagePage.this);
    }


    public void UploadToFirebase() throws IOException {


        fireDB = FirebaseFirestore.getInstance();
        DocumentReference doubt = fireDB.collection("Doubts").document();
        Uid = doubt.getId();



        if (uriImg1 == null && uriImg2 == null){

            Photo1url = "";
            Photo2url = "";
            UploadToFirestore(Uid);

            Timer timer;
            timer = new Timer();
            constFullscreen.setVisibility(View.VISIBLE);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    startActivity(new Intent(UploadImagePage.this, Home.class ));
                }
            }, 2500);

//            DoubtUploadedPopUp doubtUploadedPopUp = new DoubtUploadedPopUp();
//            doubtUploadedPopUp.show(getSupportFragmentManager(), "fra");


        }

        else if (uriImg1 != null && uriImg2 != null){

            final StorageReference reference1 = storageReference.child("Doubts/" + Uid + "/DoubtImg1.jpg");
            final StorageReference reference2 = storageReference.child("Doubts/" + Uid + "/DoubtImg2.jpg");



            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uriImg1);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos);
            byte[] data = baos.toByteArray();


            Bitmap bitmapx = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uriImg2);
            ByteArrayOutputStream baosx = new ByteArrayOutputStream();
            bitmapx.compress(Bitmap.CompressFormat.JPEG, 75, baosx);
            final byte[] data2 = baosx.toByteArray();




            reference1.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Photo1url = uri.toString();


                            reference2.putBytes(data2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    reference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri urix) {
                                            Photo2url = urix.toString();
                                            UploadToFirestore(Uid);
                                            Timer timer;
                                            timer = new Timer();
                                            constFullscreen.setVisibility(View.VISIBLE);
                                            timer.schedule(new TimerTask() {
                                                @Override
                                                public void run() {
                                                    startActivity(new Intent(UploadImagePage.this, Home.class ));
                                                }
                                            }, 2500);
                                        }
                                    });

                                    //Toast.makeText(UploadImagePage.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    constFullscreen.setVisibility(View.GONE);
                                    Toast.makeText(UploadImagePage.this, "Failed to Upload Image 1", Toast.LENGTH_SHORT).show();
                                    ConfirmDoubt.setEnabled(true);
                                    progressBar.setVisibility(View.GONE);
                                }
                            });


                        }
                    });

                    Toast.makeText(UploadImagePage.this, "Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadImagePage.this, "Failed to Upload Image 1", Toast.LENGTH_SHORT).show();
                    ConfirmDoubt.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                }
            });






        }

        else if (uriImg1 != null && uriImg2 == null){

            final StorageReference reference1 = storageReference.child("Doubts/" + Uid + "/DoubtImg1.jpg");



            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uriImg1);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos);
            byte[] data = baos.toByteArray();





            reference1.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Photo2url = "";
                            Photo1url = uri.toString();
                            UploadToFirestore(Uid);
                            Timer timer;
                            timer = new Timer();
                            constFullscreen.setVisibility(View.VISIBLE);
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(UploadImagePage.this, Home.class ));
                                }
                            }, 2500);
                        }
                    });

                    //Toast.makeText(UploadImagePage.this, "Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    constFullscreen.setVisibility(View.GONE);
                    ConfirmDoubt.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UploadImagePage.this, "Failed to Upload Image 1", Toast.LENGTH_SHORT).show();
                }
            });



        }



    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void validations(){
        String text = QuestionText.getText().toString();
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(100, true);

        if (text.isEmpty()){
            QuestionText.setError("Question is mandatory");
            QuestionText.requestFocus();
            ConfirmDoubt.setEnabled(true);
            progressBar.setVisibility(View.GONE);
        }
        else{
            try {
                UploadToFirebase();
                //DoubtUploadedPopUp doubtUploadedPopUp = new DoubtUploadedPopUp();
                //doubtUploadedPopUp.show(getSupportFragmentManager(), "fra");
                //startActivity(new Intent(UploadImagePage.this,Home.class ));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void UploadToFirestore(String Uid){


        Chapter = UploadFragment.CHAPTER;
        Subject = UploadFragment.SUBJECT;
        Name = UploadFragment.NAME;
        Email = UploadFragment.EMAIL;
        Board = HomeFragment.BOARD;
        STD = HomeFragment.CLASS;
        ProfileImageURL = HomeFragment.PROFILEURL;

        Text = QuestionText.getText().toString().trim();

        DocumentReference doubt = fireDB.collection("Doubts/").document(Uid);


        Status = "Unsolved";


        Created = "True";
        Date date = new Date();

        //DateTime = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        DoubtCarrier studentDoubtCarrier = new DoubtCarrier(

                Name, Email, AnsPhotoUrl1, AnsPhotoUrl2, AnsText, AudioUrl,Chapter, FileUrl,Link,
                Photo1url, Photo2url, ProfileImageURL, "Unsolved", Text, Board, STD, Uid,Subject,Teacher, "True", date,
                "",date,""



        );

        doubt.set(studentDoubtCarrier).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressBar.setVisibility(View.GONE);
                //Toast.makeText(UploadImagePage.this, "Document Uploaded successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                constFullscreen.setVisibility(View.GONE);
                ConfirmDoubt.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(UploadImagePage.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onBackPressed() {
        FLAG = 1;
        super.onBackPressed();
    }




}