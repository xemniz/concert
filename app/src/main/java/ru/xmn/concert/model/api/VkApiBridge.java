package ru.xmn.concert.model.api;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKRequest;

/**
 * Created by xmn on 29.05.2016.
 */

public class VkApiBridge {
    public VKRequest bandList(){
        return VKApi.audio().get();
    }
}
