package com.capstone.self_training.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.capstone.self_training.R;
import com.capstone.self_training.dto.EnrollmentDTO;
import com.capstone.self_training.model.Account;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ManageTraineeTrainerAdapter extends BaseAdapter{
    private List<Account> list;
    private Context context;
    public ManageTraineeTrainerAdapter(List<Account> list, Context context){
        this.list = list;
        this.context = context;
    }
    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_manage_trainee_trainer, null);
            // ánh xạ view
            viewHolder.circleImageView = (CircleImageView) convertView.findViewById(R.id.manage_traineeTrainer_circleImageview);
            viewHolder.name = (TextView) convertView.findViewById(R.id.txtName_manageTraineeTrainer);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // gán giá trị
        final Account dto = list.get(position);
        Picasso.get().load(dto.getImgUrl()).into(viewHolder.circleImageView);

        viewHolder.name.setText(dto.getUsername().toString());

        return convertView;
    }
    private class ViewHolder{
        CircleImageView circleImageView;
        TextView name;
    }
}
