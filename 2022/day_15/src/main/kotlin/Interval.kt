class Interval {
    val ranges: List<LongRange>

    constructor(start: Long, end: Long) {
        ranges = listOf(LongRange(start, end))
    }

    constructor(r: List<LongRange>) {
        ranges = r.toMutableList().toList()
    }

    operator fun minus(other: Interval): Interval {
        val result = mutableListOf<LongRange>()
        for (r1 in ranges) {
            for(r2 in other.ranges) {
                result.addAll(rangeSubtract(r1, r2))
            }
        }

        return Interval(result)
    }

    fun isEmpty() = ranges.isEmpty()
    fun isNotEmpty() = ranges.isNotEmpty()

    override fun toString(): String {
        return "[" + ranges.joinToString(", ") { r -> r.toString() } + "]"
    }

    private fun rangeSubtract(int1: LongRange,int2: LongRange): List<LongRange> {
        return if(int2.last >= int1.first && int2.first <= int1.last) {
            if(int2.first <= int1.first && int2.last >= int1.last) {
                listOf()
            } else if(int2.first > int1.first && int2.last < int1.last) {
                // Split interval
                listOf(int1.first until int2.first, int2.last + 1 .. int1.last)
            } else {
                if(int2.first > int1.first) {
                    listOf(int1.first until int2.first)
                } else {
                    listOf(int2.last + 1 .. int1.last)
                }
            }
        } else listOf(int1)
    }
}