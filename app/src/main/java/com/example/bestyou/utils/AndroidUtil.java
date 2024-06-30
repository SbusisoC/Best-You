package com.example.bestyou.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bestyou.models.UserModel;

public class AndroidUtil {

    public static  void showToast(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

    public static void setProfilePic(Context context, Uri imageUri, ImageView imageView){
        Glide.with(context).load(imageUri).apply(RequestOptions.circleCropTransform()).into(imageView);
    }

    public static void passUserModelAsIntent(Intent intent, UserModel model){
        intent.putExtra("username",model.getUsername());
        intent.putExtra("userId",model.getUserId());
    }

    public static UserModel getUserModelFromIntent(Intent intent){
        UserModel userModel  = new UserModel();
        userModel.setUsername(intent.getStringExtra("username"));
        userModel.setUserId(intent.getStringExtra("userId"));
        return userModel;
    }
}
