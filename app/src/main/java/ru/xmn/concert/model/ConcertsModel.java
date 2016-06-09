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
import rx.functions.Func2;
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
        System.out.println(band + " in eventlist");

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

    public Observable getArtistInfo(final String band) throws IOException {
        return lastfmApi.getBandInfo(band)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<List<Band>> getBandsGigsVk() {
        List<Band> gigsVkRockgig = new ArrayList<Band>() {
        };
        System.out.println("INCONCERTSMODEL " + Thread.currentThread().getName());
        VkApiBridge vkApiBridge = new VkApiBridge();
//        List<String> vkAudioList = vkApiBridge.bandList();
        return Observable
                .zip(vkApiBridge.bandList(), rockGigApi.getEventsRockGig(), (strings, rockGigEvents) -> {
                    System.out.println("CONCMODEL COMBLATEST " + rockGigEvents.size());
                    for (RockGigEvent event : rockGigEvents) {
                        for (Band band : event.getBands()) {
                            if (strings.contains(band.getBand().trim().toLowerCase())) {

                                boolean isBandInList = false;
                                for (Band bandInList :
                                        gigsVkRockgig) {
                                    if (bandInList.equals(band)){
                                        bandInList.getGigs().add(event);
                                        isBandInList = true;
                                    }
                                }
                                if (!isBandInList){
                                    band.getGigs().add(event);
                                    gigsVkRockgig.add(band);
                                }

                                try {
                                    System.out.println("CONCERTMODEL THREAD IS " + Thread.currentThread().getName());
                                    lastfmApi.getBandInfo(band.getBand())
                                            .subscribe(bandLastfm -> band.setBandImageUrl(bandLastfm.getImageUrl()));
                                    System.out.println(band.getBandImageUrl());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    System.out.println("CONCERTMODEL BEFORERETURN " + gigsVkRockgig.size());
                    return gigsVkRockgig;
                }).observeOn(Schedulers.io());
    }
}
