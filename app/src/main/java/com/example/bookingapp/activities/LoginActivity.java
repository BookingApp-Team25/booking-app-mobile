package com.example.bookingapp.activities;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookingapp.R;
import com.example.bookingapp.clients.ClientUtils;
import com.example.bookingapp.dto.LoginRequest;
import com.example.bookingapp.dto.LoginResponse;
import com.example.bookingapp.dto.MessageResponse;
import com.example.bookingapp.security.JwtTokenUtil;
import com.example.bookingapp.security.UserInfo;

import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "app_notification_channel";
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    1);
        } else {
            // Permission has already been granted
            // Proceed with your code
        }


        Button btnLogin = findViewById(R.id.buttonLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.e("LoginActivity", "logging in started...");
                EditText emailEditText = findViewById(R.id.emailText);
                EditText passwordEditText = findViewById(R.id.passwordText);

                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                LoginRequest loginRequest = new LoginRequest(email, password);

                Call<LoginResponse> call = ClientUtils.authService.login(loginRequest);

                call.enqueue(new Callback<LoginResponse>() {
                                 @Override
                                 public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                                     if(response.code()==200){
                                         String token=response.body().getJwt();
                                         Log.e("LoginActivity", token);
                                         String username=JwtTokenUtil.getUsername(token);
                                         String role=JwtTokenUtil.getRole(token);
                                         UserInfo.setToken(token);
                                         UserInfo.setUsername(username);
                                         UserInfo.setRole(role);
                                         if(role.equals("ROLE_Host")){
                                             Log.e("Role", "host");
                                             Intent intent = new Intent(LoginActivity.this, HostMainActivity.class);
                                             startActivity(intent);
                                         }
                                         else if(role.equals("ROLE_Admin")){
                                             Log.e("Role", "admin");
                                             promptInfo = new BiometricPrompt.PromptInfo.Builder()
                                                     .setTitle("Admin fingerprint authentication")
                                                     .setSubtitle("Log in using your fingerprint")
                                                     .setNegativeButtonText("CANCEL")
                                                     .build();
                                             BiometricManager biometricManager = BiometricManager.from(LoginActivity.this);
                                             int biometricStatus = biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL);

                                             if (biometricStatus == BiometricManager.BIOMETRIC_SUCCESS) {
                                                 biometricPrompt.authenticate(promptInfo);
                                             } else {
                                                 // Handle cases where biometric authentication is not available
                                                 Toast.makeText(getApplicationContext(),
                                                         "Biometric authentication is not available", Toast.LENGTH_SHORT).show();
                                             }


                                         }
                                         else{
                                             Log.e("Role", "guest");
                                             Intent intent = new Intent(LoginActivity.this, GuestMainActivity.class);
                                             startActivity(intent);
                                         }
                                     } else if (response.code()==403) {
                                         Toast.makeText(LoginActivity.this, "Username and password don't match", Toast.LENGTH_SHORT).show();
                                     }
                                 }

                                 @Override
                                 public void onFailure(Call<LoginResponse> call, Throwable t) {
                                     Toast.makeText(LoginActivity.this, "An error encountered", Toast.LENGTH_SHORT).show();
                                 }
                             }

                );
            }
        });

        TextView btnNewAccount = findViewById(R.id.textNewAccount);
        btnNewAccount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(LoginActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                                "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, AdministratorMainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                                Toast.LENGTH_SHORT)
                        .show();
            }
        });


    }

    @Override
    public void onBackPressed() {
        // Move the task to the background
        super.onBackPressed();
        moveTaskToBack(true);
    }
}