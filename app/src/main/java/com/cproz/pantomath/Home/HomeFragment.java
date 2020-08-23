package com.cproz.pantomath.Home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cproz.pantomath.R;
import com.cproz.pantomath.StudentProfile.StudentProfile;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

    public static String BOARD = HomeFragment.BOARD, CLASS = HomeFragment.CLASS, PROFILEURL = HomeFragment.PROFILEURL, NAME = HomeFragment.NAME, PROFILEURLX = HomeFragment.PROFILEURLX = "";
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    String email = user != null ? user.getEmail() : null;
    private DocumentReference ref = firebaseFirestore.collection("Users/Students/StudentsInfo/" ).document(String.valueOf(email));
    ImageView searchIcon;


    ImageView noResults;

String ProfileImageURL = "";




    RecyclerView recyclerView;
    private List<HomeDoubtData> DoubtList2;
    private HomeDoubtData homeDoubtData;
    private FirebaseFirestore db;
    CircleImageView ProfilePictureHome;
    ImageView Cross;
    EditText SearchView;
    HomeDoubtAdapter homeDoubtAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    CardView cardView;


    @SuppressLint({"ResourceAsColor", "ClickableViewAccessibility"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
         final View root = inflater.inflate(R.layout.home_fragment, container, false);





        db = FirebaseFirestore.getInstance();

        recyclerView = root.findViewById(R.id.RecyclerViewHome);
        ProfilePictureHome = root.findViewById(R.id.ProfilePictureHome);
        SearchView = root.findViewById(R.id.SearchEditText);
        swipeRefreshLayout = root.findViewById(R.id.refreshLayout);
        searchIcon = root.findViewById(R.id.searchIcon);
        cardView = root.findViewById(R.id.searchBarHome);
        Cross = root.findViewById(R.id.Cross);
        noResults = root.findViewById(R.id.noResultsImg);


        Cross.setVisibility(View.GONE);
        noResults.setVisibility(View.GONE);
        /*SearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchIcon.setImageResource(R.drawable.back_search);
                searchIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getContext()).getSystemService(Context.INPUT_METHOD_SERVICE);
                        assert imm != null;
                        imm.hideSoftInputFromWindow(root.getWindowToken(), 0);
                        SearchView.clearFocus();
                        SearchView.getText().clear();
                        searchIcon.setImageResource(R.drawable.ic_round_search_24);
                    }
                });
            }
        });*/



        /*SearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });*/




        SearchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
                Objects.requireNonNull(imm).hideSoftInputFromWindow(SearchView.getWindowToken(), 0);




                return false;

            }
        });




        ProfilePictureHome.setImageResource(R.drawable.personal_info);


        swipeRefreshLayout.setColorSchemeResources(R.color.blue);

         GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);

         recyclerView.setLayoutManager(gridLayoutManager);
        DoubtList2 = new ArrayList<>();


        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    CLASS  = documentSnapshot.getString("Class");
                    BOARD = documentSnapshot.getString("Board");
                    PROFILEURL = documentSnapshot.getString("profileURL");
                    NAME = documentSnapshot.getString("Name");

                    System.out.print(DoubtList2.size());


                    if (PROFILEURL.equals("")){
                        ProfilePictureHome.setImageResource(R.drawable.personal_info);
                    }
                    else {
                        Picasso.get().load(PROFILEURL).into(ProfilePictureHome);
                    }



                    ProfilePictureHome.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getContext(), StudentProfile.class));

                        }
                    });


                    DoubtList2.clear();
                    DataFromFirestore();

                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {


                            DoubtList2.clear();
                            DataFromFirestore();

                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });


                }
                else{
                    System.out.println("does not exist");
                }
            }
        });


        SearchView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                searchIcon.setImageResource(R.drawable.back_search);
                ProfilePictureHome.setVisibility(View.GONE);
                searchIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getContext()).getSystemService(Context.INPUT_METHOD_SERVICE);
                        assert imm != null;
                        imm.hideSoftInputFromWindow(root.getWindowToken(), 0);
                        SearchView.clearFocus();
                        SearchView.getText().clear();
                        ProfilePictureHome.setImageResource(R.drawable.personal_info);
                        ProfilePictureHome.setVisibility(View.VISIBLE);
                        Cross.setVisibility(View.GONE);
                        searchIcon.setImageResource(R.drawable.ic_round_search_24);
                        swipeRefreshLayout.setEnabled(true);
                        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()){
                                    CLASS  = documentSnapshot.getString("Class");
                                    BOARD = documentSnapshot.getString("Board");
                                    PROFILEURL = documentSnapshot.getString("profileURL");
                                    NAME = documentSnapshot.getString("Name");

                                    System.out.print(DoubtList2.size());


                                    if (PROFILEURL.equals("")){
                                        ProfilePictureHome.setImageResource(R.drawable.personal_info);
                                    }
                                    else {
                                        Picasso.get().load(PROFILEURL).into(ProfilePictureHome);
                                    }



                                    ProfilePictureHome.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            startActivity(new Intent(getContext(), StudentProfile.class));

                                        }
                                    });


                                    DoubtList2.clear();
                                    DataFromFirestore();

                                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                        @Override
                                        public void onRefresh() {


                                            DoubtList2.clear();
                                            DataFromFirestore();

                                            swipeRefreshLayout.setRefreshing(false);
                                        }
                                    });


                                }
                                else{
                                    System.out.println("does not exist");
                                }
                            }
                        });
                    }
                });



                SearchView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @SuppressLint("ClickableViewAccessibility")
                    @Override
                    public void afterTextChanged(Editable s) {
                        ProfilePictureHome.setVisibility(View.GONE);
                        //ProfilePictureHome.setImageResource(R.drawable.cross);
                        Cross.setVisibility(View.VISIBLE);
                        Cross.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                SearchView.getText().clear();
                                ProfilePictureHome.setVisibility(View.GONE);
                                Cross.setVisibility(View.GONE);


                            }
                        });



                    }
                });


                return false;
            }
        });







         return root;
    }


    public void DataFromFirestore(){


        db.collection("Doubts").whereEqualTo("Board", BOARD).whereEqualTo("STD",CLASS).orderBy("DateTime", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot querySnapshot : Objects.requireNonNull(task.getResult())){


                    //Date date = new Date();





                    /*firebaseFirestore.collection("Users/Students/StudentsInfo/" ).document(String.valueOf(querySnapshot.getString("Email"))).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            HomeFragment.PROFILEURLX = documentSnapshot.getString("profileURL");


                        }
                    });*/







                    homeDoubtData = new HomeDoubtData(querySnapshot.getString("AnsPhotoUrl1"), querySnapshot.getString("AnsPhotoUrl2"), querySnapshot.getString("AnsText"),
                            querySnapshot.getString("AudioUrl"), querySnapshot.getString("Board"), querySnapshot.getString("Chapter"),
                            querySnapshot.getString("Email"), querySnapshot.getString("FileUrl"), querySnapshot.getString("Link"),
                            querySnapshot.getString("Name"), querySnapshot.getString("Photo1url"), querySnapshot.getString("Photo2url"),
                            querySnapshot.getString("ProfileImageURL"), querySnapshot.getString("QText"), querySnapshot.getString("STD"),
                            querySnapshot.getString("Status"), querySnapshot.getString("Subject"), querySnapshot.getString("Teacher"), querySnapshot.getString("Uid")
                            , querySnapshot.getDate("DateTime"),"",querySnapshot.getDate("QuestionDate"));

                    DoubtList2.add(homeDoubtData);



                    homeDoubtAdapter = new HomeDoubtAdapter(getContext(), DoubtList2);


                    recyclerView.setItemViewCacheSize(40);

                    recyclerView.setAdapter(homeDoubtAdapter);


                }
            }
        });

        SearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filter(s.toString());



            }
        });
    }

    @SuppressLint("ResourceAsColor")
    private void filter(String text) {

        recyclerView.setBackgroundColor(Color.parseColor("#ffffff"));

        ArrayList<HomeDoubtData> filteredList = new ArrayList<>();
        for (HomeDoubtData item: DoubtList2){

            if (item.getTeacher().contains(text.toLowerCase())||item.getSubject().toLowerCase().contains(text.toLowerCase()) || item.getChapter().toLowerCase().contains(text.toLowerCase()) || item.getQText().contains(text.toLowerCase())
            || item.getAnsText().contains(text.toLowerCase())){

                filteredList.add(item);
                homeDoubtAdapter.filteredList(filteredList);
                swipeRefreshLayout.setEnabled(false);
                noResults.setVisibility(View.GONE);

            }



        }
        if (filteredList.isEmpty()){

            homeDoubtAdapter.filteredList(filteredList);
            //recyclerView.setBackgroundColor(R.drawable.notfound);
            noResults.setVisibility(View.VISIBLE);


            //Toast.makeText(getContext(), "No results found", Toast.LENGTH_SHORT).show();
        }






    }







}
