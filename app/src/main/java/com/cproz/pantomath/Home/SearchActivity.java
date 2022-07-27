package com.cproz.pantomath.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.cproz.pantomath.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class SearchActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText SearchEditText;
    RecyclerView searchRecyclerView;
    HomeDoubtData homeDoubtData;
    GridLayoutManager gridLayoutManager;
    HomeDoubtAdapter homeDoubtAdapter;
    ImageView noResultsSearch, Cross;
    TextView noOfSearchResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        initialization();

        Cross.setVisibility(View.GONE);
        //Toast.makeText(this, HomeFragment.CLASS + " " + HomeFragment.BOARD, Toast.LENGTH_SHORT).show();
        gridLayoutManager = new GridLayoutManager(this, 1);

        searchRecyclerView.setLayoutManager(gridLayoutManager);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(Color.parseColor("#5f6368"), PorterDuff.Mode.SRC_ATOP);

        SearchEditText.requestFocus();

        noResultsSearch.setVisibility(View.GONE);
        SearchEditText.requestFocus();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        final Client client = new Client("LR58F2D6BJ", "15a6509e475919b488d08415a5a1f408");
        Index index = client.getIndex("Doubts");



        Cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchEditText.getText().clear();
                SearchEditText.requestFocus();
//                SearchActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
            }
        });






        SearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (count == 0){
                    Cross.setVisibility(View.GONE);

                }else{
                    Cross.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                Index index = client.getIndex("Doubts");
                Query query = new Query(s)
                        .setHitsPerPage(1000);
                index.searchAsync(query, new CompletionHandler() {

                    @SuppressLint("SetTextI18n")
                    public void requestCompleted(JSONObject content, AlgoliaException error) {
                        try {
                            JSONArray hits = content.getJSONArray("hits");
                            List<HomeDoubtData> SearchDoubtList = new ArrayList<>();
                            for (int i = 0; i < hits.length(); i++){
                                JSONObject jsonObject = hits.getJSONObject(i);

                                Date dateTime = new Date();

                                if (jsonObject.getString("STD").equals(HomeFragment.CLASS)
                                        && jsonObject.getString("Board").equals(HomeFragment.BOARD) &&
                                        jsonObject.getString("Status").equals("Solved")){

                                    homeDoubtData =
                                            new HomeDoubtData(
                                                    jsonObject.getString("AnsPhotoUrl1"),
                                                    jsonObject.getString("AnsPhotoUrl2"),
                                                    jsonObject.getString("AnsText"),
                                                    jsonObject.getString("AudioUrl"),
                                                    jsonObject.getString("Board"),
                                                    jsonObject.getString("Chapter"),
                                                    jsonObject.getString("Email"),
                                                    jsonObject.getString("FileUrl"),
                                                    jsonObject.getString("Link"),
                                                    jsonObject.getString("Name"),
                                                    jsonObject.getString("Photo1url"),
                                                    jsonObject.getString("Photo2url"),
                                                    jsonObject.getString("ProfileImageURL"),
                                                    jsonObject.getString("QText"),
                                                    jsonObject.getString("STD"),
                                                    jsonObject.getString("Status"),
                                                    jsonObject.getString("Subject"),
                                                    jsonObject.getString("Teacher"),
                                                    jsonObject.getString("Uid"),
                                                    dateTime,
                                                    jsonObject.getString("TeacherImageUrl"),
                                                    dateTime);

                                    SearchDoubtList.add(homeDoubtData);
                                }




                            }



                            if (SearchDoubtList.isEmpty()){
                                noResultsSearch.setVisibility(View.VISIBLE);
                            }else{
                                noResultsSearch.setVisibility(View.GONE);
                            }


                            homeDoubtAdapter = new HomeDoubtAdapter(SearchActivity.this, SearchDoubtList);

                            if (SearchDoubtList.size() == 1){
                                noOfSearchResults.setText(SearchDoubtList.size() + " search result");
                            }else if (SearchDoubtList.size() == 0){
                                noOfSearchResults.setText("No search results");
                            }else{
                                noOfSearchResults.setText(SearchDoubtList.size() + " search results");
                            }

                            if (SearchEditText.getText().length() == 0){
                                searchRecyclerView.setVisibility(View.GONE);
                                noOfSearchResults.setText("");
                            }else{
                                searchRecyclerView.setVisibility(View.VISIBLE);
                            }

                            homeDoubtAdapter.shimmer = false;
                            searchRecyclerView.setItemViewCacheSize(40);

                            searchRecyclerView.setAdapter(homeDoubtAdapter);





                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });




    }

    private void initialization() {
        toolbar = findViewById(R.id.SearchToolBar);
        SearchEditText = findViewById(R.id.SearchEditText);
        searchRecyclerView = findViewById(R.id.searchRecyclerView);
        noResultsSearch = findViewById(R.id.noResultsSearch);
        noOfSearchResults = findViewById(R.id.noOfSearchResults);
        Cross = findViewById(R.id.Cross);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }



}