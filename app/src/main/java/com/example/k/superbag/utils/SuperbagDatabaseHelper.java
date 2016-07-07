package com.example.k.superbag.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by K on 2016/6/28.
 */
public class SuperbagDatabaseHelper extends SQLiteOpenHelper {

    private static final String CREATE_DB = "create table superbag("
            +"id integer primary key autoincrement,"
            +"tag text,"
            +"content text,"
            +"isMemo blob,"
            +"importance integer,"
            +"oldTime text,"
            +"newTime text"
            +")";

    private Context cxt;
    private SQLiteDatabase db;

    public SuperbagDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        cxt = context;
    }

    //创建数据库
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_DB);
        Log.d("数据库已创建","");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertToDB(SQLiteDatabase db,String tag,String content,boolean isMemo,int importance,
                            String oldTime,String newTime){
        Log.d("正在插入数据","...");
        db.execSQL("insert into superbag(tag,content,isMemo,importance,oldTime,newTime)" +
                "values(?,?,?,?,?,?)",new String[] {tag,content,isMemo+"",importance+"",oldTime,newTime});
    }

    private void updateDB(SQLiteDatabase db, ContentValues values){
    }

    private void deleteDB(){

    }

    private void queryBD(){

    }

}
