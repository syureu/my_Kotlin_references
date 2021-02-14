class Node {
    var l: Node? = null
    var r: Node? = null
    var p: Node? = null
}

class Splay(N: Int) {
    var root: Node? = null
    // var pointer = Array<Node?>(N) { null }

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
}