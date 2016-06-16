package ru.xmn.concert.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Place extends RealmObject{

    @SerializedName("clubid")
    @Expose
    private String clubid;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("adress")
    @Expose
    private String adress;
    @SerializedName("site")
    @Expose
    private String site;
    @SerializedName("clubvk")
    @Expose
    private String clubvk;

    /**
     *
     * @return
     * The clubid
     */
    public String getClubid() {
        return clubid;
    }

    /**
     *
     * @param clubid
     * The clubid
     */
    public void setClubid(String clubid) {
        this.clubid = clubid;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The adress
     */
    public String getAdress() {
        return adress;
    }

    /**
     *
     * @param adress
     * The adress
     */
    public void setAdress(String adress) {
        this.adress = adress;
    }

    /**
     *
     * @return
     * The site
     */
    public String getSite() {
        return site;
    }

    /**
     *
     * @param site
     * The site
     */
    public void setSite(String site) {
        this.site = site;
    }

    /**
     *
     * @return
     * The clubvk
     */
    public String getClubvk() {
        return clubvk;
    }

    /**
     *
     * @param clubvk
     * The clubvk
     */
    public void setClubvk(String clubvk) {
        this.clubvk = clubvk;
    }

}

