
package com.capstone.self_training.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.capstone.self_training.R;
import com.capstone.self_training.helper.TimeHelper;
import com.capstone.self_training.model.Account;
import com.capstone.self_training.service.dataservice.AccountService;
import com.capstone.self_training.util.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfileActivity extends AppCompatActivity {

    private CircleImageView civUserImg;
    private TextInputEditText edtEmail;
    private TextInputEditText edtAddress;
    private TextInputEditText edtPhone;
    private Button btnUpdate;
    private TextInputEditText edtFullname;
    private RadioButton rdbMale, rdbFemale;
    private Toolbar toolbar;
    private int userId;
    private AccountService accountService;
    private Account currentUser;
    private Uri userImageUri = null;

    private StorageReference storageReference;
    SharedPreferences mPerferences;
    SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        init();
        displayToolBar();
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

    private void init() {
        civUserImg = (CircleImageView) findViewById(R.id.civUseImg);
        edtAddress = (TextInputEditText) findViewById(R.id.edtAddress);
        edtFullname = (TextInputEditText) findViewById(R.id.edtUpdatefullname);
        edtEmail = (TextInputEditText) findViewById(R.id.edtUpdateEmail);
        edtPhone = (TextInputEditText) findViewById(R.id.edtPhoneNumber);
        toolbar = (Toolbar) findViewById(R.id.toolbar_update_profile_id);
        btnUpdate = (Button) findViewById(R.id.btnUpdateProfile);
        userId = getIntent().getIntExtra("USER_ID", 0);
        rdbMale = (RadioButton) findViewById(R.id.rdbMale);
        rdbFemale = (RadioButton) findViewById(R.id.rdbFemale);

        accountService = new AccountService(getApplicationContext());
        currentUser = accountService.getAccount(userId);
        storageReference = FirebaseStorage.getInstance().getReference();
        mPerferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mEditor = mPerferences.edit();

        if (currentUser != null) {
            if (currentUser.getImgUrl() != null) {
                Picasso.get().load(currentUser.getImgUrl()).into(civUserImg);
            }
            if (currentUser.getEmail() != null) {
                edtEmail.setText(currentUser.getEmail());
            }
            if (currentUser.getPhone() != null) {
                edtPhone.setText(currentUser.getPhone());
            }
            if (currentUser.getAddress() != null) {
                edtAddress.setText(currentUser.getAddress());
            }
            if (currentUser.getGender() != null) {
                if (currentUser.getGender().equalsIgnoreCase(Constants.GENDER_MALE)) {
                    rdbMale.setChecked(true);
                } else {
                    rdbMale.setChecked(false);
                }
                if (currentUser.getGender().equalsIgnoreCase(Constants.GENDER_FEMALE)) {
                    rdbFemale.setChecked(true);
                } else {
                    rdbFemale.setChecked(false);
                }
            }
            if (currentUser.getFullname() != null) {
                edtFullname.setText(currentUser.getFullname());
            }
        }
    }

    private boolean validateInfo() {
        if (edtFullname.getText().toString().equals("") || edtFullname.getText().toString() == null) {
            Toast.makeText(this, "Bạn chưa điền tên đầy đủ!", Toast.LENGTH_LONG).show();
            return false;
        } else if (edtEmail.getText().toString().equals("") || edtEmail.getText().toString() == null) {
            Toast.makeText(this, "Bạn chưa điền email!", Toast.LENGTH_LONG).show();
            return false;
        } else if (edtPhone.getText().toString().equals("") || edtPhone.getText().toString() == null) {
            Toast.makeText(this, "Bạn chưa điền số điện thoại!", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private static final int READ_REQUEST_CODE = 42;

    public void openImageChooser(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                userImageUri = data.getData();
                Picasso.get().load(userImageUri).into(civUserImg);
            }
        }

    }

    public Account setAccountToEdit(String imgUserUri) {
        if (imgUserUri != null) {
            currentUser.setImgUrl(imgUserUri);
        }

        if (rdbMale.isChecked()) {
            currentUser.setGender(Constants.GENDER_MALE);
        } else if (rdbFemale.isChecked()) {
            currentUser.setGender(Constants.GENDER_FEMALE);
        }

        currentUser.setEmail(edtEmail.getText().toString().trim());
        currentUser.setPhone(edtPhone.getText().toString().trim());
        currentUser.setAddress(edtAddress.getText().toString().trim());
        currentUser.setUpdatedTime(TimeHelper.getCurrentTime());
        currentUser.setFullname(edtFullname.getText().toString());

        return currentUser;
    }

    public void updateProfileInfo(View view) {
        if(validateInfo()) {
            confirmUpdateProfile();
        }else{
            Toast.makeText(this, "Xin vui lòng điền những thông tin cần thiết", Toast.LENGTH_SHORT).show();
        }
    }

    public void clickYesButton() {
        final ProgressDialog progressDialog = new ProgressDialog(UpdateProfileActivity.this);
        progressDialog.setTitle("Đang xử lý");
        progressDialog.show();
        if (userImageUri != null) {
            StorageReference srf = storageReference.child(Constants.USER_IMAGE_FOLDER_UPLOAD_FIREBASE + "/" + currentUser.getUsername());
            srf.putFile(userImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    mEditor.putString("imgAccount", taskSnapshot.getDownloadUrl().toString());
                    mEditor.commit();

                    mEditor.putString("fullname", currentUser.getFullname());
                    mEditor.commit();
                    Log.e("imgAccountda commit = ", mPerferences.getString(getString(R.string.imgAccount), ""));
                    Account accountToEdit = setAccountToEdit(taskSnapshot.getDownloadUrl().toString());
                    accountService.updateProfile(mPerferences.getString(getString(R.string.token), ""), accountToEdit);
                    progressDialog.dismiss();
                    Toast.makeText(UpdateProfileActivity.this, "Cập nhập thành công!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("imgAccount", mPerferences.getString(getString(R.string.imgAccount), ""));
                    intent.putExtra("fullname", mPerferences.getString(getString(R.string.fullname), ""));
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                    Log.e("imgAccountaaaaaaa = ", mPerferences.getString(getString(R.string.imgAccount), ""));
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
        } else {
            Account accountToEdit = setAccountToEdit(currentUser.getImgUrl());
            accountService.updateProfile(mPerferences.getString(getString(R.string.token), ""), accountToEdit);
            progressDialog.dismiss();
            Intent intent = new Intent();
            intent.putExtra("imgAccount", mPerferences.getString(getString(R.string.imgAccount), ""));
            intent.putExtra("fullname", mPerferences.getString(getString(R.string.fullname), ""));
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    public void confirmUpdateProfile() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Cập nhập thông tin cá nhân");

        alertDialog.setMessage("Bạn có muốn cập nhập thông tin cá nhân?");
        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clickYesButton();
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
