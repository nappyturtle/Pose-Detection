package com.capstone.self_training.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.capstone.self_training.R;
import com.capstone.self_training.model.Course;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CourseByNameAdapter extends ArrayAdapter<Course> {
    public CourseByNameAdapter(@NonNull Context context, ArrayList<Course> courses) {
        super(context, 0, courses);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @SuppressLint("ResourceAsColor")
    private View initView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_course_by_name, parent, false);
        }
        TextView tvCourseName = convertView.findViewById(R.id.tvCourseNAme);
        ImageView ivCourseThumbnail = convertView.findViewById(R.id.iv_course_by_name);
        TextView tvCourseItemPrice = convertView.findViewById(R.id.tv_course_item_price);

        Course cur = getItem(position);
        if (cur != null) {
            if (cur.getName().equalsIgnoreCase("miễn phí") || cur.getName().equalsIgnoreCase("mien phi") || cur.getPrice() == 0) {
                tvCourseName.setText("Miễn phí");
                //tvCourseName.setTextColor(android.R.color.holo_green_light);
                tvCourseName.setTextColor(R.color.holo_green_light);
                tvCourseName.setGravity(View.TEXT_ALIGNMENT_TEXT_START);
                tvCourseItemPrice.setVisibility(View.INVISIBLE);
                ivCourseThumbnail.setVisibility(View.INVISIBLE);
            } else {
                tvCourseName.setText(cur.getName());
                tvCourseItemPrice.setText(cur.getPrice() + ".000 đ");
                Picasso.get().load(cur.getThumbnail()).fit().into(ivCourseThumbnail);
            }

        }
        return convertView;
    }
    /*@NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    pr
    *//*private View initView(int pos, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_course_by_name, parent, false);
        }

        TextView tvCourseName = convertView.findViewById(R.id.tvCourseNAme);

        Course current = getItem(pos);

        if (currentCate != null) {
            categoryItem.setText(currentCate.getName());
        }

        return convertView;
    }*/
}
