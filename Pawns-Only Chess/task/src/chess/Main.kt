package chess

// Chessboard here is upside down, since
// on the board the lowest is at bottom
val cb = mutableListOf(
    mutableListOf(' ',' ',' ',' ',' ',' ',' ',' '),
    mutableListOf('W','W','W','W','W','W','W','W'),
    mutableListOf(' ',' ',' ',' ',' ',' ',' ',' '),
    mutableListOf(' ',' ',' ',' ',' ',' ',' ',' '),
    mutableListOf(' ',' ',' ',' ',' ',' ',' ',' '),
    mutableListOf(' ',' ',' ',' ',' ',' ',' ',' '),
    mutableListOf('B','B','B','B','B','B','B','B',),
    mutableListOf(' ',' ',' ',' ',' ',' ',' ',' ')
)

var playerOnesTurn = true

fun main() {
    showMenu()
}

fun showMenu() {
    println("Pawns-Only Chess")
    val player1 = inputFromPrompt("First Player's name:")       //White
    val player2 = inputFromPrompt("Second Player's name:")      //Black
    drawChessboard()

    while (true) {
        val currentPlayer = if (playerOnesTurn) player1 else player2
        val input = inputFromPrompt("$currentPlayer's turn:")
        if (input == "exit") {
            println("Bye!")
            break
        }
        makeMove(input)
    }
}

fun drawChessboard() {
    val line = "+---+---+---+---+---+---+---+---+"

    println("""
  $line
${getLine(8)}
  $line
${getLine(7)}
  $line
${getLine(6)}
  $line
${getLine(5)}
  $line
${getLine(4)}
  $line
${getLine(3)}
  $line
${getLine(2)}
  $line
${getLine(1)}
  $line
    a   b   c   d   e   f   g   h
    """.trimIndent())
}

fun getLine(line: Int): String {
    return "$line | ${cb[line-1].joinToString(" | ")} |"
}

fun inputFromPrompt(prompt: String): String {
    println(prompt)
    return readln()
}

fun makeMove(move: String) {

    val piece = if (playerOnesTurn) 'W' else 'B'

    if (move.matches(Regex("[a-h][1-8][a-h][1-8]"))) {
        val origin = "${move[0]}${move[1]}"
        val destination = "${move[2]}${move[3]}"
        if (piece == getPiece(origin)) {
            if (getPiece(destination) == ' ') {
                if (getPossibleDestinations(origin).contains(destination)) {
                    setPiece(origin,' ')
                    setPiece(destination, piece)
                    drawChessboard()
                    playerOnesTurn = !playerOnesTurn
                } else {
                    println("Invalid Input")
                }
            } else {
                println("Invalid Input")
            }
        } else {
            val color = if (piece == 'W') "white" else "black"
            println("No $color pawn at $origin")
        }
    } else {
        println("Invalid Input")
    }
}

fun getPiece(location: String): Char? {
    val locationArray = regexToLocation(location)
    if (locationArray.isEmpty()) {
        return null
    }
    val column = locationArray[0]
    val row = locationArray[1]
    return cb[column][row]
}

fun setPiece(location: String, piece: Char) {
    val locationArray = regexToLocation(location)
    cb[locationArray[0]][locationArray[1]] = piece
}

fun getPossibleDestinations(location: String): Array<String> {
    val piece = getPiece(location)

    val firstMoves: Array<String> = if (piece == 'W') {
        if (location[1] == '2') {
            arrayOf(
                "${location[0]}3",
                "${location[0]}4"
            )
        } else {
            arrayOf("${location[0]}${location[1]+1}")
        }
    } else if (piece == 'B'){
        if (location[1] == '7') {
            arrayOf(
                "${location[0]}6",
                "${location[0]}5"
            )
        } else {
            arrayOf("${location[0]}${location[1]-1}")
        }
    } else arrayOf()

    val secondMoves: Array<String> = if (piece == 'W') {
        val nextRow = location[1]+1
        if (getPiece("${location[0]}$nextRow") == ' ') {
            arrayOf("${location[0]}$nextRow")
        } else arrayOf()
    } else arrayOf()

    val capturing: Array<String> = arrayOf()

    val enPassant: Array<String> = arrayOf()

    return firstMoves.plus(secondMoves).plus(capturing).plus(enPassant)

}

fun regexToLocation(location: String): Array<Int> {
    if (location.matches(Regex("[a-h][1-8]"))) {
        val column = when (location[0]) {
            'a' -> 0
            'b' -> 1
            'c' -> 2
            'd' -> 3
            'e' -> 4
            'f' -> 5
            'g' -> 6
            else -> 7
        }
        val row = location[1].digitToInt() - 1
        return arrayOf(row,column)
    } else {
        return arrayOf()
    }
}