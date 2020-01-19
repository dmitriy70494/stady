package ru.dirbez;

import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ChessboardTest {

    Сhessboard chessboard3x3;
    Сhessboard chessboard5x5;

    @Before
    public void beforeTest() {
        this.chessboard3x3 = new Сhessboard(3,3, new Knight());
        this.chessboard5x5 = new Сhessboard(5,5, new Knight());
    }

    @Test
    public void whenBoard3x3CountPath() {
        long result = this.chessboard3x3.countPath(new Point(1,1));
        assertThat(result, is(0L));
    }

    @Test
    public void whenBoard4x4CountPath() {
        long result = this.chessboard5x5.countPath(new Point(1,1));
        assertThat(result, is(16L));
    }
}
