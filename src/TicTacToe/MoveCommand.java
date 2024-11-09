package TicTacToe;

public class MoveCommand implements Command {
    private int x, y;
    private char symbol;
    private Board board;

    public MoveCommand(Board board, int x, int y, char symbol) {
        this.board = board;
        this.x = x;
        this.y = y;
        this.symbol = symbol;
    }

    @Override
    public void execute() {
        board.updateBoard(x, y, symbol);
    }
}
