fun main(args: Array<String>) {
    val input =
        { }.javaClass.getResourceAsStream("input.txt")?.bufferedReader() ?: throw Exception("input.txt file not found")

    val inputLines = input.readLines().filter { it.isNotBlank() }.map { it.trim() }

    val matrix = TreeMatrix<Int>(inputLines.size, inputLines[0].length)

    inputLines.forEachIndexed { row, line ->
        line.forEachIndexed { column, number ->
            matrix.set(row, column, number.toString().toInt())
        }
    }

    val visibleCells =
        matrix
            .count { it.isVisible() }
    println("Visible cells $visibleCells")

    val maxScore = matrix.maxOf { it.scenicScore() }
    println("Max scenic score $maxScore")
}