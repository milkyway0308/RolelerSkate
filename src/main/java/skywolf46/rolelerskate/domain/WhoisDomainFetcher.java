package skywolf46.rolelerskate.domain;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import skywolf46.rolelerskate.maplestory.CharacterInfoGathering;

import java.io.IOException;

public class WhoisDomainFetcher {

    public static CharacterInfoGathering.CharacterInfo gather(String host) throws IOException {
        String url = "https://api.ip2whois.com/v1";
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);

        HttpResponse resp = httpClient.execute(get);
        String content = EntityUtils.toString(resp.getEntity());
        Document doc = Jsoup.parse(content);
        Element el = doc.selectFirst("div.char_img > div > img");
        if (el == null)
            return null;
        return null;
    }
}
