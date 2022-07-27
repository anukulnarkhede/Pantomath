package com.cproz.pantomath.Signup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.cproz.pantomath.AdditionalInfo.AdditionalInfo;
import com.cproz.pantomath.Login.Login;
import com.cproz.pantomath.LoginAndRegistrationNew.Login_new;
import com.cproz.pantomath.Signup.ProfilePictureSignup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RetryPopup extends AppCompatDialogFragment {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Error")
                .setMessage("There was some problem sending verification email")
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AdditionalInfo additionalInfo = new AdditionalInfo();
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        additionalInfo.verification(user);

                        //startActivity(new Intent(getContext(), Login.class));

                    }
                }).setNegativeButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getContext(), Login_new.class));
            }
        });

        return builder.create();
    }

}
