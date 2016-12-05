package ru.xmn.concert.gigs.filter;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.xmn.concert.gigs.GigsFragment;
import ru.xmn.concert.gigs.MainActivity;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by xmn on 18.11.2016.
 */
public class GigsFilter {
    private SparseArray<GigsFilterItem> mItems = new SparseArray<>();

    public void addItem(int id, GigsFilterItem item) {
        mItems.put(id, item);
    }

    public Observable<List<String>> getFilterList() {
        List<Observable<String>> observables = new ArrayList<>();

        for (int i = 0; i < mItems.size(); i++) {
            int key = mItems.keyAt(i);
            GigsFilterItem item = mItems.get(key);
            if (item instanceof FilterItemObservable && item.isApply())
                observables.add(
                        ((FilterItemObservable) item).list()
                                .subscribeOn(Schedulers.computation())
                );
        }

        return Observable.from(observables).flatMap(listObservable -> listObservable).toList().single();
    }

    public boolean getApplyById(int id) {
        return mItems.get(id).isApply();
    }

    public void setApplyById(int id, boolean isApply) {
        mItems.get(id).setApply(isApply);
    }

    public boolean isDisabled() {
        for (int i = 0; i < mItems.size(); i++) {
            int key = mItems.keyAt(i);
            GigsFilterItem item = mItems.get(key);
            if (item.isApply())
                return false;
        }
        return true;
    }
}
