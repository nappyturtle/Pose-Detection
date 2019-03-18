package com.capstone.self_training.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

public class SuggestionDetailByTrainerActivity extends AppCompatActivity {

   private ImageView imgUrl;
   private ImageView imgStandardUrl;
   private ExpandableListView expandableListView;
   private EditText txtComment;
   private Button buttonSave;
    List<String> listHeader; // list chứa các item cha
    HashMap<String, List<String>> listChild;  // list chứa các item con trong mỗi item cha
    ExpandableListViewAdapter adapter;
    private SharedPreferences mPerferences;
    private String token;
    private SuggestionDetail suggestionDetailTemp;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion_detail_by_trainer);
        reflect();
        displayToolBar();
        getDataFromIntent();
        clickToSaveComment();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        suggestionDetailTemp = (SuggestionDetail) intent.getSerializableExtra("SuggestionDetail");
        Picasso.get().load(suggestionDetailTemp.getImgUrl()).fit().into(imgUrl);
        Picasso.get().load(suggestionDetailTemp.getStandardImgUrl()).fit().into(imgStandardUrl);

        listHeader.add("Xem chi tiết");
        listHeader.add("Xem ghi chú");
        List<String> detail = new ArrayList<>();
        List<String> comment = new ArrayList<>();

        detail.add(suggestionDetailTemp.getDescription());
        if (suggestionDetailTemp.getComment() == null) { // nếu comment == null thì xoá item ghi chú
            listHeader.remove(1);
            listChild.put(listHeader.get(0), detail);

        } else {
            comment.add(suggestionDetailTemp.getComment());
            listChild.put(listHeader.get(0), detail);
            listChild.put(listHeader.get(1), comment);
        }


        //clickToSaveComment(suggestionDetailTemp);
    }
    private void clickToSaveComment(){
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             confirmToSaveComment();
            }
        });
    }
    private void displayToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void saveComment(SuggestionDetail suggestionDetail) {
        SuggestionDetailService suggestionDetailService = new SuggestionDetailService();
        suggestionDetail.setComment(txtComment.getText().toString());
        if (suggestionDetailService.saveComment(token, suggestionDetail)) {
            Toast.makeText(getApplicationContext(), "Lưu bình luận thành công", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("suggestionId",suggestionDetail.getSuggestionId());
            setResult(Activity.RESULT_OK,intent);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Lưu bình luận không thành công", Toast.LENGTH_SHORT).show();
        }

    }

    private void confirmToSaveComment() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Binh luận");

        alertDialog.setMessage("Bạn có muốn lưu bình luận này không ?");
        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveComment(suggestionDetailTemp);
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

    private void reflect() {
        toolbar = (Toolbar) findViewById(R.id.suggestionDetail_byTrainer_toolbar_id);
        imgUrl = (ImageView) findViewById(R.id.suggestionDetail_imageView_id_trainee_byTrainer);
        imgStandardUrl = (ImageView) findViewById(R.id.suggestionDetail_imageView_id_trainer_byTrainer);
        expandableListView = (ExpandableListView) findViewById(R.id.expandable_listview_byTrainer);
        txtComment = (EditText) findViewById(R.id.edtComment_byTrainer);
        buttonSave = (Button) findViewById(R.id.btnSaveComment_byTrainer);

        listHeader = new ArrayList<>();
        listChild = new HashMap<>();
        adapter = new ExpandableListViewAdapter(getApplicationContext(), listHeader, listChild);
        expandableListView.setAdapter(adapter);

        mPerferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = mPerferences.getString(getString(R.string.token),"");
    }
}
