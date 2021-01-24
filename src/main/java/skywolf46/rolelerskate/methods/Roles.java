package skywolf46.rolelerskate.methods;

import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.Role;
import discord4j.core.object.reaction.ReactionEmoji;
import discord4j.rest.util.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static skywolf46.rolelerskate.RolelerSkate.VERSION;


@SuppressWarnings("ALL")
public class Roles {

    public static void removeUnused(Message message) {
        try {
            Guild guild = message.getGuild().block();
            if (guild.getOwnerId().asLong() == message.getAuthor().get().getId().asLong()) {
                Message msg = message.getChannel().block().createEmbed(spec -> {
                    spec
                            .setColor(Color.GREEN)
                            .addField("정보", "사용되지 않는 계급을 삭제합니다.", true)
                            .setFooter("RolelerSkate " + VERSION, null);
                }).block();
                List<Role> roles = guild.getRoles().toStream().collect(Collectors.toList());
                roles.remove(guild.getEveryoneRole().block());
                List<Member> members = new ArrayList<>();
                if (guild.getMembers().count().block() <= 1) {
                    message.getChannel().block().createEmbed(spec -> {
                        spec
                                .setColor(Color.GREEN)
                                .addField("이런!", "무언가 잘못되었습니다. \n봇이 방에 참여한 유저 데이터를 받아오지 못했습니다.", true)
                                .setFooter("RolelerSkate " + VERSION, null);
                    });
                    return;
                }
                for (Member mb : guild.getMembers().toIterable()) {
                    for (Role role : mb.getRoles().toIterable()) {
                        roles.remove(role);
                    }
                }

                if (roles.size() <= 0) {
                    msg.edit(mSpec -> {
                        mSpec.setEmbed(spec -> {
                            spec
                                    .setColor(Color.RED)
                                    .addField("정보", "사용되지 않은 계급이 발견되지 않았습니다.\n삭제 명령을 완료하였습니다.", true)
                                    .setFooter("RolelerSkate " + VERSION, null);
                        });
                    }).block();
                } else {

                    msg.edit(mSpec -> {
                        mSpec.setEmbed(spec -> {
                            spec
                                    .setColor(Color.GREEN)
                                    .addField("정보", "사용되지 않은 계급이 " + roles.size() + "개 발견되었습니다. 삭제 명령을 수행합니다.", true)
                                    .setFooter("RolelerSkate " + VERSION, null);
                        });
                    }).block();
//                            System.out.println(roles);
//                        for (Role rl : roles)
//                            rl.delete();
                    StringBuilder sb = new StringBuilder();
                    List<Role> failed = new ArrayList<>();
                    sb.append("```");
                    for (Role x : roles) {
                        try {
                            x.delete().block();
                            sb.append(x.getName()).append("\n");
                        } catch (Exception ex) {
                            failed.add(x);
                        }
                    }
                    sb.deleteCharAt(sb.length() - 1);
                    sb.append("```");

                    StringBuilder failedSb = new StringBuilder();
                    if (failed.size() > 0) {
                        failedSb.append("```");
                        for (Role x : failed) {
                            failedSb.append(x.getName()).append("\n");
                        }
                        failedSb.deleteCharAt(failedSb.length() - 1);
                        failedSb.append("```");
                    }
                    msg.edit(mSpec -> {
                        mSpec.setEmbed(spec -> {
                            spec
                                    .setColor(Color.GREEN)
                                    .addField("정보", "사용되지 않은 계급이 모두 삭제되었습니다.\n 삭제 명령을 완료하였습니다.", true);

                            if (failed.size() != roles.size()) {
                                spec.addField("삭제된 계급", sb.toString(), false);
                            }
                            if (failed.size() > 0) {
                                spec.addField("삭제에 실패한 계급", failedSb.toString(), false);
                            }
                            spec.setFooter("RolelerSkate " + VERSION, null);
                        });
                    }).block();
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            message.getChannel().block().createEmbed(spec -> {
                spec.setColor(Color.RED)
                        .addField("오류!", "계급 삭제 중 오류가 발생하였습니다.", false);
            }).block();
        }
    }

    public static void colorOf(Message message, String color) {
        try {
            java.awt.Color col = java.awt.Color.decode(color);
            Message msg = message.getChannel().block().createEmbed(spec -> {
                spec.setColor(Color.of(col.getRGB()))
                        .addField("원하는 색이 이 색이 맞나요? ", "이 색을 현재 닉네임의 색상으로 변경하려면 :heavy_check_mark: 이모지를 눌러주세요.\n\n _이 요청은 30초 뒤에 만료됩니다._", false)
                        .setDescription(message.getAuthor().get().getMention())
                        .setFooter("RolelerSkate - Color Changer | " + System.currentTimeMillis() + " | " + message.getAuthor().get().getId().asLong(), null);
            }).block();
            msg.addReaction(ReactionEmoji.unicode("✔"))
                    .block();
            msg.addReaction(ReactionEmoji.unicode("❌"))
                    .block();

            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            msg.delete("시간 만료").block();
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            message.getChannel().block().createEmbed(spec -> {
                spec.setColor(Color.RED)
                        .addField("오류!", "파싱할 수 없는 색 코드입니다. 다시 한번 확인해주세요.", false);
            }).block();
        } catch (Exception ex) {
            ex.printStackTrace();
            message.getChannel().block().createEmbed(spec -> {
                spec.setColor(Color.RED)
                        .addField("오류!", "파싱할 수 없는 색 코드입니다. 다시 한번 확인해주세요.", false);
            }).block();
        }
    }
}
