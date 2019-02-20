package com.capstone.self_training.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.capstone.self_training.R;
import com.capstone.self_training.activity.PlayVideoActivity;
import com.capstone.self_training.model.Video;
import com.capstone.self_training.util.TransformDataUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RelateVideoAdapter extends RecyclerView.Adapter<RelateVideoAdapter.ViewHolder> {
    private List<Video> models;
    private Context context;

    public RelateVideoAdapter(List<Video> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_relate_video, parent , false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Video video = models.get(position);

        //BIND DATA
        Picasso.get().load(video.getThumnailUrl()).fit().into(holder.thumbnail);

        holder.title.setText(video.getTitle());
        holder.username.setText(video.getUsername());
        holder.totalView.setText(TransformDataUtil.totalViewToText(video.getNumOfView()));

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intend to View Video Activity here
                Intent intent = new Intent(context, PlayVideoActivity.class);
                intent.putExtra("PLAYVIDEO", video);
                context.startActivity(intent);
                ((Activity)context).finish();

            }
        });
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intend to View Video Activity here
                Intent intent = new Intent(context, PlayVideoActivity.class);
                intent.putExtra("PLAYVIDEO", video);
                context.startActivity(intent);
                ((Activity)context).finish();

            }
        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        public TextView title, totalView, username;
        public ViewHolder(View itemView) {
            super(itemView);

            thumbnail = (ImageView) itemView.findViewById(R.id.relate_video_thumbnail);
            title = (TextView) itemView.findViewById(R.id.relate_video_title);
            totalView = (TextView) itemView.findViewById(R.id.relate_video_view);
            username = (TextView) itemView.findViewById(R.id.relate_video_username);
        }
    }
}
