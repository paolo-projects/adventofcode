fun main(args: Array<String>) {
    val input =
        { }.javaClass.getResourceAsStream("input.txt")?.bufferedReader() ?: throw Exception("input.txt file not found")

    val ranges = arrayListOf<Pair<IntRange, IntRange>>()

    for (line in input.lines()) {
        val (firstRange, secondRange) = line.split(',')
        val (firstRangeStart, firstRangeEnd) = firstRange.split('-')
        val (secondRangeStart, secondRangeEnd) = secondRange.split('-')

        ranges.add(
            IntRange(firstRangeStart.toInt(), firstRangeEnd.toInt()) to IntRange(
                secondRangeStart.toInt(),
                secondRangeEnd.toInt()
            )
        )
    }

    val countOfContained = ranges.fold(0) { sum, (firstRange, secondRange) ->
        sum + if ((firstRange.first <= secondRange.first && firstRange.last >= secondRange.last) ||
            (secondRange.first <= firstRange.first && secondRange.last >= firstRange.last)
        ) 1 else 0
    }

    println("Count of contained ranges: $countOfContained")

    val countOfOverlapping = ranges.fold(0) { sum, (firstRange, secondRange) ->
        val (lowerRange, upperRange) = if (firstRange.first < secondRange.first) listOf(
            firstRange,
            secondRange
        ) else listOf(secondRange, firstRange)

        sum + if (lowerRange.last >= upperRange.first) 1 else 0
    }

    println("Count of overlapping ranges: $countOfOverlapping")
}