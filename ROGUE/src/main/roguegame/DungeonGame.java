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
    private List<Item> items;
    private List<Trap> traps;
    private List<Quest> quests;
    private Timer hungerTimer;
    private Timer dayNightTimer;
    private boolean isDay = true;

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
        items = new ArrayList<>();
        traps = new ArrayList<>();
        quests = new ArrayList<>();
        spawnMonsters();
        spawnItems();
        spawnTraps();
        generateQuests();

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

        // Hunger timer
        hungerTimer = new Timer(10000, e -> { // Decrease hunger every 10 seconds
            player.decreaseHunger(10);
            updateStats();
        });
        hungerTimer.start();

        // Day-night timer
        dayNightTimer = new Timer(60000, e -> { // Toggle day/night every 60 seconds
            isDay = !isDay;
            updateStats();
        });
        dayNightTimer.start();
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
                g.setColor(isDay ? Color.GREEN : Color.RED); // Monster color (green during day, red at night)
                g.fillRect(monster.getX() * TILE_SIZE, monster.getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }

        // Draw items
        for (Item item : items) {
            if (item.getFloor() == currentFloor) {
                g.setColor(Color.BLUE); // Item color
                g.fillRect(item.getX() * TILE_SIZE, item.getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }

        // Draw traps
        for (Trap trap : traps) {
            if (trap.getFloor() == currentFloor) {
                g.setColor(Color.ORANGE); // Trap color
                g.fillRect(trap.getX() * TILE_SIZE, trap.getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    private void updateStats() {
        String timeOfDay = isDay ? "Day" : "Night";
        statsLabel.setText("Health: " + player.getHealth() + " | Level: " + player.getLevel() + " | Gold: " + player.getGold() + " | Armor: " + player.getArmor() + " | Food: " + player.getFood() + " | Hunger: " + player.getHunger() + " | Time: " + timeOfDay);
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

            monsters.add(new Monster(x, y, currentFloor, isDay ? 20 : 30, isDay ? 5 : 10)); // Stronger at night
        }
    }

    private void spawnItems() {
        Random random = new Random();
        GameMap currentMap = floors[currentFloor];
        for (int i = 0; i < 5; i++) { // Spawn 5 items per floor
            int x, y;
            do {
                x = random.nextInt(currentMap.getWidth());
                y = random.nextInt(currentMap.getHeight());
            } while (currentMap.getTile(x, y) != '.' || (x == player.getX() && y == player.getY()));

            // Randomly choose an item type
            int itemType = random.nextInt(4);
            Item item = switch (itemType) {
                case 0 -> new Weapon("Sword", x, y, 10);
                case 1 -> new Armor("Shield", x, y, 5);
                case 2 -> new Food("Bread", x, y, 20);
                case 3 -> new Gold("Gold Coin", x, y, 50);
                default -> throw new IllegalStateException("Unexpected value: " + itemType);
            };
            item.setFloor(currentFloor); // Set the floor for the item
            items.add(item);
        }
    }

    private void spawnTraps() {
        Random random = new Random();
        GameMap currentMap = floors[currentFloor];
        for (int i = 0; i < 3; i++) { // Spawn 3 traps per floor
            int x, y;
            do {
                x = random.nextInt(currentMap.getWidth());
                y = random.nextInt(currentMap.getHeight());
            } while (currentMap.getTile(x, y) != '.' || (x == player.getX() && y == player.getY()));

            Trap trap = new Trap("Spike Trap", x, y, 10);
            trap.setFloor(currentFloor); // Set the floor for the trap
            traps.add(trap);
        }
    }

    private void generateQuests() {
        quests.add(new Quest("Kill 5 monsters"));
        quests.add(new Quest("Find the golden sword"));
    }

    private void combat(Monster monster) {
        Random random = new Random();
        // Player attacks monster
        if (random.nextInt(10) < 2) { // 20% chance to miss
            JOptionPane.showMessageDialog(this, "You missed the monster!", "Combat", JOptionPane.INFORMATION_MESSAGE);
        } else {
            int damage = player.getDamage();
            if (random.nextInt(10) < 1) { // 10% chance for critical hit
                damage *= 2;
                JOptionPane.showMessageDialog(this, "Critical hit!", "Combat", JOptionPane.INFORMATION_MESSAGE);
            }
            monster.takeDamage(damage);
            if (monster.isDead()) {
                monsters.remove(monster);
                player.addExperience(monster.getExperience());
                if (player.getExperience() >= 100) {
                    player.levelUp();
                }
            } else {
                // Monster attacks player (reduce damage by armor)
                int monsterDamage = Math.max(0, monster.getDamage() - player.getArmor());
                player.takeDamage(monsterDamage);
                if (player.isDead()) {
                    JOptionPane.showMessageDialog(this, "You have been defeated!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                }
            }
        }
        updateStats();
    }

    private void pickUpItem(int x, int y) {
        for (Item item : items) {
            if (item.getX() == x && item.getY() == y && item.getFloor() == currentFloor) {
                if (player.addItemToInventory(item)) {
                    items.remove(item);
                    JOptionPane.showMessageDialog(this, "Picked up: " + item.getName(), "Item Collected", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Inventory is full!", "Inventory Full", JOptionPane.WARNING_MESSAGE);
                }
                break;
            }
        }
    }

    private void triggerTrap(int x, int y) {
        for (Trap trap : traps) {
            if (trap.getX() == x && trap.getY() == y && trap.getFloor() == currentFloor) {
                trap.use(player);
                traps.remove(trap);
                break;
            }
        }
    }

    private void resetGamePanel() {
        gamePanel.repaint(); // Redraw the game panel
        updateStats(); // Update the stats label
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
            case KeyEvent.VK_E -> player.consumeFood(); // Consume food
        }

        GameMap currentMap = floors[currentFloor];
        int newX = player.getX() + dx;
        int newY = player.getY() + dy;

        // Check if the new position is within bounds
        if (newX >= 0 && newX < currentMap.getWidth() && newY >= 0 && newY < currentMap.getHeight()) {
            char tile = currentMap.getTile(newX, newY);

            // If the tile is not a wall, move the player
            if (tile != '#') {
                // Check for traps
                triggerTrap(newX, newY);

                // Check for monsters
                Monster monster = getMonsterAt(newX, newY);
                if (monster != null) {
                    combat(monster);
                } else {
                    player.move(dx, dy);

                    // Check for items
                    pickUpItem(newX, newY);

                    // If the player reaches the stairs, move to the next floor
                    if (tile == '>' && currentFloor < FLOORS - 1) {
                        currentFloor++; // Move to the next floor
                        player.setPosition(1, 1); // Reset player position to the start of the new floor
                        spawnMonsters();
                        spawnItems();
                        spawnTraps();
                        resetGamePanel(); // Refresh the game panel
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
