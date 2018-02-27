package by.offvanhooijdonk.tofreedom.helper.fancies;

import android.animation.ValueAnimator;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import by.offvanhooijdonk.tofreedom.R;
import by.offvanhooijdonk.tofreedom.app.ToFreedomApp;

public class MusicHelper {
    private static final int VOLUME_FADE_OUT_DURATION = 2500;
    private static final float VOLUME_LOWEST = 0.1f;
    private static final float VOLUME_FULL = 1.0f;

    private MediaPlayer player;
    private static Set<MusicBean> musicSamples;
    private MusicBean pickedMusic;
    private Context ctx;
    private ValueAnimator volumeAnim;
    private boolean isStopped = false;

    public MusicHelper(@NonNull Context context) {
        this.ctx = context;
        if (musicSamples == null) {
            initTracks();
        }
        shuffle();
    }

    public void play(@Nullable ValueAnimator.AnimatorUpdateListener lStart, @Nullable ValueAnimator.AnimatorUpdateListener lStop) {
        initPlayer();

        volumeAnim = ValueAnimator.ofFloat(VOLUME_LOWEST, VOLUME_FULL)
                .setDuration(ctx.getResources().getInteger(pickedMusic.getVolumeIncreaseTimeRes()));
        volumeAnim.setInterpolator(new AccelerateInterpolator(3.0f));
        volumeAnim.addUpdateListener(animation -> {
            if (isStopped) return;
            Float val = (Float) animation.getAnimatedValue();
            player.setVolume(val, val);
            if (lStart != null) lStart.onAnimationUpdate(animation);
        });

        player.start();
        volumeAnim.start();
        setupFadeTrack(lStop);
    }

    public int getPunchTime() {
        return ctx.getResources().getInteger(pickedMusic.getDelayTillPunchRes());
    }

    public void shuffle() {
        pickedMusic = getRandomMusicSample();
    }

    public void releasePlayer() {
        isStopped = true;
        if (volumeAnim != null) volumeAnim.cancel();
        if (player != null) player.release();
    }

    private void setupFadeTrack(@Nullable ValueAnimator.AnimatorUpdateListener lStop) {
        int duration = ctx.getResources().getInteger(pickedMusic.getDurationRes());
        new Handler().postDelayed(() -> {
            if (isStopped) return;
            ValueAnimator anim = ValueAnimator.ofFloat(VOLUME_FULL, VOLUME_LOWEST)
                    .setDuration(VOLUME_FADE_OUT_DURATION);
            anim.setInterpolator(new DecelerateInterpolator(2.0f));
            anim.addUpdateListener(animation -> {
                Float val = (Float) animation.getAnimatedValue();
                player.setVolume(val, val);
                if (Float.compare(animation.getAnimatedFraction(), 1.0f) == 0) {
                    Log.i(ToFreedomApp.LOG, "Stop sound!");
                    player.stop();
                }
                if (lStop != null) lStop.onAnimationUpdate(animation);
            });
            anim.start();
        }, duration - VOLUME_FADE_OUT_DURATION);
    }

    private void initTracks() {
        musicSamples = new HashSet<>();
        musicSamples.add(new MusicBean(R.raw.overture1812, R.integer.sound_time_1812, R.integer.punch_time_1812, R.integer.track_duration_1812));
    }

    private void initPlayer() {
        if (player != null) player.release();

        player = MediaPlayer.create(ctx, R.raw.overture1812);
        player.setVolume(VOLUME_LOWEST, VOLUME_LOWEST);
        player.seekTo(9500); //  TODO remove when tracks are cut
    }

    private MusicBean getRandomMusicSample() {
        int index = new Random().nextInt(musicSamples.size());

        MusicBean bean = null;
        int i = 0;
        for (MusicBean mb : musicSamples) {
            if (i == index) {
                bean = mb;
                break;
            }
            i++;
        }

        return bean;
    }

    private static class MusicBean {
        private int trackRes;
        private int volumeIncreaseTimeRes;
        private int delayTillPunchRes;
        private int durationRes;

        public MusicBean(int trackRes, int volumeIncreaseTimeRes, int delayTillPunchRes, int durationRes) {
            this.trackRes = trackRes;
            this.volumeIncreaseTimeRes = volumeIncreaseTimeRes;
            this.delayTillPunchRes = delayTillPunchRes;
            this.durationRes = durationRes;
        }

        public int getTrackRes() {
            return trackRes;
        }

        public void setTrackRes(int trackRes) {
            this.trackRes = trackRes;
        }

        public int getVolumeIncreaseTimeRes() {
            return volumeIncreaseTimeRes;
        }

        public void setVolumeIncreaseTimeRes(int volumeIncreaseTimeRes) {
            this.volumeIncreaseTimeRes = volumeIncreaseTimeRes;
        }

        public int getDelayTillPunchRes() {
            return delayTillPunchRes;
        }

        public void setDelayTillPunchRes(int delayTillPunchRes) {
            this.delayTillPunchRes = delayTillPunchRes;
        }

        public int getDurationRes() {
            return durationRes;
        }

        public void setDurationRes(int durationRes) {
            this.durationRes = durationRes;
        }

        @Override
        public int hashCode() {
            return trackRes;
        }
    }
}
