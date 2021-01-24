package skywolf46.rolelerskate.methods;

import discord4j.core.object.entity.Message;
import discord4j.rest.util.Color;

import static skywolf46.rolelerskate.RolelerSkate.VERSION;
import static skywolf46.rolelerskate.RolelerSkate.getRandomMessage;

public class Errors {
    public static void throwException(Message message){
        String substring = message.getContent();
        substring = substring.substring(substring.indexOf("(") + 1, substring.lastIndexOf(")")).trim();
        if (substring.isEmpty())
            substring = "java.lang.Exception";
        try {
            Class<?> cls = Class.forName(substring);
            if (!Throwable.class.isAssignableFrom(cls))
                throw new ClassCastException("대상 클래스가 Throwable을 상속받은 클래스가 아닙니다.");
            throw (Throwable) cls.getConstructor(String.class).newInstance((Object) null);
        } catch (Throwable ex) {
            message.getChannel().block().createEmbed(spec -> {
                spec.setColor(Color.RED)
                        .addField(getRandomMessage(), "애석하게도 새로운 오류가 발생한 것 같군요.", false)
                        .addField(ex.getClass().getName(), ex.getMessage() == null || ex.getMessage().isEmpty() ? "메시지 없음" : ex.getMessage(), false)
                        .setFooter("RolelerSkate " + VERSION + " | Error", null);
            }).block();
        }
    }
}
