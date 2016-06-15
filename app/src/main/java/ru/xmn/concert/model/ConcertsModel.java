package ru.xmn.concert.model;

import com.vk.sdk.api.model.VKApiAudio;
import com.vk.sdk.api.model.VKList;

import ru.xmn.concert.JobExecutor;
import ru.xmn.concert.model.api.LastfmApi;
import ru.xmn.concert.model.api.RockGigApi;
import ru.xmn.concert.model.api.VkApiBridge;
import ru.xmn.concert.model.data.BandRockGig;
import ru.xmn.concert.model.data.EventGig;
import ru.xmn.concert.model.data.EventRockGig;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConcertsModel {
    RockGigApi rockGigApi = new RockGigApi();
    LastfmApi lastfmApi = new LastfmApi();
    VKList<VKApiAudio> list;
    List<BandRockGig> gigsVkRockgig = new ArrayList<>();

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


    public Observable<List<BandRockGig>> getBandsGigsVk(boolean isRefreshing) {

        System.out.println("INCONCERTSMODEL " + Thread.currentThread().getName() + " gigsVkRockgig " + gigsVkRockgig.size());
        VkApiBridge vkApiBridge = new VkApiBridge();
//        List<String> vkAudioList = vkApiBridge.bandList();
        List<BandRockGig> tmpGigsVkRockGig = new ArrayList<>();
        tmpGigsVkRockGig.addAll(gigsVkRockgig);
        if (isRefreshing) {
        gigsVkRockgig = new ArrayList<BandRockGig>() {};
        return Observable
                .zip(vkApiBridge.bandList(), rockGigApi.getEventsRockGig(), (strings, rockGigEvents) -> {
                    System.out.println("CONCMODEL COMBLATEST " + rockGigEvents.size());
                    for (EventRockGig event : rockGigEvents) {
                        for (BandRockGig bandRockGig : event.getBandRockGigs()) {
                            if (strings.contains(bandRockGig.getBand().trim().toLowerCase())) {

                                boolean isBandInList = false;
                                for (BandRockGig bandRockGigInList :
                                        gigsVkRockgig) {
                                    if (bandRockGigInList.equals(bandRockGig)){
                                        bandRockGigInList.getGigs().add(event);
                                        isBandInList = true;
                                    }
                                }
                                if (!isBandInList){
                                    bandRockGig.getGigs().add(event);
                                    gigsVkRockgig.add(bandRockGig);
                                }

                                try {
                                    System.out.println("CONCERTMODEL THREAD IS " + Thread.currentThread().getName());
                                    lastfmApi.getBandInfo(bandRockGig.getBand())
                                            .subscribe(bandLastfm -> bandRockGig.setBandImageUrl(bandLastfm.getImageUrl()));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    System.out.println("CONCERTMODEL BEFORERETURN " + gigsVkRockgig.size());
                    return gigsVkRockgig;
                }).observeOn(Schedulers.io());
        } else {
            return Observable.just(tmpGigsVkRockGig);
        }

    }

    public Observable<List<EventRockGig>> getAllRockGigEvents (int PAGE, int IT_ON_PAGE) {
        return rockGigApi.getEventsRockGig()
                .flatMap(rockGigEvents -> Observable.from(rockGigEvents))
                .skip(IT_ON_PAGE*PAGE)
                .take(IT_ON_PAGE)
                .map(rockGigEvent -> {
                    for (BandRockGig bandRockGig : rockGigEvent.getBandRockGigs()) {
                        try {
                            lastfmApi.getBandInfo(bandRockGig.getBand())
                                    .subscribe(bandLastfm -> {
                                        bandRockGig.setBandImageUrl(bandLastfm.getImageUrl());
                                        System.out.println("BAND URL ." + bandLastfm.getImageUrl()+".");
                                        if (bandRockGig.getBandImageUrl().length()<3&& bandRockGig.getBandImageUrl().equals("")) {
                                            bandRockGig.setBandImageUrl("http://blog.songcastmusic.com/wp-content/uploads/2013/08/iStock_000006170746XSmall.jpg");
                                        }
                                        System.out.println("BAND URL " + bandLastfm.getImageUrl());
                                    });
                        } catch (IOException e) {
                            e.printStackTrace();
                            bandRockGig.setBandImageUrl("http://p4cdn4static.sharpschool.com/UserFiles/Servers/Server_91869/Image/Band4.jpg");
                        }
                    }
                    return rockGigEvent;
                })
                .toList();
    }
}
