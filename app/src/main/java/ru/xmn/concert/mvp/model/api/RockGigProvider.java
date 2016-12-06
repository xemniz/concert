package ru.xmn.concert.mvp.model.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import ru.xmn.concert.mvp.model.data.Event;
import ru.xmn.concert.mvp.model.utils.UnzipUtils;
import rx.Observable;


public class RockGigProvider {
    private Gson mGson;
    private Appfeed mAppfeed;
    private Retrofit mBuilder;

    public RockGigProvider() {
        mGson = new Gson();

        mBuilder = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl("http://rockgig.net/feed/")
                .build();

        mAppfeed = mBuilder.create(Appfeed.class);
    }

    public Observable<List<Event>> getEventsRockGig() {
        return mAppfeed.id()
                .map(s -> {
                    try {
                        return UnzipUtils.getStringFromZipUrl(s);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .flatMap(s -> {
                    Type listType = new TypeToken<ArrayList<Event>>() {
                    }.getType();
                    List<Event> eventRockGigs = mGson.fromJson(s, listType);
                    return Observable.just(eventRockGigs);
                });
    }

    //region Deprecated
    public List<Event> eventsByBand(final String band) throws IOException {
        List<Event> gigsList = new ArrayList<>();
        return gigsList;
    }
    //endregion
}
