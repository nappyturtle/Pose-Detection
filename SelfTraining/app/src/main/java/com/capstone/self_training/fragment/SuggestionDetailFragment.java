package com.capstone.self_training.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.capstone.self_training.R;
import com.capstone.self_training.adapter.ExpandableListViewAdapter;
import com.capstone.self_training.model.SuggestionDetail;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SuggestionDetailFragment extends Fragment {

    View view;
    List<String> listHeader; // list chứa các item cha
    HashMap<String, List<String>> listChild;  // list chứa các item con trong mỗi item cha
    ExpandableListViewAdapter adapter;

    // tạo 1 class định nghĩa các component riêng để khi kéo qua , kéo lại thì nó lưu được phần giao diện trước đó
    private class ViewHolder {
        ImageView imgUrl;
        ImageView imgStandardUrl;
        TextView result;
        ExpandableListView expandableListView;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewHolder viewHolder = null;
        savedInstanceState = getArguments();
        SuggestionDetail suggestionDetail = (SuggestionDetail) savedInstanceState.getSerializable("suggestionDetail");

        Log.e("detail = ", suggestionDetail.getId() + " - " + suggestionDetail.getDescription() + " - " + suggestionDetail.getImgUrl()
                + " - " + suggestionDetail.getStandardImgUrl());

        // nếu view ban đầu = null thì sẽ render ra giao diện,
        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.suggestion_detail_fragment, container, false);

            viewHolder.imgUrl = (ImageView) view.findViewById(R.id.suggestionDetail_imageView_id_trainee);
            viewHolder.imgStandardUrl = (ImageView) view.findViewById(R.id.suggestionDetail_imageView_id_trainer);
            viewHolder.expandableListView = (ExpandableListView) view.findViewById(R.id.expandable_listview);

            view.setTag(viewHolder);
            Log.e("ahihi ", "ahihii");
        } else {
            viewHolder = (ViewHolder) view.getTag(); // get ra vì đã có sẵn ko cần phải render lại, đỡ tốn performance
            Log.e("da vao dây", "da vao đây rồi nè");
        }

        Picasso.get().load(suggestionDetail.getImgUrl()).fit().into(viewHolder.imgUrl);
        Picasso.get().load(suggestionDetail.getStandardImgUrl()).fit().into(viewHolder.imgStandardUrl);

        listHeader = new ArrayList<>();
        listChild = new HashMap<>();
        listHeader.add("Xem chi tiết");
        listHeader.add("Xem ghi chú");
        List<String> detail = new ArrayList<>();
        List<String> comment = new ArrayList<>();

        detail.add(suggestionDetail.getDescription());
        if (suggestionDetail.getComment() == null) { // nếu comment == null thì xoá item ghi chú
            listHeader.remove(1);
            listChild.put(listHeader.get(0), detail);

        } else {
            comment.add(suggestionDetail.getComment());
            listChild.put(listHeader.get(0), detail);
            listChild.put(listHeader.get(1), comment);
        }
        adapter = new ExpandableListViewAdapter(getContext(), listHeader, listChild);
        viewHolder.expandableListView.setAdapter(adapter);
        return view;
    }


    // constructor
    public static SuggestionDetailFragment newInstance(SuggestionDetail suggestionDetail) {
        SuggestionDetailFragment f = new SuggestionDetailFragment();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putSerializable("suggestionDetail", suggestionDetail);
        f.setArguments(args);
        return f;
    }
}