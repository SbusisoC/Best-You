package com.example.bestyou.models;



import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bestyou.utils.Accesstoken;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Sendnotification {
    private final String userFcmToken;
    private final String title;
    private final String body;
    private final Context context;
    private final String postUrl = "";

    public Sendnotification(String userFcmToken, String title, String body, Context context) {
        this.userFcmToken = userFcmToken;
        this.title = title;
        this.body = body;
        this.context = context;
    }

    public void SendNotifications(){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject mainObj = new JSONObject();
        try{
            JSONObject messageObject = new JSONObject();
            JSONObject notificationObject = new JSONObject();
            notificationObject.put("title", title);
            notificationObject.put("body", body);
            messageObject.put("token", userFcmToken);
            messageObject.put("notification", notificationObject);
            mainObj.put("message", messageObject);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj, response ->{

            }, volleyError ->{

            }){
                @NonNull
                @Override
                public Map<String,String> getHeaders(){
                    Accesstoken accessToken = new Accesstoken();
                    String accessKey = accessToken.getAccessToken();
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "Bearer" + accessKey);
                    return header;
                }
            };

            requestQueue.add(request);

        } catch (JSONException e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
