package com.psteam.foodlocationbusiness.socket;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.psteam.foodlocationbusiness.socket.models.MessageSenderFromRes;
import com.psteam.foodlocationbusiness.socket.models.MessageSenderFromUser;
import com.psteam.foodlocationbusiness.socket.models.User;

import io.socket.client.Socket;

public class setupSocket {
    //uriGlobal = "https://food-location.herokuapp.com/";
    //uriLocal = "http://192.168.1.4:3030"
    public static String uriLocal = "https://food-location.herokuapp.com/";
    public static String deviceId;
    public static Socket mSocket;

    // get device token from FCM
    public static void getToken(String user, Socket mSocket){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.e("notification_getToken", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        deviceId = task.getResult();

                        Gson gson = new Gson();
                        User user1 = new User(user, deviceId);
                        mSocket.emit("login", gson.toJson(user1));
                        // Log and toast
                        Log.e("notification_getToken", deviceId);

                    }
                });
    }

    //socket.io
    public static void signIn(String user){
        getToken(user, mSocket);
    }

    //reconnect
    public static void reconnect(String user, Socket mSocket){
        Gson gson = new Gson();
        User user1 = new User(user, deviceId);
        mSocket.emit("login", gson.toJson(user1));
    }

    //user send res
    public static void notificationFromUser(MessageSenderFromUser message, Socket mSocket){
        Gson gson = new Gson();

        mSocket.emit("notificationFromUser", gson.toJson(message));
    }

    //reserveTable
    public static void reserveTable(MessageSenderFromRes message){
        Gson gson = new Gson();

        mSocket.emit("notificationFromRes", gson.toJson(message));
    }

    //signout
    public static void signOut(){
        mSocket.emit("signout");
    }
}
