package nl.joyce.connectfour.tudelft.ide.software.connectfour.model;

/**
 * Created by joyce on 24-2-2018.
 */

public class Player {
    /**
     * Player's name
     */
    private String name;
    /**
     * Number of time the player played a game
     */
    private int playedGames;
    /**
     * Number of time the player won a game.
     */
    private int wonGames;
    // MDH we keep track of the games actually lost as well
    /**
     * Number of time the player lost a game.
     */
    private int lostGames;
    /**
     * id of the player during a game (1 or 2)
     */
    private int gameId;

    /**
     * Constructor
     *
     * @param playerName the name of the player
     */
    public Player(String playerName) {
        name = playerName;
        playedGames = 0;
        wonGames = 0;
        lostGames = 0; // MDH keep track of the lost games as well
    }

    public String getName() {
        return name;
    }

    public int getPlayedGames() {
        return playedGames;
    }

    public int getWonGames() {
        return wonGames;
    }

    public int getLostGames() {
        return lostGames;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    /**
     * Increment both the number of played and won games
     */
    public void addVictory() {
        wonGames++;             // equivalent to wonGames += 1 or wonGames = wonGames + 1
        playedGames++;
    }

    /**
     * Increment only the number of played games
     */
    public void addDefeat() {
        lostGames++; // MDH count that the game was lost
        playedGames++;
    }

    // MDH when a game does not have a winner
    public void addDraw() {
        playedGames++;
    }

    /**
     * String view of the player.
     *
     * @return player name and stats
     */
    @Override
    public String toString() {
        return name + " (" + wonGames + "/" + playedGames + ")";
    }


}
