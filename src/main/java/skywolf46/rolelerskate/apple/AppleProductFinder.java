package skywolf46.rolelerskate.apple;

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
import java.util.ArrayList;
import java.util.List;

public class AppleProductFinder {

    public static List<AppleProduct> find(String text) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet("https://www.apple.com/us/search/" + URLEncoder.encode(text, "utf-8") + "?sel=accessories&src=serp");
        String x = EntityUtils.toString(httpClient.execute(get).getEntity());
        Document docs = Jsoup.parse(x);
        Element el = docs.selectFirst("div.as-accessories-header > div > h1");
        if (el == null)
            return null;
        String xg = el.text();
        int index = Integer.parseInt(xg.substring(text.length(), xg.indexOf(" ", text.length())));
        List<AppleProduct> products = new ArrayList<>();
        if (index <= 0)
            return products;
        Elements ell = docs.select("#as-accessories > div.as-accessories-results.as-search-desktop > div.column.small-12.as-search-results-tiles.as-search-results-width > div");
        for (Element e : ell) {
            AppleProduct prdt = new AppleProduct();
//            prdt.url = e.selectFirst("img").attr("src");
            prdt.productURL = "https://www.apple.com/" + e.selectFirst("a").attr("href");
            String texter = e.selectFirst("a").attr("href");
            texter = texter.substring(texter.indexOf("product/") + "product/".length());
            texter = texter.substring(0, texter.indexOf("/") - 2);
            prdt.url = "https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/" + texter + "?wid=445&hei=445&fmt=jpeg";
            prdt.name = e.select("a > span:nth-child(1)").text();
            prdt.isNew = !e.select("a > span:nth-child(2)").text().trim().isEmpty();

            prdt.price = e.select("div.as-price-currentprice.as-producttile-currentprice").text();
            products.add(prdt);
//            System.out.println("X1...");
        }
//        System.out.println("X2...");
        return products;
    }

    public static class AppleProduct {
        private String name;
        private String url;
        private String productURL;
        private String price;
        private List<String> colors = new ArrayList<>();
        private boolean isNew = false;
        // 애플 독점?
        private boolean onlyAtApple = false;

        public String getImageURL() {
            return url;
        }

        public String getProductURL() {
            return productURL;
        }

        public String getName() {
            return name;
        }

        public String getPrice() {
            return price;
        }

        public List<String> getColors() {
            return colors;
        }

        public boolean isNew() {
            return isNew;
        }

        public boolean isOnlyAtApple() {
            return onlyAtApple;
        }
    }
}
