package com.zhangqianyuan.teamwork.lostandfound.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhangqianyuan.teamwork.lostandfound.R;
import com.zhangqianyuan.teamwork.lostandfound.bean.DynamicItem;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author zhoudada
 * @version $Rev$
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class DynamicItemAdapter extends RecyclerView.Adapter<DynamicItemAdapter.ViewHolder> {
   private Context mContext;
   private List<DynamicItem> lists;


    public static class ViewHolder extends RecyclerView.ViewHolder{
       CardView mCardView;
       CircleImageView headimg;
       TextView        neckname;
       TextView        time;
       TextView        description;
       TextView        thingType;

       public ViewHolder(View view){
           super(view);
           mCardView = view.findViewById(R.id.dynamic_card);
           headimg = view.findViewById(R.id.dynamic_card_headimg);
           neckname = view.findViewById(R.id.dynamic_card_neckname);
           time  = view.findViewById(R.id.dynamic_card_time);
           description = view.findViewById(R.id.dynamic_card_description);
           thingType = view.findViewById(R.id.dynamic_card_thingtype);
       }
   }
    public DynamicItemAdapter(List<DynamicItem> list){
        this.lists = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext==null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_dynamic_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DynamicItem dynamicItem = lists.get(position);
        if (dynamicItem.getCardType()==DynamicItem.TYPE_LOST){
        holder.mCardView.setBackgroundColor(Color.parseColor("FFE086A9"));
    }
       if (dynamicItem.getCardType()==DynamicItem.TYPE_FIND){
            holder.mCardView.setBackgroundColor(Color.parseColor("FF9C8BE7"));
       }
        holder.headimg.setImageResource(dynamicItem.getHeadImg());
        holder.neckname.setText(dynamicItem.getNeckName());
        holder.time.setText(dynamicItem.getTime());
        holder.description.setText(dynamicItem.getDescription());
        holder.thingType.setText(dynamicItem.getThingType());
    }

    @Override
    public int getItemCount() {
        return  lists.size();
    }

}
