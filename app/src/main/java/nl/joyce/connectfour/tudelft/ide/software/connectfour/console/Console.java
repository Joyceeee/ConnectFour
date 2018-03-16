package nl.joyce.connectfour.tudelft.ide.software.connectfour.console;

import java.util.Scanner;

import nl.joyce.connectfour.tudelft.ide.software.connectfour.model.Game;
import nl.joyce.connectfour.tudelft.ide.software.connectfour.model.Player;
import nl.joyce.connectfour.tudelft.ide.software.connectfour.exception.ColumnRangeException;
/**
 * Created by joyce on 24-2-2018.
 */

public class Console {

    public static Scanner scanIn = new Scanner(System.in);

    private static int selectColumn(Player player) {
        int columnNumber = -1;
        System.out.print(player.getName() + ", please enter the column number of your next move: "); // MDH CORRECTION: ln removed, looks nicer!!!
        // Iterate till we get a valid column number.
        while (columnNumber == -1) {
            try {
                if (scanIn.hasNextInt()) {
                    int inputNumber = scanIn.nextInt();
                    // if is it in the range of a column number, throw an exception
                    if (inputNumber < 1 || inputNumber > Game.NB_COLUMNS) { // MDH CORRECTION: && should be ||
                        throw new ColumnRangeException();
                    }
                    columnNumber = inputNumber;
                }
            } catch (NumberFormatException | ColumnRangeException e) {
                // The conversion failed, the input was not a int
                System.out.println("Wrong column format, please try again: ");
            }
        }
        return columnNumber;
    }
    /**
     * A main function to start the game in console mode.
     *
     * @param args
     */
    public static void main(String[] args) {

        Player player1 = promptForPlayerName();
        Player player2 = new Player("Computer"); // MDH CORRECTION: No need for ConsolePlayer anymore!!!
        Game game = new Game(player1, player2);

        // iterate till the game is over
        while (!game.isGameOver()) {
            Player player = game.getCurrentPlayer();
            int column = selectColumn(player);
            int row = game.move(column);
            if (row>=game.NB_ROWS) // an invalid move, because the column is full
                System.out.println("The selected column is already full. Please try again.");
            else // a valid move, show the game board
                System.out.println(game);
			/* MDH getNextPlayer() is replaced with getCurrentPlayer(), selectColumn() is now a local static method,
				   and the playerId is removed from move, and move now returns a row
			Player player = game.getNextPlayer();
			boolean validMove = false;
			int playerId = player.getGameId();
			while (!validMove) {
				int column = player.selectColumn();
				validMove = game.move(playerId, column);
			}
			System.out.println(game);
			*/
        }

        // The game is over, we show the results
        System.out.println(game.results());

    }

    public static Player promptForPlayerName() {
        System.out.print("Enter the name of a player: "); // MDH CORRECTION: ln removed, blank added
        if (scanIn.hasNextLine()) {
            String inputString = scanIn.nextLine();
            Player player = new Player(inputString); // MDH CORRECTION: same here!!!
            System.out.println(player);
            return player;
        }
        return null;
    }
}
