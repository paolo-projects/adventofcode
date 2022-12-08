package tree

class Folder(name: String) : TreeNode(name), Iterable<Folder> {
    private val children = arrayListOf<TreeNode>()

    override fun getSize(): Int {
        return children.fold(0) { size, entry -> size + entry.getSize() }
    }

    fun addChild(node: TreeNode) {
        node.parent = this
        children.add(node)
    }

    fun removeChild(node: TreeNode) {
        children.remove(node)
        node.parent = null
    }

    fun clearChildren() {
        children.forEach { it.parent = null }
        children.clear()
    }

    fun getChild(name: String): TreeNode? {
        return children.firstOrNull { it.name == name }
    }

    fun getChildren(): List<TreeNode> {
        return children
    }

    fun getFolderChildren(): List<Folder> {
        return children.filterIsInstance<Folder>()
    }

    override fun iterator(): Iterator<Folder> = FolderIterator()

    inner class FolderIterator : Iterator<Folder> {
        private var selfVisited = false
        private var subFolders = getFolderChildren().toMutableList()
        private var currentIterator: Iterator<Folder>? = null

        override fun next(): Folder {
            return if (!selfVisited) {
                selfVisited = true
                currentIterator = subFolders.firstOrNull()?.iterator()
                this@Folder
            } else {
                val nextVal = currentIterator!!.next()
                if(!currentIterator!!.hasNext()) {
                    subFolders.removeAt(0)

                    if(subFolders.isNotEmpty()) {
                        currentIterator = subFolders.first().iterator()
                    }
                }
                nextVal
            }
        }

        override fun hasNext(): Boolean {
            return !selfVisited || currentIterator?.hasNext() == true
        }
    }
}