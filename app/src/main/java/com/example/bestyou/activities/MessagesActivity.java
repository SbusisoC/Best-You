package com.example.bestyou.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.widget.Toolbar;

import com.example.bestyou.R;

import java.util.Objects;

public class MessagesActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageButton searchButton, searchIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        toolbar = findViewById(R.id.notifications_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(view -> finish());

        searchButton = findViewById(R.id.search_bar);
        searchIcon = findViewById(R.id.search_icon);

        searchButton.setOnClickListener(view ->
                startActivity(new Intent(MessagesActivity.this,SearchUserActivity.class)));

        searchIcon.setOnClickListener(view ->
                startActivity(new Intent(MessagesActivity.this,SearchUserActivity.class)));
    }
}