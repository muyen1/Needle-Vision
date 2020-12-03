package com.example.needlevision.View;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.needlevision.Model.GMailSender;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.needlevision.Model.Database;
import com.example.needlevision.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.example.needlevision.Presenter.PhotoPresenter;

public class PhotoActivity extends AppCompatActivity implements LocationListener {

    private String photoPath;
    private ImageView imageView;

    String sEmail;
    String sPassword ;

    String eSubject;
    String eBody;
    String eRecipients;
    String eSender;

    String eUsername;
    String ePassword;
    private TextView label_date;
    private TextView tv_date;
    private TextView label_location;
    private TextView tv_location;
    private EditText et_description;

    private Database db;

    private Calendar calendar;
    private String date;
    private double latitude;
    private double longitude;

    private PhotoPresenter pp;

    private LocationManager lm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        pp = new PhotoPresenter(this);

        eSubject = pp.geteSubject();
        eBody = pp.geteBody();
        eRecipients = pp.geteRecipients();
        eSender = pp.geteSender();
        eUsername = pp.geteUsername();
        ePassword = pp.getePassword();
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new Database();

        imageView = findViewById(R.id.imageView);
        label_date = findViewById(R.id.label_date);
        tv_date = findViewById(R.id.tv_date);
        label_location = findViewById(R.id.label_location);
        tv_location = findViewById(R.id.tv_location);
        et_description = findViewById(R.id.et_description);

        photoPath = getIntent().getStringExtra("PHOTO_PATH");
        pp.setPicture(photoPath, imageView);

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy h:mm a");
        calendar = Calendar.getInstance();
        date = df.format(calendar.getTime());
        tv_date.setText(date);

        lm = pp.initLocation();

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) this,
                    new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION }, 1);
        }

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
        String coordinates = Double.toString(latitude) + ", " + Double.toString(longitude);
        tv_location.setText(coordinates);
    }

    /*
    public void initLocation(){
        lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) this, new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION },
                    1);
        }
    }
     */

    public void onLocationChanged(Location location) {
        if (location != null) {
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Photo GPS data not saved, location services disabled",
                Toast.LENGTH_LONG).show();
    }

    // Set the current picture
    /*
    public void setPicture(String photoPath) {
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
     */

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.photo_menu, menu);
        setActionBarTitle("Photo Details");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        Toast.makeText(this, "toast",
                Toast.LENGTH_LONG).show();
        if (id == R.id.photo_send_btn){
            String description = et_description.getText().toString();

            eBody = "Date: " + date + "\n\n";
            eBody +="Lat: " + latitude + "\n";
            eBody +="Long: " + longitude + "\n\n";
            eBody += "Description: \n";
            eBody += description + "\n\n";

            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        GMailSender sender = new GMailSender(eUsername,
                                ePassword);
                        sender.sendMail(eSubject, eBody,
                                eSender, eRecipients, photoPath);
                    } catch (Exception e) {
                        Log.e("SendMail", e.getMessage(), e);
                    }
                }

            }).start();

            db.upload(photoPath, description, date, latitude, longitude);
            setResult(RESULT_OK);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}