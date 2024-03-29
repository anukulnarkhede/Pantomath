package com.cproz.pantomath.Notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cproz.pantomath.Home.HomeDoubtAdapter;
import com.cproz.pantomath.Home.HomeDoubtData;
import com.cproz.pantomath.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class NotificationsFragments extends Fragment {


    private FirebaseFirestore db;
    private List<HomeDoubtData> DoubtList1;
    public String Board, Class;
    private HomeDoubtData homeDoubtData;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    String email = user != null ? user.getEmail() : null;
    private DocumentReference ref = firebaseFirestore.collection("Users/Students/StudentsInfo/" ).document(String.valueOf(email));


    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.notification, container, false);
        recyclerView = root.findViewById(R.id.recyclerNotifications);
        swipeRefreshLayout = root.findViewById(R.id.swipeRefNotification);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        db = FirebaseFirestore.getInstance();

        DoubtList1 = new ArrayList<>();



        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                email = documentSnapshot.getString("Email");
                Class = documentSnapshot.getString("Class");
                Board = documentSnapshot.getString("Board");
                LoadNotifications();


                swipeRefreshLayout.setColorSchemeResources(R.color.blue);
                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        DoubtList1.clear();
                        swipeRefreshLayout.setEnabled(false);

//                        LoadNotifications();

                        LoadNotifications();

//                        NotificationAdapter notificationAdapter = new NotificationAdapter(getContext(),DoubtList1);
//                        recyclerView.setItemViewCacheSize(40);
//
//                        recyclerView.setAdapter(notificationAdapter);

                        swipeRefreshLayout.setRefreshing(false);
                    }
                });


            }
        });






        return root;
    }

    private void LoadNotifications() {


        db.collection("Doubts")
                .whereEqualTo("Email", email)
                .whereEqualTo("Status", "Solved")
                .whereEqualTo("STD",Class )
                .whereEqualTo("Board", Board)
                .orderBy("DateTime", Query.Direction.DESCENDING)
                .limit(30)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot querySnapshot : Objects.requireNonNull(task.getResult())){

                    if (!Objects.equals(querySnapshot.getString("Status"), "Reported")){
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
                                querySnapshot.getString("Uid")
                                , querySnapshot.getDate("DateTime"),
                                querySnapshot.getString("TeacherImageUrl"),
                                querySnapshot.getDate("QuestionDate"));

                        DoubtList1.add(homeDoubtData);
                    }


                    NotificationAdapter notificationAdapter = new NotificationAdapter(getContext(),DoubtList1);
                    recyclerView.setItemViewCacheSize(40);

                    recyclerView.setAdapter(notificationAdapter);


                }
                swipeRefreshLayout.setEnabled(true);
            }
        });


    }


//    public void refreshLoad(){
//
//        db.collection("Doubts")
//                .whereEqualTo("Email", email)
//                .whereEqualTo("Status", "Solved")
//                .whereEqualTo("STD",Class )
//                .whereEqualTo("Board", Board)
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
//                            List<DocumentChange> DoubtListUpdated = value.getDocumentChanges();
//                            for (DocumentChange documentChange :  DoubtListUpdated){
//
////                                homeDoubtData = new HomeDoubtData(querySnapshot.getString("AnsPhotoUrl1"), querySnapshot.getString("AnsPhotoUrl2"), querySnapshot.getString("AnsText"),
////                                        querySnapshot.getString("AudioUrl"), querySnapshot.getString("Board"), querySnapshot.getString("Chapter"),
////                                        querySnapshot.getString("Email"), querySnapshot.getString("FileUrl"), querySnapshot.getString("Link"),
////                                        querySnapshot.getString("Name"), querySnapshot.getString("Photo1url"), querySnapshot.getString("Photo2url"),
////                                        querySnapshot.getString("ProfileImageURL"), querySnapshot.getString("QText"), querySnapshot.getString("STD"),
////                                        querySnapshot.getString("Status"), querySnapshot.getString("Subject"), querySnapshot.getString("Teacher"), querySnapshot.getString("Uid")
////                                        , querySnapshot.getDate("DateTime"),querySnapshot.getString("TeacherImageUrl"),querySnapshot.getDate("QuestionDate"));
//
//                                if (!Objects.equals(documentChange.getDocument().getString("Status"), "Reported")) {
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
//                                    DoubtList1.add(homeDoubtData);
//
//                                }
//
//                            }
//                            swipeRefreshLayout.setEnabled(true);
//                        }
//                    }
//                });
//    }





}
