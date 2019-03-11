package com.capstone.self_training.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.Nullable;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.self_training.R;
import com.capstone.self_training.adapter.SuggestionAdapter;
import com.capstone.self_training.model.Suggestion;
import com.capstone.self_training.service.dataservice.SuggestionService;
import com.capstone.self_training.util.CheckConnection;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class Fragment_Suggestion extends Fragment {

    private View view;
    private ListView listViewSuggestion;
    private ArrayList<Suggestion> suggestionList;
    private SuggestionAdapter suggestionAdapter;
    private String token;
    private int id;
    mHandler mHandler;
    private SharedPreferences mPerferences;
    private SharedPreferences.Editor mEditor;
    private View progressBar;
    private int page = 0;
    private int size = 5;
    private int checkedSuggestionList = 0;
    private TextView txtIsEmptySuggestion;
    private boolean limitedData = false;
    private boolean isLoading = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @android.support.annotation.Nullable ViewGroup container, @android.support.annotation.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_suggestion, container, false);
        savedInstanceState = getArguments();
        id = savedInstanceState.getInt("accountId");
        if (CheckConnection.haveNetworkConnection(getContext())) {
            init();
            loadData(page, size);
            loadMoreData();
            return view;
        } else {
            CheckConnection.showConnection(getContext(), "Kiểm tra kết nối internet");
        }
        return null;
    }



    private void loadMoreData() {
        listViewSuggestion.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isLoading = false;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount && !isLoading && !limitedData & totalItemCount != 0) {
                    isLoading = true;
                    ThreadData threadData = new ThreadData();
                    threadData.start();
                }
            }
        });
    }


    private void init() {
        listViewSuggestion = (ListView) view.findViewById(R.id.fragmentSuggestion_suggestionList_listview);
        suggestionList = new ArrayList<>();
        suggestionAdapter = new SuggestionAdapter(getContext(), suggestionList);
        listViewSuggestion.setAdapter(suggestionAdapter);
        mPerferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        mEditor = mPerferences.edit();
        mHandler = new mHandler();
        token = mPerferences.getString(getString(R.string.token), "");
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        progressBar = layoutInflater.inflate(R.layout.progressbar, null);

        txtIsEmptySuggestion = (TextView) view.findViewById(R.id.fragmentSuggestion_txtSuggestionIsEmpty);
    }

    private void loadData(int page, int size) {

        SuggestionService suggestionService = new SuggestionService();
        ArrayList<Suggestion> listTemp = (ArrayList<Suggestion>) suggestionService.getSuggestionList(token, page, size, id);
        if (listTemp.size() <= 0 && checkedSuggestionList == 0) {
            listViewSuggestion.setVisibility(View.INVISIBLE);
            txtIsEmptySuggestion.setVisibility(View.VISIBLE);
        } else if (listTemp.size() <= 0 && checkedSuggestionList == 1) {
            Log.e("ddasdasdasd <=0 ", "dasdasd <= 0");
            limitedData = true;
            listViewSuggestion.removeFooterView(progressBar);
            Toast.makeText(getContext(), "Đã hết dữ liệu", Toast.LENGTH_SHORT).show();
        } else {
            listViewSuggestion.removeFooterView(progressBar);
            for (Suggestion su : listTemp) {
                suggestionList.add(su);
                suggestionAdapter.notifyDataSetChanged();
            }
            Log.e("ddasdasdasd > 0 ", "dasdasd > 0");
            checkedSuggestionList = 1;
        }

    }
    public static Fragment_Suggestion newInstance(int accountId){
        Fragment_Suggestion f = new Fragment_Suggestion();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("accountId", accountId);
        f.setArguments(args);
        return f;
    }

    // handler dùng để quản lí các thread
    // thread này là luồng phụ chạy song song vs luồng chính, dùng để cập nhật số lượng dữ liệu
    public class mHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    loadData(++page, size);
                    suggestionAdapter.notifyDataSetChanged();
                    isLoading = false;
                    progressBar.setVisibility(View.GONE);
                    break;
            }
            super.handleMessage(msg);
        }
    }

    public class ThreadData extends Thread {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(0);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = mHandler.obtainMessage(1);
            mHandler.sendMessage(message);
            super.run();
        }
    }

}
