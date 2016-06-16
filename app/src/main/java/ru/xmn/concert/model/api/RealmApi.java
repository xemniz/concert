package ru.xmn.concert.model.api;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import ru.xmn.concert.Application;
import ru.xmn.concert.model.data.BandRockGig;
import ru.xmn.concert.model.data.EventRealm;
import ru.xmn.concert.model.data.EventRockGig;
import rx.Observable;

public class RealmApi {
    static final Realm myRealm = Realm.getInstance(
                    new RealmConfiguration.Builder(Application.get().getApplicationContext())
                            .name("myOtherRealm.realm")
                            .build()
            );

    public void GigsToRealm (Observable<List<EventRockGig>> gigsRockGig){
//        gigsRockGig
//                .flatMap(eventRockGigs -> Observable.from(eventRockGigs))
//                .flatMap(eventRockGig -> {
//                    EventRealm eventRealm = new EventRealm();
//                    eventRealm.setBandRockGigs(new RealmList<BandRockGig>(eventRockGig.getBandRockGigs().toArray()));
//                })
    }
}
