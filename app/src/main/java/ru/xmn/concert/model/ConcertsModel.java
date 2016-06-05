package ru.xmn.concert.model;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.model.VKApiAudio;
import com.vk.sdk.api.model.VKList;

import ru.xmn.concert.JobExecutor;
import ru.xmn.concert.model.api.LastfmApi;
import ru.xmn.concert.model.api.RockGigApi;
import ru.xmn.concert.model.api.VkApiBridge;
import ru.xmn.concert.model.data.Band;
import ru.xmn.concert.model.data.BandLastfm;
import ru.xmn.concert.model.data.EventGig;
import ru.xmn.concert.model.data.RockGigEvent;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConcertsModel {
    RockGigApi rockGigApi = new RockGigApi();
    LastfmApi lastfmApi = new LastfmApi();
    VKList<VKApiAudio> list;

    public VKList<VKApiAudio> getList() {
        return list;
    }

    public void setList(VKList<VKApiAudio> list) {
        this.list = list;
    }

    public Observable eventList(final String band) {
        System.out.println(band+" in eventlist");

        return Observable
                .create(new Observable.OnSubscribe<List<EventGig>>() {
                    @Override
                    public void call(Subscriber<? super List<EventGig>> subscriber) {
                        try {
                            subscriber.onNext(rockGigApi.eventsByBand(band));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .subscribeOn(Schedulers.from(JobExecutor.getInstance()));
//                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable bandList(final String band) {
        return Observable
                .create(new Observable.OnSubscribe<List<String>>() {
                    @Override
                    public void call(Subscriber<? super List<String>> subscriber) {
                        try {
                            subscriber.onNext(rockGigApi.findBands(band));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                ;
    }

    public Observable getArtistInfo(final String band) {
        return Observable
                .create(new Observable.OnSubscribe<BandLastfm>() {
                    @Override
                    public void call(Subscriber<? super BandLastfm> subscriber) {
                        try {
                            subscriber.onNext(lastfmApi.getBandInfo(band));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }



    public Observable<Set<Band>> getBandsGigsVk (){
        Set<Band> gigsVkRockgig = new HashSet<Band>() {};
        System.out.println("INCONCERTSMODEL");
        VkApiBridge vkApiBridge = new VkApiBridge();
        List<String> vkAudioList = vkApiBridge.bandList();
        return rockGigApi.getEventsRockGig()
                .flatMap(rockGigEvents -> {
                    for (RockGigEvent event :
                            rockGigEvents) {
                        for (Band band :
                                event.getBands()) {
                            if (vkAudioList.contains(band.getBand().trim().toLowerCase())) {
                                band.getGigs().add(event);
                                System.out.println(band.getBand());
                                gigsVkRockgig.add(band);
                                try {
                                    band.setBandImageUrl(lastfmApi.getBandInfo(band.getBand()).getImageUrl());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    return Observable.just(gigsVkRockgig);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
