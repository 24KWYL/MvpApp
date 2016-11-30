package com.dl7.mvp.module.news.home;

import com.dl7.mvp.local.table.NewsTypeBean;
import com.dl7.mvp.local.table.NewsTypeBeanDao;
import com.dl7.mvp.module.base.IRxBusPresenter;
import com.dl7.mvp.rxbus.RxBus;
import com.orhanobut.logger.Logger;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by long on 2016/9/1.
 * 主页 Presenter
 */
public class MainPresenter implements IRxBusPresenter {

    private final IMainView mView;
    private final NewsTypeBeanDao mDbDao;
    private final RxBus mRxBus;

    public MainPresenter(IMainView view, NewsTypeBeanDao dbDao, RxBus rxBus) {
        mView = view;
        mDbDao = dbDao;
        mRxBus = rxBus;
    }

    @Override
    public void getData() {
        mDbDao.queryBuilder().rx().list()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<NewsTypeBean>>() {
                    @Override
                    public void call(List<NewsTypeBean> newsTypeBeen) {
                        mView.loadData(newsTypeBeen);
                    }
                });
    }

    @Override
    public void getMoreData() {
    }

//    @Override
//    public <T> void registerRxBus(Class<T> eventType) {
//        Subscription subscription = mRxBus.doSubscribe(eventType, new Action1<T>() {
//            @Override
//            public void call(T t) {
//                getData();
//            }
//        }, new Action1<Throwable>() {
//            @Override
//            public void call(Throwable throwable) {
//
//            }
//        });
//        mRxBus.addSubscription(this, subscription);
//    }

    @Override
    public <T> void registerRxBus(Class<T> eventType, Action1<T> action) {
        Subscription subscription = mRxBus.doSubscribe(eventType, action, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Logger.e(throwable.toString());
            }
        });
        mRxBus.addSubscription(this, subscription);
    }

    @Override
    public void unregisterRxBus() {
        mRxBus.unSubscribe(this);
    }
}