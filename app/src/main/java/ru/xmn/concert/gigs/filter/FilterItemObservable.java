package ru.xmn.concert.gigs.filter;

import java.util.List;

import rx.Observable;

/**
 * Created by xmn on 23.11.2016.
 */

interface FilterItemObservable {
    Observable<String> list();
}