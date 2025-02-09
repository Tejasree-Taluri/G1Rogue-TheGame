import main.roguegame.GameMap;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class GameMapTest {
    private GameMap map;

    @Before
    public void setUp() {
        map = new GameMap(20, 15);  // Map with width 20 and height 15
    }

    @Test
    public void testGenerateMap() {
        map.generateMap();
        assertTrue(map.getWidth() > 0);
        assertTrue(map.getHeight() > 0);
        assertNotNull(map.getStairsX());
        assertNotNull(map.getStairsY());
    }

}
