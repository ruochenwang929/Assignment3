package com.example.assignment3.gym;

import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.assignment3.DrawerActivity;
import com.example.assignment3.R;
import com.example.assignment3.databinding.ActivityGymBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;

public class GymActivity extends DrawerActivity implements OnMapReadyCallback {

    private ActivityGymBinding binding;
    private MapView mMapView;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        getLocationPermission();
    }

    private void getLocationPermission() {
        String[] permission = {FINE_LOCATION, COURSE_LOCATION};

        boolean permissionGranted =
                (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                        &&
                ContextCompat.checkSelfPermission(this.getApplicationContext(),
                COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (!permissionGranted)
            ActivityCompat.requestPermissions(this, permission, 1);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney and move the camera
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
                mMap.addMarker(new MarkerOptions().position(myLocation).title("It's Me!"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 13));
            }
        });
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