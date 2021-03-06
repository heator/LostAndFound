package com.yf107.teamwork.lostandfound.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.shashank.sony.fancytoastlib.FancyToast;
import com.yf107.teamwork.lostandfound.adapter.UploadFragmentAdapter;
import com.yf107.teamwork.lostandfound.network.AllURI;
import com.yf107.teamwork.lostandfound.services.ActivityManager;
import com.yf107.teamwork.lostandfound.utils.StatusBarUtil;
import com.yf107.teamwork.lostandfound.R;
import com.yf107.teamwork.lostandfound.bean.UploadItemBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.yf107.teamwork.lostandfound.view.activity.MainActivity.QISHILEIXING;
import static com.yf107.teamwork.lostandfound.view.activity.SignInActivity.SESSION;

/**
 * Description
 * 上传失物/招领启事界面

 */
public class UploadActivity extends AppCompatActivity {

    private List<UploadItemBean> lists = new ArrayList<>();

    @BindView(R.id.upload_list)
    RecyclerView mRecyclerView;

    @BindView(R.id.upload_toolbar)
    Toolbar mToolbar;

    private SharedPreferences sharedPreferences;

    private UploadFragmentAdapter uploadFragmentAdapter;
    private View statusBarView;
    private Integer qishileixing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        ActivityManager.getActivityManager().add(this);
        sharedPreferences = getSharedPreferences("users", Context.MODE_PRIVATE);
        if (savedInstanceState == null) {
            initLists();
        }
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void initView() {
        Intent intent = getIntent();
        qishileixing = intent.getIntExtra(QISHILEIXING, 0);
        Log.e("qishileixing","qishileixing1 = "+qishileixing);
        ButterKnife.bind(this);
        //实现渐变式状态栏
        StatusBarUtil.setGradientStatusBarColor(this, statusBarView);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        uploadFragmentAdapter = new UploadFragmentAdapter(lists, qishileixing);
        mRecyclerView.setAdapter(uploadFragmentAdapter);
    }

    public void initLists() {
        int i = AllURI.allTypeImgsList.size();
        String session = sharedPreferences.getString(SESSION, " ");
        for (int k = 0; k < i; k++) {
            String s = "";
            s = AllURI.getTypePhoto(session, AllURI.allTypeImgsList.get(k));
            UploadItemBean uploadItemBean = new UploadItemBean(s, AllURI.allTypeBeanList.get(k));
            lists.add(uploadItemBean);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.upload, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                break;
            }
            default: {
                break;
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
