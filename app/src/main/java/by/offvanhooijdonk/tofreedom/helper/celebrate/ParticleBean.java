package by.offvanhooijdonk.tofreedom.helper.celebrate;

import java.util.Objects;

public class ParticleBean {
    private int drawableColor;
    private float speedFrom;
    private float speedTo;
    private long duration;
    private float scaleFrom = 1.0f;
    private float scaleTo = 2.0f;
    private int number;

    public ParticleBean(int color, int number, float speedFrom, float speedTo, long duration, float scaleFrom, float scaleTo) {
        this.drawableColor = color;
        this.speedFrom = speedFrom;
        this.speedTo = speedTo;
        this.duration = duration;
        this.scaleFrom = scaleFrom;
        this.scaleTo = scaleTo;
        this.number = number;
    }

    public ParticleBean(int drawableColor, int number, float speedFrom, float speedTo, long duration) {
        this.drawableColor = drawableColor;
        this.speedFrom = speedFrom;
        this.speedTo = speedTo;
        this.duration = duration;
        this.number = number;
    }

    public int getDrawableColor() {
        return drawableColor;
    }

    public void setDrawableColor(int drawableColor) {
        this.drawableColor = drawableColor;
    }

    public float getSpeedFrom() {
        return speedFrom;
    }

    public void setSpeedFrom(float speedFrom) {
        this.speedFrom = speedFrom;
    }

    public float getSpeedTo() {
        return speedTo;
    }

    public void setSpeedTo(float speedTo) {
        this.speedTo = speedTo;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public float getScaleFrom() {
        return scaleFrom;
    }

    public void setScaleFrom(float scaleFrom) {
        this.scaleFrom = scaleFrom;
    }

    public float getScaleTo() {
        return scaleTo;
    }

    public void setScaleTo(float scaleTo) {
        this.scaleTo = scaleTo;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, drawableColor, duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParticleBean)) return false;
        ParticleBean that = (ParticleBean) o;
        return drawableColor == that.drawableColor &&
                Float.compare(that.speedFrom, speedFrom) == 0 &&
                Float.compare(that.speedTo, speedTo) == 0 &&
                duration == that.duration &&
                Float.compare(that.scaleFrom, scaleFrom) == 0 &&
                Float.compare(that.scaleTo, scaleTo) == 0 &&
                number == that.number;
    }
}
