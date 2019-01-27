package com.capstone.self_training.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.capstone.self_training.R;
import com.capstone.self_training.model.Suggestion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SuggestionAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Suggestion> suggestionList;
    private int layout;

    public SuggestionAdapter(Context context, int layout, ArrayList<Suggestion> suggestionList) {
        this.context = context;
        this.layout = layout;
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
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);
            // ánh xạ view
            viewHolder.suggestion_image = (ImageView) convertView.findViewById(R.id.suggestionList_item_imageview);
            viewHolder.suggestion_name = (TextView) convertView.findViewById(R.id.suggestionList_item_name);
            viewHolder.suggestion_date = (TextView) convertView.findViewById(R.id.suggestionList_item_date);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // gán giá trị
        Suggestion suggestion = suggestionList.get(position);
        Picasso.get().load(suggestion.getImgUrl()).placeholder(R.drawable.error).
                error(R.drawable.errors).into(viewHolder.suggestion_image);

        viewHolder.suggestion_name.setText(suggestion.getName().toString());
        viewHolder.suggestion_date.setText(suggestion.getCreatedTime().toString());
        return convertView;
    }
}
