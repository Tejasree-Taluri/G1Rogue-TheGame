package main.roguegame;

public class Player {
    private int x;
    private int y;
    private int health;
    private int level;
    private int gold;
    private int armor;
    private int food;
    private int experience;
    private int damage;

    public Player(int x, int y, int health, int gold, int armor) {
        this.x = x;
        this.y = y;
        this.health = health;
        this.level = 1;  // Starting level
        this.gold = gold;
        this.armor = armor;
        this.food = 0; // Starting food
        this.experience = 0;
        this.damage = 10; // Base damage
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

    public int getExperience() {
        return experience;
    }

    public int getDamage() {
        return damage;
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

    public void takeDamage(int damage) {
        this.health -= damage;
    }

    public void addExperience(int experience) {
        this.experience += experience;
    }

    public void levelUp() {
        this.level++;
        this.health += 20;
        this.damage += 5;
        this.experience = 0;
    }

    public boolean isDead() {
        return health <= 0;
    }
}
