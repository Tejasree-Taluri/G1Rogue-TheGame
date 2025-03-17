package roguegame;

class Food extends Item {
    private int nutrition;

    public Food(String name, int x, int y, int nutrition) {
        super(name, x, y); // Call the superclass constructor
        this.nutrition = nutrition;
    }

    @Override
    public void use(Player player) {
        player.addFood(nutrition);
    }
}
