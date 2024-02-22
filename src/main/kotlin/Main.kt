import kotlin.random.Random

/**
 * Implementation of a Connect 4 Game. This game allows a human player to play a random computer player. To win, a player
 * must place 4 of their pieces in a row (horizontally, vertically, or diagonally).
 *
 * @author Ryan Johnson
 * @property HUMAN_PLAYER ID number for the human player on the game board, used for identifying pieces on the game board
 * and keeping track of which player is currently playing
 * @property COMPUTER_PLAYER ID number for the computer player on the game board, used for identifying pieces on the game board
 *  * and keeping track of which player is currently playing
 * @property currentPlayer identifies which player is currently playing (1 for the human player, 2 for the computer player)
 * @property board 5x7 array storing the state of the game board
 */
class Connect4Game(private val HUMAN_PLAYER: Int = 1,
                   private val COMPUTER_PLAYER: Int = 2,
                   private var currentPlayer: Int = 1,
                   private var board: Array<Array<Int>> = Array(5) { Array<Int>(7) { 0 } }) {

    /**
     * Takes input from the human player and places their piece on the game board.
     *
     * @return coordinates of newly placed piece
     */
    fun humanMove(): Pair<Int, Int> {
        var chosenCol: Int = -1
        var validCol: Boolean = false
        while (!validCol) {
            print("\nEnter the corresponding number of the column in which to place your piece (0-6): ")
            try {
                chosenCol = readln().toInt()
            } catch(e: NumberFormatException) {
                println("That is not a number. Try again.")
                continue
            }
            if (chosenCol !in 0..6) {
                println("That is not a valid column number. Try again.")
                continue
            }
            if (!placePiece(chosenCol)) {
                continue
            }
            validCol = true
        }

        // Determine which row the played piece landed in
        var col = ArrayList<Int>()
        for (row in board) col.add(row[chosenCol])
        return Pair(col.lastIndexOf(0) + 1, chosenCol)
    }

    /**
     * Randomly chooses a column to play in for the computer and places the piece on the game board.
     *
     * @return coordinates of newly placed piece
     */
    fun computerMove(): Pair<Int, Int> {
        // Sleeps to simulate computer thinking
        Thread.sleep(1000)
        var validCol = false
        var chosenCol: Int = -1
        while (!validCol) {
            chosenCol = Random.nextInt(0,6)
            if (placePiece(chosenCol)) {
                validCol = true
            }
        }

        var col = ArrayList<Int>()
        for (row in board) col.add(row[chosenCol])
        return Pair(col.lastIndexOf(0) + 1, chosenCol)
    }

    /**
     * Places a piece into the designated column for the current player. Moves the piece down the column until it reaches
     * a previously played piece.
     *
     * @param col integer representing the index of the column to place the piece into
     * @return true if the piece is correctly placed; false if the column input is invalid or the column is already full
     */
    fun placePiece(col: Int): Boolean {
        if (col !in 0..6) {
            println("Invalid column number input to playPiece")
            return false
        }
        if  (board[0][col] != 0) {
            println("That column is already full. Try again.")
            return false
        }
        for (i in (0..4).reversed()) {
            if (board[i][col] == 0) {
                board[i][col] = currentPlayer
                return true
            }
        }
        return false
    }

    /**
     * Displays the game board to the console.
     */
    fun displayBoard() {
        println("----------------------------")
        for (col in board) println(col.contentDeepToString())
    }

    /**
     * Determines if either player has won the game.
     *
     * @param chosenRow integer representing the row the last piece was placed in
     * @param chosenCol integer representing the column the last piece was placed in
     * @return true if either player has won; false otherwise
     */
    fun hasWon(chosenRow: Int, chosenCol: Int): Boolean {
        return checkRowWin(chosenRow) ||
                checkColWin(chosenRow, chosenCol) ||
                checkDownLeftDiagWin(chosenRow, chosenCol) ||
                checkDownRightDiagWin(chosenRow, chosenCol) ||
                checkUpLeftDiagWin(chosenRow, chosenCol) ||
                checkUpRightDiagWin(chosenRow, chosenCol)
    }

    /**
     * Determines if there is a win (4 horizontal pieces in a row) in the row that the last piece was placed in
     *
     * @param chosenRow integer representing the row the last piece was placed in
     * @return true if there is a win in the row; false otherwise
     */
    fun checkRowWin(chosenRow: Int): Boolean {
        for (i in 0..3) {
            if (currentPlayer == board[chosenRow][i] &&
                currentPlayer == board[chosenRow][i + 1] &&
                currentPlayer == board[chosenRow][i + 2] &&
                currentPlayer == board[chosenRow][i + 3]
            ) {
                println("row win")
                return true
            }
        }
        return false
    }

    /**
     * Determines if there is a win (4 vertical in a row) in the column that the last piece was placed in
     *
     * @param chosenRow integer representing the row the last piece was placed in
     * @param chosenCol integer representing the column the last piece was placed in
     * @return true if there is a win in the column; false otherwise
     */
    fun checkColWin(chosenRow: Int, chosenCol: Int): Boolean {
        if (chosenRow <= 1) {
            if (currentPlayer == board[chosenRow][chosenCol] &&
                currentPlayer == board[chosenRow + 1][chosenCol] &&
                currentPlayer == board[chosenRow + 2][chosenCol] &&
                currentPlayer == board[chosenRow + 3][chosenCol]
            ) {
                println("col win")
                return true
            }
        }
        return false
    }

    /**
     * Determines if there is a win (4 diagonal in a row) in the diagonal below and to the left from the last piece
     *
     * @param chosenRow integer representing the row the last piece was placed in
     * @param chosenCol integer representing the column the last piece was placed in
     * @return true if there is a win in the diagonal; false otherwise
     */
    fun checkDownLeftDiagWin(chosenRow: Int, chosenCol: Int): Boolean {
        if (chosenRow <= 1 && chosenCol >= 3) {
            for (i in 0..1) {
                for (j in 3..6) {
                    if (currentPlayer == board[i][j] &&
                        currentPlayer == board[i + 1][j - 1] &&
                        currentPlayer == board[i + 2][j - 2] &&
                        currentPlayer == board[i + 3][j - 3]
                    ) {
                        println("downleft win")
                        return true
                    }
                }
            }
        }
        return false
    }

    /**
     * Determines if there is a win (4 diagonal in a row) in the diagonal above and to the left from the last piece
     *
     * @param chosenRow integer representing the row the last piece was placed in
     * @param chosenCol integer representing the column the last piece was placed in
     * @return true if there is a win in the diagonal; false otherwise
     */
    fun checkUpLeftDiagWin(chosenRow: Int, chosenCol: Int): Boolean {
        if (chosenRow >= 3 && chosenCol >= 3) {
            for (i in 3..4) {
                for (j in 3..6) {
                    if (currentPlayer == board[i][j] &&
                        currentPlayer == board[i - 1][j - 1] &&
                        currentPlayer == board[i - 2][j - 2] &&
                        currentPlayer == board[i - 3][j - 3]
                    ) {
                        println("upleft win")
                        return true
                    }
                }
            }
        }
        return false
    }

    /**
     * Determines if there is a win (4 diagonal in a row) in the diagonal below and to the right from the last piece
     *
     * @param chosenRow integer representing the row the last piece was placed in
     * @param chosenCol integer representing the column the last piece was placed in
     * @return true if there is a win in the diagonal; false otherwise
     */
    fun checkDownRightDiagWin(chosenRow: Int, chosenCol: Int): Boolean {
        if (chosenRow <= 1 && chosenCol <= 3) {
            for (i in 0..1) {
                for (j in 0..3) {
                    if (currentPlayer == board[i][j] &&
                        currentPlayer == board[i + 1][j + 1] &&
                        currentPlayer == board[i + 2][j + 2] &&
                        currentPlayer == board[i + 3][j + 3]
                        ) {
                        println("downright win")
                        return true
                    }
                }
            }
        }
        return false
    }

    /**
     * Determines if there is a win (4 diagonal in a row) in the diagonal above and to the right from the last piece
     *
     * @param chosenRow integer representing the row the last piece was placed in
     * @param chosenCol integer representing the column the last piece was placed in
     * @return true if there is a win in the diagonal; false otherwise
     */
    fun checkUpRightDiagWin(chosenRow: Int, chosenCol: Int): Boolean {
        if (chosenRow >= 3 && chosenCol <= 3) {
            for (i in 3..4) {
                for (j in 0..3) {
                    if (currentPlayer == board[i][j] &&
                        currentPlayer == board[i - 1][j + 1] &&
                        currentPlayer == board[i - 2][j + 2] &&
                        currentPlayer == board[i - 3][j + 3]
                    ) {
                        println("upright win")
                        return true
                    }
                }
            }
        }
        return false
    }

    /**
     * Allows user to play as many games as they desire, with the board being displayed throughout the game.
     */
    fun playGame() {
        var playing = true
        while (playing) {
            println("\nWelcome to Connect 4! When looking at the board, your pieces are marked with a 1, the computer's pieces with a 2, and empty spaces with a 0.")
            println("Here's the current board:")
            while (true) {
                displayBoard()

                val humanCoords = humanMove()
                val humanRow = humanCoords.first
                val humanCol = humanCoords.second
                if (hasWon(humanRow, humanCol)) break
                currentPlayer = 2

                val computerCoords = computerMove()
                val computerRow = computerCoords.first
                val computerCol = computerCoords.second
                if (hasWon(computerRow, computerCol)) break
                val comments: List<String> = listOf("The computer played a beautiful move. How will you stop it?", "Well played!", "Keep up the good work!",
                    "Oh, bold move.", "Are you sure that was the right move?", "You got this.", "Nice move!", "I think a 2-year-old would have made a better move there...",
                    "You're getting closer to your inevitable win. Am I talking to you or the computer... you'll never know ;-)")
                var randIndex = Random.nextInt(comments.size)
                println("----------------------------\n${comments[randIndex]}")
                currentPlayer = 1
            }

            if (currentPlayer == 1) println("----------------------------\nCongratulations! Nice win!")
            if (currentPlayer == 2) println("----------------------------\nHow unfortunate... you've been beat by the computer!")
            displayBoard()

            var validInput = false
            while (!validInput) {
                print("\nWould you like to play again? (Y/N)")
                val playAgain = readln()
                if (playAgain.lowercase() !in "yn") {
                    println("Invalid input. Please enter 'Y' to play again or 'N' to quit.")
                    continue
                }
                validInput = true
                if (playAgain == "n") playing = false
            }
            println("----------------------------")
        }
        println("Thanks for playing!")
    }
}

fun main(args: Array<String>) {
    val game = Connect4Game()
    game.playGame()
}