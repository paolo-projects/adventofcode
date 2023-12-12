package op

import reg.X

class AddX(private val xReg: X, private val value: Int): Instruction(2) {
    override fun execute() {
        xReg.value += value
    }
}