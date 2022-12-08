package tree

abstract class TreeNode(val name: String) {
    var parent: Folder? = null

    abstract fun getSize(): Int
}