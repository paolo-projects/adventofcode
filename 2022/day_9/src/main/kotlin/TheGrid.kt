class TheGrid(knotsCount: Int = 1) {
    private val knots = List(knotsCount) { Knot(0, 0, null, null) }.apply {
        for (i in indices) {
            if(i > 0) {
                get(i).head = get(i - 1)
            }
            if(i < size - 1) {
                get(i).tail = get(i + 1)
            }
        }
    }

    fun moveHead(direction: Direction, amount: Int) {
        knots[0].move(direction, amount)
    }

    fun getVisitedCells(index: Int): Set<GridEntry> = knots[index].visitedCells
}