fun solution(strings: List<String>, str: String): Int {
    var count = 0
    for (element in strings) {
        if (element == str) count++
    }
    return count
    // put your code here
}