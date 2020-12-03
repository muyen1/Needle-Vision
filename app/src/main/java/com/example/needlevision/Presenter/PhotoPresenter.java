package com.example.needlevision.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.provider.ContactsContract;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PhotoPresenter {

    Context c;

    private String eSubject = "email Subject";
    private String eBody = "email body";
    private String eRecipients = "comp7082group1@gmail.com";
    private String eSender = "comp7082group1@gmail.com";
    private String eUsername = "comp7082group1@gmail.com";
    private String ePassword = "lol12345LOL";

    public PhotoPresenter(Context c) {
        this.c = c;
    }

    public LocationManager initLocation(){
        LocationManager lm = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(c,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) c, new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION },
                    1);
        }
        return lm;
    }

    public void setPicture(String photoPath, ImageView imageView) {
        int targetWidth = imageView.getWidth();
        int targetHeight = imageView.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(photoPath, bmOptions);

        int photoWidth = bmOptions.outWidth;
        int photoHeight = bmOptions.outHeight;

        bmOptions.inJustDecodeBounds = false;

        Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
        imageView.setImageBitmap(bitmap);
    }

    public String geteSubject() {
        return eSubject;
    }

    public String geteBody() {
        return eBody;
    }

    public String geteRecipients() {
        return eRecipients;
    }

    public String geteSender() {
        return eSender;
    }

    public String geteUsername() {
        return eUsername;
    }

    public String getePassword()
    {
        return ePassword;
    }
}
