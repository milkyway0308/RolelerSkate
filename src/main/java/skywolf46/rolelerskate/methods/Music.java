package skywolf46.rolelerskate.methods;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Message;
import discord4j.rest.util.Color;
import skywolf46.rolelerskate.RolelerSkate;
import skywolf46.rolelerskate.voice.ThinkingChair;

import java.util.HashMap;

import static skywolf46.rolelerskate.RolelerSkate.THINKING;
import static skywolf46.rolelerskate.RolelerSkate.VERSION;

@SuppressWarnings("ConstantConditions")
public class Music {


    public static void poll(Message message) {
        if (message.getGuildId().isEmpty())
            return;
        synchronized (Music.class) {
            ThinkingChair chair = THINKING.get(message.getGuildId().get());
            if (chair != null) {
                if (!chair.hasNext()) {
                    message.getChannel().block().createEmbed(spec -> {
                        spec.setColor(Color.GREEN)
                                .addField("정보", "재생중인 음악이 존재하지 않습니다.", false)
                                .setFooter("RolelerSkate " + VERSION, null);
                    }).block();
                    return;
                }
                chair.skip();
                message.getChannel().block().createEmbed(spec -> {
                    spec.setColor(Color.GREEN)
                            .addField("정보", "음악이 스킵되었습니다.", false)
                            .setFooter("RolelerSkate " + VERSION, null);
                }).block();
            } else {
                message.getChannel().block().createEmbed(spec -> {
                    spec.setColor(Color.RED)
                            .addField("이런!", "봇이 통화방에 참여한 상태가 아닙니다.", false)
                            .setFooter("RolelerSkate " + VERSION, null);
                }).block();
            }
        }
    }

    public static void peek(Message message) {
        if (message.getGuildId().isEmpty())
            return;
        synchronized (RolelerSkate.class) {
            ThinkingChair chair = THINKING.get(message.getGuildId().get());
            if (chair != null) {
                if (!chair.hasNext()) {
                    message.getChannel().block().createEmbed(spec -> {
                        spec.setColor(Color.GREEN)
                                .addField("정보", "재생중인 음악이 존재하지 않습니다.", false)
                                .setFooter("RolelerSkate " + VERSION, null);
                    }).block();
                    return;
                }
                AudioTrack tr = chair.getCurrentTrack();

                message.getChannel().block().createEmbed(spec -> {
                    spec.setColor(Color.GREEN)
                            .setTitle(tr.getInfo().title)
                            .setUrl(tr.getInfo().uri)
                            .addField("재생 시간", toMinuteSecond((int) (tr.getPosition() / 1000)) + " / " + toMinuteSecond((int) (tr.getDuration() / 1000)), false)
                            .setFooter("RolelerSkate " + VERSION, null);

                }).block();
            } else {
                message.getChannel().block().createEmbed(spec -> {
                    spec.setColor(Color.RED)
                            .addField("이런!", "봇이 통화방에 참여한 상태가 아닙니다.", false)
                            .setFooter("RolelerSkate " + VERSION, null);
                }).block();
            }
        }
    }

    public static void queue(Message message, String url) {
        if (message.getGuildId().isEmpty())
            return;
        message.delete().block();

        ThinkingChair chair = THINKING.get(message.getGuildId().get());
        if (chair != null) {
            Message msg = message.getChannel().block().createEmbed(spec -> {
                spec.setColor(Color.GRAY)
                        .addField("정보", "링크를 불러오는 중...", false);
            }).block();

            ThinkingChair.PLAYER_MANAGER.loadItem(url, new AudioLoadResultHandler() {
                @Override
                public void trackLoaded(AudioTrack track) {
                    msg.edit(mSpec -> {
                        mSpec.setEmbed(spec -> {
                            spec
                                    .setColor(Color.GREEN)
                                    .setTitle(track.getInfo().title)
                                    .setUrl(track.getInfo().uri)
                                    .setDescription("다음 대기열에 1개의 영상이 추가되었습니다.");
                        });
                    }).block();
                    chair.queue(track);
                }

                @Override
                public void playlistLoaded(AudioPlaylist playlist) {
                    msg.edit(mSpec -> {
                        mSpec.setEmbed(spec -> {
                            spec
                                    .setColor(Color.GREEN)
                                    .setTitle(playlist.getName())
                                    .setUrl(url)
                                    .setDescription("다음 대기열에 " + playlist.getTracks().size() + "개의 영상이 추가되었습니다.");
                        });
                    }).block();
                    for (AudioTrack tr : playlist.getTracks())
                        chair.queue(tr);
                }

                @Override
                public void noMatches() {
                    msg.edit(mSpec -> {
                        mSpec.setEmbed(spec -> {
                            spec
                                    .setColor(Color.GREEN)
                                    .setTitle("오류 발생!")
                                    .setUrl(url)
                                    .setDescription("일치하는 영상이 존재하지 않아 대기열 추가에 실패하였습니다.");
                        });
                    }).block();
                }

                @Override
                public void loadFailed(FriendlyException exception) {
                    msg.edit(mSpec -> {
                        mSpec.setEmbed(spec -> {
                            spec
                                    .setColor(Color.GREEN)
                                    .setTitle("오류 발생!")
                                    .setUrl(url)
                                    .setDescription("링크에서 데이터를 불러 올 수 없습니다.");
                        });
                    }).block();
                }
            });

        } else {
            message.getChannel().block().createEmbed(spec -> {
                spec.setColor(Color.RED)
                        .addField("이런!", "봇이 통화방에 참여한 상태가 아닙니다.", false)
                        .setFooter("RolelerSkate " + VERSION, null);
            }).block();
        }
    }

    public static void setRepeat(Message message, boolean isRepeat) {
        message.delete().block();
        synchronized (RolelerSkate.class) {
            ThinkingChair chair = THINKING.get(message.getGuildId().get());
            if (chair != null) {
                chair.setRepeat(isRepeat);
                if (isRepeat) {
                    message.getChannel().block().createEmbed(spec -> {
                        spec.setColor(Color.GREEN)
                                .addField("적용 완료!", "음악 큐의 상태가 **반복** 상태로 변경되었습니다.", false)
                                .setFooter("RolelerSkate " + VERSION, null);
                    }).block();
                } else {
                    message.getChannel().block().createEmbed(spec -> {
                        spec.setColor(Color.GREEN)
                                .addField("적용 완료!", "음악 큐의 상태가 **반복 안함** 상태로 변경되었습니다.", false)
                                .setFooter("RolelerSkate " + VERSION, null);
                    }).block();
                }
            } else {
                message.getChannel().block().createEmbed(spec -> {
                    spec.setColor(Color.RED)
                            .addField("이런!", "봇이 통화방에 참여한 상태가 아닙니다.", false)
                            .setFooter("RolelerSkate " + VERSION, null);
                }).block();
            }
        }
    }

    private static String toMinuteSecond(int sec) {
        int min = sec / 60;
        sec -= min * 60;
        return min + "분 " + sec + "초";
    }

}
