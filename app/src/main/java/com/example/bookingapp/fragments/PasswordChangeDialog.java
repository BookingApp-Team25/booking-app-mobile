package com.example.bookingapp.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.bookingapp.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class PasswordChangeDialog extends AppCompatDialogFragment {
    private PasswordChangeListener passwordChangeListener;
    public void setPasswordChangeListener(PasswordChangeListener listener) {
        this.passwordChangeListener = listener;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_change_password,null);

        builder.setView(view).setTitle("Password change").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (passwordChangeListener != null) {
                    EditText passwordEditText = view.findViewById(R.id.editTextPassword);
                    EditText repeatedPasswordEditText = view.findViewById(R.id.editTextRepeatPassword);

                    String password = passwordEditText.getText().toString();
                    String repeatedPassword = repeatedPasswordEditText.getText().toString();

                    passwordChangeListener.onChangePassword(password, repeatedPassword);
                }
            }
        });
        return builder.create();
    }

    public interface PasswordChangeListener {
        void onChangePassword(String password, String repeatedPassword);
    }
}
