package by.offvanhooijdonk.tofreedom.helper.countdown

import java.util.*

/**
 * Created by Yahor_Fralou on 8/2/2017 6:07 PM.
 */

data class CountdownBean(
        var year: String? = null,
        var month: String? = null,
        var day: String? = null,
        var hour: String? = null,
        var minute: String? = null,
        var second: String? = null) {

    override fun equals(other: Any?): Boolean {
        if (other !is CountdownBean) return false

        return this.year == other.year
                && this.month == other.month
                && this.day == other.day
                && this.hour == other.hour
                && this.minute == other.minute
                && this.second == other.second
    }

    override fun hashCode(): Int = Objects.hash(year, month, day, hour, minute, second)
}
