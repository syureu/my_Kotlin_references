fun main() {

}

class SplayTree {
    private var root: Node? = null

    private fun updateSize(target: Node) {
        target.size = 1
        if (target.left != null) {
            target.size += target.left!!.size
        }
        if (target.right != null) {
            target.size += target.right!!.size
        }
    }

    private fun rotate(target: Node) {
        val parent = target.parent!! // splay시 parent 없는 target의 rotate는 수행하지 않는다
        val xChild: Node?
        if (target == parent.left) {
            xChild = target.right
            parent.left = xChild
            target.right = parent
        } else {
            xChild = target.left
            parent.right = xChild
            target.left = parent
        }
        target.parent = parent.parent
        parent.parent = target
        if (xChild != null) {
            xChild.parent = parent
        }
        val grandParent = target.parent
        if (grandParent != null) {
            if (parent == grandParent.left) {
                grandParent.left = target
            } else {
                grandParent.right = target
            }
        } else {
            root = target
        }
        updateSize(parent)
        updateSize(target)
    }

    fun splay(target: Node) {
        while (target.parent != null) {
            val parent = target.parent!!
            val grandParent = parent.parent
            if (grandParent != null) {
                rotate(if ((target == parent.left) == (parent == grandParent.left)) parent else target)
            }
            rotate(target)
        }
    }

    fun insert(key: Int) {
        if (root == null) { // 빈 스플레이 트리
            root = Node(key)
            return
        }
        var parent = root
        while (true) {
            if (key == parent!!.key) { // 이미 키가 있음
                return
            }
            if (key < parent.key) { // 왼쪽으로 가야한다면
                if (parent.left == null) { // 왼쪽이 비었다면
                    parent.left = Node(key) // 왼쪽에 삽입
                    parent.left!!.parent = parent
                    splay(parent.left!!)
                    return
                }
                parent = parent.left // 안비었다면 왼쪽을 부모로 다시
            } else {
                if (parent.right == null) { // 오른쪽에 대하여 같이
                    parent.right = Node(key)
                    parent.right!!.parent = parent
                    splay(parent.right!!)
                    return
                }
                parent = parent.right
            }
        }
    }

    fun find(key: Int): Node? {
        if (root == null) {
            return null
        }
        var parent = root
        while (parent != null) {
            if (key == parent.key) {
                break
            }
            if (key < parent.key) {
                if (parent.left == null) {
                    break
                }
                parent = parent.left
            } else {
                if (parent.right == null) {
                    break
                }
                parent = parent.right
            }
        }
        splay(parent!!)
        return if (key == parent.key) parent else null
    }

    fun delete(key: Int) {
        if (find(key) == null) {
            return
        }
        val pointer = root!!
        if (pointer.left != null && pointer.right != null) {
            root = pointer.left
            root!!.parent = null

            var rightPointer = root!!
            while (rightPointer.right != null) {
                rightPointer = rightPointer.right!!
            }
            rightPointer.right = pointer.right
            pointer.right!!.parent = rightPointer
        } else if (pointer.left != null) {
            root = pointer.left
            root!!.parent = null
        } else if (pointer.right != null) {
            root = pointer.right
            root!!.parent = null
        } else {
            root = null
        }
    }

    fun kth(k: Int) { // k is 0-based
        var k = k
        var target = root
        while (true) {
            while (target!!.left != null && target.left!!.size > k) {
                target = target.left
            }
            if (target.left != null) {
                k -= target.left!!.size
            }
            if (k-- == 0) {
                break
            }
            target = target.right
        }
        splay(target!!)
    }

    class Node(key: Int) {
        var left: Node? = null
        var right: Node? = null
        var parent: Node? = null
        var key = key
        var size = 1
    }
}