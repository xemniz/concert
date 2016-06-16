package ru.xmn.concert.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by xmn on 15.06.2016.
 */

public class BandRealm extends RealmObject {
    private String bandid;
    @PrimaryKey
    private String name;
    private String bandgenre;
    private String bandvk;
    private RealmList<EventRealm> gigs = new RealmList<>();
    private String bandImageUrl;
}
