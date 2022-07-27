package com.cproz.pantomath.Login;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.cproz.pantomath.LoginAndRegistrationNew.Login_new;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgetPopup extends AppCompatDialogFragment {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("We've sent a Password reset email")
                .setMessage("Click the link to reset your password")
                .setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        /*startActivity(new Intent(getContext(), Login.class));
                        Intent intent = Objects.requireNonNull(getActivity()).getPackageManager().getLaunchIntentForPackage("com.google.android.gm");
                        startActivity(intent);*/
                        firebaseAuth.signOut();
                        startActivity(new Intent(getContext(), Login_new.class));

                        //startActivity(new Intent(getContext(), Login.class));

                    }
                });

        return builder.create();
    }

}
