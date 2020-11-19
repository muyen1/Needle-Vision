package com.example.needlevision;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class PhotoActivity extends AppCompatActivity {

    private String photoPath;
    private ImageButton btn_back;
    private ImageButton btn_upload;
    private ImageView imageView;

    String sEmail;
    String sPassword ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);


       // sEmail  = "comp7082group1@gmail.com";
       // sPassword = "lol12345LOL";


        btn_back = findViewById(R.id.btn_back);
        btn_upload = findViewById(R.id.btn_upload);
        imageView = findViewById(R.id.imageView);
        photoPath = getIntent().getStringExtra("PHOTO_PATH");
        setPicture(photoPath);

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
                            GMailSender sender = new GMailSender("comp7082group1@gmail.com",
                                    "lol12345LOL");
                            sender.sendMail("Hello from JavaMail", "Body from JavaMail",
                                    "comp7082group1@gmail.com", "comp7082group1@gmail.com");
                        } catch (Exception e) {
                            Log.e("SendMail", e.getMessage(), e);
                        }
                    }

                }).start();


                setResult(RESULT_OK);
                finish();
            }
        });
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