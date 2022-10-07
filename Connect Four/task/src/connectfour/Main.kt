package connectfour

var boardAA: Int = 6
var boardBB: Int = 7
var board = arrayOf<Array<Char>>()
var readMove = ""
var checkGameKind = 0
var gameKind = ""
var totalGame = 1
var countScoreFirst = 0
var countScoreSecond = 0
var firstIcon = 'o'
var secondIcon = '*'


fun main() {
    println("Connect Four")
    println("First player's name:")
    val first = readln()
    println("Second player's name:")
    val second = readln()
    var firstTurnMove = first
    var secondTurnMove = second
    var checkOk = 0
    val regex = Regex("[0-9][0-9]?X[0-9][0-9]?")
    val regexInputLine = Regex("[0-9][0-9]?[0-9]?")

//        уточняем размеры поля
    loop@    do {
        println(
            """
        Set the board dimensions (Rows x Columns)
        Press Enter for default (6 x 7)
    """.trimIndent()
        )
        val inputLine = readln().filter { !it.isWhitespace() }.uppercase()
        if (inputLine == "") {
            boardAA = 6
            boardBB = 7
            checkOk = 1
        } else if ("X" !in inputLine || !regex.matches(inputLine)) {
            println("Invalid input")
            continue@loop
        } else {
            boardAA = inputLine.trim().substringBefore('X').toInt()
            boardBB = inputLine.trim().substringAfter('X').toInt()
            checkOk = 1
            if (boardAA < 5 || boardAA > 9) {
                println("Board rows should be from 5 to 9")
                checkOk = 0
                continue@loop
            }
            if (boardBB < 5 || boardBB > 9) {
                println("Board columns should be from 5 to 9")
                checkOk = 0
                continue@loop
            }

        }
    } while (checkOk != 1)

//    уточняем кол-во игр
    do {
        println(
            """
            Do you want to play single or multiple games?
            For a single game, input 1 or press Enter
            Input a number of games:
        """.trimIndent()
        )
        gameKind = readln()
        if (gameKind.isEmpty()) {
            checkGameKind = 1
            totalGame = 1
        } else if (gameKind.toIntOrNull() == null || gameKind == "0") {
            println("Invalid input")
            checkGameKind = 0
        } else {
            checkGameKind = 1
            totalGame = gameKind.toInt()
        }

    } while (checkGameKind != 1)

    println("""
        $first VS $second
        $boardAA X $boardBB board        
    """.trimIndent())
    if (totalGame > 1) println("Total $totalGame games")

//        формируем массив
    for (i in 0..boardAA-1) {
        var array = arrayOf<Char>()
        for (j in 0..boardBB-1) {
            array += ' '
        }
        board += array
    }

    for (gameRound in 1 .. totalGame) {
        if (totalGame == 1) {
            println("Single game")
            printBoard()
        } else {
            if (readMove == "end") break
            println("Game #$gameRound")
//            формируем пустой массив каждый раз под новую игру
            for (i in 0..boardAA-1) {
                for (j in 0..boardBB - 1){
                    board[i][j] = ' '
                }
            }
            printBoard()
//            вот тут производим смену игрока у которого первый ход
            firstTurnMove = first
            firstIcon = 'o'
            secondIcon = '*'
            secondTurnMove = second
            if (gameRound % 2 == 0) {
                firstTurnMove = second
                firstIcon = '*'
                secondTurnMove = first
                secondIcon = 'o'
            }
        }

        loop111@ do {
//        ход первого игрока
            do {
                println("$firstTurnMove's turn:")
                readMove = readln()
                if (readMove == "end") {
                    checkOk = 0
                    break@loop111
                } else if (regexInputLine.matches(readMove) &&
                    readMove.toInt() !in 1..boardBB
                ) {
                    println("The column number is out of range (1 - $boardBB)")
                    checkOk = 0
                } else if (regexInputLine.matches(readMove) &&
                    readMove.toInt() in 1..boardBB
                ) {
                    for (i in boardAA - 1 downTo 0) {
                        if (board[i][readMove.toInt() - 1] == ' ') {
                            board[i][readMove.toInt() - 1] = firstIcon
                            checkOk = 1
                            if (checkWinFirstPlayer(i, readMove.toInt() - 1) == 1) {
                                printBoard()
                                if (firstTurnMove == first) countScoreFirst += 2 else countScoreSecond +=2
                                println("Player $firstTurnMove won")
                                println("""
                                    Score
                                    $first: $countScoreFirst $second: $countScoreSecond
                                """.trimIndent())

                                break@loop111
                            }
                            var checkDraw = 1
                            for (j in 0..boardAA - 1) {
                                if (' ' in board[j]) checkDraw = 0
                            }
                            if (checkDraw == 1) {
                                printBoard()
                                countScoreFirst ++
                                countScoreSecond ++
                                println("It is a draw")
                                println("""
                                    Score
                                    $first: $countScoreFirst $second: $countScoreSecond
                                """.trimIndent())
                                break@loop111

                            }
                            break
                        } else if (i == 0 && board[i][readMove.toInt() - 1] != ' ') {
                            println("Column $readMove is full")
                            checkOk = 0
                            break
                        }
                    }
                } else {
                    println("Incorrect column number")
                    checkOk = 0
                    continue
                }
            } while (checkOk != 1)
            if (checkOk == 1) printBoard()
//        далее ход второго игрока
            do {
                println("$secondTurnMove's turn:")
                readMove = readln()
                if (readMove == "end") {
                    checkOk = 0
                    break@loop111
                } else if (regexInputLine.matches(readMove) &&
                    readMove.toInt() !in 1..boardBB
                ) {
                    println("The column number is out of range (1 - $boardBB)")
                    checkOk = 0
                } else if (regexInputLine.matches(readMove) &&
                    readMove.toInt() in 1..boardBB
                ) {
                    for (i in boardAA - 1 downTo 0) {
                        if (board[i][readMove.toInt() - 1] == ' ') {
                            board[i][readMove.toInt() - 1] = secondIcon
                            checkOk = 1
                            if (checkWinSecondPlayer(i, readMove.toInt() - 1) == 1) {
                                printBoard()
                                if (firstTurnMove == first) countScoreFirst += 2 else countScoreSecond +=2
                                println("Player $secondTurnMove won")
                                println("""
                                    Score
                                    $first: $countScoreFirst $second: $countScoreSecond
                                """.trimIndent())
                                break@loop111
                            }
                            var checkDraw = 1
                            for (j in 0..boardAA - 1) {
                                if (' ' in board[j]) checkDraw = 0
                            }
                            if (checkDraw == 1) {
                                printBoard()
                                countScoreFirst ++
                                countScoreSecond ++
                                println("It is a draw")
                                println("""
                                    Score
                                    $first: $countScoreFirst $second: $countScoreSecond
                                """.trimIndent())
                                break@loop111
                            }
                            break
                        } else if (i == 0 && board[i][readMove.toInt() - 1] != ' ') {
                            println("Column $readMove is full")
                            checkOk = 0
                            break
                        }
                    }
                } else {
                    println("Incorrect column number")
                    checkOk = 0
                    continue

                }
            } while (checkOk != 1)
            if (checkOk == 1) printBoard()


        } while (readMove != "end")
    }
    println("Game over!")

}

fun printBoard () {
    for (i in 1..boardBB) print(" $i")
    println()
    for (i in 0..boardAA - 1) {
        println(board[i].joinToString(prefix = "║", separator = "║", postfix = "║"))
    }
    for (i in 0..boardBB) {
        if (i == 0) print("╚")
        else if (i == boardBB) println("═╝")
        else print("═╩")
    }
}

fun checkWinFirstPlayer (rows: Int, columns: Int): Int {
    var count = 1
    var checkWinNumber = 0


////        вниз
    for (j in rows..boardAA-1) {
        if (j == boardAA-1) break
        if (board[j + 1][columns] == firstIcon) {
            count++
            if (count == 4) {
                checkWinNumber = 1
                break
            }
        } else break
    }
//        вправо и сразу влево
    count = 1
    for (j in columns..boardBB-1) {
        if (j == boardBB-1) break
        if (board[rows][j+1] == firstIcon) {
            count++
            if (count == 4) {
                checkWinNumber = 1
                break
            }
        } else break
    }
    for (j in columns downTo 0) {
        if (j == 0) break
        if (board[rows][j-1] == firstIcon) {
            count++
            if (count == 4) {
                checkWinNumber = 1
                break
            }
        } else break
    }

//        влево-вверх и сразу вправо вниз
    count = 1
    var rowsSet = rows
    for (j in columns downTo 0) {
        if (j == 0 || rowsSet == 0) break
        if (board[rowsSet-1][j-1] == firstIcon) {
            count++
            rowsSet --
            if (count == 4) {
                checkWinNumber = 1
                break
            }
        } else break
    }
    rowsSet = rows
    for (j in columns .. boardBB-1) {
        if (j == boardBB-1 || rowsSet == boardAA-1) break
        if (board[rowsSet+1][j+1] == firstIcon) {
            count++
            rowsSet ++
            if (count == 4) {
                checkWinNumber = 1
                break
            }
        } else break
    }

//        вправо-вверх и сразу влево-вниз
    count = 1
    rowsSet = rows
    for (j in columns downTo 0) {
        if (j == 0 || rowsSet == boardAA-1) break
        if (board[rowsSet+1][j-1] == firstIcon) {
            count++
            rowsSet ++
            if (count == 4) {
                checkWinNumber = 1
                break
            }
        } else break
    }
    rowsSet = rows
    for (j in columns .. boardBB-1) {
        if (j == boardBB-1 || rowsSet == 0) break
        if (board[rowsSet-1][j+1] == firstIcon) {
            count++
            rowsSet --
            if (count == 4) {
                checkWinNumber = 1
                break
            }
        } else break
    }
    return checkWinNumber
}
fun checkWinSecondPlayer (rows: Int, columns: Int): Int {
    var count = 1
    var checkWinNumber = 0

////        вниз
    for (j in rows..boardAA-1) {
        if (j == boardAA-1) break
        if (board[j + 1][columns] == secondIcon) {
            count++
            if (count == 4) {
                checkWinNumber = 1
                break
            }
        } else break
    }
//        вправо и сразу влево
    count = 1
    for (j in columns..boardBB-1) {
        if (j == boardBB-1) break
        if (board[rows][j+1] == secondIcon) {
            count++
            if (count == 4) {
                checkWinNumber = 1
                break
            }
        } else break
    }
    for (j in columns downTo 0) {
        if (j == 0) break
        if (board[rows][j-1] == secondIcon) {
            count++
            if (count == 4) {
                checkWinNumber = 1
                break
            }
        } else break
    }

//        влево-вверх и сразу вправо вниз
    count = 1
    var rowsSet = rows
    for (j in columns downTo 0) {
        if (j == 0 || rowsSet == 0) break
        if (board[rowsSet-1][j-1] == secondIcon) {
            count++
            rowsSet --
            if (count == 4) {
                checkWinNumber = 1
                break
            }
        } else break
    }
    rowsSet = rows
    for (j in columns .. boardBB-1) {
        if (j == boardBB-1 || rowsSet == boardAA-1) break
        if (board[rowsSet+1][j+1] == secondIcon) {
            count++
            rowsSet ++
            if (count == 4) {
                checkWinNumber = 1
                break
            }
        } else break
    }

//        вправо-вверх и сразу влево-вниз
    count = 1
    rowsSet = rows
    for (j in columns downTo 0) {
        if (j == 0 || rowsSet == boardAA-1) break
        if (board[rowsSet+1][j-1] == secondIcon) {
            count++
            rowsSet ++
            if (count == 4) {
                checkWinNumber = 1
                break
            }
        } else break
    }
    rowsSet = rows
    for (j in columns .. boardBB-1) {
        if (j == boardBB-1 || rowsSet == 0) break
        if (board[rowsSet-1][j+1] == secondIcon) {
            count++
            rowsSet --
            if (count == 4) {
                checkWinNumber = 1
                break
            }
        } else break
    }


    return checkWinNumber

}
