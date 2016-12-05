package ru.xmn.concert.gigs.filter;

import java.util.List;

import ru.xmn.concert.mvp.model.api.VkApiBridge;
import rx.Observable;

/**
 * Created by xmn on 23.11.2016.
 */

public class VkFilterItem extends GigsFilterItem implements FilterItemObservable {
    VkApiBridge mVkApiBridge;

    public VkFilterItem(VkApiBridge vkApiBridge) {
        mVkApiBridge = vkApiBridge;
    }

    @Override
    public Observable<String> list() {
        return mVkApiBridge.bandList();
    }
}
