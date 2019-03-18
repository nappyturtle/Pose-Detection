package com.capstone.self_training.adapter;

import android.app.Activity;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.self_training.R;
import com.capstone.self_training.activity.CourseDetailPayment;
import com.capstone.self_training.activity.CourseInforActivity;
import com.capstone.self_training.activity.LoginActivity;
import com.capstone.self_training.activity.SuggestionDetailListByTrainerActivity;
import com.capstone.self_training.activity.TrainerUploadedCourseActivity;
import com.capstone.self_training.dto.CourseDTO;
import com.capstone.self_training.dto.EnrollmentDTO;
import com.capstone.self_training.helper.TimeHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrainerUploadedCourseAdapter extends BaseAdapter {
    private List<CourseDTO> models;
    private Context context;

    public TrainerUploadedCourseAdapter(List<CourseDTO> models, Context context) {
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
            courseViewHolder.price = (TextView) convertView.findViewById(R.id.txtPrice_item_bought_course);
            convertView.setTag(courseViewHolder);
        }else{
            courseViewHolder = (CourseViewHolder)convertView.getTag();
        }
        final CourseDTO courseDTO = models.get(position);
        Picasso.get().load(courseDTO.getCourse().getThumbnail()).into(courseViewHolder.thumbnailCourse);

        courseViewHolder.quantityVideo.setText(String.valueOf(courseDTO.getNumberOfVideoInCourse()));
        courseViewHolder.coursename.setText(courseDTO.getCourse().getName());
        courseViewHolder.trainername.setMaxLines(1);
        courseViewHolder.trainername.setEllipsize(TextUtils.TruncateAt.END);
        courseViewHolder.trainername.setText(courseDTO.getTrainerName());
        courseViewHolder.postTime.setText(TimeHelper.showPeriodOfTime(courseDTO.getCourse().getCreatedTime()));
        if(courseDTO.getCourse().getPrice() == 0){
            courseViewHolder.price.setText(courseDTO.getCourse().getPrice() + "đ");
            courseViewHolder.price.setTextColor(Color.RED);
        }else {
            courseViewHolder.price.setText(courseDTO.getCourse().getPrice() + ".000đ");
            courseViewHolder.price.setTextColor(Color.RED);
        }
        courseViewHolder.thumbnailCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,CourseInforActivity.class);
                intent.putExtra("courseDTO",courseDTO);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ((Activity)context).startActivityForResult(intent, TrainerUploadedCourseActivity.REQUEST_CODE_EDIT_COURSE);
            }
        });
        return convertView;
    }

    public class CourseViewHolder {
        public ImageView thumbnailCourse;
        public TextView quantityVideo;
        public TextView coursename;
        public TextView trainername;
        public TextView postTime;
        public TextView price;
    }

}
