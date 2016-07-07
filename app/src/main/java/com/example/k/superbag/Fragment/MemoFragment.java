package com.example.k.superbag.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.k.superbag.R;
import com.example.k.superbag.adapter.MemoPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by K on 2016/6/26.
 */
public class MemoFragment extends Fragment implements View.OnClickListener{

    private Button memo_Todo;
    private Button memo_Done;
    private ViewPager memo_Pager;

    private List<View> viewList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_memo,container,false);
        memo_Todo = (Button)v.findViewById(R.id.memo_todo);
        memo_Done = (Button)v.findViewById(R.id.memo_done);
        memo_Pager = (ViewPager) v.findViewById(R.id.memo_viewPager);

        initViewList();
        init();
        return v;
    }

    private void initViewList(){
        viewList = new ArrayList<>();
        View viewTodo = LayoutInflater.from(getContext()).inflate(R.layout.pager_todo,null);
        View viewDone = LayoutInflater.from(getContext()).inflate(R.layout.pager_done,null);
        viewList.add(viewTodo);
        viewList.add(viewDone);

        MemoPagerAdapter pagerAdapter = new MemoPagerAdapter(viewList);
        memo_Pager.setAdapter(pagerAdapter);
    }

    private void init(){
        memo_Todo.setOnClickListener(MemoFragment.this);
        memo_Done.setOnClickListener(MemoFragment.this);
        memo_Pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                resetUI(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.memo_todo:
                if (memo_Pager.getCurrentItem() == 0){
                    return;
                } else {
                    memo_Pager.setCurrentItem(0);
                    resetUI(0);
                }
                break;
            case R.id.memo_done:
                if (memo_Pager.getCurrentItem() == 1){
                    return;
                } else {
                    memo_Pager.setCurrentItem(1);
                  resetUI(1);
                }
                break;
        }
    }

    private void resetUI(int i){
        if (i == 1){
            memo_Todo.setTextColor(getResources().getColor(R.color.gray));
            memo_Done.setTextColor(getResources().getColor(R.color.black));
        } else {
            memo_Todo.setTextColor(getResources().getColor(R.color.black));
            memo_Done.setTextColor(getResources().getColor(R.color.gray));
        }
    }
}
