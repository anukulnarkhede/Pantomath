package com.cproz.pantomath.Upload;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cproz.pantomath.Home.HomeDoubtData;
import com.cproz.pantomath.Home.HomeFragment;
import com.cproz.pantomath.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.errorprone.annotations.SuppressPackageLocation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.UploadTask;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class UploadFragment extends Fragment {


    RelativeLayout relativeLayout;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    String email = user != null ? user.getEmail() : null;
    private DocumentReference ref = firebaseFirestore.collection("Users/Students/StudentsInfo/" ).document(String.valueOf(email));
    String StudentClass = null, StudentBoard = null;
    public static String SUBJECT = UploadFragment.SUBJECT, CHAPTER = UploadFragment.CHAPTER, NAME = UploadFragment.NAME, EMAIL = UploadFragment.EMAIL;
    Toolbar toolbar;
    TextView selectSubject;
    RecyclerView recyclerView;
    private String url1;
    private List<HomeDoubtData> DoubtList1;
    private HomeDoubtData homeDoubtData;
    private FirebaseFirestore db;
    private TextView text;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.upload_fragment, container, false);


        relativeLayout = root.findViewById(R.id.buttonPlaceHolder);
        selectSubject = root.findViewById(R.id.selectSubject);

        db = FirebaseFirestore.getInstance();
        recyclerView = root.findViewById(R.id.smartSuggestionRecyclerView);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        DoubtList1 = new ArrayList<>();


        //Timer timer = new Timer();

        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    StudentClass  = documentSnapshot.getString("Class");
                    StudentBoard = documentSnapshot.getString("Board");
                    UploadFragment.NAME = documentSnapshot.getString("Name");
                    UploadFragment.EMAIL = documentSnapshot.getString("Email");
                    email = documentSnapshot.getString("Email");
                    loadDataFromFirebase();



                }
                else{
                    System.out.println("does not exist");
                }
            }
        });






        //mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        //loadDataFromFirebase();


        subjectFilter(root);


        //loadDataFromFirebase();
        //loadDataFromFirebase();





        //loadDataFromFirebase();




        return root;



    }

    public void layoutInflat(int LayID){

        relativeLayout.removeAllViews();
        View subjectButtns = LayoutInflater.from(getContext()).inflate(LayID, relativeLayout, false);
        RelativeLayout.LayoutParams ch9c = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        subjectButtns.setLayoutParams(ch9c);
        relativeLayout.addView(subjectButtns);

    }

    @SuppressLint("SetTextI18n")
    public void subjectFilter(final View root){
        selectSubject.setText("Select Subject");
        layoutInflat(R.layout.subject_buttons);
        final LinearLayoutCompat algebraFilter, geometryFilter, physicsFilter, chemistryFilter, biologyFilter, historyFilter, geographyFilter, languagesFilter;
        algebraFilter = root.findViewById(R.id.AlgebraFilter);
        geometryFilter = root.findViewById(R.id.GeometryFilter);
        physicsFilter = root.findViewById(R.id.PhysicsFilter);
        chemistryFilter = root.findViewById(R.id.ChemistryFilter);
        biologyFilter = root.findViewById(R.id.BiologyFilter);
        historyFilter = root.findViewById(R.id.HistoryFilter);
        geographyFilter = root.findViewById(R.id.GeographyFilter);
        languagesFilter = root.findViewById(R.id.LanguagesFilter);


        algebraFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SUBJECT = "Algebra";
                DoubtList1.clear();
                loadDataFromFirebaseSubject();
                if (StudentBoard == null){
                    System.out.println("Board does not exist");
                }
                else if (StudentBoard.equals("SSC")){
                    if (StudentClass == null){
                        System.out.println("Class does not exist");
                    }
                    else if (StudentClass.equals("9th")){
                        chapterFilters(root,"Sets","Real Numbers","Polynomials","Ratio and Proportion",
                                "Linear Equation in Two Variable","Financial Planning","Statistics","h","i","j",
                                "k","l"
                                ,View.VISIBLE, View.VISIBLE,View.VISIBLE, View.VISIBLE, View.VISIBLE,
                                View.VISIBLE, View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE,View.GONE);


                    }
                    else if (StudentClass.equals("10th")){

                        chapterFilters(root,"Linear Equation in Two Variable","Quadratic Equation","Arithmetic Progression","Financial Planning",
                                "Probability","Statistics","g","h","i","j",
                                "k","l"
                                ,View.VISIBLE, View.VISIBLE,View.VISIBLE, View.VISIBLE, View.VISIBLE,
                                View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,View.GONE);

                    }
                }
                else if (StudentBoard.equals("CBSE")){
                    if (StudentClass == null){
                        System.out.println("Class does not exist");
                    }
                    else if (StudentClass.equals("9th")){

                        chapterFilters(root,"Number System","Polynomials","Linear Equation in Two Variables","Statistics",
                                "Probability","x","x","h","i","j",
                                "k","l"
                                ,View.VISIBLE , View.VISIBLE , View.VISIBLE, View.VISIBLE, View.VISIBLE,
                                View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,View.GONE);

                    }
                    else if (StudentClass.equals("10th")){


                        chapterFilters(root,"Real Numbers","Polynomials","Pair of Linear Equation in Two Variables","Statistics",
                                "Quadratic Equation","Arithmetic Progression","Probability","h","i","j",
                                "k","l"
                                ,View.VISIBLE , View.VISIBLE , View.VISIBLE, View.VISIBLE, View.VISIBLE,
                                View.VISIBLE, View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE,View.GONE);

                    }
                }//algebra
                else if (StudentBoard.equals("ICSE")){
                    if (StudentClass == null){
                        System.out.println("Class does not exist");
                    }
                    else if (StudentClass.equals("9th")){

                        chapterFilters(root,"Pure Arithmetic","Commercial Mathematics","Expansion","Factorisation",
                                "Simultaneous linear equation in 2 variable","Indices and Exponents","Statistics","h","i","j",
                                "k","l"
                                ,View.VISIBLE , View.VISIBLE , View.VISIBLE, View.VISIBLE, View.VISIBLE,
                                View.VISIBLE, View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE,View.GONE);

                    }
                    else if (StudentClass.equals("10th")){


                        chapterFilters(root,"Compound Interest","Sales Tax & Value Added Tax","Banking","Shares & Dividends",
                                "Linear in Equations","Quadratic Equation","Reflection","Ration & Proportion","Factorisation","Matrices",
                                "Co-ordinate Geometry","Probability"
                                ,View.VISIBLE , View.VISIBLE , View.VISIBLE, View.VISIBLE, View.VISIBLE,
                                View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE,View.VISIBLE);

                    }
                }

            }
        });


        geometryFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SUBJECT = "Geometry";
                DoubtList1.clear();
                loadDataFromFirebaseSubject();
                if (StudentBoard == null){
                    System.out.println("Board does not exist");
                }
                else if (StudentBoard.equals("SSC")){
                    if (StudentClass == null){
                        System.out.println("Class does not exist");
                    }
                    else if (StudentClass.equals("9th")){
                        chapterFilters(root,"Basic Concepts in Geometry","Parallel Lines","Triangles","Construction of Triangles",
                                "Quadrilaterals","Circle","Co-ordinate Geometry","Trigonometry","Surface Area and Volume","j",
                                "k","l"
                                ,View.VISIBLE, View.VISIBLE,View.VISIBLE, View.VISIBLE, View.VISIBLE,
                                View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.GONE, View.GONE,View.GONE);


                    }
                    else if (StudentClass.equals("10th")){

                        chapterFilters(root,"Similarity","Pythagoras Theorem","Circle","Geometric Construction",
                                "Co-ordinate Geometry","Trigonometry","Mensuration","h","i","j",
                                "k","l"
                                ,View.VISIBLE, View.VISIBLE,View.VISIBLE, View.VISIBLE, View.VISIBLE,
                                View.VISIBLE, View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE,View.GONE);

                    }
                }
                else if (StudentBoard.equals("CBSE")){
                    if (StudentClass == null){
                        System.out.println("Class does not exist");
                    }
                    else if (StudentClass.equals("9th")){

                        chapterFilters(root,"Introduction to Euclid's Geometry","Lines and Angles","Triangles","Coordinate Geometry",
                                "Heron's Theorem","Quadrilaterals","Area of Parallelograms and Triangles","Circles","Construction","Surface Area and Volumes",
                                "k","l"
                                ,View.VISIBLE , View.VISIBLE , View.VISIBLE, View.VISIBLE, View.VISIBLE,
                                View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.GONE,View.GONE);

                    }
                    else if (StudentClass.equals("10th")){

                        chapterFilters(root,"Triangles","Introduction to Trigonometry","Circles","Construction",
                                "Some Applications of Trigonometry","Coordinate Geometry","Area Related to Circles","Surface Area and Volume","i","j",
                                "k","l"
                                ,View.VISIBLE , View.VISIBLE , View.VISIBLE, View.VISIBLE, View.VISIBLE,
                                View.VISIBLE, View.VISIBLE, View.VISIBLE, View.GONE, View.GONE, View.GONE,View.GONE);

                    }
                }//Geometry
                else if (StudentBoard.equals("ICSE")){
                    if (StudentClass == null){
                        System.out.println("Class does not exist");
                    }
                    else if (StudentClass.equals("9th")){

                        chapterFilters(root,"Triangles","Rectilinear Figures","Circle","Mensuration",
                                "Trigonometry","Coordinate Geometry","c","c","c","c",
                                "k","l"
                                ,View.VISIBLE , View.VISIBLE , View.VISIBLE, View.VISIBLE, View.VISIBLE,
                                View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,View.GONE);

                    }
                    else if (StudentClass.equals("10th")){

                        chapterFilters(root,"Symmetry","Similarity","Loci","Circles",
                                "Construction","Mensuration","Trigonometry","Statistics","i","j",
                                "k","l"
                                ,View.VISIBLE , View.VISIBLE , View.VISIBLE, View.VISIBLE, View.VISIBLE,
                                View.VISIBLE, View.VISIBLE, View.VISIBLE, View.GONE, View.GONE, View.GONE,View.GONE);

                    }
                }


            }
        });



        physicsFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SUBJECT = "Physics";
                DoubtList1.clear();
                loadDataFromFirebaseSubject();
                if (StudentBoard == null){
                    System.out.println("Board does not exist");
                }
                else if (StudentBoard.equals("SSC")){
                    if (StudentClass == null){
                        System.out.println("Class does not exist");
                    }
                    else if (StudentClass.equals("9th")){
                        chapterFilters(root,"Laws of Motion","Work and Energy","Current Electricity","ICT",
                                "Reflection of Light","Study of Sound","Observing Space : Telescopes","q","q","j",
                                "k","l"
                                ,View.VISIBLE, View.VISIBLE,View.VISIBLE, View.VISIBLE, View.VISIBLE,
                                View.VISIBLE, View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE,View.GONE);


                    }
                    else if (StudentClass.equals("10th")){

                        chapterFilters(root,"Gravitation","Effects of electric current","Heat","Refraction of light",
                                "Lenses","Space Missions","b","h","i","j",
                                "k","l"
                                ,View.VISIBLE, View.VISIBLE,View.VISIBLE, View.VISIBLE, View.VISIBLE,
                                View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,View.GONE);

                    }
                }
                else if (StudentBoard.equals("CBSE")){
                    if (StudentClass == null){
                        System.out.println("Class does not exist");
                    }
                    else if (StudentClass.equals("9th")){

                        chapterFilters(root,"Motion","Force and Laws of Motion","Gravitation","Work and Energy",
                                "Sound","m","x","h","i","j",
                                "k","l"
                                ,View.VISIBLE, View.VISIBLE,View.VISIBLE, View.VISIBLE, View.VISIBLE,
                                View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,View.GONE);

                    }
                    else if (StudentClass.equals("10th")){


                        chapterFilters(root,"Electricity","Magnetic Effects of Electric Current","Sources of Energy","Light: Reflection and Refraction",
                                "b","m","x","h","i","j",
                                "k","l"
                                ,View.VISIBLE, View.VISIBLE,View.VISIBLE, View.VISIBLE, View.GONE,
                                View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,View.GONE);

                    }//Physics
                }
                else if (StudentBoard.equals("ICSE")){
                    if (StudentClass == null){
                        System.out.println("Class does not exist");
                    }
                    else if (StudentClass.equals("9th")){

                        chapterFilters(root,"Measurement & Experimentation","Motion in One Direction","Laws of Motion","Fluids",
                                "Heat & Energy","Light","Sound","Electricity & Magnetism","i","j",
                                "k","l"
                                ,View.VISIBLE, View.VISIBLE,View.VISIBLE, View.VISIBLE, View.VISIBLE,
                                View.VISIBLE, View.VISIBLE, View.VISIBLE, View.GONE, View.GONE, View.GONE,View.GONE);

                    }
                    else if (StudentClass.equals("10th")){


                        chapterFilters(root,"Force, Work, Power & Energy","Light","Sound","Electricity & Magnetism",
                                "Heat","Modern Physics","x","h","i","j",
                                "k","l"
                                ,View.VISIBLE, View.VISIBLE,View.VISIBLE, View.VISIBLE, View.VISIBLE,
                                View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,View.GONE);

                    }//Physics
                }

            }
        });





        chemistryFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SUBJECT = "Chemistry";
                DoubtList1.clear();
                loadDataFromFirebaseSubject();
                if (StudentBoard == null){
                    System.out.println("Board does not exist");
                }
                else if (StudentBoard.equals("SSC")){
                    if (StudentClass == null){
                        System.out.println("Class does not exist");
                    }
                    else if (StudentClass.equals("9th")){
                        chapterFilters(root,"Measurement of Matter","Acids, Bases and Salts","Carbon : An important element","Substances in Common Use",
                                "x","x","x","q","q","j",
                                "k","l"
                                ,View.VISIBLE, View.VISIBLE,View.VISIBLE, View.VISIBLE, View.GONE,
                                View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,View.GONE);


                    }
                    else if (StudentClass.equals("10th")){

                        chapterFilters(root,"Periodic Classification of Element","Chemical reactions and equations","Metallurgy","Carbon compounds",
                                "x","x","x","h","i","j",
                                "k","l"
                                ,View.VISIBLE, View.VISIBLE,View.VISIBLE, View.VISIBLE, View.GONE,
                                View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,View.GONE);

                    }
                }
                else if (StudentBoard.equals("CBSE")){
                    if (StudentClass == null){
                        System.out.println("Class does not exist");
                    }
                    else if (StudentClass.equals("9th")){

                        chapterFilters(root,"Matter in Our Surrounding","Is Matter Around us Pure","Atoms and Molecules","Structure of Atoms",
                                "x","x","x","h","i","j",
                                "k","l"
                                ,View.VISIBLE, View.VISIBLE,View.VISIBLE, View.VISIBLE, View.VISIBLE,
                                View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,View.GONE);

                    }
                    else if (StudentClass.equals("10th")){

                        chapterFilters(root,"Chemical Reactions and Equation","Acids, Bases and Salts","Metals and Non-Metals","Carbon and its Compounds",
                                "Periodic Classification of Element","n","x","h","i","j",
                                "k","l"
                                ,View.VISIBLE, View.VISIBLE,View.VISIBLE, View.VISIBLE, View.VISIBLE,
                                View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,View.GONE);

                    }
                }//Chemistry
                else if (StudentBoard.equals("ICSE")){
                    if (StudentClass == null){
                        System.out.println("Class does not exist");
                    }
                    else if (StudentClass.equals("9th")){

                        chapterFilters(root,"The Language of Chemistry","Chemical Changes and Reactions","Water","Atomic Structure and Chemical Bonding",
                                "The Periodic Table","Study of First element - Hydrogen","Study of Gas Laws","h","i","j",
                                "k","l"
                                ,View.VISIBLE, View.VISIBLE,View.VISIBLE, View.VISIBLE, View.VISIBLE,
                                View.VISIBLE, View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE,View.GONE);

                    }
                    else if (StudentClass.equals("10th")){

                        chapterFilters(root,"Periodic Properties and Variation of Properties- Physical & Chemical","Chemical Bonding","Student of Acids, Bases & Salts","Analytical Chemistry",
                                "Mole Concept and Stoichiometry","Electrolysis","Metallurgy","Study of Compounds","Organic Chemistry","j",
                                "k","l"
                                ,View.VISIBLE, View.VISIBLE,View.VISIBLE, View.VISIBLE, View.VISIBLE,
                                View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.GONE, View.GONE,View.GONE);

                    }
                }

            }
        });





        biologyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SUBJECT = "Biology";
                DoubtList1.clear();
                loadDataFromFirebaseSubject();
                if (StudentBoard == null){
                    System.out.println("Board does not exist");
                }
                else if (StudentBoard.equals("SSC")){
                    if (StudentClass == null){
                        System.out.println("Class does not exist");
                    }
                    else if (StudentClass.equals("9th")){
                        chapterFilters(root,"Classification of Plants","Energy Flow in an Ecosystem ","Useful and Harmful Microbes","Life Processes in Living Organisms",
                                "Heredity and Variation","Introduction to Biotechnology","1","q","q","j",
                                "k","l"
                                ,View.VISIBLE, View.VISIBLE,View.VISIBLE, View.VISIBLE, View.VISIBLE,
                                View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,View.GONE);


                    }
                    else if (StudentClass.equals("10th")){

                        chapterFilters(root,"Heredity and Evolution","Life Processes in living organisms Part -1","Life Processes in living organisms Part -1","Environmental management",
                                "Towards Green Energy","Animal Classification","Introduction to Microbiology","Cell Biology and Biotechnology","Social health","Disaster Management",
                                "k","l"
                                ,View.VISIBLE, View.VISIBLE,View.VISIBLE, View.VISIBLE, View.VISIBLE,
                                View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.GONE,View.GONE);

                    }
                }
                else if (StudentBoard.equals("CBSE")){
                    if (StudentClass == null){
                        System.out.println("Class does not exist");
                    }
                    else if (StudentClass.equals("9th")){

                        chapterFilters(root,"Improvement in Food Resources","The Fundamental Unit of Life","Tissues","Diversity in Living Organisms",
                                "Why do We Fall Ill","Natural Resources","x","x","x","x",
                                "k","l"
                                ,View.VISIBLE, View.VISIBLE,View.VISIBLE, View.VISIBLE, View.VISIBLE,
                                View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,View.GONE);

                    }
                    else if (StudentClass.equals("10th")){


                        chapterFilters(root,"Life Processes","Control and Coordination","How do Organisms Reproduce","Heredity and Evolution",
                                "Human Eye and Colourful World","Our Environment","Management of Natural Resources","x","x","x",
                                "k","l"
                                ,View.VISIBLE, View.VISIBLE,View.VISIBLE, View.VISIBLE, View.VISIBLE,
                                View.VISIBLE, View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE,View.GONE);

                    }
                }//Biology
                else if (StudentBoard.equals("ICSE")){
                    if (StudentClass == null){
                        System.out.println("Class does not exist");
                    }
                    else if (StudentClass.equals("9th")){

                        chapterFilters(root,"Basic Biology","Flowering Plants","Plants Physiology","Diversity in Living Organisms",
                                "Human Anatomy and Physiology","x","x","x","x","x",
                                "k","l"
                                ,View.VISIBLE, View.VISIBLE,View.VISIBLE, View.VISIBLE, View.VISIBLE,
                                View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,View.GONE);

                    }
                    else if (StudentClass.equals("10th")){


                        chapterFilters(root,"Basic Biology","Plants Physiology","Human Anatomy and Physiology","x",
                                "x","x","x","x","x","x",
                                "k","l"
                                ,View.VISIBLE, View.VISIBLE,View.VISIBLE, View.GONE, View.GONE,
                                View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,View.GONE);

                    }
                }

            }
        });


        historyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SUBJECT = "History";
                DoubtList1.clear();
                loadDataFromFirebaseSubject();
                if (StudentBoard == null){
                    System.out.println("Board does not exist");
                }
                else if (StudentBoard.equals("SSC")){
                    if (StudentClass == null){
                        System.out.println("Class does not exist");
                    }
                    else if (StudentClass.equals("9th")){
                        chapterFilters(root,"Sources of History","India : Events after 1960","India’s Internal Challenges","Economic Development",
                                "Education","Empowerment of Women and other Weaker Sections","Science and Technology","Industry and Trade","Changing Life : 1","Changing Life : 2",
                                "k","l"
                                ,View.VISIBLE, View.VISIBLE,View.VISIBLE, View.VISIBLE, View.VISIBLE,
                                View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.GONE,View.GONE);


                    }
                    else if (StudentClass.equals("10th")){

                        chapterFilters(root,"Development in the West","Indian Tradition","Applied History","History of Indian Arts",
                                "Mass Media and History","Entertainment and History","Sports and History","Tourism and History","Heritage Management","D",
                                "k","l"
                                ,View.VISIBLE, View.VISIBLE,View.VISIBLE, View.VISIBLE, View.VISIBLE,
                                View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.GONE, View.GONE,View.GONE);

                    }
                }
                else if (StudentBoard.equals("CBSE")){
                    if (StudentClass == null){
                        System.out.println("Class does not exist");
                    }
                    else if (StudentClass.equals("9th")){


                        chapterFilters(root,"India and the Contemporary World I","Democratic Politics I","x","x",
                                "x","x","x","x","x","D",
                                "k","l"
                                ,View.VISIBLE, View.VISIBLE,View.GONE, View.GONE, View.GONE,
                                View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,View.GONE);


                    }
                    else if (StudentClass.equals("10th")){


                        chapterFilters(root,"India and the Contemporary World II","Democratic Politics II","x","x",
                                "x","x","x","x","x","D",
                                "k","l"
                                ,View.VISIBLE, View.VISIBLE,View.GONE, View.GONE, View.GONE,
                                View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,View.GONE);

                    }
                }
                //History
                else if (StudentBoard.equals("ICSE")){
                    if (StudentClass == null){
                        System.out.println("Class does not exist");
                    }
                    else if (StudentClass.equals("9th")){


                        chapterFilters(root,"The Harappan Civilization","The Vedic Period","Jainism and Buddhism","The Sangam Age",
                                "The Age of The Guptas","Medieval Indian","The Modern Age in Europe","Our Constitution","Election","Local Self Government",
                                "k","l"
                                ,View.VISIBLE, View.VISIBLE,View.VISIBLE, View.VISIBLE, View.VISIBLE,
                                View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.GONE,View.GONE);


                    }
                    else if (StudentClass.equals("10th")){


                        chapterFilters(root,"The Indian National Movement","Mass Phase of The National Movement","The Contemporary World","The Union Legislature",
                                "The Union Executive","The Judiciary","x","x","x","D",
                                "k","l"
                                ,View.VISIBLE, View.VISIBLE,View.VISIBLE, View.VISIBLE, View.VISIBLE,
                                View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,View.GONE);

                    }
                }


            }
        });



        geographyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SUBJECT = "Geography";
                DoubtList1.clear();
                loadDataFromFirebaseSubject();

                if (StudentBoard == null){
                    System.out.println("Board does not exist");
                }
                else if (StudentBoard.equals("SSC")){
                    if (StudentClass == null){
                        System.out.println("Class does not exist");
                    }
                    else if (StudentClass.equals("9th")){
                        chapterFilters(root,"Distributional Maps","Endogenetic Movements","Exogenetic Processes Part-1","Exogenetic Processes Part-2",
                                "Precipitation","The Properties of Sea Water","International Date Line","Introduction to Economics","Trade","Urbanisation",
                                "Transport and Communication","Tourism"
                                ,View.VISIBLE, View.VISIBLE,View.VISIBLE, View.VISIBLE, View.VISIBLE,
                                View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE,View.VISIBLE);


                    }
                    else if (StudentClass.equals("10th")){

                        chapterFilters(root,"Field Visit","Location and Extent","Physiography and Drainage","Climate",
                                "Natural Vegetation and Wildlife","Population","Human Settlements","Economy and Occupations","Tourism, Transport and Communication","D",
                                "k","l"
                                ,View.VISIBLE, View.VISIBLE,View.VISIBLE, View.VISIBLE, View.VISIBLE,
                                View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.GONE, View.GONE,View.GONE);

                    }
                }
                else if (StudentBoard.equals("CBSE")){
                    if (StudentClass == null){
                        System.out.println("Class does not exist");
                    }
                    else if (StudentClass.equals("9th")){


                        chapterFilters(root,"Contemporary India I","Economics","Disaster Management","x",
                                "x","x","x","x","x","D",
                                "k","l"
                                ,View.VISIBLE, View.VISIBLE,View.VISIBLE, View.GONE, View.GONE,
                                View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,View.GONE);


                    }
                    else if (StudentClass.equals("10th")){

                        chapterFilters(root,"Contemporary India II","Understanding Economic Development","Disaster Management","x",
                                "x","x","x","x","x","D",
                                "k","l"
                                ,View.VISIBLE, View.VISIBLE,View.VISIBLE, View.GONE, View.GONE,
                                View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,View.GONE);

                    }
                }//Geography
                else if (StudentBoard.equals("ICSE")){
                    if (StudentClass == null){
                        System.out.println("Class does not exist");
                    }
                    else if (StudentClass.equals("9th")){


                        chapterFilters(root,"Our World","Structure of Earth","Hydrosphere","Atmosphere",
                                "Pollution","Natural Region of The World","Map Work","Introduction to Economics","Types of Economics",
                                "The Indian Economy : A Study",
                                "k","l"
                                ,View.VISIBLE, View.VISIBLE,View.VISIBLE, View.VISIBLE, View.VISIBLE,
                                View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,View.GONE);


                    }
                    else if (StudentClass.equals("10th")){

                        chapterFilters(root,"Maps Works","Location, Extent & Physical Features Climate","Climate","Soil Resources",
                                "Natural Vegetation","Water Resources","Mineral and Energy Resources","Agriculture","Manufacturing Industries",
                                "Transport",
                                "Waste Management","Economics"
                                ,View.VISIBLE, View.VISIBLE,View.VISIBLE, View.VISIBLE, View.VISIBLE,
                                View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE,View.VISIBLE);

                    }
                }

            }
        });



        languagesFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DoubtList1.clear();
                loadDataFromFirebaseSubject();
                chapterFiltersForLanguages(root, "English", "Hindi", "Marathi", "Sanskrit", "French",View.VISIBLE,View.VISIBLE,View.VISIBLE
                ,View.VISIBLE,View.VISIBLE);
            }
        });











    }




    @SuppressLint("SetTextI18n")
    public void chapterFilters(final View root, final String chapter1, final String chapter2, final String chapter3, final String chapter4, final String chapter5, final String chapter6, final String chapter7, final String chapter8, final String chapter9
    , final String chapter10 , final String chapter11, final String chapter12, int chap1Visiblity, int chap2Visiblity, int chap3Visiblity, int chap4Visiblity, int chap5Visiblity, int chap6Visiblity, int chap7Visiblity,
                               int chap8Visiblity, int chap9Visiblity, int chap10Visiblity, int chap11Visiblity, int chap12Visiblity){


        layoutInflat(R.layout.chapter_buttons);


        selectSubject.setText("< Select Chapter");
        selectSubject.setTextColor(Color.parseColor("#121212"));
        selectSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CHAPTER = null;
                DoubtList1.clear();
                loadDataFromFirebase();


                selectSubject.setTextColor(Color.parseColor("#121212"));
                subjectFilter(root);
            }
        });

        Button Chapter1, Chapter2, Chapter3, Chapter4, Chapter5, Chapter6, Chapter7, Chapter8, Chapter9, Chapter10, Chapter11, Chapter12;

        Chapter1 = root.findViewById(R.id.chapter1);
        Chapter2 = root.findViewById(R.id.chapter2);
        Chapter3 = root.findViewById(R.id.chapter3);
        Chapter4 = root.findViewById(R.id.chapter4);
        Chapter5 = root.findViewById(R.id.chapter5);
        Chapter6 = root.findViewById(R.id.chapter6);
        Chapter7 = root.findViewById(R.id.chapter7);
        Chapter8 = root.findViewById(R.id.chapter8);
        Chapter9 = root.findViewById(R.id.chapter9);
        Chapter10 = root.findViewById(R.id.chapter10);
        Chapter11 = root.findViewById(R.id.chapter11);
        Chapter12 = root.findViewById(R.id.chapter12);

        Chapter1.setText(chapter1);
        Chapter1.setVisibility(chap1Visiblity);

        Chapter2.setText(chapter2);
        Chapter2.setVisibility(chap2Visiblity);

        Chapter3.setText(chapter3);
        Chapter3.setVisibility(chap3Visiblity);

        Chapter4.setText(chapter4);
        Chapter4.setVisibility(chap4Visiblity);

        Chapter5.setText(chapter5);
        Chapter5.setVisibility(chap5Visiblity);

        Chapter6.setText(chapter6);
        Chapter6.setVisibility(chap6Visiblity);

        Chapter7.setText(chapter7);
        Chapter7.setVisibility(chap7Visiblity);

        Chapter8.setText(chapter8);
        Chapter8.setVisibility(chap8Visiblity);

        Chapter9.setText(chapter9);
        Chapter9.setVisibility(chap9Visiblity);

        Chapter10.setText(chapter10);
        Chapter10.setVisibility(chap10Visiblity);

        Chapter11.setText(chapter11);
        Chapter11.setVisibility(chap11Visiblity);

        Chapter12.setText(chapter12);
        Chapter12.setVisibility(chap12Visiblity);

        Chapter1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CHAPTER = chapter1;

                startActivity(new Intent(getContext(), UploadImagePage.class));


            }
        });

        Chapter2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CHAPTER = chapter2;

                startActivity(new Intent(getContext(), UploadImagePage.class));

            }
        });

        Chapter3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CHAPTER = chapter3;

                startActivity(new Intent(getContext(), UploadImagePage.class));

            }
        });

        Chapter4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CHAPTER = chapter4;

                startActivity(new Intent(getContext(), UploadImagePage.class));

            }
        });

        Chapter5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CHAPTER = chapter5;

                startActivity(new Intent(getContext(), UploadImagePage.class));


            }
        });

        Chapter6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CHAPTER = chapter6;

                startActivity(new Intent(getContext(), UploadImagePage.class));

            }
        });

        Chapter7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CHAPTER = chapter7;

                startActivity(new Intent(getContext(), UploadImagePage.class));

            }
        });

        Chapter8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CHAPTER = chapter8;

                startActivity(new Intent(getContext(), UploadImagePage.class));

            }
        });

        Chapter9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CHAPTER = chapter9;

                startActivity(new Intent(getContext(), UploadImagePage.class));

            }
        });

        Chapter10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CHAPTER = chapter10;

                startActivity(new Intent(getContext(), UploadImagePage.class));

            }
        });

        Chapter11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CHAPTER = chapter11;

                startActivity(new Intent(getContext(), UploadImagePage.class));

            }
        });

        Chapter12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CHAPTER = chapter12;

                startActivity(new Intent(getContext(), UploadImagePage.class));

            }
        });



    }








    @SuppressLint("SetTextI18n")
    public void chapterFiltersForLanguages(final View root, final String chapter1, final String chapter2, final String chapter3, final String chapter4, final String chapter5, int chap1Visiblity, int chap2Visiblity, int chap3Visiblity, int chap4Visiblity, int chap5Visiblity){


        layoutInflat(R.layout.chapter_buttons);


        selectSubject.setText("< Back");
        selectSubject.setTextColor(Color.parseColor("#121212"));
        selectSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CHAPTER = null;
                DoubtList1.clear();
                loadDataFromFirebase();


                selectSubject.setTextColor(Color.parseColor("#121212"));
                subjectFilter(root);
            }
        });

        Button Chapter1, Chapter2, Chapter3, Chapter4, Chapter5,Chapter6, Chapter7, Chapter8, Chapter9, Chapter10, Chapter11, Chapter12;

        Chapter1 = root.findViewById(R.id.chapter1);
        Chapter2 = root.findViewById(R.id.chapter2);
        Chapter3 = root.findViewById(R.id.chapter3);
        Chapter4 = root.findViewById(R.id.chapter4);
        Chapter5 = root.findViewById(R.id.chapter5);
        Chapter6 = root.findViewById(R.id.chapter6);
        Chapter7 = root.findViewById(R.id.chapter7);
        Chapter8 = root.findViewById(R.id.chapter8);
        Chapter9 = root.findViewById(R.id.chapter9);
        Chapter10 = root.findViewById(R.id.chapter10);
        Chapter11 = root.findViewById(R.id.chapter11);
        Chapter12 = root.findViewById(R.id.chapter12);


        Chapter1.setText(chapter1);
        Chapter1.setVisibility(chap1Visiblity);

        Chapter2.setText(chapter2);
        Chapter2.setVisibility(chap2Visiblity);

        Chapter3.setText(chapter3);
        Chapter3.setVisibility(chap3Visiblity);

        Chapter4.setText(chapter4);
        Chapter4.setVisibility(chap4Visiblity);

        Chapter5.setText(chapter5);
        Chapter5.setVisibility(chap5Visiblity);


        Chapter6.setVisibility(View.GONE);
        Chapter7.setVisibility(View.GONE);
        Chapter8.setVisibility(View.GONE);
        Chapter9.setVisibility(View.GONE);
        Chapter10.setVisibility(View.GONE);
        Chapter11.setVisibility(View.GONE);
        Chapter12.setVisibility(View.GONE);




        Chapter1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SUBJECT = chapter1;

                startActivity(new Intent(getContext(), UploadImagePage.class));


            }
        });

        Chapter2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SUBJECT = chapter2;

                startActivity(new Intent(getContext(), UploadImagePage.class));

            }
        });

        Chapter3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SUBJECT = chapter3;

                startActivity(new Intent(getContext(), UploadImagePage.class));

            }
        });

        Chapter4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SUBJECT = chapter4;

                startActivity(new Intent(getContext(), UploadImagePage.class));

            }
        });

        Chapter5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SUBJECT = chapter5;

                startActivity(new Intent(getContext(), UploadImagePage.class));


            }
        });





    }














    private void loadDataFromFirebase() {
        db.collection("Doubts").whereEqualTo("Board", HomeFragment.BOARD).whereEqualTo("STD",HomeFragment.CLASS).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot querySnapshot : Objects.requireNonNull(task.getResult())){


                    //Date date = new Date();








                    if (Objects.equals(querySnapshot.getString("Status"), "Solved") || (querySnapshot.getString("Status").equals("Unsolved") && querySnapshot.getString("Email").equals(email))){
                        homeDoubtData = new HomeDoubtData(querySnapshot.getString("AnsPhotoUrl1"), querySnapshot.getString("AnsPhotoUrl2"), querySnapshot.getString("AnsText"),
                                querySnapshot.getString("AudioUrl"), querySnapshot.getString("Board"), querySnapshot.getString("Chapter"),
                                querySnapshot.getString("Email"), querySnapshot.getString("FileUrl"), querySnapshot.getString("Link"),
                                querySnapshot.getString("Name"), querySnapshot.getString("Photo1url"), querySnapshot.getString("Photo2url"),
                                querySnapshot.getString("ProfileImageURL"), querySnapshot.getString("QText"), querySnapshot.getString("STD"),
                                querySnapshot.getString("Status"), querySnapshot.getString("Subject"), querySnapshot.getString("Teacher"), querySnapshot.getString("Uid")
                                , querySnapshot.getDate("DateTime"),"", querySnapshot.getDate("QuestionDate"));

                        DoubtList1.add(homeDoubtData);
                    }


                   // Random random = new Random();



                    //HomeStuDoubtCardAdapter homeStuDoubtCardAdapter = new HomeStuDoubtCardAdapter(getContext(), DoubtList1);


                    SmartSuggestionAdapter smartSuggestionAdapter = new SmartSuggestionAdapter(getActivity(),DoubtList1);







                    recyclerView.setItemViewCacheSize(25);

                    recyclerView.setAdapter(smartSuggestionAdapter);

                }

                if (DoubtList1.isEmpty()){
                    recyclerView.setAlpha(0);
                }
                else{
                    recyclerView.setAlpha(1);
                }
            }
        });
    }


    private void loadDataFromFirebaseSubject() {
        db.collection("Doubts").whereEqualTo("Board", HomeFragment.BOARD).whereEqualTo("STD",HomeFragment.CLASS).whereEqualTo("Subject", UploadFragment.SUBJECT).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot querySnapshot : Objects.requireNonNull(task.getResult())){



                    //Date date = new Date();








                    if (Objects.equals(querySnapshot.getString("Status"), "Solved") || (querySnapshot.getString("Status").equals("Unsolved") && querySnapshot.getString("Email").equals(email))){

                        homeDoubtData = new HomeDoubtData(querySnapshot.getString("AnsPhotoUrl1"), querySnapshot.getString("AnsPhotoUrl2"), querySnapshot.getString("AnsText"),
                                querySnapshot.getString("AudioUrl"), querySnapshot.getString("Board"), querySnapshot.getString("Chapter"),
                                querySnapshot.getString("Email"), querySnapshot.getString("FileUrl"), querySnapshot.getString("Link"),
                                querySnapshot.getString("Name"), querySnapshot.getString("Photo1url"), querySnapshot.getString("Photo2url"),
                                querySnapshot.getString("ProfileImageURL"), querySnapshot.getString("QText"), querySnapshot.getString("STD"),
                                querySnapshot.getString("Status"), querySnapshot.getString("Subject"), querySnapshot.getString("Teacher"), querySnapshot.getString("Uid")
                                , querySnapshot.getDate("DateTime"),"",querySnapshot.getDate("QuestionDate"));

                        DoubtList1.add(homeDoubtData);
                    }



                    //HomeStuDoubtCardAdapter homeStuDoubtCardAdapter = new HomeStuDoubtCardAdapter(getContext(), DoubtList1);


                    SmartSuggestionAdapter smartSuggestionAdapter = new SmartSuggestionAdapter(getActivity(),DoubtList1);







                    recyclerView.setItemViewCacheSize(25);

                    recyclerView.setAdapter(smartSuggestionAdapter);

                }
                if (DoubtList1.isEmpty()){
                    recyclerView.setAlpha(0);
                }
                else{
                    recyclerView.setAlpha(1);
                }
            }
        });
    }







}
