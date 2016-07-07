package com.example.k.superbag.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import java.io.FileNotFoundException;
import java.security.Permission;
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
    private Context context;
    private int takeFlags;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_firstpage,container,false);

        context = getContext();
        fPBackgroundIV = (ImageView)v.findViewById(R.id.top_background);
        fPHeadIconIV = (ImageView)v.findViewById(R.id.top_head_icon);
        fPSummaryTV = (TextView)v.findViewById(R.id.top_summary);
        fPListView = (ListView)v.findViewById(R.id.firstPage_lv);

        Intent intent = new Intent();
        takeFlags = intent.getFlags()
                & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        initView();
        initData();
        return v;
    }

    //根据本地存储的数据，初始化背景，头像
    private void initView(){

        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(context);
        String head = pref.getString("headIconUri","no");
        Uri headUri = Uri.parse(head);
        Log.d("头像是",head);
        Bitmap headBM = getBMFromUri(headUri);
        if (headBM != null) {
            fPHeadIconIV.setImageBitmap(headBM);
        } else {
            fPHeadIconIV.setImageResource(R.drawable.pic4);
        }

        String background = pref.getString("bgUri","no");
        Uri bgUri = Uri.parse(background);
        Log.d("背景是",background);
        Bitmap bgBM = getBMFromUri(bgUri);
        if (bgBM != null) {
            fPBackgroundIV.setImageBitmap(bgBM);
        } else {
            fPBackgroundIV.setImageResource(R.drawable.pic6);
        }

    }

    private void initData(){
        ListItem item1 = new ListItem("0pic",0);
        ListItem item2 = new ListItem("1pic",1);
        ListItem item3 = new ListItem("2pic",2);
        list.add(item1);
        list.add(item2);
        list.add(item3);

    }

    //根据Uri得到图片
    //有bug
    private Bitmap getBMFromUri(Uri uri){
        Bitmap bitmap = null;
        /*try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getContext().getContentResolver().takePersistableUriPermission(uri,takeFlags);
            }
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
        return bitmap;
    }

}
