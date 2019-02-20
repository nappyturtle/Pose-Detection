package com.example.pdst.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pdst.Activity.PlayVideoActivity;
import com.example.pdst.Model.Video;
import com.example.pdst.R;
import com.example.pdst.Utils.TransformDataUtil;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeVideoAdapter extends RecyclerView.Adapter<HomeVideoAdapter.ViewHolder> {
    private List<Video> models;
    private Context context;

    public HomeVideoAdapter(List<Video> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home_video, parent , false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Video model = models.get(position);

        //BIND IMAGE
        Picasso.get().load(model.getThumbnail_url()).fit().into(holder.thumbnail);
        Picasso.get().load(model.getAccount().getImg_url()).fit().into(holder.userImg);

        //BIND TEXT
        holder.title.setText(model.getTitle());
        holder.userName.setText(model.getAccount().getUsername());

        holder.postTime.setText(TransformDataUtil.dateToTimestampAgoText(model.getCreated_time()));
        holder.totalView.setText(TransformDataUtil.totalViewToText(model.getNum_of_view()));

        //BIND CLICK EVENT
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intend to View Video Activity here
                Intent intent = new Intent(context, PlayVideoActivity.class);
                intent.putExtra("PLAYVIDEO", model);
                context.startActivity(intent);
//                Toast.makeText(context, "Clicked to view video", Toast.LENGTH_SHORT).show();

            }
        });

        holder.userImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intend to Profile Activity here
                Toast.makeText(context, "View Profile Clicked", Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView thumbnail;
        public CircleImageView userImg;
        public TextView userName;
        public TextView title;
        public TextView postTime;
        public TextView totalView;


        public ViewHolder(View itemView) {
            super(itemView);

            thumbnail = (ImageView) itemView.findViewById(R.id.home_video_thumbnail);
            userImg = (CircleImageView) itemView.findViewById(R.id.home_video_userImg);
            userName = (TextView) itemView.findViewById(R.id.home_video_username);
            title = (TextView) itemView.findViewById(R.id.home_video_title);
            postTime = (TextView) itemView.findViewById(R.id.home_video_time);
            totalView = (TextView) itemView.findViewById(R.id.home_video_view);
        }
    }
}
