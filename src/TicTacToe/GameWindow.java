package TicTacToe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class GameWindow extends JFrame {
    private JButton[][] buttons;
    private JLabel scoreLabel;
    private Game game;
    private char leftSymbol;
    private char rightSymbol;
    private boolean isPlayerTurn; // Переменная для отслеживания хода игрока

    public GameWindow() {
        game = Game.getInstance();
        buttons = new JButton[3][3];
        chooseGameMode();
        initUI();
    }

    private void chooseGameMode() {
        String[] options = {"Play with Bot", "Play without Bot"};
        int choice = JOptionPane.showOptionDialog(this, "Choose game mode:", "Tic Tac Toe",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        boolean playWithBot = (choice == 0);

        String[] symbols = {"X", "O"};
        int symbolChoice = JOptionPane.showOptionDialog(this, "Choose your symbol:", "Tic Tac Toe",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, symbols, symbols[0]);
        char userSymbol = symbols[symbolChoice].charAt(0);

        // Устанавливаем символы для левой и правой кнопок мыши
        if (userSymbol == 'O') {
            leftSymbol = 'O';
            rightSymbol = 'X';
        } else {
            leftSymbol = 'X';
            rightSymbol = 'O';
        }

        game.initializePlayers(userSymbol, playWithBot);
        isPlayerTurn = true; // Устанавливаем ход игрока в начале
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

                buttons[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        int row = -1, col = -1;

                        // Найти координаты button
                        for (int i = 0; i < 3; i++) {
                            for (int j = 0; j < 3; j++) {
                                if (buttons[i][j] == e.getSource()) {
                                    row = i;
                                    col = j;
                                    break;
                                }
                            }
                        }

                        // Обработка хода игрока
                        if (row != -1 && col != -1 && buttons[row][col].getText().equals("") && isPlayerTurn) {
                            char symbol = (e.getButton() == MouseEvent.BUTTON1) ? leftSymbol : rightSymbol;
                            buttons[row][col].setText(String.valueOf(symbol));

                            MoveCommand move = new MoveCommand(game.getBoard(), row, col, symbol);
                            move.execute();

                            if (game.getWinStrategy().checkWin(game.getBoard().getBoard(), symbol)) {
                                JOptionPane.showMessageDialog(GameWindow.this, "Player " + symbol + " wins!");
                                game.incrementWin(symbol);
                                updateScoreLabel();
                                resetBoard();
                            } else if (isBoardFull()) {
                                JOptionPane.showMessageDialog(GameWindow.this, "It's a draw!");
                                resetBoard();
                            } else {
                                isPlayerTurn = false; // Блокируем ход игрока до следующего раунда
                                game.switchPlayer();

                                if (game.isPlayWithBot() && game.getCurrentPlayer() == game.getBotPlayer()) {
                                    botTurn(); // Ход бота
                                } else {
                                    isPlayerTurn = true; // Разблокируем для следующего игрока в режиме без бота
                                }
                            }
                        }
                    }
                });
                boardPanel.add(buttons[i][j]);
            }
        }
        add(boardPanel, BorderLayout.CENTER);

        scoreLabel = new JLabel("Player X Wins: 0 | Player O Wins: 0", JLabel.CENTER);
        add(scoreLabel, BorderLayout.SOUTH);
    }

    private void botTurn() {
        game.botMove();
        updateBoardUI();

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
            isPlayerTurn = true; // Возвращаем ход игроку после хода бота
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
        isPlayerTurn = true; // Сброс хода к началу игры
    }
}



