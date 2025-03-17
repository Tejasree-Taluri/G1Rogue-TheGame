package roguegame;

import javax.swing.*;

class Trap extends Item {
    private int damage;

    public Trap(String name, int x, int y, int damage) {
        super(name, x, y); // Call the superclass constructor
        this.damage = damage;
    }

    @Override
    public void use(Player player) {
        player.takeDamage(damage);
        JOptionPane.showMessageDialog(null, "You stepped on a trap and took " + damage + " damage!", "Trap", JOptionPane.WARNING_MESSAGE);
    }
}
