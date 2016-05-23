package ru.xmn.concert.model.api;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
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
        Document doc = Jsoup.connect("http://rockgig.net/?submit.x=0&submit.y=0&t=" + URLEncoder.encode(band, "Cp1251") + "&q=search&c=msk")
                .get();

        List<EventGig> gigsList = new ArrayList<>();
        Elements gigs = doc.getElementsByClass("ElDiv");
        for (Element gigEl : gigs){
            EventGig gigData = new EventGig(gigEl.getElementsByClass("ElDivBand").get(0).text()
                    , gigEl.getElementsByClass("ElDateM").get(0).text()+" "+gigEl.getElementsByClass("ElDateD").get(0).text());
            gigsList.add(gigData);
        }
        return gigsList;
    }
}
