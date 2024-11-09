package TicTacToe;

public class Board {
    private char[][] board;

    public Board() {
        board = new char[3][3];
    }

    public void updateBoard(int x, int y, char symbol) {
        board[x][y] = symbol;
    }

    public char[][] getBoard() {
        return board;
    }
}
