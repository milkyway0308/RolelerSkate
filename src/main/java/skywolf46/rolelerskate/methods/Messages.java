package skywolf46.rolelerskate.methods;

import discord4j.core.object.entity.Message;
import discord4j.rest.util.Color;
import skywolf46.rolelerskate.RolelerSkate;

import static skywolf46.rolelerskate.RolelerSkate.VERSION;
import static skywolf46.rolelerskate.RolelerSkate.getRandomMessage;

public class Messages {
    public static void removeSelf(Message message, int target) {
        if (target <= 0) {
            message.getChannel().block().createEmbed(spec -> {
                spec.setColor(Color.RED)
                        .addField("오류!", "삭제할 범위는 메시지 1개 이상이여야만 합니다.", false);
            }).block();
            return;
        }
        if (target >= 200) {
            message.getChannel().block().createEmbed(spec -> {
                spec.setColor(Color.RED)
                        .addField("오류!", "200개 이상의 범위는 해당 봇의 퍼포먼스를 위해 제한되어 있습니다.", false);
            }).block();
            return;
        }
        try {

            int cloneTarget = target;
            for (Message msg : message.getChannel().block().getMessagesBefore(message.getId()).toIterable()) {
                if (cloneTarget-- <= 0)
                    break;
                if (!msg.getAuthor().isPresent() || !msg.getAuthor().get().getId().equals(RolelerSkate.getGateway().getSelfId()))
                    continue;
                msg.delete().block();
            }
            message.getChannel().block().createMessage(target + " 메시지 내의 해당 봇이 전송한 메시지를 지웠습니다.").block();
        } catch (Exception ex) {
            message.getChannel().block().createEmbed(spec -> {
                spec.setColor(Color.RED)
                        .addField(getRandomMessage(), "애석하게도 새로운 오류가 발생한 것 같군요.", false)
                        .addField(ex.getClass().getName(), ex.getMessage(), false)
                        .setFooter("RolelerSkate " + VERSION + " | Error", null);
            }).block();
        }
    }
}
