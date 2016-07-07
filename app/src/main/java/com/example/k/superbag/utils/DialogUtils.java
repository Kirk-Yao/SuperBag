package com.example.k.superbag.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;

/**
 * Created by K on 2016/6/27.
 */
public class DialogUtils {

    public static AlertDialog.Builder builder;
    public static Dialog dialog;

    public static void showDialog(Context context,String content){
        builder = new AlertDialog.Builder(context);
        builder.setMessage(content);
        builder.setPositiveButton("确定",null);
        builder.setNegativeButton("取消",null);
        dialog = builder.show();
    }

    public static void cancelDialog(){
        dialog.dismiss();
    }


}
