package skywolf46.rolelerskate.methods;

import discord4j.core.object.entity.Message;
import discord4j.rest.util.Color;
import skywolf46.rolelerskate.maplestory.CharacterInfoGathering;
import skywolf46.rolelerskate.maplestory.UserIDGathering;

import java.io.IOException;

import static skywolf46.rolelerskate.RolelerSkate.VERSION;
import static skywolf46.rolelerskate.RolelerSkate.getRandomMessage;


public class Maple {
    public static void search(Message message, String character) {
        Message msg = message.getChannel().block().createEmbed(spec -> {
            spec.setColor(Color.GRAY)
                    .addField("검색중..", "메이플스토리 캐릭터 " + character + "의 정보를 검색중입니다.", false)
                    .setFooter("RolelerSkate " + VERSION, null);
        }).block();
        long start = System.currentTimeMillis();
        try {
            String id = UserIDGathering.gatherUserID(character);
            if (id == null) {
                msg.edit(mSpec -> {
                    mSpec.setEmbed(spec -> {
                        spec.setColor(Color.RED)
                                .addField("오류", "캐릭터 " + character + "은 등록된 캐릭터가 아닙니다.", false)
                                .setFooter("RolelerSkate " + VERSION, null);
                    });
                }).block();
                return;
            }
            CharacterInfoGathering.CharacterInfo info = CharacterInfoGathering.gather(character, id);
            if (info == null) {
                msg.edit(mSpec -> {
                    mSpec.setEmbed(spec -> {
                        spec.setColor(Color.RED)
                                .addField("오류", "캐릭터 " + character + "의 정보를 가져오던 중 오류가 발생하였습니다.", false)
                                .setFooter("RolelerSkate " + VERSION, null);
                    });
                }).block();
                return;
            }
            msg.edit(mSpec -> {
                mSpec.setEmbed(spec -> {
                    spec.setColor(Color.CYAN)
                            .setTitle("캐릭터 - " + character)
                            .setImage(info.getAvatarURL())
                            .addField("레벨", info.getLevel(), true)
                            .addField("직업", info.getClassName(), true)
                            .addField("서버", info.getServer(), true)
                            .setFooter("RolelerSkate " + VERSION + " | " + (System.currentTimeMillis() - start) + "ms", null);
                });
            }).block();

        } catch (IOException ex) {
            message.getChannel().block().createEmbed(spec -> {
                spec.setColor(Color.RED)
                        .addField(getRandomMessage(), "애석하게도 새로운 오류가 발생한 것 같군요.", false)
                        .addField(ex.getClass().getName(), ex.getMessage(), false)
                        .setFooter("RolelerSkate " + VERSION + " | Error", null);
            }).block();
        }
    }
}
