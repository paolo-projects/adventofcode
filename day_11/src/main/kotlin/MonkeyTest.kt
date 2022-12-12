typealias TestFunction = (worryLevel: ULong) -> Boolean

class MonkeyTest(var test: TestFunction, var testTrueDestination: Int? = null, var testFalseDestination: Int? = null)