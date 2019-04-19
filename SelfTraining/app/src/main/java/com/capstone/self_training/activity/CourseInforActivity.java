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
import android.text.Editable;
import android.text.TextWatcher;
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
import com.capstone.self_training.util.PayPalConfig;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.math.BigDecimal;
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
    private Spinner spnStatus;
    private String statusSelected;
    private EditText edtMaxNumberOfTrainee, edtMaxNumberOfVideo;
    private TextView tv_fee_create_course, tv_fee_to_create_course_title;
    private static int PICK_IMAGE_REQUEST = 1;
    int maxNumberOfVideo = 0, maxNumberOfTrainee = 0;

    private StorageReference storageReference;
    private ArrayList<Category> categoryArrayList;
    private CategoryAdapter categoryAdapter;
    private CategoryService categoryService;
    private Uri courseThumbnailUri;
    private SharedPreferences mPerferences;
    private boolean isChooseThumbnail = false;
    private CourseDTO courseDTO;
    Category category;
    private String token;
    private int accountId;


    public static final int PAYPAL_REQUEST_CODE = 123;
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);
        Intent intent = getIntent();
        courseDTO = (CourseDTO) intent.getSerializableExtra("courseDTO");
        System.out.println(courseDTO);
        init();
        openFileChooser();
        getDataFromIntent();
        initCategoryListAdapter();
        initCourseListAdapter();
        clickToSaveButton();

        Intent intentPaypal = new Intent(this, PayPalService.class);
        intentPaypal.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intentPaypal);
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    private void initCourseListAdapter() {

        List<String> dataSrc = new ArrayList<String>();

        if (courseDTO.getCourse().getStatus().equals("active")) {
            dataSrc.add("Đang hoạt động");
            dataSrc.add("Ngừng hoạt động");
        } else {
            dataSrc.add("Ngừng hoạt động");
            dataSrc.add("Đang hoạt động");
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dataSrc);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnStatus.setAdapter(arrayAdapter);
        spnStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                statusSelected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        init();
//    }

    private void getDataFromIntent() {
        edtCourseName.setText(courseDTO.getCourse().getName());
        Picasso.get().load(courseDTO.getCourse().getThumbnail()).into(ivCourseThumbnail);
        edtCoursePrice.setText(String.valueOf(courseDTO.getCourse().getPrice()));
        toolbar.setTitle(courseDTO.getCourse().getName());
        if (courseDTO.getCourse().getVideoLimit() != null) {
            maxNumberOfVideo = courseDTO.getCourse().getVideoLimit();
            edtMaxNumberOfVideo.setText(courseDTO.getCourse().getVideoLimit() + "");
        }

        if (courseDTO.getCourse().getEnrollmentLimit() != null) {
            maxNumberOfTrainee = courseDTO.getCourse().getEnrollmentLimit();
            edtMaxNumberOfTrainee.setText(courseDTO.getCourse().getEnrollmentLimit() + "");
        }


        //int coursefee = maxNumberOfTrainee * maxNumberOfVideo * Constants.FREE_SUGGESTION_TURN_FOR_TRAINEE * Constants.PRICE_OF_A_SUGGESTION_TURN;
        tv_fee_create_course.setText(0 + "");
    }


    private boolean validateCourse() {
        if (edtCourseName.getText().toString().equals("") || edtCourseName.getText().toString() == null) {
            Toast.makeText(this, "Bạn chưa điền tên khóa học!", Toast.LENGTH_LONG).show();
            return false;
        } else if (edtMaxNumberOfTrainee.getText().toString().trim().equalsIgnoreCase("") || edtMaxNumberOfTrainee.getText() == null) {
            Toast.makeText(this, "Bạn chưa nhập số lượng học viên nhiều nhất!", Toast.LENGTH_LONG).show();
            return false;
        } else if (edtMaxNumberOfVideo.getText().toString().trim().equalsIgnoreCase("") || edtMaxNumberOfVideo.getText() == null) {
            Toast.makeText(this, "Bạn chưa nhập số lượng video nhiều nhất!", Toast.LENGTH_LONG).show();
            return false;
        } else if (courseDTO.getCourse().getEnrollmentLimit() > Integer.parseInt((edtMaxNumberOfTrainee.getText().toString().trim()))) {
            Toast.makeText(this, "Số lượng học viên nhiều nhất không dược nhỏ hơn giá trị cũ", Toast.LENGTH_SHORT).show();
            return false;
        } else if (courseDTO.getCourse().getVideoLimit() > Integer.parseInt(edtMaxNumberOfVideo.getText().toString().trim())) {
            Toast.makeText(this, "Số lượng video nhiều nhất không được nhỏ hơn giá trị cũ", Toast.LENGTH_LONG).show();
            return false;
        } else if (edtCoursePrice.getText().toString().trim().equals("") || edtCoursePrice.getText().toString() == null) {
            Toast.makeText(this, "Bạn chưa nhập giá tiền!", Toast.LENGTH_LONG).show();
            return false;
        } else if (Integer.parseInt(edtCoursePrice.getText().toString().trim()) < 1) {
            Toast.makeText(this, "Giá tiền nhỏ nhất" +
                    " 1.000 đ", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean isChangeCreateCourseFee() {
        if (courseDTO.getCourse().getVideoLimit() < Integer.parseInt(edtMaxNumberOfVideo.getText().toString().trim())) {
            return true;
        }
        if (courseDTO.getCourse().getEnrollmentLimit() < Integer.parseInt(edtMaxNumberOfTrainee.getText().toString().trim())) {
            return true;
        }
        return false;
    }

    private void clickToSaveButton() {

        btnCreateCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateCourse()) {
                    if (isChangeCreateCourseFee()) {
                        //Toast.makeText(CourseInforActivity.this, "Course fee is changed", Toast.LENGTH_SHORT).show();
                        comfirmPayCourseFee();
                    } else {
                        confirmEditCourse();
                    }
                }
            }
        });
    }

    public void comfirmPayCourseFee() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Thanh toán phí");

        alertDialog.setMessage("Phí khóa học đã thay đổi. Xác nhận thanh toán phí ?");
        alertDialog.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //createCourse();
                getPayment();
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

    private void getPayment() {
        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(tv_fee_create_course.getText().toString().trim()), "USD", "Self-Training",
                PayPalPayment.PAYMENT_INTENT_SALE);

        //Creating Paypal Payment activity intent
        Intent intent = new Intent(this, PaymentActivity.class);

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        //Puting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    private void init() {
        edtCourseName = findViewById(R.id.edtCourseName);
        spnCate = findViewById(R.id.spnCate);
        spnStatus = findViewById(R.id.spnStatusCourse);
        edtCoursePrice = findViewById(R.id.edtCoursePrice);
        ivCourseThumbnail = findViewById(R.id.ivCourseThumbnail);
        toolbar = findViewById(R.id.toolbar_create_course);
        edtMaxNumberOfTrainee = findViewById(R.id.edtMaxNumberTrainee);
        edtMaxNumberOfVideo = findViewById(R.id.edtMaxNumberOfVideo);
        tv_fee_create_course = findViewById(R.id.tv_fee_to_create_course);
        tv_fee_to_create_course_title = findViewById(R.id.tv_fee_to_create_course_title);
        tv_fee_to_create_course_title.setText("Phí khóa học tăng thêm: ");

        currency_vnd = findViewById(R.id.currency_id);
        btnCreateCourse = findViewById(R.id.btnCreateCourse);
        btnCreateCourse.setText("Thay đổi");
        setupToolbar();

        storageReference = FirebaseStorage.getInstance().getReference();
        mPerferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = mPerferences.getString(getString(R.string.token), "");
        accountId = mPerferences.getInt(getString(R.string.id), 0);

        edtMaxNumberOfTrainee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Toast.makeText(CreateCourseActivity.this, "onTextChanged = " + edtMaxNumberTrainee.getText(), Toast.LENGTH_LONG).show();
                tv_fee_create_course.setText(calCourseFee() + "");
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Toast.makeText(CreateCourseActivity.this, "afterTextChanged = " + edtMaxNumberTrainee.getText(), Toast.LENGTH_LONG).show();
            }
        });

        edtMaxNumberOfVideo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_fee_create_course.setText(calCourseFee() + "");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private int calCourseFee() {
        int courseFee = 0;
        if (!edtMaxNumberOfTrainee.getText().toString().trim().equalsIgnoreCase("") && edtMaxNumberOfTrainee.getText() != null) {
            maxNumberOfTrainee = Integer.parseInt(edtMaxNumberOfTrainee.getText().toString().trim());
        }
        if (!edtMaxNumberOfVideo.getText().toString().trim().equalsIgnoreCase("") && edtMaxNumberOfVideo.getText() != null) {
            maxNumberOfVideo = Integer.parseInt(edtMaxNumberOfVideo.getText().toString().trim());
        }
        courseFee = maxNumberOfTrainee * maxNumberOfVideo * Constants.FREE_SUGGESTION_TURN_FOR_TRAINEE * Constants.PRICE_OF_A_SUGGESTION_TURN
                - courseDTO.getCourse().getVideoLimit() * courseDTO.getCourse().getEnrollmentLimit() * Constants.FREE_SUGGESTION_TURN_FOR_TRAINEE * Constants.PRICE_OF_A_SUGGESTION_TURN;
        if (courseFee < 0) {
            return 0;
        }
        return courseFee;
    }

    private void openFileChooser() {
        ivCourseThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
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

        //If the result is from paypal
        if (requestCode == PAYPAL_REQUEST_CODE) {

            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {
                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.e("paymentExample", paymentDetails);
                        editCourse();

                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
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

            if (statusSelected.equals("Đang hoạt động")) {
                statusSelected = "active";
            } else if (statusSelected.equals("Ngừng hoạt động")) {
                statusSelected = "inactive";
            }
            Log.e("statusSelected = ", statusSelected);
            course.setStatus(statusSelected);
            course.setVideoLimit(Integer.parseInt(edtMaxNumberOfVideo.getText().toString().trim()));
            course.setEnrollmentLimit(Integer.parseInt(edtMaxNumberOfTrainee.getText().toString().trim()));

            CourseDTO courseDTOToUpdate = new CourseDTO();
            courseDTOToUpdate.setCourse(course);
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

                    if (statusSelected.equals("Đang hoạt động")) {
                        statusSelected = "active";
                    } else if (statusSelected.equals("Ngừng hoạt động")) {
                        statusSelected = "inactive";
                    }
                    Log.e("statusSelected = ", statusSelected);
                    course.setStatus(statusSelected);

                    course.setVideoLimit(Integer.parseInt(edtMaxNumberOfVideo.getText().toString().trim()));
                    course.setEnrollmentLimit(Integer.parseInt(edtMaxNumberOfTrainee.getText().toString().trim()));

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
