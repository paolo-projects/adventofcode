import java.util.Stack
import kotlin.math.ceil

val definitionChars = "[]" + ('A'.code..'Z'.code).map { Char(it) }.joinToString()

fun main(args: Array<String>) {
    val definitionStrings = arrayListOf<String>()

    val input =
        { }.javaClass.getResourceAsStream("input.txt")?.bufferedReader() ?: throw Exception("input.txt file not found")

    do {
        val line = input.readLine()
        if (line.toSet().subtract(definitionChars.toSet()).isNotEmpty()) {
            break
        }

        definitionStrings.add(line)
    } while (true)

    val stacks = List(ceil(definitionStrings.last().length / 4.0).toInt()) { Stack<Char>() }
    definitionStrings.reversed().forEach { line ->
        for (i in line.indices step 4) {
            if (line[i + 1] != ' ') {
                stacks[i / 4].push(line[i + 1])
            }
        }
    }

    val firstSolutionStacks = stacks.map { it.clone() as Stack<Char> }.toList()
    val pattern = Regex("move (\\d+) from (\\d+) to (\\d+)")

    val movesLines = input.readLines().filter { pattern.matches(it) }

    movesLines.forEach { line ->
        val match = pattern.matchEntire(line)
        if (match != null) {
            val (countToMove, stackStart, stackEnd) = match.destructured

            (0 until countToMove.toInt()).forEach { _ ->
                firstSolutionStacks[stackEnd.toInt() - 1].push(firstSolutionStacks[stackStart.toInt() - 1].pop())
            }
        }
    }

    val stacksString = firstSolutionStacks.fold("") { result, stack ->
        result + stack.peek()
    }

    println("Crates: $stacksString")

    val secondSolutionStacks = stacks.map { it.clone() as Stack<Char> }.toList()

    movesLines.forEach { line ->
        val match = pattern.matchEntire(line)
        if (match != null) {
            val (countToMove, stackStart, stackEnd) = match.destructured
            val tempStack = Stack<Char>()

            (0 until countToMove.toInt()).apply {
                forEach { _ ->
                    tempStack.push(secondSolutionStacks[stackStart.toInt() - 1].pop())
                }
                forEach { _ ->
                    secondSolutionStacks[stackEnd.toInt() - 1].push(tempStack.pop())
                }
            }
        }
    }

    val secondStacksString = secondSolutionStacks.fold("") { result, stack ->
        result + stack.peek()
    }

    println("Crates #2: $secondStacksString")
}