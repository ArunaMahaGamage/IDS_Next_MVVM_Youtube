package com.aruna.mvvmexample.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.aruna.mvvmexample.R;
import com.aruna.mvvmexample.models.NicePlace;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<NicePlace> mNicePlaces = new ArrayList<>();
    private Context mContext;

    public RecyclerAdapter(Context context, List<NicePlace> nicePlaces) {
        mNicePlaces = nicePlaces;
        mContext = context;
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

        // Set the image
        RequestOptions defaultOptions = new RequestOptions()
                .error(R.drawable.ic_launcher_background);
        Glide.with(mContext)
                .setDefaultRequestOptions(defaultOptions)
                .load(mNicePlaces.get(i).getImageUrl())
                .into(((ViewHolder)viewHolder).mImage);

//        String link = "http://learntv.lk/playlists/grade-10_playlist_medium.m3u8";
        String link = "https://www.radiantmediaplayer.com/media/bbb-360p.mp4";

        ((ViewHolder)viewHolder).Vv.setVideoURI(Uri.parse(link));
        ((ViewHolder)viewHolder).Vv.setMediaController(new MediaController(mContext));
        ((ViewHolder)viewHolder).Vv.requestFocus();
        new Thread(new Runnable() {
            public void run() {
                ((ViewHolder)viewHolder).Vv.start();
            }
        }).start();

        MediaController ctrl = new MediaController(mContext);
        ctrl.setVisibility(View.GONE);
        ((ViewHolder)viewHolder).Vv.setMediaController(ctrl);
    }

    @Override
    public int getItemCount() {
        return mNicePlaces.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView mImage;
        private TextView mName;
        private VideoView Vv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.image);
            mName = itemView.findViewById(R.id.image_name);
            Vv = (VideoView) itemView.findViewById(R.id.videoView);
        }
    }
}
