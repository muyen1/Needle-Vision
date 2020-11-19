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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager pager;
    private PagerAdapter pagerAdapter;

    private static final int RC_SIGN_IN = 1;
    private static final int CAMERA_PERM_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 102;
    private static final int PHOTO_REQUEST_CODE = 103;
    private String photoPath;
    GoogleSignInClient mGoogleSignInClient;
    SignInButton signInButton;
    boolean signedIntoAppBefore;

    private GoogleMap mMap;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLoginPage();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("892938951159-0vupf97va7hukrr41e16qauu52tgkqht.apps.googleusercontent.com")
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        // Read from the database upon adding new entry (UPDATE POSTINGS HERE)
        mDatabaseReference.child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> postChildren = dataSnapshot.getChildren();
                ArrayList<Post> posts = new ArrayList<>();
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

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Error", "Failed to read value.", error.toException());
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(account == null){
            signedIntoAppBefore = false;
        } else {
            signedIntoAppBefore = true;
        }
    }
    
    private void loadLoginPage(){
        setContentView(R.layout.activity_login);
        findViewById(R.id.guest_btn).setOnClickListener(new View.OnClickListener() {

            // Load the loadPagerPage function
            @Override
            public void onClick(View v) {
                loadPagerPage();
                guestSignIn();

            }
        });
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {

            // Load the loadPagerPage function
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    // Loads the Fragment Slides
    private void loadPagerPage(){
        setContentView(R.layout.activity_main);
        List<Fragment> list = new ArrayList<>();
        list.add(new map_fragment());
        list.add(new posts_fragment());

        pager = findViewById(R.id.pager);
        pagerAdapter = new SlidePagerAdapter(getSupportFragmentManager(), list);
        pager.setAdapter(pagerAdapter);

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

    private void checkCameraPermissions() {
        // Display camera permission prompt
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA }, CAMERA_PERM_CODE);
        } else {
            //Open Camera
            openCamera();
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

    private void openCamera() {
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

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                File nullPhoto = new File(photoPath);
                nullPhoto.delete();
            } else {
                Intent photoActivity = new Intent(MainActivity.this, PhotoActivity.class);
                photoActivity.putExtra("PHOTO_PATH", photoPath);
                startActivityForResult(photoActivity, PHOTO_REQUEST_CODE);
            }
        } else if (requestCode == PHOTO_REQUEST_CODE) {
            //Return from the photo activity
            //Refresh map pins/list view entries?
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(account.getIdToken());

//            // Signed in successfully, show authenticated UI.
//            Intent intent = new Intent(MainActivity.this, PostLoginActivity.class);
//            startActivity(intent);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("error", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void guestSignIn(){
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i("Success", "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            // Signed in successfully, show authenticated UI.
                            Intent intent = new Intent(MainActivity.this, PostLoginActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Error", "signInAnonymously:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Success", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            // Signed in successfully, show authenticated UI.
                            Intent intent = new Intent(MainActivity.this, PostLoginActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Error", "signInWithCredential:failure", task.getException());
                        }

                        // ...
                    }
                });
    }
    private void socialMediaUpload() {
        Intent mediaUpload = new Intent();
        mediaUpload.setAction(Intent.ACTION_SEND);
        mediaUpload.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(this, "com.example.android.FileProvider", new File(photoPath)));
        mediaUpload.setType("image/jpg");
        this.startActivity(Intent.createChooser(mediaUpload, this.getResources().getText(R.string.send_to)));
    }
}