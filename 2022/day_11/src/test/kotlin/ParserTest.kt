import kotlin.test.Test
import kotlin.test.assertEquals

class ParserTest {
    @Test
    fun testParserSum() {
        assertEquals(parseOperation("new = old + 10")(5.toWorryLevelType()), 15.toWorryLevelType())
        assertEquals(parseOperation("new = old + old")(5.toWorryLevelType()), 10.toWorryLevelType())
        assertEquals(parseOperation("new = 10 + 20")(5.toWorryLevelType()), 30.toWorryLevelType())
        assertEquals(parseOperation("new = old + 123 + old")(5.toWorryLevelType()), 133.toWorryLevelType())
    }
    @Test
    fun testParserSubtraction() {
        assertEquals(parseOperation("new = old - 10")(5.toWorryLevelType()), -5.toWorryLevelType())
        assertEquals(parseOperation("new = old - old")(5.toWorryLevelType()), 0.toWorryLevelType())
        assertEquals(parseOperation("new = 100 - 20")(5.toWorryLevelType()), 80.toWorryLevelType())
        assertEquals(parseOperation("new = old - 123 - old")(5.toWorryLevelType()), -123.toWorryLevelType())
    }
    @Test
    fun testParserMultiplication() {
        assertEquals(parseOperation("new = old * 10")(5.toWorryLevelType()), 50.toWorryLevelType())
        assertEquals(parseOperation("new = 10 * old")(5.toWorryLevelType()), 50.toWorryLevelType())
        assertEquals(parseOperation("new = old * old")(5.toWorryLevelType()), 25.toWorryLevelType())
        assertEquals(parseOperation("new = 100 * 20")(5.toWorryLevelType()), 2000.toWorryLevelType())
        assertEquals(parseOperation("new = old * 2 * old")(5.toWorryLevelType()), 50.toWorryLevelType())
    }
    @Test
    fun testParserDivision() {
        assertEquals(parseOperation("new = old / 10")(50.toWorryLevelType()), 5.toWorryLevelType())
        assertEquals(parseOperation("new = old / old")(5.toWorryLevelType()), 1.toWorryLevelType())
        assertEquals(parseOperation("new = 100 / 20")(5.toWorryLevelType()), 5.toWorryLevelType())
        assertEquals(parseOperation("new = old / old * 2")(80.toWorryLevelType()), 2.toWorryLevelType())
    }
}