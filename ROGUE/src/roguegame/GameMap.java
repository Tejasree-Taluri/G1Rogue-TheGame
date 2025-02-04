package roguegame;

import java.util.Random;

public class GameMap {
    private int width;
    private int height;
    private char[][] map;
    private boolean hasFood;

    public GameMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.map = new char[height][width];
        generateMap();
    }

    private void generateMap() {
        Random random = new Random();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x == 0 || x == width - 1 || y == 0 || y == height - 1) {
                    map[y][x] = '#'; // Walls
                } else {
                    map[y][x] = random.nextDouble() < 0.7 ? '.' : '#'; // Random floor or wall
                }
            }
        }
        // Add stairs
        map[height - 2][width - 2] = '>';

        // Add food
        if (random.nextDouble() < 0.1) { // 10% chance of food
            int foodX = random.nextInt(width - 2) + 1;
            int foodY = random.nextInt(height - 2) + 1;
            map[foodY][foodX] = 'a'; // Food item (a)
            hasFood = true;
        }
    }

    public char getTile(int x, int y) {
        return map[y][x];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean hasFood() {
        return hasFood;
    }

    public void resetFood() {
        hasFood = false;
    }
}
