package roguegame;

class Weapon extends Item {
    private int damage;

    public Weapon(String name, int x, int y, int damage) {
        super(name, x, y); // Call the superclass constructor
        this.damage = damage;
    }

    @Override
    public void use(Player player) {
        player.setDamage(damage);
    }
}
