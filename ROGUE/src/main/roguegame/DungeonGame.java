package roguegame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DungeonGame extends JFrame implements KeyListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int TILE_SIZE = 32;
    private static final int FLOORS = 5;

    private Player player;
    private GameMap[] floors;
    private int currentFloor;
    private JPanel gamePanel;
    private JLabel statsLabel;
    private List<Monster> monsters;

    public DungeonGame() {
        setTitle("Rogue-like Dungeon Game");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Initialize game
        player = new Player(1, 1, 100, 0, 5); // Starting with 100 health, 0 gold, 5 armor
        floors = new GameMap[FLOORS];
        for (int i = 0; i < FLOORS; i++) {
            floors[i] = new GameMap(20, 15); // 20x15 grid
        }
        currentFloor = 0;
        monsters = new ArrayList<>();
        spawnMonsters();

        // Setup GUI
        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGame(g);
            }
        };
        gamePanel.setPreferredSize(new Dimension(WIDTH, HEIGHT - 50));
        gamePanel.setBackground(Color.BLACK);

        statsLabel = new JLabel();
        statsLabel.setPreferredSize(new Dimension(WIDTH, 50));
        statsLabel.setForeground(Color.WHITE);
        statsLabel.setFont(new Font("Arial", Font.BOLD, 16));

        add(gamePanel, BorderLayout.CENTER);
        add(statsLabel, BorderLayout.SOUTH);

        addKeyListener(this);
        setFocusable(true);
        updateStats();
    }

    private void drawGame(Graphics g) {
        GameMap currentMap = floors[currentFloor];
        for (int y = 0; y < currentMap.getHeight(); y++) {
            for (int x = 0; x < currentMap.getWidth(); x++) {
                char tile = currentMap.getTile(x, y);
                if (x == player.getX() && y == player.getY()) {
                    g.setColor(Color.YELLOW); // Player color
                    g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                } else if (tile == '#') {
                    g.setColor(Color.GRAY); // Wall color
                    g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                } else if (tile == '.') {
                    g.setColor(Color.DARK_GRAY); // Floor color
                    g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                } else if (tile == '>') {
                    g.setColor(Color.RED); // Stairs color
                    g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
        }

        // Draw monsters
        for (Monster monster : monsters) {
            if (monster.getFloor() == currentFloor) {
                g.setColor(Color.GREEN); // Monster color
                g.fillRect(monster.getX() * TILE_SIZE, monster.getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    private void updateStats() {
        statsLabel.setText("Health: " + player.getHealth() + " | Level: " + player.getLevel() + " | Gold: " + player.getGold() + " | Armor: " + player.getArmor() + " | Food: " + player.getFood());
    }

    private void spawnMonsters() {
        Random random = new Random();
        GameMap currentMap = floors[currentFloor];
        for (int i = 0; i < 5; i++) { // Spawn 5 monsters per floor
            int x, y;
            do {
                x = random.nextInt(currentMap.getWidth());
                y = random.nextInt(currentMap.getHeight());
            } while (currentMap.getTile(x, y) != '.' || (x == player.getX() && y == player.getY()));

            monsters.add(new Monster(x, y, currentFloor, 20, 5)); // Monster with 20 health and 5 damage
        }
    }

    private void combat(Monster monster) {
        // Player attacks monster
        monster.takeDamage(player.getDamage());
        if (monster.isDead()) {
            monsters.remove(monster);
            player.addExperience(monster.getExperience());
            if (player.getExperience() >= 100) {
                player.levelUp();
            }
        } else {
            // Monster attacks player
            player.takeDamage(monster.getDamage());
            if (player.isDead()) {
                JOptionPane.showMessageDialog(this, "You have been defeated!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        }
        updateStats();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        int dx = 0, dy = 0;

        switch (key) {
            case KeyEvent.VK_W -> dy = -1;
            case KeyEvent.VK_S -> dy = 1;
            case KeyEvent.VK_A -> dx = -1;
            case KeyEvent.VK_D -> dx = 1;
        }

        GameMap currentMap = floors[currentFloor];
        int newX = player.getX() + dx;
        int newY = player.getY() + dy;

        // Check if the new position is within bounds
        if (newX >= 0 && newX < currentMap.getWidth() && newY >= 0 && newY < currentMap.getHeight()) {
            char tile = currentMap.getTile(newX, newY);

            // If the tile is not a wall, move the player
            if (tile != '#') {
                // Check for monsters
                Monster monster = getMonsterAt(newX, newY);
                if (monster != null) {
                    combat(monster);
                } else {
                    player.move(dx, dy);

                    // If the player reaches the stairs, move to the next floor
                    if (tile == '>' && currentFloor < FLOORS - 1) {
                        currentFloor++; // Move to the next floor
                        player.setPosition(1, 1); // Reset player position to the start of the new floor
                        spawnMonsters();
                    }
                }
            }
        }

        updateStats(); // Update player stats
        gamePanel.repaint(); // Redraw the game panel
    }

    private Monster getMonsterAt(int x, int y) {
        for (Monster monster : monsters) {
            if (monster.getX() == x && monster.getY() == y && monster.getFloor() == currentFloor) {
                return monster;
            }
        }
        return null;
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DungeonGame game = new DungeonGame();
            game.setVisible(true);
        });
    }
}
