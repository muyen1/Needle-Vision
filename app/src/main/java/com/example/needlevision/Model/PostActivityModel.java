package com.example.needlevision.Model;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PostActivityModel {
    private Context context;
    private String photoPath;

    public PostActivityModel(Context context){
        this.context = context;
    }

    public void deleteNullPhoto(){
        File nullPhoto = new File(photoPath);
        nullPhoto.delete();
    }

    public String getPhotoPath(){
        return photoPath;
    }
    public void setPhotoPath(String photoPath){
        this.photoPath = photoPath;
    }

    public File createImageFile() {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String imageName = timestamp + "_";
        File storageDirectory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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
