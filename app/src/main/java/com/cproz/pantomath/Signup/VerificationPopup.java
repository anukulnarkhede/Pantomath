package com.cproz.pantomath.Signup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.cproz.pantomath.Home.Home;

import java.util.Objects;

public class VerificationPopup extends AppCompatDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("We've sent a verification email")
                .setMessage("Click the link to verify your account")
                .setPositiveButton("Go to email app", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        Intent intent = Objects.requireNonNull(getActivity()).getPackageManager().getLaunchIntentForPackage("com.google.android.gm");
                        startActivity(intent);

//                        Intent intent = new Intent(Intent.ACTION_MAIN);
//                        intent.addCategory(Intent.CATEGORY_APP_EMAIL);
//                        Objects.requireNonNull(getContext()).startActivity(intent);

                        //startActivity(new Intent(getContext(), Login.class));

                    }
                });

        return builder.create();
    }

}
