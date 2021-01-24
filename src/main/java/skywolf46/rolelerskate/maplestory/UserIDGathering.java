package skywolf46.rolelerskate.maplestory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;

public class UserIDGathering {
    public static String gatherUserID(String idName) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();

        HttpGet get = new HttpGet("https://maplestory.nexon.com/Ranking/World/Total?c=" + idName);
        get.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; .NET CLR 1.0.3705; .NET CLR 1.1.4322; .NET CLR 1.2.30703)");
        HttpResponse resp = httpClient.execute(get);
        String content = EntityUtils.toString(resp.getEntity());
        Document doc = Jsoup.parse(content);
        Elements el = doc.select("td.left > dl > dt > a");
        String ecd = URLEncoder.encode(idName,"utf-8");
        for (Element e : el) {
            String text = e.attr("href");
//            System.out.println(text);
            if (text != null && text.toLowerCase().startsWith(("/Common/Character/Detail/" + ecd + "?p=").toLowerCase())) {
                return text.substring(text.indexOf("?p=") + 3);
            }
        }
        return null;
    }
}
