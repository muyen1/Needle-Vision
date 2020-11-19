package com.example.needlevision.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.needlevision.Post;
import com.example.needlevision.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class PostListAdapter extends ArrayAdapter<Post> {

    // activity instance
    private Activity context;
    // list of offices
    private List<Post> postsList;

    ImageView img;

    public PostListAdapter(Activity context, List<Post> postsList) {
        super(context, R.layout.post_layout, postsList);
        this.context = context;
        this.postsList = postsList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        // view of list items
        View listViewItem = inflater.inflate(R.layout.post_layout, null, true);

        img = listViewItem.findViewById(R.id.post_img);
        TextView status = listViewItem.findViewById(R.id.post_status);
        TextView date = listViewItem.findViewById(R.id.post_date);
        TextView time = listViewItem.findViewById(R.id.post_time);
        TextView location = listViewItem.findViewById(R.id.post_location);
        TextView des = listViewItem.findViewById(R.id.post_description);
        // textview to hold post distance
        TextView dis = listViewItem.findViewById(R.id.post_dis);


        Post pt = postsList.get(position);
        // setting image
        if(pt.getImageURL().isEmpty()) {
            Toast.makeText(context.getApplicationContext(), "No Image Url!!", Toast.LENGTH_LONG).show();
        } else {
            LoadImage loadImage = new LoadImage(img);
            loadImage.execute(pt.getImageURL());
        }
        status.setText(pt.getStatus());

        if (pt.getDate().length() > 10) {
            date.setText(pt.getDate().substring(0, 10));
            time.setText(pt.getDate().substring(11, pt.getDate().length() -1 ));
        } else {
            date.setText(pt.getDate().substring(0, 9));
            time.setText("No Time");
        }

        location.setText(pt.getLatitude() + ", " + pt.getLongitude());
        des.setText(pt.getDescription());
        // set the office distance
        dis.setText(pt.getDistance() + " km");

        return listViewItem;
    }

    // load image
    private class LoadImage extends AsyncTask<String, Void, Bitmap> {

        ImageView imageView;
        public  LoadImage(ImageView img) {
            this.imageView = img;
        }
        @Override
        protected Bitmap doInBackground(String... strings) {
            String urlLink = strings[0];
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new java.net.URL(urlLink).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }
}
