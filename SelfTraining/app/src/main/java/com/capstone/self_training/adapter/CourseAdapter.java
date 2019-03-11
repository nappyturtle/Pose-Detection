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
import com.capstone.self_training.dto.CourseDTO;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseHolder> {

    private Context context;
    private List<CourseDTO> list;

    public CourseAdapter(List<CourseDTO> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public CourseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        return new CourseHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseHolder holder, int position) {
        final CourseDTO courseDTO = list.get(position);
        if (courseDTO != null) {
            Picasso.get().load(courseDTO.getThumbnail()).fit().into(holder.ivCourseThumbnail);

        }

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    public class CourseHolder extends RecyclerView.ViewHolder {

        private ImageView ivCourseThumbnail;
        private TextView tvVideoQuantity, tvCourseName, tvTrainerName, tvCreatedTime, tvRegis, tvPrice;

        public CourseHolder(View itemView) {
            super(itemView);

            ivCourseThumbnail = itemView.findViewById(R.id.iv_course_thumbnail);
            tvCourseName = itemView.findViewById(R.id.tv_course_name);
            tvTrainerName = itemView.findViewById(R.id.tv_trainer_name);
            tvVideoQuantity = itemView.findViewById(R.id.tv_video_quantity);
        }
    }
}
