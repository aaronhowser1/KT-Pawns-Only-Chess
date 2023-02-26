package chess

// Chessboard here is upside down, since
// on the board the lowest is at bottom
val cb = mutableListOf(
    mutableListOf(' ',' ',' ',' ',' ',' ',' ',' '),
    mutableListOf('W',' ','W','W','W','W','W','W'),
    mutableListOf(' ',' ',' ',' ',' ',' ',' ',' '),
    mutableListOf(' ',' ',' ',' ',' ',' ',' ',' '),
    mutableListOf(' ','W',' ',' ',' ',' ',' ',' '),
    mutableListOf(' ',' ',' ',' ',' ',' ',' ',' '),
    mutableListOf('B','B','B','B','B','B','B','B'),
    mutableListOf(' ',' ',' ',' ',' ',' ',' ',' ')
)

var prevCB = copyBoard(cb)

var playerOnesTurn = true
var enPassanted = false

val currentPiece: Char
    get() = if (playerOnesTurn) 'W' else 'B'

val oppositePiece: Char
    get() = if (playerOnesTurn) 'B' else 'W'

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

fun drawChessboard(board: MutableList<MutableList<Char>> = cb) {
    val line = "+---+---+---+---+---+---+---+---+"

    println("""
  $line
${getLine(8, board)}
  $line
${getLine(7, board)}
  $line
${getLine(6, board)}
  $line
${getLine(5, board)}
  $line
${getLine(4, board)}
  $line
${getLine(3, board)}
  $line
${getLine(2, board)}
  $line
${getLine(1, board)}
  $line
    a   b   c   d   e   f   g   h
    """.trimIndent())
}

fun getLine(line: Int, board: MutableList<MutableList<Char>>): String {
    return "$line | ${board[line-1].joinToString(" | ")} |"
}

fun inputFromPrompt(prompt: String): String {
    println(prompt)
    return readln()
}

fun makeMove(move: String) {

    if (move.matches(Regex("[a-h][1-8][a-h][1-8]"))) {
        val origin = "${move[0]}${move[1]}"
        val destination = "${move[2]}${move[3]}"

        if (currentPiece == getPiece(origin)) {
            if (getPossibleDestinations(origin).contains(destination)) {
                prevCB = copyBoard(cb)
                setPiece(origin,' ')
                setPiece(destination, currentPiece)

                drawChessboard(cb)
                playerOnesTurn = !playerOnesTurn
            } else {
                println("Invalid Input")
            }
        } else {
            val color = if (currentPiece == 'W') "white" else "black"
            println("No $color pawn at $origin")
        }
    } else {
        println("Invalid Input")
    }
}

fun getPiece(location: String, board: MutableList<MutableList<Char>> = cb): Char? {
    val locationArray = regexToLocation(location)
    if (locationArray.isEmpty()) {
        return null
    }
    val column = locationArray[0]
    val row = locationArray[1]

    return if (board == cb) cb[column][row] else prevCB[column][row]
}

fun setPiece(location: String, piece: Char) {
    val locationArray = regexToLocation(location)
    cb[locationArray[0]][locationArray[1]] = piece
}

fun getPossibleForwards(origin: String): Array<String> {
    val piece = getPiece(origin)

    val pieceX = origin.first()
    val pieceY = origin.last()

    val nextY = pieceY + if (piece == 'W') 1 else -1
    val secondNextY = pieceY + if (piece == 'W') 2 else -2


    val leftX: Char? = if (pieceX == 'a') null else {
        columnAsChar(columnAsNumber(pieceX)-1)
    }
    val rightX: Char? = if (pieceX == 'h') null else {
        columnAsChar(columnAsNumber(pieceX)+1)
    }

    val hasntMoved: Boolean = when (piece) {
        'W' -> pieceY == '2'
        'B' -> pieceY == '7'
        else -> false
    }


    val oneForward = "$pieceX$nextY"
    val twoForward = "$pieceX${pieceY + (if (piece == 'W') 2 else -2)}"

    val forward: Array<String> =
        if (getPiece(oneForward) == ' ') {
            if (hasntMoved && getPiece(twoForward) == ' ') arrayOf(oneForward,twoForward) else arrayOf(oneForward)
        } else arrayOf()
    return forward
}

fun getPossibleCaptures(origin: String): Array<String> {
    val piece = getPiece(origin)

    val pieceX = origin.first()
    val pieceY = origin.last()

    val nextY = pieceY + if (piece == 'W') 1 else -1

    val leftX: Char? = if (pieceX == 'a') null else {
        columnAsChar(columnAsNumber(pieceX)-1)
    }
    val rightX: Char? = if (pieceX == 'h') null else {
        columnAsChar(columnAsNumber(pieceX)+1)
    }

    val diagonalLeft = "$leftX$nextY"
    val diagonalRight = "$rightX$nextY"

    //Diagonal forward, if there's an enemy piece
    var capturingDiagonals = arrayOf<String>()
    if (!diagonalLeft.contains("null")) if (getPiece(diagonalLeft) == oppositePiece) capturingDiagonals+=arrayOf(diagonalLeft)
    if (!diagonalRight.contains("null")) if (getPiece(diagonalRight) == oppositePiece) capturingDiagonals+=arrayOf(diagonalRight)

    return capturingDiagonals
}

fun getPossibleEnPassants(origin: String): Array<String> {

    val piece = getPiece(origin)

    val pieceX = origin.first()
    val pieceY = origin.last()

    val nextY = pieceY + if (piece == 'W') 1 else -1
    val secondNextY = pieceY + if (piece == 'W') 2 else -2

    val leftX: Char? = if (pieceX == 'a') null else {
        columnAsChar(columnAsNumber(pieceX)-1)
    }
    val rightX: Char? = if (pieceX == 'h') null else {
        columnAsChar(columnAsNumber(pieceX)+1)
    }

    val diagonalLeft = "$leftX$nextY"
    val diagonalRight = "$rightX$nextY"

    //To be checked on prevCB, not cb
    val enPassantLeft = "$leftX${secondNextY}"
    val enPassantRight = "$rightX${secondNextY}"

    //Diagonal forward, if there's an adjacent enemy on the current board while there's an enemy forward 2 left/right 1 just before
    var enPassant = arrayOf<String>()
    if (!diagonalLeft.contains("null")) {
        if (getPiece(enPassantLeft, prevCB) == oppositePiece) {
            if (getPiece("$leftX$pieceY") == oppositePiece) {
                enPassant+=arrayOf(diagonalLeft)
            }
        }
    }
    if (!diagonalRight.contains("null")) {
        if (getPiece(enPassantRight, prevCB) == oppositePiece) {
            if (getPiece("$rightX$pieceY") == oppositePiece) {
                enPassant+=arrayOf(diagonalRight)
            }
        }
    }

    return enPassant

}

fun getPossibleDestinations(origin: String): Array<String> {

    return getPossibleForwards(origin).plus(getPossibleCaptures(origin)).plus(getPossibleEnPassants(origin))

}

fun columnAsNumber(column: Char): Int {
    return when (column) {
        'a' -> 0
        'b' -> 1
        'c' -> 2
        'd' -> 3
        'e' -> 4
        'f' -> 5
        'g' -> 6
        else -> 7
    }
}

fun columnAsChar(column: Int): Char {
    return when (column) {
        0 -> 'a'
        1 -> 'b'
        2 -> 'c'
        3 -> 'd'
        4 -> 'e'
        5 -> 'f'
        6 -> 'g'
        else -> 'h'
    }
}

fun regexToLocation(location: String): Array<Int> {
    return if (location.matches(Regex("[a-h][1-8]"))) {
        val column = columnAsNumber(location[0])
        val row = location[1].digitToInt() - 1
        arrayOf(row,column)
    } else {
        arrayOf()
    }
}

fun copyBoard(board: MutableList<MutableList<Char>>): MutableList<MutableList<Char>> {
    val boardCopy = mutableListOf<MutableList<Char>>()

    for (list in board) {
        val tempList = mutableListOf<Char>()
        for (char in list) tempList.add(char)
        boardCopy.add(tempList)
    }

    return boardCopy
}
