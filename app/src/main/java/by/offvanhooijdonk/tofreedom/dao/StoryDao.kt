package by.offvanhooijdonk.tofreedom.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

import by.offvanhooijdonk.tofreedom.model.StoryModel
import io.reactivex.Maybe

@Dao
interface StoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(model: StoryModel)

    @Query("SELECT * FROM stories WHERE type = :type ORDER BY date_created asc")
    fun getAllByType(type: Int): Maybe<List<StoryModel>>

    @Query("SELECT * FROM stories WHERE id = :id AND type = 0")
    fun getFeelTodayById(id: Int): Maybe<StoryModel>
}
