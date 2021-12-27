package com.psteam.foodlocationbusiness.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.psteam.foodlocationbusiness.R;
import com.psteam.foodlocationbusiness.activites.ReserveTableDetailsActivity;
import com.psteam.foodlocationbusiness.socket.models.BodySenderFromUser;

public class ServiceFCM extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        //String action = remoteMessage.getNotification().getClickAction();

        BodySenderFromUser response = new BodySenderFromUser();
        response.setUserId(remoteMessage.getData().get("userId"));
        response.setTime(remoteMessage.getData().get("time"));
        response.setRestaurantId(remoteMessage.getData().get("restaurantId"));
        response.setPromotionId(remoteMessage.getData().get("promotionId"));
        response.setPhone(remoteMessage.getData().get("phone"));
        response.setNote(remoteMessage.getData().get("note"));
        response.setReserveTableId(remoteMessage.getData().get("reserveTableId"));
        if(remoteMessage.getData().get("quantity") != null) {
            response.setQuantity(Integer.parseInt(remoteMessage.getData().get("quantity")));
        }
        response.setName(remoteMessage.getData().get("name"));

        //notification of foreground
        notify(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), response);
    }

    public void notify(String title, String message, BodySenderFromUser response) {
        String GROUP_KEY_WORK_EMAIL = "com.psteam.foodlocationbusiness.NOTIFICATION";
        String channelId = "location_notification_channel";
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // create the intent and set the action
        Intent intent = new Intent(getApplicationContext(), ReserveTableDetailsActivity.class);
        intent.putExtra("response", response);
        intent.setAction("SOME_ACTION");

        // create a pending intent
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.icon_tasty)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setGroup(GROUP_KEY_WORK_EMAIL);


        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null && notificationManager.getNotificationChannel(channelId) == null) {
                NotificationChannel notificationChannel = new NotificationChannel(
                        channelId,
                        "Location Service",
                        NotificationManager.IMPORTANCE_HIGH
                );
                notificationChannel.setDescription("This channel is used by location service");
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(123, builder.build());
    }
}
