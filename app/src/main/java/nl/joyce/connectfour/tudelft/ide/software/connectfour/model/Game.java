package nl.joyce.connectfour.tudelft.ide.software.connectfour.model;

/**
 * Created by joyce on 24-2-2018.
 */

public class Game {
    /**
     * Number of aligned token to win
     */
    private static final int WIN_LENGTH = 4;
    // MDH CORRECTION: there are six (6) horizontal rows and seven (7) vertical columns on the board!!!!
    /**
     * Number of rows on the board
     */
    public static final int NB_ROWS = 6;
    /**
     * Number of columns on the board
     */
    public static final int NB_COLUMNS = 7;
    /**
     * Player's symbol on the board
     */
    private static final char[] SYMBOL = {' ', 'x', 'o'};

    // MDH CORRECTION: we're NOT subclassing yet!!!!
    /**
     * The players of the game
     */
    private Player player1;
    private Player player2;

    private int playerId=1; // MDH ADDITION: keep track of the current player id!!!

    public Player getCurrentPlayer() { return (playerId==1?player1:player2); } // MDH ADDITION: expose the current player

    /**
     * The board of the game.
     */
    private int[][] board;

    /**
     * State of the game.
     */
    private int winnerId = 0;
    public int getWinnerId() { return winnerId; } // MDH expose winner!!!!

    private int moveCount = 0;

    /**
     * Keep track of the last move.
     */
    private int lastPlayerId = 2; // MDH CORRECTION: not subclassing yet
    private int lastRow;
    private int lastColumn;

    /**
     * @param player1 the player 1 (not necessarily the first to play)
     * @param player2 the player 2
     */
    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        player1.setGameId(1);
        player2.setGameId(2);

        initializeGrid(); // MDH COMMENT: not actually required all array elements default to zero anyway
    }

    private void initializeGrid() {
        // instantiate and initialize the board
        board = new int[NB_ROWS][NB_COLUMNS];
        for (int i = 0; i < NB_ROWS; i++) {
            for (int j = 0; j < NB_COLUMNS; j++) {
                board[i][j] = 0;
            }
        }
    }

    // MDH remove playerId from move, and have it return the row being occupied!!!!!
    /**
     * Tries to apply the move.
     *
     * @param column   the column number (between 0 and nbColumn)
     * @return false if the column was already full, true otherwise
     */
    public int move(int column) {
        // We search for the first free row for the given column
        int row = getFirstRowAvailable(column);
        // If we did not reach the limit of the board, there is a free row
        if (row <= NB_ROWS) {
            setMove(row, column);
            if (checkWinner()) {
                winnerId=playerId;
                // MDH ADDITION: update the result of each winner accordingly
                switch (winnerId) {
                    case 1: player1.addVictory(); player2.addDefeat(); break;
                    case 2: player1.addDefeat(); player1.addVictory(); break;
                }
            } else { // MDH ADDITION: switch players if a valid move
                playerId=3-playerId;
                if(moveCount==getMaxMoves()) { // nobody won
                    player1.addDraw(); player2.addDraw();
                }
            }
        }
        return row;
    }

    /**
     * Look for the first row available for a given column.
     *
     * @param column the column to look at
     * @return row number of the first free row,
     * if row > number of rows, the column is full
     */
    private int getFirstRowAvailable(int column) {
        int row = 1;
        while (row <= NB_ROWS && board[row - 1][column - 1] != 0) {
            row++;
        }
        return row;
    }

    /**
     * Check is we reach the condition of game over:
     * a winnerId or a maximum number of moves.
     *
     * @return true if the game is over.
     */
    public boolean isGameOver() {
        return (winnerId > 0 || moveCount == getMaxMoves());
    }

    /**
     * Calculate the maximum number of moves.
     *
     * @return Maximum number of moves
     */
    private int getMaxMoves() {
        return NB_ROWS * NB_COLUMNS;
    }

    /**
     * This method is called to apply a move
     * once we know it is valid.
     *
     * @param column   The column of the board
     * @param row      The row of the board
     */
    private void setMove(int row,
                         int column) {
        board[row - 1][column - 1] = playerId;
        lastPlayerId = playerId;
        lastColumn = column;
        lastRow = row;
        moveCount++;
    }

    /**
     * Count the number of identical tokens in the specified direction.
     *
     * @param deltaColumn column direction
     * @param deltaRow    row direction
     * @return number of identical tokens
     */
    private int countIdentical(int deltaColumn,
                               int deltaRow) {
        int count = 0;
        // The first cell to check
        int columnIndex = lastColumn - 1 + deltaColumn;
        int rowIndex = lastRow - 1 + deltaRow;

        while (columnIndex >= 0 && columnIndex < NB_COLUMNS         // Is the current position in the column limits
                && rowIndex >= 0 && rowIndex < NB_ROWS              // Is the current position in the row limits
                && board[rowIndex][columnIndex] == lastPlayerId) {  // Is the token belongs to the last player
            count++;
            columnIndex += deltaColumn;
            rowIndex += deltaRow;
        }
        return count;
    }

    /**
     * Check WIN_LENGTH horizontally from the last move.
     * (using appropriate calls to countIdentical)
     *
     * @return returns true if 4 in a row horizontally, false otherwise
     */
    private boolean horizontal() {
        return (countIdentical(-1, 0) + countIdentical(1, 0) >= WIN_LENGTH - 1);
    }

    /**
     * Check WIN_LENGTH vertically from the last move.
     * (using appropriate calls to countIdentical)
     *
     * @return true if 4 in a row vertically, false otherwise
     */
    private boolean vertical() {
        return (countIdentical(0, -1) + countIdentical(0, 1) >= WIN_LENGTH - 1);
    }

    /**
     * Check WIN_LENGTH diagonally from the last move.
     * (using appropriate calls to countIdentical)
     *
     * @return true if 4 in a row diagonally, false otherwise
     */
    private boolean diagonal() {
        return (countIdentical(-1, -1) + countIdentical(1, 1) >= WIN_LENGTH - 1);
    }

    /**
     * Check WIN_LENGTH on the reverse diagonal from the last move.
     * (using appropriate calls to countIdentical)
     *
     * @return true if 4 in a row reverse diagonally, false otherwise.
     */
    private boolean reverseDiagonal() {
        return (countIdentical(-1, 1) + countIdentical(1, -1) >= WIN_LENGTH - 1);
    }

    /**
     * Check in all position from the last move to detect a potential winnerId.
     *
     * @return true if the current player won, false otherwise
     */
    private boolean checkWinner() {
        // No need to check before the first player reach WIN_LENGTH number of moves
        if (moveCount > (WIN_LENGTH - 1) * 2) {
            // We need to know the last column and row occupied
            if (horizontal()) return true;
            if (vertical()) return true;
            if (diagonal()) return true;
            if (reverseDiagonal()) return true;
        }
        return false;
    }


    /**
     * Get the next player, the first one is selected randomly.
     *
     * @return the player of the next move.
     */
    // MDH now obsolete
	/*
	public Player getNextPlayer() {
		if (lastPlayerId == player1.getGameId()) {
			return player2;
		}
		return player1;
	}
	*/
    /**
     * Override parent's method 'toString' to show the board.
     *
     * @return String representing the board in its current state
     */
    @Override
    public String toString() {
        String strBoard = "";
        for (int i = 1; i <= NB_COLUMNS; i++) {
            strBoard += " " + i;
        }
        strBoard += " \n";
        for (int i = NB_ROWS - 1; i >= 0; i--) {
            strBoard += "|";
            for (int j = 0; j < NB_COLUMNS; j++) {
                strBoard += SYMBOL[board[i][j]] + "|";
            }
            strBoard += "\n";
        }
        return strBoard;
    }

    public String results() {
        String resultMessage = "## ## ## ## ##\n";
        if (winnerId==1) {
            resultMessage += player1.getName();
        } else if(winnerId==2) {
            resultMessage += player2.getName();
        } else {
            resultMessage += "Nobody";
        }
        resultMessage += " won the game!\n"; // MDH CORRECT

        // add the display of the board
        resultMessage += toString();

        return resultMessage;
    }

}
