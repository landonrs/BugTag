package test;

import com.badlogic.gdx.math.GridPoint2;
import com.cs246team01.bugtag.GridObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Created by Landon on 2/22/2017.
 */

public class GridObjectUnitTest {



    @Test
    public void position_change() throws Exception {
        System.out.println("Position_Change");
        GridPoint2 expected = new GridPoint2(3, 4);

        GridObject go = new GridObject();
        go.setPosition(expected);

        assertEquals(expected, go.getPosition());
   }

        @Test
        public void priority_change() throws Exception {

            GridObject go = new GridObject();
            go.setPriority(12);
            assertEquals(12, go.getPriority());
        }

    }

