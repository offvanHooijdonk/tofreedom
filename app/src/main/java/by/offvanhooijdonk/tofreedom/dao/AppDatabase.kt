package by.offvanhooijdonk.tofreedom.dao

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

import by.offvanhooijdonk.tofreedom.model.StoryModel

@Database(entities = [StoryModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun storyDao(): StoryDao

    companion object {
        private const val DB_NAME = "to-freedom-v0.3"

        fun buildDatabase(ctx: Context): AppDatabase {
            return Room.databaseBuilder<AppDatabase>(ctx, AppDatabase::class.java, DB_NAME).build()
        }
    }
}
