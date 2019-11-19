package com.zhangqianyuan.teamwork.lostandfound.model;

import android.util.Log;

import com.google.gson.Gson;
import com.zhangqianyuan.teamwork.lostandfound.bean.MyHistoryItem;
import com.zhangqianyuan.teamwork.lostandfound.bean.SendMyHistoryBean;
import com.zhangqianyuan.teamwork.lostandfound.bean.StatusBean;
import com.zhangqianyuan.teamwork.lostandfound.bean.TheLostBean;
import com.zhangqianyuan.teamwork.lostandfound.model.interfaces.IMyUploadModel;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Callback;

/**
 * @author zhoudada
 * @version $Rev$
 * @des
 * @updateAuthor $Author$
 * @updateDes
 */
public class MyLoadModel extends  BaseModel implements IMyUploadModel {

    public MyLoadModel(){
        super();
    }


    @Override
    public void getMyLoadData(String jsessionid,int start,int end ,Callback<MyHistoryItem> callback) {
        SendMyHistoryBean bean = new SendMyHistoryBean();
        bean.setStart(start);
        bean.setEnd(end);
        api.getMyLoadData(jsessionid,new Gson().toJson(bean)).enqueue(callback);
    }


    @Override
    public void postDelete(String jsessionid,int id, Observer<StatusBean> observer) {
        Log.e("Tag","Model完好");
        api.postDelete(jsessionid,id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        Log.e("Tag","正确"+id);
        Log.e("Tag","正确"+jsessionid);
    }
}
