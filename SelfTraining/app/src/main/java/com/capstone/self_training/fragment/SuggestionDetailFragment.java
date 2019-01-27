package com.capstone.self_training.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.capstone.self_training.R;
import com.capstone.self_training.adapter.MainSuggestionDetailAdapter;
import com.capstone.self_training.model.SuggestionDetail;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

public class SuggestionDetailFragment extends Fragment {

    private class ViewHolder{
        ImageView imgUrl;
        ImageView imgStandardUrl;
        TextView result;
    }
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewHolder viewHolder = null;
        savedInstanceState = getArguments();
        SuggestionDetail suggestionDetail = (SuggestionDetail) savedInstanceState.getSerializable("suggestionDetail");
        Log.e("detail = ",suggestionDetail.getId() + " - "+suggestionDetail.getResult());
        if(view == null){
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.suggestion_detail_fragment,container,false);


            viewHolder.imgUrl = (ImageView)view.findViewById(R.id.suggestionDetail_imageView_id_trainer);
            viewHolder.imgStandardUrl = (ImageView)view.findViewById(R.id.suggestionDetail_imageView_id_trainee);
            viewHolder.result = (TextView)view.findViewById(R.id.suggestionDetail_resultComment);
            view.setTag(viewHolder);
            Log.e("ahihi ","ahihii");
        }else{
            viewHolder = (ViewHolder)view.getTag();
            Log.e("da vao dây","da vao đây rồi nè");
        }


            Picasso.get().load(suggestionDetail.getImgUrl()).placeholder(R.drawable.error).
                    error(R.drawable.errors).into(viewHolder.imgUrl);
            Picasso.get().load(suggestionDetail.getImgStandardUrl()).placeholder(R.drawable.error).
                    error(R.drawable.errors).into(viewHolder.imgStandardUrl);
            viewHolder.result.setText(suggestionDetail.getResult().toString());
            return view;
    }
    public static SuggestionDetailFragment newInstance(SuggestionDetail suggestionDetail){
        SuggestionDetailFragment f = new SuggestionDetailFragment();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putSerializable("suggestionDetail", suggestionDetail);
        f.setArguments(args);
        return f;
    }

}
