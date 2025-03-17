package roguegame;

class Armor extends Item {
    private int defense;

    public Armor(String name, int x, int y, int defense) {
        super(name, x, y); // Call the superclass constructor
        this.defense = defense;
    }

    @Override
    public void use(Player player) {
        player.setArmor(defense);
    }
}
