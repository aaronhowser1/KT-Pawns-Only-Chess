package chess

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
    drawChessboard()
}

fun drawChessboard() {
    val line = "+---+---+---+---+---+---+---+---+"

    println("""
         Pawns-Only Chess
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