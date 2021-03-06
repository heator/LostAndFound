package com.yf107.teamwork.lostandfound.model;

import com.yf107.teamwork.lostandfound.model.interfaces.ILogInModel;
import com.yf107.teamwork.lostandfound.bean.SendCheckCodeBean;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LogInModel extends BaseModel implements ILogInModel {

    public LogInModel(){
        super();
    }

    @Override
    public void getInfo(String email, Observer<SendCheckCodeBean> observer) {
        api.getSendCheckCode(email)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
