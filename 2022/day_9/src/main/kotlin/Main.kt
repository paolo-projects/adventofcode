fun main(args: Array<String>) {
    val input =
        { }.javaClass.getResourceAsStream("input.txt")?.bufferedReader()
            ?: throw Exception("input.txt file not found")

    val grid = TheGrid(2)
    val grid2 = TheGrid(10)

    for (line in input.lines()) {
        val (dirText, amountText) = line.split(' ')
        val direction = when (dirText) {
            "U" -> Direction.UP
            "R" -> Direction.RIGHT
            "D" -> Direction.DOWN
            "L" -> Direction.LEFT
            else -> throw Exception("Direction not allowed")
        }

        grid.moveHead(direction, amountText.toInt())
        grid2.moveHead(direction, amountText.toInt())
    }

    println("Visited by tail: ${grid.getVisitedCells(1).size}")
    println("Visited by longer tail: ${grid2.getVisitedCells(9).size}")
}
