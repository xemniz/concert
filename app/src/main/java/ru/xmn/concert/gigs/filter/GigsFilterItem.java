package ru.xmn.concert.gigs.filter;

/**
 * Created by xmn on 23.11.2016.
 */

public abstract class GigsFilterItem {
    private boolean isApply;

    boolean isApply() {
        return isApply;
    }

    public void setApply(boolean apply) {
        isApply = apply;
    }
}
