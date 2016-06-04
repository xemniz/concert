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
import java.util.Locale;

import de.umass.lastfm.Artist;
import de.umass.lastfm.ImageSize;
import ru.xmn.concert.model.data.EventGig;


public class RockGigApi {

    Locale dLocale = new Locale("ru", "RU");
    private final String key = "2bfb15fbcefb91f170233c99ff4af225";      // api key
    private final String secret = "b8c42600c5d31faa6d282cb5f104b9b9";   // api secret

    /**
     * @param band
     * @return future events of band
     * @throws IOException
     */
    public List<EventGig> eventsByBand(final String band) throws IOException {

//        System.out.println(band + " band in rockgigapi");
//        Document doc = Jsoup
//
//                .connect("http://rockgig.net/?submit.x=0&submit.y=0&t=" + URLEncoder.encode(band, "Cp1251") + "&q=search&c=msk")
//                .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
//                .get();

        List<EventGig> gigsList = new ArrayList<>();
//        Elements gigs = doc.getElementsByClass("ElDiv");
//        for (Element gigEl : gigs) {
//            String timePrice = gigEl.getElementsByClass("ElDivTime").get(0).text();
//            String bandsFromElStr=gigEl.getElementsByClass("ElDivBand").get(0).text();
//            String time;
//            String price;
//            if (timePrice.contains("/")){
//                time = gigEl.getElementsByClass("ElDivTime").get(0).text().substring(1, timePrice.indexOf("/") - 1);
//                price = gigEl.getElementsByClass("ElDivTime").get(0).text().substring(timePrice.indexOf("/") + 2);
//            } else {
//                time = timePrice;
//                price = " ";
//            }
//
//            boolean isManyBands = bandsFromElStr.contains(", ");
//            ArrayList<String> bandsFromEl;
//            if (isManyBands)
//            bandsFromEl = new ArrayList<>(Arrays.asList(gigEl.getElementsByClass("ElDivBand").get(0).text().toLowerCase().split(", ")));
//            else
//            bandsFromEl = new ArrayList<>(Arrays.asList(bandsFromElStr.substring(0, bandsFromElStr.length()-1).toLowerCase()));
//
//            if (bandsFromEl.contains(band.trim().toLowerCase())) {
//                EventGig gigData = new EventGig(
//                gigEl.getElementsByClass("ElDivBand").get(0).text(),
//                gigEl.getElementsByClass("ElDateM").get(0).text() + " " + gigEl.getElementsByClass("ElDateD").get(0).text(),
//                time,
//                gigEl.getElementsByClass("ElDivName").get(0).select("span[itemprop = location]").select("span[itemprop = name]").text(),
//                gigEl.getElementsByClass("ElDivName").get(0).select("span[class = elname]").select("span[itemprop = name]").text(),
//                price,
//                band,
//                gigs.size()
//                );
//                Artist artistLFMAPI = Artist.getInfo(band, dLocale, "pe-psy", key);
//                gigData.setBandImageUrl(artistLFMAPI.getImageURL(ImageSize.EXTRALARGE));
//                System.out.println(gigData.toString());
//                gigsList.add(gigData);
//            }
//        }
//        for (EventGig g :
//        gigsList) {
//            System.out.println("RESULT FOR BANDS " + g);
//        }

        return gigsList;
    }

    public List<String> findBands(final String band) throws IOException {
//        Document doc = Jsoup.connect(
//        "http://rockgig.net/?submit.x=0&submit.y=0&t=" + URLEncoder.encode(band, "Cp1251") + "&q=search&c=msk")
//                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
//                .get();
        List<String> bandsList = new ArrayList<>();
//
//        System.out.println(doc.getElementsByClass("SearchResult").get(0).text());
//        Elements bandsListJsoup = doc.getElementsByClass("ListBandName");
//        for (Element bandEl : bandsListJsoup) {
//            bandsList.add(bandEl.getElementsByTag("a").text());
//        }
        return bandsList;
    }
}
