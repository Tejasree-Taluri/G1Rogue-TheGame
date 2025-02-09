import main.roguegame.Player;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PlayerTest {
    private Player player;

    @Before
    public void setUp() {
        // Initialize the player before each test
        player = new Player(1, 1, 100, 0, 5);  // Player with initial position (1, 1), 100 health, 0 gold, 5 armor
    }

    @Test
    public void testGetX() {
        assertEquals(1, player.getX());
    }

    @Test
    public void testGetY() {
        assertEquals(1, player.getY());
    }

}
