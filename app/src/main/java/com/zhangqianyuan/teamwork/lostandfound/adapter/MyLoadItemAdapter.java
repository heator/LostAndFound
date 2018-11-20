package com.zhangqianyuan.teamwork.lostandfound.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhangqianyuan.teamwork.lostandfound.R;
import com.zhangqianyuan.teamwork.lostandfound.bean.MyHistoryItem;
import com.zhangqianyuan.teamwork.lostandfound.bean.MyLoadItemBean;
import com.zhangqianyuan.teamwork.lostandfound.model.MyHistoryModel;
import com.zhangqianyuan.teamwork.lostandfound.model.MyLoadModel;
import com.zhangqianyuan.teamwork.lostandfound.network.AllURI;
import com.zhangqianyuan.teamwork.lostandfound.presenter.MyHistoryPresenter;
import com.zhangqianyuan.teamwork.lostandfound.presenter.MyLoadPresenter;
import com.zhangqianyuan.teamwork.lostandfound.view.interfaces.IMyHistoryActivity;
import com.zhangqianyuan.teamwork.lostandfound.view.interfaces.IMyLoadActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author zhoudada
 * @version $Rev$
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */

public class MyLoadItemAdapter  extends RecyclerView.Adapter<MyLoadItemAdapter.ViewHolder>  {
    private List<MyHistoryItem.DataBean> lists ;
    private Context mContext;


    public static class ViewHolder extends  RecyclerView.ViewHolder {
        @BindView(R.id.myhistory_thingtype)
        ImageView  thingtype;

        @BindView(R.id.myhistory_thingtype_txt)
        ImageView  thingtypetxt;

        @BindView(R.id.myhistory_eventtype)
        ImageView  eventtype;

        @BindView(R.id.myhistory_time)
        TextView  time  ;                     //月 日

        @BindView(R.id.myhistory_time2)
        TextView  time2;                     //19:00

        @BindView(R.id.myhistory_where)
        TextView  where;

        @BindView(R.id.isdone_description)
        TextView   isdong;

        public ViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }

    public MyLoadItemAdapter (List<MyHistoryItem.DataBean> list){
        this.lists=list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("oncreateviewholder","seccess");
        mContext=parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.userinfo_myhistory_item,parent,false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("onbindviewholder","seccess");
        String s = AllURI.getUserPhoto(mContext.getSharedPreferences("users",Context.MODE_PRIVATE).getString("SESSION",null),lists.get(position).getPhoto());;
        Glide.with(mContext)
                .load(s)
                .asBitmap()
                .into(holder.thingtype);

        holder.isdong.setText(lists.get(position).getDescription());
        if (lists.get(position).getLosttype()==0){
            //加载丢的图片
        }else if (lists.get(position).getLosttype()==1){
            //加载拾的图片
        }
        String lostPlace = AllURI.allPlaceBeanList.get(lists.get(position).getPlaceid());
        // TODO: 2018/11/20  将类型小图片 用swith 方法进行选择加载
        holder.where.setText(lostPlace);
        String publishTime= lists.get(position).getPublishtime();
        if (!publishTime.equals("")){
            String   mouth = publishTime.substring(4,6);
            String    day = publishTime.substring(6,8);
            String    hours=publishTime.substring(8,10);
            String time1 = mouth+" 月"+"  "+day+" 日";
            String time2=  hours+":00";
            holder.time.setText(time1);
            holder.time2.setText(time2);
        }

    }

    @Override
    public int getItemCount() {
        return lists.size();
    }



}
