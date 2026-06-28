import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Random;
public class EliteTicTacToe extends JFrame {
    private final int BOARD_SIZE = 3;
    private final char[][] board = new char[BOARD_SIZE][BOARD_SIZE];
    private char currentPlayer = 'X';
    private boolean gameActive = true;
    private boolean isAiMode = false;
    private String statusText = "Player X's Turn";
    private int scoreX = 0;
    private int scoreO = 0;
    private final Color COLOR_BG_TOP = new Color(15, 23, 42); 
    private final Color COLOR_BG_BOTTOM = new Color(30, 41, 59);   // Rich Obsidian Slate
    private final Color COLOR_CARD = new Color(51, 65, 85, 180);   // frosted glass
    private final Color COLOR_GRID_LINE = new Color(71, 85, 105);  // Grid separators
    private final Color COLOR_TEXT = new Color(241, 245, 249);    // Crisp white text
    private final Color COLOR_X = new Color(244, 63, 94);         // Cyber Neon Pink
    private final Color COLOR_O = new Color(14, 165, 233);        // Neon Cyan
    private final Color COLOR_ACCENT = new Color(16, 185, 129);   // Emerald Green
    private final GameCanvas canvas;
    public EliteTicTacToe() {
        setTitle("Tic-Tac-Toe Elite Pro + AI");
        setSize(460, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initializeBoard();
        canvas = new GameCanvas();
        add(canvas, BorderLayout.CENTER);
    }
    private void initializeBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = ' ';
            }
        }
    }
    private class GameCanvas extends JPanel {
        private int hoverRow = -1;
        private int hoverCol = -1;
        public GameCanvas() {
            setBackground(COLOR_BG_TOP);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    handleCanvasClick(e.getX(), e.getY());
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    hoverRow = -1;
                    hoverCol = -1;
                    repaint();
                }
            });
            addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    handleCanvasHover(e.getX(), e.getY());
                }
            });
        }
        private void handleCanvasHover(int x, int y) {
            if (!gameActive || (isAiMode && currentPlayer == 'O')) return;
            int gridLeft = 50;
            int gridTop = 190;
            int cellSize = 120;
            if (x >= gridLeft && x <= gridLeft + 360 && y >= gridTop && y <= gridTop + 360) {
                int col = (x - gridLeft) / cellSize;
                int row = (y - gridTop) / cellSize;
                if (row != hoverRow || col != hoverCol) {
                    hoverRow = row;
                    hoverCol = col;
                    repaint();
                }
            } else {
                if (hoverRow != -1 || hoverCol != -1) {
                    hoverRow = -1;
                    hoverCol = -1;
                    repaint();
                }
            }
        }

        private void handleCanvasClick(int x, int y) {
            if (x >= 35 && x <= 220 && y >= 20 && y <= 60) {
                isAiMode = false;
                hardReset();
                repaint();
                return;
            }
            if (x >= 240 && x <= 425 && y >= 20 && y <= 60) {
                isAiMode = true;
                hardReset();
                repaint();
                return;
            }
            if (x >= 130 && x <= 330 && y >= 580 && y <= 625) {
                resetMatch();
                repaint();
                return;
            }

            if (!gameActive || (isAiMode && currentPlayer == 'O')) return;

            int gridLeft = 50;
            int gridTop = 190;
            int cellSize = 120;

            if (x >= gridLeft && x <= gridLeft + 360 && y >= gridTop && y <= gridTop + 360) {
                int col = (x - gridLeft) / cellSize;
                int row = (y - gridTop) / cellSize;

                if (board[row][col] == ' ') {
                    processMove(row, col);
                    if (gameActive && isAiMode && currentPlayer == 'O') {
                        statusText = "Computer is thinking...";
                        repaint();
                        Timer aiTimer = new Timer(500, event -> triggerAiMove());
                        aiTimer.setRepeats(false);
                        aiTimer.start();
                    }
                }
            }
        }
        private void processMove(int row, int col) {
            board[row][col] = currentPlayer;

            if (checkWin()) {
                statusText = "🎉 Player " + currentPlayer + " Wins! 🎉";
                if (currentPlayer == 'X') scoreX++; else scoreO++;
                gameActive = false;
            } else if (isBoardFull()) {
                statusText = "🤝 It's a Premium Tie! 🤝";
                gameActive = false;
            } else {
                currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
                statusText = (isAiMode && currentPlayer == 'O') ? "Computer is thinking..." : "Player " + currentPlayer + "'s Turn";
            }
            repaint();
        }

        private void triggerAiMove() {
            if (!gameActive) return;
            Point bestMove = findWinOrBlock('O');
            if (bestMove == null) {
                bestMove = findWinOrBlock('X');
            }
            if (bestMove == null && board[1][1] == ' ') {
                bestMove = new Point(1, 1);
            }
            if (bestMove == null) {
                ArrayList<Point> available = new ArrayList<>();
                for (int r = 0; r < BOARD_SIZE; r++) {
                    for (int c = 0; c < BOARD_SIZE; c++) {
                        if (board[r][c] == ' ') available.add(new Point(r, c));
                    }
                }
                if (!available.isEmpty()) {
                    bestMove = available.get(new Random().nextInt(available.size()));
                }
            }

            if (bestMove != null) {
                processMove(bestMove.x, bestMove.y);
            }
        }

        private Point findWinOrBlock(char token) {
            // Check Rows
            for (int r = 0; r < BOARD_SIZE; r++) {
                if (board[r][0] == token && board[r][1] == token && board[r][2] == ' ') return new Point(r, 2);
                if (board[r][0] == token && board[r][2] == token && board[r][1] == ' ') return new Point(r, 1);
                if (board[r][1] == token && board[r][2] == token && board[r][0] == ' ') return new Point(r, 0);
            }
            // Check Columns
            for (int c = 0; c < BOARD_SIZE; c++) {
                if (board[0][c] == token && board[1][c] == token && board[2][c] == ' ') return new Point(2, c);
                if (board[0][c] == token && board[2][c] == token && board[1][c] == ' ') return new Point(1, c);
                if (board[1][c] == token && board[2][c] == token && board[0][c] == ' ') return new Point(0, c);
            }
            // Diagonals
            if (board[0][0] == token && board[1][1] == token && board[2][2] == ' ') return new Point(2, 2);
            if (board[0][0] == token && board[2][2] == token && board[1][1] == ' ') return new Point(1, 1);
            if (board[1][1] == token && board[2][2] == token && board[0][0] == ' ') return new Point(0, 0);

            if (board[0][2] == token && board[1][1] == token && board[2][0] == ' ') return new Point(2, 0);
            if (board[0][2] == token && board[2][0] == token && board[1][1] == ' ') return new Point(1, 1);
            if (board[1][1] == token && board[2][0] == token && board[0][2] == ' ') return new Point(0, 2);

            return null;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            GradientPaint gradient = new GradientPaint(0, 0, COLOR_BG_TOP, 0, getHeight(), COLOR_BG_BOTTOM);
            g2.setPaint(gradient);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
            g2.setColor(!isAiMode ? COLOR_ACCENT : COLOR_CARD);
            g2.fillRoundRect(35, 20, 185, 40, 10, 10);
            g2.setColor(COLOR_TEXT);
            g2.drawString("PvP Multi-Player", 75, 45);
            g2.setColor(isAiMode ? COLOR_ACCENT : COLOR_CARD);
            g2.fillRoundRect(240, 20, 185, 40, 10, 10);
            g2.setColor(COLOR_TEXT);
            g2.drawString("VS Computer (AI)", 280, 45);
            g2.setColor(COLOR_CARD);
            g2.fillRoundRect(25, 75, 410, 95, 16, 16);
            g2.setColor(COLOR_TEXT);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 20));
            String oLabel = isAiMode ? "CPU" : "Player O";
            g2.drawString("Player X:  " + scoreX + "   |   " + oLabel + ":  " + scoreO, 105, 115);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            g2.setColor(new Color(148, 163, 184));
            g2.drawString(statusText, 175, 145);
            int gridLeft = 50;
            int gridTop = 190;
            int cellSize = 120;
            g2.setColor(COLOR_CARD);
            g2.fillRoundRect(gridLeft - 10, gridTop - 10, 380, 380, 24, 24);
            if (gameActive && hoverRow != -1 && hoverCol != -1 && board[hoverRow][hoverCol] == ' ') {
                g2.setColor(new Color(255, 255, 255, 20));
                g2.fillRoundRect(gridLeft + hoverCol * cellSize + 5, gridTop + hoverRow * cellSize + 5, cellSize - 10, cellSize - 10, 12, 12);
            }
            g2.setColor(COLOR_GRID_LINE);
            g2.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.draw(new Line2D.Float(gridLeft + cellSize, gridTop + 10, gridLeft + cellSize, gridTop + 350));
            g2.draw(new Line2D.Float(gridLeft + cellSize * 2, gridTop + 10, gridLeft + cellSize * 2, gridTop + 350));
            g2.draw(new Line2D.Float(gridLeft + 10, gridTop + cellSize, gridLeft + 350, gridTop + cellSize));
            g2.draw(new Line2D.Float(gridLeft + 10, gridTop + cellSize * 2, gridLeft + 350, gridTop + cellSize * 2));
            for (int r = 0; r < BOARD_SIZE; r++) {
                for (int c = 0; c < BOARD_SIZE; c++) {
                    int xPos = gridLeft + c * cellSize + 25;
                    int yPos = gridTop + r * cellSize + 25;
                    int size = 70;

                    if (board[r][c] == 'X') {
                        // Neon Pink Shadow Blur
                        g2.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                        g2.setColor(new Color(244, 63, 94, 55)); 
                        g2.draw(new Line2D.Float(xPos, yPos, xPos + size, yPos + size));
                        g2.draw(new Line2D.Float(xPos + size, yPos, xPos, yPos + size));

                        // Solid Foreground
                        g2.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                        g2.setColor(COLOR_X);
                        g2.draw(new Line2D.Float(xPos, yPos, xPos + size, yPos + size));
                        g2.draw(new Line2D.Float(xPos + size, yPos, xPos, yPos + size));

                    } else if (board[r][c] == 'O') {
                        // Neon Cyan Glow Blur
                        g2.setStroke(new BasicStroke(10));
                        g2.setColor(new Color(14, 165, 233, 55));
                        g2.draw(new Ellipse2D.Float(xPos, yPos, size, size));

                        // Solid Foreground
                        g2.setStroke(new BasicStroke(6));
                        g2.setColor(COLOR_O);
                        g2.draw(new Ellipse2D.Float(xPos, yPos, size, size));
                    }
                }
            }
            g2.setColor(COLOR_ACCENT);
            g2.fillRoundRect(130, 580, 200, 45, 12, 12);
            g2.setColor(COLOR_TEXT);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
            g2.drawString("Restart Match", 182, 607);
        }
    }

    private boolean checkWin() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (board[i][0] != ' ' && board[i][0] == board[i][1] && board[i][0] == board[i][2]) return true;
            if (board[0][i] != ' ' && board[0][i] == board[1][i] && board[0][i] == board[2][i]) return true;
        }
        if (board[0][0] != ' ' && board[0][0] == board[1][1] && board[0][0] == board[2][2]) return true;
        if (board[0][2] != ' ' && board[0][2] == board[1][1] && board[0][2] == board[2][0]) return true;
        return false;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == ' ') return false;
            }
        }
        return true;
    }

    private void resetMatch() {
        initializeBoard();
        currentPlayer = 'X';
        gameActive = true;
        statusText = "Player X's Turn";
    }

    private void hardReset() {
        resetMatch();
        scoreX = 0;
        scoreO = 0;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EliteTicTacToe().setVisible(true));
    }
}