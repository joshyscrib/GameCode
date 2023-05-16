import static org.junit.Assert.assertEquals;

import org.junit.Test;

import junit.*;

public class TestGuard {
 
    @Test
    public void testCreateGuard(){
        Guard g = new Guard(10, 10);
        assertEquals("hp test", 50, g.hp);
    }

    @Test
    public void testCreatePrincess(){
        Princess g = new Princess(10, 10);
        assertEquals("hp test", 200, g.hp);
        // princess got hit from left, should lose 100 hp, and be moved
        g.gotHit(100, Direction.Left);
        assertEquals("hp test 2", 100, g.hp);
        assertEquals("princess y should not change", 0, g.nextKnockbackY);
        assertEquals("princess x should have changed", -5, g.nextKnockbackX);

    }
}
