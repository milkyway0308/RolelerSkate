package skywolf46.rolelerskate.util;

import discord4j.core.object.entity.Message;
import discord4j.rest.util.Color;
import skywolf46.rolelerskate.RolelerSkate;

import java.lang.reflect.Method;
import java.util.HashMap;

public class MethodParser {
    private static HashMap<String, MethodInvoker> invokers = new HashMap<>();

    public static void parse(Message msg, String where) {
        System.out.println(where);
        int startIndex = where.indexOf("(");
        int endIndex = where.lastIndexOf(")");
        if (startIndex == -1 || endIndex == -1 || endIndex < startIndex)
            return;
        int dotIndex = where.indexOf(".");
        if (dotIndex >= startIndex)
            return;
        String methods = where.substring(0, startIndex);
        if (invokers.containsKey(methods)) {
            invoke(msg, invokers.get(methods), where.substring(startIndex + 1, endIndex));
            return;
        }
        String className = methods.substring(0, methods.indexOf("."));
        String methodName = methods.substring(className.length() + 1);
        try {
            Class<?> cl = Class.forName("skywolf46.rolelerskate.methods." + className);
            MethodInvoker mi = null;
            for (Method mtd : cl.getMethods()) {
                if (mtd.getName().equals(methodName)) {
                    //Construct MethodInvoker
                    mi = new MethodInvoker(mtd);
                    invokers.put(methodName, mi);
                    break;
                }
            }
            if(mi == null)
                return;
        } catch (Exception ignored) {
//            ignored.printStackTrace();
            return;
        }
        System.out.println(invokers.get(methodName));
        invoke(msg, invokers.get(methodName), startIndex + 1 == endIndex ? "" : where.substring(startIndex + 1, endIndex));
    }

    private static void invoke(Message msg, MethodInvoker methodInvoker, String where) {
        Object[] ox = ObjectParsingUtil.parseArray(where,
                (ix, str) -> {
                    msg.getChannel().block().createEmbed(spec -> {
                        spec.setColor(Color.RED)
                                .setTitle("삽질을 중단하십시오")
                                .setDescription("파라미터를 파싱할 수 없습니다.")
                                .setThumbnail("https://i.imgur.com/orJbJTI.png")
                                .addField("파싱에 실패한 값", str, false)
                                .setFooter("RolelerSkate " + RolelerSkate.VERSION, null);
                    }).block();
                });
        if (ox == null)
            return;
        methodInvoker.invoke(msg, ox);
    }
}
