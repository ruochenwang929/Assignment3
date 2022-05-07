package com.example.assignment3.gym;

import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;

import androidx.annotation.NonNull;

import android.annotation.SuppressLint;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.assignment3.DrawerActivity;
import com.example.assignment3.R;
import com.example.assignment3.databinding.ActivityGymBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GymActivity extends DrawerActivity implements OnMapReadyCallback {

    private ActivityGymBinding binding;
    private MapView mMapView;
    private GoogleMap mMap;
    private ArrayList<Marker> markers;
    private FusedLocationProviderClient fusedLocationClient;


    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        markers = new ArrayList<>();

        binding = ActivityGymBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot().getSourceLayoutResId());

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView = findViewById(R.id.mapView);
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);

        fusedLocationClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY, new CancellationToken() {
            @NonNull
            @Override
            public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                return null;
            }

            @Override
            public boolean isCancellationRequested() {
                return false;
            }
        })
        .addOnSuccessListener(this, location -> {
            if (location != null) {
                LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                //mMap.addMarker(new MarkerOptions().position(myLocation).title("It's Me!"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));

                try {
                    searchNearbyLocationsByKeyword("gym", myLocation, 1500);
                } catch (Exception e) {
                    Toast.makeText(this, "Unexpected error occured. Please try relaunching the application", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void searchNearbyLocationsByKeyword(String keyword, LatLng position, int radius) throws PackageManager.NameNotFoundException, IOException, JSONException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GoogleMapService.MAP_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        String locString = "" + position.latitude + "," + position.longitude;

        ApplicationInfo app;
        app = this.getApplicationContext().getPackageManager().getApplicationInfo(
                this.getApplicationContext().getPackageName(), PackageManager.GET_META_DATA);

        Bundle bundle = app.metaData;
        String apiKey = bundle.getString("com.google.android.geo.API_KEY");

        GoogleMapService googleMapService = retrofit.create(GoogleMapService.class);

        googleMapService.searchNearbyLocationByKeyword(locString, keyword, radius, apiKey).enqueue(new Callback<PlaceList>() {
            @Override
            public void onResponse(Call<PlaceList> call, Response<PlaceList> response) {
                try {
                    List<PlaceModel> models = response.body().placeModelList;
                    for (PlaceModel model : models) {
                        addMarker(model);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<PlaceList> call, Throwable t) {
            }
        });
    }

    private void addMarker(PlaceModel model) {
        PlaceModel.Location location = model.getGeometry().getLocation();
        LatLng markloc = new LatLng(location.getLat(), location.getLng());
        Marker marker = mMap.addMarker(new MarkerOptions().position(markloc));
        marker.setTag(model.getPlaceId());
        markers.add(marker);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

}