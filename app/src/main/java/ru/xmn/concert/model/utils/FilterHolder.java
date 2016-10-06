package ru.xmn.concert.model.utils;

import rx.Observable;
import rx.subjects.PublishSubject;

public class FilterHolder {
    private final PublishSubject<Filter> subject = PublishSubject.create();
    private Filter current;

    public Observable<Filter> observeChanges(boolean emitCurrentValue) {
        return emitCurrentValue ? subject.startWith(current) : subject;
    }

    public void set(Filter filter) {
        this.current = filter;
        subject.onNext(filter);
    }
}