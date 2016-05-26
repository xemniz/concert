package ru.xmn.concert.model.data;

/**
 * Created by xmn on 26.05.2016.
 */

public class Band {
    private String imageUrl;
    private String wiki;
    private String name;

    public Band(String name, String wiki, String imageUrl) {
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
