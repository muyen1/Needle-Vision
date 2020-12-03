package com.example.needlevision.Presenter;

import android.content.Context;

import com.example.needlevision.Model.MapFragmentModel;
import com.google.android.gms.maps.GoogleMap;

public class MapFragmentPresenter {
    MapFragmentModel mfp;

    public MapFragmentPresenter(Context context){
        mfp = new MapFragmentModel(context);
    }

    public void getDeviceLocation(){
        mfp.getDeviceLocation();
    }

}
