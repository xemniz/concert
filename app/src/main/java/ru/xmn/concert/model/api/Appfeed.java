package ru.xmn.concert.model.api;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by xmn on 05.06.2016.
 */

public interface Appfeed {
    @GET("appfeed.id")
    Observable<String> id();
}
