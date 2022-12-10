package op

class NoOp: Instruction(1) {
    override fun execute() {
        /* no-op */
    }
}