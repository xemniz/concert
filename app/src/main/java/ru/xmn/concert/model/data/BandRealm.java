package ru.xmn.concert.model.data;

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
    private String bandImageUrl;

    public BandRealm() {
    }

    public BandRealm(Band band) {
        bandid = band.getBandid();
        bandgenre = band.getBandgenre();
        bandvk = band.getBandvk();
        name = band.getBand();
    }

    public String getBandid() {
        return bandid;
    }

    public void setBandid(String bandid) {
        this.bandid = bandid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBandgenre() {
        return bandgenre;
    }

    public void setBandgenre(String bandgenre) {
        this.bandgenre = bandgenre;
    }

    public String getBandvk() {
        return bandvk;
    }

    public void setBandvk(String bandvk) {
        this.bandvk = bandvk;
    }

    public String getBandImageUrl() {
        return bandImageUrl;
    }

    public void setBandImageUrl(String bandImageUrl) {
        this.bandImageUrl = bandImageUrl;
    }

    @Override
    public String toString() {
        return "BandRealm{" +
                "bandImageUrl='" + bandImageUrl + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
