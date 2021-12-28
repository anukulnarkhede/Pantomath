package com.cproz.pantomath.Signup;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.cproz.pantomath.Home.Home;
import com.cproz.pantomath.Login.Login;

import java.util.Objects;

public class EmailVerificationPopUp extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("We've sent a verification email")
                .setMessage("Click the link to verify your account")
                .setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        /*startActivity(new Intent(getContext(), Login.class));
                        Intent intent = Objects.requireNonNull(getActivity()).getPackageManager().getLaunchIntentForPackage("com.google.android.gm");
                        startActivity(intent);*/
                        startActivity(new Intent(getContext(), Home.class));

                        //startActivity(new Intent(getContext(), Login.class));

                    }
                });

        return builder.create();
    }
}
