package com.example.assignment3.gym;

import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.squareup.okhttp.ResponseBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GymActivity extends DrawerActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private ActivityGymBinding binding;
    private MapView mMapView;
    private GoogleMap mMap;
    private ArrayList<Marker> markers;
    private FusedLocationProviderClient fusedLocationClient;
    private PlaceViewModel viewModel;


    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        markers = new ArrayList<>();

        binding = ActivityGymBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(PlaceViewModel.class);

        setContentView(binding.getRoot());

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView = findViewById(R.id.mapView);
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);

        closePlaceFragment();

//        getSupportFragmentManager().beginTransaction()
//                .setReorderingAllowed(true)
//                .add(R.id.place_fragment, new PlaceFragment())
//                .commit();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);

        mMap.setOnMapClickListener(latLng -> closePlaceFragment());

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
                mMap.setOnMarkerClickListener(this);

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

        String apiKey = getGoogleMapKey();

        GoogleMapService googleMapService = retrofit.create(GoogleMapService.class);

        GymActivity context = this;
        googleMapService.searchNearbyLocationByKeyword(locString, keyword, radius, apiKey).enqueue(new Callback<PlaceList>() {
            @Override
            public void onResponse(Call<PlaceList> call, Response<PlaceList> response) {
                if (response.body() == null) {
                    Toast.makeText(context, "No result found", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<PlaceModel> models = response.body().placeModelList;
                for (PlaceModel model : models) {
                    addMarker(model);
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

    private void closePlaceFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.place_fragment);
        if (fragment == null)
            return;

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .remove(fragment)
                .commit();
    }

    private String getGoogleMapKey() {
        ApplicationInfo app;
        try {
            app = this.getApplicationContext().getPackageManager().getApplicationInfo(
                    this.getApplicationContext().getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(this, "error fetching data, please try again",Toast.LENGTH_SHORT).show();
            return "";
        }
        Bundle bundle = app.metaData;
        return bundle.getString("com.google.android.geo.API_KEY");
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        String placeId = (String) marker.getTag();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GoogleMapService.MAP_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        String apiKey = getGoogleMapKey();
        viewModel.setGoogleApiKey(apiKey);

        GoogleMapService googleMapService = retrofit.create(GoogleMapService.class);

        GymActivity context = this;
        googleMapService.getPlaceDetail(placeId, apiKey).enqueue(new Callback<PlaceDetail>() {
            @Override
            public void onResponse(Call<PlaceDetail> call, Response<PlaceDetail> response) {
                if (response.body() == null) {
                    Toast.makeText(context, "Cannot find the place detail", Toast.LENGTH_SHORT).show();
                    return;
                }

                PlaceDetail.PlaceDetailInfo placeDetail = response.body().detail;

                viewModel.setLocation(placeDetail.getLocation());
                viewModel.setName(placeDetail.getName());
                viewModel.setRating(placeDetail.getRating());
                viewModel.setIfNewMarkClicked(true);
                if (Objects.nonNull(placeDetail.getOpeningHours()) && placeDetail.getOpeningHours().getWeekday().size() == 7) {
                    List<String> weekdays = placeDetail.getOpeningHours().getWeekday();
                    Collections.rotate(weekdays, 1);
                    Calendar c = Calendar.getInstance();
                    c.setTime(new Date());
                    int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
                    viewModel.setWeekday(weekdays.get(dayOfWeek-1));
                }
                else {
                    viewModel.setWeekday("Opening hour info not available");
                }

                if (placeDetail.getPhotoRef() != null) {
                    for (PlaceDetail.PhotoRef ref : placeDetail.getPhotoRef()) {
                        viewModel.setPhoto(ref.getPhotoRef());
                    }
                }
                else {
                    viewModel.setPhoto("");
                }

                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.place_fragment, new PlaceFragment())
                        .commit();
            }

            @Override
            public void onFailure(Call<PlaceDetail> call, Throwable t) {
            }
        });

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
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