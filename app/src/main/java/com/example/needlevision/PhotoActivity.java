package com.example.needlevision;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import com.example.needlevision.service.GMailSender;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static android.app.Activity.RESULT_OK;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.needlevision.service.Database;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PhotoActivity extends AppCompatActivity implements LocationListener {

    private String photoPath;
    private ImageButton btn_back;
    private ImageButton btn_upload;
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

    private LocationManager lm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);


       // sEmail  = "comp7082group1@gmail.com";
       // sPassword = "lol12345LOL";

        eSubject = "email Subject";
        eBody = "email body";
        eRecipients = "comp7082group1@gmail.com";

        eUsername = "comp7082group1@gmail.com";
        ePassword = "lol12345LOL";
        
        db = new Database();

        btn_back = findViewById(R.id.btn_back);
        btn_upload = findViewById(R.id.btn_upload);
        imageView = findViewById(R.id.imageView);
        label_date = findViewById(R.id.label_date);
        tv_date = findViewById(R.id.tv_date);
        label_location = findViewById(R.id.label_location);
        tv_location = findViewById(R.id.tv_location);
        et_description = findViewById(R.id.et_description);

        photoPath = getIntent().getStringExtra("PHOTO_PATH");
        setPicture(photoPath);

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy h:mm a");
        calendar = Calendar.getInstance();
        date = df.format(calendar.getTime());
        tv_date.setText(date);

        initLocation();

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

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });

        //This function probably needs to be expanded to include other functionality.
        //Currently just returns to the main activity
        btn_upload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(),"Hello Javatpoint",Toast.LENGTH_SHORT).show();

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


                String description = et_description.getText().toString();
                db.upload(photoPath, description, date, latitude, longitude);
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    public void initLocation(){
        lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) this, new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION },
                    1);
        }
    }

    public void onLocationChanged(Location location) {
        if (location != null) {
        }
    }

    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Photo GPS data not saved, location services disabled",
                Toast.LENGTH_LONG).show();
    }

    // Set the current picture
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
}