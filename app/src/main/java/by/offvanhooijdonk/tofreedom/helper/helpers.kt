package by.offvanhooijdonk.tofreedom.helper

import java.util.*

private val RANDOM = Random()

fun randomize(base: Int, radius: Float): Int {
    return (base * (1 + (RANDOM.nextFloat() - 0.5) * radius)).toInt()
}