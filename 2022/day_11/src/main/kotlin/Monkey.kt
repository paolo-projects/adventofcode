typealias MonkeyOperation = (worryLevel: ULong) -> ULong

class Monkey(
    val operation: MonkeyOperation,
    val test: MonkeyTest,
    val worryLevels: MutableList<ULong> = arrayListOf(),
)