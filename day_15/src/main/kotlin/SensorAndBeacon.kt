import kotlin.math.abs
import kotlin.math.max

data class SensorAndBeacon(val sensor: Coordinate, val beacon: Coordinate) {
    val manhattanLength: Long get() {
        return abs(sensor.x - beacon.x) + abs(sensor.y - beacon.y)
    }
    val normalLength: Long get() {
        return max(abs(sensor.x - beacon.x), abs(sensor.y - beacon.y))
    }
}
