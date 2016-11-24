package ru.xmn.concert.mvp.model.utils;

import rx.Observable;
import rx.subjects.PublishSubject;

public class FilterHolder {
    public final PublishSubject<Filter> subject = PublishSubject.create();
    public Filter current;

    public FilterHolder(Filter filter) {
        current = filter;
        filter.setHolder(this);
    }

    //    @RxLogObservable(RxLogObservable.Scope.EVERYTHING)
    public Observable<Filter> observeChanges(boolean emitCurrentValue) {
        return emitCurrentValue ? subject.startWith(current) : subject;
    }

    public void set(Filter filter) {
        this.current = filter;
        subject.onNext(filter);
    }
}