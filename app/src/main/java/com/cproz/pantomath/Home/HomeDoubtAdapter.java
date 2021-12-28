package com.cproz.pantomath.Home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.cproz.pantomath.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.DateTime;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeDoubtAdapter extends RecyclerView.Adapter<HomeDoubtViewHolder> {




    private Context mContext;
    private List<HomeDoubtData> DoubtList;
    boolean shimmer = true;
    int SHIMMER_ITEMS = 3;

    public HomeDoubtAdapter(Context mContext, List<HomeDoubtData> doubtList){

        this.mContext = mContext;
        DoubtList = doubtList;

        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public HomeDoubtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.doubt_card_home, parent, false);
        return new HomeDoubtViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final HomeDoubtViewHolder holder, int position) {


        if (shimmer){
            holder.shimmerFrameLayout.startShimmer();
            holder.profilePictureDC.setImageResource(R.drawable.shimmer_profile_bg);
            holder.imageHolder.setBackgroundResource(R.drawable.shimmer_card_bg);
            holder.CardBg.setBackgroundResource(R.drawable.shimmer_card_bg);
            holder.UserName.setBackgroundResource(R.drawable.shimmer_text_bg);
            holder.subjectTag.setBackgroundResource(R.drawable.shimmer_sub_tag_bg);
            holder.subjectTag.setText("       ");
            holder.UserName.setText("                                       ");
            holder.questionText.setBackgroundResource(R.drawable.shimmer_text_bg);
            holder.solvedText.setBackgroundResource(R.drawable.shimmer_text_bg);
            holder.solvedText.setText("                 ");
            holder.DateText.setText("      ");
            holder.solved.setImageResource(R.drawable.shimmer_sub_tag_bg);
            holder.solved.setBackgroundResource(R.drawable.shimmer_sub_tag_bg);
            holder.linearLayout.setVisibility(View.GONE);

            holder.questionText.setText("                                   \n" +
                    "                                                        " +
                    "                                                     " +
                    "                                " +
                    "                                          " +
                    "                          ");


        }else{
            holder.shimmerFrameLayout.stopShimmer();
            holder.shimmerFrameLayout.setShimmer(null);


            {
                String [] imageUrls = new String[0];





                holder.UserName.setText(toTitleCase(DoubtList.get(position).getNameHome()));

                holder.questionText.setText(DoubtList.get(position).getQText());

                Date dateTime = DoubtList.get(position).getQuestionDate();
                assert dateTime != null;
                long mili = dateTime.getTime();
                final String QuestionDate = DateUtils.getRelativeTimeSpanString(mili).toString();
                holder.DateText.setText(QuestionDate);



                Intent intent = new Intent(mContext, DoubtDetails.class);
                intent.putExtra("QuestionImage1Url", DoubtList.get(holder.getAdapterPosition()).getPhoto1url());
                intent.putExtra("QuestionImage2Url", DoubtList.get(holder.getAdapterPosition()).getPhoto2url());
                intent.putExtra("AnsImage1Url", DoubtList.get(holder.getAdapterPosition()).getAnsPhotoUrl1());
                intent.putExtra("AnsImage2Url", DoubtList.get(holder.getAdapterPosition()).getAnsPhotoUrl2());
                intent.putExtra("AnsText", DoubtList.get(holder.getAdapterPosition()).getAnsText());
                intent.putExtra("AudioUrl", DoubtList.get(holder.getAdapterPosition()).getAudioUrl());
                intent.putExtra("QuestionText", DoubtList.get(holder.getAdapterPosition()).getQText());
                intent.putExtra("Board", DoubtList.get(holder.getAdapterPosition()).getBoard());
                intent.putExtra("Chapter", DoubtList.get(holder.getAdapterPosition()).getChapter());
                intent.putExtra("DateTime", DoubtList.get(holder.getAdapterPosition()).getDateTime());
                intent.putExtra("Email", DoubtList.get(holder.getAdapterPosition()).getEmailHome());
                intent.putExtra("FileUrl", DoubtList.get(holder.getAdapterPosition()).getFileUrl());
                intent.putExtra("Link", DoubtList.get(holder.getAdapterPosition()).getLink());
                intent.putExtra("Name", DoubtList.get(holder.getAdapterPosition()).getNameHome());
                intent.putExtra("ProfileImage", DoubtList.get(holder.getAdapterPosition()).getProfileImageURL());
                intent.putExtra("STD", DoubtList.get(holder.getAdapterPosition()).getSTD());
                intent.putExtra("Status", DoubtList.get(holder.getAdapterPosition()).getStatus());
                intent.putExtra("Subject", DoubtList.get(holder.getAdapterPosition()).getSubject());
                intent.putExtra("Teacher", DoubtList.get(holder.getAdapterPosition()).getTeacher());
                intent.putExtra("TeacherImage", DoubtList.get(holder.getAdapterPosition()).getTeacherImageUrl());
                intent.putExtra("QuestionDate", QuestionDate);
                intent.putExtra("Uid", DoubtList.get(holder.getAdapterPosition()).getUid());






                String AnsText = DoubtList.get(position).getAnsText();
                String AnsPhotoUrl1 = DoubtList.get(position).getAnsPhotoUrl1(), AnsPhotoUrl2 = DoubtList.get(position).getAnsPhotoUrl2(), FileUrl = DoubtList.get(position).getFileUrl(),
                        Link = DoubtList.get(position).getLink(), Board = DoubtList.get(position).getBoard(), AudioUrl = DoubtList.get(position).getAudioUrl(),
                        Chapter = DoubtList.get(position).getChapter(), Email = DoubtList.get(position).getEmailHome(), STD = DoubtList.get(position).getSTD(), Teacher = DoubtList.get(position).getTeacher(),
                        Uid = DoubtList.get(position).getUid();



                if (!DoubtList.get(position).getProfileImageURL().equals("")){
                    Picasso.get().load(DoubtList.get(position).getProfileImageURL()).into(holder.profilePictureDC);
                }
                else if (DoubtList.get(position).getProfileImageURL().equals(""))
                {
                    holder.profilePictureDC.setImageResource(R.drawable.profile_defult);

                }

                if (DoubtList.get(position).getPhoto1url().equals("") && DoubtList.get(position).getPhoto2url().equals("")){
                    holder.imageHolder.setVisibility(View.GONE);
                    holder.linearLayout.setVisibility(View.GONE);

                }
                else if (!DoubtList.get(position).getPhoto1url().equals("") && DoubtList.get(position).getPhoto2url().equals("")){
                    holder.imageHolder.setVisibility(View.VISIBLE);
                    imageUrls = new String[]{
                            DoubtList.get(position).getPhoto1url()

                    };


                    ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(mContext, imageUrls, intent);
                    holder.imageHolder.setAdapter(viewPagerAdapter);
                    holder.linearLayout.setVisibility(View.GONE);
                }

                else if (!DoubtList.get(position).getPhoto1url().equals("") && !DoubtList.get(position).getPhoto2url().equals("")){
                    holder.imageHolder.setVisibility(View.VISIBLE);
                    imageUrls = new String[]{
                            DoubtList.get(position).getPhoto1url(), DoubtList.get(position).getPhoto2url()

                    };

                    ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(mContext, imageUrls,intent);
                    holder.imageHolder.setAdapter(viewPagerAdapter);
                    holder.linearLayout.setVisibility(View.VISIBLE);

                }

                holder.imageHolder.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        if (position == 0){
                            holder.linearLayout.setBackgroundResource(R.drawable.ic_dot_first_photo);
                        }
                        else if (position == 1){
                            holder.linearLayout.setBackgroundResource(R.drawable.ic_dots_second_photo);
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                holder.subjectTag.setText(DoubtList.get(position).getSubject());



                switch (DoubtList.get(position).getSubject()){
                    case "Algebra":
                        holder.subjectTag.setBackgroundResource(R.drawable.subject_button_bg);
                        holder.subjectTag.setTextColor(Color.parseColor("#FF2829"));
                        holder.UserName.setTextColor(Color.parseColor("#FF2829"));

                        holder.CardBg.setBackgroundResource(R.drawable.smart_suggestion_card_bg_alg);
                        holder.solved.setBackgroundResource(R.drawable.square_small_bg_alg);
                        if (DoubtList.get(position).getStatus().equals("Unsolved")){
                            holder.solved.setImageResource(R.drawable.ic_round_check_circle_24_grey);
                            holder.solvedText.setText("Unsolved");
                            holder.solved.setBackgroundResource(R.drawable.square_small_bg_grey);
                            holder.solvedText.setTextColor(Color.parseColor("#999999"));

                        }
                        else if (DoubtList.get(position).getStatus().equals("Solved")){
                            holder.solved.setImageResource(R.drawable.ic_round_check_circle_24_alg);
                            holder.solvedText.setText("Solved");
                            holder.solvedText.setTextColor(Color.parseColor("#FF2829"));

                        }

                        break;

                    case "Geometry":
                        holder.subjectTag.setBackgroundResource(R.drawable.subject_button_bg_geom);
                        holder.subjectTag.setTextColor(Color.parseColor("#9A0D91"));
                        holder.UserName.setTextColor(Color.parseColor("#9A0D91"));
                        holder.CardBg.setBackgroundResource(R.drawable.smart_suggestion_card_bg_geom);
                        holder.solved.setBackgroundResource(R.drawable.square_small_bg_geom);
                        if (DoubtList.get(position).getStatus().equals("Unsolved")){
                            holder.solved.setImageResource(R.drawable.ic_round_check_circle_24_grey);
                            holder.solvedText.setText("Unsolved");
                            holder.solvedText.setTextColor(Color.parseColor("#999999"));
                            holder.solved.setBackgroundResource(R.drawable.square_small_bg_grey);
                        }
                        else if (DoubtList.get(position).getStatus().equals("Solved")){
                            holder.solved.setImageResource(R.drawable.ic_round_check_circle_24_geom);
                            holder.solvedText.setText("Solved");
                            holder.solvedText.setTextColor(Color.parseColor("#9A0D91"));
                        }
                        break;

                    case "Physics":
                        holder.subjectTag.setBackgroundResource(R.drawable.subject_button_bg_phy);
                        holder.subjectTag.setTextColor(Color.parseColor("#0078FF"));
                        holder.CardBg.setBackgroundResource(R.drawable.smart_suggestion_card_bg);
                        holder.UserName.setTextColor(Color.parseColor("#0476D9"));
                        holder.solved.setBackgroundResource(R.drawable.square_small_bg);
                        if (DoubtList.get(position).getStatus().equals("Unsolved")){
                            holder.solved.setImageResource(R.drawable.ic_round_check_circle_24_grey);
                            holder.solvedText.setTextColor(Color.parseColor("#999999"));
                            holder.solvedText.setText("Unsolved");
                            holder.solved.setBackgroundResource(R.drawable.square_small_bg_grey);
                        }
                        else if (DoubtList.get(position).getStatus().equals("Solved")){
                            holder.solved.setImageResource(R.drawable.ic_round_check_circle_24);
                            holder.solvedText.setText("Solved");
                            holder.solvedText.setTextColor(Color.parseColor("#0476D9"));
                        }
                        break;

                    case "Chemistry":
                        holder.subjectTag.setBackgroundResource(R.drawable.subject_button_bg_chem);
                        holder.subjectTag.setTextColor(Color.parseColor("#FF9B00"));
                        holder.UserName.setTextColor(Color.parseColor("#FF9B00"));
                        holder.CardBg.setBackgroundResource(R.drawable.smart_suggestion_card_bg_chem);
                        holder.solved.setBackgroundResource(R.drawable.small_square_bg_chem);
                        if (DoubtList.get(position).getStatus().equals("Unsolved")){
                            holder.solved.setImageResource(R.drawable.ic_round_check_circle_24_grey);
                            holder.solvedText.setTextColor(Color.parseColor("#999999"));
                            holder.solvedText.setText("Unsolved");
                            holder.solved.setBackgroundResource(R.drawable.square_small_bg_grey);
                        }
                        else if (DoubtList.get(position).getStatus().equals("Solved")){
                            holder.solved.setImageResource(R.drawable.ic_round_check_circle_24_chem);
                            holder.solvedText.setTextColor(Color.parseColor("#FF9B00"));
                            holder.solvedText.setText("Solved");
                        }
                        break;

                    case "Biology":
                        holder.subjectTag.setBackgroundResource(R.drawable.subject_button_bg_bio);
                        holder.UserName.setTextColor(Color.parseColor("#FF1ADD"));
                        holder.subjectTag.setTextColor(Color.parseColor("#FF1ADD"));
                        holder.CardBg.setBackgroundResource(R.drawable.smart_suggestion_card_bg_bio);
                        holder.solved.setBackgroundResource(R.drawable.small_squar_bg_bio);
                        if (DoubtList.get(position).getStatus().equals("Unsolved")){
                            holder.solved.setImageResource(R.drawable.ic_round_check_circle_24_grey);
                            holder.solvedText.setText("Unsolved");
                            holder.solvedText.setTextColor(Color.parseColor("#999999"));
                            holder.solved.setBackgroundResource(R.drawable.square_small_bg_grey);
                        }
                        else if (DoubtList.get(position).getStatus().equals("Solved")){
                            holder.solved.setImageResource(R.drawable.ic_round_check_circle_24_bio);
                            holder.solvedText.setText("Solved");
                            holder.solvedText.setTextColor(Color.parseColor("#FF1ADD"));
                        }
                        break;

                    case "History":
                        holder.subjectTag.setBackgroundResource(R.drawable.subject_button_bg_his);
                        holder.subjectTag.setTextColor(Color.parseColor("#813912"));
                        holder.solved.setBackgroundResource(R.drawable.small_squar_bg_his);
                        holder.UserName.setTextColor(Color.parseColor("#813912"));

                        holder.CardBg.setBackgroundResource(R.drawable.smart_suggestion_card_bg_his);
                        if (DoubtList.get(position).getStatus().equals("Unsolved")){
                            holder.solved.setImageResource(R.drawable.ic_round_check_circle_24_grey);
                            holder.solvedText.setText("Unsolved");
                            holder.solvedText.setTextColor(Color.parseColor("#999999"));
                            holder.solved.setBackgroundResource(R.drawable.square_small_bg_grey);
                        }
                        else if (DoubtList.get(position).getStatus().equals("Solved")){
                            holder.solved.setImageResource(R.drawable.ic_round_check_circle_24_his);
                            holder.solvedText.setText("Solved");
                            holder.solvedText.setTextColor(Color.parseColor("#813912"));
                        }
                        break;

                    case "Geography":
                        holder.subjectTag.setBackgroundResource(R.drawable.subject_button_bg_geog);
                        holder.UserName.setTextColor(Color.parseColor("#009F37"));
                        holder.subjectTag.setTextColor(Color.parseColor("#009F37"));
                        holder.CardBg.setBackgroundResource(R.drawable.smart_suggestion_card_bg_geog);
                        holder.solved.setBackgroundResource(R.drawable.small_squar_bg_geog);
                        if (DoubtList.get(position).getStatus().equals("Unsolved")){
                            holder.solved.setImageResource(R.drawable.ic_round_check_circle_24_grey);
                            holder.solvedText.setText("Unsolved");
                            holder.solvedText.setTextColor(Color.parseColor("#999999"));
                            holder.solved.setBackgroundResource(R.drawable.square_small_bg_grey);
                        }
                        else if (DoubtList.get(position).getStatus().equals("Solved")){
                            holder.solved.setImageResource(R.drawable.ic_round_check_circle_24_geog);
                            holder.solvedText.setText("Solved");
                            holder.solvedText.setTextColor(Color.parseColor("#009F37"));
                        }
                        break;

                    case "English":
                        holder.subjectTag.setBackgroundResource(R.drawable.subject_button_bg_lang);
                        holder.subjectTag.setTextColor(Color.parseColor("#5550B6"));
                        holder.UserName.setTextColor(Color.parseColor("#5550B6"));
                        holder.CardBg.setBackgroundResource(R.drawable.smart_suggestion_card_bg_lang);
                        holder.solved.setBackgroundResource(R.drawable.small_squar_bg_lang);
                        if (DoubtList.get(position).getStatus().equals("Unsolved")){
                            holder.solved.setImageResource(R.drawable.ic_round_check_circle_24_grey);
                            holder.solvedText.setTextColor(Color.parseColor("#999999"));
                            holder.solvedText.setText("Unsolved");
                            holder.solved.setBackgroundResource(R.drawable.square_small_bg_grey);
                        }
                        else if (DoubtList.get(position).getStatus().equals("Solved")){
                            holder.solved.setImageResource(R.drawable.ic_round_check_circle_24_lang);
                            holder.solvedText.setTextColor(Color.parseColor("#5550B6"));
                            holder.solvedText.setText("Solved");
                        }
                        break;

                    case "Hindi":
                        holder.subjectTag.setBackgroundResource(R.drawable.subject_button_bg_lang);
                        holder.subjectTag.setTextColor(Color.parseColor("#5550B6"));
                        holder.UserName.setTextColor(Color.parseColor("#5550B6"));
                        holder.CardBg.setBackgroundResource(R.drawable.smart_suggestion_card_bg_lang);
                        holder.solved.setBackgroundResource(R.drawable.small_squar_bg_lang);
                        if (DoubtList.get(position).getStatus().equals("Unsolved")){
                            holder.solved.setImageResource(R.drawable.ic_round_check_circle_24_grey);
                            holder.solvedText.setTextColor(Color.parseColor("#999999"));
                            holder.solvedText.setText("Unsolved");
                            holder.solved.setBackgroundResource(R.drawable.square_small_bg_grey);
                        }
                        else if (DoubtList.get(position).getStatus().equals("Solved")){
                            holder.solved.setImageResource(R.drawable.ic_round_check_circle_24_lang);
                            holder.solvedText.setTextColor(Color.parseColor("#5550B6"));
                            holder.solvedText.setText("Solved");
                        }
                        break;


                    case "Marathi":
                        holder.subjectTag.setBackgroundResource(R.drawable.subject_button_bg_lang);
                        holder.subjectTag.setTextColor(Color.parseColor("#5550B6"));
                        holder.UserName.setTextColor(Color.parseColor("#5550B6"));
                        holder.CardBg.setBackgroundResource(R.drawable.smart_suggestion_card_bg_lang);
                        holder.solved.setBackgroundResource(R.drawable.small_squar_bg_lang);
                        if (DoubtList.get(position).getStatus().equals("Unsolved")){
                            holder.solved.setImageResource(R.drawable.ic_round_check_circle_24_grey);
                            holder.solvedText.setTextColor(Color.parseColor("#999999"));
                            holder.solvedText.setText("Unsolved");
                            holder.solved.setBackgroundResource(R.drawable.square_small_bg_grey);
                        }
                        else if (DoubtList.get(position).getStatus().equals("Solved")){
                            holder.solved.setImageResource(R.drawable.ic_round_check_circle_24_lang);
                            holder.solvedText.setTextColor(Color.parseColor("#5550B6"));
                            holder.solvedText.setText("Solved");
                        }
                        break;

                    case "Sanskrit":
                        holder.subjectTag.setBackgroundResource(R.drawable.subject_button_bg_lang);
                        holder.subjectTag.setTextColor(Color.parseColor("#5550B6"));
                        holder.UserName.setTextColor(Color.parseColor("#5550B6"));
                        holder.CardBg.setBackgroundResource(R.drawable.smart_suggestion_card_bg_lang);
                        holder.solved.setBackgroundResource(R.drawable.small_squar_bg_lang);
                        if (DoubtList.get(position).getStatus().equals("Unsolved")){
                            holder.solved.setImageResource(R.drawable.ic_round_check_circle_24_grey);
                            holder.solvedText.setTextColor(Color.parseColor("#999999"));
                            holder.solvedText.setText("Unsolved");
                            holder.solved.setBackgroundResource(R.drawable.square_small_bg_grey);
                        }
                        else if (DoubtList.get(position).getStatus().equals("Solved")){
                            holder.solved.setImageResource(R.drawable.ic_round_check_circle_24_lang);
                            holder.solvedText.setTextColor(Color.parseColor("#5550B6"));
                            holder.solvedText.setText("Solved");
                        }
                        break;


                    case "French":
                        holder.subjectTag.setBackgroundResource(R.drawable.subject_button_bg_lang);
                        holder.subjectTag.setTextColor(Color.parseColor("#5550B6"));
                        holder.UserName.setTextColor(Color.parseColor("#5550B6"));
                        holder.CardBg.setBackgroundResource(R.drawable.smart_suggestion_card_bg_lang);
                        holder.solved.setBackgroundResource(R.drawable.small_squar_bg_lang);
                        if (DoubtList.get(position).getStatus().equals("Unsolved")){
                            holder.solved.setImageResource(R.drawable.ic_round_check_circle_24_grey);
                            holder.solvedText.setTextColor(Color.parseColor("#999999"));
                            holder.solvedText.setText("Unsolved");
                            holder.solved.setBackgroundResource(R.drawable.square_small_bg_grey);
                        }
                        else if (DoubtList.get(position).getStatus().equals("Solved")){
                            holder.solved.setImageResource(R.drawable.ic_round_check_circle_24_lang);
                            holder.solvedText.setTextColor(Color.parseColor("#5550B6"));
                            holder.solvedText.setText("Solved");
                        }
                        break;

                    default:
                        throw new IllegalStateException("Unexpected value: ");


                }






        /*Date dateTimex = DoubtList.get(position).getDateTime();

        assert dateTimex != null;
        long milix = dateTimex.getTime();

        final String DateTime = DateUtils.getRelativeTimeSpanString(milix).toString();*/



                holder.CardBg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, DoubtDetails.class);
                        intent.putExtra("QuestionImage1Url", DoubtList.get(holder.getAdapterPosition()).getPhoto1url());
                        intent.putExtra("QuestionImage2Url", DoubtList.get(holder.getAdapterPosition()).getPhoto2url());
                        intent.putExtra("AnsImage1Url", DoubtList.get(holder.getAdapterPosition()).getAnsPhotoUrl1());
                        intent.putExtra("AnsImage2Url", DoubtList.get(holder.getAdapterPosition()).getAnsPhotoUrl2());
                        intent.putExtra("AnsText", DoubtList.get(holder.getAdapterPosition()).getAnsText());
                        intent.putExtra("AudioUrl", DoubtList.get(holder.getAdapterPosition()).getAudioUrl());
                        intent.putExtra("QuestionText", DoubtList.get(holder.getAdapterPosition()).getQText());
                        intent.putExtra("Board", DoubtList.get(holder.getAdapterPosition()).getBoard());
                        intent.putExtra("Chapter", DoubtList.get(holder.getAdapterPosition()).getChapter());
                        intent.putExtra("DateTime", DoubtList.get(holder.getAdapterPosition()).getDateTime());
                        intent.putExtra("Email", DoubtList.get(holder.getAdapterPosition()).getEmailHome());
                        intent.putExtra("FileUrl", DoubtList.get(holder.getAdapterPosition()).getFileUrl());
                        intent.putExtra("Link", DoubtList.get(holder.getAdapterPosition()).getLink());
                        intent.putExtra("Name", DoubtList.get(holder.getAdapterPosition()).getNameHome());
                        intent.putExtra("ProfileImage", DoubtList.get(holder.getAdapterPosition()).getProfileImageURL());
                        intent.putExtra("STD", DoubtList.get(holder.getAdapterPosition()).getSTD());
                        intent.putExtra("Status", DoubtList.get(holder.getAdapterPosition()).getStatus());
                        intent.putExtra("Subject", DoubtList.get(holder.getAdapterPosition()).getSubject());
                        intent.putExtra("Teacher", DoubtList.get(holder.getAdapterPosition()).getTeacher());
                        intent.putExtra("TeacherImage", DoubtList.get(holder.getAdapterPosition()).getTeacherImageUrl());
                        intent.putExtra("QuestionDate", QuestionDate);
                        intent.putExtra("Uid", DoubtList.get(holder.getAdapterPosition()).getUid());


                        mContext.startActivity(intent);
                    }
                });




            }
        }





    }

    @Override
    public int getItemCount() {


        return shimmer?SHIMMER_ITEMS:DoubtList.size();
    }

    public void filteredList(ArrayList<HomeDoubtData> filteredList) {

        DoubtList = filteredList;
        notifyDataSetChanged();



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


}


class HomeDoubtViewHolder extends RecyclerView.ViewHolder {


    CircleImageView profilePictureDC;
    ImageView saved, solved;
    Button subjectTag;
    ViewPager imageHolder;
    TextView questionText, UserName, DateText, solvedText;
    LinearLayoutCompat linearLayout;
    ConstraintLayout CardBg;
    ShimmerFrameLayout shimmerFrameLayout;

    public HomeDoubtViewHolder(@NonNull View itemView) {
        super(itemView);

        shimmerFrameLayout = itemView.findViewById(R.id.shimmerLayoutHome);
        profilePictureDC = itemView.findViewById(R.id.ProfilePictureDCHome);
        saved = itemView.findViewById(R.id.saved_DCHome);
        solved = itemView.findViewById(R.id.solvedDCHome);
        subjectTag = itemView.findViewById(R.id.subjectTagDChome);
        imageHolder = itemView.findViewById(R.id.imageSliderCardHome);
        questionText = itemView.findViewById(R.id.QuestionTextDCHome);
        UserName = itemView.findViewById(R.id.NameHolderDCHome);
        DateText = itemView.findViewById(R.id.timeText);
        solvedText = itemView.findViewById(R.id.solvedTextDCHome);
        linearLayout = itemView.findViewById(R.id.linearLayout_dotsIndicator);
        CardBg = itemView.findViewById(R.id.cardHome);



    }
}