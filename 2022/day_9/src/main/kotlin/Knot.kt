import kotlin.math.abs

class Knot(x: Int, y: Int, var head: Knot?, var tail: Knot?): GridEntry(x, y) {
    var visitedCells = mutableSetOf(GridEntry(0, 0))
        private set

    fun move(direction: Direction, amount: Int) {
        (0 until amount).forEach { _ ->
            when (direction) {
                Direction.UP -> y++
                Direction.RIGHT -> x++
                Direction.DOWN -> y--
                Direction.LEFT -> x--
            }

            tail?.advance()
        }
    }

    private fun advance() {
        val deltaX = head!!.x - x
        val deltaY = head!!.y - y

        if (abs(deltaX) > 1 || abs(deltaY) > 1) {
            if (abs(deltaX) > abs(deltaY)) {
                x = if (deltaX > 0) head!!.x - 1 else head!!.x + 1
                if (abs(deltaY) > 0) {
                    y += if (deltaY > 0) 1 else -1
                }
            } else if (abs(deltaY) > abs(deltaX)) {
                y = if (deltaY > 0) head!!.y - 1 else head!!.y + 1
                if (abs(deltaX) > 0) {
                    x += if (deltaX > 0) 1 else -1
                }
            } else {
                x = if (deltaX > 0) head!!.x - 1 else head!!.x + 1
                y = if (deltaY > 0) head!!.y - 1 else head!!.y + 1
            }
            visitedCells.add(GridEntry(x, y))

            tail?.advance()
        }
    }
}