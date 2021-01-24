package skywolf46.rolelerskate.methods;

import discord4j.core.object.entity.Message;
import discord4j.rest.util.Color;
import skywolf46.rolelerskate.eve.universe.JumpCatcher;
import skywolf46.rolelerskate.eve.universe.KillMailCatcher;
import skywolf46.rolelerskate.eve.universe.SystemSearcher;

import java.io.IOException;

import static skywolf46.rolelerskate.RolelerSkate.VERSION;
import static skywolf46.rolelerskate.RolelerSkate.getRandomMessage;

public class Eve {
    public static void systemOf(Message message, String where) {
        try {
            long id = SystemSearcher.requestSystemID(where);
            message.getChannel().block().createEmbed(spec -> {
                if (id == -1) {
                    spec
                            .setColor(Color.RED)
                            .addField("오류", "이브 온라인 API에서 해당되는 성계의 데이터를 찾지 못했습니다.", false)
                            .setFooter("RolelerSkate " + VERSION, null);
                } else {
                    spec
                            .setColor(Color.GREEN)
                            .addField("정보", "성계 시스템 ID: " + id, false)
                            .setFooter("RolelerSkate " + VERSION, null);
                }
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

    public static void guessCamping(Message message, String area) {
        long start = System.currentTimeMillis();
        Message mm = message.getChannel().block().createEmbed(spec -> {
            spec
                    .setColor(Color.GRAY)
                    .addField("정보", "데이터를 불러오는 중입니다...", false)
                    .setFooter("RolelerSkate " + VERSION, null);
        }).block();

        try {
            long id = SystemSearcher.requestSystemID(area);
            if (id == -1) {
                mm.edit(msg -> {

                    msg.setEmbed(spec -> {
                        spec.setColor(Color.RED)
                                .addField("오류", "이브 온라인 API에서 해당되는 성계의 데이터를 찾지 못했습니다.", false)
                                .setFooter("RolelerSkate " + VERSION, null);
                    });
                }).block();
                return;
            }

            int jump;
            int[] killed;

            try {
                jump = JumpCatcher.catchJump(id);
            } catch (Exception ex) {
                jump = -2;
            }
            try {
                killed = KillMailCatcher.catchKillMail(id);
            } catch (Exception ex) {
                ex.printStackTrace();
                killed = null;
            }
            int[] finalKilled = killed;
            int finalJump = jump;
            mm.edit(msg -> {
                msg.setEmbed(spec -> {
                    int gateCampingDangerous = 0;
                    if (finalKilled != null) {
                        if (finalJump <= 0)
                            gateCampingDangerous = -1;
                        else {
                            gateCampingDangerous += (double) finalKilled[1] / (double) finalKilled[0] * 50d;
                            gateCampingDangerous += (double) finalKilled[0] * 2 / (double) finalJump * 50d;
                        }
                    }
                    gateCampingDangerous = Math.max(0, Math.min(100, gateCampingDangerous));
                    spec.setColor(gateCampingDangerous < 20 ? Color.GREEN : (gateCampingDangerous < 30 ? Color.GRAY : Color.RED))
                            .setTitle("성계 : " + area)
                            .addField("데이터 불러오기 완료 (" + (System.currentTimeMillis() - start) + "ms)",
                                    "최근 1시간간 해당 성계의 기록\n**점프 횟수:** "
                                            + (finalJump == -2 ? "오류로 인한 데이터 소실" : (finalJump == -1 ? "데이터 없음" : finalJump + "회")) + "\n"
                                            + (finalKilled == null ? "**파괴된 함선:** 오류로 인한 데이터 소실\n**파괴된 캡슐:** 오류로 인한 데이터 소실\n"
                                            :
                                            "**파괴된 함선: **" + finalKilled[0] + "척\n"
                                                    + "**파괴된 캡슐: ** " + finalKilled[1] + "개\n"
                                    )
                                    , false)
                            .addField("평가", "**위험도:** " + gateCampingDangerous + " / 100\n\n"
                                            + "_100에 가까울수록 게이트 캠핑일 확률이 높은 지역입니다._\n"
                                            + "_해당 데이터는 1시간간의 데이터 지표이기 떄문에, 정확하지 않을 수 있습니다._"
                                    , false)
                            .setFooter("RolelerSkate " + VERSION, null);
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
