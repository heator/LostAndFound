package com.yf107.teamwork.lostandfound.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eyeofcloud.Eyeofcloud;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.yf107.teamwork.lostandfound.network.AllURI;
import com.yf107.teamwork.lostandfound.presenter.AllTypesAndPlacesPresenter;
import com.yf107.teamwork.lostandfound.presenter.SignPresenter;
import com.yf107.teamwork.lostandfound.utils.EditUtil;
import com.yf107.teamwork.lostandfound.utils.StatusBarUtil;
import com.yf107.teamwork.lostandfound.utils.ViewUtil;
import com.yf107.teamwork.lostandfound.view.interfaces.IAllTypesAndPlaces;
import com.yf107.teamwork.lostandfound.view.interfaces.ISignInActivity;
import com.yf107.teamwork.lostandfound.R;
import com.yf107.teamwork.lostandfound.bean.PlaceBean;
import com.yf107.teamwork.lostandfound.bean.SignInBean;
import com.yf107.teamwork.lostandfound.bean.TypeBean;

import org.opencv.android.OpenCVLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SignInActivity extends AppCompatActivity implements ISignInActivity, IAllTypesAndPlaces {

    public static final String PWD = "PWD";
    public static final String PNB = "PNB";
    public static final String EMAIL = "EMAIL";
    public static final String STU = "STU";
    public static final String NICKNAME = "NICKNAME";
    public static final String SESSION = "SESSION";
    public static final String USERPHOTO = "USERPHOTO";
    public static final String ALLTYPES = "ALLTYPES";
    public static final String ALLPLACES = "ALLPLACES";
    public static final String ISEXIT = "isExit";
    public static final String EXIT_BACK = "isExit1";

//    public static final String DElETED_NUM_LIST = "DElETED_NUM_LIST";

    @BindView(R.id.signin_signin)
    Button signin;

    @BindView(R.id.signin_pwd)
    EditText pwd;

    @BindView(R.id.signin_username)
    EditText stu;
    @BindView(R.id.signin_tologin)
    TextView register;

    @BindView(R.id.wrong1)
    TextView wrong1;

    @BindView(R.id.reset_password)
    TextView reset_password;

    @BindView(R.id.all_clear)
    Button all_clear;

    private SignPresenter signPresenter;
    private AllTypesAndPlacesPresenter allTypesAndPlacesPresenter;

    private SharedPreferences sharedPreferences;

    private SharedPreferences.Editor editor;

    private Intent mIntent;
    private String session;
    //判断是否用户自己选择退出
    private boolean isExit ;
    private View statusBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        boolean success = OpenCVLoader.initDebug();
        ButterKnife.bind(this);
//        Eyeofcloud.enableEditor(); //注意：正式发布的App中⼀定 不要 调⽤此⽅法！
        Eyeofcloud.startEyeofcloudWithAPIToken("67e81f06e40a2fa3cc2c478344c637b3~10058",getApplication());
        sharedPreferences = getSharedPreferences("users", Context.MODE_PRIVATE);
        signPresenter = new SignPresenter(this);
        allTypesAndPlacesPresenter = new AllTypesAndPlacesPresenter(this);
        boolean isExit = Boolean.parseBoolean(sharedPreferences.getString(ISEXIT,"true"));
        boolean isExit1 = getIntent().getBooleanExtra(EXIT_BACK,false);
        Log.d("Boomerr---日志打印", String.valueOf(isExit));
        /**
         * isExit是写入缓存中判断当前用户是否退出了登录  默认是true
         * 当用户登陆成功后会将这个值重写为false
         * isExit1是用户从设置界面退出后传出的值，一边情况设置为false  当用户退出登录的时候回传一个true 阻止直接跳过登录页面
         *
         * */
        if(isExit1){
            sharedPreferences.edit().putString(ISEXIT,"true").commit();
            Log.d("Boomerr日志打印","更新isEixt" + sharedPreferences.getString(ISEXIT,"true"));
        }
        //如果已经登录过，再次登陆，直接进入缓冲页
        Log.e("SignInActivity","STU="+sharedPreferences.getString(STU, "")
        +sharedPreferences.getString(EMAIL, "")
        +sharedPreferences.getString(PNB, "")
                +sharedPreferences.getString(PWD, "")
        +sharedPreferences.getString(NICKNAME, "")
        +sharedPreferences.getString(SESSION,""));
        if (!"".equals(sharedPreferences.getString(STU, "")) && !"".equals(sharedPreferences.getString(SESSION, "")) && !isExit && !isExit1) {
            Intent intent = new Intent(SignInActivity.this, BufferPageActivity.class);
            startActivity(intent);
            finish();
//            email.setText(sharedPreferences.getString(EMAIL, "null"));
//            pwd.setText(sharedPreferences.getString(PWD, "null"));
//            signPresenter.getSignIn(sharedPreferences.getString(EMAIL, "null"), sharedPreferences.getString(PWD, "null"));
        } else if (isExit) {
            stu.setText("");
            pwd.setText("");
            isExit = false;
        }
//        else {
//            mIntent = getIntent();
//            if (!mIntent.getBooleanExtra(TO_SIGN_IN, true)) {
//                signPresenter.getSignIn(mIntent.getStringExtra(EMAIL), mIntent.getStringExtra(PWD));
//            }
//        }
        //实现渐变式状态栏
        StatusBarUtil.setGradientStatusBarColor(this,statusBarView);
        //字体
        register.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        reset_password.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        stu.addTextChangedListener(new CheckOutTextWatcher(stu,pwd));
        EditUtil.EditAllClear(all_clear,pwd);
    }

    private class CheckOutTextWatcher implements TextWatcher {
        private EditText mView; // 声明一个编辑框对象
        private int mMaxLength; // 声明一个最大长度变量
        private CharSequence mStr; // 声明一个文本串
        private EditText mNextView;// 声明下一个视图对象

        public CheckOutTextWatcher(EditText vThis, EditText vNext) {
            super();
            mView = vThis;
            if (vNext != null) {
                mNextView = vNext;
                mMaxLength = ViewUtil.getMaxLength(vThis);
            }
        }

        public CheckOutTextWatcher(EditText v) {
            super();
            mView = v;
            // 通过反射机制获取编辑框的最大长度
            mMaxLength = ViewUtil.getMaxLength(v);
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence s, int i, int i1, int i2) {
            mStr = s;
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mStr == null || mStr.length() == 0)
                return;
            // 输入文本达到10位跳转到下一个编辑框
            if (mStr.length() == 10 && mMaxLength == 10) {
                // 让下一个视图获得焦点，即将光标移到下个视图
                mNextView.requestFocus();
                // 让光标自动移到编辑框内部的文本末尾
                EditText et = (EditText) mNextView;
                et.setSelection(et.getText().length());
            }

        }
    }

    @Override
    protected void onDestroy() {
        allTypesAndPlacesPresenter.dettachActivity();
        signPresenter.dettachActivity();
        super.onDestroy();
    }

    @OnClick({R.id.signin_tologin, R.id.signin_signin,R.id.all_clear,R.id.reset_password})
    void onClicked(View view) {
        switch (view.getId()) {
            //点击登陆
            case R.id.signin_signin: {
                if ("".equals(pwd.getText().toString()) || "".equals(stu.getText().toString())) {
                    FancyToast.makeText(SignInActivity.this, "输入有问题", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                } else {
                    String student = stu.getText().toString();
                    String password = pwd.getText().toString();
                    signPresenter.getSignIn(student, password);
                }
                break;
            }
            //点击“未有账号，点击注册”
            case R.id.signin_tologin: {
                Intent intent = new Intent(SignInActivity.this, LogInActivity.class);
                startActivity(intent);

                break;
            }

            //一键清除清除文本框
            case R.id.all_clear:{
                pwd.setText("");
                pwd.requestFocusFromTouch();
                break;
            }
            //重置密码

            case R.id.reset_password:{
                Intent intent = new Intent(SignInActivity.this,ForgetPasswordActivity.class);
                intent.putExtra("session",SESSION);
                startActivity(intent);

                break;
            }
            default: {
                break;
            }
        }
    }


    @Override
    public void showSignInStatus(Boolean status, SignInBean signInBean) {
        if (status) {
            editor = sharedPreferences.edit();
//            Intent intent = getIntent();
//            String s = intent.getStringExtra(PWD);
//            signInBean.getUser().setPassword(s);
            int[] arrayint = new int[10000];
            editor.putString(EMAIL, signInBean.getUser().getUsername());
            editor.putString(PWD, signInBean.getUser().getPassword());
            editor.putString(STU, stu.getText().toString());//signInBean.getUser().getStu()一直是空
            editor.putString(NICKNAME, signInBean.getUser().getNickname());
            editor.putString(PNB, signInBean.getUser().getPhonenumber());
            editor.putString(USERPHOTO, signInBean.getUser().getPhoto());
            editor.putString(SESSION, signInBean.getJSESSIONID());
            editor.putString(ISEXIT,"false");
            session = signInBean.getJSESSIONID();
            Log.e("Sign",""+STU);
            //去获取所有的丢失物品类型和地点
            editor.commit();
            allTypesAndPlacesPresenter.getAllTypesAndPlaces(signInBean.getJSESSIONID());
        } else {
            wrong1.setText("  您输入的账号或密码错误！");
            //FancyToast.makeText(SignInActivity.this, "登录失败", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
        }
    }

    @Override
    public void getIAllTypesAndPlaces(Boolean status, List<TypeBean> typeBeanList, List<PlaceBean> placeBeanList) {
        if (status) {
            int i = typeBeanList.size();
            for (int k = 0; k < i; k++) {
                AllURI.allTypeBeanList.add(typeBeanList.get(k).getName());
                AllURI.allTypeImgsList.add(typeBeanList.get(k).getPhoto());
            }
            i = placeBeanList.size();
            for (int k = 0; k < i; k++) {
                AllURI.allPlaceBeanList.add(placeBeanList.get(k).getName());
            }
            Log.e("SignIn", AllURI.allPlaceBeanList.toString());
            Log.e("SignIn", AllURI.allTypeBeanList.toString());
            Log.e("SignIn", AllURI.allTypeImgsList.toString());
            Log.e("SignIn", sharedPreferences.getString(SESSION, "null"));
            Intent intent = new Intent(SignInActivity.this, BufferPageActivity.class);
            intent.putExtra(SESSION, session);
            startActivity(intent);
            finish();
        } else {
            FancyToast.makeText(SignInActivity.this, "无法获取丢失物品类型和地点", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
