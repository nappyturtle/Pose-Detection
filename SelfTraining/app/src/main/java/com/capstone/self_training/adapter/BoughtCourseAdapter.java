package com.capstone.self_training.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.self_training.R;
import com.capstone.self_training.dto.EnrollmentDTO;
import com.capstone.self_training.helper.TimeHelper;
import com.squareup.picasso.Picasso;

import java.util.List;


public class BoughtCourseAdapter extends BaseAdapter {
    private List<EnrollmentDTO> models;
    private Context context;

    public BoughtCourseAdapter(List<EnrollmentDTO> models, Context context) {
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
        CourseViewHolder courseViewHolder = null;
        if(convertView == null){
            courseViewHolder = new CourseViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_bought_course, null);

            courseViewHolder.thumbnailCourse = (ImageView) convertView.findViewById(R.id.iv_course__bought_thumbnail);
            courseViewHolder.quantityVideo = (TextView) convertView.findViewById(R.id.tv_bought_video_quantity);
            courseViewHolder.coursename = (TextView) convertView.findViewById(R.id.tv__bought_course_name);
            courseViewHolder.trainername = (TextView) convertView.findViewById(R.id.tv_bought_trainer_name);
            courseViewHolder.postTime = (TextView) convertView.findViewById(R.id.tv_bought_course_created_time);
            convertView.setTag(courseViewHolder);
        }else{
            courseViewHolder = (CourseViewHolder)convertView.getTag();
        }
        EnrollmentDTO enrollmentDTO = models.get(position);
        Picasso.get().load(enrollmentDTO.getThumbnail()).into(courseViewHolder.thumbnailCourse);

        courseViewHolder.quantityVideo.setText(String.valueOf(enrollmentDTO.getTotalVideo()));
        courseViewHolder.coursename.setText(enrollmentDTO.getCourseName().toString());
        courseViewHolder.trainername.setMaxLines(1);
        courseViewHolder.trainername.setEllipsize(TextUtils.TruncateAt.END);
        courseViewHolder.trainername.setText(enrollmentDTO.getUsername().toString());
        courseViewHolder.postTime.setText(TimeHelper.showPeriodOfTime(enrollmentDTO.getCreatedTime().toString()));
        return convertView;
    }

    public class CourseViewHolder {
        public ImageView thumbnailCourse;
        public TextView quantityVideo;
        public TextView coursename;
        public TextView trainername;
        public TextView postTime;

    }

}
