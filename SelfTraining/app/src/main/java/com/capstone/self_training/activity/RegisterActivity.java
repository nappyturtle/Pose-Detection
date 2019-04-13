package com.capstone.self_training.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.capstone.self_training.R;
import com.capstone.self_training.helper.TimeHelper;
import com.capstone.self_training.model.Account;
import com.capstone.self_training.service.dataservice.AccountService;
import com.capstone.self_training.util.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    private static int PICK_IMAGE_REQUEST = 1;
    private final String FOLDER_NAME = "User image";

    private StorageReference srf;

    private CircleImageView civUserImg;
    private EditText edtUsername;
    private EditText edtPassword;
    private EditText edtConfirmPass;
    private EditText edtEmail;
    private Button btnRegister;
    private EditText edtFullname;
    private Uri userImgUri;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        init();
        setupToolbar();
        chooseUserImg();
        register();
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
    private void init() {
        civUserImg = (CircleImageView) findViewById(R.id.ciwUserImg);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtConfirmPass = (EditText) findViewById(R.id.edtConfirmPass);
        edtEmail = (EditText) findViewById(R.id.edtEmial);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        edtFullname = (EditText) findViewById(R.id.edtFullname);
        toolbar = (Toolbar) findViewById(R.id.register_toolbar_id);
        srf = FirebaseStorage.getInstance().getReference();

    }

    private void chooseUserImg() {
        civUserImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void register() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                String confirmPass = edtConfirmPass.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String fullname = edtFullname.getText().toString().trim();
                if (validAccount(username, password, confirmPass, email, fullname)) {
                    confirmRegistration();
                } else {
                    Toast.makeText(RegisterActivity.this, "Xin vui lòng điền các thông tin cần thiết", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void clickReigstration() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String fullname = edtFullname.getText().toString().trim();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Đang xử lý");
        progressDialog.show();
        StorageReference storageReference = srf.child(FOLDER_NAME + "/" + username);
        storageReference.putFile(userImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Account registerAccount = new Account();
                registerAccount.setUsername(username);
                registerAccount.setPassword(password);
                registerAccount.setImgUrl(taskSnapshot.getDownloadUrl().toString());
                registerAccount.setFullname(fullname);
                registerAccount.setEmail(email);
                registerAccount.setRoleId(4);
                registerAccount.setStatus("active");
                registerAccount.setPrevStatus("active");
                registerAccount.setCreatedTime(TimeHelper.getCurrentTime());

                AccountService accountService = new AccountService(getApplicationContext());
                Integer status = accountService.register(registerAccount);
                if (status == Constants.Status_Conflit) {
                    Toast.makeText(getApplicationContext(), "Username của đã tồn tại, vui lòng chon username khác",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Đăng kí thành công", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(RegisterActivity.this, MainActivity_Home.class);
                    startActivity(i);
                }

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

    public void confirmRegistration() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Đăng kí tài khoản");

        alertDialog.setMessage("Bạn có muốn tạo khoản này không ?");
        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clickReigstration();
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


    private boolean validAccount(String username, String password, String confirm, String email, String fullname) {
        if (username == null || username.contains(" ") || username.equals("")) {
            Toast.makeText(this, "Tên đăng nhập không được chứa khoảng trắng.", Toast.LENGTH_LONG).show();
            return false;
        } else if (fullname == null || fullname.equals("")) {
            Toast.makeText(this, "Xin vui lòng nhập tên đầy đủ", Toast.LENGTH_LONG).show();
            return false;
        } else if (password == null || password.contains(" ") || password.equals("")) {
            Toast.makeText(this, "Mật khẩu không được chứa khoảng trắng.", Toast.LENGTH_LONG).show();
            return false;
        } else if (!confirm.equals(password)) {
            Toast.makeText(this, "Xác nhận mật khẩu không đúng.", Toast.LENGTH_LONG).show();
            return false;
        } else if (email == null || email.equals("")) {
            Toast.makeText(this, "Email không được bỏ trống.", Toast.LENGTH_LONG).show();
            return false;
        } else if (userImgUri == null) {
            Toast.makeText(this, "Bạn chưa chọn hình đại diện.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            userImgUri = data.getData();
            Picasso.get().load(userImgUri).into(civUserImg);
        }

    }
}
