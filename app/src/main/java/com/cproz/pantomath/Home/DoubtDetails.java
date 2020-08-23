package com.cproz.pantomath.Home;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cproz.pantomath.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoubtDetails extends AppCompatActivity {

    Toolbar toolbar;
    CircleImageView StudentProfilePic, TeacherProfilePic;
    TextView StudentUserName, TeacherUserName, TimeText1, TimeText2, QuestionText, AnswerText, Solved;
    ViewPager viewPager, viewPagerAns;
    ImageView SolvedIcon, upArrow;
    Button SubjectTag,Play, Pause;
    SeekBar seekBar;
    Chronometer chronometer;
    LinearLayoutCompat linearLayout, AnslinearLayoutCompat,AudioPlayer;
    ConstraintLayout constraintLayout;
    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    Runnable runnable;
    String AudioUrl;

    Intent intent;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doubt_details);

        Initialisation();
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);


        final Bundle bundle = getIntent().getExtras();
        assert bundle != null;


        intent = new Intent(DoubtDetails.this, Image.class);

        StudentUserName.setText(toTitleCase(Objects.requireNonNull(bundle.getString("Name"))));

        if (Objects.equals(bundle.getString("Link"), "")){
            AnswerText.setText(bundle.getString("AnsText"));
        }
        else
        {
            String AnswerTextx = bundle.getString("AnsText") + "\n\nHere's a link for you: " + bundle.getString("Link");

            AnswerText.setText(AnswerTextx);
        }






        if (Objects.equals(bundle.getString("ProfileImage"), "")){
            StudentProfilePic.setImageResource(R.drawable.personal_info);
        }
        else if (!Objects.equals(bundle.getString("ProfileImage"), "")){
            Picasso.get().load(bundle.getString("ProfileImage")).into(StudentProfilePic);
        }

        if (Objects.equals(bundle.getString("TeacherImage"), "")){
            TeacherProfilePic.setImageResource(R.drawable.personal_info);
        }
        else
            if (!Objects.equals(bundle.getString("TeacherImage"), "")){
                Picasso.get().load(bundle.getString("TeacherImage")).into(TeacherProfilePic);
            }

            QuestionText.setText(bundle.getString("QuestionText"));


        switch (Objects.requireNonNull(bundle.getString("Subject"))){
            case "Algebra":
                SubjectTag.setBackgroundResource(R.drawable.subject_button_bg);
                SubjectTag.setTextColor(Color.parseColor("#FF2829"));
                StudentUserName.setTextColor(Color.parseColor("#FF2829"));
                TeacherUserName.setTextColor(Color.parseColor("#FF2829"));
                SubjectTag.setText("Algebra");
                constraintLayout.setBackgroundResource(R.drawable.doubt_card_bg);

                if (Objects.equals(bundle.getString("Status"), "Unsolved")){
                    SolvedIcon.setImageResource(R.drawable.ic_round_check_circle_24_grey);
                    Solved.setText("Unsolved");
                    Solved.setTextColor(Color.parseColor("#999999"));
                    SolvedIcon.setBackgroundResource(R.drawable.square_small_bg_grey);
                    constraintLayout.setVisibility(View.GONE);
                    upArrow.setVisibility(View.GONE);
                }
                else if (Objects.equals(bundle.getString("Status"), "Solved")){
                    SolvedIcon.setImageResource(R.drawable.ic_round_check_circle_24_alg);
                    SolvedIcon.setBackgroundResource(R.drawable.square_small_bg_alg);
                    Solved.setText("Solved");
                    Solved.setTextColor(Color.parseColor("#FF2829"));
                    constraintLayout.setVisibility(View.VISIBLE);
                    upArrow.setVisibility(View.VISIBLE);
                    Play.setBackgroundResource(R.drawable.play_red);
                    Pause.setBackgroundResource(R.drawable.pause_red);
                    AudioPlayer.setBackgroundResource(R.drawable.text_view_bg_red);
                    chronometer.setTextColor(Color.parseColor("#FF2829"));
                    if (Objects.equals(bundle.getString("AudioUrl"), "")){
                        AudioPlayer.setVisibility(View.GONE);
                    }

                }

                break;

            case "Geometry":

                SubjectTag.setBackgroundResource(R.drawable.subject_button_bg_geom);
                constraintLayout.setBackgroundResource(R.drawable.doubt_card_bg_geom);
                SubjectTag.setTextColor(Color.parseColor("#9A0D91"));
                StudentUserName.setTextColor(Color.parseColor("#9A0D91"));
                TeacherUserName.setTextColor(Color.parseColor("#9A0D91"));
                SubjectTag.setText("Geometry");
                if (Objects.equals(bundle.getString("Status"), "Unsolved")){
                    SolvedIcon.setImageResource(R.drawable.ic_round_check_circle_24_grey);
                    Solved.setText("Unsolved");
                    Solved.setTextColor(Color.parseColor("#999999"));
                    SolvedIcon.setBackgroundResource(R.drawable.square_small_bg_grey);
                    constraintLayout.setVisibility(View.GONE);
                    upArrow.setVisibility(View.GONE);

                }
                else if (Objects.equals(bundle.getString("Status"), "Solved")){
                    SolvedIcon.setImageResource(R.drawable.ic_round_check_circle_24_geom);
                    SolvedIcon.setBackgroundResource(R.drawable.square_small_bg_geom);
                    Solved.setText("Solved");
                    Solved.setTextColor(Color.parseColor("#9A0D91"));
                    constraintLayout.setVisibility(View.VISIBLE);
                    upArrow.setVisibility(View.VISIBLE);
                    Play.setBackgroundResource(R.drawable.play_geometry);
                    Pause.setBackgroundResource(R.drawable.pause_geom);
                    AudioPlayer.setBackgroundResource(R.drawable.text_view_bg_geom);
                    chronometer.setTextColor(Color.parseColor("#9A0D91"));
                    if (Objects.equals(bundle.getString("AudioUrl"), "")){
                        AudioPlayer.setVisibility(View.GONE);
                    }

                }

                break;

            case "Physics":

                SubjectTag.setBackgroundResource(R.drawable.subject_button_bg_phy);
                constraintLayout.setBackgroundResource(R.drawable.doubt_card_bg_physics);
                SubjectTag.setTextColor(Color.parseColor("#0078FF"));
                StudentUserName.setTextColor(Color.parseColor("#0078FF"));
                TeacherUserName.setTextColor(Color.parseColor("#0078FF"));
                SubjectTag.setText("Physics");

                if (Objects.equals(bundle.getString("Status"), "Unsolved")){
                    SolvedIcon.setImageResource(R.drawable.ic_round_check_circle_24_grey);
                    Solved.setText("Unsolved");
                    Solved.setTextColor(Color.parseColor("#999999"));
                    SolvedIcon.setBackgroundResource(R.drawable.square_small_bg_grey);
                    constraintLayout.setVisibility(View.GONE);
                    upArrow.setVisibility(View.GONE);

                }
                else if (Objects.equals(bundle.getString("Status"), "Solved")){
                    SolvedIcon.setImageResource(R.drawable.ic_round_check_circle_24);
                    SolvedIcon.setBackgroundResource(R.drawable.square_small_bg);
                    Solved.setText("Solved");
                    Solved.setTextColor(Color.parseColor("#0078FF"));
                    constraintLayout.setVisibility(View.VISIBLE);
                    upArrow.setVisibility(View.VISIBLE);
                    Play.setBackgroundResource(R.drawable.play_blue);
                    Pause.setBackgroundResource(R.drawable.pause_blue);
                    AudioPlayer.setBackgroundResource(R.drawable.text_view_bg);
                    chronometer.setTextColor(Color.parseColor("#0078FF"));
                    if (Objects.equals(bundle.getString("AudioUrl"), "")){
                        AudioPlayer.setVisibility(View.GONE);
                    }

                }

                break;

            case "Chemistry":

                SubjectTag.setBackgroundResource(R.drawable.subject_button_bg_chem);
                constraintLayout.setBackgroundResource(R.drawable.doubt_card_bg_chem);
                SubjectTag.setTextColor(Color.parseColor("#FF9B00"));
                StudentUserName.setTextColor(Color.parseColor("#FF9B00"));
                TeacherUserName.setTextColor(Color.parseColor("#FF9B00"));
                SubjectTag.setText("Chemistry");


                if (Objects.equals(bundle.getString("Status"), "Unsolved")){
                    SolvedIcon.setImageResource(R.drawable.ic_round_check_circle_24_grey);
                    Solved.setText("Unsolved");
                    Solved.setTextColor(Color.parseColor("#999999"));
                    SolvedIcon.setBackgroundResource(R.drawable.square_small_bg_grey);
                    constraintLayout.setVisibility(View.GONE);
                    upArrow.setVisibility(View.GONE);

                }
                else if (Objects.equals(bundle.getString("Status"), "Solved")){
                    SolvedIcon.setImageResource(R.drawable.ic_round_check_circle_24_chem);
                    SolvedIcon.setBackgroundResource(R.drawable.small_square_bg_chem);
                    if (Objects.equals(bundle.getString("AudioUrl"), "")){
                        AudioPlayer.setVisibility(View.GONE);
                    }

                    Solved.setText("Solved");
                    Solved.setTextColor(Color.parseColor("#FF9B00"));
                    constraintLayout.setVisibility(View.VISIBLE);
                    upArrow.setVisibility(View.VISIBLE);
                    Play.setBackgroundResource(R.drawable.play_chem);
                    Pause.setBackgroundResource(R.drawable.pause_chem);
                    AudioPlayer.setBackgroundResource(R.drawable.text_view_bg_chem);
                    chronometer.setTextColor(Color.parseColor("#FF9B00"));
                }

                break;

            case "Biology":

                SubjectTag.setBackgroundResource(R.drawable.subject_button_bg_bio);
                constraintLayout.setBackgroundResource(R.drawable.doubt_card_bg_bio);
                SubjectTag.setTextColor(Color.parseColor("#FF1ADD"));
                StudentUserName.setTextColor(Color.parseColor("#FF1ADD"));
                TeacherUserName.setTextColor(Color.parseColor("#FF1ADD"));
                SubjectTag.setText("Biology");

                if (Objects.equals(bundle.getString("Status"), "Unsolved")){
                    SolvedIcon.setImageResource(R.drawable.ic_round_check_circle_24_grey);
                    Solved.setText("Unsolved");
                    Solved.setTextColor(Color.parseColor("#999999"));
                    SolvedIcon.setBackgroundResource(R.drawable.square_small_bg_grey);
                    constraintLayout.setVisibility(View.GONE);
                    upArrow.setVisibility(View.GONE);

                }
                else if (Objects.equals(bundle.getString("Status"), "Solved")){
                    SolvedIcon.setImageResource(R.drawable.ic_round_check_circle_24_bio);
                    SolvedIcon.setBackgroundResource(R.drawable.small_squar_bg_bio);
                    if (Objects.equals(bundle.getString("AudioUrl"), "")){
                        AudioPlayer.setVisibility(View.GONE);
                    }

                    Solved.setText("Solved");
                    Solved.setTextColor(Color.parseColor("#FF1ADD"));
                    constraintLayout.setVisibility(View.VISIBLE);
                    upArrow.setVisibility(View.VISIBLE);
                    Play.setBackgroundResource(R.drawable.play_bio);
                    Pause.setBackgroundResource(R.drawable.pause_bio);
                    AudioPlayer.setBackgroundResource(R.drawable.text_view_bg_bio);
                    chronometer.setTextColor(Color.parseColor("#FF1ADD"));
                }

                break;

            case "History":

                SubjectTag.setBackgroundResource(R.drawable.subject_button_bg_his);
                constraintLayout.setBackgroundResource(R.drawable.doubt_card_bg_his);
                SubjectTag.setTextColor(Color.parseColor("#813912"));
                StudentUserName.setTextColor(Color.parseColor("#813912"));
                TeacherUserName.setTextColor(Color.parseColor("#813912"));
                SubjectTag.setText("History");


                if (Objects.equals(bundle.getString("Status"), "Unsolved")){
                    SolvedIcon.setImageResource(R.drawable.ic_round_check_circle_24_grey);
                    Solved.setText("Unsolved");
                    Solved.setTextColor(Color.parseColor("#999999"));
                    SolvedIcon.setBackgroundResource(R.drawable.square_small_bg_grey);
                    constraintLayout.setVisibility(View.GONE);
                    upArrow.setVisibility(View.GONE);

                }
                else if (Objects.equals(bundle.getString("Status"), "Solved")){
                    SolvedIcon.setImageResource(R.drawable.ic_round_check_circle_24_his);
                    SolvedIcon.setBackgroundResource(R.drawable.small_squar_bg_his);
                    Solved.setText("Solved");
                    Solved.setTextColor(Color.parseColor("#813912"));
                    if (Objects.equals(bundle.getString("AudioUrl"), "")){
                        AudioPlayer.setVisibility(View.GONE);
                    }

                    constraintLayout.setVisibility(View.VISIBLE);
                    upArrow.setVisibility(View.VISIBLE);
                    Play.setBackgroundResource(R.drawable.play_his);
                    Pause.setBackgroundResource(R.drawable.pause_his);
                    AudioPlayer.setBackgroundResource(R.drawable.text_view_bg_his);
                    chronometer.setTextColor(Color.parseColor("#813912"));
                }

                break;

            case "Geography":

                SubjectTag.setBackgroundResource(R.drawable.subject_button_bg_geog);
                constraintLayout.setBackgroundResource(R.drawable.doubt_card_bg_geog);
                SubjectTag.setTextColor(Color.parseColor("#009F37"));
                StudentUserName.setTextColor(Color.parseColor("#009F37"));
                TeacherUserName.setTextColor(Color.parseColor("#009F37"));
                SubjectTag.setText("Geography");


                if (Objects.equals(bundle.getString("Status"), "Unsolved")){
                    SolvedIcon.setImageResource(R.drawable.ic_round_check_circle_24_grey);
                    Solved.setText("Unsolved");
                    Solved.setTextColor(Color.parseColor("#999999"));
                    SolvedIcon.setBackgroundResource(R.drawable.square_small_bg_grey);
                    constraintLayout.setVisibility(View.GONE);
                    upArrow.setVisibility(View.GONE);

                }
                else if (Objects.equals(bundle.getString("Status"), "Solved")){
                    SolvedIcon.setImageResource(R.drawable.ic_round_check_circle_24_geog);
                    SolvedIcon.setBackgroundResource(R.drawable.small_squar_bg_geog);
                    Solved.setText("Solved");
                    Solved.setTextColor(Color.parseColor("#009F37"));
                    if (Objects.equals(bundle.getString("AudioUrl"), "")){
                        AudioPlayer.setVisibility(View.GONE);
                    }

                    constraintLayout.setVisibility(View.VISIBLE);
                    upArrow.setVisibility(View.VISIBLE);
                    Play.setBackgroundResource(R.drawable.play_geog);
                    Pause.setBackgroundResource(R.drawable.pause_geog);
                    AudioPlayer.setBackgroundResource(R.drawable.text_view_bg_geog);
                    chronometer.setTextColor(Color.parseColor("#009F37"));
                }

                break;

            case "Languages":

                SubjectTag.setBackgroundResource(R.drawable.subject_button_bg_lang);
                constraintLayout.setBackgroundResource(R.drawable.doubt_card_bg_lang);
                SubjectTag.setTextColor(Color.parseColor("#5550B6"));
                StudentUserName.setTextColor(Color.parseColor("#5550B6"));
                TeacherUserName.setTextColor(Color.parseColor("#5550B6"));
                SubjectTag.setText("Languages");


                if (Objects.equals(bundle.getString("Status"), "Unsolved")){
                    SolvedIcon.setImageResource(R.drawable.ic_round_check_circle_24_grey);
                    Solved.setText("Unsolved");
                    Solved.setTextColor(Color.parseColor("#999999"));
                    SolvedIcon.setBackgroundResource(R.drawable.square_small_bg_grey);
                    constraintLayout.setVisibility(View.GONE);
                    upArrow.setVisibility(View.GONE);

                }
                else if (Objects.equals(bundle.getString("Status"), "Solved")){
                    SolvedIcon.setImageResource(R.drawable.ic_round_check_circle_24_lang);
                    SolvedIcon.setBackgroundResource(R.drawable.small_squar_bg_lang);
                    Solved.setText("Solved");
                    Solved.setTextColor(Color.parseColor("#5550B6"));
                    if (Objects.equals(bundle.getString("AudioUrl"), "")){
                        AudioPlayer.setVisibility(View.GONE);
                    }

                    constraintLayout.setVisibility(View.VISIBLE);
                    upArrow.setVisibility(View.VISIBLE);
                    Play.setBackgroundResource(R.drawable.play_lang);
                    Pause.setBackgroundResource(R.drawable.pause_lang);
                    AudioPlayer.setBackgroundResource(R.drawable.text_view_bg_lang);
                    chronometer.setTextColor(Color.parseColor("#5550B6"));
                }

                break;

            default:
                throw new IllegalStateException("Unexpected value: ");


        }



        if (bundle.getString("AudioUrl").equals("")){
            AudioPlayer.setVisibility(View.GONE);
        }else if (!bundle.getString("AudioUrl").equals("")){
            AudioPlayer.setVisibility(View.VISIBLE);
            Play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StreamAudio(bundle);
                    Play.setVisibility(View.GONE);
                    Pause.setVisibility(View.VISIBLE);
                }
            });


        }

        Pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                handler.removeCallbacks(runnable);
                Play.setVisibility(View.VISIBLE);
                Pause.setVisibility(View.GONE);
            }
        });


        TimeText1.setText(bundle.getString("QuestionDate"));


        try {
            Date dateTimex = (Date) bundle.get("DateTime");

            assert dateTimex != null;
            long milix = dateTimex.getTime();

            final String DateTime = DateUtils.getRelativeTimeSpanString(milix).toString();


            TimeText2.setText(DateTime);
        }
        catch (Exception e){
            System.out.println("error occured "+e);
        }





        TeacherUserName.setText(bundle.getString("Teacher"));


        String[] imageUrls = new String[0];


        if (Objects.equals(bundle.getString("QuestionImage1Url"), "") && Objects.equals(bundle.getString("QuestionImage2Url"), "")){
            viewPager.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);

        }
        else if (!Objects.equals(bundle.getString("QuestionImage1Url"), "") && Objects.equals(bundle.getString("QuestionImage2Url"), "")){
            viewPager.setVisibility(View.VISIBLE);

            imageUrls = new String[] {
                    bundle.getString("QuestionImage1Url")
            };

            ViewPagerAdapterx viewPagerAdapterx = new ViewPagerAdapterx(this, imageUrls);
            viewPager.setAdapter(viewPagerAdapterx);
            linearLayout.setVisibility(View.GONE);

            }
            else if (!Objects.equals(bundle.getString("QuestionImage1Url"), "") && !Objects.equals(bundle.getString("QuestionImage2Url"), "")){


            viewPager.setVisibility(View.VISIBLE);

            imageUrls = new String[] {
                    bundle.getString("QuestionImage1Url"),  bundle.getString("QuestionImage2Url")
            };

            ViewPagerAdapterx viewPagerAdapterx = new ViewPagerAdapterx(this, imageUrls);
            viewPager.setAdapter(viewPagerAdapterx);
            linearLayout.setVisibility(View.VISIBLE);


        }



        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0){
                    linearLayout.setBackgroundResource(R.drawable.ic_dot_first_photo);



                    intent.putExtra("Photo1", bundle.getString("QuestionImage1Url"));




                }
                else if (position == 1){
                    linearLayout.setBackgroundResource(R.drawable.ic_dots_second_photo);

                    intent.putExtra("Photo1", bundle.getString("QuestionImage2Url"));



                }





            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        String[] imageUrls1 = new String[0];


        if (Objects.equals(bundle.getString("AnsImage1Url"), "") && Objects.equals(bundle.getString("AnsImage2Url"), "")){
            viewPagerAns.setVisibility(View.GONE);
            AnslinearLayoutCompat.setVisibility(View.GONE);

        }
        else if (!Objects.equals(bundle.getString("AnsImage1Url"), "") && Objects.equals(bundle.getString("AnsImage2Url"), "")){
            viewPagerAns.setVisibility(View.VISIBLE);

            imageUrls1 = new String[] {
                    bundle.getString("AnsImage1Url")
            };

            ViewPagerAdapterx viewPagerAdapterx = new ViewPagerAdapterx(this, imageUrls1);
            viewPagerAns.setAdapter(viewPagerAdapterx);
            AnslinearLayoutCompat.setVisibility(View.GONE);

        }
        else if (!Objects.equals(bundle.getString("AnsImage1Url"), "") && !Objects.equals(bundle.getString("AnsImage2Url"), "")){


            viewPagerAns.setVisibility(View.VISIBLE);

            imageUrls1 = new String[] {
                    bundle.getString("AnsImage1Url"),  bundle.getString("AnsImage2Url")
            };

            ViewPagerAdapterx viewPagerAdapterx = new ViewPagerAdapterx(this, imageUrls1);
            viewPagerAns.setAdapter(viewPagerAdapterx);
            AnslinearLayoutCompat.setVisibility(View.VISIBLE);


        }

        viewPagerAns.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0){
                    AnslinearLayoutCompat.setBackgroundResource(R.drawable.ic_dot_first_photo);
                }
                else if (position == 1){
                    AnslinearLayoutCompat.setBackgroundResource(R.drawable.ic_dots_second_photo);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });







    }

    public void Initialisation(){
        toolbar = findViewById(R.id.DoubtDetailsToolBar);
        StudentProfilePic = findViewById(R.id.ProfilePictureDoubtsDetails);
        TeacherProfilePic = findViewById(R.id.ProfilePictureTeacherDoubtsDetails);
        StudentUserName = findViewById(R.id.NameHolderDoubtDetails);
        TeacherUserName = findViewById(R.id.NameHolderDoubtDetailsTeacher);
        TimeText1 = findViewById(R.id.timeTextDD);
        TimeText2 = findViewById(R.id.timeTextDDTeacher);
        QuestionText = findViewById(R.id.QuestionTextDD);
        AnswerText = findViewById(R.id.AnswerTextDD);
        Solved = findViewById(R.id.solvedTextDD);
        viewPager = findViewById(R.id.imageSliderDD);
        SolvedIcon = findViewById(R.id.solvedDD);
        linearLayout = findViewById(R.id.linearLayout_dotsIndicator_DD);
        SubjectTag = findViewById(R.id.subjectTagDD);
        constraintLayout = findViewById(R.id.cardHomeDD);
        upArrow = findViewById(R.id.UpArrow);
        viewPagerAns = findViewById(R.id.AnsimageSliderDD);
        AnslinearLayoutCompat = findViewById(R.id.AnslinearLayout_dotsIndicator_DD);
        AudioPlayer = findViewById(R.id.TimerRecordDD);
        seekBar = findViewById(R.id.audioSeekBarDD);
        Play = findViewById(R.id.PlayPauseDD);
        Pause = findViewById(R.id.PauseDD);
        chronometer = findViewById(R.id.timerDD);

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


    public void StreamAudio(Bundle bundle){



        runnable = new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 00);


            }
        };

        AudioUrl = bundle.getString("AudioUrl");
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);


        try {
            mediaPlayer.setDataSource(AudioUrl);
            //loding
            mediaPlayer.prepare();
            int duration = mediaPlayer.getDuration();
            @SuppressLint("DefaultLocale") String time = String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(duration),
                    TimeUnit.MILLISECONDS.toSeconds(duration) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
            );
            chronometer.setText(time);

        } catch (IOException e) {
            e.printStackTrace();
        }


        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration()-90);
        handler.postDelayed(runnable, 0);
        mediaPlayer.setOnCompletionListener(cListener);





    }

    MediaPlayer.OnCompletionListener cListener = new MediaPlayer.OnCompletionListener(){

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void onCompletion(MediaPlayer mp){

            Pause.setVisibility(View.GONE);
            Play.setVisibility(View.VISIBLE);
            //seekBar.setProgress(1);

        }
    };

    @Override
    protected void onResume() {


        super.onResume();


    }

    @Override
    protected void onPause() {
        try {
            mediaPlayer.stop();
        }catch (Exception e){
            System.out.println(e);
        }
        super.onPause();
    }
}