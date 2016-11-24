package ru.xmn.concert.mvp.model.data;

/**
 * Created by xmn on 19.05.2016.
 */
public class EventGig {
    private String band;
    private String bandImageUrl;
    private String date;
    private String time;
    private String place;
    private String name;
    private String price;
    private String requestBand;
    private int countOfSimilar;

    public EventGig(String band, String date, String time, String place, String name, String price, String requestBand, int countOfSimilar) {
        this.band = band;
        this.date = date;
        this.time = time;
        this.place = place;
        this.name = name;
        this.price = price;
        this.requestBand = requestBand;
        this.countOfSimilar = countOfSimilar;
    }

    public int getCountOfSimilar() {
        return countOfSimilar;
    }

    public String getBandImageUrl() {
        return bandImageUrl;
    }

    public void setBandImageUrl(String bandImageUrl) {
        this.bandImageUrl = bandImageUrl;
    }

    public String getRequestBand() {
        return requestBand;
    }

    public void setRequestBand(String requestBand) {
        this.requestBand = requestBand;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public EventGig(String band, String dateTime) {
        this.band = band;
        this.date = dateTime;
    }

    public String getBand() {
        return band;
    }

    public void setBand(String band) {
        this.band = band;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "EventGig{" +
                "band='" + band + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", place='" + place + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
