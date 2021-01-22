package skywolf.rolelerskate.eve.universe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SystemSearcher {

    public static long requestSystemID(String name) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://esi.evetech.net/latest/universe/ids/?datasource=tranquility&language=en-us");
        JsonArray jl = new JsonArray();
        jl.add(name);
        httpPost.setHeader("Content-type", "application/json");
        JsonObject json = new JsonObject();
        json.add("names", jl);
        StringEntity stringEntity = new StringEntity(jl.toString());
        httpPost.getRequestLine();
        httpPost.setEntity(stringEntity);
        /*
         * Execute the HTTP Request
         */
        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity respEntity = response.getEntity();

        if (respEntity != null) {
            // EntityUtils to get the response content
            String content = EntityUtils.toString(respEntity);
            JsonElement obj = new JsonParser().parse(content);
            if (!(obj instanceof JsonObject))
                throw new IllegalStateException("성계 데이터의 반환값이 정상적이지 않습니다.");
            JsonObject jo = (JsonObject) obj;
            if (jo.has("systems")) {
                return ((JsonObject) ((JsonArray) jo.get("systems")).get(0)).get("id").getAsLong();
            }
        }

//        JsonObject json = new JsonParser().parse(Files.read);
        return -1;
    }
}
