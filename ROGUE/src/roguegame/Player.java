package roguegame;

public class Player {
    private int x;
    private int y;
    private int health;
    private int level;
    private int gold;
    private int armor;
    private int food; // New attribute to track food

    public Player(int x, int y, int health, int gold, int armor) {
        this.x = x;
        this.y = y;
        this.health = health;
        this.level = 1;  // Starting level
        this.gold = gold;
        this.armor = armor;
        this.food = 0; // Starting food
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHealth() {
        return health;
    }

    public int getLevel() {
        return level;
    }

    public int getGold() {
        return gold;
    }

    public int getArmor() {
        return armor;
    }

    public int getFood() {
        return food;
    }

    public void move(int dx, int dy) {
        x += dx;
        y += dy;
    }

    public void addFood(int amount) {
        this.food += amount;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
