package TicTacToe;

public interface WinStrategy {
    boolean checkWin(char[][] board, char symbol);
}