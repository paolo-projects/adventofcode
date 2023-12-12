import java.math.BigInteger
import java.util.*

fun playGame(monkeys: SortedMap<Int, Monkey>, withRelief: Boolean, monkeyInspected: (monkeyId: Int) -> Unit) {
    for ((i, monkey) in monkeys) {
        while(monkey.worryLevels.isNotEmpty()) {
            val level = monkey.worryLevels.removeFirst()
            monkeyInspected(i)

            val newLevel = if(withRelief) monkey.operation(level).floorDiv(3UL) else monkey.operation(level)
            val destinationMonkey = if (monkey.test.test(newLevel)) monkey.test.testTrueDestination!! else monkey.test.testFalseDestination!!

            monkeys[destinationMonkey]!!.worryLevels.add(
                newLevel
            )
        }
    }
}