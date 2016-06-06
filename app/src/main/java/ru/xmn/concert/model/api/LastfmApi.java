package ru.xmn.concert.model.api;

import java.io.IOException;
import java.util.Locale;

import de.umass.lastfm.Artist;
import de.umass.lastfm.ImageSize;
import ru.xmn.concert.model.data.BandLastfm;
import rx.Observable;
import rx.schedulers.Schedulers;

public class LastfmApi {
    private final String key = "2bfb15fbcefb91f170233c99ff4af225";      // api key
    private final String secret = "b8c42600c5d31faa6d282cb5f104b9b9";   // api secret
    Locale dLocale = new Locale("ru", "RU");

    public Observable<BandLastfm> getBandInfo(String bandName) throws IOException {
        return Observable
                .just(Artist.getInfo(bandName, dLocale, "pe-psy", key))
                .observeOn(Schedulers.io())
                .flatMap(artist -> Observable.just(new BandLastfm(artist.getName(),
                        artist.getWikiSummary(),
                        artist.getImageURL(ImageSize.EXTRALARGE))))
                ;
    }
}
