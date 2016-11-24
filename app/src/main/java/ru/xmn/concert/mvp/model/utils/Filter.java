package ru.xmn.concert.mvp.model.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by xmn on 06.10.2016.
 */

public class Filter {
    public List<String> bandList;
    public List<Observable<List<String>>> mLists;
    private FilterHolder holder;

    public Filter(){
        mLists = new ArrayList<>();
        ArrayList<String> value = new ArrayList<>();
        mLists.add(Observable.just(value));
        Log.d(this.getClass().getSimpleName(), "BREAKPOINT");
    }

    public Filter(List<Observable<List<String>>> list) {
        mLists = list;
    }

    public void addToBandList (List<Observable<List<String>>> lists) {
        mLists = lists;
    }

    public void applyList(){

    }

    public void setHolder(FilterHolder holder) {
        this.holder = holder;
    }

    public void onCompleted() {
        holder.subject.onCompleted();
    }
}
