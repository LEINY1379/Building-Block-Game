import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class CandyStackGame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Theme Colors (Default: Bright Candy)
    private Color bgColor = new Color(255, 154, 158);
    private Color cardBg = Color.WHITE;
    private Color textColor = new Color(74, 74, 74);
    private Color accentColor = new Color(255, 107, 129);
    private Color[] blockColors = {
        new Color(255, 107, 129),
        new Color(112, 161, 255),
        new Color(123, 237, 159),
        new Color(236, 204, 104)
    };

    // Game Settings & State
    private int highScore = 0;
    private double speedMultiplier = 1.0; // Easy = 0.7, Medium = 1.0, Hard = 1.5

    // Panels
    private GamePanel gamePanel;
    private JLabel leaderboardScoreLabel;

    public CandyStackGame() {
        setTitle("Candy Stack Builder");
        setSize(375, 667); // Mobile phone viewport dimensions
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Add Screens
        mainPanel.add(createHomeScreen(), "Home");
        gamePanel = new GamePanel();
        mainPanel.add(gamePanel, "Game");
        mainPanel.add(createLeaderboardScreen(), "Leaderboard");
        mainPanel.add(createSettingsScreen(), "Settings");

        add(mainPanel);
        setVisible(true);
    }

    // --- 1. HOME SCREEN ---
    private JPanel createHomeScreen() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(cardBg);

        JLabel titleLabel = new JLabel("🍬 Candy Stack");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setForeground(accentColor);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton startBtn = createStyledButton("Start Game");
        JButton leaderboardBtn = createStyledButton("Leaderboard");
        JButton settingsBtn = createStyledButton("Settings");

        startBtn.addActionListener(e -> {
            gamePanel.resetGame();
            cardLayout.show(mainPanel, "Game");
            gamePanel.requestFocusInWindow();
        });

        leaderboardBtn.addActionListener(e -> {
            leaderboardScoreLabel.setText("1. High Score: " + highScore);
            cardLayout.show(mainPanel, "Leaderboard");
        });

        settingsBtn.addActionListener(e -> cardLayout.show(mainPanel, "Settings"));

        panel.add(Box.createVerticalStrut(80));
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(50));
        panel.add(startBtn);
        panel.add(Box.createVerticalStrut(15));
        panel.add(leaderboardBtn);
        panel.add(Box.createVerticalStrut(15));
        panel.add(settingsBtn);

        return panel;
    }

    // --- 2. LEADERBOARD SCREEN ---
    private JPanel createLeaderboardScreen() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(cardBg);

        JLabel title = new JLabel("🏆 Leaderboard");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(accentColor);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        leaderboardScoreLabel = new JLabel("1. High Score: 0");
        leaderboardScoreLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        leaderboardScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton backBtn = createStyledButton("Back");
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "Home"));

        panel.add(Box.createVerticalStrut(50));
        panel.add(title);
        panel.add(Box.createVerticalStrut(40));
        panel.add(leaderboardScoreLabel);
        panel.add(Box.createVerticalStrut(100));
        panel.add(backBtn);

        return panel;
    }

    // --- 3. SETTINGS SCREEN ---
    private JPanel createSettingsScreen() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(cardBg);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("⚙️ Settings");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(accentColor);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Theme Selector
        JLabel themeLabel = new JLabel("Theme:");
        themeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        String[] themes = {"Bright Candy (Default)", "Dark Mode", "Pastel"};
        JComboBox<String> themeDropdown = new JComboBox<>(themes);
        themeDropdown.setMaximumSize(new Dimension(250, 30));
        themeDropdown.addActionListener(e -> applyTheme((String) themeDropdown.getSelectedItem()));

        // Difficulty Selector
        JLabel diffLabel = new JLabel("Difficulty:");
        diffLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        String[] difficulties = {"Easy", "Medium", "Hard"};
        JComboBox<String> diffDropdown = new JComboBox<>(difficulties);
        diffDropdown.setSelectedItem("Medium");
        diffDropdown.setMaximumSize(new Dimension(250, 30));
        diffDropdown.addActionListener(e -> {
            String selected = (String) diffDropdown.getSelectedItem();
            if ("Easy".equals(selected)) speedMultiplier = 0.7;
            else if ("Medium".equals(selected)) speedMultiplier = 1.0;
            else if ("Hard".equals(selected)) speedMultiplier = 1.6;
        });

        // Feedback Textarea
        JLabel feedbackLabel = new JLabel("Feedback:");
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextArea feedbackText = new JTextArea(3, 20);
        feedbackText.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(feedbackText);
        scrollPane.setMaximumSize(new Dimension(250, 80));

        JButton submitFeedbackBtn = createStyledButton("Submit Feedback");
        submitFeedbackBtn.addActionListener(e -> {
            if (!feedbackText.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Thank you for your feedback!", "Submitted", JOptionPane.INFORMATION_MESSAGE);
                feedbackText.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Please enter some text before submitting.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        JButton backBtn = createStyledButton("Back");
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "Home"));

        panel.add(title);
        panel.add(Box.createVerticalStrut(15));
        panel.add(themeLabel);
        panel.add(themeDropdown);
        panel.add(Box.createVerticalStrut(15));
        panel.add(diffLabel);
        panel.add(diffDropdown);
        panel.add(Box.createVerticalStrut(15));
        panel.add(feedbackLabel);
        panel.add(scrollPane);
        panel.add(Box.createVerticalStrut(10));
        panel.add(submitFeedbackBtn);
        panel.add(Box.createVerticalStrut(15));
        panel.add(backBtn);

        return panel;
    }

    private void applyTheme(String themeName) {
        if ("Dark Mode".equals(themeName)) {
            bgColor = new Color(30, 30, 46);
            cardBg = new Color(43, 43, 61);
            accentColor = new Color(189, 147, 249);
            blockColors = new Color[]{
                new Color(189, 147, 249),
                new Color(255, 121, 198),
                new Color(139, 233, 253),
                new Color(80, 250, 123)
            };
        } else if ("Pastel".equals(themeName)) {
            bgColor = new Color(252, 244, 221);
            cardBg = Color.WHITE;
            accentColor = new Color(180, 200, 220);
            blockColors = new Color[]{
                new Color(248, 180, 190),
                new Color(242, 198, 222),
                new Color(252, 225, 228),
                new Color(221, 237, 234)
            };
        } else { // Bright Candy (Default)
            bgColor = new Color(255, 154, 158);
            cardBg = Color.WHITE;
            accentColor = new Color(255, 107, 129);
            blockColors = new Color[]{
                new Color(255, 107, 129),
                new Color(112, 161, 255),
                new Color(123, 237, 159),
                new Color(236, 204, 104)
            };
        }
        repaint();
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(220, 40));
        button.setBackground(accentColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        return button;
    }

    // --- 4. GAME PANEL (GAMEPLAY CANVAS) ---
    private class GamePanel extends JPanel implements ActionListener {
        private Timer timer;
        private int currentScore = 0;
        private boolean isPlaying = false;

        private double blockX = 0;
        private int blockY = 0;
        private int blockWidth = 180;
        private int blockHeight = 25;
        private double blockSpeed = 4;
        private int direction = 1;

        private ArrayList<Block> stackedBlocks = new ArrayList<>();

        public GamePanel() {
            setBackground(bgColor);
            setFocusable(true);
            timer = new Timer(16, this); // ~60 FPS

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (isPlaying) {
                        dropBlock();
                    }
                }
            });
        }

        public void resetGame() {
            currentScore = 0;
            blockWidth = 180;
            stackedBlocks.clear();

            // Add base block
            stackedBlocks.add(new Block(80, getHeight() - 60, blockWidth, blockColors[0]));

            spawnNextBlock();
            isPlaying = true;
            timer.start();
        }

        private void spawnNextBlock() {
            Block last = stackedBlocks.get(stackedBlocks.size() - 1);
            blockX = 0;
            blockY = last.y - blockHeight;
            blockSpeed = (4.0 + (currentScore * 0.2)) * speedMultiplier;
            direction = 1;
        }

        private void dropBlock() {
            Block last = stackedBlocks.get(stackedBlocks.size() - 1);
            double diff = blockX - last.x;

            if (Math.abs(diff) >= blockWidth) {
                // Game Over
                isPlaying = false;
                timer.stop();
                JOptionPane.showMessageDialog(this, "Game Over!\nScore: " + currentScore, "Game Over", JOptionPane.INFORMATION_MESSAGE);
                cardLayout.show(mainPanel, "Home");
                return;
            }

            // Calculate overhang trimming
            int newWidth = blockWidth - (int) Math.abs(diff);
            int newX = (diff > 0) ? (int) blockX : last.x;

            blockWidth = newWidth;
            Color color = blockColors[stackedBlocks.size() % blockColors.length];
            stackedBlocks.add(new Block(newX, blockY, blockWidth, color));

            currentScore++;
            if (currentScore > highScore) {
                highScore = currentScore;
            }

            // Move camera down when tower builds up high
            if (blockY < getHeight() / 2) {
                for (Block b : stackedBlocks) {
                    b.y += blockHeight;
                }
            }

            spawnNextBlock();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isPlaying) {
                blockX += blockSpeed * direction;
                if (blockX + blockWidth >= getWidth() || blockX <= 0) {
                    direction *= -1; // Bounce
                }
                repaint();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw Background
            g2.setColor(bgColor);
            g2.fillRect(0, 0, getWidth(), getHeight());

            // Draw HUD (Top Scores)
            g2.setColor(textColor);
            g2.setFont(new Font("SansSerif", Font.BOLD, 16));
            g2.drawString("Score: " + currentScore, 20, 35);
            g2.drawString("High Score: " + highScore, getWidth() - 140, 35);

            // Draw Stacked Blocks
            for (Block b : stackedBlocks) {
                g2.setColor(b.color);
                g2.fillRect(b.x, b.y, b.width, blockHeight);
                g2.setColor(Color.WHITE);
                g2.drawRect(b.x, b.y, b.width, blockHeight);
            }

            // Draw Moving Block
            if (isPlaying) {
                Color color = blockColors[stackedBlocks.size() % blockColors.length];
                g2.setColor(color);
                g2.fillRect((int) blockX, blockY, blockWidth, blockHeight);
                g2.setColor(Color.WHITE);
                g2.drawRect((int) blockX, blockY, blockWidth, blockHeight);
            }
        }
    }

    // Helper class for block attributes
    private static class Block {
        int x, y, width;
        Color color;

        Block(int x, int y, int width, Color color) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.color = color;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CandyStackGame::new);
    }
}