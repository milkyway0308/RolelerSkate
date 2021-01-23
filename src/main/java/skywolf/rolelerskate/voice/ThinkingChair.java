package skywolf.rolelerskate.voice;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackState;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ThinkingChair extends AudioEventAdapter {
    public static final AudioPlayerManager PLAYER_MANAGER;

    static {
        PLAYER_MANAGER = new DefaultAudioPlayerManager();
        // This is an optimization strategy that Discord4J can utilize to minimize allocations
        PLAYER_MANAGER.getConfiguration().setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);
        AudioSourceManagers.registerRemoteSources(PLAYER_MANAGER);
        AudioSourceManagers.registerLocalSource(PLAYER_MANAGER);

    }

//    private final List<AudioTrack> queue;

    private final AudioPlayer player;

    private List<AudioTrack> tracks = new ArrayList<>();

    private ThinkingChairAudioProvider prov;

    private static Random r = new Random();

    private static ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    public ThinkingChair() {
        // The queue may be modifed by different threads so guarantee memory safety
        // This does not, however, remove several race conditions currently present
//        queue = Collections.synchronizedList(new LinkedList<>());
        this.player = PLAYER_MANAGER.createPlayer();
        player.addListener(this);
        prov = new ThinkingChairAudioProvider(this.player);
    }

    public AudioTrack getCurrentTrack() {
        return player.getPlayingTrack();
    }


    private void playNext() {
        rwLock.writeLock().lock();
        player.stopTrack();
        if (tracks.size() <= 0) {
            return;
        }
        player.startTrack(tracks.remove(0), true);
        rwLock.writeLock().unlock();
    }

    public ThinkingChairAudioProvider getProvider() {
        return prov;
    }

//    public void restart() {
//        reload();
//        System.out.println(trackRepeat);
//        player.startTrack(trackRepeat, true);
//
//    }

    public void stop() {
        rwLock.writeLock().lock();
        player.stopTrack();
        player.destroy();
        tracks.clear();
        rwLock.writeLock().unlock();
    }

    @Override
    public void onTrackEnd(final AudioPlayer player, final AudioTrack track, final AudioTrackEndReason endReason) {
//        System.out.println(endReason);
        if (endReason.mayStartNext) {
            playNext();
        }
    }


    public void skip() {
        playNext();
    }


    public void queue(AudioTrack track) {
        boolean playNext = false;
        rwLock.writeLock().lock();
        tracks.add(track);
        playNext = player.isPaused() || player.getPlayingTrack() == null || player.getPlayingTrack().getState() == AudioTrackState.INACTIVE ||
                (player.getPlayingTrack().getState() != AudioTrackState.PLAYING
                        &&
                        player.getPlayingTrack().getState() != AudioTrackState.LOADING
                );
        rwLock.writeLock().unlock();
        if (playNext)
            playNext();
    }
}
