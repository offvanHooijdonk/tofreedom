package by.offvanhooijdonk.tofreedom.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import by.offvanhooijdonk.tofreedom.model.StoryModel;
import io.reactivex.Maybe;

@Dao
public interface StoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(StoryModel model);

    @Query("SELECT * FROM stories WHERE type = :type ORDER BY date_created asc")
    Maybe<List<StoryModel>> getAllByType(int type);

    @Query("SELECT * FROM stories WHERE id = :id AND type = 0")
    Maybe<StoryModel> getFeelTodayById(int id);
}
