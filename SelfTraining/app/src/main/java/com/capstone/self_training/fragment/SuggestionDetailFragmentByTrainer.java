package com.capstone.self_training.fragment;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;

import com.capstone.self_training.R;
import com.capstone.self_training.adapter.ExpandableListViewAdapter;
import com.capstone.self_training.model.SuggestionDetail;
import com.capstone.self_training.service.dataservice.SuggestionDetailService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SuggestionDetailFragmentByTrainer extends Fragment {

    View view;
    List<String> listHeader; // list chứa các item cha
    HashMap<String, List<String>> listChild;  // list chứa các item con trong mỗi item cha
    ExpandableListViewAdapter adapter;
    private SharedPreferences mPerferences;
    private String token;
    // tạo 1 class định nghĩa các component riêng để khi kéo qua , kéo lại thì nó lưu được phần giao diện trước đó
    private class ViewHolder {
        ImageView imgUrl;
        ImageView imgStandardUrl;
        ExpandableListView expandableListView;
        EditText txtComment;
        Button buttonSave;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewHolder viewHolder = null;
        savedInstanceState = getArguments();
        final SuggestionDetail suggestionDetail = (SuggestionDetail) savedInstanceState.getSerializable("suggestionDetail");
        Log.e("detail = ", suggestionDetail.getId() + " - " + suggestionDetail.getDescription() + " - " + suggestionDetail.getImgUrl()
                + " - " + suggestionDetail.getStandardImgUrl());
        mPerferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        token = mPerferences.getString(getString(R.string.token),"");
        // nếu view ban đầu = null thì sẽ render ra giao diện,
        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.item_suggestion_detail_by_trainer, container, false);

            viewHolder.imgUrl = (ImageView) view.findViewById(R.id.suggestionDetail_imageView_id_trainee_byTrainer);
            viewHolder.imgStandardUrl = (ImageView) view.findViewById(R.id.suggestionDetail_imageView_id_trainer_byTrainer);
            viewHolder.expandableListView = (ExpandableListView) view.findViewById(R.id.expandable_listview_byTrainer);
            viewHolder.txtComment = (EditText) view.findViewById(R.id.edtComment_byTrainer);
            viewHolder.buttonSave = (Button) view.findViewById(R.id.btnSaveComment_byTrainer);

            view.setTag(viewHolder); // set giá trị vào
            Log.e("ahihi ", "ahihii");
        } else {
            viewHolder = (ViewHolder) view.getTag(); // get ra vì đã có sẵn ko cần phải render lại, đỡ tốn performance
            Log.e("da vao dây", "da vao đây rồi nè");
        }

        Picasso.get().load(suggestionDetail.getImgUrl()).fit().into(viewHolder.imgUrl);
        Picasso.get().load(suggestionDetail.getStandardImgUrl()).fit().into(viewHolder.imgStandardUrl);

        listHeader = new ArrayList<>();
        listChild = new HashMap<String, List<String>>();
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

        viewHolder.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmToSaveComment(token,suggestionDetail.getId());
            }
        });
        return view;
    }

    // constructor
    public static SuggestionDetailFragmentByTrainer newInstance(SuggestionDetail suggestionDetail) {
        SuggestionDetailFragmentByTrainer f = new SuggestionDetailFragmentByTrainer();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putSerializable("suggestionDetail", suggestionDetail);
        f.setArguments(args);
        return f;
    }
    private void saveComment(String token, int suggestionDetailId){
        SuggestionDetailService suggestionDetailService = new SuggestionDetailService();
        if (suggestionDetailService.saveComment(token,suggestionDetailId)){
            Toast.makeText(getContext(), "Luu binh luan thanh cong", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getContext(), "Luu binh luan thanh cong", Toast.LENGTH_SHORT).show();
        }

    }
    private void confirmToSaveComment(final String token, final int suggestionDetailId){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Đăng video");

        alertDialog.setMessage("Bạn có muốn luu binh luan nay khong ?");
        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveComment(token,suggestionDetailId);
            }
        });
        alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
            }
        });
        alertDialog.show();
    }

}

