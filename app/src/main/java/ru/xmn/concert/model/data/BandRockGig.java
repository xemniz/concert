package ru.xmn.concert.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BandRockGig {

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

    private List<EventRockGig> gigs = new ArrayList<>();
    private String bandImageUrl;

    public String getBandImageUrl() {
        return bandImageUrl;
    }

    public void setBandImageUrl(String bandImageUrl) {
        this.bandImageUrl = bandImageUrl;
    }

    public List<EventRockGig> getGigs() {
        return gigs;
    }

    public void setGigs(List<EventRockGig> gigs) {
        this.gigs = gigs;
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

        BandRockGig bandRockGig1 = (BandRockGig) o;

        return band != null ? band.equals(bandRockGig1.band) : bandRockGig1.band == null;

    }

    @Override
    public int hashCode() {
        return band != null ? band.hashCode() : 0;
    }
}

