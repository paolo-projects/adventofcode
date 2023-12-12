fun main(args: Array<String>) {
    val input =
        { }.javaClass.getResourceAsStream("input.txt")?.bufferedReader() ?: throw Exception("input.txt file not found")

    val monkeys = parseMonkeys(input).toSortedMap()
    var inspectedTimes = monkeys.toList().fold(mutableMapOf<Int, ULong>()) { map, (key, _) ->
        map[key] = 0UL
        map
    }

    (1 .. 20).forEach { round ->
        playGame(monkeys, true) { inspectedMonkey ->
            inspectedTimes[inspectedMonkey] = inspectedTimes[inspectedMonkey]!! + 1UL
        }
    }

    println(inspectedTimes.values.sortedDescending().take(2).reduce { acc, i -> acc*i })
    inspectedTimes = monkeys.toList().fold(mutableMapOf()) { map, (key, _) ->
        map[key] = 0UL
        map
    }

    (1 .. 10000).forEach { round ->
        playGame(monkeys, false) { inspectedMonkey ->
            inspectedTimes[inspectedMonkey] = inspectedTimes[inspectedMonkey]!! + 1UL
        }
    }

    // Overflow issue here. The result can't be retrieved through normal calculation
    // Even using arbitrarily large integers (BigInteger), the computation gets too slow after a few cycles
    // The solution to 2nd part shall be retrieved mathematically, through optimization of the calculations

    println(inspectedTimes.values.sortedDescending().take(2).reduce { acc, i -> acc*i })
}