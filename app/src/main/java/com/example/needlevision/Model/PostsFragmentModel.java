package com.example.needlevision.Model;

import android.content.Context;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class PostsFragmentModel {
    private Context context;
    List<Post> listPosts;

    public PostsFragmentModel(Context context){
        this.context = context;
        this.listPosts = new ArrayList<>();
    }

    public void generateListPost( ArrayList<String> userIds,  ArrayList<String> dess,  ArrayList<String> statuss,
                                  ArrayList<String> dates, ArrayList<String> imageurls, double lats[], double longs[]){
        for (int i = 0; i < statuss.size(); i++) {
            Post pt;
            // getting the logLat
            LatLng ptLatlng = new LatLng(lats[i], longs[i]);
            pt = new Post(userIds.get(i), dess.get(i), statuss.get(i),
                    dates.get(i), lats[i], longs[i], imageurls.get(i));
            listPosts.add(pt);
        }
    }

    public List<Post> getListPost(){
        return listPosts;
    }


}
