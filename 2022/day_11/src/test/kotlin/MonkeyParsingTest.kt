import java.io.BufferedReader
import java.io.StringReader
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MonkeyParsingTest {
    private val testMonkey = """
        Monkey 0:
          Starting items: 99, 67, 92, 61, 83, 64, 98
          Operation: new = old * 17
          Test: divisible by 3
            If true: throw to monkey 4
            If false: throw to monkey 2

        Monkey 5:
          Starting items: 76
          Operation: new = old + 8
          Test: divisible by 17
            If true: throw to monkey 0
            If false: throw to monkey 2
            
    """.trimIndent()

    @Test
    fun testMonkeyParsing() {
        val monkeys = parseMonkeys(BufferedReader(StringReader(testMonkey)))

        assertTrue { monkeys.containsKey(0) }
        assertEquals(
            monkeys[0]!!.worryLevels,
            mutableListOf(
                99.toWorryLevelType(),
                67.toWorryLevelType(),
                92.toWorryLevelType(),
                61.toWorryLevelType(),
                83.toWorryLevelType(),
                64.toWorryLevelType(),
                98.toWorryLevelType()
            )
        )
        assertEquals(monkeys[0]!!.operation(5.toWorryLevelType()), 85.toWorryLevelType())
        assertEquals(monkeys[0]!!.test.test(5.toWorryLevelType()), false)
        assertEquals(monkeys[0]!!.test.test(9.toWorryLevelType()), true)
        assertEquals(monkeys[0]!!.test.testTrueDestination, 4)
        assertEquals(monkeys[0]!!.test.testFalseDestination, 2)

        assertTrue { monkeys.containsKey(5) }
        assertEquals(monkeys[5]!!.worryLevels, mutableListOf(76.toWorryLevelType()))
        assertEquals(monkeys[5]!!.operation(5.toWorryLevelType()), 13.toWorryLevelType())
        assertEquals(monkeys[5]!!.test.test(80.toWorryLevelType()), false)
        assertEquals(monkeys[5]!!.test.test(85.toWorryLevelType()), true)
        assertEquals(monkeys[5]!!.test.testTrueDestination, 0)
        assertEquals(monkeys[5]!!.test.testFalseDestination, 2)
    }
}