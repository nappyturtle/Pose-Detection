package com.capstone.self_training.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.self_training.R;
import com.capstone.self_training.dto.CourseDTO;
import com.capstone.self_training.helper.TimeHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeCourseAdapter extends RecyclerView.Adapter<HomeCourseAdapter.CourseHolder> {

    private Context context;
    private List<CourseDTO> list;

    public HomeCourseAdapter(List<CourseDTO> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeCourseAdapter.CourseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        return new HomeCourseAdapter.CourseHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeCourseAdapter.CourseHolder holder, int position) {
        final CourseDTO courseDto = list.get(position);
        if (courseDto != null) {
            Picasso.get().load(courseDto.getCourse().getThumbnail()).fit().into(holder.ivCourseThumbnail);
            holder.tvCourseName.setText(courseDto.getCourse().getName().toString().trim());
            holder.tvTrainerName.setText(courseDto.getTrainerName().toString().trim());
            holder.tvCreatedTime.setText(TimeHelper.showPeriodOfTime(courseDto.getCourse().getCreatedTime()));
            holder.tvPrice.setText(courseDto.getCourse().getPrice() + ".000 đ");
            holder.tvRegis.setText(courseDto.getNumberOfRegister() + " lượt đăng kí");
            holder.tvVideoQuantity.setText(courseDto.getNumberOfVideoInCourse() + "");

            holder.lnCourseItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Bạn phải mua khóa học này!", Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class CourseHolder extends RecyclerView.ViewHolder {

        private ImageView ivCourseThumbnail;
        private TextView tvVideoQuantity, tvCourseName, tvTrainerName, tvCreatedTime, tvRegis, tvPrice;
        private LinearLayout lnCourseItem;

        public CourseHolder(View itemView) {
            super(itemView);

            ivCourseThumbnail = itemView.findViewById(R.id.iv_course_thumbnail);
            tvCourseName = itemView.findViewById(R.id.tv_course_name);
            tvTrainerName = itemView.findViewById(R.id.tv_trainer_name);
            tvVideoQuantity = itemView.findViewById(R.id.tv_video_quantity);
            tvCreatedTime = itemView.findViewById(R.id.tv_course_created_time);
            tvRegis = itemView.findViewById(R.id.tv_course_regis_quantity);
            tvPrice = itemView.findViewById(R.id.tv_course_price);
            lnCourseItem = itemView.findViewById(R.id.ln_course_item);
        }
    }
}
