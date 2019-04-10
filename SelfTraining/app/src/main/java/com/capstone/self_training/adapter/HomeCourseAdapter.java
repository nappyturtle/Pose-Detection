package com.capstone.self_training.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.capstone.self_training.activity.CourseDetailPayment;
import com.capstone.self_training.activity.LoginActivity;
import com.capstone.self_training.activity.TrainerVideoListActivity;
import com.capstone.self_training.dto.CourseDTO;
import com.capstone.self_training.helper.TimeHelper;
import com.capstone.self_training.service.dataservice.EnrollmentService;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

public class HomeCourseAdapter extends RecyclerView.Adapter<HomeCourseAdapter.CourseHolder> {

    private Context context;
    private List<CourseDTO> list;
    int trainerId;
    int currentUserId;
    String token;
    private static final int REQUEST_CODE_LOGIN = 0x9345;

    public HomeCourseAdapter(List<CourseDTO> list, Context context, int currentUserId) {
        this.list = list;
        this.context = context;
        this.currentUserId = currentUserId;
    }
    public HomeCourseAdapter(List<CourseDTO> list, Context context, int trainerId, int currentUserId, String token) {
        this.list = list;
        this.context = context;
        this.trainerId = trainerId;
        this.currentUserId = currentUserId;
        this.token = token;
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
            holder.tvCourseName.setText(courseDto.getCourse().getName());
            holder.tvTrainerName.setText(courseDto.getTrainerName());
            holder.tvCreatedTime.setText(TimeHelper.showPeriodOfTime(courseDto.getCourse().getCreatedTime()));
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            String moneyDots = decimalFormat.format(courseDto.getCourse().getPrice())+",000 đồng ";
            holder.tvPrice.setText(moneyDots);
            holder.tvRegis.setText(courseDto.getNumberOfRegister() + " lượt đăng kí");
            holder.tvVideoQuantity.setText(courseDto.getNumberOfVideoInCourse() + "");

            holder.lnCourseItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentUserId == 0) {
                        Toast.makeText(context, "Bạn chưa đăng nhập!!!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, LoginActivity.class);
                        ((Activity) context).startActivityForResult(intent, REQUEST_CODE_LOGIN);
                    } else if (trainerId == currentUserId || checkEnrollmentExisted(token, currentUserId, courseDto.getCourse().getId())) {
                        Intent intent = new Intent(context, TrainerVideoListActivity.class);
                        intent.putExtra("courseId", courseDto.getCourse().getId());
                        intent.putExtra("trainerId", trainerId);
                        context.startActivity(intent);
//                        Toast.makeText(context, "Bạn không thể mua khóa học của chính mình", Toast.LENGTH_SHORT).show();
                    } else {
//                        if(checkEnrollmentExisted(token,currentUserId,courseDto.getCourse().getId())){
//                            Toast.makeText(context, "Bạn đã mua khóa học này", Toast.LENGTH_SHORT).show();
//                        } else {
                            Intent intent = new Intent(context, CourseDetailPayment.class);
                            intent.putExtra("courseDTO", courseDto);
                            context.startActivity(intent);
//                        }
                    }
                }
            });
        }
    }
    private boolean checkEnrollmentExisted(String token, int traineeId, int courseId){
        EnrollmentService enrollmentService = new EnrollmentService();
        return enrollmentService.checkEnrollmentExistedOrNot(token,traineeId,courseId);
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
