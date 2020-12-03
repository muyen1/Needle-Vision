package com.example.needlevision.Model;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.needlevision.R;
import com.example.needlevision.View.Filter;
import com.example.needlevision.View.PostActivity;
import com.example.needlevision.View.adapters.PostListAdapter;
import com.example.needlevision.View.fragments.map_fragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class MapFragmentModel {
    private Context context;

    private static final int FILTER_ID = 9808;

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
    // list of posts
    List<Post> listPosts, fullList;
    // data
    ArrayList<String> userIds;
    ArrayList<String> dess;
    ArrayList<String> statuss;
    ArrayList<String> dates;
    double lats[];
    double longs[];
    ArrayList<String> imageurls;
    // markers for selected post
    Marker markers[];

    public MapFragmentModel(Context context){
        this.context = context;
    }

    // set markers on map
    private void generateMarks() {
        // list item listener after user choose a post
        postsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mMap.clear();
                // getting the logLat
                LatLng ptLatlng = new LatLng(listPosts.get(position).getLatitude(), listPosts.get(position).getLongitude());
                setMarks(currentLatLng, ptLatlng, listPosts.get(position).getDescription());
            }
        });
        for (int i = 0; i < statuss.size(); i++) {
            Post pt;
            // getting the logLat
            LatLng ptLatlng = new LatLng(lats[i], longs[i]);
            pt = new Post(userIds.get(i), dess.get(i), statuss.get(i),
                    dates.get(i), lats[i], longs[i], imageurls.get(i));
            pt.setDistance(CalculationByDistance(currentLatLng, ptLatlng));
            listPosts.add(pt);
        }
        // sort the list by distance
        Collections.sort(listPosts, new Comparator<Post>() {
            @Override
            public int compare(Post p1, Post p2) {
                return Double.compare(p1.getDistance(), p2.getDistance());
            }
        });
        fullList = listPosts;
        // constructing posts adapter
        PostListAdapter adapter = new PostListAdapter((Activity) context, listPosts);
        postsList.setAdapter(adapter);
    }

    // show the current location and the selected post markers
    private void setMarks(LatLng start, LatLng end, String des) {
        markers = new Marker[2];
        // call showRouting()
        // showRouting(new MarkerOptions().position(start), new MarkerOptions().position(end));
        // add marker on map
        markers[0]= mMap.addMarker(new MarkerOptions().position(start).title("You are here!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        markers[1] = mMap.addMarker(new MarkerOptions().position(end).title(des));
        markers[1].showInfoWindow();
        // auto move the camera to cover two locations
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        int padding = 200; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cu);
    }

    // calculate the distance between each post to the current location
    private double CalculationByDistance(LatLng StartP, LatLng EndP) {
        float results[] = new float[1000];
        Location.distanceBetween(StartP.latitude, StartP.longitude, EndP.latitude, EndP.longitude, results);
        return Double.parseDouble(new DecimalFormat("0.#").format(results[0] / 1000 + 10));
    }

    // get the Device current location
    public void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
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
                            generateMarks();
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

}
