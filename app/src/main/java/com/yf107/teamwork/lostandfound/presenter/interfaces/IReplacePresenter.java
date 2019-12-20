package com.yf107.teamwork.lostandfound.presenter.interfaces;

import com.yf107.teamwork.lostandfound.bean.TheLostBean;

import java.io.File;
import java.util.List;

public interface IReplacePresenter {
    void postReplace(String session, TheLostBean bean, int id);
    void postReplace(String session, TheLostBean bean, List<File> fileList,int id);
}
