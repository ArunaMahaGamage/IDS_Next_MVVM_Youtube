package com.aruna.mvvmexample;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.aruna.mvvmexample.adapters.RecyclerAdapter;
import com.bumptech.glide.Glide;

public class DetailsActivity extends AppCompatActivity {
    private String name,url;
    private ImageView iv_logo;
    private TextView tv_name;
    private VideoView Vv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        name = intent.getStringExtra("title");
        url = intent.getStringExtra("image");

        initUI();
    }

    void initUI() {
        iv_logo = findViewById(R.id.iv_img);
        tv_name = findViewById(R.id.tv_name);
        Vv = (VideoView) findViewById(R.id.videoView);

        Glide.with(this)
                .load(url)
                .into(iv_logo);
        tv_name.setText(name);

        String link = "https://www.radiantmediaplayer.com/media/bbb-360p.mp4";

        Vv.setVideoURI(Uri.parse(link));
        Vv.setMediaController(new MediaController(this));
        Vv.requestFocus();
        new Thread(new Runnable() {
            public void run() {
                Vv.start();
            }
        }).start();
    }
}
