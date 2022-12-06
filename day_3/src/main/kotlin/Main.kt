

fun mapToPriority(c: Char): Int {
    return if(c.isLowerCase()) {
        c.code - 0x61 + 1
    } else if(c.isUpperCase()) {
        c.code - 0x41 + 27
    } else {
        throw Exception("Character not in range of lowercase and uppercase characters")
    }
}

fun main(args: Array<String>) {
    println("Loading input file...")

    val sharedItems = arrayListOf<Char>()

    val input = { }.javaClass.getResourceAsStream("input.txt")?.bufferedReader() ?: throw Exception("input.txt file not found")

    for(line in input.lines()) {
        val firstSack = line.take(line.length / 2)
        val lastSack = line.drop(line.length / 2)

        val intersection = firstSack.toSet().intersect(lastSack.toSet())

        if(intersection.size == 1) {
            sharedItems.add(intersection.elementAt(0))
        } else {
            throw Exception("More than one shared item in the sacks")
        }
    }

    val priorities = sharedItems.fold(0) { acc, char -> acc + mapToPriority(char) }

    println("Priorities sum: $priorities")
}