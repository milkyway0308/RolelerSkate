package skywolf46.rolelerskate.util;

import discord4j.core.object.entity.Message;
import discord4j.rest.util.Color;
import skywolf46.rolelerskate.RolelerSkate;

import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodInvoker {
    private final Method mtd;
    private final Class<?>[] cls;

    public MethodInvoker(Method mtd) {
        cls = (this.mtd = mtd).getParameterTypes();
    }

    public void invoke(Message mtd, Object[] ox) {
        if (ox.length + 1 != cls.length) {
            onUnmatchedRaw(mtd, ox);
            return;
        }
        for (int i = 0; i < ox.length; i++) {
            if (!ox[i].getClass().equals(cls[i + 1])) {
                if (cls[i + 1].equals(String.class)) {
                    ox[i] = ox[i].toString();
                    continue;
                }
                onUnmatchedRaw(mtd, ox);
                return;
            }
        }
        Object[] arx = new Object[ox.length + 1];
        arx[0] = mtd;
        System.arraycopy(ox, 0, arx, 1, ox.length);
        RolelerSkate.EXECUTOR.submit(() -> {
            try {
                this.mtd.invoke(null, arx);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }

    private void onUnmatchedRaw(Message mtd, Object[] ox) {
        Class<?>[] cx = new Class[ox.length];
        for (int x = 0; x < ox.length; x++)
            cx[x] = ox[x].getClass();
        onUnmatched(mtd, cx);
    }

    public void onUnmatched(Message mtd, Class<?>[] given) {
        mtd.getChannel().block().createEmbed(spec -> {
            String target = toString(false, given);
            target = target.isEmpty() ? "없음" : target;
            spec.setColor(Color.RED)
                    .setTitle("삽질을 중단하십시오")
                    .setDescription("대상 메서드의 파라미터와 주어진 값이 매칭되지 않습니다.")
                    .setThumbnail("https://i.imgur.com/orJbJTI.png")
                    .addField("메서드 파라미터", toString(true, cls), false)
                    .addField("주어진 값", target, false)
                    .setFooter("RolelerSkate " + RolelerSkate.VERSION, null);
            System.out.println("Complete! blocking.");
        }).block();
    }

    private String toString(boolean ignoreFirst, Class<?>[] cx) {
        StringBuilder sx = new StringBuilder();
        for (Class<?> c : cx) {
            if (ignoreFirst) {
                ignoreFirst = false;
                continue;
            }
            sx.append(c.getSimpleName()).append(", ");
        }
        if (sx.length() > 0)
            sx.delete(sx.length() - 2, sx.length());
        return sx.toString();
    }
}
