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

fun main() {
    showMenu()
}

fun showMenu() {
    println("Pawns-Only Chess")
    val player1 = inputFromPrompt("First Player's name:")
    val player2 = inputFromPrompt("Second Player's name:")

    var playerOnesTurn = true
    drawChessboard()
    while (true) {
        val currentPlayer = if (playerOnesTurn) player1 else player2
        val input = inputFromPrompt("$currentPlayer's turn:")
        if (input == "exit") {
            println("Bye!")
            break
        }
        if (input.matches(Regex("[a-h][1-8][a-h][1-8]"))) {
            playerOnesTurn = !playerOnesTurn
        } else {
            println("Invalid Input")
        }
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