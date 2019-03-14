
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
    private RadioButton rdbMale, rdbFemale;

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
    }

    private void init() {
        civUserImg = (CircleImageView) findViewById(R.id.civUseImg);
        edtAddress = (TextInputEditText) findViewById(R.id.edtAddress);
        edtEmail = (TextInputEditText) findViewById(R.id.edtUpdateEmail);
        edtPhone = (TextInputEditText) findViewById(R.id.edtPhoneNumber);
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

        return currentUser;
    }

    public void updateProfileInfo(View view) {
        confirmUpdateProfile();
    }

    public void confirmUpdateProfile() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Cập nhập thông tin cá nhân");

        alertDialog.setMessage("Bạn có muốn cập nhập thông tin cá nhân?");
        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
                progressDialog.setTitle("Đang xử lý");
                progressDialog.show();
                if (userImageUri != null) {
                    StorageReference srf = storageReference.child(Constants.USER_IMAGE_FOLDER_UPLOAD_FIREBASE + "/" + currentUser.getUsername());
                    srf.putFile(userImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            mEditor.putString("imgAccount", taskSnapshot.getDownloadUrl().toString());
                            mEditor.commit();
                            Log.e("imgAccountda commit = ", mPerferences.getString(getString(R.string.imgAccount), ""));
                            accountService.updateProfile(setAccountToEdit(taskSnapshot.getDownloadUrl().toString()));
                            Toast.makeText(UpdateProfileActivity.this, "Cập nhập thành công!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.putExtra("imgAccount", mPerferences.getString(getString(R.string.imgAccount), ""));
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                            Log.e("imgAccountaaaaaaa = ", mPerferences.getString(getString(R.string.imgAccount), ""));
//                            Intent intent = new Intent(getApplicationContext(),TraineeProfileActivity.class);
//                            startActivity(intent);
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
                    progressDialog.setMessage("Hoàn thành " + "10%...");
                    accountService.updateProfile(setAccountToEdit(currentUser.getImgUrl()));
                    //double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage("Hoàn thành " + "100%");
                    progressDialog.dismiss();
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
}
