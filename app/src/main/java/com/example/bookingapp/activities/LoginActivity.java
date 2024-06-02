package com.example.bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = findViewById(R.id.buttonLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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
                                         Intent intent = new Intent(LoginActivity.this, GuestMainActivity.class);
                                         startActivity(intent);
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
    }

    @Override
    public void onBackPressed() {
        // Move the task to the background
        super.onBackPressed();
        moveTaskToBack(true);
    }
}