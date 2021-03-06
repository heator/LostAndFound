package com.yf107.teamwork.lostandfound.model.interfaces;

import com.yf107.teamwork.lostandfound.bean.ImageBean;
import com.yf107.teamwork.lostandfound.bean.MyHistoryItem;

import io.reactivex.Observer;
import retrofit2.Callback;

public interface IMyHistoryModel {
    void getMyHistoryData(String jsessionid,int start,int end,Callback<MyHistoryItem> callback);
}
