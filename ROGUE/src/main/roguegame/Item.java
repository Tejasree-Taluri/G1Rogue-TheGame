package roguegame;

abstract class Item {
    private String name;
    private int x, y;
    private int floor;

    public Item(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.floor = 0; // Default floor
    }

    public String getName() {
        return name;
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

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public abstract void use(Player player);
}
