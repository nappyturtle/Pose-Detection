package com.capstone.self_training.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.self_training.R;
import com.capstone.self_training.activity.PlayVideoActivity;
import com.capstone.self_training.helper.TimeHelper;
import com.capstone.self_training.model.Account;
import com.capstone.self_training.model.Video;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeVideoAdapter extends RecyclerView.Adapter<HomeVideoAdapter.VideoViewHolder> {
    private List<Video> models;
    private Context context;
    private List<Account> accounts;
    private int currentUserId;

    public HomeVideoAdapter(List<Video> models, Context context, List<Account> accounts) {
        this.models = models;
        this.context = context;
        this.accounts = accounts;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home_video, parent, false);
        return new VideoViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        final Video model = models.get(position);
        final Account account = accounts.get(position);

        //BIND IMAGE
        Picasso.get().load(model.getThumnailUrl()).fit().into(holder.thumbnail);
        Picasso.get().load(account.getImgUrl()).fit().into(holder.userImg);

        //BIND TEXT
        holder.title.setText(model.getTitle());
        holder.userName.setText(account.getUsername());

        holder.postTime.setText(TimeHelper.showPeriodOfTime(model.getCreatedTime()));
        holder.totalView.setText(model.getNumOfView() + " lượt xem");


        //BIND CLICK EVENT
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        currentUserId = sharedPreferences.getInt("com.capstone.self_training.activity.id", 0);
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, PlayVideoActivity.class);
                intent.putExtra("PLAYVIDEO", model);
                intent.putExtra("ACCOUNT", account);
                context.startActivity(intent);

            }
        });

        holder.userImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intend to Profile Activity here
                Toast.makeText(context, "View Profile Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return models == null ? 0 : models.size();

    }


    public class VideoViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        public CircleImageView userImg;
        public TextView userName;
        public TextView title;
        public TextView postTime;
        public TextView totalView;
        public TextView twHomeVideoPayment;


        public VideoViewHolder(View itemView) {
            super(itemView);

            thumbnail = (ImageView) itemView.findViewById(R.id.home_video_thumbnail);
            userImg = (CircleImageView) itemView.findViewById(R.id.home_video_userImg);
            userName = (TextView) itemView.findViewById(R.id.home_video_username);
            title = (TextView) itemView.findViewById(R.id.home_video_title);
            postTime = (TextView) itemView.findViewById(R.id.home_video_time);
            totalView = (TextView) itemView.findViewById(R.id.home_video_view);
        }
    }

}

