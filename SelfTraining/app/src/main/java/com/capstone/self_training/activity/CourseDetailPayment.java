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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.self_training.R;
import com.capstone.self_training.dto.CourseDTO;
import com.capstone.self_training.helper.TimeHelper;
import com.capstone.self_training.model.Enrollment;
import com.capstone.self_training.service.dataservice.EnrollmentService;
import com.capstone.self_training.util.CheckConnection;
import com.capstone.self_training.util.PayPalConfig;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class CourseDetailPayment extends AppCompatActivity {

    private ImageView imgCourse;
    private TextView txtTitle;
    private TextView txtTrainername;
    private TextView txtPrice;
    private Button btnPay;
    private CourseDTO dto;
    private Toolbar toolbar;
    private String token;
    private int accountId;
    private boolean updatedCourse;
    private SharedPreferences mPerferences;
    public static final int PAYPAL_REQUEST_CODE = 123;
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail_payment);
        if (CheckConnection.haveNetworkConnection(this)) {

            reflect();
            getDataFromIntent();
            Intent intent = new Intent(this, PayPalService.class);

            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

            startService(intent);
            saveToPayment();
            displayToolBar();
        } else {
            CheckConnection.showConnection(this, "Kiểm tra kết nối Internet của bạn!!!");
        }
    }

    private void getPayment() {


        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(dto.getCourse().getPrice())), "VNĐ", "Self-Training",
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

    private void saveToPayment() {
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmPayment();
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

    private void confirmPayment() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Thanh toán");

        alertDialog.setMessage("Bạn có muốn thanh toán khóa học này không ?");
        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                if(checkEnrollmentExisted(token,traineeId,dto.getCourse().getId())){
//                    Toast.makeText(CourseDetailPayment.this, "Bạn đã mua khóa học này rồi", Toast.LENGTH_SHORT).show();
//                }else {
                    getPayment();
                //}
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
    private boolean checkEnrollmentExisted(String token, int traineeId, int courseId){
        EnrollmentService enrollmentService = new EnrollmentService();
        return enrollmentService.checkEnrollmentExistedOrNot(token,traineeId,courseId);
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        dto = (CourseDTO) intent.getSerializableExtra("courseDTO");
        updatedCourse = intent.getBooleanExtra("updatedCourse",false);
//        if(updatedCourse){
//            saveToPayment();
//        }else{
//            Toast.makeText(this, "Bạn đã mua khóa học này rồi", Toast.LENGTH_SHORT).show();
//        }
        Picasso.get().load(dto.getCourse().getThumbnail()).fit().into(imgCourse);
        txtTitle.setText(dto.getCourse().getName());
        txtTrainername.setText(dto.getTrainerName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        String moneyDots = decimalFormat.format(dto.getCourse().getPrice()) + ",000 đồng ";
        txtPrice.setText(moneyDots);
    }

    private void reflect() {
        imgCourse = (ImageView) findViewById(R.id.img_courseDetail_payment);
        txtTitle = (TextView) findViewById(R.id.txtNameCourse_courseDetail_payment);
        txtTrainername = (TextView) findViewById(R.id.txtTrainer_courseDetail_payment);
        txtPrice = (TextView) findViewById(R.id.price_courseDetail_payment);
        btnPay = (Button) findViewById(R.id.btnPay_courseDetail_payment);
        toolbar = (Toolbar) findViewById(R.id.toolbar_courseDetail_payment);
        mPerferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = mPerferences.getString(getString(R.string.token), "");
        accountId = mPerferences.getInt(getString(R.string.id), 0);
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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

                        //Starting a new activity for the payment details and also putting the payment details with intent
//                        startActivity(new Intent(this, ConfirmationActivity.class)
//                                .putExtra("PaymentDetails", paymentDetails)
//                                .putExtra("courseDTO", dto));
                        Enrollment enrollment = new Enrollment(dto.getCourse().getId(), accountId, dto.getCourse().getPrice(), TimeHelper.getCurrentTime());
                        EnrollmentService enrollmentService = new EnrollmentService();
                        if (enrollmentService.createEnrollment(token, enrollment)) {
                            Toast.makeText(this, "Bạn đã đăng kí thành công", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(this, ConfirmationActivity.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("courseDTO", dto));
                        } else {
                            Toast.makeText(this, "Hệ thống đang gặp sự cố!!!!!", Toast.LENGTH_SHORT).show();
                        }

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
