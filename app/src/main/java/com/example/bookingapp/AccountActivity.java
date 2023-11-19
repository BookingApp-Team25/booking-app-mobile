package com.example.bookingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookingapp.databinding.ActivityAccountBinding;

public class AccountActivity extends AppCompatActivity {

    private ActivityAccountBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        TextView passwordChangeText = binding.accountPasswordChangeText;
        ImageView profilePicture = binding.profilePicture;
        profilePicture.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        Button logOutButton = binding.accountLogoutButton;
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AccountActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        passwordChangeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }
    private void openDialog(){
         PasswordChangeDialog dialog = new PasswordChangeDialog();
         dialog.show(getSupportFragmentManager(), "Change password dialog");
    }
}