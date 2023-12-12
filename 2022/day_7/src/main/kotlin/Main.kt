import tree.File
import tree.Folder


fun main(args: Array<String>) {
    val input =
        { }.javaClass.getResourceAsStream("input.txt")?.bufferedReader() ?: throw Exception("input.txt file not found")

    val root = Folder("/")
    var currentNode: Folder = root

    input.lines().forEach { line ->
        if(line.startsWith('$')) {
            // Command
            val cmd = line.drop(2)
            if(cmd.startsWith("cd")) {
                val cdArg = cmd.replace("cd ", "")

                currentNode = when (cdArg) {
                    "/" -> {
                        root
                    }
                    ".." -> {
                        currentNode.parent!!
                    }
                    else -> {
                        var childNode = currentNode.getChild(cdArg)
                        if(childNode == null) {
                            childNode = Folder(cdArg)
                            currentNode.addChild(childNode)
                        } else if (childNode is File) {
                            throw Exception("Can't cd into the child node $cdArg: is a file")
                        }

                        childNode as Folder
                    }
                }
            }
        } else {
            // Output of ls
            val firstToken = line.takeWhile { it != ' ' }
            val fileName = line.dropWhile { it != ' ' }.trim()

            if(firstToken == "dir") {
                currentNode.addChild(Folder(fileName))
            } else {
                currentNode.addChild(File(fileName, firstToken.toInt()))
            }
        }
    }

    val totalSizes = root.filter { it.getSize() <= 100000 }.fold(0) { sum, folder -> sum + folder.getSize() }
    println("Total Sizes: $totalSizes")

    val spaceToFree = 30000000 - (70000000 - root.getSize())

    val toDelete = root.map { it.getSize() }.sorted().first { it >= spaceToFree }

    println("Dir size to delete $toDelete")
}