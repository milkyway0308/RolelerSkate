package skywolf.rolelerskate.eve.universe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class KillMailCatcher {
    public static int[] catchKillMail(long systemID) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet("https://esi.evetech.net/latest/universe/system_kills/?datasource=tranquility");
        HttpResponse resp = httpClient.execute(get);
        String content = EntityUtils.toString(resp.getEntity());
        JsonArray obj = (JsonArray) new JsonParser().parse(content);
        String id = String.valueOf(systemID);
        for (JsonElement el : obj) {
            JsonObject jso = (JsonObject) el;
            if (jso.get("system_id").getAsString().equals(id)) {
                return new int[]{
                        jso.get("ship_kills").getAsInt(),
                        jso.get("pod_kills").getAsInt(),
                };
            }
        }
        return new int[]{0, 0};
    }
}
