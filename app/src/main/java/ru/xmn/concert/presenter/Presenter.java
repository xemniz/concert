package ru.xmn.concert.presenter;

import android.os.Parcel;
import android.os.Parcelable;

import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiAudio;
import com.vk.sdk.api.model.VKList;

import ru.xmn.concert.model.ConcertsModel;
import ru.xmn.concert.model.data.Band;
import ru.xmn.concert.model.data.EventGig;
import ru.xmn.concert.view.MainView;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by xmn on 19.05.2016.
 */
public class Presenter {
    ConcertsModel concertsModel = new ConcertsModel();
    private MainView mainView;

    public Presenter(MainView mainView) {
        this.mainView = mainView;
    }

    private Subscription subscription = Subscriptions.empty();

//    public void eventList(String band) {
//        if (!subscription.isUnsubscribed()) {
//            subscription.unsubscribe();
//        }
//
//        subscription = concertsModel.eventList(band)
//                .subscribe(new Observer<List<EventGig>>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onNext(List<EventGig> data) {
//                        if (data != null && !data.isEmpty()) {
//                            mainView.showData(data);
//                        } else {
//                        }
//                    }
//                });
//    }

    public void bandList() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        System.out.println("");

        subscription = concertsModel
                .getBandsGigsVk()
                .subscribe(new Observer<Set<Band>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Set<Band> data) {
                        if (data != null && !data.isEmpty()) {
                            mainView.showData(data);
                        } else {
                        }
                    }
                });
    }


}
