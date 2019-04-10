package com.capstone.self_training.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.self_training.R;
import com.capstone.self_training.adapter.CategoryAdapter;
import com.capstone.self_training.dto.CourseDTO;
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

public class CourseInforActivity extends AppCompatActivity {

    private TextInputEditText edtCourseName;
    private Spinner spnCate;
    private EditText edtCoursePrice;
    private ImageView ivCourseThumbnail;
    private Toolbar toolbar;
    private Button btnCreateCourse;
    private TextView currency_vnd;

    private static int PICK_IMAGE_REQUEST = 1;

    private StorageReference storageReference;
    private ArrayList<Category> categoryArrayList;
    private CategoryAdapter categoryAdapter;
    private CategoryService categoryService;
    private Uri courseThumbnailUri;
    private SharedPreferences sharedPreferences;
    private boolean isChooseThumbnail = false;
    private CourseDTO courseDTO;
    Category category;
    private String token;
    private int accountId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);
        Intent intent = getIntent();
        courseDTO = (CourseDTO) intent.getSerializableExtra("courseDTO");
        init();
        clickToSaveButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void getDataFromIntent() {

        edtCourseName.setText(courseDTO.getCourse().getName());
        Picasso.get().load(courseDTO.getCourse().getThumbnail()).into(ivCourseThumbnail);
        edtCoursePrice.setText(String.valueOf(courseDTO.getCourse().getPrice()));

    }

    private void clickToSaveButton() {
        btnCreateCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmEditCourse();
            }
        });
    }

    private void init() {
        edtCourseName = findViewById(R.id.edtCourseName);
        spnCate = findViewById(R.id.spnCate);
        edtCoursePrice = findViewById(R.id.edtCoursePrice);
        ivCourseThumbnail = findViewById(R.id.ivCourseThumbnail);
        toolbar = findViewById(R.id.toolbar_create_course);
        currency_vnd = findViewById(R.id.currency_id);
        btnCreateCourse = findViewById(R.id.btnCreateCourse);
        btnCreateCourse.setText("Lưu");
        setupToolbar();

        storageReference = FirebaseStorage.getInstance().getReference();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = sharedPreferences.getString(getString(R.string.token), "");
        accountId = sharedPreferences.getInt(getString(R.string.id), 0);
        ivCourseThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        getDataFromIntent();
        initCategoryListAdapter();


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
        categoryAdapter = new CategoryAdapter(this, categoryArrayList);
        spnCate.setAdapter(categoryAdapter);
        for (int i = 0; i < categoryAdapter.getCount(); i++) {

            Category category = (Category) spnCate.getItemAtPosition(i);
            if (category.getId() == courseDTO.getCourse().getCategoryId()) {
                spnCate.setSelection(i);
                break;
            }
        }

        spnCate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = (Category) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
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

    private void editCourse() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Đang xử lý");
        progressDialog.show();
        if (!isChooseThumbnail) {
            Course course = new Course();

            course.setId(courseDTO.getCourse().getId());
            course.setUpdatedTime(TimeHelper.getCurrentTime());
            course.setName(edtCourseName.getText().toString());
            course.setThumbnail(courseDTO.getCourse().getThumbnail());
            course.setCategoryId(category.getId());
            course.setPrice(Integer.parseInt(edtCoursePrice.getText().toString().trim()));

            CourseService courseService = new CourseService();
            if (courseService.editCourse(token, course)) {
                Toast.makeText(CourseInforActivity.this, "Thay đổi thông tin khóa học thành công", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(CourseInforActivity.this, "Thay đổi thông tin khóa học không thành công", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }


        } else {
            StorageReference srf = storageReference.child(Constants.COURSE_THUMBNAIL_FIREBASE_FOLDER + "/" + edtCourseName.getText().toString().trim());
            srf.putFile(courseThumbnailUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Course course = new Course();

                    course.setId(courseDTO.getCourse().getId());
                    course.setUpdatedTime(TimeHelper.getCurrentTime());
                    course.setName(edtCourseName.getText().toString().trim());
                    course.setCategoryId(category.getId());
                    course.setThumbnail(taskSnapshot.getDownloadUrl().toString());
                    course.setPrice(Integer.parseInt(edtCoursePrice.getText().toString().trim()));

                    CourseService courseService = new CourseService();
                    if (courseService.editCourse(token, course)) {
                        Toast.makeText(CourseInforActivity.this, "Thay đổi thông tin khóa học thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    } else {
                        Toast.makeText(CourseInforActivity.this, "Thay đổi thông tin khóa học không thành công", Toast.LENGTH_SHORT).show();
                    }

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage("Hoàn thành " + (int) progress + "%...");
                    if ((int) progress == 100) {
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }


    public void confirmEditCourse() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Thay đổi thông tin ");

        alertDialog.setMessage("Bạn có muốn thay đổi thông khóa học này ?");
        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editCourse();

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
