package ru.xmn.concert.model.data;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import ru.xmn.concert.model.api.LastfmApi;

public class EventRealm extends RealmObject {
    @Ignore
    private LastfmApi lastfmApi;
    private String eventid;
    @PrimaryKey
    private String name;
    private Date date;
    private String time;
    private String price;
    private String gigvk;
    private String poster;
    private String genres;
    private Place place;
    private RealmList<BandRealm> bandRockGigs = new RealmList<>();

    public EventRealm() {
    }

    public EventRealm(EventRockGig eventRockGig) {
        eventid = eventRockGig.getEventid();
        name = eventRockGig.getName();
        time = eventRockGig.getTime();
        price = eventRockGig.getPrice();
        gigvk = eventRockGig.getGigvk();
        poster = eventRockGig.getPoster();
        place = eventRockGig.getPlace();

        //setting genres
        genres = eventRockGig.getGenre();

        //setting bands
//        Log.d(getClass().getSimpleName(), "Setting bands " + eventRockGig.getBands().size());
        for (Band band :
                eventRockGig.getBands()) {
            bandRockGigs.add(new BandRealm(band));
//            Log.d(getClass().getSimpleName(), "BAND NAME " + bandRockGigs.get(0));
        }
//        if (bandRockGigs.size() < 1) {
//            try {
////                Log.d(getClass().getSimpleName(), "Setting bands bandRockGigs.size() < 1" + eventRockGig.getBands().size());
//                BandLastfm bandLastfm = lastfmApi.getBandInfo(name);
//                BandRealm bandRealm = new BandRealm();
//                bandRealm.setBandImageUrl(bandLastfm.getImageUrl());
//                bandRealm.setName(name);
//                bandRockGigs.add(bandRealm);
//            } catch (Exception e) {
//                //no band on lastfm
//            }
//        }


        //setting date
        date = toDate(eventRockGig.getDate());
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

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public RealmList<BandRealm> getBandRockGigs() {
        return bandRockGigs;
    }

    public void setBandRockGigs(RealmList<BandRealm> bandRockGigs) {
        this.bandRockGigs = bandRockGigs;
    }

    public Date toDate(String dateString) {
        dateString += " " + time + ":00";
        Date date = null;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            date = formatter.parse(dateString);
//            Log.e("Print result: ", String.valueOf(date));

        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return date;
    }

    @Override
    public String toString() {
        String s = "EventRealm{" +
                "eventid='" + eventid + '\'' +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", time='" + time + '\'' +
                ", price='" + price + '\'' +
                ", gigvk='" + gigvk + '\'' +
                ", poster='" + poster + '\'' +
                ", genres='" + genres + '\'' +
                ", place=" + place +
                ", bands=";
        for (BandRealm band :
                bandRockGigs) {
            s = s + "! " + band.toString() + " !";
        }

        s += '}';
        return s;
    }
}
