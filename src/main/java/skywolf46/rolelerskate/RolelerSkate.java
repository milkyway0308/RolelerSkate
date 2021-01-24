package skywolf46.rolelerskate;

import br.com.azalim.mcserverping.MCPingResponse;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.VoiceStateUpdateEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.event.domain.message.ReactionAddEvent;
import discord4j.core.object.Embed;
import discord4j.core.object.entity.*;
import discord4j.core.object.entity.channel.VoiceChannel;
import discord4j.core.object.reaction.ReactionEmoji;
import discord4j.core.shard.GatewayBootstrap;
import discord4j.gateway.intent.IntentSet;
import discord4j.rest.util.Color;
import skywolf46.rolelerskate.apple.AppleProductFinder;
import skywolf46.rolelerskate.eve.universe.JumpCatcher;
import skywolf46.rolelerskate.eve.universe.KillMailCatcher;
import skywolf46.rolelerskate.eve.universe.SystemSearcher;
import skywolf46.rolelerskate.maplestory.CharacterInfoGathering;
import skywolf46.rolelerskate.maplestory.UserIDGathering;
import skywolf46.rolelerskate.minecraft.MinecraftPingUtil;
import skywolf46.rolelerskate.util.MethodParser;
import skywolf46.rolelerskate.voice.ThinkingChair;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
public class RolelerSkate {

    public static final String VERSION = "Alpha 0.1.0";

    // TODO fix it
    public static final HashMap<Snowflake, ThinkingChair> THINKING = new HashMap<>();
    public static ExecutorService EXECUTOR = Executors.newCachedThreadPool();

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
                    "하하, 자바가 사람 하나 잡겠네.",
                    "난 여기서 빠져 나가야 되겠어. 안되잖아?",
                    "판사님 이 코드는 제 고양이가 짰습니다",
                    "코드가! 덜 익었잖아!"
            )
    );

    private static GatewayDiscordClient gateway;


    private static Random random = new Random();

    public static String getRandomMessage() {
        return ERRORS.get(random.nextInt(ERRORS.size()));
    }

    public static void main(final String[] args) {
        final String token = "Your bot token here";
//        System.out.println(token);
        final DiscordClient client = DiscordClient.create(token);


        gateway = GatewayBootstrap.create(client).setEnabledIntents(
                IntentSet.all()
        ).login().block();
//        Activity
//        gateway.updatePresence(su);
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

//        for (Guild g : gateway.getGuilds().toIterable()) {
//            for (GuildChannel gg : g.getChannels().toIterable()) {
//                if (gg.getType() == Channel.Type.GUILD_VOICE) {
//                    if (gg.getName().equals("생각하는-의자")) {
//                        VoiceChannel vc = (VoiceChannel) gg;
//                        vc.join(spec -> {
//                            ThinkingChair thinkering = THINKING.computeIfAbsent(g.getId(), a -> new ThinkingChair());
//                            spec.setSelfDeaf(true);
//                            spec.setProvider(thinkering.getProvider());
//                        }).block();
//                    }
//                }
//            }
//        }
        gateway.on(VoiceStateUpdateEvent.class).subscribe(ev -> {
            if (ev.isJoinEvent()) {
                VoiceChannel vc = ev.getCurrent().getChannel().block();
                if (!vc.getName().equals("생각하는 의자"))
                    return;
                if (vc.isMemberConnected(gateway.getSelfId()).block()) {
                    return;
                }
                synchronized (RolelerSkate.class) {
                    ThinkingChair tc = THINKING.computeIfAbsent(vc.getGuildId(), xa -> new ThinkingChair());
                    vc.join(spec -> {
                        spec.setSelfDeaf(true);
//                        tc.restart();
                        spec.setProvider(tc.getProvider());

                    }).block();
                }
            } else {
                if (!ev.getOld().isPresent())
                    return;
                VoiceChannel vc = ev.getOld().get().getChannel().block();
                if (!vc.getName().equals("생각하는 의자"))
                    return;
                if (vc.getVoiceStates().count().block() == 1) {
                    synchronized (RolelerSkate.class) {
                        vc.getVoiceConnection().block().disconnect().block();
                        ThinkingChair tc = THINKING.remove(vc.getGuildId());
                        tc.stop();
                    }
                }
            }
//            VoiceChannel vc = (ev.isJoinEvent() ? ev.getCurrent().getChannel().block() : ev.getOld().get().getChannel().block());
//            if (vc.isMemberConnected(gateway.getSelfId()).block()) {
//                ThinkingChair tc = THINKING.get(ev.getCurrent().getGuildId());
//                if (tc == null)
//                    return;
//                if (ev.isLeaveEvent()) {
//                    if (vc.getVoiceStates().count().block() == 1) {
//                        tc.stop();
//                    }
//                } else if (ev.isJoinEvent()) {
//                    if (vc.getVoiceStates().count().block() == 2) {
//                        tc.restart();
//                    }
//                }
//            }
        });
        gateway
                .on(MessageCreateEvent.class).subscribe(event -> {
            final Message message = event.getMessage();
            if (!message.getAuthor().isPresent() || message.getAuthor().get().isBot())
                return;
            if (message.getContent().endsWith(");")) {
                MethodParser.parse(message, message.getContent());
                return;
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

    public static GatewayDiscordClient getGateway() {
        return gateway;
    }
}
