package roguegame;

import java.util.ArrayList;
import java.util.List;

class Player {
    private int x;
    private int y;
    private int health;
    private int level;
    private int gold;
    private int armor;
    private int food;
    private int experience;
    private int damage;
    private int hunger;
    private List<Item> inventory;
    private int maxInventorySize;

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
        this.hunger = 100; // Starting hunger level
        this.inventory = new ArrayList<>();
        this.maxInventorySize = 10; // Max inventory size
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

    public int getHunger() {
        return hunger;
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public void move(int dx, int dy) {
        x += dx;
        y += dy;
    }

    public void addFood(int amount) {
        this.food += amount;
    }

    public void addGold(int amount) {
        this.gold += amount;
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

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public void decreaseHunger(int amount) {
        this.hunger -= amount;
        if (this.hunger <= 0) {
            this.health -= 10; // Starvation damage
            this.hunger = 0;
        }
    }

    public void consumeFood() {
        if (this.food > 0) {
            this.food--;
            this.hunger += 20;
            if (this.hunger > 100) this.hunger = 100;
        }
    }

    public boolean isDead() {
        return health <= 0;
    }

    public boolean addItemToInventory(Item item) {
        if (inventory.size() < maxInventorySize) {
            inventory.add(item);
            return true;
        }
        return false; // Inventory is full
    }

    public void dropItem(Item item) {
        inventory.remove(item);
    }
}
