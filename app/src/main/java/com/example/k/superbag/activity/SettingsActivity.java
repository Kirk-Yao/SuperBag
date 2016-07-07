package com.example.k.superbag.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.k.superbag.R;

import java.io.File;
import java.io.IOException;


/**
 * Created by K on 2016/6/28.
 */
public class SettingsActivity extends Activity implements View.OnClickListener{

    private LinearLayout changeHeadLL,changeBgLL,uploadLL,downloadLL,aboutLL;
    private TextView changeHeadStatus,changeBgStatus;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initView();
        initListener();
    }

    private void initView(){
        changeHeadLL = (LinearLayout)findViewById(R.id.change_head_ll);
        changeBgLL = (LinearLayout)findViewById(R.id.change_bg_ll);
        uploadLL = (LinearLayout)findViewById(R.id.upload_ll);
        downloadLL = (LinearLayout)findViewById(R.id.download_ll);
        changeHeadStatus = (TextView)findViewById(R.id.change_head_status);
        changeBgStatus = (TextView)findViewById(R.id.change_bg_status);
        aboutLL = (LinearLayout)findViewById(R.id.about_ll);
    }

    private void initListener(){
        changeHeadLL.setOnClickListener(this);
        changeBgLL.setOnClickListener(this);
        uploadLL.setOnClickListener(this);
        downloadLL.setOnClickListener(this);
        aboutLL.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.change_head_ll:
                selectFromAlbum(1);
                break;
            case R.id.change_bg_ll:
                selectFromAlbum(2);
                break;
            case R.id.about_ll:
                Toast.makeText(SettingsActivity.this,"两个大帅比的作品",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //从相册选取,参数用于确定头像还是背景
    //头像为1，背景为2
    private void selectFromAlbum(int i){
        createUri(i);
        /*Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image*//*");
        intent.putExtra("crop",true);
        intent.putExtra("scale",true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,i);*/

        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
            intent.setType("image");
            intent.addCategory("android.intent.category.OPENABLE");
            startActivityForResult(intent,i);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            try {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image");
                startActivityForResult(intent,i);
            } catch (Exception e2) {
                e.printStackTrace();
            }
        }

       /* if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("*image*//*");
            startActivityForResult(intent, i);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*image*//*");
            startActivityForResult(intent, i);
        }
*/
    }

    //创建File对象，用于存储选择的照片
    private void createUri(int i){
        File outputImage;
        if (i == 1) {
            outputImage = new File(Environment.getExternalStorageDirectory(), "sbHeadTemp.jpg");
        } else {
            outputImage = new File(Environment.getExternalStorageDirectory(),"sbBGTemp.jpg");
        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode != RESULT_OK) {
                    Log.d("不等于","");
                    return;
                }  else {  //头像
                    //测试
                    imageUri = data.getData();
                    saveData(1);
                    changeHeadStatus.setText("自定义");
                }
                break;
            case 2:
                imageUri = data.getData();
                saveData(2);
                changeBgStatus.setText("自定义");
                break;
        }
    }

    //存储，参数为1代表头像，2代表背景
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void saveData(int i){
        Log.d("uri是  ",imageUri.toString());

        SharedPreferences.Editor editor =
                PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this).edit();
        if (i == 1) {
            editor.putString("headIconUri", imageUri.toString());
            Toast.makeText(SettingsActivity.this,"头像更换成功！",Toast.LENGTH_SHORT).show();
        } else {
            editor.putString("bgUri",imageUri.toString());
            Toast.makeText(SettingsActivity.this,"背景更换成功！",Toast.LENGTH_SHORT).show();
        }
        editor.commit();
    }

}
