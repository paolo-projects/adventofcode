import java.io.BufferedReader
import java.io.StringReader
import kotlin.test.Test
import kotlin.test.assertEquals

class GameTest {
    private val testGame = """
        Monkey 0:
          Starting items: 79, 98
          Operation: new = old * 19
          Test: divisible by 23
            If true: throw to monkey 2
            If false: throw to monkey 3
        
        Monkey 1:
          Starting items: 54, 65, 75, 74
          Operation: new = old + 6
          Test: divisible by 19
            If true: throw to monkey 2
            If false: throw to monkey 0
        
        Monkey 2:
          Starting items: 79, 60, 97
          Operation: new = old * old
          Test: divisible by 13
            If true: throw to monkey 1
            If false: throw to monkey 3
        
        Monkey 3:
          Starting items: 74
          Operation: new = old + 3
          Test: divisible by 17
            If true: throw to monkey 0
            If false: throw to monkey 1
    """.trimIndent()

    @Test
    fun testGame() {
        val monkeys = parseMonkeys(BufferedReader(StringReader(testGame))).toSortedMap()
        assertEquals(monkeys.size, 4)
        assertEquals(monkeys.keys, setOf(0, 1, 2, 3))

        val inspected = monkeys.keys.fold(mutableMapOf<Int, Int>()) { map, monkey ->
            map[monkey] = 0
            map
        }

        (1 .. 20).forEach {round ->
            playGame(monkeys, true) {
                inspected[it] = inspected[it]!! + 1
            }
            println("Inspected at round #$round")
            println(inspected)
        }

        assertEquals(inspected[0], 101)
        assertEquals(inspected[1], 95)
        assertEquals(inspected[2], 7)
        assertEquals(inspected[3], 105)

        assertEquals(inspected.values.sortedDescending().take(2).reduce { acc, i -> acc*i }, 10605)
    }
}