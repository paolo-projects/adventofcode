fun Char.mapToPriority(): Int {
    return if (isLowerCase()) {
        code - 0x61 + 1
    } else if (isUpperCase()) {
        code - 0x41 + 27
    } else {
        throw Exception("Character not in range of lowercase and uppercase characters")
    }
}

fun main(args: Array<String>) {
    println("Loading input file...")

    val sharedItems = arrayListOf<Char>()
    val groupsIntersections = arrayListOf<Set<Char>>()

    val input =
        { }.javaClass.getResourceAsStream("input.txt")?.bufferedReader() ?: throw Exception("input.txt file not found")
    var i = 0;

    for (line in input.lines()) {
        val firstSack = line.take(line.length / 2)
        val lastSack = line.drop(line.length / 2)

        val intersection = firstSack.toSet().intersect(lastSack.toSet())

        if (intersection.size == 1) {
            sharedItems.add(intersection.elementAt(0))
        } else {
            throw Exception("More than one shared item in the sacks")
        }

        if (i.mod(3) == 0) {
            if ((groupsIntersections.lastOrNull()?.size ?: 0) > 1) {
                throw Exception("More than one badge in last group")
            }

            groupsIntersections.add(line.toSet())
        } else {
            groupsIntersections[groupsIntersections.size - 1] = groupsIntersections.last().intersect(line.toSet())
        }

        i += 1
    }

    val priorities = sharedItems.fold(0) { acc, char -> acc + char.mapToPriority() }

    println("Priorities sum: $priorities")

    val sumOfBadges =
        groupsIntersections.fold(0) { acc, intersection -> acc + intersection.elementAt(0).mapToPriority() }

    println("Sum of badges: $sumOfBadges")
}