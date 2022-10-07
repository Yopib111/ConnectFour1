fun solution(names: List<String>): Int {
    var count = 0
    for (i in 1..names.size - 1 step 2) {
        if (names[i].first() == 'T') {
            count = i
            break
        }
    }
    // put your code here
    return count
}
