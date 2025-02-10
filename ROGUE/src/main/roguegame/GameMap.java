package main.roguegame;
//test
import java.util.Random;

public class GameMap {
    private int width;
    private int height;
    private char[][] map;
    private int stairsX, stairsY; // Stairs position

    public GameMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.map = new char[height][width];
        generateMap();
    }

    public void generateMap() {
        Random random = new Random();
        // Fill the map with walls
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                map[y][x] = '#'; // Walls by default
            }
        }

        // Create rooms and corridors
        int numRooms = random.nextInt(5) + 5; // 5 to 9 rooms
        for (int i = 0; i < numRooms; i++) {
            int roomWidth = random.nextInt(6) + 4; // Room width between 4 and 9
            int roomHeight = random.nextInt(6) + 4; // Room height between 4 and 9
            int roomX = random.nextInt(width - roomWidth - 1) + 1;
            int roomY = random.nextInt(height - roomHeight - 1) + 1;

            // Carve out the room
            for (int y = roomY; y < roomY + roomHeight; y++) {
                for (int x = roomX; x < roomX + roomWidth; x++) {
                    map[y][x] = '.'; // Floor tiles
                }
            }

            // Connect rooms with corridors
            if (i > 0) {
                int prevRoomCenterX = stairsX; // Use stairs position as previous room center
                int prevRoomCenterY = stairsY;
                int newRoomCenterX = roomX + roomWidth / 2;
                int newRoomCenterY = roomY + roomHeight / 2;

                // Horizontal corridor
                for (int x = Math.min(prevRoomCenterX, newRoomCenterX); x <= Math.max(prevRoomCenterX, newRoomCenterX); x++) {
                    map[prevRoomCenterY][x] = '.'; // Horizontal path
                }
                // Vertical corridor
                for (int y = Math.min(prevRoomCenterY, newRoomCenterY); y <= Math.max(prevRoomCenterY, newRoomCenterY); y++) {
                    map[y][newRoomCenterX] = '.'; // Vertical path
                }
            }

            // Place stairs in the last room
            if (i == numRooms - 1) {
                stairsX = roomX + roomWidth / 2;
                stairsY = roomY + roomHeight / 2;
                map[stairsY][stairsX] = '>'; // Stairs tile
            }
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

    public int getStairsX() {
        return stairsX;
    }

    public int getStairsY() {
        return stairsY;
    }
}
