package com.cproz.pantomath.Home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cproz.pantomath.BuildConfig;
import com.cproz.pantomath.Feedback.Feedback;
import com.cproz.pantomath.R;
import com.cproz.pantomath.StudentProfile.StudentProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment {

    public static String BOARD = HomeFragment.BOARD, CLASS = HomeFragment.CLASS,
            PROFILEURL = HomeFragment.PROFILEURL, NAME = HomeFragment.NAME,
            PROFILEURLX = HomeFragment.PROFILEURLX = "";
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    String email = user != null ? user.getEmail() : null;
    private final DocumentReference ref = firebaseFirestore
            .collection("Users/Students/StudentsInfo/" )
            .document(String.valueOf(email));

    ImageView searchIcon;
    DocumentSnapshot lastVisible;
    private boolean isScrolling, lastItemReached;
    GridLayoutManager gridLayoutManager;
    TextView feedbackCardText;
    ImageView noResults;

    String ProfileImageURL = "";
    private static final int SCROLL_DIRECTION_UP = -1;


    ConstraintLayout feedbackCard;


    AppBarLayout appBarLayout;
    Toolbar toolbar;
    RecyclerView recyclerView;
    private List<HomeDoubtData> DoubtList2;
    private HomeDoubtData homeDoubtData;
    private FirebaseFirestore db;
    CircleImageView ProfilePictureHome;


    HomeDoubtAdapter homeDoubtAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    CardView cardView;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint({"ResourceAsColor", "ClickableViewAccessibility", "SetTextI18n"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
         final View root = inflater.inflate(R.layout.home_fragment, container, false);


        db = FirebaseFirestore.getInstance();
        feedbackCard = root.findViewById(R.id.homeFeedbackCard);



        feedbackCardText = root.findViewById(R.id.homeFeedbackCardText);

        String text = "<b><font color=#0476D9>Feedback & Suggestions: </font> <font color=#5f6368>Share your<br>valuable feedback & suggestions.</font></b>";


        feedbackCardText.setText(Html.fromHtml(text));



        feedbackCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Feedback.class));
            }
        });

        recyclerView = root.findViewById(R.id.RecyclerViewHome);
        ProfilePictureHome = root.findViewById(R.id.ProfilePictureHome);
        swipeRefreshLayout = root.findViewById(R.id.refreshLayout);

        toolbar = root.findViewById(R.id.NewAccToolBar);

        searchIcon = root.findViewById(R.id.searchIcon);


        noResults = root.findViewById(R.id.noResultsImg);

        appBarLayout = root.findViewById(R.id.appBarLayout);


        noResults.setVisibility(View.GONE);

        Objects.requireNonNull(getActivity()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


//        SearchView.setText(BuildConfig.VERSION_CODE);


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


        //refreshLoad();




// ...
// Put this into your RecyclerView.OnScrollListener > onScrolled() method
        if (recyclerView.canScrollVertically(SCROLL_DIRECTION_UP)) {
            // Remove elevation
            appBarLayout.setElevation(0f);
        } else {
            // Show elevation
            appBarLayout.setElevation(0f);
        }








        ProfilePictureHome.setImageResource(R.drawable.personal_info);


        swipeRefreshLayout.setColorSchemeResources(R.color.blue);

        gridLayoutManager = new GridLayoutManager(getContext(), 1);

         recyclerView.setLayoutManager(gridLayoutManager);
        DoubtList2 = new ArrayList<>();

        DoubtList2.clear();
        homeDoubtAdapter = new HomeDoubtAdapter(getContext(), DoubtList2);


        recyclerView.setItemViewCacheSize(40);

        recyclerView.setAdapter(homeDoubtAdapter);

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
//                    RefreshedList.clear();
                    DataFromFirestore();
                    homeDoubtAdapter = new HomeDoubtAdapter(getContext(), DoubtList2);


                    recyclerView.setItemViewCacheSize(40);

                    recyclerView.setAdapter(homeDoubtAdapter);
                    //refreshLoad();

                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {


                            noResults.setVisibility(View.GONE);

                            homeDoubtAdapter = new HomeDoubtAdapter(getContext(), DoubtList2);


                            recyclerView.setItemViewCacheSize(40);

                            recyclerView.setAdapter(homeDoubtAdapter);



                            DoubtList2.clear();
                            DataFromFirestore();
                            homeDoubtAdapter = new HomeDoubtAdapter(getContext(), DoubtList2);


                            recyclerView.setItemViewCacheSize(40);

                            recyclerView.setAdapter(homeDoubtAdapter);



                            //DoubtList2.clear();
                            //swipeRefreshLayout.setEnabled(false);
                            //DoubtList2.clear();
                            //DataFromFirestore();


//                            RefreshedList.clear();
//                            refreshLoad();
//
//                            if (RefreshedList.isEmpty()){
//                                homeDoubtAdapter = new HomeDoubtAdapter(getContext(), DoubtList2);
//                                Toast.makeText(getContext(), "Exe1", Toast.LENGTH_SHORT).show();
//                            }else{
//                                homeDoubtAdapter = new HomeDoubtAdapter(getContext(), RefreshedList);
//                                Toast.makeText(getContext(), "Exe2", Toast.LENGTH_SHORT).show();
//                            }
//
//
//
//
//
//
//
//
//                            recyclerView.setItemViewCacheSize(40);
//
//                            recyclerView.setAdapter(homeDoubtAdapter);
//
//
//
                             swipeRefreshLayout.setRefreshing(false);

                        }
                    });


                }
                else{
                    System.out.println("does not exist");
                }
            }
        });

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SearchActivity.class));

            }
        });

//        SearchView.setOnTouchListener(new View.OnTouchListener() {
//            @SuppressLint("ClickableViewAccessibility")
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//
//
//                searchIcon.setImageResource(R.drawable.back_search);
//                ProfilePictureHome.setVisibility(View.GONE);
//                searchIcon.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//
//                        InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getContext()).getSystemService(Context.INPUT_METHOD_SERVICE);
//                        assert imm != null;
//                        imm.hideSoftInputFromWindow(root.getWindowToken(), 0);
//                        SearchView.clearFocus();
//                        //SearchView.getText().clear();
//                        ProfilePictureHome.setImageResource(R.drawable.personal_info);
//                        ProfilePictureHome.setVisibility(View.VISIBLE);
//                        Cross.setVisibility(View.GONE);
//                        searchIcon.setImageResource(R.drawable.ic_round_search_24);
//                        swipeRefreshLayout.setEnabled(true);
//
//                    }
//                });
//
//
//
//                SearchView.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                    }
//
//                    @SuppressLint("ClickableViewAccessibility")
//                    @Override
//                    public void afterTextChanged(Editable s) {
//                        ProfilePictureHome.setVisibility(View.GONE);
//                        //ProfilePictureHome.setImageResource(R.drawable.cross);
//                        Cross.setVisibility(View.VISIBLE);
//                        Cross.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                               //SearchView.getText().clear();
//                                ProfilePictureHome.setVisibility(View.GONE);
//                                Cross.setVisibility(View.GONE);
//
//
//                            }
//                        });
//
//
//
//                    }
//                });
//
//
//                return false;
//            }
//        });







         return root;
    }


    public void DataFromFirestore(){


        db.collection("Doubts")
                .whereEqualTo("Board", BOARD)
                .whereEqualTo("STD",CLASS)
                .whereEqualTo("Status", "Solved")
                .whereEqualTo("AcademicYear", "2021-22")
                .orderBy("DateTime", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {


                for (QueryDocumentSnapshot querySnapshot : Objects.requireNonNull(task.getResult())){








                    /*firebaseFirestore.collection("Users/Students/StudentsInfo/" ).document(String.valueOf(querySnapshot.getString("Email"))).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            HomeFragment.PROFILEURLX = documentSnapshot.getString("profileURL");


                        }
                    });*/



                        homeDoubtData = new HomeDoubtData(querySnapshot.getString("AnsPhotoUrl1"),
                                querySnapshot.getString("AnsPhotoUrl2"),
                                querySnapshot.getString("AnsText"),
                                querySnapshot.getString("AudioUrl"),
                                querySnapshot.getString("Board"),
                                querySnapshot.getString("Chapter"),
                                querySnapshot.getString("Email"),
                                querySnapshot.getString("FileUrl"),
                                querySnapshot.getString("Link"),
                                querySnapshot.getString("Name"),
                                querySnapshot.getString("Photo1url"),
                                querySnapshot.getString("Photo2url"),
                                querySnapshot.getString("ProfileImageURL"),
                                querySnapshot.getString("QText"),
                                querySnapshot.getString("STD"),
                                querySnapshot.getString("Status"),
                                querySnapshot.getString("Subject"),
                                querySnapshot.getString("Teacher"),
                                querySnapshot.getString("Uid"),
                                querySnapshot.getDate("DateTime"),
                                querySnapshot.getString("TeacherImageUrl"),
                                querySnapshot.getDate("QuestionDate"));

                        DoubtList2.add(homeDoubtData);










                    homeDoubtAdapter = new HomeDoubtAdapter(getContext(), DoubtList2);


                    recyclerView.setItemViewCacheSize(40);

                    recyclerView.setAdapter(homeDoubtAdapter);


                }
                swipeRefreshLayout.setEnabled(true);
                homeDoubtAdapter.shimmer = false;
                homeDoubtAdapter.notifyDataSetChanged();

                if (task.getResult().size() > 0){
                    lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);
                }

                RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);

                        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                            isScrolling = true;
                        }
                    }

                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        int firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();
                        int visibleItemCount = gridLayoutManager.getChildCount();
                        int totalItemCount = gridLayoutManager.getItemCount();

                        if (isScrolling && (firstVisibleItem+visibleItemCount == totalItemCount) && !lastItemReached) {
                            isScrolling = false;

                            Query queryNext = db.collection("Doubts")
                                    .whereEqualTo("Board", BOARD)
                                    .whereEqualTo("STD",CLASS)
                                    .whereEqualTo("Status", "Solved")
                                    .whereEqualTo("AcademicYear", "2021-22")
                                    .orderBy("DateTime", Query.Direction.DESCENDING)
                                    .startAfter(lastVisible)
                                    .limit(5);

                            queryNext.get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            for (QueryDocumentSnapshot querySnapshot : Objects.requireNonNull(task.getResult())){








                    /*firebaseFirestore.collection("Users/Students/StudentsInfo/" ).document(String.valueOf(querySnapshot.getString("Email"))).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            HomeFragment.PROFILEURLX = documentSnapshot.getString("profileURL");


                        }
                    });*/



                                                    homeDoubtData = new HomeDoubtData(querySnapshot.getString("AnsPhotoUrl1"),
                                                            querySnapshot.getString("AnsPhotoUrl2"),
                                                            querySnapshot.getString("AnsText"),
                                                            querySnapshot.getString("AudioUrl"),
                                                            querySnapshot.getString("Board"),
                                                            querySnapshot.getString("Chapter"),
                                                            querySnapshot.getString("Email"),
                                                            querySnapshot.getString("FileUrl"),
                                                            querySnapshot.getString("Link"),
                                                            querySnapshot.getString("Name"),
                                                            querySnapshot.getString("Photo1url"),
                                                            querySnapshot.getString("Photo2url"),
                                                            querySnapshot.getString("ProfileImageURL"),
                                                            querySnapshot.getString("QText"),
                                                            querySnapshot.getString("STD"),
                                                            querySnapshot.getString("Status"),
                                                            querySnapshot.getString("Subject"),
                                                            querySnapshot.getString("Teacher"),
                                                            querySnapshot.getString("Uid"),
                                                            querySnapshot.getDate("DateTime"),
                                                            querySnapshot.getString("TeacherImageUrl"),
                                                            querySnapshot.getDate("QuestionDate"));

                                                    DoubtList2.add(homeDoubtData);













                                            }
                                            homeDoubtAdapter.notifyDataSetChanged();

                                            if (task.getResult().size() > 0){
                                                lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);
                                            }


                                            if (task.getResult().size() < 5){
                                                lastItemReached = true;
                                            }
                                        }
                                    });
                        }

                    }
                };
                recyclerView.addOnScrollListener(onScrollListener);
                lastItemReached = false;
                swipeRefreshLayout.setEnabled(true);

                if (DoubtList2.isEmpty()){
                    noResults.setVisibility(View.VISIBLE);

                    homeDoubtAdapter = new HomeDoubtAdapter(getContext(), DoubtList2);


                    recyclerView.setItemViewCacheSize(40);

                    recyclerView.setAdapter(homeDoubtAdapter);
                    homeDoubtAdapter.shimmer = false;

                }else{
                    noResults.setVisibility(View.GONE);
                }
                //homeDoubtAdapter.shimmer = false;
            }

        });



    }

//    @SuppressLint("ResourceAsColor")
//    private void filter(String text) {
//
//        recyclerView.setBackgroundColor(Color.parseColor("#ffffff"));
//
//        ArrayList<HomeDoubtData> filteredList = new ArrayList<>();
//        for (HomeDoubtData item: DoubtList2){
//
//            if (item.getSubject().toLowerCase().contains(text.toLowerCase())
//                    || item.getChapter().toLowerCase().contains(text.toLowerCase())
//                    || item.getQText().toLowerCase().contains(text.toLowerCase())
//            || item.getAnsText().toLowerCase().contains(text.toLowerCase())){
//
//                filteredList.add(item);
//                homeDoubtAdapter.filteredList(filteredList);
//                swipeRefreshLayout.setEnabled(false);
//                noResults.setVisibility(View.GONE);
//
//            }
//
//
//
//        }
//        if (filteredList.isEmpty()){
//            homeDoubtAdapter.filteredList(filteredList);
//            noResults.setVisibility(View.VISIBLE);
//        }
//
//
//
//
//
//
//    }


//    public void refreshLoad(){
//
//        db.collection("Doubts")
//                .whereEqualTo("Board", BOARD)
//                .whereEqualTo("STD",CLASS)
//                .orderBy("DateTime", Query.Direction.DESCENDING)
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        if (error != null) {
//                            Log.e(TAG, "onEvent", error);
//
//
//
//                        }
//                        if (value != null){
//
//                            List<DocumentChange> DoubtListUpdated = value.getDocumentChanges();
//                            for (DocumentChange documentChange :  DoubtListUpdated){
//
//
//                                if (documentChange.getType() == DocumentChange.Type.REMOVED){
//                                    Log.d(TAG, "Removed Doubt: \n" + documentChange.getDocument().getData());
//                                    break;
//                                }else if (documentChange.getType() == DocumentChange.Type.ADDED){
//                                    Log.d(TAG, "Added Doubt: \n" + documentChange.getDocument().getData());
//                                    break;
//                                }else if (documentChange.getType() == DocumentChange.Type.MODIFIED){
//                                    Log.d(TAG, "Modified Doubt: \n" + documentChange.getDocument().getData());
//                                    break;
//                                }
////                                homeDoubtData = new HomeDoubtData(querySnapshot.getString("AnsPhotoUrl1"), querySnapshot.getString("AnsPhotoUrl2"), querySnapshot.getString("AnsText"),
////                                        querySnapshot.getString("AudioUrl"), querySnapshot.getString("Board"), querySnapshot.getString("Chapter"),
////                                        querySnapshot.getString("Email"), querySnapshot.getString("FileUrl"), querySnapshot.getString("Link"),
////                                        querySnapshot.getString("Name"), querySnapshot.getString("Photo1url"), querySnapshot.getString("Photo2url"),
////                                        querySnapshot.getString("ProfileImageURL"), querySnapshot.getString("QText"), querySnapshot.getString("STD"),
////                                        querySnapshot.getString("Status"), querySnapshot.getString("Subject"), querySnapshot.getString("Teacher"), querySnapshot.getString("Uid")
////                                        , querySnapshot.getDate("DateTime"),querySnapshot.getString("TeacherImageUrl"),querySnapshot.getDate("QuestionDate"));
//
//                                if ((Objects.equals(documentChange.getDocument().getString("Status"), "Solved"))
//                                        || ((Objects.equals(documentChange.getDocument().getString("Status"), "Unsolved"))
//                                        && Objects.equals(documentChange.getDocument().getString("Email"), email))) {
//
//
//                                    homeDoubtData = new HomeDoubtData(
//                                            documentChange.getDocument().getString("AnsPhotoUrl1"),
//                                            documentChange.getDocument().getString("AnsPhotoUrl2"),
//                                            documentChange.getDocument().getString("AnsText"),
//                                            documentChange.getDocument().getString("AudioUrl"),
//                                            documentChange.getDocument().getString("Board"),
//                                            documentChange.getDocument().getString("Chapter"),
//                                            documentChange.getDocument().getString("Email"),
//                                            documentChange.getDocument().getString("FileUrl"),
//                                            documentChange.getDocument().getString("Link"),
//                                            documentChange.getDocument().getString("Name"),
//                                            documentChange.getDocument().getString("Photo1url"),
//                                            documentChange.getDocument().getString("Photo2url"),
//                                            documentChange.getDocument().getString("ProfileImageURL"),
//                                            documentChange.getDocument().getString("QText"),
//                                            documentChange.getDocument().getString("STD"),
//                                            documentChange.getDocument().getString("Status"),
//                                            documentChange.getDocument().getString("Subject"),
//                                            documentChange.getDocument().getString("Teacher"),
//                                            documentChange.getDocument().getString("Uid"),
//                                            documentChange.getDocument().getDate("DateTime"),
//                                            documentChange.getDocument().getString("TeacherImageUrl"),
//                                            documentChange.getDocument().getDate("QuestionDate"));
//
//                                    RefreshedList.add(homeDoubtData);
//
//                                }
//
//                            }
//
//                            swipeRefreshLayout.setEnabled(true);
//                        }else {
//                            RefreshedList.clear();
//                        }
//
//
//
//                    }
//                });
//    }








}
