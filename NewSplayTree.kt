import kotlin.math.*

fun main() {
}

class SplayTree {
    var root: Node? = null
    lateinit var nodePointer: Array<Node?>

    private fun update(target: Node) {
        target.size = 1
        target.sum = 0L + target.value
        target.max = target.value
        target.min = target.value
        if (target.left != null) {
            target.size += target.left!!.size
            target.sum += target.left!!.sum
            target.max = max(target.max, target.left!!.max)
            target.min = min(target.min, target.left!!.min)
        }
        if (target.right != null) {
            target.size += target.right!!.size
            target.sum += target.right!!.sum
            target.max = max(target.max, target.right!!.max)
            target.min = min(target.min, target.right!!.min)
        }
    }

    private fun rotate(target: Node) {
        val parent = target.parent!! // splay시 parent 없는 target의 rotate는 수행하지 않는다
        val xChild: Node?
        propFlip(parent)
        propFlip(target)
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
        update(parent)
        update(target)
    }

    private fun propSum(target: Node) {
        target.value += target.lazy
        if (target.left != null) {
            target.left!!.lazy += target.lazy
            target.left!!.sum += target.left!!.size * target.lazy
        }
        if (target.right != null) {
            target.right!!.lazy += target.lazy
            target.right!!.sum += target.right!!.size * target.lazy
        }
        target.lazy = 0
    }

    private fun propFlip(target: Node) {
        if (!target.flip) {
            return
        }
        val leftNode = target.left
        target.left = target.right
        target.right = leftNode

        target.flip = false

        if (target.left != null) {
            target.left!!.flip = !target.left!!.flip
        }
        if (target.right != null) {
            target.right!!.flip = !target.right!!.flip
        }
    }

    fun init(inputs: LongArray) {
        nodePointer = Array(inputs.size + 2) { null }

        nodePointer[0] = insert(-1, 0)
        inputs.forEachIndexed { index, i -> nodePointer[index + 1] = insert(index, i) }
        nodePointer[inputs.size + 1] = insert(inputs.size, 0)

        for (node in nodePointer) {
            update(node as Node)
        }
        splay(nodePointer[inputs.size shr 1]!!)
    }

    fun splay(target: Node, grandParent: Node? = null) {
        while (target.parent != grandParent) {
            val pointer = target.parent!!
            if (pointer.parent == grandParent) {
                rotate(target)
                break
            }
            val pointerParent = pointer.parent!!
            rotate(if ((target == pointer.left) == (pointer == pointerParent.left)) pointer else target)
            rotate(target)
        }
        if (grandParent == null) {
            root = target
        }
    }

    fun insert(key: Int, value: Long): Node {
        if (root == null) { // 빈 스플레이 트리
            root = Node(key, value)
            return root as Node
        }
        var pointer = root
        while (true) {
            if (key == pointer!!.key) { // 이미 키가 있음
                return pointer
            }
            if (key < pointer.key) { // 왼쪽으로 가야한다면
                if (pointer.left == null) { // 왼쪽이 비었다면
                    pointer.left = Node(key, value) // 왼쪽에 삽입
                    pointer.left!!.parent = pointer
                    splay(pointer.left!!)
                    return root as Node
                }
                pointer = pointer.left // 안비었다면 왼쪽을 부모로 다시
            } else {
                if (pointer.right == null) { // 오른쪽에 대하여 같이
                    pointer.right = Node(key, value)
                    pointer.right!!.parent = pointer
                    splay(pointer.right!!)
                    return root as Node
                }
                pointer = pointer.right
            }
        }
    }

    fun find(key: Int): Node? {
        if (root == null) {
            return null
        }
        var pointer = root
        while (pointer != null) {
            if (key == pointer.key) {
                break
            }
            if (key < pointer.key) {
                if (pointer.left == null) {
                    break
                }
                pointer = pointer.left
            } else {
                if (pointer.right == null) {
                    break
                }
                pointer = pointer.right
            }
        }
        splay(pointer!!)
        return if (key == pointer.key) pointer else null
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

    fun kth(k: Int): Node {
        var k = k
        var target = root
        propSum(target!!)
        propFlip(target!!)
        while (true) {
            while (target!!.left != null && target.left!!.size > k) {
                target = target.left
                propSum(target!!)
                propFlip(target!!)
            }
            if (target.left != null) {
                k -= target.left!!.size
            }
            if (k-- == 0) {
                break
            }
            target = target.right
            propSum(target!!)
            propFlip(target!!)
        }
        splay(target!!)
        return target
    }

    fun range(left: Int, right: Int): Node? { // [left, right]
        val rightOfRight = kth(right + 1)
        val leftOfLeft = kth(left - 1)
        splay(rightOfRight, leftOfLeft)
        return root?.right?.left
    }

    fun flip(left: Int, right: Int) { // [left, right]
        flip(range(left, right)!!)
    }

    fun flip(target: Node) {
        target.flip = !target.flip
    }

    fun shift(left: Int, right: Int, count: Int) {
        if (count == 0) {
            return
        }
        flip(left, right)
        flip(left, left + count - 1)
        flip(left + count, right)
    }

    class Node(var key: Int, var value: Long) {
        var left: Node? = null
        var right: Node? = null
        var parent: Node? = null
        var size = 1
        var sum = value
        var lazy = 0L
        var max = value
        var min = value
        var flip = false
    }
}