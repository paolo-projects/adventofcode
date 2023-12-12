import ext.CRT
import op.AddX
import op.NoOp
import reg.X

fun main(args: Array<String>) {
    val input =
        { }.javaClass.getResourceAsStream("input.txt")?.bufferedReader() ?: throw Exception("input.txt file not found")

    val clock = ClockSource(20)
    val registry = X()
    val crt = CRT(registry)

    var strengths = 0

    clock.addISR { counter ->
        if((counter - 20).mod(40) == 0 && counter <= 220) {
            strengths += registry.value*counter
        }
    }

    clock.addInstructions(input.lines().map { line ->
        if(line.startsWith("noop")) {
            NoOp()
        } else if (line.startsWith("addx")) {
            AddX(registry, line.drop(5).toInt())
        } else {
            throw Exception("Couldn't parse instruction $line")
        }
    }.toList())

    clock.execute {
        crt.draw()
    }

    println("Strengths: $strengths")

    println("====================================")
    println("=============== CRT ================")
    println()
    for(y in 0 until crt.height) {
        for(x in 0 until crt.width) {
            print(crt.screen.get(y, x).value)
        }
        println()
    }
}