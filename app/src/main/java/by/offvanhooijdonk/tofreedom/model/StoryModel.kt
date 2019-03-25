package by.offvanhooijdonk.tofreedom.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "stories")
data class StoryModel (
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    @ColumnInfo(name = "mood_option")
    var moodOption: Int = 0,
    @ColumnInfo(name = "text")
    var text: String? = null,
    @ColumnInfo(name = "date_created")
    var dateCreated: Long = 0,
    @ColumnInfo(name = "type")
    var type: Int = 0 // make as ENUM and add converter)
) {
    override fun equals(other: Any?): Boolean = this === other || other is StoryModel && id == other.id

    override fun hashCode(): Int = super.hashCode()

    enum class Mood(val index: Int) {
        VERY_BAD(0), BAD(1), NEUTRAL(2), GOOD(3), VERY_GOOD(4);

        companion object {
            fun fromIndex(i: Int): Mood {
                return values()[i]
            }
        }
    }

    enum class Type constructor(val index: Int) {
        FEEL_TODAY(0), FUTURE_PLAN(1);

        companion object {
            fun fromIndex(i: Int): Type {
                return values()[i]
            }
        }
    }
}