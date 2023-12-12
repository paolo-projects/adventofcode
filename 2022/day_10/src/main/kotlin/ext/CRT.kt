package ext

import Matrix
import reg.X

class CRT(private val xReg: X, val width: Int = 40, val height: Int = 6) {
    private var xPos = 0
    private var yPos = 0

    val screen = Matrix(height, width) { _, _ ->
        Pixel.DARK
    }

    fun draw() {
        if(IntRange(xReg.value - 1, xReg.value + 1).contains(xPos)) {
            screen.set(yPos, xPos, Pixel.LIT)
        }
        if(++xPos >= width) {
            xPos = 0
            if(++yPos >= height) {
                yPos = 0
            }
        }
    }
}