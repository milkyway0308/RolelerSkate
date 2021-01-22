package skywolf.rolelerskate.minecraft;

import br.com.azalim.mcserverping.MCPing;
import br.com.azalim.mcserverping.MCPingOptions;
import br.com.azalim.mcserverping.MCPingResponse;

import java.io.IOException;

public class MinecraftPingUtil {

    public static MCPingResponse ping(String text) throws IOException {
        MCPingOptions opt = new MCPingOptions();
        if (text.contains(":")) {
            String substring01 = text.substring(0, text.indexOf(":"));
            String substring02 = text.substring(text.indexOf(":") + 1);
            opt.setHostname(substring01);
            opt.setPort(Integer.parseInt(substring02));
        } else opt.setHostname(text);
        return MCPing.getPing(opt);
    }
}
