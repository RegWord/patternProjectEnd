package TicTacToe;

public class PlayerFactory {
    public static Player createPlayer(String symbol) {
        return new Player(symbol.charAt(0));
    }
}
