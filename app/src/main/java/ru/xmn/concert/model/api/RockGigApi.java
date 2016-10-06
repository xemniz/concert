package ru.xmn.concert.model.api;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import ru.xmn.concert.Application;
import ru.xmn.concert.model.data.BandRealm;
import ru.xmn.concert.model.data.EventGig;
import ru.xmn.concert.model.data.EventRealm;
import ru.xmn.concert.model.data.EventRockGig;
import ru.xmn.concert.model.utils.Utils;
import rx.Observable;


public class RockGigApi {

    private RealmApi mRealmApi = new RealmApi();
    Locale dLocale = new Locale("ru", "RU");
    private final String key = "2bfb15fbcefb91f170233c99ff4af225";      // api key
    private final String secret = "b8c42600c5d31faa6d282cb5f104b9b9";   // api secret

    /**
     * @param band
     * @return future events of band
     * @throws IOException
     */
    public List<EventGig> eventsByBand(final String band) throws IOException {
        List<EventGig> gigsList = new ArrayList<>();
        return gigsList;
    }

    public List<String> findBands(final String band) throws IOException {
        List<String> bandsList = new ArrayList<>();
        return bandsList;
    }

    public Observable<List<EventRockGig>> getEventsRockGig() {
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
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<EventRockGig>>() {
                    }.getType();
                    List<EventRockGig> eventRockGigs = gson.fromJson(s, listType);
                    return Observable.just(eventRockGigs);
                });
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
            realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(new EventRealm(eventRockGig)));
            realm.close();
            Log.d(getClass().getSimpleName(), eventRockGig.getName());
        }
        return true;
    }

    private String unZip(String feedId) throws Exception {
        File file = new File(Application.get().getBaseContext().getCacheDir(), "feed" + feedId);
        File unzipped = new File(file, "appfeed" + feedId + ".json");

        if (!unzipped.exists()) {
            Utils.unpackArchive(new URL("http://rockgig.net/feed/appfeed" + feedId + ".zip"), file);
            gigsToRealm(Utils.getStringFromFile(unzipped.getPath()));
            System.out.println("download zip");
        }
        return Utils.getStringFromFile(unzipped.getPath());
    }
}
