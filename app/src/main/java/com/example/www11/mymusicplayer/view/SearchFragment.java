package com.example.www11.mymusicplayer.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.mymusicplayer.R;
import com.example.www11.mymusicplayer.contract.SearchContract;
import com.example.www11.mymusicplayer.presenter.SearchPresenterImpl;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SearchFragment extends Fragment implements SearchContract.OnSearchView {
    private View view;
    private SearchContract.SearchPresenter mSearchPresenter;
    private EditText mSearchContent;
    private Button mSearchBtn;
    
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search,container,false);
        
        initData();
        initEvent();
        
        return view;
    }
    
    private void initData(){
        mSearchPresenter = new SearchPresenterImpl(this);
        
        mSearchContent = view.findViewById(R.id.search_content);
        mSearchBtn = view.findViewById(R.id.search_btn);
    }
    
    private void initEvent(){
        //搜索按钮的点击事件
        mSearchBtn.setOnClickListener(v -> {
            String content = mSearchContent.getText().toString();
            mSearchPresenter.searchMusic(content);
        });
    }
}
