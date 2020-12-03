package com.example.needlevision.Presenter;

import android.content.Context;

import com.example.needlevision.Model.PostActivityModel;
import com.example.needlevision.View.PostActivity;

import java.io.File;

public class PostActivityPresenter {
    Context context;
    PostActivityModel pam;
    public PostActivityPresenter(Context context){
        this.context = context;
        pam = new PostActivityModel(context);
    }

    public void deleteNullPhoto(){
        pam.deleteNullPhoto();
    }

    public File createImageFile(){
        return pam.createImageFile();
    }

    public String getPhotoPath(){
        return pam.getPhotoPath();
    }
}
