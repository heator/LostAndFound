package com.yf107.teamwork.lostandfound.model;

import android.util.Log;

import com.google.gson.Gson;
import com.yf107.teamwork.lostandfound.bean.ImageBean;
import com.yf107.teamwork.lostandfound.model.interfaces.IThingDetailModel;
import com.yf107.teamwork.lostandfound.bean.CommentFeedBack;
import com.yf107.teamwork.lostandfound.bean.StatusBean;
import com.yf107.teamwork.lostandfound.bean.ThingDetailBean;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Callback;

public class ThingDetailModel extends BaseModel implements IThingDetailModel {

    public ThingDetailModel(){
        super();
    }

    @Override
    public void sendInternetData(String session, Integer id,int lostid,String time,String content, Callback<StatusBean> callback) {
        ThingDetailBean  bean = new ThingDetailBean();
        ThingDetailBean.Comment con = new ThingDetailBean.Comment();
        con.setContent(content);
        con.setId(id);
        con.setTime(time);
        bean.setLostid(lostid);
        bean.setComment(con);
        api.uploadComment(session,new Gson().toJson(bean)).enqueue(callback);
    }

    @Override
    public void
    getInternetData(String session, int lostid,  Callback<CommentFeedBack> callback) {
        api.getComment(session,lostid).enqueue(callback);
    }

    @Override
    public void sendMessage(String session, int id, Observer<StatusBean> observer) {

    }
//    @Override
//    public void sendMessage(String session,int id , Observer<StatusBean> observer) {
//        api.sendMessage(session,id)
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer);
//        Log.e("ReturnModel","完好"+session+"+"+id);
//    }

    @Override
    public void getphoto(String name, Observer<ImageBean> observer) {

        api.getTheLostPhoto(name)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
