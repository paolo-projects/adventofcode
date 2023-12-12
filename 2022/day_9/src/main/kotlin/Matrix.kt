import java.util.Vector
import kotlin.math.floor

class Matrix<T>(val rows: Int, val columns: Int, initValue: (row: Int, column: Int) -> T) : Iterable<Matrix<T>.MatrixPoint> {
    private val values = Vector<T>(rows * columns).apply {
        (0 until rows * columns).forEach { i ->
            add(initValue(floor(i.toDouble() / columns).toInt(), i.mod(columns)))
        }
    }

    fun get(row: Int, column: Int): T {
        return values[row * columns + column]
    }

    fun set(row: Int, column: Int, value: T) {
        values[row * columns + column] = value
    }

    override fun iterator(): Iterator<MatrixPoint> = MatrixIterator()

    inner class MatrixPoint(val row: Int, val column: Int, val value: T)

    inner class MatrixIterator : Iterator<MatrixPoint> {
        private var row = 0
        private var column = 0

        override fun next(): MatrixPoint {
            val (r, c) = arrayOf(row++, column++)
            val result = MatrixPoint(r, c, get(r, c))
            if (column >= columns) {
                column = 0
                row++
            }
            return result
        }

        override fun hasNext(): Boolean {
            return row < rows && column < columns
        }
    }
}