package com.capstone.self_training.activity;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.capstone.self_training.R;
import com.capstone.self_training.adapter.CategoryAdapter;
import com.capstone.self_training.model.Category;
import com.capstone.self_training.model.Video;
import com.capstone.self_training.service.dataservice.CategoryService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EditVideoActivity extends AppCompatActivity {
    private Video video;
    private TextInputEditText edtEditVideoTitle;
    private ImageView ivVideoThumbnail;
    private Spinner spnCategory, spnStatus;
    private CategoryService categoryService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_video);

        video = (Video) getIntent().getSerializableExtra("EDIT_VIDEO");
        edtEditVideoTitle = findViewById(R.id.edtEditVideoTitle);
        edtEditVideoTitle.setText(video.getTitle());

        ivVideoThumbnail = findViewById(R.id.ivEditVideoThumbnail);
        Picasso.get().load(video.getThumnailUrl()).fit().into(ivVideoThumbnail);

        //status
    }

    /*private int getIndexCate(int cateId) {
        for (Category cate : cateList) {
            if (cate.getId() == cateId) {
                return cateList.indexOf(cate);
            }
        }
        return 0;
    }*/

    /*private void initCateList() {
        categoryService = new CategoryService();
        List<Category> arrCate = categoryService.getAllcategories();
        if (cateList == null) {
            cateList = new ArrayList<Category>();
        }
        if (arrCate != null) {
            for (Category c : arrCate) {
                cateList.add(c);
            }
        }
    }*/

}
