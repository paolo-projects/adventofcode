package tree

class File(name: String, private val size: Int): TreeNode(name) {
    override fun getSize(): Int {
        return size
    }
}