package com.capstone.self_training.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.capstone.self_training.R;
import com.capstone.self_training.helper.TimeHelper;
import com.capstone.self_training.model.Video;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.capstone.self_training.R.drawable.ic_small_earth;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoHolder> {

    private List<Video> listVideo;
    private Context context;

    public VideoListAdapter(List<Video> listVideo, Context context) {
        this.listVideo = listVideo;
        this.context = context;
    }

    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trainer_list_video, parent, false);
        return new VideoHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {
        Video video = listVideo.get(position);
        if (video != null) {
            Picasso.get().load(video.getThumnailUrl()).fit().into(holder.ivVideoThumbnail);
            holder.tvVideoName.setText(video.getTitle());
            holder.tvVideoCreatedTime.setText(TimeHelper.showPeriodOfTime(video.getCreatedTime()));
            holder.tvVideoViewNumber.setText(video.getNumOfView() + " lượt xem");
            if (video.getStatus() != null) {
                if (video.getStatus().equalsIgnoreCase("active")) {
                    holder.ivVideoStatus.setImageResource(R.drawable.ic_small_earth);
                } else {
                    holder.ivVideoStatus.setImageResource(R.drawable.ic_small_lock);
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return listVideo == null ? 0 : listVideo.size();
    }

    public class VideoHolder extends RecyclerView.ViewHolder {

        private ImageView ivVideoThumbnail, ivVideoStatus, ivVideoEdit;
        private TextView tvVideoName, tvVideoCreatedTime, tvVideoViewNumber;

        public VideoHolder(View itemView) {
            super(itemView);
            ivVideoThumbnail = itemView.findViewById(R.id.iv_video_thumbnail);
//            ivVideoStatus = itemView.findViewById(R.id.trainer_video_status);
            ivVideoEdit = itemView.findViewById(R.id.trainer_video_edit);
            tvVideoName = itemView.findViewById(R.id.trainer_video_name);
            tvVideoCreatedTime = itemView.findViewById(R.id.tv_trainer_video_created_time);
            tvVideoViewNumber = itemView.findViewById(R.id.tv_trainer_video_view_number);
        }
    }
}
