package com.zhangqianyuan.teamwork.lostandfound.view.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.zhangqianyuan.teamwork.lostandfound.R;
import com.zhangqianyuan.teamwork.lostandfound.adapter.MyLoadItemAdapter;
import com.zhangqianyuan.teamwork.lostandfound.bean.MyHistoryItem;
import com.zhangqianyuan.teamwork.lostandfound.bean.MyLoadItemBean;
import com.zhangqianyuan.teamwork.lostandfound.bean.TheLostBean;
import com.zhangqianyuan.teamwork.lostandfound.model.MyHistoryModel;
import com.zhangqianyuan.teamwork.lostandfound.presenter.MyHistoryPresenter;
import com.zhangqianyuan.teamwork.lostandfound.view.interfaces.IMyHistoryActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Description
 * 我的板块中 我的发布板块
 * @author  zhou
 */
// TODO: 2018/11/13  加入 上传item数据
public class UserInfoMyUpload extends AppCompatActivity implements IMyHistoryActivity {
    private List<TheLostBean> lists = new ArrayList<>();
    private MyHistoryPresenter presenter = new MyHistoryPresenter(new MyHistoryModel());
    private MyLoadItemAdapter  mAdapter ;
   @BindView(R.id.myhistory_myload_list)
   RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_my_upload);
        initMvp();
        initView();
    }

    public void initView(){
        ButterKnife.bind(this);
        Log.d("123456","success");
        list.setLayoutManager(new LinearLayoutManager(this));
    }

    public void initMvp(){
        presenter.attachActivity(this);
        presenter.getMyHistoryData(getSharedPreferences("users",MODE_PRIVATE).getString("SESSION",null),0,15);
    }

    @Override
    public void showData(List<TheLostBean> beans) {
        lists.clear();
        lists.addAll(beans);
        mAdapter=new MyLoadItemAdapter(lists);
        list.setAdapter(mAdapter);
    }
}
