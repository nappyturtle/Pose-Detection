package com.capstone.self_training.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.self_training.R;
import com.capstone.self_training.adapter.CategoryAdapter;
import com.capstone.self_training.dto.CourseDTO;
import com.capstone.self_training.helper.TimeHelper;
import com.capstone.self_training.model.Category;
import com.capstone.self_training.model.Course;
import com.capstone.self_training.model.Enrollment;
import com.capstone.self_training.model.UpgradeCourseTransaction;
import com.capstone.self_training.service.dataservice.CategoryService;
import com.capstone.self_training.service.dataservice.CourseService;
import com.capstone.self_training.service.dataservice.EnrollmentService;
import com.capstone.self_training.util.Constants;
import com.capstone.self_training.util.PayPalConfig;
import com.google.android.gms.tasks.OnFailureListener;
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

public class CreateCourseActivity extends AppCompatActivity {

    private TextInputEditText edtCourseName;
    private Spinner spnCate;
    private EditText edtCoursePrice;
    private ImageView ivCourseThumbnail;
    private Toolbar toolbar;
    private LinearLayout lnStatus;
    private EditText edtMaxNumberTrainee, edtMaxNumberOfVideo;
    private TextView tv_fee_to_create_course;
    private static int PICK_IMAGE_REQUEST = 1;
    private int maxNumberOfTrainee = 0, maxNumberOfVideo = 0;

    private StorageReference storageReference;
    private ArrayList<Category> categoryArrayList;
    private CategoryAdapter categoryAdapter;
    private CategoryService categoryService;
    private Uri courseThumbnailUri;
    private SharedPreferences sharedPreferences;
    private boolean isChooseThumbnail = false;

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

//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);

        init();
        Intent intent = new Intent(this, PayPalService.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        startService(intent);
    }

    private void init() {
        edtCourseName = findViewById(R.id.edtCourseName);
        spnCate = findViewById(R.id.spnCate);
        lnStatus = findViewById(R.id.lnStatusCourse);
        lnStatus.setVisibility(View.GONE);
        edtCoursePrice = findViewById(R.id.edtCoursePrice);
        ivCourseThumbnail = findViewById(R.id.ivCourseThumbnail);
        edtMaxNumberOfVideo = findViewById(R.id.edtMaxNumberOfVideo);
        edtMaxNumberTrainee = findViewById(R.id.edtMaxNumberTrainee);
        tv_fee_to_create_course = findViewById(R.id.tv_fee_to_create_course);
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

        Toast.makeText(this, maxNumberOfTrainee + " " + maxNumberOfVideo, Toast.LENGTH_LONG).show();
        edtMaxNumberTrainee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Toast.makeText(CreateCourseActivity.this, "onTextChanged = " + edtMaxNumberTrainee.getText(), Toast.LENGTH_LONG).show();
                tv_fee_to_create_course.setText(calCourseFee() + "");
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
                tv_fee_to_create_course.setText(calCourseFee() + "");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private int calCourseFee() {
        int courseFee = 0;
        if (!edtMaxNumberTrainee.getText().toString().trim().equalsIgnoreCase("") && edtMaxNumberTrainee.getText() != null) {
            maxNumberOfTrainee = Integer.parseInt(edtMaxNumberTrainee.getText().toString().trim());
        }
        if (!edtMaxNumberOfVideo.getText().toString().trim().equalsIgnoreCase("") && edtMaxNumberOfVideo.getText() != null) {
            /*if (edtMaxNumberOfVideo.getText().toString().trim().equalsIgnoreCase("")) {
                maxNumberOfVideo = 0;
            } else {
                maxNumberOfVideo = Integer.parseInt(edtMaxNumberOfVideo.getText().toString().trim());
            }*/
            maxNumberOfVideo = Integer.parseInt(edtMaxNumberOfVideo.getText().toString().trim());
        }
        courseFee = maxNumberOfTrainee * maxNumberOfVideo * Constants.FREE_SUGGESTION_TURN_FOR_TRAINEE * Constants.PRICE_OF_A_SUGGESTION_TURN;
        return courseFee;
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

        if (validateNewCourse()) {
            confirmCreateCourse();
        }
    }

    public void confirmCreateCourse() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Thanh toán phí");

        alertDialog.setMessage("Xác nhận thanh toán phí tạo khóa học ?");
        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
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
                newCourse.setPrevStatus("active");
                newCourse.setThumbnail(taskSnapshot.getDownloadUrl().toString());
                Category category = (Category) spnCate.getSelectedItem();
                newCourse.setCategoryId(category.getId());
                newCourse.setPrice(Integer.parseInt(edtCoursePrice.getText().toString().trim()));
                newCourse.setEnrollmentLimit(maxNumberOfTrainee);
                newCourse.setVideoLimit(maxNumberOfVideo);

                CourseDTO courseDTO = new CourseDTO();
                courseDTO.setCourse(newCourse);
                courseDTO.setCreateCourseFee(Integer.parseInt(tv_fee_to_create_course.getText().toString().trim()));

                CourseService courseService = new CourseService();
                courseService.createCourse(sharedPreferences.getString(getString(R.string.token), ""), courseDTO);

                Toast.makeText(CreateCourseActivity.this, "Tạo khóa học thành công!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(CreateCourseActivity.this, TrainerProfileActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Upload thumbnail course: ", e.getMessage());
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

    private void getPayment() {


        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(tv_fee_to_create_course.getText().toString().trim()), "USD", "Self-Training",
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

    private boolean validateNewCourse() {
        if (edtCourseName.getText().toString().trim().equals("") || edtCourseName.getText() == null) {
            Toast.makeText(this, "Bạn chưa điền tên khóa học!", Toast.LENGTH_LONG).show();
            return false;
        } else if (edtCoursePrice.getText().toString().trim().equals("") || edtCoursePrice.getText() == null) {
            Toast.makeText(this, "Bạn chưa nhập giá tiền!", Toast.LENGTH_LONG).show();
            return false;
        } else if (Integer.parseInt(edtCoursePrice.getText().toString().trim()) < 1) {
            Toast.makeText(this, "Giá tiền nhỏ nhất" +
                    " 1.000 đ", Toast.LENGTH_SHORT).show();
            return false;
        } else if (courseThumbnailUri == null) {
            Toast.makeText(CreateCourseActivity.this, "Bạn chưa chon hình nền cho khóa học!", Toast.LENGTH_LONG).show();
            return false;
        } else if (edtMaxNumberTrainee.getText().toString().trim().equalsIgnoreCase("") || edtMaxNumberTrainee.getText() == null) {
            Toast.makeText(this, "Bạn chưa nhập số lượng học viên lớn nhất!", Toast.LENGTH_LONG).show();
            return false;
        } else if (edtMaxNumberOfVideo.getText().toString().trim().equalsIgnoreCase("") || edtMaxNumberOfVideo.getText() == null) {
            Toast.makeText(this, "Bạn chưa nhập số lượng video lớn nhất!", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
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

                        createCourse();

                        /*Enrollment enrollment = new Enrollment(dto.getCourse().getId(), accountId, dto.getCourse().getPrice(), TimeHelper.getCurrentTime());
                        EnrollmentService enrollmentService = new EnrollmentService();
                        if (enrollmentService.createEnrollment(token, enrollment)) {
                            Toast.makeText(this, "Bạn đã đăng kí thành công", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, ConfirmationActivity.class)
                                    .putExtra("PaymentDetails", paymentDetails)
                                    .putExtra("courseDTO", dto));
                        } else {
                            Toast.makeText(this, "Hệ thống đang gặp sự cố!!!!!", Toast.LENGTH_SHORT).show();
                        }*/

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
}
