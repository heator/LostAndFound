package com.zhangqianyuan.teamwork.lostandfound.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.shashank.sony.fancytoastlib.FancyToast;
import com.squareup.leakcanary.LeakCanary;
import com.zhangqianyuan.teamwork.lostandfound.R;
import com.zhangqianyuan.teamwork.lostandfound.adapter.MainViewAdapter;
import com.zhangqianyuan.teamwork.lostandfound.model.UserInfoModel;
import com.zhangqianyuan.teamwork.lostandfound.presenter.UserInfoPresenter;
import com.zhangqianyuan.teamwork.lostandfound.services.ActivityManager;
import com.zhangqianyuan.teamwork.lostandfound.view.fragment.DynamicFragment;
import com.zhangqianyuan.teamwork.lostandfound.view.fragment.MessageFragment;
import com.zhangqianyuan.teamwork.lostandfound.view.fragment.SearchFragment;
import com.zhangqianyuan.teamwork.lostandfound.view.fragment.UserInfoFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zhangqianyuan.teamwork.lostandfound.view.activity.SignInActivity.SESSION;


/**
 * Description: 主页面
 * Created at: 2018/10/30 0:08
 * @author: zhangqianyuan
 * Email: zhang.qianyuan@foxmail.com
 */
public class MainActivity extends AppCompatActivity {

    public static final Integer FINE_INTERNET_STATUS = 200;
    public static final Integer BAD_INTERNET_STATUS = 400;
    public static final String TYPEID = "TYPEID";
    public static final String QISHILEIXING = "QISHILEIXING";

    /** 上次点击返回键的时间 */
    private long lastBackPressed;

    /** 两次点击的间隔时间 */
    private static final int QUIT_INTERVAL = 2000;

    /**
     * 几个常量
     */
    private final int DONGTAI_FRAGMENT = 0;
    private final int SEARCH_FRAGMENT = 1;
    private final int MESSAGE_FRAGMENT = 2;
    private final int MINE_FRAGMENT = 3;

    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;

    @BindView(R.id.main_title_tv)
    TextView mTitleTv;

    @BindView(R.id.main_view_pager)
    ViewPager mViewPager;

    /* 这个里面包含了图标和文字 */
    @BindView(R.id.navigation)
    BottomNavigationView mBottomNav;

    private String[] titles = new String[]{"动态", "搜索", "消息","我的"};
    private String session = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main);

        ActivityManager.getActivityManager().add(this);
        ButterKnife.bind(this);
        try {
            initView();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Intent intent = getIntent();
        session = intent.getStringExtra(SESSION);
        initPermission();
    }


    /**
     * Description: 初始化界面
     */
    private void initView() throws NoSuchFieldException, IllegalAccessException {

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new DynamicFragment(session));
        fragments.add(SearchFragment.newInstance());
        fragments.add(MessageFragment.newInstance());
        fragments.add(new UserInfoFragment());

        MainViewAdapter mainViewAdapter = new MainViewAdapter(getSupportFragmentManager());

        mainViewAdapter.setFragments(fragments);

        mViewPager.setAdapter(mainViewAdapter);
        mViewPager.setOffscreenPageLimit(3);

        //划页监听器
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position == 1||position==3){
                    appBarLayout.setVisibility(View.GONE);
                }else {
                    appBarLayout.setVisibility(View.VISIBLE);
                }
                mTitleTv.setText(titles[position]);
                if(position<=1){
                    mBottomNav.getMenu().getItem(position).setChecked(true);
                }else if(position>=2&&position<=3){
                    mBottomNav.getMenu().getItem(position+1).setChecked(true);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if(position == 1||position==3){
                    appBarLayout.setVisibility(View.GONE);
                }else {
                    appBarLayout.setVisibility(View.VISIBLE);
                }
                mTitleTv.setText(titles[position]);
                if(position<=1){
                    mBottomNav.getMenu().getItem(position).setChecked(true);
                }else if(position>=2&&position<=3){
                    mBottomNav.getMenu().getItem(position+1).setChecked(true);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        disableShiftMode(mBottomNav);

        //加入底部导航监听
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.dongtai_ui:{
                        mViewPager.setCurrentItem(DONGTAI_FRAGMENT);
                        return true;
                    }
                    case R.id.search_ui:{
                        mViewPager.setCurrentItem(SEARCH_FRAGMENT);
                        return true;
                    }
                    case R.id.newmessage_ui:{
                        showPopUpWindow();
                        return true;
                    }
                    case R.id.message_ui:{
                        mViewPager.setCurrentItem(MESSAGE_FRAGMENT);
                        return true;
                    }
                    case R.id.mine_ui:{
                        mViewPager.setCurrentItem(MINE_FRAGMENT);
                        return true;
                    }
                    default:{
                        return true;
                    }
                }
            }
        });
    }

    /**
     * popupWindows
     */
    private void showPopUpWindow() {
        View contentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.choose, null);
        final PopupWindow mPopWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        mPopWindow.setContentView(contentView);
        ImageView newShiWu = contentView.findViewById(R.id.newShiWu);
        ImageView newZhaoLing = contentView.findViewById(R.id.newZhaoLing);
        ImageView cancel = contentView.findViewById(R.id.cancel);

        View rootView = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null);
        mPopWindow.setTouchable(true);
        mPopWindow.setOutsideTouchable(true);
        mPopWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);


        //新建失物
        newShiWu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new  Intent(view.getContext(),UploadActivity.class);
                intent.putExtra(QISHILEIXING,0);
                startActivity(intent);
            }
        });

        //ic_newzhaolin
        newZhaoLing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new  Intent(view.getContext(),UploadActivity.class);
                intent.putExtra(QISHILEIXING,1);
                startActivity(intent);
            }
        });

        //取消
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopWindow.dismiss();
            }
        });
    }


    /**
     * 申请权限
     */
    private void initPermission() {
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET, Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
    }

    /**
     * 申请权限回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                } else {
                    FancyToast.makeText(MainActivity.this,"你拒绝了权限", FancyToast.ERROR, Toast.LENGTH_SHORT,false).show();
                }
                break;
            default:
                break;
        }
    }


    /**
     * 重写返回键响应函数，按两次才退出
     */
    @Override
    public void onBackPressed() {
        long backPressed = System.currentTimeMillis();
        if (backPressed - lastBackPressed > QUIT_INTERVAL) {
            lastBackPressed = backPressed;
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_LONG).show();
        } else {
            finish();
            System.exit(0);
        }
    }

    
    /**
     * Description: 反射解决使用的时候 item 数大于 3 个时会有位移
     */
    @SuppressLint("RestrictedApi")
    public void disableShiftMode(BottomNavigationView view) throws NoSuchFieldException, IllegalAccessException {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try { Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true); shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                //noinspection RestrictedApi
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                // noinspection RestrictedApi
                item.setChecked(item.getItemData().isChecked());
                }
        } catch (NoSuchFieldException e) {
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e);
        }
    }

}
