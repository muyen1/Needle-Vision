package com.example.needlevision.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.needlevision.Post;
import com.example.needlevision.PostActivity;
import com.example.needlevision.R;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;

public class posts_fragment extends Fragment {

    ViewGroup context;
    // data
    ArrayList<String> userIds;
    ArrayList<String> dess;
    ArrayList<String> statuss;
    ArrayList<String> dates;
    double lats[];
    double longs[];
    ArrayList<String> imageurls;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.posts_page,container,false);

        context = rootView;

        // firebase date
        // finally got the data here
        userIds = getArguments().getStringArrayList("userIds");
        dess = getArguments().getStringArrayList("dess");
        statuss = getArguments().getStringArrayList("statuss");
        dates = getArguments().getStringArrayList("dates");
        lats = getArguments().getDoubleArray("lats");
        longs = getArguments().getDoubleArray("lngs");
        imageurls = getArguments().getStringArrayList("imageurls");

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.posts_menu, menu);
        ((PostActivity) getActivity()).setActionBarTitle("Posts");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.filter_option_btn){
            Snackbar.make(context, "Filter", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        } else if (id == R.id.logout_option_btn){
            Snackbar.make(context, "Logout", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
