import java.io.BufferedReader
import java.math.BigInteger
import kotlin.math.round

fun parseMonkeys(input: BufferedReader): Map<Int, Monkey> {
    val results = mutableMapOf<Int, Monkey>()

    var monkeyIndex: Int? = null
    var startingItems: List<ULong>? = null
    var operation: MonkeyOperation? = null
    var test: MonkeyTest? = null

    for (line in input.lines()) {
        if (line.trim().startsWith("Monkey")) {
            monkeyIndex = Regex("Monkey (\\d+):").matchEntire(line)?.groupValues?.get(1)?.toInt()
        } else if (line.trim().startsWith("Starting items:")) {
            startingItems =
                line.trim().replace("Starting items: ", "").trim().split(", ").map { it.toULong() }.toList()
        } else if (line.trim().startsWith("Operation:")) {
            val op = line.trim().replace("Operation: ", "").trim()
            operation = parseOperation(op)
        } else if (line.trim().startsWith("Test:")) {
            test = MonkeyTest(test=parseTest(line.trim().replace("Test:", "")))
        } else if (line.trim().startsWith("If true:")) {
            test!!.testTrueDestination = line.trim().replace("If true: throw to monkey", "").trim().toInt()
        } else if (line.trim().startsWith("If false:")) {
            test!!.testFalseDestination = line.trim().replace("If false: throw to monkey", "").trim().toInt()
        } else if (line.isBlank()) {
            results[monkeyIndex!!] = Monkey(
                operation = operation!!,
                test = test!!,
                worryLevels = startingItems!!.toMutableList()
            )
            monkeyIndex = null
            startingItems = null
            operation = null
            test = null
        }
    }

    if (monkeyIndex != null &&
        startingItems != null &&
        operation != null &&
        test != null
    ) {
        results[monkeyIndex] = Monkey(
            operation = operation,
            test = test,
            worryLevels = startingItems.toMutableList()
        )
    }

    return results
}

fun parseOperation(operation: String): MonkeyOperation {

    return { worryLevel ->
        var resultValue: ULong? = null
        var op: ((ULong, ULong) -> ULong)? = null

        fun setResult(value: ULong) {
            if (resultValue == null) {
                resultValue = value
            } else {
                resultValue = op!!(resultValue!!, value)
                op = null
            }
        }

        operation.split(' ').forEach { token ->
            when (token.trim()) {
                "new" -> {}
                "=" -> {}
                "old" -> setResult(worryLevel)
                ///////
                "+" -> op = { a, b -> a + b }
                "-" -> op = { a, b -> a - b }
                "*" -> op = { a, b -> a * b }
                "/" -> op = { a, b -> a / b }
                else -> setResult(token.trim().toULongOrNull() ?: throw Exception("Couldn't parse token $token"))
            }
        }

        resultValue!!
    }
}

fun parseTest(test: String): TestFunction {
    return { worryLevel ->
        if (test.trim().startsWith("divisible by")) {
            val divBy = test.trim().replace("divisible by", "").trim().toULong()
            worryLevel.mod(divBy) == 0UL
        } else {
            throw Exception("Unsupported test: $test")
        }
    }
}