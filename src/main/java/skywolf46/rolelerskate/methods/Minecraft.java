package skywolf46.rolelerskate.methods;

import br.com.azalim.mcserverping.MCPingResponse;
import discord4j.core.object.entity.Message;
import discord4j.rest.util.Color;
import skywolf46.rolelerskate.minecraft.MinecraftPingUtil;

import java.io.IOException;
import java.net.MalformedURLException;

import static skywolf46.rolelerskate.RolelerSkate.*;

public class Minecraft {
    public static void ping(Message message, String parameter) {
        Message msg = message.getChannel().block().createEmbed(spec -> {
            spec.setColor(Color.GRAY)
                    .setTitle("데이터를 불러오는 중입니다.")
                    .setDescription("봇이 데이터를 처리하는 동안 대기해주세요.");
        }).block();
        try {
            MCPingResponse resp = MinecraftPingUtil.ping(parameter);
            msg.edit(mSpec -> {
                mSpec.setEmbed(spec -> {
                    spec.setColor(Color.GREEN)
                            .addField(resp.getDescription().getStrippedText(),
                                    "**버전 :** " + resp.getVersion().getName() + " ( 프로토콜 " + resp.getVersion().getProtocol() + " ) \n" +
                                            "**플레이어 : **" + resp.getPlayers().getOnline() + " / " + resp.getPlayers().getMax() + "\n"
                                            + "\n\n _서버 반응 속도 " + resp.getPing() + "ms_"
                                    , false);
                });
            }).block();
        } catch (NumberFormatException ex) {
            msg.edit(mSpec -> {
                mSpec.setEmbed(spec -> {
                    spec.setColor(Color.RED)
                            .addField("오류!", "포트가 숫자가 아닙니다.", false);
                });
            }).block();
        } catch (MalformedURLException ex) {
            msg.edit(mSpec -> {
                mSpec.setEmbed(spec -> {
                    spec.setColor(Color.RED)
                            .addField("오류!", "URL이 정상이 아닙니다..", false);
                });
            }).block();
        } catch (IOException ex) {
            msg.edit(mSpec -> {
                mSpec.setEmbed(spec -> {
                    spec.setColor(Color.RED)
                            .addField("오류!", "연결에 실패하였습니다.", false)
                            .addField(ex.getClass().getName(), ex.getMessage() == null || ex.getMessage().isEmpty() ? "오류 메시지가 없습니다." : ex.getMessage(), false);
                });
            }).block();
        } catch (Exception ex) {
            msg.edit(mSpec -> {
                mSpec.setEmbed(spec -> {
                    spec.setColor(Color.RED)
                            .addField(getRandomMessage(), "애석하게도 새로운 오류가 발생한 것 같군요.", false)
                            .addField(ex.getClass().getName(), ex.getMessage(), false)
                            .setFooter("RolelerSkate " + VERSION + " | Error", null);
                });
            }).block();
        }
    }
}
