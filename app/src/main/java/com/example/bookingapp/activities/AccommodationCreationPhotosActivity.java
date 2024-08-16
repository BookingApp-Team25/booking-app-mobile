package com.example.bookingapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingapp.R;
import com.example.bookingapp.adapters.accCreationImagesAdapter;
import com.example.bookingapp.clients.ClientUtils;
import com.example.bookingapp.databinding.ActivityAccommodationCreationMapBinding;
import com.example.bookingapp.databinding.ActivityAccommodationCreationPhotosBinding;
import com.example.bookingapp.dto.AccommodationRequest;
import com.example.bookingapp.dto.LoginResponse;
import com.example.bookingapp.dto.MessageResponse;
import com.example.bookingapp.entities.Image;
import com.example.bookingapp.security.UserInfo;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationCreationPhotosActivity extends AppCompatActivity {
    private ActivityAccommodationCreationPhotosBinding binding;
    private RecyclerView photoRecyclerView;
    private Button addImageButton;
    List<Image> photoList;
    private accCreationImagesAdapter adapter;
    ActivityResultLauncher<Intent> resultLauncher;
    private Button creationFinishButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        photoList = new ArrayList<>();
        adapter = new accCreationImagesAdapter(AccommodationCreationPhotosActivity.this, photoList);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_accommodation_creation_photos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding = ActivityAccommodationCreationPhotosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        photoRecyclerView = binding.photosRecyclerView;
        photoRecyclerView.setHasFixedSize(true);
        photoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        photoRecyclerView.setAdapter(adapter);

        if(AccommodationCreationActivity.isEdit){
            for(String base64 : AccommodationCreationActivity.newAccommodationRequest.getPhotos()){
                photoList.add(new Image(base64.split(",")[1]));
            }
        }

        creationFinishButton = binding.creationFinishButton;
        creationFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> byte64Photos = new ArrayList<>();
                for(Image photo : photoList){
                    byte64Photos.add(photo.getBase64());
                }
                AccommodationCreationActivity.newAccommodationRequest.setPhotos(byte64Photos);
                AccommodationCreationActivity.newAccommodationRequest.setHostId(HostMainActivity.hostId.toString());
                if(AccommodationCreationActivity.isEdit){
                    Call<MessageResponse> call = ClientUtils.accommodationUpdateService.editAccommodationRequest(AccommodationCreationActivity.accommodationUUID,AccommodationCreationActivity.newAccommodationRequest,UserInfo.getToken());
                    call.enqueue(new Callback<MessageResponse>() {
                        @Override
                        public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                            if(response.code()==200){
                                Toast.makeText(AccommodationCreationPhotosActivity.this, "Appartment succesfuly edited", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(AccommodationCreationPhotosActivity.this, "FAILED EDDITINGAPARTMENT", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MessageResponse> call, Throwable t) {
                            Toast.makeText(AccommodationCreationPhotosActivity.this, "FAILURE ERROR", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    Call<MessageResponse> call = ClientUtils.accommodationUpdateService.createAccommodationRequest(AccommodationCreationActivity.newAccommodationRequest, UserInfo.getToken());
                    call.enqueue(new Callback<MessageResponse>() {
                        @Override
                        public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                            if(response.code()==200){
                                Toast.makeText(AccommodationCreationPhotosActivity.this, "Successfully added new apartment request", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(AccommodationCreationPhotosActivity.this, "FAILED ADDING APARTMENT", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MessageResponse> call, Throwable t) {
                            Toast.makeText(AccommodationCreationPhotosActivity.this, "FAILURE ERROR", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                AccommodationCreationActivity.accommodationUUID = null;
                AccommodationCreationActivity.newAccommodationRequest = null;
                AccommodationCreationActivity.isEdit = false;

                Intent intent = new Intent(AccommodationCreationPhotosActivity.this, HostMainActivity.class);
                startActivity(intent);
            }
        });

        addImageButton = binding.addPhotoButton;
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,3);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null){
            Uri selectedImageUri = data.getData();
            try {
                InputStream imageStream = getContentResolver().openInputStream(selectedImageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                String base64String = encodeImageToBase64(selectedImage);
                photoList.add(new Image(base64String));
                Log.e("image added:", String.valueOf(photoList.size()));
                adapter.notifyDataSetChanged();
                Toast.makeText(AccommodationCreationPhotosActivity.this, "Photo added", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private String encodeImageToBase64(Bitmap image) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}