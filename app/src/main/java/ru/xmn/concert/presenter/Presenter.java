package ru.xmn.concert.presenter;

import android.os.Parcel;
import android.os.Parcelable;

import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiAudio;
import com.vk.sdk.api.model.VKList;

import ru.xmn.concert.model.ConcertsModel;
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

    public void eventList(String band) {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }

        subscription = concertsModel.eventList(band)
                .subscribe(new Observer<List<EventGig>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<EventGig> data) {
                        if (data != null && !data.isEmpty()) {
                            mainView.showData(data);
                        } else {
                        }
                    }
                });
    }
    public void bandList(){
        concertsModel.bandList().executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                VKList<VKApiAudio> list =(VKList<VKApiAudio>) response.parsedModel;
                concertsModel.setList(list);

//                Observable
//                        .from(list)
//                        .debounce(3, TimeUnit.SECONDS)
//                        .subscribeOn(Schedulers.io())
//                        .subscribe(vkApiAudio -> eventList(vkApiAudio.artist));

                Observable
                        .from(list)
                        .flatMap(new Func1<VKApiAudio, Observable<String>>() {
                            @Override
                            public Observable<String> call(VKApiAudio vkApiAudio) {
                                return Observable.just(vkApiAudio.artist);
                            }
                        })
                        .distinct()
                        .flatMap(s -> concertsModel.eventList(s))
//                        .take(20)
//                        .flatMap(vkApiAudio -> concertsModel.eventList(vkApiAudio.artist))
//                        .concatMap(vkApiAudio -> concertsModel.eventList(vkApiAudio.artist))
//                        .debounce(3, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<EventGig>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(List<EventGig> data) {
                                if (data != null && !data.isEmpty()) {
                                    mainView.showData(data);
                                } else {
                                }
                            }
                        });
            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                super.attemptFailed(request, attemptNumber, totalAttempts);
                System.out.println("attemptFailed");
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                System.out.println(error.toString());
            }

            @Override
            public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
                super.onProgress(progressType, bytesLoaded, bytesTotal);
            }
        });
    }


}
