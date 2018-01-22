package by.offvanhooijdonk.tofreedom.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import by.offvanhooijdonk.tofreedom.model.StoryModel;

@Database(entities = {StoryModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "to-freedom-v0.3";

    public static AppDatabase buildDatabase(Context ctx) {
        return Room.databaseBuilder(ctx, AppDatabase.class, DB_NAME).build();
    }

    public abstract StoryDao storyDao();
}
