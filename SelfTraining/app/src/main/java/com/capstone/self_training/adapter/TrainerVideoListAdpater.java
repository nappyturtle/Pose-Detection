package com.capstone.self_training.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.capstone.self_training.R;
import com.capstone.self_training.activity.PlayVideoActivity;
import com.capstone.self_training.activity.TrainerVideoListActivity;
import com.capstone.self_training.activity.TrainerVideoUploadedActivity;
import com.capstone.self_training.activity.VideoInforActivity;
import com.capstone.self_training.helper.TimeHelper;
import com.capstone.self_training.model.Account;
import com.capstone.self_training.model.Video;
import com.capstone.self_training.service.dataservice.AccountService;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrainerVideoListAdpater extends RecyclerView.Adapter<TrainerVideoListAdpater.VideoHolder> {
    private List<Video> listVideo;
    private Context context;
    private int trainerId;
    private SharedPreferences mPerferences;
    private int currentUserId;
    private AccountService accountService;
    public TrainerVideoListAdpater(List<Video> listVideo, int trainerId, Context context) {
        this.trainerId = trainerId;
        this.listVideo = listVideo;
        this.context = context;
        mPerferences = PreferenceManager.getDefaultSharedPreferences(context);
        currentUserId = mPerferences.getInt(context.getString(R.string.id), 0);
    }


    @NonNull
    @Override
    public TrainerVideoListAdpater.VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trainer_list_video, parent, false);
        return new TrainerVideoListAdpater.VideoHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainerVideoListAdpater.VideoHolder holder, int position) {
        final Video video = listVideo.get(position);
        if (video != null) {
            Picasso.get().load(video.getThumnailUrl()).fit().into(holder.ivVideoThumbnail);
            holder.tvVideoName.setText(video.getTitle());
            holder.tvVideoCreatedTime.setText(TimeHelper.showPeriodOfTime(video.getCreatedTime()));
            holder.tvVideoViewNumber.setText(video.getNumOfView() + " lượt xem");

            holder.ivVideoEdit.setVisibility(View.INVISIBLE);
            accountService = new AccountService(context);
//            holder.ln_trainer_video_item.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    Intent intent = new Intent(context, VideoInforActivity.class);
//                    intent.putExtra("EDIT_VIDEO", video);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    ((Activity) context).startActivityForResult(intent, TrainerVideoListActivity.REQUEST_CODE_EDIT_VIDEO);
//
//                }
//            });
//            holder.img_play.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context, TrainerVideoUploadedActivity.class);
//                    intent.putExtra("videoUrl", video.getContentUrl());
//                    context.startActivity(intent);
//                }
//            });
            holder.ln_trainer_video_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(currentUserId == trainerId){
//                    if(currentUserId != 0 && roleId == 3){
                        Intent intent = new Intent(context, VideoInforActivity.class);
                        intent.putExtra("EDIT_VIDEO", video);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ((Activity)context).startActivityForResult(intent, TrainerVideoListActivity.REQUEST_CODE_EDIT_VIDEO);
                    }
                    else {
                        Intent intent = new Intent(context, PlayVideoActivity.class);
                        intent.putExtra("PLAYVIDEO", video);
                        intent.putExtra("ACCOUNT", accountService.getAccount(trainerId));
                        context.startActivity(intent);
                    }

                }
            });
            holder.img_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(currentUserId == trainerId) {
                        Intent intent = new Intent(context, TrainerVideoUploadedActivity.class);
                        intent.putExtra("videoUrl", video.getContentUrl());
                        context.startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(context, PlayVideoActivity.class);
                        intent.putExtra("PLAYVIDEO", video);
                        intent.putExtra("ACCOUNT", accountService.getAccount(trainerId));
                        context.startActivity(intent);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return listVideo == null ? 0 : listVideo.size();
    }

    public class VideoHolder extends RecyclerView.ViewHolder {

        private ImageView ivVideoThumbnail, img_play, ivVideoEdit;
        private TextView tvVideoName, tvVideoCreatedTime, tvVideoViewNumber;
        private LinearLayout ln_trainer_video_item;

        public VideoHolder(View itemView) {
            super(itemView);
            ivVideoThumbnail = itemView.findViewById(R.id.iv_video_thumbnail);
            //ivVideoStatus = itemView.findViewById(R.id.trainer_video_status);
            ivVideoEdit = itemView.findViewById(R.id.trainer_video_edit);
            tvVideoName = itemView.findViewById(R.id.trainer_video_name);
            tvVideoCreatedTime = itemView.findViewById(R.id.tv_trainer_video_created_time);
            tvVideoViewNumber = itemView.findViewById(R.id.tv_trainer_video_view_number);
            ivVideoEdit = itemView.findViewById(R.id.trainer_video_edit);
            ln_trainer_video_item = itemView.findViewById(R.id.ln_trainer_video_item);
            img_play = itemView.findViewById(R.id.iv_video_thumbnail_play_video);
        }
    }
}

