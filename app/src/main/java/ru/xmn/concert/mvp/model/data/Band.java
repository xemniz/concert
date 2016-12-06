package ru.xmn.concert.mvp.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Band extends RealmObject {
    public Band(){}

    @PrimaryKey
    @SerializedName("bandid")
    @Expose
    private String bandid;
    @SerializedName("band")
    @Expose
    private String band;
    @SerializedName("bandgenre")
    @Expose
    private String bandgenre;
    @SerializedName("bandvk")
    @Expose
    private String bandvk;

    private String bandImageUrl;

    public String getBandImageUrl() {
        return bandImageUrl;
    }

    public void setBandImageUrl(String bandImageUrl) {
        this.bandImageUrl = bandImageUrl;
    }


    /**
     *
     * @return
     * The bandid
     */
    public String getBandid() {
        return bandid;
    }

    /**
     *
     * @param bandid
     * The bandid
     */
    public void setBandid(String bandid) {
        this.bandid = bandid;
    }

    /**
     *
     * @return
     * The band
     */
    public String getBand() {
        return band;
    }

    /**
     *
     * @param band
     * The band
     */
    public void setBand(String band) {
        this.band = band;
    }

    /**
     *
     * @return
     * The bandgenre
     */
    public String getBandgenre() {
        return bandgenre;
    }

    /**
     *
     * @param bandgenre
     * The bandgenre
     */
    public void setBandgenre(String bandgenre) {
        this.bandgenre = bandgenre;
    }

    /**
     *
     * @return
     * The bandvk
     */
    public String getBandvk() {
        return bandvk;
    }

    /**
     *
     * @param bandvk
     * The bandvk
     */
    public void setBandvk(String bandvk) {
        this.bandvk = bandvk;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Band band1 = (Band) o;

        return band != null ? band.equals(band1.band) : band1.band == null;

    }

    @Override
    public int hashCode() {
        return band != null ? band.hashCode() : 0;
    }
}

