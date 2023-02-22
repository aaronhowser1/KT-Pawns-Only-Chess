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
    mutableListOf('B','B','B','B','B','B','B','B'),
    mutableListOf(' ',' ',' ',' ',' ',' ',' ',' ')
)

var prevCB = cb

var playerOnesTurn = true

var currentPiece: Char = ' '
    get() = if (playerOnesTurn) 'W' else 'B'

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

    if (move.matches(Regex("[a-h][1-8][a-h][1-8]"))) {
        val origin = "${move[0]}${move[1]}"
        val destination = "${move[2]}${move[3]}"

//        println(getPossibleDestinations(origin).joinToString(" "))

        if (currentPiece == getPiece(origin)) {
            if (getPiece(destination) == ' ') {
                if (getPossibleDestinations(origin).contains(destination)) {
                    prevCB = cb
                    setPiece(origin,' ')
                    setPiece(destination, currentPiece)
                    drawChessboard()
                    playerOnesTurn = !playerOnesTurn
                } else {
                    println("Invalid Input")
                }
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

fun getPossibleDestinations(origin: String): Array<String> {
    val piece = getPiece(origin)
//    val nextRow = if (piece == 'W') origin[1]+1 else origin[1]-1

    val pieceX = origin.first()
    val pieceY = origin.last()

    val oneForward = "${pieceX}${pieceY + (if (piece == 'W') 1 else -1)}"
    val twoForward = "${pieceX}${pieceY + (if (piece == 'W') 2 else -2)}"

    //Two forward or one forward, if first move
    val firstMoves: Array<String> = if (piece == 'W') {
        if (origin[1] == '2') {
            arrayOf(
                oneForward,
                twoForward
            )
        } else {
            arrayOf(oneForward)
        }
    } else if (piece == 'B'){
        if (origin[1] == '7') {
            arrayOf(
                "${origin[0]}6",
                "${origin[0]}5"
            )
        } else {
            arrayOf("${origin[0]}${origin[1]-1}")
        }
    } else arrayOf()

    //One forward, anywhere
    val secondMoves: Array<String> = if (piece != ' ' && getPiece(oneForward) == ' ') {
        arrayOf(oneForward)
    } else {
        arrayOf()
    }
//    val secondMoves: Array<String> = if (piece == 'W') {
//        if (getPiece(oneForward) == ' ') {
//            arrayOf(oneForward)
//        } else arrayOf()
//    } else if (piece == 'B') {
//        if (getPiece(oneForward) == ' ') {
//            arrayOf(oneForward)
//        } else arrayOf()
//    } else arrayOf()

    //Diagonal forward, if there's an enemy piece
    val capturing: Array<String> = if (piece == 'W') {
        var diagonals = arrayOf<String>()
//        if (getPiece("") ) {}
        arrayOf()

    } else if (piece == 'B') {
        arrayOf()
    } else arrayOf()

    //Diagonal forward, if there's an adjacent enemy on the current board while there's an enemy forward 2 left/right 1 just before
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