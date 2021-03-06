package com.yf107.teamwork.lostandfound.model.interfaces;

import com.yf107.teamwork.lostandfound.bean.AddCommitBean;
import com.yf107.teamwork.lostandfound.bean.StatusBean;
import com.yf107.teamwork.lostandfound.bean.TheLostBean;

import java.io.File;
import java.util.List;

import io.reactivex.Observer;
import retrofit2.Callback;


public interface IUploadModel {
    void postUpload(String session, TheLostBean bean, List<File> fileList, Observer<AddCommitBean> observer);
    void postUpload(String session, TheLostBean bean, Observer<AddCommitBean> observer);
    void cardUpload(String stu, String session, TheLostBean bean, List<File> fileList, Observer<AddCommitBean> observer);
    void cardUpload(String stu, String session, TheLostBean bean, Observer<AddCommitBean> observer);
   void publishcomment(String session, Integer id, int lostid, String time, String content, Callback<StatusBean> callback);
}
