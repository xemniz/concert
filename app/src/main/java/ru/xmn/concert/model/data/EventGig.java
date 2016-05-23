package ru.xmn.concert.model.data;

/**
 * Created by xmn on 19.05.2016.
 */
public class EventGig {
    String band;
    String dateTime;

    public EventGig(String band, String dateTime) {
        this.band = band;
        this.dateTime = dateTime;
    }

    public String getBand() {
        return band;
    }

    public void setBand(String band) {
        this.band = band;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
