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

import com.capstone.self_training.R;
import com.capstone.self_training.activity.SuggestionDetailByTrainerActivity;
import com.capstone.self_training.activity.SuggestionDetailListByTrainerActivity;
import com.capstone.self_training.model.SuggestionDetail;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.support.v4.app.ActivityCompat.startActivityForResult;


public class SuggestionDetailByTrainerAdapter extends RecyclerView.Adapter<SuggestionDetailByTrainerAdapter.SuggtiondDetailHolder> {
    private List<SuggestionDetail> models;
    private Context context;

    public SuggestionDetailByTrainerAdapter(List<SuggestionDetail> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @NonNull
    @Override
    public SuggestionDetailByTrainerAdapter.SuggtiondDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_suggestion_detail_list_by_trainer, parent, false);
        return new SuggestionDetailByTrainerAdapter.SuggtiondDetailHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull SuggestionDetailByTrainerAdapter.SuggtiondDetailHolder holder, int position) {
        final SuggestionDetail model = models.get(position);

        //BIND IMAGE
        Picasso.get().load(model.getImgUrl()).fit().into(holder.img);
        Picasso.get().load(model.getStandardImgUrl()).fit().into(holder.stImg);

    }


    @Override
    public int getItemCount() {
        return models == null ? 0 : models.size();

    }

    public class SuggtiondDetailHolder extends RecyclerView.ViewHolder {
        public ImageView img;
        public ImageView stImg;

        public SuggtiondDetailHolder(View itemView) {
            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.item_suggestion_detail_by_trainer_img);
            stImg = (ImageView) itemView.findViewById(R.id.item_suggestion_detail_by_trainer_stImg);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(context, SuggestionDetailByTrainer.class);
//                    intent.putExtra("SuggestionDetail",models.get(getPosition()));
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent);

                    Intent intent = new Intent(context, SuggestionDetailByTrainerActivity.class);
                    intent.putExtra("SuggestionDetail",models.get(getPosition()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ((Activity)context).startActivityForResult(intent,SuggestionDetailListByTrainerActivity.REQUEST_CODE_SAVE_COMMENT);
                }
            });
        }
    }

}
