package by.offvanhooijdonk.tofreedom.helper.fancies;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import by.offvanhooijdonk.tofreedom.R;

public class MusicHelper {

    private static Set<MusicBean> musicSamples;
    private MusicBean pickedMusic;

    public MusicHelper() {
        if (musicSamples != null) {
            musicSamples = new HashSet<>();

            musicSamples.add(new MusicBean(R.raw.overture1812, R.integer.sound_time_1812, R.integer.punch_time_1812));
        }

        shuffle();
    }

    public void shuffle() {
        pickedMusic = getRandomMusicSample();
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

        public MusicBean(int trackRes, int volumeIncreaseTimeRes, int delayTillPunchRes) {
            this.trackRes = trackRes;
            this.volumeIncreaseTimeRes = volumeIncreaseTimeRes;
            this.delayTillPunchRes = delayTillPunchRes;
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

        @Override
        public int hashCode() {
            return trackRes;
        }
    }
}
