package com.capstone.self_training.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.capstone.self_training.R;
import com.capstone.self_training.model.Course;

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

    private View initView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_course_by_name, parent, false);
        }
        TextView tvCourseName = convertView.findViewById(R.id.tvCourseNAme);
        Course cur = getItem(position);
        if(cur != null){
            tvCourseName.setText(cur.getName());
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
