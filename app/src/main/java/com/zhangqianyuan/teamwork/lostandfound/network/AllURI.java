package com.zhangqianyuan.teamwork.lostandfound.network;

import com.zhangqianyuan.teamwork.lostandfound.bean.PlaceBean;
import com.zhangqianyuan.teamwork.lostandfound.bean.TypeBean;

import java.util.ArrayList;
import java.util.List;

public class AllURI {
    public static final String BaseUrl = "http://47.106.232.219/";

    //我们的官网
    public static final String OurWebsite = "http://47.106.232.219/passlove/index.html";

    public static List<String> allTypeBeanList = new ArrayList<>();
    public static List<String> allPlaceBeanList = new ArrayList<>();
    public static List<String> allTypeImgsList = new ArrayList<>();
    //小标签list
    public static List<String> allTypeLittleImgsList = new ArrayList<>();

    //得到用户头像图片
    public static String getUserPhoto(String JSESSION, String imgName){
        return "http://47.106.232.219/passlove/img/user?" + "JSESSIONID="+JSESSION +"&name="+imgName;
    }

    //得到类型的图片
    public static String getTypePhoto(String JSESSION, String imgName){
        return "http://47.106.232.219/passlove/img/losttype?" + "JSESSIONID="+JSESSION +"&name="+imgName;
    }

    //得到类型的小标签
    public static String getTypeLittlePhoto(String JSESSION, String imgName){
        return "http://47.106.232.219/passlove/img/losttype_char?" + "JSESSIONID="+JSESSION +"&name="+imgName;
    }

    //得到失物招领东西的图片
    public static String getLostThingsPhoto(String JSESSION, String imgName){
        return "http://47.106.232.219/passlove/img/thelost?" + "JSESSIONID="+JSESSION +"&name="+imgName;
    }
}
