package com.example.needlevision;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
import java.util.Calendar;
import java.util.Map;
import java.util.UUID;

public class PostLoginActivity extends AppCompatActivity {

    TextView nameTV, emailTV, idTV;
    Button signOut;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseStorage storage;
    StorageReference storageRef;
    UploadTask uploadTask;

    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_login);
        calendar = Calendar.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        nameTV = findViewById(R.id.nameTextView);
        emailTV = findViewById(R.id.emailTextView);
        idTV = findViewById(R.id.idTextView);
        signOut = findViewById(R.id.signOutButton);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    // ...
                    case R.id.signOutButton:
                        signOut();
                        break;
                    // ...
                }


            }
        });


        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();

            nameTV.setText(personName);
            emailTV.setText(personEmail);
            idTV.setText(personId);
        }

        upload();


    }
    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(PostLoginActivity.this, "Signed out successfully", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
    }
//private void upload(String path)
    private void upload(){
        final String randomKey = UUID.randomUUID().toString();
        final StorageReference imageRef = storageRef.child("/images/" + randomKey);

        Uri file = Uri.fromFile(new File("/storage/emulated/0/Android/data/com.example.needlevision/files/Pictures/2020-11-18_4898790545471508903.jpg"));
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
                    writeNewPost("another23", "desc1", "stat1", date, 123, 321, downloadUri.toString());
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


}