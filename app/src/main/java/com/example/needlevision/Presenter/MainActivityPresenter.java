package com.example.needlevision.Presenter;

import android.content.Context;
import android.content.Intent;
import androidx.core.content.FileProvider;
import com.example.needlevision.R;
import com.example.needlevision.View.PostActivity;
import java.io.File;

public class MainActivityPresenter {

    Context c;

    public MainActivityPresenter(Context c) {
        this.c = c;
    }

    public void loadPager(){
        Intent intent = new Intent(c, PostActivity.class);
        c.startActivity(intent);
    }

    public void socialMediaUpload(String photoPath) {
        Intent mediaUpload = new Intent();
        mediaUpload.setAction(Intent.ACTION_SEND);
        mediaUpload.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(c, "com.example.android.FileProvider", new File(photoPath)));
        mediaUpload.setType("image/jpg");
        c.startActivity(Intent.createChooser(mediaUpload, c.getResources().getText(R.string.send_to)));
    }
}
