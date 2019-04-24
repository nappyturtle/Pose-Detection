package com.capstone.self_training.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.capstone.self_training.R;
import com.capstone.self_training.activity.TraineeVideoUploadedActivity;
import com.capstone.self_training.dto.SuggestionDTO;
import com.capstone.self_training.helper.TimeHelper;
import com.capstone.self_training.model.Suggestion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SuggestionAdapter extends BaseAdapter {
    private Context context;
    private List<SuggestionDTO> suggestionList;


    public SuggestionAdapter(Context context, List<SuggestionDTO> suggestionList) {
        this.context = context;
        this.suggestionList = suggestionList;
    }

    @Override
    public int getCount() {
        return suggestionList.size();
    }

    @Override
    public Object getItem(int position) {
        return suggestionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        ImageView suggestion_image;
        TextView suggestion_name;
        TextView suggestion_date;
        TextView suggestion_status;
        ImageView suggestion_image_play;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.suggestion_list_item, null);
            // ánh xạ view
            viewHolder.suggestion_image = (ImageView) convertView.findViewById(R.id.suggestionList_item_imageview);
            viewHolder.suggestion_name = (TextView) convertView.findViewById(R.id.suggestionList_item_name);
            viewHolder.suggestion_date = (TextView) convertView.findViewById(R.id.suggestionList_item_date);
            viewHolder.suggestion_status = (TextView) convertView.findViewById(R.id.suggestionList_item_status);
            viewHolder.suggestion_image_play = (ImageView) convertView.findViewById(R.id.suggestionList_item_play);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // gán giá trị
        final SuggestionDTO suggestion = suggestionList.get(position);
        Log.e("SuggestionDTO = ",+suggestion.getId()+" - "+suggestion.getThumnailUrl());
        if(suggestion != null) {
            Picasso.get().load(suggestion.getThumnailUrl()).fit().placeholder(R.drawable.error).
                    into(viewHolder.suggestion_image);

            viewHolder.suggestion_name.setText(suggestion.getVideoName());
            viewHolder.suggestion_date.setText(TimeHelper.showPeriodOfTime(suggestion.getCreatedTime()));
            String status = suggestion.getStatus();
            if (status.equals("active")) {
                viewHolder.suggestion_status.setText("Hoàn tất");
                viewHolder.suggestion_status.setTextColor(Color.parseColor("#2fef21"));
            } else if (status.equals("processing")) {
                viewHolder.suggestion_status.setText("Đang xử lí");
                viewHolder.suggestion_status.setTextColor(Color.parseColor("#ffee00"));
            }
            viewHolder.suggestion_image_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, TraineeVideoUploadedActivity.class);
                    intent.putExtra("Suggestion", suggestion);
                    context.startActivity(intent);
                }
            });
        }
        return convertView;
    }
}
