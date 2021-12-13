package com.psteam.foodlocationbusiness.ultilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;

public class DataTokenAndUserId {
    private Context context ;
    private SharedPreferences settings;

    public static FirebaseAuth mAuth;
    public static String mVerificationId;

    public DataTokenAndUserId(Context context) {
        this.context = context;
        settings = context.getSharedPreferences("data", Context.MODE_PRIVATE);
    }

    public void saveToken(String token){
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("token", "Bearer " +  token);
        //Này là định thời gian hết hạn của token
        editor.putLong("expires", System.currentTimeMillis() + 82800000); //23 * 3600000 = 82800000
        editor.apply();
    }

    public String getToken() {
        SharedPreferences.Editor editor = settings.edit();

        //Nếu mà thời gian token hết hạn thì lấy cái token mới
        long expires = settings.getLong("expires", 0);
        if (expires < System.currentTimeMillis()) {
//            GetNewToken getNewToken = new GetNewToken(context);
//            getNewToken.CallAPILoginGetToken();
            return "0";
        }
        String s = settings.getString("token", "");
        return s;
    }

    public void saveUserId(String userId){
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("userId", userId);
        editor.apply();
        editor.commit();
    }

    public String getUserId(){
        return settings.getString("userId","");
    }

    public void saveRestaurantId(String restaurantId){
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("restaurantId", restaurantId);
        editor.apply();
        editor.commit();
    }

    public String getRestaurantId(){
        return settings.getString("restaurantId", "");
    }
}
