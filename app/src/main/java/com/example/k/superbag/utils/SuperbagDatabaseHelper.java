package com.example.k.superbag.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.k.superbag.bean.ItemBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    private static Context cxt;
    private static String name;
    private static SQLiteDatabase.CursorFactory factory;
    private static int version;

    private static SQLiteDatabase db;
    private static SuperbagDatabaseHelper databaseHelper;

    public SuperbagDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        cxt = context;
        this.name = name;
        this.factory = factory;
        this.version = version;
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

    public void insertToDB(String tag,String content,boolean isMemo,int importance,
                           String oldTime,String newTime){
        Log.d("正在插入数据","...");
        if (db == null){
            getDatabase();
        }
        db.execSQL("insert into superbag(tag,content,isMemo,importance,oldTime,newTime)" +
                "values(?,?,?,?,?,?)",new String[] {tag,content,isMemo+"",importance+"",oldTime,newTime});
    }

    private void updateDB(SQLiteDatabase db, ContentValues values){
    }

    private void deleteDB(){

    }


    /**
     * 返回所有数据的列表，数据过多后，可以一次返回十条等
     * 默认为按照最新时间返回
     */
    public List<ItemBean> queryBD(){
        if (db == null){
            getDatabase();
        }
        Cursor cursor = db.query("superbag",null,null,null,null,null,null);
        List<ItemBean> list = new ArrayList<>();
        Log.d("长度是",cursor.getCount()+"");
        if (cursor.moveToNext()){
            do {
                String tag = cursor.getString(cursor.getColumnIndex("tag"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String oldTime = cursor.getString(cursor.getColumnIndex("oldTime"));
                String isMemo = cursor.getString(cursor.getColumnIndex("isMemo"));
                Log.d("tag 是",tag);
                Log.d("content 是",content);
                Log.d("是否备忘",isMemo);
                Log.d("旧时间是",oldTime);
                ItemBean item = new ItemBean(tag,content,isMemo,1,oldTime,"2020");
                list.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        Collections.reverse(list);
        return list;
    }

    /**
     * 根据固定行号查询
     */
    public static ItemBean queryBD(int lineIndex){
        if (db == null){
            getDatabase();
        }
        ItemBean item = null;
        Cursor cursor = db.query("superbag",null, String.valueOf(lineIndex),null,null,null,null);
        Log.d("cursor长度",cursor.getColumnCount()+"");

        if (cursor.moveToPosition(lineIndex)){

                String tag = cursor.getString(cursor.getColumnIndex("tag"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String oldTime = cursor.getString(cursor.getColumnIndex("oldTime"));
                String isMemo = cursor.getString(cursor.getColumnIndex("isMemo"));
                Log.d("查询结果---tag 是",tag);
                Log.d("查询结果---content 是",content);
                Log.d("查询结果---是否备忘",isMemo);
                Log.d("查询结果---旧时间是",oldTime);
                item = new ItemBean(tag,content,isMemo,1,oldTime,"2020");
        }
        cursor.close();
        return item;
    }

    public static SQLiteDatabase getDatabase(){
        //----------
        databaseHelper = new SuperbagDatabaseHelper(cxt,name,factory,version);
        db = databaseHelper.getWritableDatabase();
        return db;
    }

}
