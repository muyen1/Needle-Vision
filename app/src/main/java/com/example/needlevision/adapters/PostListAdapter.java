package com.example.needlevision.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.needlevision.Post;
import com.example.needlevision.R;

import java.util.ArrayList;


public class PostListAdapter extends ArrayAdapter<Post> {

    private Context context;
    private int resource;

    public PostListAdapter(Context context, int resource, ArrayList<Post> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        boolean status = getItem(position).isDisposed();
//        String date = getItem(position).getDate();
//        String time = getItem(position).getTime();
//        String location = getItem(position).getLocation();
//        String description = getItem(position).getDescription();
//
//        //Post post = new Post(status, date, time, location, description);
//
//        LayoutInflater inflater = LayoutInflater.from(context);
//        convertView = inflater.inflate(resource, parent, false);
//
//        // Needle still active
//        if (status){
//            ((TextView)convertView.findViewById(R.id.post_status)).setText(R.string.disposed);
//            ((TextView)convertView.findViewById(R.id.post_status)).setTextColor(ContextCompat.getColor(context, R.color.colorNull));
//        } else{
//            ((TextView)convertView.findViewById(R.id.post_status)).setText(R.string.active);
//            ((TextView)convertView.findViewById(R.id.post_status)).setTextColor(ContextCompat.getColor(context, R.color.colorWarning));
//        }
//        ((TextView)convertView.findViewById(R.id.post_date)).setText(date);
//        ((TextView)convertView.findViewById(R.id.post_time)).setText(time);
//        ((TextView)convertView.findViewById(R.id.post_location)).setText(location);
//        ((TextView)convertView.findViewById(R.id.post_description)).setText(description);

        return convertView;
    }
}
