package ru.xmn.concert.model.data;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class EventRealm extends RealmObject {
    private String eventid;
    @PrimaryKey
    private String name;
    private Date date;
    private String time;
    private String price;
    private String gigvk;
    private String poster;
    private RealmList<GenreRealm> genres = new RealmList<>();
    private Place place;
    private RealmList<BandRockGig> bandRockGigs = new RealmList<>();

    public EventRealm() {
    }

    EventRealm(EventRockGig eventRockGig) {
        eventid = eventRockGig.getEventid();
        name = eventRockGig.getName();
        time = eventRockGig.getTime();
        price = eventRockGig.getPrice();
        gigvk = eventRockGig.getGigvk();
        poster = eventRockGig.getPoster();
        place = eventRockGig.getPlace();

        //setting genres
        String[] genresArr = eventRockGig.getGenre().split(", ");
        GenreRealm[] genreRealms = new GenreRealm[genresArr.length];

        int i = 0;
        for (String s :
                genresArr) {
            genreRealms[i].name = s;
            i++;
        }
        genres = new RealmList<>(genreRealms);

        //setting bands
        bandRockGigs = new RealmList<>((BandRockGig[]) eventRockGig.getBandRockGigs().toArray());

        //setting date

    }

    public String getEventid() {
        return eventid;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getGigvk() {
        return gigvk;
    }

    public void setGigvk(String gigvk) {
        this.gigvk = gigvk;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public RealmList<GenreRealm> getGenres() {
        return genres;
    }

    public void setGenres(RealmList<GenreRealm> genres) {
        this.genres = genres;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public RealmList<BandRockGig> getBandRockGigs() {
        return bandRockGigs;
    }

    public void setBandRockGigs(RealmList<BandRockGig> bandRockGigs) {
        this.bandRockGigs = bandRockGigs;
    }

    public Date toDate(String dateString) {
        Date date = null;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            date = formatter.parse(dateString);
            Log.e("Print result: ", String.valueOf(date));

        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return date;
    }

}
