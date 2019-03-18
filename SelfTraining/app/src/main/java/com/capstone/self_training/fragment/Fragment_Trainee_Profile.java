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
import com.capstone.self_training.dto.TrainerInfo;
import com.capstone.self_training.model.Account;
import com.capstone.self_training.service.dataservice.AccountService;
import com.capstone.self_training.util.CheckConnection;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_Trainee_Profile extends Fragment{
    private View view;
    private CircleImageView cvTraineeImg;
    private TextView tvTraineeName;
    private TextView tvPhoneNumber, tvEmail;

    private int id;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_trainee_profile, container, false);
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
        cvTraineeImg = view.findViewById(R.id.intro_trainee_channel_userImg);
        tvTraineeName = view.findViewById(R.id.intro_trianee_channel_username);
        tvPhoneNumber = view.findViewById(R.id.intro_trainee_phone);
        tvEmail = view.findViewById(R.id.intro_trainee_email);
    }
    private void getData(){
        AccountService accountService = new AccountService(getContext());
        Account account = accountService.getAccount(id);
        Picasso.get().load(account.getImgUrl()).fit().into(cvTraineeImg);
        tvTraineeName.setText(account.getUsername());
        tvEmail.setText(account.getEmail());
        tvPhoneNumber.setText(account.getPhone());
    }
    public static Fragment_Trainee_Profile newInstance(int accountId){
        Fragment_Trainee_Profile f = new Fragment_Trainee_Profile();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("accountId", accountId);
        f.setArguments(args);
        return f;
    }
}
