package ru.xmn.concert.mvp.model.data;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Event extends RealmObject {
    @PrimaryKey
    @SerializedName("eventid")
    @Expose
    private String eventid;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("gigvk")
    @Expose
    private String gigvk;
    @SerializedName("poster")
    @Expose
    private String poster;
    @SerializedName("genre")
    @Expose
    private String genre;
    @SerializedName("place")
    @Expose
    private Place place;
    @SerializedName("bands")
    @Expose
    private RealmList<Band> bands;

    public RealmList<Band> getBands() {
        return bands;
    }

    public String getEventid() {
        return eventid;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getPrice() {
        return price;
    }

    public String getGigvk() {
        return gigvk;
    }

    public String getPoster() {
        return poster;
    }

    public String getGenre() {
        return genre;
    }

    public Place getPlace() {
        return place;
    }
}
