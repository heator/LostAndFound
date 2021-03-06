package com.yf107.teamwork.lostandfound.model;

import com.yf107.teamwork.lostandfound.model.interfaces.IAllPlacesModel;
import com.yf107.teamwork.lostandfound.bean.AllPlacesBean;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AllPlacesModel extends BaseModel implements IAllPlacesModel {

    public AllPlacesModel(){
        super();
    }


    @Override
    public void getAllPlaces(String session, Observer<AllPlacesBean> observer) {
        api.getAllPlaces(session)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
