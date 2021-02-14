class Node(var key: Long) {
    var l: Node? = null
    var r: Node? = null
    var p: Node? = null
    var cnt = 0
    var sum = 0L
    var dummy = false
}

class Splay(val N: Int, val inputs: LongArray) {
    var root: Node? = null
    var pointer = Array<Node?>(N + 2) { null }

    fun init() {
        var x = Node(Long.MIN_VALUE)
        root = x
        pointer[0] = x
        for (i in 0 until N) {
            x.r = Node(inputs[i])
            x = x.r!!
            pointer[i + 1] = x
        }
        x.r = Node(Long.MAX_VALUE)
        pointer[N + 1] = x.r
        root!!.dummy = true
        x.r!!.dummy = true
        for (i in N downTo 1) update(pointer[i]!!)
        splay(pointer[N / 2]!!)
    }

    fun rotate(x: Node) {
        if (x.p == null) return
        var p = x.p
        var b: Node?
        if (x == p!!.l) {
            b = x.r
            p.l = b
            x.r = p
        } else {
            b = x.l
            p.r = b
            x.l = p
        }
        x.p = p.p
        p.p = x
        if (b != null) b.p = p
        if (x.p != null) {
            if (p == x.p!!.l) x.p!!.l = x
            else x.p!!.r = x
        } else root = x
        update(p)
        update(x)
    }

    fun splay(x: Node, g: Node? = null) {
        while (x.p != g) {
            var p = x.p
            if (p!!.p == g) {
                rotate(x)
                break
            }
            var pp = p.p
            rotate(if ((p.l == x) == (pp!!.l == p)) p else x)
            rotate(x)
        }
        if (g == null) root = x
    }

    fun update(x: Node) {
        x.cnt = 1
        x.sum = x.key
        if (x.l != null) {
            x.cnt += x.l!!.cnt
            x.sum += x.l!!.sum
        }
        if (x.r != null) {
            x.cnt += x.r!!.cnt
            x.sum += x.r!!.sum
        }
    }

    fun kth(k: Int) { // 0 - based
        var x = root
        var tmpK = k
        while (true) {
            while (x!!.l != null && x.l!!.cnt > tmpK) x = x.l
            if (x.l != null) tmpK -= x.l!!.cnt
            if (tmpK-- == 0) break;
            x = x.r
        }
        splay(x!!)
    }

    fun range(s: Int, e: Int): Node {
        kth(e + 1)
        var tmp = root
        kth(s - 1)
        splay(tmp!!, root)
        return root!!.r!!.l!!
    }
}