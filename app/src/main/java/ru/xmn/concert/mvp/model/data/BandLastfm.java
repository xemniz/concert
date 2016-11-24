package ru.xmn.concert.mvp.model.data;

/**
 * Created by xmn on 26.05.2016.
 */

public class BandLastfm {
    private String imageUrl = "http://blog.songcastmusic.com/wp-content/uploads/2013/08/iStock_000006170746XSmall.jpg";
    private String wiki;
    private String name;

    public BandLastfm(String name, String wiki, String imageUrl) {
        this.imageUrl = imageUrl;
        this.wiki = wiki;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getWiki() {
        return wiki;
    }

    public void setWiki(String wiki) {
        this.wiki = wiki;
    }
}
