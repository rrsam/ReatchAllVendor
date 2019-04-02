package com.reatchall.charan.reatchallVendor.Utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.reatchall.charan.reatchallVendor.Intro.SplashActivity;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Vendor.Orders.VendorCurrentOrderActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMessagingServ";
    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.e(TAG, "onNewToken: "+token );
        if(PrefManager.isVendorLoggedIn(MyFirebaseMessagingService.this))
                sendNewTokenToServer(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e(TAG, "onMessageReceived: "+remoteMessage.getData().get("click_action"));
        showNotification(remoteMessage);
    }


    private void showNotification(RemoteMessage remoteMessage){
        PrefManager prefManager = new PrefManager(this);
        if(remoteMessage.getData().get("vendor_id")!=null){
            if(remoteMessage.getData().get("vendor_id").equals(prefManager.getVendorId(this))) {
                Map<String, String> dataMap = remoteMessage.getData();
                String click_action = dataMap.get("click_action");
                Intent notificationIntent = new Intent(this, SplashActivity.class);
                notificationIntent.setAction(Intent.ACTION_MAIN);
                notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                notificationIntent.putExtra(StringConstants.CURRENT_ORDER_ID, dataMap.get("order_id"));
                notificationIntent.putExtra(StringConstants.BUZ_ID, dataMap.get("business_id"));
                notificationIntent.putExtra("ACTION", click_action);
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                NotificationManager nMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                PendingIntent intent2 = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder nBuilder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.splash_logo)
                                .setContentTitle(dataMap.get("title"))
                                .setContentText(dataMap.get("message"))
                                .setContentIntent(intent2)
                                .setAutoCancel(true);
                nMgr.notify(Constants.getCurrentNotificationId(this), nBuilder.build());
            }
        }
    }

    private void sendNoti(RemoteMessage remoteMessage){

        String click_action=remoteMessage.getData().get("click_action");
        Intent intent=new Intent(click_action);
        //Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT|PendingIntent.FLAG_UPDATE_CURRENT);

        String channelId = "101";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.splash_logo)
                        .setContentTitle("New Order receieved!!")
                        .setContentText("Click to view details")
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
    private void sendNewTokenToServer(String token){
        PrefManager prefManager = new PrefManager(MyFirebaseMessagingService.this);
        String url = Constants.BASE_URL+"vendor/add-refresh-token";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("vendor_id",prefManager.getVendorId(getApplicationContext()));
            jsonObject.put("token",token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        ReatchAll helper = ReatchAll.getInstance();
        helper.addToRequestQueue(customJsonRequest,"UPDATE_TOKEN");
    }
}
