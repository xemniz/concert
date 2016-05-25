package ru.xmn.concert.model.api;

import de.umass.lastfm.Artist;

/**
 * Created by xmn on 24.05.2016.
 */

public class LastfmApi {
    private final String key = "2bfb15fbcefb91f170233c99ff4af225";      // api key
    private final String secret = "b8c42600c5d31faa6d282cb5f104b9b9";   // api secret

    public Artist getBandInfo (String band){

        return Artist.getInfo(band, key);
    }

}
