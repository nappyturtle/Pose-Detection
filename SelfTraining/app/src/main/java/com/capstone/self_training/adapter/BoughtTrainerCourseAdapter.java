package com.capstone.self_training.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.capstone.self_training.R;
import com.capstone.self_training.dto.EnrollmentDTO;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BoughtTrainerCourseAdapter extends RecyclerView.Adapter<BoughtTrainerCourseAdapter.TrainerCourseHolder> {
    private List<EnrollmentDTO> trainerList;
    private Context context;

    public BoughtTrainerCourseAdapter(List<EnrollmentDTO> trainerList, Context context) {
        this.trainerList = trainerList;
        this.context = context;
    }

    @NonNull
    @Override
    public TrainerCourseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bought_trainer_course, parent, false);
        return new TrainerCourseHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainerCourseHolder holder, int position) {
        EnrollmentDTO enrollmentDTO = trainerList.get(position);
        Picasso.get().load(enrollmentDTO.getAccountThumbnail()).fit().into(holder.circleImageViewTrainer);
        holder.trainername.setMaxLines(1);
        holder.trainername.setEllipsize(TextUtils.TruncateAt.END);
        holder.trainername.setText(enrollmentDTO.getUsername());
        holder.circleImageViewTrainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return trainerList == null ? 0 : trainerList.size();
    }

    public class TrainerCourseHolder extends RecyclerView.ViewHolder {
        public CircleImageView circleImageViewTrainer;
        public TextView trainername;

        public TrainerCourseHolder(View itemView) {
            super(itemView);
            circleImageViewTrainer = (CircleImageView) itemView.findViewById(R.id.trainer_bought_course_thumbnail);
            trainername = (TextView) itemView.findViewById(R.id.txtTrainerName_bought_course);
        }
    }
}
