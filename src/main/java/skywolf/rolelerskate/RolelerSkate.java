package skywolf.rolelerskate;

import br.com.azalim.mcserverping.MCPingResponse;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.event.domain.message.ReactionAddEvent;
import discord4j.core.object.Embed;
import discord4j.core.object.entity.*;
import discord4j.core.object.reaction.ReactionEmoji;
import discord4j.core.shard.GatewayBootstrap;
import discord4j.gateway.intent.IntentSet;
import discord4j.rest.util.Color;
import skywolf.rolelerskate.apple.AppleProductFinder;
import skywolf.rolelerskate.eve.universe.JumpCatcher;
import skywolf.rolelerskate.eve.universe.KillMailCatcher;
import skywolf.rolelerskate.eve.universe.SystemSearcher;
import skywolf.rolelerskate.maplestory.CharacterInfoGathering;
import skywolf.rolelerskate.maplestory.UserIDGathering;
import skywolf.rolelerskate.minecraft.MinecraftPingUtil;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
public class RolelerSkate {

    private static final String VERSION = "Alpha 0.0.18";

    private static ExecutorService EXECUTOR = Executors.newCachedThreadPool();

    private static List<String> ERRORS = new ArrayList<>(
            Arrays.asList(
                    "이런! 롤러스케이트 2077이 뻗었습니다.",
                    ":( 롤러스케이트에 문제가 발생하여 재부팅이 필요하지 않습니다.",
                    "503 Internal Mental Error",
                    "(시끄러운 귀뚜라미 소리)",
                    "브루투스 너마저?",
                    "괜찮습니다, 버그는 다시 자라요.",
                    ":ragequit",
                    "괜찮습니다, 곧 새로운 늑대를 해동해 드리겠습니다!",
                    "하하, 자바가 사람 하나 잡겠네."
            )
    );

    private static Random random = new Random();

    public static String getRandomMessage() {
        return ERRORS.get(random.nextInt(ERRORS.size()));
    }

    public static void main(final String[] args) {
        final String token = "Your bot token here";
//        System.out.println(token);
        final DiscordClient client = DiscordClient.create(token);


        final GatewayDiscordClient gateway = GatewayBootstrap.create(client).setEnabledIntents(
                IntentSet.all()
        ).login().block();
//        for (Guild g : gateway.getGuilds().toIterable()) {
//
//            int top = Integer.MAX_VALUE;
//            MessageChannel msg = null;
//            for (GuildChannel gg : g.getChannels().toIterable()) {
//                int level = gg.getRawPosition();
//                if (gg instanceof MessageChannel && level < top) {
//                    top = level;
//                    msg = (MessageChannel) gg;
//                }
//            }
//            msg.createEmbed(spec -> {
//                spec.setColor(Color.GREEN)
//                        .addField("안녕하신가, 세상!", "롤러스케이트 봇이 활성화 되었습니다.", false)
//                        .setFooter("RolelerSkate " + VERSION + "", null);
//            }).block();
//        }

        gateway
                .on(MessageCreateEvent.class).subscribe(event -> {
            final Message message = event.getMessage();
            if ("Roles.removeUnused();".equals(message.getContent())) {
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
                                .addField("오류!", "파싱할 수 없는 색 코드입니다. 다시 한번 확인해주세요.", false);
                    }).block();
                }
            } else if (message.getContent().startsWith("Roles.colorOf(")) {
                if (message.getContent().endsWith(");")) {
                    if (!message.getAuthor().isPresent() || message.getAuthor().get().isBot())
                        return;
                    String color = message.getContent();
                    color = message.getContent().substring(color.indexOf("(") + 1, color.length() - 2);
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
                        EXECUTOR.submit(() -> {
                            try {
                                Thread.sleep(30000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            msg.delete("시간 만료").block();
                        });
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
            } else if (message.getContent().startsWith("Messages.removeSelf(")) {
                if (!message.getContent().endsWith(");"))
                    return;
                String substring = message.getContent();
                substring = substring.substring(substring.indexOf("(") + 1, substring.lastIndexOf(")")).trim();
                int target = 400;
                try {
                    if (!substring.isEmpty()) {
                        target = Integer.parseInt(substring);
                    }
                } catch (Exception ex) {
                    message.getChannel().block().createEmbed(spec -> {
                        spec.setColor(Color.RED)
                                .addField("오류!", "삭제할 범위는 숫자여야만 합니다.", false);
                    }).block();
                }
                if (target <= 0) {
                    message.getChannel().block().createEmbed(spec -> {
                        spec.setColor(Color.RED)
                                .addField("오류!", "삭제할 범위는 메시지 1개 이상이여야만 합니다.", false);
                    }).block();
                    return;
                }
                try {

                    int cloneTarget = target;
                    for (Message msg : message.getChannel().block().getMessagesBefore(message.getId()).toIterable()) {
                        if (cloneTarget-- <= 0)
                            break;
                        if (!msg.getAuthor().isPresent() || !msg.getAuthor().get().getId().equals(gateway.getSelfId()))
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
            } else if (message.getContent().startsWith("Errors.throwException(")) {
                if (!message.getContent().endsWith(");"))
                    return;
                String substring = message.getContent();
                substring = substring.substring(substring.indexOf("(") + 1, substring.lastIndexOf(")")).trim();
                if (substring.isEmpty())
                    substring = "java.lang.Exception";
                try {
                    Class cls = Class.forName(substring);
                    if (!Throwable.class.isAssignableFrom(cls))
                        throw new ClassCastException("대상 클래스가 Throwable을 상속받은 클래스가 아닙니다.");
                    Throwable th = (Throwable) cls.getConstructor(String.class).newInstance((Object) null);
                    throw th;
                } catch (Throwable ex) {
                    message.getChannel().block().createEmbed(spec -> {
                        spec.setColor(Color.RED)
                                .addField(getRandomMessage(), "애석하게도 새로운 오류가 발생한 것 같군요.", false)
                                .addField(ex.getClass().getName(), ex.getMessage() == null || ex.getMessage().isEmpty() ? "메시지 없음" : ex.getMessage(), false)
                                .setFooter("RolelerSkate " + VERSION + " | Error", null);
                    }).block();
                }
            } else if (message.getContent().startsWith("Minecraft.ping(")) {
                if (!message.getContent().endsWith(");"))
                    return;
                String substring = message.getContent();
                substring = substring.substring(substring.indexOf("(") + 1, substring.lastIndexOf(")")).trim();
                if (substring.isEmpty()) {
                    message.getChannel().block().createEmbed(spec -> {
                        spec.setColor(Color.RED)
                                .addField("오류!", "반드시 주소를 입력해야 합니다.", false);
                    }).block();
                    return;
                }
                String finalSubstring = substring;
                EXECUTOR.submit(() -> {
                    try {
                        MCPingResponse resp = MinecraftPingUtil.ping(finalSubstring);
                        message.getChannel().block().createEmbed(spec -> {
                            spec.setColor(Color.GREEN)
                                    .addField(resp.getDescription().getStrippedText(),
                                            "**버전 :** " + resp.getVersion().getName() + " ( 프로토콜 " + resp.getVersion().getProtocol() + " ) \n" +
                                                    "**플레이어 : **" + resp.getPlayers().getOnline() + " / " + resp.getPlayers().getMax() + "\n"
                                                    + "\n\n _서버 반응 속도 " + resp.getPing() + "ms_"
                                            , false);
                        }).block();
                    } catch (NumberFormatException ex) {
                        message.getChannel().block().createEmbed(spec -> {
                            spec.setColor(Color.RED)
                                    .addField("오류!", "포트가 숫자가 아닙니다.", false);
                        }).block();
                    } catch (MalformedURLException ex) {
                        message.getChannel().block().createEmbed(spec -> {
                            spec.setColor(Color.RED)
                                    .addField("오류!", "URL이 정상이 아닙니다..", false);
                        }).block();
                    } catch (IOException ex) {
                        message.getChannel().block().createEmbed(spec -> {
                            spec.setColor(Color.RED)
                                    .addField("오류!", "연결에 실패하였습니다.", false)
                                    .addField(ex.getClass().getName(), ex.getMessage() == null || ex.getMessage().isEmpty() ? "오류 메시지가 없습니다." : ex.getMessage(), false);
                        }).block();
                    } catch (Exception ex) {
                        message.getChannel().block().createEmbed(spec -> {
                            spec.setColor(Color.RED)
                                    .addField(getRandomMessage(), "애석하게도 새로운 오류가 발생한 것 같군요.", false)
                                    .addField(ex.getClass().getName(), ex.getMessage(), false)
                                    .setFooter("RolelerSkate " + VERSION + " | Error", null);
                        }).block();
                    }
                });
            } else if (message.getContent().startsWith("Eve.systemOf(")) {
                if (!message.getContent().endsWith(");"))
                    return;
                String substring = message.getContent();
                substring = substring.substring(substring.indexOf("(") + 1, substring.lastIndexOf(")")).trim();
                String finalSubstring = substring;
                EXECUTOR.submit(() -> {
                    try {
                        long id = SystemSearcher.requestSystemID(finalSubstring);
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
                });
            } else if (message.getContent().startsWith("Eve.guessCamping(")) {
                if (!message.getContent().endsWith(");"))
                    return;
                long start = System.currentTimeMillis();
                String substring = message.getContent();
                substring = substring.substring(substring.indexOf("(") + 1, substring.lastIndexOf(")")).trim();
                String finalSubstring = substring;
                Message mm = message.getChannel().block().createEmbed(spec -> {
                    spec
                            .setColor(Color.GRAY)
                            .addField("정보", "데이터를 불러오는 중입니다...", false)
                            .setFooter("RolelerSkate " + VERSION, null);
                }).block();
                EXECUTOR.submit(() -> {
                    try {
                        long id = SystemSearcher.requestSystemID(finalSubstring);
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
                                        .setTitle("성계 : " + finalSubstring)
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
                });
            } else if (message.getContent().startsWith("Maple.search(")) {
                if (!message.getContent().endsWith(");"))
                    return;
                String substring = message.getContent();
                substring = substring.substring(substring.indexOf("(") + 1, substring.lastIndexOf(")")).trim();
                if (substring.isEmpty()) {
                    message.getChannel().block().createEmbed(spec -> {
                        spec.setColor(Color.RED)
                                .addField("오류", "Maple.search(CharacterName) 명령어는 빈 칸을 허용하지 않습니다.", false)
                                .setFooter("RolelerSkate " + VERSION, null);
                    });
                    return;
                }
                String finalSubstring = substring;
                Message msg = message.getChannel().block().createEmbed(spec -> {
                    spec.setColor(Color.GRAY)
                            .addField("검색중..", "메이플스토리 캐릭터 " + finalSubstring + "의 정보를 검색중입니다.", false)
                            .setFooter("RolelerSkate " + VERSION, null);
                }).block();
                long start = System.currentTimeMillis();

                EXECUTOR.submit(() -> {
                    try {
                        String id = UserIDGathering.gatherUserID(finalSubstring);
                        if (id == null) {
                            msg.edit(mSpec -> {
                                mSpec.setEmbed(spec -> {
                                    spec.setColor(Color.RED)
                                            .addField("오류", "캐릭터 " + finalSubstring + "은 등록된 캐릭터가 아닙니다.", false)
                                            .setFooter("RolelerSkate " + VERSION, null);
                                });
                            }).block();
                            return;
                        }
                        CharacterInfoGathering.CharacterInfo info = CharacterInfoGathering.gather(finalSubstring, id);
                        if (info == null) {
                            msg.edit(mSpec -> {
                                mSpec.setEmbed(spec -> {
                                    spec.setColor(Color.RED)
                                            .addField("오류", "캐릭터 " + finalSubstring + "의 정보를 가져오던 중 오류가 발생하였습니다.", false)
                                            .setFooter("RolelerSkate " + VERSION, null);
                                });
                            }).block();
                            return;
                        }
                        msg.edit(mSpec -> {
                            mSpec.setEmbed(spec -> {
                                spec.setColor(Color.CYAN)
                                        .setTitle("캐릭터 - " + finalSubstring)
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
                });
            } else if (message.getContent().startsWith("Apple.searchAccessories(")) {
                if (!message.getContent().endsWith(");"))
                    return;
                String substring = message.getContent();
                substring = substring.substring(substring.indexOf("(") + 1, substring.lastIndexOf(")")).trim();
                if (substring.isEmpty()) {
                    message.getChannel().block().createEmbed(spec -> {
                        spec.setColor(Color.RED)
                                .addField("오류!", "반드시 검색어를 입력해야 합니다.", false);
                    }).block();
                    return;
                }
                Message msg = message.getChannel().block().createEmbed(spec -> {
                    spec.setColor(Color.GRAY)
                            .addField("검색중..", "애플 스토어에서 데이터를 받아오는 중입니다.", false)
                            .setFooter("RolelerSkate " + VERSION, null);
                }).block();
                String finalSubstring = substring;
                EXECUTOR.submit(() -> {
                    try {
                        List<AppleProductFinder.AppleProduct> product = AppleProductFinder.find(finalSubstring);
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
                        if(product.size() <= 0){
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
                                        .setImage(pr.getImageURL())
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
                });

            }
        });

        gateway.on(ReactionAddEvent.class)
                .subscribe(ev -> {
                    try {
                        Message mv = ev.getMessage().block();
                        if (mv == null)
                            return;
                        if (mv.getUserData().id().equals(gateway.getSelfId().asString())) {
                            if (mv.getEmbeds().size() <= 0)
                                return;
                            Embed em = mv.getEmbeds().get(0);
                            String text = em.getFooter().get().getText();
                            List<String> splitter = Arrays.stream(text.split("\\|")).map(x -> x.trim()).collect(Collectors.toList());
                            if (splitter.get(0).equals("RolelerSkate - Color Changer")) {
                                if (splitter.get(2).equals(ev.getUserId().asString())) {
                                    long lastTime = Long.parseLong(splitter.get(1));
                                    if (System.currentTimeMillis() - lastTime > 30000) {
                                        return;
                                    }
                                    Color nextClr = em.getColor().get();
                                    mv.delete("임무를 완수했다").block();
                                    Member us = ev.getMember().get();
                                    us.getRoles().subscribe(rx -> {
                                        if (rx.getName().startsWith("RSColor_")) {
                                            us.removeRole(rx.getId()).block();
                                        }
                                    });
                                    String colorName;
                                    String roleName = "RSColor_" + (colorName = String.format("#%02x%02x%02x", nextClr.getRed(), nextClr.getGreen(), nextClr.getBlue()));
                                    Guild guild = ev.getGuild().block();
                                    Role target = null;
                                    for (Role r : guild.getRoles().toIterable()) {
                                        if (r.getName().equals(roleName)) {
                                            target = r;
                                            break;
                                        }
                                    }
                                    if (target == null) {

                                        target = guild.createRole(spec -> {
                                            spec.setName(roleName);
                                            spec.setColor(nextClr);
                                            spec.setReason("RolelerSkate colored role creation");
                                        }).block();
                                        Member rxTarget = guild.getMemberById(gateway.getSelfId()).block();
                                        int indx = rxTarget.getHighestRole().block().getRawPosition();
                                        target.changePosition(indx - 1).subscribe();
                                    }
                                    us.addRole(target.getId()).block();
                                    ev.getChannel().block().createEmbed(spec -> {
                                        spec.setColor(nextClr)
                                                .addField("변경 완료!", "닉네임 색상 변경에 성공하였습니다. \n현재 색상: " + colorName, false)
                                                .setFooter("RolelerSkate " + VERSION, null);
                                    }).block();
                                }
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        ev.getChannel().block().createEmbed(spec -> {
                            spec.setColor(Color.RED)
                                    .addField(getRandomMessage(), "애석하게도 새로운 오류가 발생한 것 같군요.", false)
                                    .addField(ex.getClass().getName(), ex.getMessage(), false)
                                    .setFooter("RolelerSkate " + VERSION + " | Error", null);
                        }).block();
                    }
                });

        gateway.onDisconnect().block();
    }
}
