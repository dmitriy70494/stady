package ru.dirbez;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class KnightTest {

    Knight knight;

    @Before
    public void beforeTest() {
        knight = new Knight();
    }

    @Test
    public void whenBoard8x8Knight1a1() {
        List<Point> result = this.knight.getSteps(new Point(1,1), new boolean[8][8]);
        assertThat(result.size(), is(2));
    }

    @Test
    public void whenBoard8x8Knight4a4() {
        List<Point> result = this.knight.getSteps(new Point(4,4), new boolean[8][8]);
        assertThat(result.size(), is(4));
    }
}
