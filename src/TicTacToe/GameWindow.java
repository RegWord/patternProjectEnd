package TicTacToe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWindow extends JFrame implements ActionListener {
    private JButton[][] buttons;
    private JLabel scoreLabel;
    private Game game;

    public GameWindow() {
        game = Game.getInstance();
        buttons = new JButton[3][3];
        choosePlayerSymbol();
        initUI();
    }

    private void choosePlayerSymbol() {
        String[] options = {"X", "O"};
        int choice = JOptionPane.showOptionDialog(this, "Choose your symbol:", "Tic Tac Toe",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        char userSymbol = options[choice].charAt(0);
        game.initializePlayers(userSymbol);
    }

    private void initUI() {
        setTitle("Tic Tac Toe");
        setSize(300, 350);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel boardPanel = new JPanel(new GridLayout(3, 3));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 60));
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].addActionListener(this);
                boardPanel.add(buttons[i][j]);
            }
        }
        add(boardPanel, BorderLayout.CENTER);

        scoreLabel = new JLabel("Player X Wins: 0 | Player O Wins: 0", JLabel.CENTER);
        add(scoreLabel, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clickedButton = (JButton) e.getSource();
        int row = -1, col = -1;

        // Находим координаты кнопки
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j] == clickedButton) {
                    row = i;
                    col = j;
                    break;
                }
            }
        }

        if (row != -1 && col != -1 && buttons[row][col].getText().equals("") && game.getCurrentPlayer() == game.getUserPlayer()) {
            // Ход игрока
            char symbol = game.getUserPlayer().getSymbol();
            buttons[row][col].setText(String.valueOf(symbol));

            MoveCommand move = new MoveCommand(game.getBoard(), row, col, symbol);
            move.execute();

            // Проверяем победу или ничью
            if (game.getWinStrategy().checkWin(game.getBoard().getBoard(), symbol)) {
                JOptionPane.showMessageDialog(this, "Player " + symbol + " wins!");
                game.incrementWin(symbol);
                updateScoreLabel();
                resetBoard();
            } else if (isBoardFull()) {
                JOptionPane.showMessageDialog(this, "It's a draw!");
                resetBoard();
            } else {
                game.switchPlayer();
                botTurn();
            }
        }
    }

    private void botTurn() {
        game.botMove();
        updateBoardUI();

        // Проверяем победу или ничью для бота
        if (game.getWinStrategy().checkWin(game.getBoard().getBoard(), game.getBotPlayer().getSymbol())) {
            JOptionPane.showMessageDialog(this, "Bot wins!");
            game.incrementWin(game.getBotPlayer().getSymbol());
            updateScoreLabel();
            resetBoard();
        } else if (isBoardFull()) {
            JOptionPane.showMessageDialog(this, "It's a draw!");
            resetBoard();
        } else {
            game.switchPlayer();
        }
    }

    private void updateBoardUI() {
        char[][] board = game.getBoard().getBoard();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] != '\0' && buttons[i][j].getText().equals("")) {
                    buttons[i][j].setText(String.valueOf(board[i][j]));
                }
            }
        }
    }

    private void updateScoreLabel() {
        scoreLabel.setText("Player X Wins: " + game.getPlayerXWins() + " | Player O Wins: " + game.getPlayerOWins());
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
        game.resetGame();
    }
}

