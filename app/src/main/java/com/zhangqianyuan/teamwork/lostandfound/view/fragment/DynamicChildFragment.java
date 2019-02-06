package com.zhangqianyuan.teamwork.lostandfound.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shashank.sony.fancytoastlib.FancyToast;
import com.zhangqianyuan.teamwork.lostandfound.R;
import com.zhangqianyuan.teamwork.lostandfound.adapter.DynamicItemAdapter;
import com.zhangqianyuan.teamwork.lostandfound.bean.DynamicItemBean;
import com.zhangqianyuan.teamwork.lostandfound.bean.DynamicsRequestBean;
import com.zhangqianyuan.teamwork.lostandfound.presenter.DynamicPresenter;
import com.zhangqianyuan.teamwork.lostandfound.presenter.IDynamicPresenter;
import com.zhangqianyuan.teamwork.lostandfound.services.ActivityManager;
import com.zhangqianyuan.teamwork.lostandfound.view.interfaces.IDynaicFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * Description
 * 动态板块子页面（分为失物/招领）
 *
 * @author zhoudada
 * @version $Rev$
 * @des
 * @updateAuthor Zhangqianyuan
 * @updateDes 交错layout
 */

@SuppressLint("ValidFragment")
public class DynamicChildFragment extends Fragment implements IDynaicFragment, SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout refreshLayout;
    private DynamicItemAdapter mDynamicItemAdapter;
    private List<DynamicItemBean> lists = new ArrayList<>();
    private DynamicPresenter iDynamicPresenter;
    private int pos;
    private SharedPreferences sharedPreferences;

    private String session = "";
    //控制数量
    private Integer newPosi;
    private Integer oldPosi;

    @SuppressLint("ValidFragment")
    public DynamicChildFragment(int i, String session) {
        pos = i;
        this.session = session;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_dynamic_lostorfind, container, false);

        newPosi = 15;
        oldPosi = 15;
        mRecyclerView = view.findViewById(R.id.dynamic_list);
        refreshLayout = view.findViewById(R.id.dynamic_list_swipe);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);//不设置的话，图片闪烁错位，有可能有整列错位的情况。
        ActivityManager.getActivityManager().addF(this);
        mRecyclerView.setLayoutManager(manager);
        mDynamicItemAdapter = new DynamicItemAdapter(lists, getActivity());
        mRecyclerView.setAdapter(mDynamicItemAdapter);
        refreshLayout.setOnRefreshListener(this);
        iDynamicPresenter = new DynamicPresenter();
        iDynamicPresenter.attachActivity(this);
        initLists();

        return view;
    }

    public void initLists() {
        switch (pos) {
            case 0: {
                iDynamicPresenter.getDynamicLostData(new DynamicsRequestBean(0, 15), session);
                break;
            }
            case 1: {
                iDynamicPresenter.getDynamicFindData(new DynamicsRequestBean(0, 15), session);
                break;
            }
            default: {
                break;
            }
        }
    }

    @Override
    public void onDestroyView() {
        iDynamicPresenter.dettachActivity();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        iDynamicPresenter.dettachActivity();
        super.onDestroy();
    }

    @Override
    public void showData(Boolean status, List<DynamicItemBean> searchItemBeanArrayList) {
        refreshLayout.setRefreshing(false);
        lists.clear();
        lists.addAll(searchItemBeanArrayList);
//            mDynamicItemAdapter.notifyDataSetChanged();
        mDynamicItemAdapter.notifyItemChanged(this.lists.size() - 1);
        if (status) {
            if (!newPosi.equals(oldPosi)) {
                FancyToast.makeText(getContext(), "刷新成功", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                //轮动到刷新的位置
//                mRecyclerView.scrollToPosition(oldPosi - 1);
            }


        } else {
            if (!newPosi.equals(oldPosi)) {
                FancyToast.makeText(getContext(), "刷新失败", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
            } else {
                FancyToast.makeText(getContext(), "出现了问题", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
            }
        }
    }


    /**
     * 设置上拉刷新
     */
    @Override
    public void onRefresh() {
        oldPosi = newPosi;
        newPosi = newPosi + 1;
        switch (pos) {
            case 0: {
                iDynamicPresenter.getDynamicLostData(new DynamicsRequestBean(0, 100), session);
                break;
            }
            case 1: {
                iDynamicPresenter.getDynamicFindData(new DynamicsRequestBean(0, 100), session);
                break;
            }
            default: {
                break;
            }
        }
    }
}

