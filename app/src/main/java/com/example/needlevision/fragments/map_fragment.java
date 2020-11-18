package com.example.needlevision.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.needlevision.Post;
import com.example.needlevision.PostActivity;
import com.example.needlevision.R;
import com.example.needlevision.adapters.PostListAdapter;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;


public class map_fragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "Map Fragment";
    //// the map
    private GoogleMap mMap;
    // Manifest.permission.ACCESS_FINE_LOCATION
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    // COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    // LOCATION_PERMISSION_REQUEST_CODE = 1234
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    // if current location enabled
    private Boolean mLocationPermissionsGranted = false;
    // current location api
    private FusedLocationProviderClient mFusedLocationProviderClient;
    // current location
    private Location currentLocation;
    // current lat and lng
    private LatLng currentLatLng;
    // posts listing
    private ListView postsList;

    ViewGroup context;


    // when map is ready
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i("test", "ready");
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            // ask for current location
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
        }
    }

    // on Creating View
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.map_page,container,false);
        context = rootView;

        // ask for location permission
        getLocationPermission();

        // List View
        postsList = rootView.findViewById(R.id.lvOffices);
        ArrayList<Post> postArrayList = dummy();

        PostListAdapter adapter = new PostListAdapter(getActivity(), R.layout.post_layout, postArrayList);
        postsList.setAdapter(adapter);

        return rootView;
    }

    private ArrayList<Post> dummy(){
        ArrayList<Post> postsList = new ArrayList<>();

        postsList.add(new Post(false,"Nov 1 2020", "4:20 pm" ,"123 Vancouver St.",  null));
        postsList.add(new Post(true,"Nov 5 2020", "1:50 am" ,"345 Burnaby St.",  "somewhere"));
        postsList.add(new Post(true,"Nov 10 2020", "6:40 pm" ,"245 Richmond St.",  "Under the tree"));
        postsList.add(new Post(false,"Nov 15 2020", "12:02 pm" ,"777 Port Moody St.", "on the Bench"));
        postsList.add(new Post(false,"Nov 10 2020", "3:30 pm" ,"245 Whistler St.",  "on the tree"));
        postsList.add(new Post(false,"Nov 20 2020", "11:12 pm" ,"777 Coquitlam St.", "beach"));

        return postsList;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.map_menu, menu);
        ((PostActivity) getActivity()).setActionBarTitle("Map");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.logout_btn){
            Snackbar.make(context, "Filter", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        return super.onOptionsItemSelected(item);
    }

    // initiating the map
    private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_frag);

        mapFragment.getMapAsync(map_fragment.this);
    }

    // get the Device current location
    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getActivity());
        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                            currentLocation = (Location) task.getResult();
                            currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                            moveCamera(currentLatLng, 13);
                            // generateMarks();
                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                        }
                    }
                });
            }
        } catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }
    // move the map
    private void moveCamera(LatLng latLng, float zoom){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    // get the location permission request
    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this.getActivity(),
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this.getActivity(),
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    // request device current location access
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

}
