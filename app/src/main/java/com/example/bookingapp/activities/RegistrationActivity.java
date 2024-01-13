package com.example.bookingapp.activities;

import android.os.Bundle;

import com.example.bookingapp.R;
import com.example.bookingapp.clients.ClientUtils;
import com.example.bookingapp.dto.MessageResponse;
import com.example.bookingapp.dto.RegistrationRequest;
import com.example.bookingapp.dto.enums.Role;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.bookingapp.databinding.ActivityRegistrationBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText, repeatPasswordEditText,
            firstNameEditText, lastNameEditText, phoneNumberEditText, addressEditText;
    private RadioGroup radioGroup;
    private RadioButton radioHost, radioGuest;
    private Button registrationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        usernameEditText = findViewById(R.id.emailTxt);
        passwordEditText = findViewById(R.id.passwordTxt);
        repeatPasswordEditText = findViewById(R.id.rpasswordTxt);
        firstNameEditText = findViewById(R.id.firstnameTxt);
        lastNameEditText = findViewById(R.id.lastnameTxt);
        phoneNumberEditText = findViewById(R.id.phonenumberTxt);
        addressEditText = findViewById(R.id.addressTxt);

        radioGroup = findViewById(R.id.radioGroup);
        radioHost = findViewById(R.id.radioHost);
        radioGuest = findViewById(R.id.radioGuest);

        registrationButton = findViewById(R.id.buttonLogin4);

        radioHost.setChecked(true);

        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleRegistration();
            }
        });
    }

    private void handleRegistration() {

        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String repeatPassword = repeatPasswordEditText.getText().toString();
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String phoneNumber = phoneNumberEditText.getText().toString();
        String address = addressEditText.getText().toString();

        Role role = (radioHost.isChecked()) ? Role.Host : Role.Guest;
        if( !Patterns.EMAIL_ADDRESS.matcher(username).matches()){
            Toast.makeText(RegistrationActivity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
            return;
        }
        if(username.isEmpty() || password.isEmpty() || repeatPassword.isEmpty() || firstName.isEmpty() || lastName.isEmpty() ||
        phoneNumber.isEmpty() || address.isEmpty()){
            Toast.makeText(RegistrationActivity.this, "Fields must not be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.length()<7){
            Toast.makeText(RegistrationActivity.this, "Password should be at least 6 characters long", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!password.equals(repeatPassword)){
            Toast.makeText(RegistrationActivity.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
            return;
        }
        RegistrationRequest registrationRequest = new RegistrationRequest(
                username, password, repeatPassword, firstName, lastName, phoneNumber, address, role);


        Call<MessageResponse> call = ClientUtils.authService.register(registrationRequest);
        call.enqueue(new Callback<MessageResponse>() {
                         @Override
                         public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                             Toast.makeText(RegistrationActivity.this, "Activation link is sent to "+username, Toast.LENGTH_SHORT).show();
                         }

                         @Override
                         public void onFailure(Call<MessageResponse> call, Throwable t) {
                             Toast.makeText(RegistrationActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                         }
                     }

        );
    }
}