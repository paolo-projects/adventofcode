import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin

class Matrix(
    val rows: Int,
    val columns: Int
) {
    companion object {
        fun vectorMatrix(values: Array<Double>): Matrix
        {
            return Matrix (values.size, 1) {row, _ ->
                values[row]
            }
        }

        fun diagonal(size: Int, value: Double): Matrix
        {
            return Matrix (size, size) { row, column ->
                if (row == column) value else 0.0
            }
        }

        fun unitary(size: Int): Matrix
        {
            return Matrix.diagonal(size, 1.0);
        }

        fun translation(coords: Array<Double>): Matrix
        {
            return Matrix(3, 3) {row, column ->
                if (row == column) {
                    1.0
                } else if (column == 2) {
                    coords[row]
                } else 0.0
            }
        }

        fun scaling(amount: Array<Double>): Matrix
        {
            return Matrix (3, 3) {row, column ->
                if (row == column) {
                    if (row < 2) {
                        amount[row];
                    } else {
                        1.0
                    }
                } else {
                    0.0
                }
            }
        }

        fun rotation(amountRad: Double): Matrix
        {
            return Matrix(3, 3) {row, column ->
                when(row to column) {
                    0 to 0 -> cos(amountRad)
                    0 to 1 -> -sin(amountRad)
                    1 to 0 -> sin(amountRad)
                    1 to 1 -> cos(amountRad)
                    2 to 2 -> 1.0
                    else -> 0.0
                }
            }
        }
    }

    private lateinit var values: MutableList<Double>

    constructor(
        rows: Int,
        columns: Int,
        definition: Double
    ) : this(rows, columns) {
        values = List(rows * columns) {
            definition
        }.toMutableList()
    }

    constructor(
        rows: Int,
        columns: Int,
        definition: MatrixContructionCallback
    ) : this(rows, columns) {
        values = List(rows * columns) { i ->
            definition(floor(i.toDouble() / columns).toInt(), i % columns)
        }.toMutableList()
    }

    fun copy(): Matrix {
        return Matrix(this.rows, this.columns) { row, column ->
            get(row, column)
        }
    }

    fun set(row: Int, column: Int, value: Double) {
        this.values[row * this.columns + column] = value;
    }

    fun get(row: Int, column: Int): Double {
        return this.values[row * this.columns + column];
    }

    operator fun plus(constant: Double): Matrix {
        val result = copy()
        result.values = result.values.map { v -> v + constant }.toMutableList()
        return result;
    }

    operator fun plus(other: Matrix): Matrix {
        if (other.rows != this.rows || other.columns != this.columns) {
            throw Exception("Can't add matrices of different size")
        }

        val result = this.copy();
        result.values = result.values.mapIndexed { i, v -> v + other.values[i] }.toMutableList()
        return result
    }

    operator fun times(other: Matrix): Matrix {
        if (this.columns != other.rows) {
            throw Exception(
                "Can't multiply matrices where M1 columns don't match M2 rows"
            )
        }
        return Matrix(this.rows, other.columns) { row, column ->
            var result = 0.0
            for (k in 0 until columns) {
                result += get(row, k) * other.get(k, column)
            }
            result
        }
    }

    fun determinant(): Double {
        if (this.rows != this.columns) {
            throw Exception("Can't get the determinant of a NON-square matrix");
        }

        return if (this.rows == 2) {
            this.get(0, 0) * this.get(1, 1) - this.get(0, 1) * this.get(1, 0);
        } else {
            var result = 0.0
            for (c in 0 until this.columns) {
                result += this.get(0, c) * this.subMatrix(0, c).determinant()
            }
            result
        }
    }

    fun subMatrix(excludeRow: Int, excludeColumn: Int): Matrix {
        val result = Matrix(rows - 1, columns - 1) { _, _ -> 0.0 }
        for (r in 0 until this.rows) {
            for (c in 0 until this.columns) {
                val row = if (r < excludeRow) r else r - 1
                val column = if (c < excludeColumn) c else c - 1
                if (r != excludeRow && c != excludeColumn) {
                    result.set(row, column, get(r, c));
                }
            }
        }
        return result;
    }
}

typealias MatrixContructionCallback = (row: Int, column: Int) -> Double