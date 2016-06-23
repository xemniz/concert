package ru.xmn.concert.model.api;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiAudio;
import com.vk.sdk.api.model.VKApiCommunity;
import com.vk.sdk.api.model.VKApiCommunityArray;
import com.vk.sdk.api.model.VKList;

import java.util.ArrayList;
import java.util.List;

import io.realm.internal.Group;
import ru.xmn.concert.model.data.EventGig;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class VkApiBridge {
//    VKList<VKApiAudio> list;

//    public List<String> bandList() {
//        VKApi.audio().get().executeWithListener(new VKRequest.VKRequestListener() {
//            @Override
//            public void onComplete(VKResponse response) {
//                super.onComplete(response);
//                list = (VKList<VKApiAudio>) response.parsedModel;
//
//
//            }
//
//            @Override
//            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
//                super.attemptFailed(request, attemptNumber, totalAttempts);
//                System.out.println("attemptFailed");
//            }
//
//            @Override
//            public void onError(VKError error) {
//                super.onError(error);
//                System.out.println(error.toString());
//            }
//
//            @Override
//            public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
//                super.onProgress(progressType, bytesLoaded, bytesTotal);
//            }
//        });
//        return Observable
//                .from(list)
//                .flatMap(vkApiAudio -> Observable.just(vkApiAudio.artist))
//                .distinct()
//                .map(s -> s.trim().toLowerCase())
//                .toList()
//                .toBlocking()
//                .single();
//
//    }

    public Observable<List<String>> bandList() {
        System.out.println("INVKApIBRIDGE THREAD IS " + Thread.currentThread().getName());
        return Observable
                .create(new Observable.OnSubscribe<VKResponse>() {
                    @Override
                    public void call(final Subscriber<? super VKResponse> subscriber) {
                        VKApi.audio().get().executeWithListener(new VKRequest.VKRequestListener() {
                            @Override
                            public void onComplete(VKResponse response) {
                                super.onComplete(response);
                                System.out.println("INVKApIBRIDGE_INONCOMPLETE " + Thread.currentThread().getName()+((VKList<VKApiAudio>) response.parsedModel).size());

                                subscriber.onNext(response);
                                subscriber.onCompleted();
                            }

                            @Override
                            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                                super.attemptFailed(request, attemptNumber, totalAttempts);
                                System.out.println("attemptFailed");
                                subscriber.onError(new Exception("attemptFailed"));
                                subscriber.onCompleted();
                            }

                            @Override
                            public void onError(VKError error) {
                                super.onError(error);
                                System.out.println(error.toString());
                                subscriber.onError(new Exception(error.toString()));
                                subscriber.onCompleted();
                            }

                            @Override
                            public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
                                super.onProgress(progressType, bytesLoaded, bytesTotal);
                                System.out.println("ONPROGRESS!");
                                subscriber.onCompleted();
                            }
                        });
                    }
                })
                .concatMap(vkResponse -> Observable.from((VKList<VKApiAudio>) vkResponse.parsedModel))
                .concatMap(vkApiAudio -> Observable.just(vkApiAudio.artist))
                .distinct()
                .map(s -> s.trim().toLowerCase())
                .map(s -> {
                    System.out.println(s);
                    return s;
                })
                .toList();
    }

    public Observable<String> setImage(String bandvk) {
        VKParameters params = new VKParameters();
        params.put(VKApiConst.GROUP_ID, bandvk);
        return Observable
                .create(new Observable.OnSubscribe<VKResponse>() {
                    @Override
                    public void call(final Subscriber<? super VKResponse> subscriber) {
                        VKApi.groups().getById(params)
                                .executeWithListener(new VKRequest.VKRequestListener() {
                                    @Override
                                    public void onComplete(VKResponse response) {
                                        super.onComplete(response);
                                        subscriber.onNext(response);
                                        subscriber.onCompleted();
                                    }

                                    @Override
                                    public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                                        super.attemptFailed(request, attemptNumber, totalAttempts);
                                        System.out.println("attemptFailed");
                                        subscriber.onError(new Exception("attemptFailed"));
                                    }

                                    @Override
                                    public void onError(VKError error) {
                                        super.onError(error);
                                        System.out.println(error.toString());
//                                        subscriber.onError(new Exception(error.toString()));
                                    }

                                    @Override
                                    public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
                                        super.onProgress(progressType, bytesLoaded, bytesTotal);
                                        System.out.println("ONPROGRESS!");
                                    }
                                });
                    }
                })
                .flatMap(vkResponse -> Observable.just(((VKList<VKApiCommunity>) vkResponse.parsedModel)))
                .flatMap(vkApiCommunities -> Observable.just(vkApiCommunities.get(0).photo_200))
                .map(s -> {
                    System.out.println(s);
                    return s;
                })
                .single();
    }
}