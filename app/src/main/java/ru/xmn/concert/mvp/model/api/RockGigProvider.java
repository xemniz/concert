package ru.xmn.concert.mvp.model.api;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import ru.xmn.concert.Application;
import ru.xmn.concert.mvp.model.data.BandRealm;
import ru.xmn.concert.mvp.model.data.Event;
import ru.xmn.concert.mvp.model.data.EventGig;
import ru.xmn.concert.mvp.model.data.EventRockGig;
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

    public Observable<List<EventRockGig>> getEventsRockGig() {
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
                    Type listType = new TypeToken<ArrayList<EventRockGig>>() {
                    }.getType();
                    List<EventRockGig> eventRockGigs = mGson.fromJson(s, listType);
                    return Observable.just(eventRockGigs);
                });
    }

    //region Deprecated
    public List<EventGig> eventsByBand(final String band) throws IOException {
        List<EventGig> gigsList = new ArrayList<>();
        return gigsList;
    }

    private String unZip(String feedId) throws Exception {
        File file = new File(Application.get().getBaseContext().getCacheDir(), "feed" + feedId);
        File unzipped = new File(file, "appfeed" + feedId + ".json");

        if (!unzipped.exists()) {
            UnzipUtils.unpackArchive(new URL("http://rockgig.net/feed/appfeed" + feedId + ".zip"), file);
            gigsToRealm(UnzipUtils.getStringFromFile(unzipped.getPath()));
        }
        return UnzipUtils.getStringFromFile(unzipped.getPath());
    }

    public List<String> findBands(final String band) throws IOException {
        List<String> bandsList = new ArrayList<>();
        return bandsList;
    }

    public Observable<Boolean> eventRgToRealm() {
        Retrofit builder = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl("http://rockgig.net/feed/")
                .build();
        Appfeed appfeed = builder.create(Appfeed.class);
        return appfeed.id()
                .map(s -> {
                    try {
                        return unZip(s);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .flatMap(s -> {
                    if (Realm.getDefaultInstance().where(BandRealm.class).count() < 1)
                        return Observable.just(gigsToRealm(s));
                    else
                        return Observable.just(true);
                });
    }

    private boolean gigsToRealm(String s) {
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<EventRockGig>>() {
        }.getType();
        List<EventRockGig> eventRockGigs = gson.fromJson(s, listType);
        for (EventRockGig eventRockGig :
                eventRockGigs) {
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(new Event(eventRockGig)));
            realm.close();
            Log.d(getClass().getSimpleName(), eventRockGig.getName());
        }
        return true;
    }
    //endregion
}
