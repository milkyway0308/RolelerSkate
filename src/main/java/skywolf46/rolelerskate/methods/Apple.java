package skywolf46.rolelerskate.methods;

import discord4j.core.object.entity.Message;
import discord4j.rest.util.Color;
import skywolf46.rolelerskate.apple.AppleProductFinder;

import java.util.List;

import static skywolf46.rolelerskate.RolelerSkate.VERSION;
import static skywolf46.rolelerskate.RolelerSkate.getRandomMessage;

public class Apple {
    public static void searchAccessories(Message message, String target){
        Message msg = message.getChannel().block().createEmbed(spec -> {
            spec.setColor(Color.GRAY)
                    .addField("검색중..", "애플 스토어에서 데이터를 받아오는 중입니다.", false)
                    .setFooter("RolelerSkate " + VERSION, null);
        }).block();
        try {
            List<AppleProductFinder.AppleProduct> product = AppleProductFinder.find(target);
            if (product == null) {
                msg.edit(mSpec -> {
                    mSpec.setEmbed(spec -> {
                        spec.setColor(Color.RED)

                                .addField("오류", "애플 스토어에서 데이터를 받아올 수 없습니다.", false)
                                .setFooter("RolelerSkate " + VERSION, null);
                    });
                }).block();
                return;
            }
            if (product.size() <= 0) {
                msg.edit(mSpec -> {
                    mSpec.setEmbed(spec -> {
                        spec.setColor(Color.RED)
                                .addField("오류", "애플 스토어에서 일치하는 데이터를 찾지 못했습니다.", false)
                                .setFooter("RolelerSkate " + VERSION, null);
                    });
                }).block();
                return;
            }
            AppleProductFinder.AppleProduct pr = product.get(0);
            msg.edit(mSpec -> {
                mSpec.setEmbed(spec -> {
                    spec.setColor(Color.GREEN)
                            .setTitle(pr.getName())
                            .addField("가격", pr.getPrice(), true)
                            .addField("신상 여부", String.valueOf(pr.isNew()), true)
                            .setUrl(pr.getProductURL())
                            .setThumbnail(pr.getImageURL())
                            .setFooter("RolelerSkate " + VERSION, null);
                });
            }).block();

        } catch (Exception ex) {
            ex.printStackTrace();
            message.getChannel().block().createEmbed(spec -> {
                spec.setColor(Color.RED)
                        .addField(getRandomMessage(), "애석하게도 새로운 오류가 발생한 것 같군요.", false)
                        .addField(ex.getClass().getName(), ex.getMessage(), false)
                        .setFooter("RolelerSkate " + VERSION + " | Error", null);
            }).block();
        }
    }
}
