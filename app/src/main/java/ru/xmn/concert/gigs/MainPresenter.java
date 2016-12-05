//package ru.xmn.concert.gigs;
//
//import android.support.annotation.NonNull;
//import android.util.Log;
//
//import com.fernandocejas.frodo.annotation.RxLogObservable;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import io.realm.Case;
//import io.realm.Realm;
//import io.realm.RealmQuery;
//import io.realm.RealmResults;
//import ru.xmn.concert.mvp.model.data.Event;
//import ru.xmn.concert.mvp.model.utils.Filter;
//import ru.xmn.concert.mvp.model.utils.FilterHolder;
//import ru.xmn.concert.mvp.view.EventListView;
//import rx.Observable;
//import rx.Observer;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;
//
//public class MainPresenter {
//    private GigsModel mModel;
//    private EventListView mView;
//    private Observable<RealmResults<Event>> mEventRealmList;
//    FilterHolder mFilterHolder;
//
//    public MainPresenter(EventListView view) {
//        mModel = GigsModel.INSTANCE;
//        mView = view;
//    }
//
//    public void onViewCreated(int i, int onPage) {
//        mFilterHolder = new FilterHolder(new Filter());
//        Log.d(this.getClass().getSimpleName(), "BREAKPOINT");
//        mEventRealmList = loadEventRealmList();
//        onEventRealmListLoaded(i, onPage);
//    }
//
//    @NonNull
//    @RxLogObservable(RxLogObservable.Scope.EVERYTHING)
//    private Observable<RealmResults<Event>> loadEventRealmList() {
//        return Observable.combineLatest(
//                mModel.gigsToRealm(),
//                mFilterHolder.observeChanges(true),
//                (t1, filter) -> filter)
//                .observeOn(Schedulers.io())
//                .flatMap(filter -> Observable.just(getListFromFilter(filter)))
//                .doOnNext(s -> Log.d(this.getClass().getSimpleName(), "BREAKPOINT1 " + s))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .flatMap(this::getFilteredEventsFromRealm)
//                .map(eventRealms -> {
//                    mView.setListSize(eventRealms.size());
//                    Log.d(this.getClass().getSimpleName(), ""+eventRealms.size());
//                    return eventRealms;
//                });
//    }
//
//    @NonNull
//    private Observable<? extends RealmResults<Event>> getFilteredEventsFromRealm(List<String> list) {
//        RealmQuery<Event> query = Realm.getDefaultInstance().where(Event.class);
//
//        if (list.size()>0) {
//            query.equalTo("bandRockGigs.name", "unreaaa", Case.INSENSITIVE);
//            for (String bName :
//                    list) {
//                query.or().equalTo("bandRockGigs.name", bName, Case.INSENSITIVE);
//            }
//        }
//        return query
////                        .greaterThanOrEqualTo("date", new Date(System.currentTimeMillis()))
//                .findAllSortedAsync("date")
//                .asObservable()
//                .filter(RealmResults::isLoaded)
//                .first();
//    }
//
//    private void onEventRealmListLoaded(int i, int onPage) {
//        mEventRealmList
//                .map(eventRealms -> eventRealms.subList(i * onPage, i * onPage + eventRealms.size()>onPage?onPage:eventRealms.size()))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<List<Event>>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onNext(List<Event> events) {
//                        System.out.println("ONNEXT");
//                        mView.loadEventList(events);
//                    }
//                });
//    }
//
//    public void addMoreData(int i, int onPage) {
//        onEventRealmListLoaded(i, onPage);
//    }
//
//    public void setVkList() {
//        List<Observable<List<String>>> list = new ArrayList<>();
//        list.add(mModel.vkBandList());
//        Filter filter = new Filter(list);
//        mFilterHolder.set(filter);
//    }
//
//    private List<String> getListFromFilter(Filter filter) {
//        List<String> single = Observable.from(filter.mLists)
//                .flatMap(observables -> observables)
//                .doOnNext(System.out::println)
//                .flatMap(Observable::from)
//                .distinct()
//                .toList().toBlocking().single();
//        return single;
//    }
//}