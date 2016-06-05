package ru.xmn.concert.model.data;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RockGigEvent {

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
    private List<Band> bands = new ArrayList<Band>();

    /**
     *
     * @return
     * The eventid
     */
    public String getEventid() {
        return eventid;
    }

    /**
     *
     * @param eventid
     * The eventid
     */
    public void setEventid(String eventid) {
        this.eventid = eventid;
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
     * The date
     */
    public String getDate() {
        return date;
    }

    /**
     *
     * @param date
     * The date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     *
     * @return
     * The time
     */
    public String getTime() {
        return time;
    }

    /**
     *
     * @param time
     * The time
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     *
     * @return
     * The price
     */
    public String getPrice() {
        return price;
    }

    /**
     *
     * @param price
     * The price
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     *
     * @return
     * The gigvk
     */
    public String getGigvk() {
        return gigvk;
    }

    /**
     *
     * @param gigvk
     * The gigvk
     */
    public void setGigvk(String gigvk) {
        this.gigvk = gigvk;
    }

    /**
     *
     * @return
     * The poster
     */
    public String getPoster() {
        return poster;
    }

    /**
     *
     * @param poster
     * The poster
     */
    public void setPoster(String poster) {
        this.poster = poster;
    }

    /**
     *
     * @return
     * The genre
     */
    public String getGenre() {
        return genre;
    }

    /**
     *
     * @param genre
     * The genre
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     *
     * @return
     * The place
     */
    public Place getPlace() {
        return place;
    }

    /**
     *
     * @param place
     * The place
     */
    public void setPlace(Place place) {
        this.place = place;
    }

    /**
     *
     * @return
     * The bands
     */
    public List<Band> getBands() {
        return bands;
    }

    /**
     *
     * @param bands
     * The bands
     */
    public void setBands(List<Band> bands) {
        this.bands = bands;
    }

}
