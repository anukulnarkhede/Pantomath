package com.cproz.pantomath.Upload;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.cproz.pantomath.Home.Home;
import com.cproz.pantomath.R;

import java.util.Timer;
import java.util.TimerTask;

public class DoubtUploadedPopUp extends DialogFragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.doubt_uploaded_dialogue, container,false);
        Timer timer;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(getContext(),Home.class ));
            }
        }, 2500);

        return root;
    }
}
