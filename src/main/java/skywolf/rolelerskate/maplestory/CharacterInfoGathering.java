package skywolf.rolelerskate.maplestory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class CharacterInfoGathering {
    public static CharacterInfo gather(String characterName, String characterIdentify) throws IOException {
        String url = "https://maplestory.nexon.com/Common/Character/Detail/" + characterName + "?p=" + characterIdentify;
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        get.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; .NET CLR 1.0.3705; .NET CLR 1.1.4322; .NET CLR 1.2.30703)");
        HttpResponse resp = httpClient.execute(get);
        String content = EntityUtils.toString(resp.getEntity());
        Document doc = Jsoup.parse(content);
        Element el = doc.selectFirst("div.char_img > div > img");
        if (el == null)
            return null;
        CharacterInfo ci = new CharacterInfo();
        ci.avatarURL = el.attr("src");
        ci.level = (doc.selectFirst("div.char_info > dl:nth-child(1) > dd")).text();
        ci.className = (doc.selectFirst("div.char_info > dl:nth-child(2) > dd")).text();
        ci.server = (doc.selectFirst("div.char_info > dl:nth-child(3) > dd")).text();

        ci.level = ci.level.substring(ci.level.indexOf(".") + 1).trim();
        return ci;
    }

    public static class CharacterInfo {
        private String className;
        private String level;
        private String server;
        private String avatarURL;

        private CharacterInfo() {

        }

        public String getClassName() {
            return className;
        }

        public String getLevel() {
            return level;
        }

        public String getServer() {
            return server;
        }

        public String getAvatarURL() {
            return avatarURL;
        }
    }
}
