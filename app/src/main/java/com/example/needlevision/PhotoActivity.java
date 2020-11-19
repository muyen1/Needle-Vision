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
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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


        sEmail  = "comp7082group1@gmail.com";
        sPassword = "lol12345LOL";


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



                sendEmail();
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    public void sendEmail() {


    //Initialize propteries
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        //Initialize session
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sEmail, sPassword);
            }
        });

        //Initialize email content
        try {
            Message message = new MimeMessage(session);
            //Sender email

            message.setFrom(new InternetAddress(sEmail));

            //Recipienet Email
            message.setRecipients( Message.RecipientType.TO,
                                    InternetAddress.parse("comp7082group1@gmail.com"));

            message.setSubject("test");

            message.setText("test");

            //send email

            new SendMail().execute(message);


        }catch (MessagingException e){
            e.printStackTrace();
        }


    }

    private class SendMail extends AsyncTask<Message, String, String> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(getApplicationContext(), "Please wait", "Sending mail..", false);


        }

        //Initilize progress dialog
        @Override
        protected String doInBackground(Message... messages) {
            Toast.makeText(getApplicationContext(), "BACGROUND", Toast.LENGTH_SHORT).show();

            try {
                Transport.send(messages[0]);
                return "Success";
            }catch(MessagingException e ){
                e.printStackTrace();
                return "Error";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Toast.makeText(getApplicationContext(), "post execute", Toast.LENGTH_SHORT).show();

            progressDialog.dismiss();

            if(s.equals("Success")){
                Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                 //When Successs

                //Initialize alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setCancelable(false);
                builder.setTitle(Html.fromHtml( "<font color='#509324'> Success </font>"));
                builder.setMessage("Mail send successfully. ");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                builder.show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Something went wrong ?", Toast.LENGTH_SHORT);
            }
        }
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