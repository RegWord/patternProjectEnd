package TicTacToe;

import java.util.ArrayList;
import java.util.List;

import java.util.Random;

public class Game {
    private static Game instance;
    private Board board;
    private Player currentPlayer;
    private Player userPlayer;
    private Player botPlayer;
    private boolean playWithBot;
    private List<Player> players;
    private WinStrategy winStrategy;
    private int playerXWins;
    private int playerOWins;

    private Game() {
        board = new Board();
        players = new ArrayList<>();
        winStrategy = new DefaultWinStrategy();
        playerXWins = 0;
        playerOWins = 0;
    }

    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    public void initializePlayers(char userSymbol, boolean playWithBot) {
        this.playWithBot = playWithBot;
        userPlayer = new Player(userSymbol);
        if (playWithBot) {
            botPlayer = new Player(userSymbol == 'X' ? 'O' : 'X');
        } else {
            botPlayer = new Player(userSymbol == 'X' ? 'O' : 'X');
        }
        players.add(userPlayer);
        players.add(botPlayer);
        currentPlayer = userPlayer;
    }

    public boolean isPlayWithBot() {
        return playWithBot;
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer == userPlayer) ? botPlayer : userPlayer;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getUserPlayer() {
        return userPlayer;
    }

    public Player getBotPlayer() {
        return botPlayer;
    }

    public Board getBoard() {
        return board;
    }

    public WinStrategy getWinStrategy() {
        return winStrategy;
    }

    public void resetGame() {
        board = new Board();
        currentPlayer = userPlayer;
    }

    public void incrementWin(char symbol) {
        if (symbol == 'X') {
            playerXWins++;
        } else if (symbol == 'O') {
            playerOWins++;
        }
    }

    public int getPlayerXWins() {
        return playerXWins;
    }

    public int getPlayerOWins() {
        return playerOWins;
    }

    public void botMove() {
        Random rand = new Random();
        int row, col;
        do {
            row = rand.nextInt(3);
            col = rand.nextInt(3);
        } while (board.getBoard()[row][col] != '\0');

        MoveCommand move = new MoveCommand(board, row, col, botPlayer.getSymbol());
        move.execute();
    }
}



