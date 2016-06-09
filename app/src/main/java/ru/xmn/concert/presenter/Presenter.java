package ru.xmn.concert.presenter;

import android.os.Parcel;
import android.os.Parcelable;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiAudio;
import com.vk.sdk.api.model.VKList;

import ru.xmn.concert.model.ConcertsModel;
import ru.xmn.concert.model.data.Band;
import ru.xmn.concert.model.data.EventGig;
import ru.xmn.concert.view.BandView;
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

@InjectViewState
public class Presenter extends MvpPresenter<MainView> {
    ConcertsModel concertsModel = new ConcertsModel();

    private Subscription subscription = Subscriptions.empty();

    public void bandList() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        System.out.println("inPRESENTER");

        subscription = concertsModel
                .getBandsGigsVk()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Band>>() {
                    @Override
                    public void onCompleted() {}
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                    @Override
                    public void onNext(List<Band> data) {
                        if (data != null && !data.isEmpty()) {
                            getViewState().showData(data);
                        }
                    }
                });
    }


    public void closeError()
    {
        getViewState().hideError();
    }
}
