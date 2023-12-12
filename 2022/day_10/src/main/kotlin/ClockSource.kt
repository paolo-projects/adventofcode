import op.Instruction

typealias ISR = (count: Int) -> Unit

class ClockSource(private val timerPeriod: Int) {
    private var counter = 0
    private val interrupts = arrayListOf<ISR>()
    private val program = arrayListOf<Instruction>()

    private var currentInstruction: Instruction? = null

    fun addISR(interrupt: ISR) {
        interrupts.add(interrupt)
    }

    fun addInstruction(instruction: Instruction) {
        program.add(instruction)
    }

    fun addInstructions(instructions: Collection<Instruction>) {
        program.addAll(instructions)
    }

    fun execute(onTick: () -> Unit) {
        while(program.isNotEmpty() || (currentInstruction != null && currentInstruction!!.clockCycles > 0)) {
            if(currentInstruction == null || currentInstruction!!.clockCycles == 0) {
                currentInstruction?.execute()
                currentInstruction = program.removeFirstOrNull()?.apply { clockCycles-- }
            } else {
                currentInstruction!!.clockCycles--
            }
            tick()
            onTick()
        }
    }

    private fun tick() {
        if((++counter).mod(timerPeriod) == 0) {
            interrupts.forEach { it(counter) }
        }
    }
}