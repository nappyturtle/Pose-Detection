package com.capstone.self_training.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.capstone.self_training.R;
import com.capstone.self_training.model.Account;
import com.capstone.self_training.service.dataservice.AccountService;
import com.capstone.self_training.util.CheckConnection;

public class Fragment_Profile extends Fragment{
    private View view;
    private TextView txtUsername;
    private TextView txtEmail;
    private TextView txtPhone;
    private int id;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        savedInstanceState = getArguments();
        id = savedInstanceState.getInt("accountId");
        if (CheckConnection.haveNetworkConnection(getContext())) {
            init();
            getData();
            return view;
        } else {
            CheckConnection.showConnection(getContext(), "Kiểm tra kết nối internet");
        }
        return null;
    }

    private void init() {
        txtUsername = (TextView) view.findViewById(R.id.trainee_txtUsername_profile);
        txtEmail = (TextView) view.findViewById(R.id.trainee_txtEmail_profile);
        txtPhone = (TextView) view.findViewById(R.id.trainee_txtPhone_profile);
    }
    private void getData(){
        AccountService accountService = new AccountService(getContext());
        Account account = accountService.getAccount(id);
        txtUsername.setText(account.getUsername());
        txtEmail.setText(account.getEmail());
        txtPhone.setText(account.getPhone());
    }
    public static Fragment_Profile newInstance(int accountId){
        Fragment_Profile f = new Fragment_Profile();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("accountId", accountId);
        f.setArguments(args);
        return f;
    }
}
