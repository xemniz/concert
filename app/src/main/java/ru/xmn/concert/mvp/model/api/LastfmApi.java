package ru.xmn.concert.mvp.model.api;

import java.io.IOException;
import java.util.Locale;

import de.umass.lastfm.Artist;
import de.umass.lastfm.ImageSize;
import ru.xmn.concert.mvp.model.data.BandLastfm;
import rx.Observable;
import rx.schedulers.Schedulers;

public class LastfmApi {
    private final String key = "2bfb15fbcefb91f170233c99ff4af225";      // api key
    private final String secret = "b8c42600c5d31faa6d282cb5f104b9b9";   // api secret
    Locale dLocale = new Locale("ru", "RU");
    static public final String DEFAULT_PIC = "http://blog.songcastmusic.com/wp-content/uploads/2013/08/iStock_000006170746XSmall.jpg";

    public Observable<BandLastfm> getBandInfoObs(String bandName) throws IOException {
        return Observable
                .just(Artist.getInfo(bandName, dLocale, "pe-psy", key))
                .observeOn(Schedulers.io())
                .flatMap(artist -> Observable.just(new BandLastfm(artist.getName(),
                        artist.getWikiSummary(),
                        artist.getImageURL(ImageSize.EXTRALARGE))))
                .onErrorResumeNext(throwable -> Observable.just(new BandLastfm(bandName,
                        " ",
                        DEFAULT_PIC)));
    }

    public BandLastfm getBandInfo(String bandName) {
        Artist artist = Artist.getInfo(bandName, dLocale, "pe-psy", key);
        try {
            if (artist.getImageURL(ImageSize.EXTRALARGE).length() < 1)
                return new BandLastfm(bandName,
                        artist.getWikiSummary(),
                        "http://blog.songcastmusic.com/wp-content/uploads/2013/08/iStock_000006170746XSmall.jpg");
            return new BandLastfm(artist.getName(),
                    artist.getWikiSummary(),
                    artist.getImageURL(ImageSize.EXTRALARGE));
        } catch (Exception e) {
            return new BandLastfm(bandName,
                    " ",
                    "http://blog.songcastmusic.com/wp-content/uploads/2013/08/iStock_000006170746XSmall.jpg");
        }
    }
}
