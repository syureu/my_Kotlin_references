class Node(var key: Int) {
    var l: Node? = null
    var r: Node? = null
    var p: Node? = null
    var cnt = 0
}

class Splay {
    var root: Node? = null

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

    fun insert(key: Int) {
        if (root == null) {
            root = Node(key)
            return
        }
        var p = root
        var pp: Node?
        while (true) {
            if (key == p!!.key) return
            if (key < p.key) {
                if (p.l == null) {
                    p.l = Node(key)
                    splay(p.l!!)
                    return
                }
                p = p.l
            } else {
                if (p.r == null) {
                    p.r = Node(key)
                    splay(p.r!!)
                    return
                }
                p = p.r
            }
        }
    }

    fun find(key: Int): Boolean {
        if (root == null) return false
        var p = root
        while (p != null) {
            if (key == p.key) break
            p = if (key < p.key) {
                if (p.l == null) break
                p.l
            } else {
                if (p.r == null) break
                p.r
            }
        }
        splay(p!!)
        return key == p.key
    }

    fun delete(key: Int) {
        if (!find(key)) return
        var p = root
        when {
            p!!.l != null && p!!.r != null -> {
                root = p.l
                root!!.p = null
                var x = root
                while (x!!.r != null) x = x.r
                x.r = p.r
                p.r!!.p = x
                splay(x)
                return
            }
            p.l != null -> {
                root = p.l
                root!!.p = null
                return
            }
            p.r != null -> {
                root = p.r
                root!!.p = null
                return
            }
            else -> root = null
        }
    }

    fun update(x: Node) {
        x.cnt = 1
        if (x.l != null) x.cnt += x.l!!.cnt
        if (x.r != null) x.cnt += x.r!!.cnt
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
}