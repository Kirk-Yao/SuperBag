package com.example.k.superbag.bean;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import java.util.List;

/**
 * Created by K on 2016/6/26.
 */
public class ItemBean {

    private String tag;
    private String content;
    private String oldTime;
    private String newTime;
    private List<Drawable> drawableList;
    private String dayTime;
    private String isMemo;
    private int importance;

    private List<Uri> picList;

    public ItemBean(String tag, String content, String isMemo,
                    int importance, String oldTime, String newTime, List<Uri> picList){
        this.tag = tag;
        this.content = content;
        this.isMemo = isMemo;
        this.importance = importance;
        this.oldTime = oldTime;
        this.newTime = newTime;
        this.picList = picList;
    }

    public ItemBean(){}

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOldTime() {
        return oldTime;
    }

    public void setOldTime(String oldTime) {
        this.oldTime = oldTime;
    }

    public String getNewTime() {
        return newTime;
    }

    public void setNewTime(String newTime) {
        this.newTime = newTime;
    }

    public List<Drawable> getDrawableList() {
        return drawableList;
    }

    public void setDrawableList(List<Drawable> drawableList) {
        this.drawableList = drawableList;
    }

    public String getDayTime() {
        return dayTime;
    }

    public void setDayTime(String dayTime) {
        this.dayTime = dayTime;
    }

    public String getIsMemo() {
        return isMemo;
    }

    public void setIsMemo(String isMemo) {
        this.isMemo = isMemo;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public int getPicNum() {
        return picList.size();
    }

    public List<Uri> getPicList(){
        return picList;
    }
}
