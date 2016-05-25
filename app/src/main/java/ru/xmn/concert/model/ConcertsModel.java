package ru.xmn.concert.model;

import de.umass.lastfm.Artist;
import ru.xmn.concert.model.api.LastfmApi;
import ru.xmn.concert.model.api.RockGigApi;
import ru.xmn.concert.model.data.EventGig;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.io.IOException;
import java.util.List;

public class ConcertsModel {
    RockGigApi rockGigApi = new RockGigApi();
    LastfmApi lastfmApi = new LastfmApi();

    public Observable eventList(final String band) {
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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
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
                .create(new Observable.OnSubscribe<Artist>() {
                    @Override
                    public void call(Subscriber<? super Artist> subscriber) {
                            subscriber.onNext(lastfmApi.getBandInfo(band));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
