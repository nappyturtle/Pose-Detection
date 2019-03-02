package com.capstone.self_training.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.capstone.self_training.R;
import com.capstone.self_training.activity.TraineeVideoUploadedActivity;
import com.capstone.self_training.model.Suggestion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SuggestionAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Suggestion> suggestionList;


    public SuggestionAdapter(Context context, ArrayList<Suggestion> suggestionList) {
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

    private class ViewHolder {
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
        final Suggestion suggestion = suggestionList.get(position);
        Picasso.get().load(suggestion.getThumnailUrl()).placeholder(R.drawable.error).
               into(viewHolder.suggestion_image);

        viewHolder.suggestion_name.setText(suggestion.getVideoName().toString());
        viewHolder.suggestion_date.setText(suggestion.getCreatedTime().toString());
        String status = suggestion.getStatus().toString();
        if(status.equals("active")){
            viewHolder.suggestion_status.setText("Hoàn tất");
            viewHolder.suggestion_status.setTextColor(Color.parseColor("#ff0000"));
        }else if(status.equals("processing")){
            viewHolder.suggestion_status.setText("Đang xử lí");
            viewHolder.suggestion_status.setTextColor(Color.parseColor("#7FFF00"));
        }
        viewHolder.suggestion_image_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,TraineeVideoUploadedActivity.class);
                intent.putExtra("Suggestion",suggestion);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
