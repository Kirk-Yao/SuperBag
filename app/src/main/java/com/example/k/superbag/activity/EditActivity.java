package com.example.k.superbag.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.k.superbag.R;
import com.example.k.superbag.bean.ItemBean;
import com.example.k.superbag.others.GetTime;
import com.example.k.superbag.utils.GetImageUtils;
import com.example.k.superbag.utils.SuperbagDatabaseHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by K on 2016/6/26.
 */

// 不能选标签，提醒时间等
public class EditActivity extends Activity implements View.OnClickListener,RadioGroup.OnCheckedChangeListener{

    private Button backBT,saveBT,picBT,faceBT,weatherBT,locationBT;
    private EditText contentET;
    private RadioGroup radioGroup;
    private ImageView headIcon;
    private TextView oldTime;
    private LinearLayout backLL,saveLL,bottomLL;
    private RadioButton editDiary,editMemo;

    private boolean hasSaved = false;
    private Uri imageUri;
    private boolean isMemo;
    private boolean isEditable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        initView();
        initListener();
        initData();
    }

    private void initView(){
        backBT = (Button)findViewById(R.id.edit_back);
        saveBT = (Button)findViewById(R.id.edit_save);
        picBT = (Button)findViewById(R.id.edit_pic_bt);
        faceBT = (Button)findViewById(R.id.edit_face_bt);
        weatherBT = (Button)findViewById(R.id.edit_weather_bt);
        locationBT = (Button)findViewById(R.id.edit_location_bt);
        contentET = (EditText)findViewById(R.id.edit_et);
        radioGroup = (RadioGroup)findViewById(R.id.edit_rg);
        editMemo = (RadioButton)findViewById(R.id.edit_memo);
        editDiary = (RadioButton)findViewById(R.id.edit_diary);
        headIcon = (ImageView)findViewById(R.id.edit_head_icon);
        oldTime = (TextView) findViewById(R.id.edit_time);
        backLL = (LinearLayout)findViewById(R.id.edit_back_ll);
        saveLL = (LinearLayout)findViewById(R.id.edit_save_ll);
        bottomLL = (LinearLayout)findViewById(R.id.edit_bottom_LL);

        Bitmap head = GetImageUtils.getBMFromUri(this,"headIconUri");
        if (head != null){
            headIcon.setImageBitmap(head);
        }
    }

    private void initListener(){
        backBT.setOnClickListener(this);
        saveBT.setOnClickListener(this);
        picBT.setOnClickListener(this);
        faceBT.setOnClickListener(this);
        weatherBT.setOnClickListener(this);
        locationBT.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(this);
        backLL.setOnClickListener(this);
        saveLL.setOnClickListener(this);
    }

    private void initData(){
        GetTime gt = new GetTime();
        /*Log.d("年份",gt.getYear()+"");
        Log.d("月",gt.getMonth()+"");
        Log.d("日",gt.getDay()+"");
        Log.d("小时",gt.getHour()+"");
        Log.d("分钟",gt.getMin()+"");*/
        oldTime.setText(gt.getYear()+"-"+gt.getMonth()+"-"+gt.getDay());

        //如果是从ListView点击进入活动，则初始化数据
        Intent intent = getIntent();
        int lineNum = intent.getIntExtra("lineIndex",-1);
        if (lineNum != -1){
            ItemBean item = SuperbagDatabaseHelper.queryBD(lineNum);
            bottomLL.setVisibility(View.GONE);
            saveBT.setBackground(getResources().getDrawable(R.drawable.edit));
            contentET.setClickable(false);
            contentET.setText(item.getContent());
            if(item.getIsMemo().equals("true")){
                editMemo.setChecked(true);
                editDiary.setChecked(false);
            }
            isEditable = false;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.edit_back_ll:
            case R.id.edit_back:
                if (hasSaved){
                    finish();
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                    builder.setMessage("尚未保存，确认退出？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    builder.create().dismiss();
                                }
                            });
                    builder.show();
                }
                break;
            case R.id.edit_save_ll:
            case R.id.edit_save:
                if (!isEditable){
                    saveBT.setBackground(getResources().getDrawable(R.drawable.save));
                    bottomLL.setVisibility(View.VISIBLE);
                    contentET.setClickable(true);
                    isEditable = true;
                } else {
                    String content = contentET.getText().toString();
                    Log.d("比较结果，=", (content.trim().equals("")) + "");
                    if (content.trim().equals("")) {
                        Toast.makeText(EditActivity.this, "内容不能为空哦", Toast.LENGTH_SHORT).show();
                    } else {
                        //执行保存操作
                        saveData();
                        finish();
                    }
                }

                break;
            case R.id.edit_pic_bt:
                final AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                builder.setMessage("选择图片来源")
                        .setNegativeButton("拍照", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                takePhoto();
                            }
                        })
                        .setPositiveButton("图库", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                selectFromAlbum();
                            }
                        });
                builder.show();
                break;

        }
    }

    //用于确定单选钮的选中情况
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i){
            case R.id.edit_memo:
                isMemo = true;
                break;
            case R.id.edit_diary:
                isMemo = false;
                break;
        }
    }

    //保存数据
    private void saveData(){
        String content = contentET.getText().toString().trim();
        SuperbagDatabaseHelper dbHelper = new SuperbagDatabaseHelper(this,"superbag.db",null,1);
        GetTime gt = new GetTime();
        dbHelper.insertToDB("暂无分类",content,isMemo,2,gt.getSpecificTime(),"");
        Log.d("已执行保存操作","");
    }

    //拍照
    private void takePhoto(){
        createUri();
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        //设置图片的输出地址
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        switch (requestCode){
            case 1:
                Log.d("before handle","");
                Toast.makeText(EditActivity.this,"处理中",Toast.LENGTH_SHORT).show();
                if(resultCode != RESULT_OK){
                    try {
                        //处理图片
                        Log.d("处理图片中","");
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        //
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                Log.d("default ","");
                break;
        }
    }


    //从相册选取
    private void selectFromAlbum(){
        createUri();
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/**");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,1);
    }

    //创建File对象，用于存储选择的照片
    private void createUri(){
        File outputImage = new File(Environment.getExternalStorageDirectory(),"SuperBagTemp.jpg");
        try{
            if (outputImage.exists()){
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageUri = Uri.fromFile(outputImage);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d("退出执行","");
    }
}
