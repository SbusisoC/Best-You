package com.example.bestyou.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.bestyou.R;
import com.example.bestyou.adapters.SearchUserRecyclerAdapter;
import com.example.bestyou.models.UserModel;
import com.example.bestyou.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class SearchUserActivity extends AppCompatActivity {

    EditText searchInput;
    ImageButton searchButton, backButton;
    RecyclerView recyclerView;

    SearchUserRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        searchInput = findViewById(R.id.search_username_input);
        searchButton = findViewById(R.id.search_user_btn);
        backButton = findViewById(R.id.back_btn);
        recyclerView = findViewById(R.id.search_user_recycler_view);

        backButton.setOnClickListener(v -> {
            onBackPressed();
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Add TextWatcher to search input for real-time search
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchTerm = charSequence.toString();
                if (searchTerm.length() >= 3) {
                    setupSearchRecyclerView(searchTerm);
                } else {
                    // Clear the list when less than 3 characters are typed
                    if (adapter != null) {
                        adapter.stopListening();
                        recyclerView.setAdapter(null);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // Search button click listener to trigger search manually
        searchButton.setOnClickListener(v -> {
            String searchTerm = searchInput.getText().toString();
            if (searchTerm.isEmpty() || searchTerm.length() < 3) {
                searchInput.setError("Please enter at least 3 characters");
                return;
            }
            setupSearchRecyclerView(searchTerm);
        });
    }

    void setupSearchRecyclerView(String searchTerm) {
        Query query = FirebaseUtil.allUserCollectionReference()
                .orderBy("username")
                .startAt(searchTerm)
                .endAt(searchTerm + "\uf8ff");  // Ensures prefix-based search

        FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>()
                .setQuery(query, UserModel.class)
                .build();

        adapter = new SearchUserRecyclerAdapter(options, getApplicationContext());
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.startListening();
        }
    }
}