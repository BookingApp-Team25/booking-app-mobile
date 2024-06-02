package com.example.bookingapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookingapp.clients.ClientUtils;
import com.example.bookingapp.clients.UserService;
import com.example.bookingapp.dto.AccountDetailsResponse;
import com.example.bookingapp.dto.AccountEditRequest;
import com.example.bookingapp.dto.MessageResponse;
import com.example.bookingapp.fragments.PasswordChangeDialog;
import com.example.bookingapp.databinding.ActivityAccountBinding;
import com.example.bookingapp.security.UserInfo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountActivity extends AppCompatActivity implements PasswordChangeDialog.PasswordChangeListener {

    private ActivityAccountBinding binding;
    String password="";
    String repeatPassword="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Button editButton=binding.accountModificationsAccept;
        Button deleteButton=binding.btnDelete;
        TextView passwordChangeText = binding.accountPasswordChangeText;
        ImageView profilePicture = binding.profilePicture;
        profilePicture.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);

        Call<AccountDetailsResponse> call = ClientUtils.userService.getAccountDetails(UserInfo.getUsername(), UserInfo.getToken());
        call.enqueue(new Callback<AccountDetailsResponse>() {
            @Override
            public void onResponse(Call<AccountDetailsResponse> call, Response<AccountDetailsResponse> response) {
                if(response.isSuccessful()){
                    binding.textView.setText(response.body().getUsername());
                    binding.accountUserName.setText(response.body().getFirstName());
                    binding.accountUserSurname.setText(response.body().getLastName());
                    binding.editTextPhone.setText(response.body().getPhoneNumber());
                    binding.editTextTextPostalAddress.setText(response.body().getAddress());
                }
            }

            @Override
            public void onFailure(Call<AccountDetailsResponse> call, Throwable t) {

            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName=binding.accountUserName.getText().toString();
                String lastname=binding.accountUserSurname.getText().toString();
                String phone=binding.editTextPhone.getText().toString();
                String address=binding.editTextTextPostalAddress.getText().toString();
                AccountEditRequest accountEditRequest=new AccountEditRequest(password,repeatPassword,firstName,lastname
                ,phone,address);
                Call<MessageResponse> call=ClientUtils.userService.editAccount(UserInfo.getUsername(),accountEditRequest,UserInfo.getToken());
                call.enqueue(new Callback<MessageResponse>() {
                    @Override
                    public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                        Toast.makeText(AccountActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<MessageResponse> call, Throwable t) {

                    }
                });
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();
            }
        });

        passwordChangeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }
    @Override
    public void onChangePassword(String password, String repeatedPassword) {
        this.password=password;
        this.repeatPassword=repeatedPassword;
    }

    private void deleteAccount() {
        Call<MessageResponse> call2 = ClientUtils.userService.deleteAccount(UserInfo.getUsername(), UserInfo.getToken());
        call2.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call2, Response<MessageResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AccountActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    UserInfo.reset();
                    startActivity(new Intent(AccountActivity.this, LoginActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call2, Throwable t) {
                // Handle failure
            }
        });
    }

    private void openDialog() {
        PasswordChangeDialog dialog = new PasswordChangeDialog();
        dialog.setPasswordChangeListener(this);
        dialog.show(getSupportFragmentManager(), "Change password dialog");
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to delete your account?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked Yes, proceed with the deletion
                deleteAccount();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked No, do nothing
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}