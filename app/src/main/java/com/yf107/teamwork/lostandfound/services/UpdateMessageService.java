package com.yf107.teamwork.lostandfound.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;

import com.yf107.teamwork.lostandfound.presenter.MessagePresenter;
import com.yf107.teamwork.lostandfound.bean.DynamicItemBean;
import com.yf107.teamwork.lostandfound.bean.UpDateMessageBean;
import com.yf107.teamwork.lostandfound.event.MessageEvent;
import com.yf107.teamwork.lostandfound.view.interfaces.IMessageFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.yf107.teamwork.lostandfound.view.activity.SignInActivity.EMAIL;
import static com.yf107.teamwork.lostandfound.view.activity.SignInActivity.SESSION;
import static com.yf107.teamwork.lostandfound.view.activity.SignInActivity.STU;
import static com.yf107.teamwork.lostandfound.view.activity.SignInActivity.USERPHOTO;

/**
 * Description: 更新消息的后台服务，服务只能绑定在activity上
 */
public class UpdateMessageService extends Service implements IMessageFragment {

    private MessagePresenter messagePresenter;

    private UpdateMessageListenser updateMessageListenser;
    private MsgBinder msgBinder=new MsgBinder();
    private ArrayList<Integer> arrayList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        messagePresenter=new MessagePresenter(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, START_STICKY_COMPATIBILITY, startId);
    }

    /**
     * 开始服务
     */
    public void startService() {
        //每隔30秒轮询
        Disposable disposable = Observable.interval(30,30, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        SharedPreferences sharedPreferences = getSharedPreferences("users", Context.MODE_PRIVATE);
                        String username = sharedPreferences.getString(EMAIL,"null");
                        String userphoto = sharedPreferences.getString(USERPHOTO,"null");
                        String usernickname = sharedPreferences.getString(STU,"null");
                        String jsessionid = sharedPreferences.getString(SESSION,"null");
                        messagePresenter.getMessageData(usernickname,userphoto,username,jsessionid,0,500);
                    }
                });
    }

    //messagePresenter的数据回调
    @Override
    public void onDataBack(Boolean status, List<UpDateMessageBean.DynamicsBeanX> dynamicItemBeanList) {
        if(updateMessageListenser!=null){
            updateMessageListenser.UpdateMessage(new MessageEvent(status,dynamicItemBeanList));
            for(int i = 0;i<dynamicItemBeanList.size();i++){
                arrayList.add(dynamicItemBeanList.get(i).getRead());
            }
        }
    }

    @Override
    public void isRead(boolean status) {

    }

    @Override
    public void showStatus(Boolean seesion) {

    }

    public void setUpdateMessageListenser(UpdateMessageListenser listenser){
        this.updateMessageListenser=listenser;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return msgBinder;
    }

    public class MsgBinder extends Binder{
        public UpdateMessageService getService(){
            return UpdateMessageService.this;
        }
    }

    public interface UpdateMessageListenser{
        void UpdateMessage(MessageEvent messageEvent);
    }

    public void getstatus(Boolean b){
        for(int i = 0;i<arrayList.size();i++){
            if(arrayList.get(i) == 0){
                b = true;
                break;
            }else{
                b = false;
            }
        }
    }
}
