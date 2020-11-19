package com.example.needlevision;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1;

    GoogleSignInClient mGoogleSignInClient;
    SignInButton signInButton;

    private FirebaseAuth mAuth;
    private String photoPath;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    FirebaseStorage storage;
    StorageReference storageRef;
    UploadTask uploadTask;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calendar = Calendar.getInstance();
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        FirebaseUser currentUser = mAuth.getCurrentUser();


        if(account == null){
            loadLoginPage();
            getSupportActionBar().hide();
        } else {
            // Signed in successfully, show authenticated UI. DELETE LATER
//            Intent intent = new Intent(MainActivity.this, PostLoginActivity.class);
//            startActivity(intent);
            loadPager();
        }
    }
    
    private void loadLoginPage(){
        setContentView(R.layout.activity_login);
        findViewById(R.id.guest_btn).setOnClickListener(new View.OnClickListener() {
            // Load the loadPagerPage function
            @Override
            public void onClick(View v) {

                guestSignIn();
                loadPager();

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
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(account.getIdToken());
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
                            loadPager();

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
                            loadPager();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Error", "signInWithCredential:failure", task.getException());
                        }

                        // ...
                    }
                });
    }

//    private void signOut() {
//        FirebaseAuth.getInstance().signOut();
//        mGoogleSignInClient.signOut()
//                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        Toast.makeText(PostLoginActivity.this, "Signed out successfully", Toast.LENGTH_LONG).show();
//                        finish();
//                    }
//                });
//    }
    //private void upload(String path)
    private void upload(String path){
        final String randomKey = UUID.randomUUID().toString();
        final StorageReference imageRef = storageRef.child("/images/" + randomKey);
        Uri file = Uri.fromFile(new File(path));
        uploadTask = imageRef.putFile(file);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return imageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    date = dateFormat.format(calendar.getTime());
                    //INSERT STRINGS TO POST BELOW
                    //writeNewPost(getFirebaseUserID(), "description string", "status string", date, 123, 321, downloadUri.toString());
                    Log.i("url", downloadUri.toString());
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }

    private void writeNewPost(String userID, String description, String status, String date, double latitude, double longitude, String imageURL){
        Post newPost = new Post(userID, description, status, date, latitude, longitude, imageURL);

        String key = mDatabase.child("posts").push().getKey();
        mDatabase.child("posts").child(key).setValue(newPost);

        // Read from the database upon adding new entry (UPDATE POSTINGS HERE)
        mDatabase.child("posts").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    Post data = dataSnapshot.getValue(Post.class);

//                    LOG STATEMENTS FOR DEBUGGING
//                    Log.d("Success", "userid is: " + data.getUserID());
//                    Log.d("Success", "desc is: " + data.getDescription());
//                    Log.d("Success", "status is: " + data.getStatus());
//                    Log.d("Success", "date is: " + data.getDate());
//                    Log.d("Success", "latitude is: " + data.getLatitude());
//                    Log.d("Success", "longitude is: " + data.getLongitude());
//                    Log.d("Success", "imageurl is: " + data.getImageURL());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Error", "Failed to read value.", error.toException());
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
    private void loadPager(){
        Intent intent = new Intent(MainActivity.this, PostActivity.class);
        startActivity(intent);
    }

    private String getFirebaseUserID(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid;
        if (user != null) {
            uid = user.getUid();
        } else {
            uid = "guest";
        }
        return uid;
    }

//    private void socialMediaUpload() {
//        Intent mediaUpload = new Intent();
//        mediaUpload.setAction(Intent.ACTION_SEND);
//        mediaUpload.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(this, "com.example.android.FileProvider", new File(photoPath)));
//        mediaUpload.setType("image/jpg");
//        this.startActivity(Intent.createChooser(mediaUpload, this.getResources().getText(R.string.send_to)));
//    }
}