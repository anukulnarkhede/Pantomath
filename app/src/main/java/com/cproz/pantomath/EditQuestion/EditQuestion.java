package com.cproz.pantomath.EditQuestion;

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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cproz.pantomath.Home.Home;
import com.cproz.pantomath.R;
import com.cproz.pantomath.Upload.UploadFragment;
import com.cproz.pantomath.Upload.UploadImagePage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jackandphantom.circularimageview.RoundedImage;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class EditQuestion extends AppCompatActivity {

    Button SubjTag, ConfirmDoubt, CancelImg1, CancelImg2;
    TextView AddPhoto1, AddPhoto2;
    EditText QuestionText;
    RoundedImage Image1, Image2;
    String decision;
    Uri mCropImageUri, uriImg1, uriImg2;
    int fl1 = 0, fl2 = 0;
    View root;
    ConstraintLayout constraintLayout;
    Toolbar toolbar;
    Button progressBar;
    public FirebaseStorage storage;
    public StorageReference storageReference;
    private FirebaseFirestore fireDB, firebaseFirestore;
    ConstraintLayout constFullscreen;
    LinearLayout FullScreenBgButt;
    Button crossProf;
String Image1Url, Image2Url, questionText;
    public DocumentReference ref;

    public String Name, Email,
            Photo1url = "", Photo2url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_question);
        Initialise();
        firebaseFirestore = FirebaseFirestore.getInstance();
        fireDB = FirebaseFirestore.getInstance();

        CancelImg1.setVisibility(View.GONE);
        CancelImg2.setVisibility(View.GONE);
        constFullscreen.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        final Bundle bundle = getIntent().getExtras();
        assert bundle != null;

        Image1Url = bundle.getString("QuestionImage1Url");
        Image2Url = bundle.getString("QuestionImage2Url");


        switch (Objects.requireNonNull(bundle.getString("Subject"))){
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




        if (!Objects.equals(Image1Url, "") && !Objects.equals(Image2Url, "")){
            Picasso.get().load(bundle.getString("QuestionImage1Url")).into(Image1);
            Picasso.get().load(bundle.getString("QuestionImage2Url")).into(Image2);
            CancelImg1.setVisibility(View.VISIBLE);
            CancelImg2.setVisibility(View.VISIBLE);
            AddPhoto1.setVisibility(View.GONE);
            AddPhoto2.setVisibility(View.GONE);
        }
        else if (!Objects.equals(Image1Url, "") && Objects.equals(Image2Url, "")){
            Picasso.get().load(bundle.getString("QuestionImage1Url")).into(Image1);
            Image2.setImageResource(R.drawable.image_view_bg);
            CancelImg1.setVisibility(View.VISIBLE);
            CancelImg2.setVisibility(View.GONE);
            AddPhoto1.setVisibility(View.GONE);
            AddPhoto2.setVisibility(View.VISIBLE);
        }
        else if (Objects.equals(Image1Url, "") && Objects.equals(Image2Url, "")){

            Image1.setImageResource(R.drawable.image_view_bg);
            Image2.setImageResource(R.drawable.image_view_bg);
            CancelImg1.setVisibility(View.GONE);
            CancelImg2.setVisibility(View.GONE);
            AddPhoto1.setVisibility(View.VISIBLE);
            AddPhoto2.setVisibility(View.VISIBLE);
        }

        QuestionText.setText(bundle.getString("QuestionText"));

        ConfirmDoubt.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                ConfirmDoubt.setEnabled(false);

                validations(bundle);
            }
        });

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


                if (uriImg1==null && Image1Url.equals("")){
                    Toast.makeText(EditQuestion.this, "Add First Image", Toast.LENGTH_SHORT).show();
                }

                else
                    {
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

                    Image1Url = Image2Url;
                    Image2Url = "";


                    if (!Image1Url.equals("")){
                        Picasso.get().load(Image1Url).into(Image1);
                    }

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
                    Image1Url = "";
                    Image2Url = "";
                    //Toast.makeText(EditQuestion.this, Image2Url + Image1Url + "Gone", Toast.LENGTH_SHORT).show();
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

                Image2Url = "";




            }
        });

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
        constFullscreen = findViewById(R.id.constFullscreen);
        FullScreenBgButt = findViewById(R.id.FullScreenBgButt);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

    }


    public void colorChange(int consBG, int buttBg, int textColor){
        constraintLayout.setBackgroundResource(consBG);
        SubjTag.setBackgroundResource(buttBg);
        SubjTag.setTextColor(textColor);
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
        CropImage.activity().start(EditQuestion.this);


    }




    @Override
    @SuppressLint({"NewApi", "SetTextI18n"})
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of pick image chooser
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(Objects.requireNonNull(EditQuestion.this), data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(EditQuestion.this, imageUri)) {
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
                Toast.makeText(EditQuestion.this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
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
            Toast.makeText(EditQuestion.this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }


    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(EditQuestion.this);
    }


    public void UpdateQuestToFirestore(Bundle bundle){

        Date QuestionDate = new Date();
        questionText = QuestionText.getText().toString();

        firebaseFirestore.collection("Doubts/")
                .document(Objects.requireNonNull(bundle.getString("Uid")))
                .update("QuestionDate",QuestionDate, "DateTime", QuestionDate,
                        "QText", questionText, "Photo1url", Photo1url, "Photo2url", Photo2url)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    public void UpdateQuestion(final Bundle bundle) throws IOException {

        if (uriImg1 == null && uriImg2 == null){

            Photo1url = Image1Url;
            Photo2url = Image2Url;
            UpdateQuestToFirestore(bundle);

            Timer timer;
            timer = new Timer();
            constFullscreen.setVisibility(View.VISIBLE);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    startActivity(new Intent(EditQuestion.this, Home.class ));
                }
            }, 2500);

//            DoubtUploadedPopUp doubtUploadedPopUp = new DoubtUploadedPopUp();
//            doubtUploadedPopUp.show(getSupportFragmentManager(), "fra");


        }

        else if (uriImg1 != null && uriImg2 != null){

            final StorageReference reference1 = storageReference.child("Doubts/" + bundle.getString("Uid") + "/DoubtImg1.jpg");
            final StorageReference reference2 = storageReference.child("Doubts/" + bundle.getString("Uid") + "/DoubtImg2.jpg");



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
                                            UpdateQuestToFirestore(bundle);
                                            Timer timer;
                                            timer = new Timer();
                                            constFullscreen.setVisibility(View.VISIBLE);
                                            timer.schedule(new TimerTask() {
                                                @Override
                                                public void run() {
                                                    startActivity(new Intent(EditQuestion.this, Home.class ));
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
                                    Toast.makeText(EditQuestion.this, "Failed to Upload Image 1", Toast.LENGTH_SHORT).show();
                                    ConfirmDoubt.setEnabled(true);
                                    progressBar.setVisibility(View.GONE);
                                }
                            });


                        }
                    });

                    Toast.makeText(EditQuestion.this, "Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditQuestion.this, "Failed to Upload Image 1", Toast.LENGTH_SHORT).show();
                    ConfirmDoubt.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                }
            });






        }

        else if (uriImg1 != null && uriImg2 == null){


            final StorageReference reference1 = storageReference.child("Doubts/" + bundle.getString("Uid") + "/DoubtImg1.jpg");



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
                            UpdateQuestToFirestore(bundle);
                            Timer timer;
                            timer = new Timer();
                            constFullscreen.setVisibility(View.VISIBLE);
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(EditQuestion.this, Home.class ));
                                }
                            }, 2000);
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
                    Toast.makeText(EditQuestion.this, "Failed to Upload Image 1", Toast.LENGTH_SHORT).show();
                }
            });



        }

        else if (uriImg1 == null && uriImg2 != null){
            final StorageReference reference1 = storageReference.child("Doubts/" + bundle.getString("Uid") + "/DoubtImg2.jpg");



            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uriImg2);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos);
            byte[] data = baos.toByteArray();





            reference1.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Photo1url = bundle.getString("QuestionImage1Url");
                            Photo2url = uri.toString();
                            UpdateQuestToFirestore(bundle);
                            Timer timer;
                            timer = new Timer();
                            constFullscreen.setVisibility(View.VISIBLE);
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(EditQuestion.this, Home.class ));
                                }
                            }, 2000);
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
                    Toast.makeText(EditQuestion.this, "Failed to Upload Image ", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    public void validations(Bundle bundle){
        String text = QuestionText.getText().toString();
        progressBar.setVisibility(View.VISIBLE);
//        progressBar.setProgress(100, true);

        if (text.isEmpty()){
            QuestionText.setError("Question is mandatory");
            QuestionText.requestFocus();
            ConfirmDoubt.setEnabled(true);
            progressBar.setVisibility(View.GONE);
        }
        else{
            try {
                UpdateQuestion(bundle);
                //DoubtUploadedPopUp doubtUploadedPopUp = new DoubtUploadedPopUp();
                //doubtUploadedPopUp.show(getSupportFragmentManager(), "fra");
                //startActivity(new Intent(UploadImagePage.this,Home.class ));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}