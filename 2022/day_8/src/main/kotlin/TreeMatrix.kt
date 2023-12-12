import kotlin.math.min

class TreeMatrix<T : Comparable<T>>(val rows: Int, val columns: Int) : Iterable<TreeMatrix<T>.MatrixCell> {
    private val entries = MutableList<T?>(rows * columns) { null }

    fun get(row: Int, column: Int): T? {
        if (row < rows && column < columns) {
            return entries[row * columns + column]
        } else throw Exception("Coordinates out of range")
    }

    fun set(row: Int, column: Int, value: T?) {
        if (row < rows && column < columns) {
            entries[row * columns + column] = value
        } else throw Exception("Coordinates out of range")
    }



    override fun iterator(): Iterator<MatrixCell> = TreeCellIterator()

    inner class MatrixCell(val row: Int, val column: Int) {
        val cellValue = get(row, column)

        fun isVisible(): Boolean {
            (0 until column).all { get(row, it)!! < cellValue!! } && return true
            (column + 1 until columns).all { get(row, it)!! < cellValue!! } && return true
            (0 until row).all { get(it, column)!! < cellValue!! } && return true
            (row + 1 until rows).all { get(it, column)!! < cellValue!! } && return true

            return false
        }

        fun scenicScore(): Int {
            val scoreLeft = min((column - 1 downTo 0).takeWhile { get(row, it)!! < cellValue!! }.size + 1, column)
            val scoreRight = min(
                (column + 1 until columns).takeWhile { get(row, it)!! < cellValue!! }.size + 1,
                columns - column - 1
            )
            val scoreTop = min((row - 1 downTo 0).takeWhile { get(it, column)!! < cellValue!! }.size + 1, row)
            val scoreBottom = min((row + 1 until rows).takeWhile { get(it, column)!! < cellValue!! }.size + 1, rows - row - 1)

            return scoreLeft * scoreRight * scoreTop * scoreBottom
        }
    }

    inner class TreeCellIterator : Iterator<MatrixCell> {
        var row = 0
        var column = 0

        override fun hasNext(): Boolean {
            return row < rows && column < columns
        }

        override fun next(): MatrixCell {
            val currentRow = row
            val currentColumn = column

            column++
            if (column >= columns) {
                row++
                column = 0
            }

            return MatrixCell(currentRow, currentColumn)
        }
    }
}