package by.offvanhooijdonk.tofreedom.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "stories")
public class StoryModel {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "mood_option")
    private int moodOption;
    @ColumnInfo(name = "text")
    private String text;
    @ColumnInfo(name = "date_created")
    private long dateCreated;
    @ColumnInfo(name = "type")
    private int type; // make as ENUM and add converter

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getMoodOption() {
        return moodOption;
    }

    public void setMoodOption(int moodOption) {
        this.moodOption = moodOption;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof StoryModel && id == ((StoryModel) o).id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public enum Mood {

        VERY_BAD(0), BAD(1), NEUTRAL(2), GOOD(3), VERY_GOOD(4);
        private int index;

        Mood(int i) {
            index = i;
        }

        public int getIndex() {
            return index;
        }

        public static Mood fromIndex(int i) {
            return values()[i];
        }
    }

    public enum Type {
        FEEL_TODAY(0), FUTURE_PLAN(1);

        private int index;

        Type(int i) {
            index = i;
        }

        public int getIndex() {
            return index;
        }

        public static Type fromIndex(int i) {
            return values()[i];
        }
    }
}
