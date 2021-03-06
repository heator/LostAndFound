package com.yf107.teamwork.lostandfound.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bumptech.glide.Glide;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.yf107.teamwork.lostandfound.image.GlideImageLoader;
import com.yf107.teamwork.lostandfound.network.AllURI;
import com.yf107.teamwork.lostandfound.services.ActivityManager;
import com.yf107.teamwork.lostandfound.utils.StatusBarUtil;
import com.yf107.teamwork.lostandfound.view.interfaces.IReplceActivity;
import com.yf107.teamwork.lostandfound.R;
import com.yf107.teamwork.lostandfound.bean.FormFile;
import com.yf107.teamwork.lostandfound.bean.TheLostBean;
import com.yf107.teamwork.lostandfound.presenter.ReplacePresenter;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.BuildConfig;
import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;

import static cn.finalteam.toolsfinal.DateUtils.getDay;
import static cn.finalteam.toolsfinal.DateUtils.getMonth;
import static cn.finalteam.toolsfinal.DateUtils.getYear;
import static com.yf107.teamwork.lostandfound.view.activity.SignInActivity.SESSION;

/**
 * Description
 * 点击上传界面后用户填写表单的界面
 * @author  zhou
 */
public class ReplaceActivity extends AppCompatActivity implements IReplceActivity {

    //返回按键
    @BindView(R.id.upload_lostorfind_back)
    ImageView btnBack;

    //头部标题栏
    @BindView(R.id.upload_lostorfind_qishitype)
    TextView qishiType;

    //选择时间
    @BindView(R.id.upload_lostorfind_time)
    EditText timeText;

    //选择地点
    @BindView(R.id.upload_lostorfind_place)
    EditText textPlace;

    @BindView(R.id.linearlayout)
    LinearLayout linearLayout;

    //选择学生卡
    @BindView(R.id.upload_lostorfind_stu)
    EditText stuEdit;

    //编辑标题
    @BindView(R.id.upload_lostorfind_description_title)
    EditText titleEdit;

    //编辑内容
    @BindView(R.id.upload_lostorfind_description_text)
    EditText descEdit;

    //图片
    @BindView(R.id.upload_lostorfind_description_img)
    ImageView img;

    //选择图片
    @BindView(R.id.upload_lostorfind_description_upload)
    TextView upload;

    //确认发布按键
    @BindView(R.id.upload_lostorfind_sure)
    Button btnSure;

    private static final int REQUEST_CODE_GALLERY = 1;
    //时间选择器
    private TimePickerView pvTime;

    //赏金选择器
    private OptionsPickerView pvOptionsBounty;

    //地点选择器
    private OptionsPickerView pvOptionsPlace;


    private SharedPreferences sharedPreferences;

    private ReplacePresenter replacePresenter;

    private String jsession;

    private TheLostBean bean;

    private Integer needBounty = -1;

    private List<FormFile> files = new ArrayList<>();

    private List<File> fileList = new ArrayList<>();

    private Integer placeid = -1;

    private String strphoto = "";

    private String strLostDate = "";
    private int id;
    private Integer qishileixing;
    private Integer typeid;
    private String strtitle;
    private String strdescri;
    private String stu;
    private View statusBarView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replace);
        ButterKnife.bind(this);
        //实现渐变式状态栏
        StatusBarUtil.setGradientStatusBarColor(this,statusBarView);
        ActivityManager.getActivityManager().add(this);
        sharedPreferences = getSharedPreferences("users", Context.MODE_PRIVATE);
        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);
        Log.e("ReplaceActivity",""+id);
        typeid = intent.getIntExtra("typeid", 1);
        qishileixing =intent.getIntExtra("losttype",0);
        strtitle = intent.getStringExtra("title");
        strdescri = intent.getStringExtra("description");
        placeid = intent.getIntExtra("placeid",0);
        strLostDate = intent.getStringExtra("losttime");
        strphoto = intent.getStringExtra("photo");
        initView();
        replacePresenter = new ReplacePresenter();
        replacePresenter.attachActivity(this);

    }

    @Override
    protected void onDestroy() {
        replacePresenter.dettachActivity();
        super.onDestroy();
    }

    @OnClick({R.id.upload_lostorfind_back,R.id.upload_lostorfind_time
            ,R.id.upload_lostorfind_place,R.id.upload_lostorfind_description_img
            ,R.id.upload_lostorfind_description_upload
            ,R.id.upload_lostorfind_sure})
    void onClicked(View view) {
        switch (view.getId()){
            //返回
            case R.id.upload_lostorfind_back:{
                finish();
                break;
            }
            //选择时间
            case R.id.upload_lostorfind_time:{
                pvTime.show();
                break;
            }
            //选择地点
            case R.id.upload_lostorfind_place:{
                pvOptionsPlace.show();
                break;
            }
            //选择图片
            case R.id.upload_lostorfind_description_img:
            case R.id.upload_lostorfind_description_upload:{
                initGallery();
                break;
            }
            //确认发布
            case R.id.upload_lostorfind_sure: {
                strdescri = descEdit.getText().toString();
                strtitle = titleEdit.getText().toString();
                if ("".equals(strdescri) || "".equals(strtitle) || "".equals(strLostDate) || -1 == placeid) {
                    FancyToast.makeText(ReplaceActivity.this, "填写不规范", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                } else {
                    jsession = sharedPreferences.getString(SESSION, "null");
                    if("".equals(strphoto)){
                        Log.e("UploadFromActivity","strLostDate="+strLostDate);
                        bean = new TheLostBean(typeid+1,qishileixing,strtitle,strdescri,placeid+1,"00000000",strLostDate,"default.jpg",0);
                        Log.e("THELOSTBEAN",bean.toString());
                        Log.e("ReplaceActivity",""+id);
                        replacePresenter.postReplace(jsession,bean,id);
                    }else {
                        bean = new TheLostBean(typeid+1,qishileixing,strtitle,strdescri,placeid+1,"00000000",strLostDate,strphoto,0);
                        Log.e("THELOSTBEAN",bean.toString());
                        Log.e("ReplaceActivity",""+id);
                        replacePresenter.postReplace(jsession, bean, fileList,id);
                        btnSure.setVisibility(View.GONE);
                        FancyToast.makeText(ReplaceActivity.this, "图片上传中，请耐心等侯", FancyToast.LENGTH_SHORT, FancyToast.DEFAULT, false).show();
                    }

                }
                break;
            }
            default:{
                break;
            }
        }
    }



    private void initView(){
        String strthingtype = AllURI.allTypeBeanList.get(typeid);
        String strplacetype = AllURI.allPlaceBeanList.get(placeid);
        if(qishileixing==0){
            qishiType.setText("发布失物—"+strthingtype);
        }else {
            qishiType.setText("发布招领—"+strthingtype);
        }
        timeText.setText(strLostDate);
        textPlace.setText(strplacetype);
        titleEdit.setText(strtitle);
        descEdit.setText(strdescri);
        Log.e("Replace","strphoto = "+strphoto);
        if(strphoto.equals("")||strphoto.equals("default.jpg") || strphoto == null){
            img.setImageResource(R.mipmap.user);
        }else {
            Glide.with(this)
                    .load(strphoto)
                    .asBitmap()
                    .into(img);
        }


        //地点选择
        pvOptionsPlace= new OptionsPickerBuilder(ReplaceActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        placeid = options1;
                        textPlace.setText(AllURI.allPlaceBeanList.get(options1));
                    }
                });
            }
        })
                //取消按钮文字
                .setCancelText("取消")
                //确认按钮文字
                .setSubmitText("确定")
                //是否显示为对话框样式
                .isDialog(true)
                //切换时是否还原，设置默认选中第一项。
                .isRestoreItem(false)
                .build();
        pvOptionsPlace.setPicker(AllURI.allPlaceBeanList);

        //时间选择器
        pvTime = new TimePickerBuilder(ReplaceActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                strLostDate = "";
                String month = "";
                String day = "";
                if(getMonth(date)<=9){
                    month = "0" + String.valueOf(getMonth(date));
                }else {
                    month = String.valueOf(getMonth(date));
                }
                if(getDay(date)<=9){
                    Log.e("add0","");
                    day = "0" + String.valueOf(getDay(date));
                }else {
                    Log.e("no0","");
                    day = String.valueOf(getDay(date));
                }
                strLostDate = String.valueOf(getYear(date))+month+day;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timeText.setText(String.valueOf(getYear(date))+"年"+String.valueOf(getMonth(date))+"月"+String.valueOf(getDay(date))+"日");
                    }
                });
            }

        })
                //取消按钮文字
                .setCancelText("取消")
                //确认按钮文字
                .setSubmitText("确定")
                //是否显示为对话框样式
                .isDialog(true)
                .build();

    }


    /**
     * 选择图片
     */
    private void initGallery(){
        //设置主题
        //ThemeConfig.CYAN
        ThemeConfig theme = new ThemeConfig.Builder()
                .setTitleBarBgColor(Color.rgb(0x78, 0x79, 0xFF))
                .setFabNornalColor(Color.rgb(0x78, 0x79, 0xFF))
                .setFabPressedColor(Color.rgb(0x78, 0x79, 0xFF))
                .setCropControlColor(Color.rgb(0xFF, 0xFF, 0xFF))
                .build();
        //配置功能
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(false)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setCropSquare(true)
                .setEnablePreview(false)
                .build();

        //配置imageloader
        GlideImageLoader imageloader = new GlideImageLoader();
        CoreConfig coreConfig = new CoreConfig.Builder(ReplaceActivity.this, imageloader, theme)
                .setDebug(BuildConfig.DEBUG)
                .setFunctionConfig(functionConfig).build();
        GalleryFinal.init(coreConfig);

        GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY,8 ,mOnHandlerResultCallback);
    }


    private GalleryFinal.OnHanlderResultCallback mOnHandlerResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            //进行图片上传与置换
            //置换
            String photoPath = resultList.get(0).getPhotoPath();
            img.setImageBitmap(BitmapFactory.decodeFile(photoPath));
            FancyToast.makeText(ReplaceActivity.this,"取得照片",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
            //上传
            for(int i =0 ; i<resultList.size();i++){
                if(i>0){
                    strphoto = strphoto+",";
                }
                fileList.add(new File(resultList.get(i).getPhotoPath()));
                strphoto = strphoto + resultList.get(i).getPhotoPath();
                Log.e("ImgTest",resultList.get(i).getPhotoPath());
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Log.e("editinfo",errorMsg);
            FancyToast.makeText(ReplaceActivity.this,errorMsg,FancyToast.LENGTH_SHORT,FancyToast.ERROR,true).show();
        }
    };


    @Override
    public void showStatus(Boolean status,int lostid) {
        if (status) {
            replacePresenter.sendDataToWeb(sharedPreferences.getString("SESSION", null), null, lostid, null, "");
            Intent intent = new Intent(ReplaceActivity.this, UploadSuccessActivity.class);
            intent.putExtra("SESSION", sharedPreferences.getString(SESSION, "null"));
            startActivity(intent);
            finish();
            FancyToast.makeText(ReplaceActivity.this, "发布成功", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
        } else {
            FancyToast.makeText(ReplaceActivity.this, "出现了错误", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
            btnSure.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void isSuccess(boolean status) {

        if(status){
            Log.d("成功了成功了","成功了成功了");
        }
    }
}
