package com.cproz.pantomath.Home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
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

    public static String BOARD = HomeFragment.BOARD, CLASS = HomeFragment.CLASS, PROFILEURL = HomeFragment.PROFILEURL, NAME = HomeFragment.NAME;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    String email = user != null ? user.getEmail() : null;
    private DocumentReference ref = firebaseFirestore.collection("Users/Students/StudentsInfo/" ).document(String.valueOf(email));







    RecyclerView recyclerView;
    private List<HomeDoubtData> DoubtList2;
    private HomeDoubtData homeDoubtData;
    private FirebaseFirestore db;
    CircleImageView ProfilePictureHome;
    EditText SearchView;
    HomeDoubtAdapter homeDoubtAdapter;
    SwipeRefreshLayout swipeRefreshLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         View root = inflater.inflate(R.layout.home_fragment, container, false);





        db = FirebaseFirestore.getInstance();

        recyclerView = root.findViewById(R.id.RecyclerViewHome);
        ProfilePictureHome = root.findViewById(R.id.ProfilePictureHome);
        SearchView = root.findViewById(R.id.SearchEditText);
        swipeRefreshLayout = root.findViewById(R.id.refreshLayout);





        ProfilePictureHome.setImageResource(R.drawable.defprofileimage);



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
                        ProfilePictureHome.setImageResource(R.drawable.defprofileimage);
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










         return root;
    }


    public void DataFromFirestore(){


        db.collection("Doubts").whereEqualTo("Board", BOARD).whereEqualTo("STD",CLASS).orderBy("DateTime", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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

    private void filter(String text) {

        ArrayList<HomeDoubtData> filteredList = new ArrayList<>();
        for (HomeDoubtData item: DoubtList2){

            if (item.getSubject().toLowerCase().contains(text.toLowerCase()) || item.getChapter().toLowerCase().contains(text.toLowerCase()) || item.getQText().contains(text.toLowerCase())
            || item.getAnsText().contains(text.toLowerCase())){

                filteredList.add(item);
                homeDoubtAdapter.filteredList(filteredList);

            }



        }




    }


}
