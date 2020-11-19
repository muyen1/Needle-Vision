package com.example.needlevision.service;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.needlevision.Post;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.UUID;

public class Database {

    private final FirebaseStorage storage;
    StorageReference storageRef;
    UploadTask uploadTask;

    private DatabaseReference mDatabase;

    public Database() {
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void upload(String path){
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
//                    dateFormat = new SimpleDateFormat("MM/dd/yyyy");
//                    date = dateFormat.format(calendar.getTime());
                    //INSERT STRINGS TO POST BELOW
                    writeNewPost(getFirebaseUserID(), "2description string", "2status string", "11/18/2020", 123.1, 321.1, downloadUri.toString());
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
}
