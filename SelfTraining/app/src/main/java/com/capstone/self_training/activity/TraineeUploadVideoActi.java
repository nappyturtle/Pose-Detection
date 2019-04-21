package com.capstone.self_training.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.capstone.self_training.R;
import com.capstone.self_training.helper.TimeHelper;
import com.capstone.self_training.model.Suggestion;
import com.capstone.self_training.model.SuggestionTurnTransaction;
import com.capstone.self_training.model.Video;
import com.capstone.self_training.service.dataservice.SuggestionService;
import com.capstone.self_training.service.dataservice.SuggestionTurnTransactionService;
import com.capstone.self_training.util.Constants;
import com.capstone.self_training.util.MP4Demuxer;
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

import org.jcodec.common.DemuxerTrack;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.io.SeekableByteChannel;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

public class TraineeUploadVideoActi extends AppCompatActivity {
    private static final int SELECT_VIDEO = 3;
    private static final int OPEN_CAMERA = 4;
    private static final int REQUEST_CODE_LOGIN = 0x9345;
    private Button btnChooseFile;
    private Button btnUploadVideo;
    private TextView txtVideoName;
    private VideoView videoView;
    private EditText edt_fee_extra_suggestion_turn;
    private TextView tv_fee_extra_suggestion_turn;
    private Button btnOpenCamera;
    private Toolbar toolbar;
    private StorageReference storageReference;

    private Uri selectedVideoUri;
    private String selectedPath;
    private String filename;
    private Video playingVideo;

    private SharedPreferences mPerferences;
    private SuggestionService suggestionService;
    private SuggestionTurnTransactionService suggestionTurnTransactionService;

    public static final int PAYPAL_REQUEST_CODE = 123;
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        playingVideo = (Video) getIntent().getSerializableExtra("PLAYINGVIDEO");

        init();
        displayToolBar();
        clickToOpenCamera();
        requestRead();

        uploadVideo();

        Intent intentPaypal = new Intent(this, PayPalService.class);
        intentPaypal.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intentPaypal);

    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_VIDEO && resultCode == RESULT_OK && data != null && data.getData() != null) {
            System.out.println("Select video  ");
            selectedVideoUri = data.getData();
            selectedPath = getPath(selectedVideoUri);
            filename = selectedPath.substring(selectedPath.lastIndexOf("/") + 1);
            txtVideoName.setText(filename);
            Uri uri = Uri.parse(selectedPath);
            videoView.setVideoURI(uri);
            MediaController mediaController = new MediaController(this);
            videoView.setMediaController(mediaController);
            mediaController.setAnchorView(videoView);
        }
        if (requestCode == REQUEST_CODE_LOGIN && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            System.out.println("Trainee upload");

        }
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

                        int accountId = mPerferences.getInt(getString(R.string.id), 0);
                        int videoId = playingVideo.getId();
                        int extraTurn = Integer.parseInt(edt_fee_extra_suggestion_turn.getText().toString().trim());
                        int price = Integer.parseInt(tv_fee_extra_suggestion_turn.getText().toString().trim());
                        String token = mPerferences.getString(getString(R.string.token), "");

                        SuggestionTurnTransaction newTransaction = new SuggestionTurnTransaction();
                        newTransaction.setAccountId(accountId);
                        newTransaction.setCreatedTime(TimeHelper.getCurrentTime());
                        newTransaction.setVideoId(playingVideo.getId());
                        newTransaction.setPrice(Integer.parseInt(tv_fee_extra_suggestion_turn.getText().toString().trim()));
                        newTransaction.setSuggestionTurn(Integer.parseInt(edt_fee_extra_suggestion_turn.getText().toString().trim()));

                        boolean isCreated = suggestionTurnTransactionService.buySuggestionTurn(token, newTransaction);
                        if (isCreated) {
                            Toast.makeText(this, "Thanh toán phí thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Thanh toán phí thất bại", Toast.LENGTH_SHORT).show();
                        }
                        confirmUploadingVideo();

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

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();
        cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null,
                MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        cursor.close();
        return path;
    }

    public void init() {
        btnChooseFile = (Button) findViewById(R.id.btnChooseFile);
        btnUploadVideo = (Button) findViewById(R.id.btnUploadVideo);
        txtVideoName = (TextView) findViewById(R.id.txtVideoName);
        videoView = (VideoView) findViewById(R.id.vwVideo);
        btnOpenCamera = (Button) findViewById(R.id.btnUploadVideoOpenCamera);
        storageReference = FirebaseStorage.getInstance().getReference();
        mPerferences = PreferenceManager.getDefaultSharedPreferences(this);
        suggestionService = new SuggestionService();
        suggestionTurnTransactionService = new SuggestionTurnTransactionService();

        toolbar = (Toolbar) findViewById(R.id.toolbar_uploadVideo_id);
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

    private void clickToOpenCamera() {
        btnOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OpenCameraActi.class);
                startActivity(intent);
            }
        });
    }

    public void openChooseVideoView() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a Video "), SELECT_VIDEO);
    }

    private void chooseFileUpload() {

        btnChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChooseVideoView();
            }
        });

    }

    private void uploadVideo() {
        //Video uploadedVideo = new Video();
        btnUploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = true;
                if (selectedVideoUri == null || selectedVideoUri.equals("")) {
                    checked = false;
                    Toast.makeText(TraineeUploadVideoActi.this, "Xin vui lòng chọn video trước khi đăng", Toast.LENGTH_SHORT).show();
                }
                if (checked) {
                    //Toast.makeText(TraineeUploadVideoActi.this, "da vao upload video", Toast.LENGTH_SHORT).show();
                    if (checkVideoLength()) {
                        confirmUploadingVideo();
                    }

                }
            }
        });
    }

    private boolean checkVideoLength() {
        try {
            double frameRate = 0;
            SeekableByteChannel bc = NIOUtils.readableFileChannel(String.valueOf(selectedPath));
            MP4Demuxer dm = new MP4Demuxer(bc);
            DemuxerTrack vt = dm.getVideoTrack();
            frameRate = vt.getMeta().getTotalDuration();
            //Toast.makeText(this, String.valueOf(frameRate), Toast.LENGTH_SHORT).show();
            if (frameRate > 60) {
                Toast.makeText(this, "Video của bạn quá 60 giây!!!", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                //Toast.makeText(this, "Okkkkk ", Toast.LENGTH_SHORT).show();
                return true;
            }

        } catch (Exception e) {

        }
        return false;
    }

    private String createFolderName(String username) {
        return username + "-" + System.currentTimeMillis();
    }

    private boolean isAvailableTurn() {
        String token = mPerferences.getString(getString(R.string.token), "");
        int currentAccountId = mPerferences.getInt(getString(R.string.id), 0);
        boolean result = suggestionService.isAvailableTurn(token, currentAccountId, playingVideo.getId());
        return result;
    }

    private void uploadFileToFirebase() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        //final Video uploadedVideo = new Video();
        progressDialog.setTitle("Đang xử lý");
        progressDialog.show();
        Log.e("TextView = ", txtVideoName.getText().toString());

        String username = mPerferences.getString(getString(R.string.username), "");
        final String folderName = createFolderName(username);

        StorageReference stR = storageReference.child(folderName + "/" + txtVideoName.getText().toString());
        stR.putFile(selectedVideoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // set các value cho suggestion, upload video lên storage trước
                int accountId = mPerferences.getInt(getString(R.string.id), 0);
                final String token = mPerferences.getString(getString(R.string.token), "");

                final Suggestion suggestion = new Suggestion();
                suggestion.setAccountId(accountId);
                suggestion.setVideoId(playingVideo.getId());
                suggestion.setStatus("processing");
                suggestion.setFoldernameTrainee(folderName);
                suggestion.setCreatedTime(TimeHelper.getCurrentTime());
                suggestion.setUrlVideoTrainee(taskSnapshot.getDownloadUrl().toString());

                StorageReference thumnailStr = storageReference.child(folderName + "/" + txtVideoName.getText()
                        .toString() + "-suggestion_thumbnail");
                Bitmap bitmap_thumbnail = ThumbnailUtils.createVideoThumbnail(getPath(selectedVideoUri),
                        MediaStore.Images.Thumbnails.MINI_KIND);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap_thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();
                thumnailStr.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // upload hình lên storage sau, tải hình bitmap làm thumbnail cho suggestion
                        suggestion.setThumnailUrl(taskSnapshot.getDownloadUrl().toString());

                        SuggestionService suggestionService = new SuggestionService();
                        suggestionService.createSuggestion(token, suggestion);
                        //progressDialog.dismiss();
                        Toast.makeText(TraineeUploadVideoActi.this, "Video đã được đăng", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), SuggestionListActi.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Upload thumbnail suggestion: ", e.getMessage());
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
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(TraineeUploadVideoActi.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                progressDialog.setMessage("Hoàn thành " + (int) progress + "%...");
//                if ((int) progress == 100) {
//                    progressDialog.dismiss();
//                }
            }
        });
    }

    public void confirmUploadingVideo() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Đăng video");

        alertDialog.setMessage("Bạn có muốn đăng video này không ?");
        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int id = mPerferences.getInt(getString(R.string.id), 0);
                boolean accountExisted = mPerferences.getBoolean(getString(R.string.accountExisted), false);
                if (id == 0 && !accountExisted) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_LOGIN);
                }
                if (id != 0 && accountExisted) {
                    if (isAvailableTurn()) {
                        uploadFileToFirebase();
                    } else {
                        //Toast.makeText(TraineeUploadVideoActi.this, "Da het luot", Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(TraineeUploadVideoActi.this);
                        //builder.setTitle("Thông báo");
                        View view = getLayoutInflater().inflate(R.layout.dialog_buy_suggestion_turn, null);
                        edt_fee_extra_suggestion_turn = view.findViewById(R.id.edt_fee_extra_suggestion_turn);
                        tv_fee_extra_suggestion_turn = view.findViewById(R.id.tv_fee_extra_suggestion_turn);

                        edt_fee_extra_suggestion_turn.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                if (!edt_fee_extra_suggestion_turn.getText().toString().isEmpty()) {
                                    int fee = Integer.parseInt(edt_fee_extra_suggestion_turn.getText().toString().trim()) * Constants.PRICE_OF_A_SUGGESTION_TURN;
                                    tv_fee_extra_suggestion_turn.setText(fee + "");
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });

                        builder.setView(view);
                        builder.setPositiveButton("Thanh toán phí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (edt_fee_extra_suggestion_turn.getText().toString().isEmpty()) {
                                    Toast.makeText(TraineeUploadVideoActi.this, "Bạn chưa nhập số lượt!", Toast.LENGTH_LONG).show();
                                    return;
                                }

                                getPayment();
                            }
                        });
                        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        dialog.dismiss();
                                    }
                                });
                            }
                        });
                        AlertDialog buySuggestionTurnDialog = builder.create();
                        buySuggestionTurnDialog.setTitle("Thông báo");
                        buySuggestionTurnDialog.show();
                    }
                }
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
        PayPalPayment payment = new PayPalPayment(new BigDecimal(tv_fee_extra_suggestion_turn.getText().toString().trim()), "USD", "Self-Training",
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

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    /**
     * requestPermissions and do something
     */
    public void requestRead() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            chooseFileUpload();
        }
    }

    /**
     * onRequestPermissionsResult
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                chooseFileUpload();
            } else {
                // Permission Denied
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
