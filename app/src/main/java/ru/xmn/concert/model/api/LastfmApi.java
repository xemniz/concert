package ru.xmn.concert.model.api;

import java.io.IOException;
import java.util.Locale;

import de.umass.lastfm.Artist;
import de.umass.lastfm.ImageSize;
import ru.xmn.concert.model.data.BandLastfm;


public class LastfmApi {
    private final String key = "2bfb15fbcefb91f170233c99ff4af225";      // api key
    private final String secret = "b8c42600c5d31faa6d282cb5f104b9b9";   // api secret
    Locale dLocale = new Locale("ru", "RU");

    public BandLastfm getBandInfo(String bandName) throws IOException {

        Artist artistLFMAPI = Artist.getInfo(bandName,dLocale,"pe-psy",  key);
//        System.out.println(artistLFMAPI.getWikiSummary());
//        Document doc = Jsoup.connect("http://www.last.fm/ru/music/" + bandName.replaceAll(" ", "+")+"/+wiki")
//                .get();
        return new BandLastfm(artistLFMAPI.getName(), artistLFMAPI.getWikiSummary() , artistLFMAPI.getImageURL(ImageSize.EXTRALARGE));
//        return new BandLastfm(artistLFMAPI.getName(), doc.getElementsByClass("wiki-content").text() , artistLFMAPI.getImageURL(ImageSize.EXTRALARGE));
    }

}
