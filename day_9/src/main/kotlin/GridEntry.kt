import kotlin.math.abs

open class GridEntry(var x: Int, var y: Int) {
    override fun equals(other: Any?): Boolean {
        if(other is GridEntry) {
            return x == other.x && y == other.y
        }
        return false
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }
}