package ru.xmn.concert.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by xmn on 16.06.2016.
 */

public class PlaceRealm extends RealmObject {
    private String clubid;
    private String name;
    private String adress;
    private String site;
    private String clubvk;
}
