package op

abstract class Instruction(var clockCycles: Int) {
    abstract fun execute()
}