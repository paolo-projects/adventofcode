import kotlin.math.max

class CircularBuffer<T>(private val maxSize: Int) : ArrayList<T>() {
    fun push(entry: T) {
        if (isFull()) {
            removeFirst()
        }
        add(entry)
    }

    fun isFull(): Boolean {
        return size == maxSize
    }
}

fun <T> CircularBuffer<T>.allDifferent(): Boolean {
    if(!isFull()) {
        return false
    }

    for (i in 0 until size) {
        for (n in 0 until i) {
            if (get(i) == get(n)) {
                return false
            }
        }
    }
    return true
}

fun main(args: Array<String>) {
    var input =
        { }.javaClass.getResourceAsStream("input.txt")?.bufferedReader() ?: throw Exception("input.txt file not found")

    val buffer = CircularBuffer<Char>(4)

    var i = 0
    while(input.ready()) {
        buffer.push(Char(input.read()))

        if(buffer.allDifferent()) {
            println("Marker at ${i+1}")
            break
        }

        i++
    }

    input =
        { }.javaClass.getResourceAsStream("input.txt")?.bufferedReader() ?: throw Exception("input.txt file not found")
    val buffer2 = CircularBuffer<Char>(14)

    i = 0
    while(input.ready()) {
        buffer2.push(Char(input.read()))

        if(buffer2.allDifferent()) {
            println("Marker #2 at ${i+1}")
            break
        }

        i++
    }
}