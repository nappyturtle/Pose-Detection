package com.capstone.self_training.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.capstone.self_training.R;
import com.capstone.self_training.adapter.CategoryAdapter;
import com.capstone.self_training.helper.TimeHelper;
import com.capstone.self_training.model.Category;
import com.capstone.self_training.model.Course;
import com.capstone.self_training.service.dataservice.CategoryService;
import com.capstone.self_training.service.dataservice.CourseService;
import com.capstone.self_training.util.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CreateCourseActivity extends AppCompatActivity {

    private TextInputEditText edtCourseName;
    private Spinner spnCate;
    private EditText edtCoursePrice;
    private ImageView ivCourseThumbnail;
    private Toolbar toolbar;

    private static int PICK_IMAGE_REQUEST = 1;

    private StorageReference storageReference;
    private ArrayList<Category> categoryArrayList;
    private CategoryAdapter categoryAdapter;
    private CategoryService categoryService;
    private Uri courseThumbnailUri;
    private SharedPreferences sharedPreferences;
    private boolean isChooseThumbnail = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        init();
    }

    private void init() {
        edtCourseName = findViewById(R.id.edtCourseName);
        spnCate = findViewById(R.id.spnCate);
        edtCoursePrice = findViewById(R.id.edtCoursePrice);
        ivCourseThumbnail = findViewById(R.id.ivCourseThumbnail);
        toolbar = findViewById(R.id.toolbar_create_course);
        setupToolbar();

        storageReference = FirebaseStorage.getInstance().getReference();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        ivCourseThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        initCategoryListAdapter();
        categoryAdapter = new CategoryAdapter(this, categoryArrayList);
        spnCate.setAdapter(categoryAdapter);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            courseThumbnailUri = data.getData();
            Picasso.get().load(courseThumbnailUri).into(ivCourseThumbnail);
            isChooseThumbnail = true;
        }
    }

    private void initCategoryListAdapter() {
        categoryService = new CategoryService();
        List<Category> categories = new ArrayList<>();
        categories = categoryService.getAllcategories();
        if (categoryArrayList == null) {
            categoryArrayList = new ArrayList<>();
        }
        if (categories != null) {
            for (Category c : categories) {
                categoryArrayList.add(c);
            }
        }
    }

    public void createCours(View view) {
        if (edtCourseName.getText().toString().trim().equalsIgnoreCase("") || edtCourseName.getText().toString().trim() == null) {
            Toast.makeText(this, "Bạn chưa nhập tên khóa học.", Toast.LENGTH_LONG).show();
        }

        if (!isChooseThumbnail) {
            Toast.makeText(this, "Bạn chưa chọn hình nền cho khóa học.", Toast.LENGTH_LONG).show();
        }
        confirmCreateCourse();
    }

    public void confirmCreateCourse() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Tạo khóa học");

        alertDialog.setMessage("Bạn có muốn tạo khóa học này ?");
        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(courseThumbnailUri == null){
                    Toast.makeText(CreateCourseActivity.this, "Bạn chưa chon hình nền cho khóa học!", Toast.LENGTH_LONG).show();
                    return;
                }
                createCourse();
                Intent intent = new Intent(getApplicationContext(),TrainerProfileActivity.class);
                startActivity(intent);
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

    private void createCourse() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Đang xử lý");
        progressDialog.show();

        StorageReference srf = storageReference.child(Constants.COURSE_THUMBNAIL_FIREBASE_FOLDER + "/" + edtCourseName.getText().toString().trim());
        srf.putFile(courseThumbnailUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Course newCourse = new Course();
                newCourse.setAccountId(sharedPreferences.getInt(getString(R.string.id), 0));
                newCourse.setCreatedTime(TimeHelper.getCurrentTime());
                newCourse.setName(edtCourseName.getText().toString().trim());
                newCourse.setStatus("active");
                newCourse.setThumbnail(taskSnapshot.getDownloadUrl().toString());
                Category category = (Category) spnCate.getSelectedItem();
                newCourse.setCategoryId(category.getId());
                newCourse.setPrice(Integer.parseInt(edtCoursePrice.getText().toString().trim()));

                String token = sharedPreferences.getString(getString(R.string.token), "");

                CourseService courseService = new CourseService();
                courseService.createCourse(token, newCourse);

                Toast.makeText(CreateCourseActivity.this, "Tạo khóa học thành công!", Toast.LENGTH_LONG).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                progressDialog.setMessage("Hoàn thành " + (int) progress + "%...");
                if (progress == 100) {
                    progressDialog.dismiss();
                }
            }
        });
    }
}
