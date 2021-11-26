package com.psteam.foodlocationbusiness.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

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
        response.setQuantity(Integer.parseInt(remoteMessage.getData().get("quantity")));
        response.setName(remoteMessage.getData().get("name"));

        //notification of foreground
        notify(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), response);
    }

    public void notify(String title, String message, BodySenderFromUser response) {
        // create the intent and set the action
        Intent intent = new Intent(getApplicationContext(), ReserveTableDetailsActivity.class);
        intent.putExtra("response", response);
        intent.setAction("SOME_ACTION");

        // create a pending intent
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notification_channel")
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(123, builder.build());
    }
}
