package com.example.bestyou.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.bestyou.R;
import com.example.bestyou.activities.CartActivity;
import com.example.bestyou.activities.ShowAllWorkoutsActivity;
import com.example.bestyou.adapters.CategoryAdapter;
import com.example.bestyou.adapters.PopularWorkoutsAdapter;
import com.example.bestyou.adapters.WorkoutPlansAdapter;
import com.example.bestyou.models.CategoryModel;
import com.example.bestyou.models.MyCartModel;
import com.example.bestyou.models.PopularWorkoutsModel;
import com.example.bestyou.models.WorkoutPlansModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    TextView catShowAll, workoutPlanShowAll, popularWorkoutsShowAll;

    RecyclerView catRecyclerview, workoutPlansRecyclerview, popularWorkoutRecyclerview;

    //Category recyclerview
    CategoryAdapter categoryAdapter;
    List<CategoryModel> categoryModelList;


    WorkoutPlansAdapter workoutPlansAdapter;
    List<WorkoutPlansModel> workoutPlansModelList;

    PopularWorkoutsAdapter popularWorkoutsAdapter;
    List<PopularWorkoutsModel> popularWorkoutsModelList;

    FirebaseFirestore db;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        db = FirebaseFirestore.getInstance();

        catRecyclerview = root.findViewById(R.id.rec_category);
        workoutPlansRecyclerview = root.findViewById(R.id.new_product_rec);
        popularWorkoutRecyclerview = root.findViewById(R.id.popular_rec);

        catShowAll = root.findViewById(R.id.category_see_all);
        popularWorkoutsShowAll = root.findViewById(R.id.popular_see_all);
        workoutPlanShowAll = root.findViewById(R.id.newProducts_see_all);

        catShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ShowAllWorkoutsActivity.class);
                startActivity(intent);
            }
        });

        workoutPlanShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CartActivity.class);
                startActivity(intent);
            }
        });
        popularWorkoutsShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ShowAllWorkoutsActivity.class);
                startActivity(intent);
            }
        });

        //imageslider
        ImageSlider imageSlider = root.findViewById(R.id.image_slider);
        List<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.banner1, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.banner2, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.banner3, "Takealot", ScaleTypes.CENTER_CROP));

        imageSlider.setImageList(slideModels);

        catRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        categoryModelList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getContext(), categoryModelList);
        catRecyclerview.setAdapter(categoryAdapter);

        db.collection("Category")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                CategoryModel categoryModel = document.toObject(CategoryModel.class);
                                categoryModelList.add(categoryModel);
                                categoryAdapter.notifyDataSetChanged();
                            }
                        } else {

                            Toast.makeText(getActivity(),""+task.getException(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });


        workoutPlansRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        workoutPlansModelList = new ArrayList<>();
        workoutPlansAdapter = new WorkoutPlansAdapter(getContext(),workoutPlansModelList);
        workoutPlansRecyclerview.setAdapter(workoutPlansAdapter);

        db.collection("WorkoutsPlanned")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                WorkoutPlansModel workoutPlansModel = document.toObject(WorkoutPlansModel.class);
                                workoutPlansModelList.add(workoutPlansModel);
                                workoutPlansAdapter.notifyDataSetChanged();
                            }
                        } else {

                            Toast.makeText(getActivity(),""+task.getException(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

        //Popular Products

        popularWorkoutRecyclerview .setLayoutManager(new GridLayoutManager(getActivity(),2));
        popularWorkoutsModelList = new ArrayList<>();
        popularWorkoutsAdapter = new PopularWorkoutsAdapter(getContext(),popularWorkoutsModelList);
        popularWorkoutRecyclerview.setAdapter(popularWorkoutsAdapter);

        db.collection("PopularWorkouts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                PopularWorkoutsModel popularWorkoutsModel = document.toObject(PopularWorkoutsModel.class);
                                popularWorkoutsModelList.add(popularWorkoutsModel);
                                popularWorkoutsAdapter.notifyDataSetChanged();
                            }
                        } else {

                            Toast.makeText(getActivity(),""+task.getException(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });


        return root;
    }
}