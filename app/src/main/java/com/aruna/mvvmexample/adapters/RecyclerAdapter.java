package com.aruna.mvvmexample.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.aruna.mvvmexample.DetailsActivity;
import com.aruna.mvvmexample.R;
import com.aruna.mvvmexample.models.NicePlace;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RecyclerAdapterEvent recyclerAdapterEvent;
    private List<NicePlace> mNicePlaces = new ArrayList<>();
    private Context mContext;

    public RecyclerAdapter(Context context, List<NicePlace> nicePlaces, RecyclerAdapterEvent recyclerAdapterEvents) {
        mNicePlaces = nicePlaces;
        mContext = context;
        recyclerAdapterEvent = recyclerAdapterEvents;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {

        // Set the name of the 'NicePlace'
        ((ViewHolder)viewHolder).mName.setText(mNicePlaces.get(i).getTitle());
        ((ViewHolder)viewHolder).mDetails.setText(mNicePlaces.get(i).getTitle());

        // Set the image
        if (!(mNicePlaces.get(i).getVideo_url().equalsIgnoreCase(""))) {
            RequestOptions defaultOptions = new RequestOptions()
                    .error(R.drawable.ic_launcher_background);
            Glide.with(mContext)
                    .setDefaultRequestOptions(defaultOptions)
                    .load(mNicePlaces.get(i).getImageUrl())
                    .into(((ViewHolder) viewHolder).mImage);

//        String link = "http://learntv.lk/playlists/grade-10_playlist_medium.m3u8";
            String link = mNicePlaces.get(i).getVideo_url();

            ((ViewHolder) viewHolder).Vv.setVideoURI(Uri.parse(link));
            ((ViewHolder) viewHolder).Vv.setMediaController(new MediaController(mContext));
            ((ViewHolder) viewHolder).Vv.requestFocus();
            new Thread(new Runnable() {
                public void run() {
                    ((ViewHolder) viewHolder).Vv.start();
                }
            }).start();

            MediaController ctrl = new MediaController(mContext);
            ctrl.setVisibility(View.GONE);
            ((ViewHolder) viewHolder).Vv.setMediaController(ctrl);
        }
//        if ((mNicePlaces.size() -1) == i) {
        if(false) {
            Log.e("RecyclerAdapter ", "mNicePlaces.size() " + (mNicePlaces.size() - 1) + " position i " + i);
            recyclerAdapterEvent.stayPosition(i);
        }
    }

    @Override
    public int getItemCount() {
        return mNicePlaces.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CircleImageView mImage;
        private TextView mName, mDetails;
        private VideoView Vv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.image);
            mName = itemView.findViewById(R.id.image_name);
            mDetails = itemView.findViewById(R.id.mDetails);
            Vv = (VideoView) itemView.findViewById(R.id.videoView);
            Vv.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int position = v.getVerticalScrollbarPosition();
//            recyclerAdapterEvent.onClick(v.getVerticalScrollbarPosition());
            Intent intent = new Intent(mContext, DetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("title", mNicePlaces.get(position).getTitle());
            bundle.putString("image", mNicePlaces.get(position).getImageUrl());
            mContext.startActivity(intent);
        }
    }

    public interface RecyclerAdapterEvent {
        void stayPosition(int pos);
//        void onClick(int position);
    }
}
