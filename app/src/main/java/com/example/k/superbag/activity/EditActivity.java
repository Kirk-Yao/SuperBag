package com.example.k.superbag.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.k.superbag.R;
import com.example.k.superbag.adapter.PopupPagerAdapter;
import com.example.k.superbag.bean.ItemBean;
import com.example.k.superbag.others.GetTime;
import com.example.k.superbag.utils.GetImageUtils;
import com.example.k.superbag.utils.SuperbagDatabaseHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by K on 2016/6/26.
 */

// 不能选标签，提醒时间等
public class EditActivity extends Activity implements View.OnClickListener,RadioGroup.OnCheckedChangeListener,ViewPager.OnPageChangeListener{

    private Button backBT,saveBT,picBT,faceBT,weatherBT,locationBT,editAlarm;
    private EditText contentET;
    private RadioGroup radioGroup;
    private ImageView headIcon;
    private TextView oldTime;
    private LinearLayout backLL,saveLL,bottomLL;
    private RadioButton editDiary,editMemo;
    private PopupWindow popupWindow,alarmPOpup;
    private ImageView popupPic1,popupPic2,popupPic3,popupPic4;
    private ViewPager viewPager;
    private Button setTimeBT,doneBT,cancelBT;

    private boolean hasSaved = false;
    private Uri imageUri;
    private boolean isMemo;
    private String previousTime,newTime = "-1";
    private boolean isEditable = true;
    private List<View> popupViewList;
    private boolean clickable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        initView();
        initPopup();
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
        editAlarm = (Button)findViewById(R.id.edit_alarm);
        //设置头像
        Bitmap head = GetImageUtils.getBMFromUri(this,"headIconUri");
        if (head != null){
            headIcon.setImageBitmap(head);
        }

        View v = LayoutInflater.from(this).inflate(R.layout.popup_pic,null);
        popupPic1 = (ImageView)v.findViewById(R.id.popup_pic1);
        popupPic2 = (ImageView)v.findViewById(R.id.popup_pic2);
        popupPic3 = (ImageView)v.findViewById(R.id.popup_pic3);
        popupPic4 = (ImageView)v.findViewById(R.id.popup_pic4);
        popupViewList = new ArrayList<>();
        popupViewList.add(v);
    }

    private void initPopup(){
        View view = LayoutInflater.from(this).inflate(R.layout.popup_window,null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        viewPager = (ViewPager) view.findViewById(R.id.popup_pager);
        PopupPagerAdapter pagerAdapter = new PopupPagerAdapter(popupViewList);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(this);
        //设置点击外部，popup消失
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

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
        popupPic1.setOnClickListener(this);
        popupPic2.setOnClickListener(this);
        popupPic3.setOnClickListener(this);
        popupPic4.setOnClickListener(this);
        editAlarm.setOnClickListener(this);
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
            if (!item.getNewTime().equals("-1")){
                editAlarm.setBackground(getResources().getDrawable(R.drawable.alarm_blue));
                clickable = true;
            }
            isEditable = false;
        }
        doneBT.setEnabled(clickable);
        cancelBT.setEnabled(clickable);
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
                Log.d("已点击","插入图片");
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                params.setMargins(0,0,0,600);
//                params.bottomMargin = 400;
//                bottomLL.setLayoutParams(params);
//                popupWindow.showAsDropDown(view);
//                popupWindow.showAtLocation(findViewById(R.id.edit_ll),Gravity.BOTTOM,0,0);
                Log.d("popup是否显示：",popupWindow.isShowing()+"");
                /*final AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
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
                builder.show();*/
                break;
            case R.id.edit_alarm:
                setAlarmPopup();
                break;
            case R.id.set_time_bt:
                setTime();
                break;
            case R.id.done_bt:
                editAlarm.setBackground(getResources().getDrawable(R.drawable.alarm_black));
                Toast.makeText(EditActivity.this,"已标记为已完成",Toast.LENGTH_SHORT).show();
                break;
            case R.id.cancel_bt:
                editAlarm.setBackground(getResources().getDrawable(R.drawable.alarm_black));
                Toast.makeText(EditActivity.this,"已取消提醒",Toast.LENGTH_SHORT).show();
                doneBT.setEnabled(false);
                break;
        }
    }

    private void setAlarmPopup(){
        View v = LayoutInflater.from(this).inflate(R.layout.popup_alarm,null);
        alarmPOpup = new PopupWindow(v, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置点击外部popup消失
        alarmPOpup.setOutsideTouchable(true);
        alarmPOpup.setBackgroundDrawable(new BitmapDrawable());

        setTimeBT = (Button)v.findViewById(R.id.set_time_bt);
        doneBT = (Button)v.findViewById(R.id.done_bt);
        cancelBT = (Button)v.findViewById(R.id.cancel_bt);
        setTimeBT.setOnClickListener(this);
        doneBT.setOnClickListener(this);
        cancelBT.setOnClickListener(this);
        alarmPOpup.showAsDropDown(findViewById(R.id.edit_alarm));
    }

    private void setTime(){
        Calendar calendar = Calendar.getInstance();
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        //要把timePicker写在前面，才会先显示datePicker,原因不知。。。
        TimePickerDialog timePicker = new TimePickerDialog(this, 0,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {

                        c.set(Calendar.HOUR_OF_DAY,i);
                        c.set(Calendar.MINUTE,i1);
                        Intent intent = new Intent(EditActivity.this,AlarmActivity.class);
                        PendingIntent pt = PendingIntent.getActivity(EditActivity.this,0,intent,0);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pt);
                        alarmPOpup.dismiss();
                        Toast.makeText(EditActivity.this,"提醒设置成功",Toast.LENGTH_SHORT).show();

                        editAlarm.setBackground(getResources().getDrawable(R.drawable.alarm_blue));
                        clickable = true;
                        cancelBT.setEnabled(clickable);
                        doneBT.setEnabled(clickable);
                    }
                },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false);
        timePicker.show();

        DatePickerDialog datePicker = new DatePickerDialog(this, 0,
                new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                c.set(i,i1,i2);
            }
        }, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePicker.show();

        newTime = c.getTimeInMillis()+"";
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
        dbHelper.insertToDB("暂无分类",content,isMemo,2,gt.getSpecificTime(),newTime);
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
