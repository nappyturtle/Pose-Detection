package com.capstone.self_training.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.capstone.self_training.R;
import com.capstone.self_training.activity.TraineeChannelActivity;
import com.capstone.self_training.model.Account;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TraineeRegisteredActivityAdapter extends RecyclerView.Adapter<TraineeRegisteredActivityAdapter.TrainerCourseHolder> {
    private List<Account> traineeList;
    private Context context;

    public TraineeRegisteredActivityAdapter(List<Account> traineeList, Context context) {
        this.traineeList = traineeList;
        this.context = context;
    }

    @NonNull
    @Override
    public TraineeRegisteredActivityAdapter.TrainerCourseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bought_trainer_course, parent, false);
        return new TrainerCourseHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull TraineeRegisteredActivityAdapter.TrainerCourseHolder holder, int position) {
        final Account account = traineeList.get(position);

        Picasso.get().load(account.getImgUrl()).fit().into(holder.circleImageViewTrainer);

        holder.traineename.setMaxLines(1);
        holder.traineename.setEllipsize(TextUtils.TruncateAt.END);
        holder.traineename.setText(account.getUsername());
        holder.circleImageViewTrainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TraineeChannelActivity.class);
                intent.putExtra("accountTemp", account);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return traineeList == null ? 0 : traineeList.size();
    }

    public class TrainerCourseHolder extends RecyclerView.ViewHolder {
        public CircleImageView circleImageViewTrainer;
        public TextView traineename;

        public TrainerCourseHolder(View itemView) {
            super(itemView);
            circleImageViewTrainer = (CircleImageView) itemView.findViewById(R.id.trainer_bought_course_thumbnail);
            traineename = (TextView) itemView.findViewById(R.id.txtTrainerName_bought_course);
        }
    }
}

