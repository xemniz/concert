package ru.xmn.concert.mvp.model.api;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiAudio;
import com.vk.sdk.api.model.VKApiCommunity;
import com.vk.sdk.api.model.VKList;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class VkApiBridge {
//@RxLogObservable(RxLogObservable.Scope.EVERYTHING)
    public Observable<List<String>> bandList() {
        return Observable
                .create(new Observable.OnSubscribe<VKResponse>() {
                    @Override
                    public void call(final Subscriber<? super VKResponse> subscriber) {
                        VKApi.audio().get().executeSyncWithListener(new VKRequest.VKRequestListener() {
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
                                subscriber.onError(new Exception(error.toString()));
                            }

                            @Override
                            public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
                                super.onProgress(progressType, bytesLoaded, bytesTotal);
                            }
                        });
                    }
                })
                .flatMap(vkResponse -> Observable.from((VKList<VKApiAudio>) vkResponse.parsedModel))
                .flatMap(vkApiAudio -> Observable.just(vkApiAudio.artist))
                .map(String::trim)
                .distinct()
                .toList()
                .single();
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