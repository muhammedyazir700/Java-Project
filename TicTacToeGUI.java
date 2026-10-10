import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class TicTacToeGUI extends JFrame {

    // Ultra-Premium Dark Slate & Teal Palette
    private static final Color BG_COLOR = new Color(15, 23, 42);       // Slate 900
    private static final Color PANEL_COLOR = new Color(30, 41, 59);    // Slate 800
    private static final Color TEXT_COLOR = new Color(248, 250, 252);  // Slate 50
    private static final Color ACCENT_COLOR = new Color(56, 189, 248); // Light Blue 400
    private static final Color ACCENT_HOVER = new Color(14, 165, 233); // Light Blue 500
    private static final Color X_COLOR = new Color(248, 113, 113);     // Red 400
    private static final Color O_COLOR = new Color(74, 222, 128);      // Green 400
    private static final Color DRAW_COLOR = new Color(250, 204, 21);   // Yellow 400
    private static final Color BUTTON_HOVER = new Color(51, 65, 85);   // Slate 700

    private RoundedButton[] boardButtons = new RoundedButton[9];
    private JComboBox<String> difficultyCombo;
    private JLabel statusLabel;
    
    private int playerWins = 0;
    private int computerWins = 0;
    private int draws = 0;
    private JLabel scoreLabel;
    
    private DefaultTableModel historyTableModel;
    private int gameNumber = 1;
    
    private char playerChar = 'X';
    private char computerChar = 'O';
    
    private Board board;
    private Opponent opponent = new Opponent();
    private boolean isGameActive = true;
    private int movesThisGame = 0;
    
    private Timer animationTimer;
    private JPanel boardWrapper;

    // --- Custom UI Components ---
    class RoundedPanel extends JPanel {
        private int radius;
        public RoundedPanel(int radius) {
            super();
            this.radius = radius;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    class RoundedButton extends JButton {
        private int radius;
        private Color hoverColor;
        private Color defaultColor;
        
        public RoundedButton(String text, int radius, Color defaultBg, Color hoverBg, Color textFg) {
            super(text);
            this.radius = radius;
            this.defaultColor = defaultBg;
            this.hoverColor = hoverBg;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setBackground(defaultBg);
            setForeground(textFg);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { 
                    if (isEnabled() && isHoverable()) setBackground(hoverColor); 
                }
                public void mouseExited(MouseEvent e) { 
                    if (isEnabled()) setBackground(defaultColor); 
                }
            });
        }
        
        protected boolean isHoverable() { return true; }
        public void resetBackground() { setBackground(defaultColor); }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g);
            g2.dispose();
        }
    }
    // ---------------------------

    public TicTacToeGUI() {
        setTitle("Tic-Tac-Toe AI");
        setSize(900, 700);
        setMinimumSize(new Dimension(850, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_COLOR);
        setLayout(new BorderLayout(15, 15));
        
        JPanel rootPanel = new JPanel(new BorderLayout(25, 25));
        rootPanel.setBackground(BG_COLOR);
        rootPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        add(rootPanel);
        
        setupTopPanel(rootPanel);
        setupBoardPanel(rootPanel);
        setupRightPanel(rootPanel);
    }

    private void setupTopPanel(JPanel rootPanel) {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BG_COLOR);
        
        JLabel titleLabel = new JLabel("Tic-Tac-Toe AI");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(ACCENT_COLOR);
        topPanel.add(titleLabel, BorderLayout.WEST);
        
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        controlsPanel.setBackground(BG_COLOR);
        
        JLabel diffLabel = new JLabel("Difficulty:");
        diffLabel.setForeground(TEXT_COLOR);
        diffLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        controlsPanel.add(diffLabel);
        
        String[] difficulties = {"Easy", "Medium", "Hard"};
        difficultyCombo = new JComboBox<>(difficulties);
        difficultyCombo.setSelectedIndex(2);
        difficultyCombo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        difficultyCombo.setBackground(PANEL_COLOR);
        difficultyCombo.setForeground(TEXT_COLOR);
        difficultyCombo.setFocusable(false);
        difficultyCombo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        difficultyCombo.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED && movesThisGame > 0 && isGameActive) {
                int choice = showCustomDialog("Restart Game", "Changing difficulty will restart the current game.<br><br>Continue?", new String[]{"Yes", "No"});
                if (choice == 0) {
                    promptAndStartGame(false);
                }
            }
        });
        controlsPanel.add(difficultyCombo);
        
        RoundedButton newGameBtn = new RoundedButton("New Game", 10, ACCENT_COLOR, ACCENT_HOVER, BG_COLOR);
        newGameBtn.setPreferredSize(new Dimension(110, 35));
        newGameBtn.addActionListener(e -> promptAndStartGame(true));
        controlsPanel.add(newGameBtn);
        
        topPanel.add(controlsPanel, BorderLayout.EAST);
        
        statusLabel = new JLabel("Welcome!");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        statusLabel.setForeground(TEXT_COLOR);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setBorder(new EmptyBorder(15, 0, 5, 0));
        topPanel.add(statusLabel, BorderLayout.SOUTH);
        
        rootPanel.add(topPanel, BorderLayout.NORTH);
    }

    private void setupBoardPanel(JPanel rootPanel) {
        boardWrapper = new JPanel(new GridBagLayout());
        boardWrapper.setBackground(BG_COLOR);
        
        JPanel boardPanel = new JPanel(new GridLayout(3, 3, 12, 12));
        boardPanel.setBackground(BG_COLOR); 
        boardPanel.setPreferredSize(new Dimension(450, 450));
        
        for (int i = 0; i < 9; i++) {
            boardButtons[i] = new RoundedButton("", 20, PANEL_COLOR, BUTTON_HOVER, TEXT_COLOR) {
                @Override
                protected boolean isHoverable() {
                    return isGameActive && getText().isEmpty();
                }
            };
            boardButtons[i].setFont(new Font("Segoe UI", Font.BOLD, 100));
            final int index = i;
            boardButtons[i].addActionListener(e -> handlePlayerMove(index));
            boardPanel.add(boardButtons[i]);
        }
        
        boardWrapper.add(boardPanel);
        rootPanel.add(boardWrapper, BorderLayout.CENTER);
    }

    private void setupRightPanel(JPanel rootPanel) {
        JPanel rightPanel = new JPanel(new BorderLayout(0, 25));
        rightPanel.setBackground(BG_COLOR);
        rightPanel.setPreferredSize(new Dimension(320, 0));
        
        // Scoreboard
        RoundedPanel scorePanel = new RoundedPanel(18);
        scorePanel.setBackground(PANEL_COLOR);
        scorePanel.setLayout(new BorderLayout(0, 10));
        scorePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel scoreTitle = new JLabel("Scoreboard");
        scoreTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        scoreTitle.setForeground(ACCENT_COLOR);
        scorePanel.add(scoreTitle, BorderLayout.NORTH);
        
        scoreLabel = new JLabel(getScoreText());
        scoreLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        scoreLabel.setForeground(TEXT_COLOR);
        scorePanel.add(scoreLabel, BorderLayout.CENTER);
        
        JPanel resetScorePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        resetScorePanel.setOpaque(false);
        RoundedButton resetScoreBtn = new RoundedButton("Reset", 8, BUTTON_HOVER, new Color(71, 85, 105), TEXT_COLOR);
        resetScoreBtn.setPreferredSize(new Dimension(80, 30));
        resetScoreBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        resetScoreBtn.addActionListener(e -> {
            if (showCustomDialog("Confirm Reset", "Are you sure you want to reset scores?", new String[]{"Yes", "No"}) == 0) {
                playerWins = 0; computerWins = 0; draws = 0;
                updateScoreboard();
            }
        });
        resetScorePanel.add(resetScoreBtn);
        scorePanel.add(resetScorePanel, BorderLayout.SOUTH);
        
        rightPanel.add(scorePanel, BorderLayout.NORTH);
        
        // History
        RoundedPanel historyPanel = new RoundedPanel(18);
        historyPanel.setBackground(PANEL_COLOR);
        historyPanel.setLayout(new BorderLayout(0, 15));
        historyPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel historyTitle = new JLabel("Game History");
        historyTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        historyTitle.setForeground(ACCENT_COLOR);
        historyPanel.add(historyTitle, BorderLayout.NORTH);
        
        String[] columnNames = {"#", "Diff", "Result", "Moves"};
        historyTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable historyTable = new JTable(historyTableModel);
        historyTable.setBackground(PANEL_COLOR);
        historyTable.setForeground(TEXT_COLOR);
        historyTable.setGridColor(BUTTON_HOVER);
        historyTable.setRowHeight(35);
        historyTable.setShowVerticalLines(false);
        historyTable.setIntercellSpacing(new Dimension(0, 1));
        
        historyTable.getTableHeader().setBackground(PANEL_COLOR);
        historyTable.getTableHeader().setForeground(ACCENT_COLOR);
        historyTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        historyTable.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BUTTON_HOVER));
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for(int i=0; i<4; i++) historyTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        
        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.getViewport().setBackground(PANEL_COLOR);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        historyPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel clearHistoryPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        clearHistoryPanel.setOpaque(false);
        RoundedButton clearHistoryBtn = new RoundedButton("Clear History", 8, BUTTON_HOVER, new Color(71, 85, 105), TEXT_COLOR);
        clearHistoryBtn.setPreferredSize(new Dimension(110, 30));
        clearHistoryBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        clearHistoryBtn.addActionListener(e -> {
            if (historyTableModel.getRowCount() > 0 && showCustomDialog("Confirm Clear", "Clear game history?", new String[]{"Yes", "No"}) == 0) {
                historyTableModel.setRowCount(0);
                gameNumber = 1;
            }
        });
        clearHistoryPanel.add(clearHistoryBtn);
        historyPanel.add(clearHistoryPanel, BorderLayout.SOUTH);
        
        rightPanel.add(historyPanel, BorderLayout.CENTER);
        rootPanel.add(rightPanel, BorderLayout.EAST);
    }

    private int showCustomDialog(String title, String message, String[] options) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setUndecorated(true);
        
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_COLOR);
        root.setBorder(new LineBorder(ACCENT_COLOR, 2));
        
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(PANEL_COLOR);
        titleBar.setBorder(new EmptyBorder(10, 15, 10, 15));
        
        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLbl.setForeground(ACCENT_COLOR);
        titleBar.add(titleLbl, BorderLayout.WEST);
        
        root.add(titleBar, BorderLayout.NORTH);
        
        JPanel content = new JPanel(new BorderLayout(20, 20));
        content.setBackground(BG_COLOR);
        content.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        JLabel msgLabel = new JLabel("<html><div style='text-align: center; width: 250px;'>" + message + "</div></html>");
        msgLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        msgLabel.setForeground(TEXT_COLOR);
        msgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        content.add(msgLabel, BorderLayout.CENTER);
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnPanel.setOpaque(false);
        
        final int[] result = {-1};
        for (int i = 0; i < options.length; i++) {
            final int index = i;
            RoundedButton btn = new RoundedButton(options[i], 8, BUTTON_HOVER, ACCENT_HOVER, TEXT_COLOR);
            btn.setPreferredSize(new Dimension(160, 35)); // Wide buttons for prompts
            btn.addActionListener(e -> {
                result[0] = index;
                dialog.dispose();
            });
            btnPanel.add(btn);
        }
        content.add(btnPanel, BorderLayout.SOUTH);
        
        root.add(content, BorderLayout.CENTER);
        dialog.setContentPane(root);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        
        return result[0];
    }

    private String getScoreText() {
        String pHex = (playerChar == 'X') ? "#F87171" : "#4ADE80"; // Red if X, Green if O
        String cHex = (computerChar == 'X') ? "#F87171" : "#4ADE80";
        
        return String.format("<html><table width='100%%'>" +
                             "<tr><td align='left'>Player (%c)</td><td align='right'><b><font color='%s'>%d</font></b></td></tr>" +
                             "<tr><td align='left'>Computer (%c)</td><td align='right'><b><font color='%s'>%d</font></b></td></tr>" +
                             "<tr><td align='left'>Draws</td><td align='right'><b>%d</b></td></tr></table></html>", 
            playerChar, pHex, playerWins, computerChar, cHex, computerWins, draws);
    }

    private void updateScoreboard() {
        scoreLabel.setText(getScoreText());
    }

    public void promptAndStartGame(boolean confirm) {
        if (confirm && isGameActive && movesThisGame > 0) {
            int choice = showCustomDialog("Confirm Restart", "Game in progress.<br><br>Restart?", new String[]{"Yes", "No"});
            if (choice != 0) return;
        }
        
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }

        String[] options = {"X", "O"};
        int choice = showCustomDialog("Select Piece", "Choose your piece for this game:", options);
            
        if (choice == 1) {
            playerChar = 'O';
            computerChar = 'X';
        } else {
            // Default or X selected
            playerChar = 'X';
            computerChar = 'O';
        }
        
        board = new Board(playerChar, computerChar);
        isGameActive = true;
        movesThisGame = 0;
        
        updateScoreboard();
        
        for (int i = 0; i < 9; i++) {
            boardButtons[i].setText("");
            boardButtons[i].resetBackground();
            boardButtons[i].setForeground(TEXT_COLOR);
        }
        
        if (playerChar == 'O') {
            statusLabel.setText("Computer is thinking...");
            statusLabel.setForeground(TEXT_COLOR);
            SwingUtilities.invokeLater(this::performComputerMove);
        } else {
            statusLabel.setText("Your turn!");
            statusLabel.setForeground(TEXT_COLOR);
        }
    }

    private void handlePlayerMove(int index) {
        if (!isGameActive || !board.spotAvailable(index)) return;
        
        board.newPiece(playerChar, index);
        movesThisGame++;
        boardButtons[index].setText(String.valueOf(playerChar));
        boardButtons[index].setForeground((playerChar == 'X') ? X_COLOR : O_COLOR);
        
        if (checkGameOver(playerChar)) return;
        
        statusLabel.setText("Computer is thinking...");
        SwingUtilities.invokeLater(this::performComputerMove);
    }

    private void performComputerMove() {
        if (!isGameActive) return;
        
        Opponent.Difficulty diff = Opponent.Difficulty.valueOf(((String)difficultyCombo.getSelectedItem()).toUpperCase());
        int computerMove = opponent.getMove(board, diff);
        
        if (computerMove != -1) {
            board.newPiece(computerChar, computerMove);
            movesThisGame++;
            boardButtons[computerMove].setText(String.valueOf(computerChar));
            boardButtons[computerMove].setForeground((computerChar == 'X') ? X_COLOR : O_COLOR);
            
            checkGameOver(computerChar);
        }
    }

    private boolean checkGameOver(char lastPiece) {
        if (board.isWinner(lastPiece)) {
            isGameActive = false;
            String result;
            if (lastPiece == playerChar) {
                playerWins++;
                result = "Player Wins";
                statusLabel.setText("🎉 YOU WON! 🎉");
                statusLabel.setForeground((playerChar == 'X') ? X_COLOR : O_COLOR);
                playWinEffect(board.getWinningLine(lastPiece), (playerChar == 'X') ? X_COLOR : O_COLOR);
            } else {
                computerWins++;
                result = "Computer Wins";
                statusLabel.setText("💀 DEFEAT 💀");
                statusLabel.setForeground((computerChar == 'X') ? X_COLOR : O_COLOR);
                playLoseEffect(board.getWinningLine(lastPiece), (computerChar == 'X') ? X_COLOR : O_COLOR);
            }
            updateScoreboard();
            addHistoryRecord(result);
            return true;
        } else if (board.isFull()) {
            isGameActive = false;
            draws++;
            statusLabel.setText("⚖️ DRAW ⚖️");
            statusLabel.setForeground(DRAW_COLOR);
            playDrawEffect();
            updateScoreboard();
            addHistoryRecord("Draw");
            return true;
        }
        statusLabel.setText("Your turn!");
        statusLabel.setForeground(TEXT_COLOR);
        return false;
    }
    
    private void playWinEffect(int[] winningLine, Color pieceColor) {
        if (winningLine == null) return;
        Color winCellBg = (pieceColor == X_COLOR) ? new Color(127, 29, 29) : new Color(20, 83, 45); // Dark Red or Emerald
        
        final int[] tick = {0};
        animationTimer = new Timer(150, e -> {
            if (tick[0] < 3) {
                // Sequential reveal
                int idx = winningLine[tick[0]];
                boardButtons[idx].setBackground(winCellBg);
                boardButtons[idx].setForeground(pieceColor);
                tick[0]++;
            } else {
                animationTimer.stop();
                pulseWin(winningLine, pieceColor, winCellBg);
            }
        });
        animationTimer.start();
    }
    
    private void pulseWin(int[] winningLine, Color brightColor, Color baseBgColor) {
        final int[] t = {0};
        animationTimer = new Timer(150, e -> {
            Color currentBg = (t[0] % 2 == 0) ? brightColor : baseBgColor;
            Color currentFg = (t[0] % 2 == 0) ? BG_COLOR : brightColor;
            for (int idx : winningLine) {
                boardButtons[idx].setBackground(currentBg);
                boardButtons[idx].setForeground(currentFg);
            }
            t[0]++;
            if (t[0] > 7) {
                animationTimer.stop();
                for (int idx : winningLine) {
                    boardButtons[idx].setBackground(brightColor);
                    boardButtons[idx].setForeground(BG_COLOR);
                }
            }
        });
        animationTimer.start();
    }

    private void playLoseEffect(int[] winningLine, Color pieceColor) {
        if (winningLine == null) return;
        
        // Dim the board instantly
        for (int i=0; i<9; i++) {
            boolean isWinCell = false;
            for (int w : winningLine) if (w == i) isWinCell = true;
            if (!isWinCell) {
                boardButtons[i].setForeground(new Color(100, 100, 100));
                boardButtons[i].setBackground(new Color(15, 23, 42)); // Fade to true bg
            } else {
                boardButtons[i].setBackground(pieceColor);
                boardButtons[i].setForeground(BG_COLOR);
            }
        }
        
        // Dramatic Screen Shake
        int origX = boardWrapper.getLocation().x;
        int origY = boardWrapper.getLocation().y;
        int[] offsets = {15, -15, 12, -12, 9, -9, 6, -6, 3, -3, 0};
        final int[] count = {0};
        
        animationTimer = new Timer(30, e -> {
            if (count[0] < offsets.length) {
                boardWrapper.setLocation(origX + offsets[count[0]], origY);
                count[0]++;
            } else {
                animationTimer.stop();
                boardWrapper.setLocation(origX, origY);
            }
        });
        animationTimer.start();
    }

    private void playDrawEffect() {
        Color dim = new Color(51, 65, 85);
        final int[] tick = {0};
        // Cascade fade to gray
        animationTimer = new Timer(80, e -> {
            if (tick[0] < 9) {
                boardButtons[tick[0]].setBackground(dim);
                boardButtons[tick[0]].setForeground(TEXT_COLOR);
                tick[0]++;
            } else {
                animationTimer.stop();
            }
        });
        animationTimer.start();
    }

    private void addHistoryRecord(String result) {
        String diff = (String) difficultyCombo.getSelectedItem();
        historyTableModel.addRow(new Object[]{gameNumber++, diff, result, movesThisGame});
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            // ignore
        }
        SwingUtilities.invokeLater(() -> {
            TicTacToeGUI gui = new TicTacToeGUI();
            gui.setVisible(true);
            gui.promptAndStartGame(false);
        });
    }
}
