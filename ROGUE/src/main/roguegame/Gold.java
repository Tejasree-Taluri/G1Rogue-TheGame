package roguegame;

class Gold extends Item {
    private int amount;

    public Gold(String name, int x, int y, int amount) {
        super(name, x, y); // Call the superclass constructor
        this.amount = amount;
    }

    @Override
    public void use(Player player) {
        player.addGold(amount);
    }
}
