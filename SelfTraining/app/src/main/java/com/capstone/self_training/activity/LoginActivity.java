package com.capstone.self_training.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.self_training.R;
import com.capstone.self_training.model.Account;
import com.capstone.self_training.service.dataservice.AccountService;
import com.capstone.self_training.service.iService.IAccountService;
import com.capstone.self_training.service.dataservice.DataService;
import com.capstone.self_training.util.CheckConnection;
import com.capstone.self_training.util.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    private TextInputEditText username, pass;
    private Button login;
    private ImageView logoImage;
    private ProgressBar progressBar;
    private SharedPreferences mPerferences;
    private SharedPreferences.Editor mEditor;
    private AccountService accountService;
    private TextView textview_signUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        reflect();
        if (CheckConnection.haveNetworkConnection(this)) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            signIn();
            moveToRegisterActivity();
        } else {
            CheckConnection.showConnection(this, "Please check your wifi!!!!");
        }
    }

    private void signIn() {

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                IAccountService IAccountService = DataService.sign_in();
//                Log.e("USERNAME-PASSWORD", "username = " + username.getText().toString() + " - password = " + pass.getText().toString());
//                Call<Account> callBack = IAccountService.login(new Account(username.getText().toString(), pass.getText().toString()));
//                callBack.enqueue(new Callback<Account>() {
//                    @Override
//                    public void onResponse(Call<Account> call, Response<Account> response) {
//                        if (response.code() == Constants.Status_Forbidden) {
//                            Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không đúng, Vui lòng nhập lại", Toast.LENGTH_SHORT).show();
//                        } else if (response.code() == Constants.Status_NotFound) {
//                            Toast.makeText(LoginActivity.this, (CharSequence) response.body(), Toast.LENGTH_SHORT).show();
//                        } else if (response.code() == Constants.Status_Ok) {
//                            //String token = String.valueOf(response.headers().get(Constants.header_string));
//                            Account account = response.body();
//                            //Toast.makeText(LoginActivity.this, token, Toast.LENGTH_SHORT).show();
//                            Toast.makeText(LoginActivity.this, account.getId() + " - " + account.getUsername(),
//                                    Toast.LENGTH_SHORT).show();
//
//                            //save id,username,roleId,token to Shared Preperences
//                            mEditor.putInt(getString(R.string.id), account.getId());
//                            mEditor.commit();
//
//                            mEditor.putString(getString(R.string.username), account.getUsername());
//                            mEditor.commit();
//
//                            mEditor.putInt(getString(R.string.roleId), account.getRoleId());
//                            mEditor.commit();
//
//                            mEditor.putString(getString(R.string.token), account.getToken());
//                            mEditor.commit();
//
//                            if (account.getRoleId() == 3) {
//                                Intent intent = new Intent(getApplicationContext(), TrainerProfileActivity.class);
//                                startActivity(intent);
//                            } else if (account.getRoleId() == 4) {
//                                Intent intent = new Intent(getApplicationContext(), TraineeProfileActivity.class);
//                                startActivity(intent);
//                            }
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<Account> call, Throwable t) {
//                        Log.e("Login failed : ", t.getMessage());
//                    }
//                });

                accountService = new AccountService(getApplicationContext());
                Account account = (Account) accountService.login(new Account(username.getText().toString(), pass.getText().toString()));
                if(account != null){
                    if(account.getId()==0){
                        Toast.makeText(LoginActivity.this, account.getMessage(), Toast.LENGTH_SHORT).show();
                    }else if(account.getRoleId() == 2) {
                        Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không đúng, Vui lòng nhập lại", Toast.LENGTH_SHORT).show();
                    }else{
                        Log.e("ACCCCC ","id = "+account.getId()+" - username = "+account.getUsername()
                                +" - roleId = "+account.getRoleId()+ " - token = "+account.getToken()
                        +" - message = "+account.getMessage());
                        Toast.makeText(LoginActivity.this, account.getMessage(), Toast.LENGTH_SHORT).show();

                        // lưu vào trong share Preperences
                        mEditor.putInt(getString(R.string.id), account.getId());
                        mEditor.commit();

                        mEditor.putString(getString(R.string.username), account.getUsername());
                        mEditor.commit();

                        mEditor.putInt(getString(R.string.roleId), account.getRoleId());
                        mEditor.commit();

                        mEditor.putString(getString(R.string.token), account.getToken());
                        mEditor.commit();

                        mEditor.putBoolean(getString(R.string.accountExisted),true);
                        mEditor.commit();

                        mEditor.putString(getString(R.string.imgAccount),account.getImgUrl());
                        mEditor.commit();

                        Intent intent = new Intent();
                        intent.putExtra("Account",account);
                        setResult(Activity.RESULT_OK,intent);
                        finish();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Tài khoản hoặc mật khẩu không đúng, Vui lòng nhập lại", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void reflect() {
        username = (TextInputEditText) findViewById(R.id.txtUsername);
        pass = (TextInputEditText) findViewById(R.id.txtPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBarLogin);
        logoImage = (ImageView) findViewById(R.id.logo);
        login = (Button) findViewById(R.id.btnLogin);
        textview_signUp = (TextView)findViewById(R.id.textview_signUp);
        mPerferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPerferences.edit();
    }

    private void moveToRegisterActivity(){
        textview_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {

        // đặt resultCode là Activity.RESULT_CANCELED thể hiện
        // đã thất bại khi người dùng click vào nút Back.
        // Khi này sẽ không trả về data.
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }
}
