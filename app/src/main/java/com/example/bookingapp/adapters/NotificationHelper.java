package com.example.bookingapp.adapters;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.bookingapp.R;
import com.example.bookingapp.activities.HostReservationsActivity;
import com.example.bookingapp.activities.LoginActivity;
import com.example.bookingapp.clients.ClientUtils;
import com.example.bookingapp.dto.MessageResponse;
import com.example.bookingapp.dto.NotificationRequest;
import com.example.bookingapp.security.UserInfo;

import java.time.LocalDateTime;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationHelper {

    private static final String CHANNEL_ID = "app_notification_channel";
    private static final String CHANNEL_NAME = "App Notifications";
    private static final String CHANNEL_DESCRIPTION = "Notifications for the whole app";

    public static void createNotificationChannel(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel("notification_channel", "Notification Channel", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Opis");
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public static void showNotification(Context context, String title, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notification_channel")
                .setSmallIcon(R.drawable.baseline_people_alt_24) // Replace with your icon
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(1, builder.build()); // Notification ID: 1
    }
    public static void createAndSaveNotification(Context context, NotificationRequest notificationRequest){
        NotificationHelper.createNotificationChannel(context);
        String notificationTitle = notificationRequest.getInformation().split(",")[0];
        String notificationText =  notificationRequest.getInformation().split(",")[1];
        NotificationHelper.showNotification(context, notificationTitle, notificationText);
        notificationRequest.setInformation(notificationText);
        Call<MessageResponse> call = ClientUtils.notificationService.sendNotification(notificationRequest,UserInfo.getToken());
        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if(response.code()==200){
                    Toast.makeText(context, "User notified", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(context, "server error sending notification", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Toast.makeText(context, "error sending notification", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
