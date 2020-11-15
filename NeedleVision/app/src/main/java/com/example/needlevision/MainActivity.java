package com.example.needlevision;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.needlevision.adapters.SlidePagerAdapter;
import com.example.needlevision.fragments.map_fragment;
import com.example.needlevision.fragments.posts_fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager pager;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        loadLoginPage();
    }

    private void loadLoginPage(){
        setContentView(R.layout.login_page);
        findViewById(R.id.guest_btn).setOnClickListener(new View.OnClickListener() {

            // Load the loadPagerPage function
            @Override
            public void onClick(View v) {
                loadPagerPage();
            }
        });
    }

    // Loads the Fragment Slides
    private void loadPagerPage(){
        setContentView(R.layout.activity_main);
        List<Fragment> list = new ArrayList<>();
        list.add(new map_fragment());
        list.add(new posts_fragment());

        pager = findViewById(R.id.pager);
        pagerAdapter = new SlidePagerAdapter(getSupportFragmentManager(), list);
        pager.setAdapter(pagerAdapter);

        loadFAB();
    }

    private void loadFAB(){
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Bring Up Camera", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}