package com.cproz.pantomath.Notifications;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.cproz.pantomath.Home.DoubtDetails;
import com.cproz.pantomath.Home.HomeDoubtData;
import com.cproz.pantomath.R;
import com.jackandphantom.circularimageview.CircleImage;
import com.squareup.picasso.Picasso;


import java.util.Date;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationViewHolder>{


    private Context mContext;
    public List<HomeDoubtData> DoubtList ;

    public NotificationAdapter(Context mContext, List<HomeDoubtData> doubtList) {
        this.mContext = mContext;
        DoubtList = doubtList;
    }



    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notification_card, parent, false);
        return new NotificationViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final NotificationViewHolder holder, int position) {


        Date dateTime = DoubtList.get(position).getDateTime();

        assert dateTime != null;
        long mili = dateTime.getTime();

        final String datex = DateUtils.getRelativeTimeSpanString(mili).toString();

//        holder.timeText.setText(datex);

        holder.subjectTag.setText(DoubtList.get(position).getSubject());

        holder.subjectTag.setText(DoubtList.get(position).getSubject());


        String sourceString = "<b>" + toTitleCase(DoubtList.get(position).getTeacher())  + "</b> " + " has solved your<br>Doubt<br><b>" + datex + "</b>";

       holder.NewQuestionTextNotifications.setText(Html.fromHtml(sourceString));

        if (DoubtList.get(position).getTeacherImageUrl().equals("")){
            holder.profileImage.setImageResource(R.drawable.personal_info);
        }else
        {
            Picasso.get().load(DoubtList.get(position).getTeacherImageUrl()).into(holder.profileImage);
        }

        switch (DoubtList.get(position).getSubject()){
            case "Algebra":
                holder.cardBg.setBackgroundResource(R.drawable.doubt_card_bg);
                holder.subjectTag.setBackgroundResource(R.drawable.subject_button_bg);
                holder.subjectTag.setTextColor(Color.parseColor("#FF2829"));

                break;

            case "Geometry":
                holder.cardBg.setBackgroundResource(R.drawable.doubt_card_bg_geom);
                holder.subjectTag.setBackgroundResource(R.drawable.subject_button_bg_geom);
                holder.subjectTag.setTextColor(Color.parseColor("#9A0D91"));
                break;

            case "Physics":
                holder.cardBg.setBackgroundResource(R.drawable.doubt_card_bg_physics);
                holder.subjectTag.setBackgroundResource(R.drawable.subject_button_bg_phy);
                holder.subjectTag.setTextColor(Color.parseColor("#0078FF"));
                break;

            case "Chemistry":
                holder.cardBg.setBackgroundResource(R.drawable.doubt_card_bg_chem);
                holder.subjectTag.setBackgroundResource(R.drawable.subject_button_bg_chem);
                holder.subjectTag.setTextColor(Color.parseColor("#FF9B00"));
                break;

            case "Biology":
                holder.cardBg.setBackgroundResource(R.drawable.doubt_card_bg_bio);
                holder.subjectTag.setBackgroundResource(R.drawable.subject_button_bg_bio);
                holder.subjectTag.setTextColor(Color.parseColor("#FF1ADD"));
                break;

            case "History":
                holder.cardBg.setBackgroundResource(R.drawable.doubt_card_bg_his);
                holder.subjectTag.setBackgroundResource(R.drawable.subject_button_bg_his);
                holder.subjectTag.setTextColor(Color.parseColor("#813912"));
                break;

            case "Geography":
                holder.cardBg.setBackgroundResource(R.drawable.doubt_card_bg_geog);
                holder.subjectTag.setBackgroundResource(R.drawable.subject_button_bg_geog);
                holder.subjectTag.setTextColor(Color.parseColor("#009F37"));
                break;

            case "Languages":
                holder.cardBg.setBackgroundResource(R.drawable.doubt_card_bg_lang);
                holder.subjectTag.setBackgroundResource(R.drawable.subject_button_bg_lang);
                holder.subjectTag.setTextColor(Color.parseColor("#5550B6"));
                break;
        }



        Date dateTimex = DoubtList.get(position).getQuestionDate();
        assert dateTimex != null;
        long milix = dateTimex.getTime();
        final String QuestionDate = DateUtils.getRelativeTimeSpanString(milix).toString();



        holder.cardBg.setOnClickListener(new View.OnClickListener() {
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


                mContext.startActivity(intent);
            }
        });

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

    @Override
    public int getItemCount() {
        return DoubtList.size();
    }
}


class NotificationViewHolder extends RecyclerView.ViewHolder{

    ConstraintLayout cardBg;
    Button subjectTag;
    TextView NewQuestionTextNotifications;
    CircleImage profileImage;

    public NotificationViewHolder(@NonNull View itemView) {
        super(itemView);

        NewQuestionTextNotifications = itemView.findViewById(R.id.NewDoubtTextNotification);
        cardBg = itemView.findViewById(R.id.notification_card_bg);
        subjectTag = itemView.findViewById(R.id.SubjectTag_Notification);
        profileImage = itemView.findViewById(R.id.profilePicStu);
    }
}
