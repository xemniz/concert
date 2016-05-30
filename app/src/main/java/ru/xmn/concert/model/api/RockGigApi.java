package ru.xmn.concert.model.api;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.xmn.concert.model.data.EventGig;


public class RockGigApi {

    /**
     *
     * @param band
     * @return future events of band
     * @throws IOException
     */
    public List<EventGig> eventsByBand (final String band) throws IOException {
        System.out.println(band+"band in rockgigapi");
        Document doc = Jsoup.connect("http://rockgig.net/?submit.x=0&submit.y=0&t=" + URLEncoder.encode(band, "Cp1251") + "&q=search&c=msk")
                .get();
        System.out.println("after getting doc");
        List<EventGig> gigsList = new ArrayList<>();
        Elements gigs = doc.getElementsByClass("ElDiv");
        for (Element gigEl : gigs){
            String timePrice = gigEl.getElementsByClass("ElDivTime").get(0).text();
            ArrayList<String> bandsFromEl = new ArrayList<>(Arrays.asList(gigEl.getElementsByClass("ElDivBand").get(0).text().toLowerCase().split(", ")));
            if (bandsFromEl.contains(band.trim().toLowerCase())) {
                EventGig gigData = new EventGig(
                        gigEl.getElementsByClass("ElDivBand").get(0).text(),
                        gigEl.getElementsByClass("ElDateM").get(0).text()+" "+gigEl.getElementsByClass("ElDateD").get(0).text(),
                        gigEl.getElementsByClass("ElDivTime").get(0).text().substring(1,timePrice.indexOf("/")-1),
                        gigEl.getElementsByClass("ElDivName").get(0).select("span[itemprop = location]").select("span[itemprop = name]").text(),
                        gigEl.getElementsByClass("ElDivName").get(0).select("span[class = elname]").select("span[itemprop = name]").text(),
                        gigEl.getElementsByClass("ElDivTime").get(0).text().substring(timePrice.indexOf("/")+2),
                        band
                        );
                System.out.println(gigData.toString());
                gigsList.add(gigData);
            }
        }
        return gigsList;
    }

    public List<String> findBands (final String band) throws IOException {
        Document doc = Jsoup.connect(
                "http://rockgig.net/?submit.x=0&submit.y=0&t=" + URLEncoder.encode(band, "Cp1251") + "&q=search&c=msk")
                .get();
        List<String> bandsList = new ArrayList<>();

        System.out.println(doc.getElementsByClass("SearchResult").get(0).text());
        Elements bandsListJsoup = doc.getElementsByClass("ListBandName");
        for (Element bandEl : bandsListJsoup){
            bandsList.add(bandEl.getElementsByTag("a").text());
        }
        return bandsList;
    }
}
