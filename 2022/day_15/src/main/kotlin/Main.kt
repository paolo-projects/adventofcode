import java.lang.Math.PI
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun manhattanLength(a: Coordinate, b: Coordinate): Long {
    return abs(a.x - b.x) + abs(a.y - b.y)
}

fun main(args: Array<String>) {
    val input = {}.javaClass.getResourceAsStream("input.txt")?.bufferedReader() ?: throw Exception("No input.txt")
    val pattern = Regex("^Sensor at x=([-\\d]+), y=([-\\d]+): closest beacon is at x=([-\\d]+), y=([-\\d]+)$")

    val values = input.lines().filter { l -> l.isNotBlank() }.map { l ->
        pattern.matchEntire(l)!!.groupValues.drop(1).map { v -> v.toLong() }.let { values ->
            SensorAndBeacon(Coordinate(values[0], values[1]), Coordinate(values[2], values[3]))
        }
    }.toList()

    var minX = Long.MAX_VALUE
    var maxX = Long.MIN_VALUE

    values.forEach { sb ->
        minX = min(minX, min(sb.beacon.x, sb.sensor.x))
        maxX = max(maxX, max(sb.beacon.x, sb.sensor.x))
    }

    val positions = mutableSetOf<Coordinate>()
    val fixedY = 2000000L
    values.forEach { sb ->
        val distance = manhattanLength(sb.sensor, sb.beacon)
        for (x in minX..maxX) {
            val position = Coordinate(x, fixedY)
            if (manhattanLength(sb.sensor, position) <= distance && position != sb.sensor && position != sb.beacon) {
                positions.add(Coordinate(x, fixedY))
            }
        }
    }

    println("#1 result: ${positions.size}")

    val minXY = 0L
    val maxXY = 4000000L

    /* y
       4e6
       :
       |           /\
       |        a /  \ b
       |         /    \
       |        /      \
       |        \      /
       |         \    /
       |        d \  / c
       |           \/
       0---------------------------.. 4e6 x

        P1(x, y) P2(x, y)
                 y2 - y1
        y - y1 = ------- (x - x1)
                 x2 - x1

            y - y1
        x = ------ + x1
              m

        a) P1 = (Sx - L, Sy); P2 = (Sx, Sy + L)
        b) P1 = (Sx + L, Sy); P2 = (Sx, Sy + L)
        c) P1 = (Sx + L, Sy); P2 = (Sx, Sy - L)
        d) P1 = (Sx - L, Sy); P2 = (Sx, Sy - L)

        a) x = y - Sy + Sx - L
        b) x = -y + Sy + Sx + L
        c) x = y - Sy + Sx + L
        d) x = -y + Sy + Sx - L
     */

    for ((p, y) in (minXY..maxXY).withIndex()) {
        var range = Interval(minXY, maxXY)

        val rangeCoords = mutableListOf<Interval>()
        values.forEach { sb ->
            val ml = sb.manhattanLength

            if (y in sb.sensor.y - ml..sb.sensor.y + ml) {
                val (x0, x1) = if (y > sb.sensor.y) {
                    val L = sb.manhattanLength
                    val x0 = y - sb.sensor.y + sb.sensor.x - L
                    val x1 = -y + sb.sensor.y + sb.sensor.x + L
                    arrayOf(x0, x1)
                } else {
                    val L = sb.manhattanLength
                    val x0 = -y + sb.sensor.y + sb.sensor.x - L
                    val x1 = y - sb.sensor.y + sb.sensor.x + L
                    arrayOf(x0, x1)
                }

                if (x0 <= maxXY && x1 >= 0) {
                    rangeCoords.add(Interval(x0, x1))
                }
            }
        }

        for (r in rangeCoords) {
            range -= r
        }

        if(range.isNotEmpty()) {
            println("Found! x=$range y=$y")
            val result = range.ranges[0].first * 4000000 + y
            println("Solution = $result")
            break
        }

        print("%.4f %%     \r".format(p.toDouble()/maxXY* 100))
        System.out.flush()
    }
}