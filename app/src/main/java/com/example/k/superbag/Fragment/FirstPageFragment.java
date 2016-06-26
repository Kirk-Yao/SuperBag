package com.example.k.superbag.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.k.superbag.R;
import com.example.k.superbag.others.ListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by K on 2016/6/26.
 */
public class FirstPageFragment extends Fragment {

    private ImageView fPBackgroundIV,fPHeadIconIV;
    private TextView fPSummaryTV;
    private ListView fPListView;

    public static List<ListItem> list = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_firstpage,container,false);

        fPBackgroundIV = (ImageView)v.findViewById(R.id.top_background);
        fPHeadIconIV = (ImageView)v.findViewById(R.id.top_head_icon);
        fPSummaryTV = (TextView)v.findViewById(R.id.top_summary);
        fPListView = (ListView)v.findViewById(R.id.firstPage_lv);

        init();
        initData();
        return v;
    }

    //初始化数据
    private void init(){
        fPBackgroundIV.setImageResource(R.drawable.pic6);
        fPHeadIconIV.setImageResource(R.drawable.pic4);
    }

    private void initData(){
        ListItem item1 = new ListItem("0pic",0);
        ListItem item2 = new ListItem("1pic",1);
        ListItem item3 = new ListItem("2pic",2);
        list.add(item1);
        list.add(item2);
        list.add(item3);
    }

}
