package ru.xmn.concert.gigs.filter;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by xmn on 18.11.2016.
 */
public class GigsFilter {
    private SparseArray<GigsFilterItem> mItems = new SparseArray<>();

    void addItem(int id, GigsFilterItem item) {
        mItems.put(id, item);
    }

    public Observable getFilterList() {
        Observable<List<String>> observable = Observable.just(new ArrayList<String>());
        for(int i = 0; i < mItems.size(); i++) {
            int key = mItems.keyAt(i);
            GigsFilterItem item = mItems.get(key);
            if (item instanceof FilterItemObservable && item.isApply())
                observable.mergeWith(((FilterItemObservable) item).list());
        }
        return observable;
    }

    public boolean getApplyById (int id) {
        return  mItems.get(id).isApply();
    }

    public void setApplyById (int id, boolean isApply) {
         mItems.get(id).setApply(isApply);
    }
}
