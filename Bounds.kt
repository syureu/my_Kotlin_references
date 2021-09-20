// it needs to sorted
// thanks for
// https://kodingwarrior.github.io/post/2019/04/13/Implementing-Lower-Bound-Using-Kotlin.html

fun lowerBound(inputs: List<Int>, leftCloseIndex: Int, rightOpenIndex: Int, target: Int): Int {
    var l = leftCloseIndex
    var r = rightOpenIndex
    var m: Int
    while (l < r) {
        m = (l + r) shr 1
        if (m == r) return r
        else if (inputs[m] < target) l = m + 1
        else r = m
    }
    return l
}

fun upperBound(inputs: List<Int>, leftCloseIndex: Int, rightOpenIndex: Int, target: Int): Int {
    var l = leftCloseIndex
    var r = rightOpenIndex
    var m: Int
    while (l < r) {
        m = (l + r) shr 1
        if (m == r) return r
        else if (inputs[m] <= target) l = m + 1
        else r = m
    }
    return l
}
