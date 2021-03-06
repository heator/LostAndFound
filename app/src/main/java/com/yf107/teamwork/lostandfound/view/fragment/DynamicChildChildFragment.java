package com.yf107.teamwork.lostandfound.view.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.shashank.sony.fancytoastlib.FancyToast;
import com.yf107.teamwork.lostandfound.services.ActivityManager;
import com.yf107.teamwork.lostandfound.view.interfaces.IDynaicChildFragment;
import com.yf107.teamwork.lostandfound.R;
import com.yf107.teamwork.lostandfound.adapter.DynamicChildItemAdapter;
import com.yf107.teamwork.lostandfound.bean.DynamicItemBean;
import com.yf107.teamwork.lostandfound.bean.DynamicsRequestBean;
import com.yf107.teamwork.lostandfound.presenter.DynamicChildPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Description
 * 动态板块子页面（分为今天/昨天/更早）
 * @updateDes 交错layout
 */

@SuppressLint("ValidFragment")
public class DynamicChildChildFragment extends Fragment implements IDynaicChildFragment, SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout refreshLayout;
    private DynamicChildItemAdapter mDynamicItemAdapter;



    private List<DynamicItemBean> lists = new ArrayList<>();
    private DynamicChildPresenter iDynamicChildPresenter;
    private int pos;
    private SharedPreferences sharedPreferences;

    private String session = "";
    //控制数量
    private Integer newPosi;
    private Integer oldPosi;

    @SuppressLint("ValidFragment")
    public DynamicChildChildFragment(int i, String session) {
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
        mDynamicItemAdapter = new DynamicChildItemAdapter(lists, getActivity());
        mRecyclerView.setAdapter(mDynamicItemAdapter);
       refreshLayout.setOnRefreshListener(this);

//        smartRefreshLayout.setRefreshFooter(new BallPulseFooter(getContext()).setSpinnerStyle(SpinnerStyle.Scale));
//
//        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(RefreshLayout refreshLayout) {
//
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        oldPosi = newPosi;
//                        newPosi = newPosi + 15;
//                        switch (pos) {
//                            case 2: {
//                                Log.e("LostToday","LostToday刷新");
//                                iDynamicChildPresenter.getDynamicLostTodayData(new DynamicsRequestBean(0, newPosi), session);
//                                break;
//                            }
//                            case 3: {
//                                Log.e("LostYesterday","LostYesterday刷新");
//                                iDynamicChildPresenter.getDynamicLostYesterdayData(new DynamicsRequestBean(0, newPosi), session);
//                                break;
//                            }
//                            case 4: {
//                                Log.e("LostAgo","LostAgo刷新");
//                                iDynamicChildPresenter.getDynamicLostAgoData(new DynamicsRequestBean(0, newPosi), session);
//                                break;
//                            }
//                            case 5: {
//                                Log.e("FindToday","FindToday刷新");
//                                iDynamicChildPresenter.getDynamicFindTodayData(new DynamicsRequestBean(0, newPosi), session);
//                                break;
//                            }
//                            case 6: {
//                                Log.e("FindYesterday","FindYesterday刷新");
//                                iDynamicChildPresenter.getDynamicFindYesterdayData(new DynamicsRequestBean(0, newPosi), session);
//                                break;
//                            }
//                            case 7: {
//                                Log.e("FindAgo","FindAgo刷新");
//                                iDynamicChildPresenter.getDynamicFindAgoData(new DynamicsRequestBean(0, newPosi ), session);
//                                break;
//                            }
//                            default: {
//                                break;
//                            }
//                        }
//                    }
//                },2000);
//            }
//        });
//
//        smartRefreshLayout.setEnableLoadMore(true);
//        smartRefreshLayout.autoRefresh();
        iDynamicChildPresenter = new DynamicChildPresenter();
        iDynamicChildPresenter.attachActivity(this);
        initLists();
        return view;
    }

    public void initLists() {
        switch (pos) {
            case 2: {
                Log.e("LostToday","LostToday");
                iDynamicChildPresenter.getDynamicLostTodayData(new DynamicsRequestBean(0, 15), session);
                break;
            }
            case 3: {
                Log.e("LostYesterday","LostYesterday");
                iDynamicChildPresenter.getDynamicLostYesterdayData(new DynamicsRequestBean(0, 15), session);
                break;
            }
            case 4: {
                Log.e("LostAgo","LostAgo");
                iDynamicChildPresenter.getDynamicLostAgoData(new DynamicsRequestBean(0, 15), session);
                break;
            }
            case 5: {
                Log.e("FindToday","FindToday");
                iDynamicChildPresenter.getDynamicFindTodayData(new DynamicsRequestBean(0, 15), session);
                break;
            }
            case 6: {
                Log.e("FindYesterday","FindYesterday");
                iDynamicChildPresenter.getDynamicFindYesterdayData(new DynamicsRequestBean(0, 15), session);
                break;
            }
            case 7: {
                Log.e("FindAgo","FindAgo");
                iDynamicChildPresenter.getDynamicFindAgoData(new DynamicsRequestBean(0, 15), session);
                break;
            }
            default: {
                break;
            }
        }
    }

    @Override
    public void onDestroyView() {
        iDynamicChildPresenter.dettachActivity();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        iDynamicChildPresenter.dettachActivity();
        super.onDestroy();
    }

    @Override
    public void onResume() { // 页面恢复
        super.onResume();
        initLists();
    }

    @Override
    public void showData(Boolean status, List<DynamicItemBean> searchItemBeanArrayList) {
        refreshLayout.setRefreshing(false);
        lists.clear();
        lists.addAll(searchItemBeanArrayList);
            mDynamicItemAdapter.notifyDataSetChanged();
        mDynamicItemAdapter.notifyItemChanged(this.lists.size() - 1);
        if (status) {
            if (!newPosi.equals(oldPosi)) {
      //          FancyToast.makeText(getContext(), "刷新成功", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
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
        newPosi = newPosi + 15;
        switch (pos) {
            case 2: {
                Log.e("LostToday","LostToday刷新");
                iDynamicChildPresenter.getDynamicLostTodayData(new DynamicsRequestBean(0, 100), session);
                break;
            }
            case 3: {
                Log.e("LostYesterday","LostYesterday刷新");
                iDynamicChildPresenter.getDynamicLostYesterdayData(new DynamicsRequestBean(0, 100), session);
                break;
            }
            case 4: {
                Log.e("LostAgo","LostAgo刷新");
                iDynamicChildPresenter.getDynamicLostAgoData(new DynamicsRequestBean(0, 100), session);
                break;
            }
            case 5: {
                Log.e("FindToday","FindToday刷新");
                iDynamicChildPresenter.getDynamicFindTodayData(new DynamicsRequestBean(0, 100), session);
                break;
            }
            case 6: {
                Log.e("FindYesterday","FindYesterday刷新");
                iDynamicChildPresenter.getDynamicFindYesterdayData(new DynamicsRequestBean(0, 100), session);
                break;
            }
            case 7: {
                Log.e("FindAgo","FindAgo刷新");
                iDynamicChildPresenter.getDynamicFindAgoData(new DynamicsRequestBean(0, 100), session);
                break;
            }
            default: {
                break;
            }
        }
    }
}
