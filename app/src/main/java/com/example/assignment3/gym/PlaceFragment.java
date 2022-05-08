package com.example.assignment3.gym;

import androidx.lifecycle.ViewModelProvider;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.assignment3.databinding.PlaceFragmentBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.util.ArrayList;

import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlaceFragment extends Fragment {

    private PlaceViewModel mViewModel;
    private PlaceFragmentBinding binding;
    private ImageListAdapter adapter;

    public static PlaceFragment newInstance() {
        return new PlaceFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = PlaceFragmentBinding.inflate(getLayoutInflater());

        RecyclerView recyclerView = binding.imageGallery;
        recyclerView.setHasFixedSize(true);
        adapter = new ImageListAdapter(new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(PlaceViewModel.class);
        mViewModel.getName().observe(getViewLifecycleOwner(), s -> binding.name.setText(s));
        mViewModel.getLocation().observe(getViewLifecycleOwner(), s -> binding.location.setText(s));
        mViewModel.getWeekday().observe(getViewLifecycleOwner(), s -> binding.weekday.setText(s));
        mViewModel.getRating().observe(getViewLifecycleOwner(), d -> binding.rate.setText(d + ""));
        mViewModel.getPhoto().observe(getViewLifecycleOwner(), s -> {
            if (mViewModel.isIfNewMarkClicked()) {
                adapter.cleanData();
                mViewModel.setIfNewMarkClicked(false);
            }

            if (s.equals("")) return;

            Gson gson = new GsonBuilder().setLenient().create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(GoogleMapService.MAP_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            String apiKey = mViewModel.getGoogleApiKey();
            GoogleMapService googleMapService = retrofit.create(GoogleMapService.class);

            googleMapService.getImageByPhotoReference(s, 300, 300, apiKey).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                            adapter.addItem(bmp);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                }
            });
        });
    }
}