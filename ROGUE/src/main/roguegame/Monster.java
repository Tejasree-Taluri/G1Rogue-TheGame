package main.roguegame;

class Monster {
    private int x;
    private int y;
    private int floor;
    private int health;
    private int damage;
    private int experience;

    public Monster(int x, int y, int floor, int health, int damage) {
        this.x = x;
        this.y = y;
        this.floor = floor;
        this.health = health;
        this.damage = damage;
        this.experience = 20; // Experience points awarded when defeated
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getFloor() {
        return floor;
    }

    public int getHealth() {
        return health;
    }

    public int getDamage() {
        return damage;
    }

    public int getExperience() {
        return experience;
    }

    public void takeDamage(int damage) {
        this.health -= damage;
    }

    public boolean isDead() {
        return health <= 0;
    }
}
