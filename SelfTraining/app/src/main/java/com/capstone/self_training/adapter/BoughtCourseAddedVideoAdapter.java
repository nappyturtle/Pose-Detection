package com.capstone.self_training.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.capstone.self_training.R;
import com.capstone.self_training.helper.TimeHelper;
import com.capstone.self_training.model.Video;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BoughtCourseAddedVideoAdapter extends BaseAdapter {
    private List<Video> models;
    private Context context;

    public BoughtCourseAddedVideoAdapter(List<Video> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int position) {
        return models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VideoViewHolder videoViewHolder = null;
        if (convertView == null) {
            videoViewHolder = new VideoViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_dialog_video_updated, null);

            videoViewHolder.thumbnailVideo = (ImageView) convertView.findViewById(R.id.iv_video_thumbnail_dialog_video_updated);
            videoViewHolder.videoName = (TextView) convertView.findViewById(R.id.video_name_dialog_video_updated);
            videoViewHolder.createdTime = (TextView) convertView.findViewById(R.id.video_created_time_dialog_video_updated);

            convertView.setTag(videoViewHolder);
        } else {
            videoViewHolder = (VideoViewHolder) convertView.getTag();
        }
        final Video model = models.get(position);
        Picasso.get().load(model.getThumnailUrl()).fit().into(videoViewHolder.thumbnailVideo);
        videoViewHolder.videoName.setText(model.getTitle());
        videoViewHolder.createdTime.setText(TimeHelper.showPeriodOfTime(model.getCreatedTime()));
        return convertView;
    }

    public class VideoViewHolder {
        public ImageView thumbnailVideo;
        public TextView videoName;
        public TextView createdTime;

    }

}


