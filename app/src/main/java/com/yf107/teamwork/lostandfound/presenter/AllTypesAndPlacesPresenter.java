package com.yf107.teamwork.lostandfound.presenter;

import com.yf107.teamwork.lostandfound.bean.AllPlacesBean;
import com.yf107.teamwork.lostandfound.bean.AllTypesBean;
import com.yf107.teamwork.lostandfound.model.AllPlacesModel;
import com.yf107.teamwork.lostandfound.model.AllTypesModel;
import com.yf107.teamwork.lostandfound.model.interfaces.IAllPlacesModel;
import com.yf107.teamwork.lostandfound.model.interfaces.IAllTypesModel;
import com.yf107.teamwork.lostandfound.presenter.interfaces.IAllTypesAndPlacesPresenter;
import com.yf107.teamwork.lostandfound.view.interfaces.IAllTypesAndPlaces;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.yf107.teamwork.lostandfound.view.activity.MainActivity.FINE_INTERNET_STATUS;

public class AllTypesAndPlacesPresenter extends AbstractBasePresenter<IAllTypesAndPlaces> implements IAllTypesAndPlacesPresenter {

    private IAllTypesModel iAllTypesModel;
    private IAllPlacesModel iAllPlacesModel;

    public AllTypesAndPlacesPresenter(IAllTypesAndPlaces iAllTypesAndPlaces){
        super(iAllTypesAndPlaces);
    }


    @Override
    public void getAllTypesAndPlaces(String session) {
        iAllPlacesModel = new AllPlacesModel();
        iAllTypesModel = new AllTypesModel();
        iAllTypesModel.getAllTypes(session, new Observer<AllTypesBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(AllTypesBean allTypesBean) {
                if (allTypesBean == null || !FINE_INTERNET_STATUS.equals(allTypesBean.getStatus())) {
                    v.getIAllTypesAndPlaces(false,null,null);
                }else {

                    iAllPlacesModel.getAllPlaces(session, new Observer<AllPlacesBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(AllPlacesBean allPlacesBean) {
                            if (allPlacesBean == null || !FINE_INTERNET_STATUS.equals(allPlacesBean.getStatus())) {
                                v.getIAllTypesAndPlaces(false,null,null);
                            }else {
                                v.getIAllTypesAndPlaces(true,allTypesBean.getTypes(),allPlacesBean.getTypes());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
