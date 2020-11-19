package com.example.needlevision;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.needlevision.adapters.SlidePagerAdapter;
import com.example.needlevision.fragments.map_fragment;
import com.example.needlevision.fragments.posts_fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostActivity extends AppCompatActivity {
    private static final int CAMERA_PERM_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 102;
    private static final int PHOTO_REQUEST_CODE = 103;
    private String photoPath;

    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    // firebase Ref
    private DatabaseReference mDatabase;
    // arrayList for post data
    private ArrayList<String> userIdLt;
    private ArrayList<String> desLt;
    private ArrayList<String> statusLt;
    private ArrayList<String> dateLt;
    private ArrayList<Double> latLt;
    private ArrayList<Double> lngLt;
    private ArrayList<String> imageurlLt;
    // data sets
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Read from the database upon adding new entry (UPDATE POSTINGS HERE)
        mDatabase.child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> postChildren = dataSnapshot.getChildren();
                // initiating arraylists
                userIdLt = new ArrayList<>();
                desLt = new ArrayList<>();
                statusLt = new ArrayList<>();
                dateLt = new ArrayList<>();
                latLt = new ArrayList<>();
                lngLt = new ArrayList<>();
                imageurlLt = new ArrayList<>();
                // DO STUFF IN THIS LOOP FOR EACH POST RETRIEVED
                for (DataSnapshot post : postChildren) {
                    Post p = post.getValue(Post.class);
//                    Log.d("Success", "userid is: " + p.getUserID());
//                    Log.d("Success", "desc is: " + p.getDescription());
//                    Log.d("Success", "status is: " + p.getStatus());
//                    Log.d("Success", "date is: " + p.getDate());
//                    Log.d("Success", "latitude is: " + p.getLatitude());
//                    Log.d("Success", "longitude is: " + p.getLongitude());
//                    Log.d("Success", "imageurl is: " + p.getImageURL());
                    // adding to list
                    userIdLt.add(p.getUserID());
                    desLt.add(p.getDescription());
                    statusLt.add(p.getStatus());
                    dateLt.add(p.getDate());
                    latLt.add(p.getLatitude());
                    lngLt.add(p.getLongitude());
                    imageurlLt.add(p.getImageURL());
                }
                // passing date to map_fragment and post_fragment using bundle
                bundle = new Bundle();
                bundle.putStringArrayList("userIds", userIdLt);
                bundle.putStringArrayList("dess", desLt);
                bundle.putStringArrayList("statuss", statusLt);
                bundle.putStringArrayList("dates", dateLt);
                // convertin array list to array for lat and lng
                double latArr[] = new double[statusLt.size()];
                double lngArr[] = new double[statusLt.size()];
                for(int i = 0; i < statusLt.size(); i++) {
                    latArr[i] = latLt.get(i);
                    lngArr[i] = lngLt.get(i);
                }
                bundle.putDoubleArray("lats", latArr);
                bundle.putDoubleArray("lngs", lngArr);
                bundle.putStringArrayList("imageurls", imageurlLt);

                // set Fragmentclass Arguments
                map_fragment mapFrag = new map_fragment();
                posts_fragment postFrag = new posts_fragment();
                // sending data to fragments
                mapFrag.setArguments(bundle);
                postFrag.setArguments(bundle);

                // ViewAdapter List
                List<Fragment> list = new ArrayList<>();
                list.add(mapFrag);
                list.add(postFrag);

                pager = findViewById(R.id.pager);
                pagerAdapter = new SlidePagerAdapter(getSupportFragmentManager(), list);
                pager.setAdapter(pagerAdapter);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Error", "Failed to read value.", error.toException());
            }
        });

        loadFAB();
    }

    private void loadFAB(){
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Bring Up Camera", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                checkCameraPermissions();

            }
        });
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void checkCameraPermissions() {
        // Display camera permission prompt
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA }, CAMERA_PERM_CODE);
        } else {
            //Open Camera
            openCamera();
        }
    }

    public void openCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (camera.resolveActivity(this.getPackageManager()) != null) {
            File photoFile = null;
            photoFile = createImageFile();
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.android.FileProvider", photoFile);
                camera.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(camera, CAMERA_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                File nullPhoto = new File(photoPath);
                nullPhoto.delete();
            } else {
                Intent photoActivity = new Intent(this, PhotoActivity.class);
                photoActivity.putExtra("PHOTO_PATH", photoPath);
                startActivityForResult(photoActivity, PHOTO_REQUEST_CODE);
            }
        } else if (requestCode == PHOTO_REQUEST_CODE) {
            //Return from the photo activity
            //Refresh map pins/list view entries?
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera Permission is Required to Use camera.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Create Image File
    public File createImageFile() {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String imageName = timestamp + "_";
        File storageDirectory = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;

        try {
            image = File.createTempFile(imageName, ".jpg", storageDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }
        photoPath = image.getAbsolutePath();
        Log.i("photopath", photoPath);
        return image;
    }


}