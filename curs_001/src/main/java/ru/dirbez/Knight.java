package ru.dirbez;

import java.util.LinkedList;
import java.util.List;

public class Knight {

    private int min;

    private int[]x = {1, 1, 2, 2, -1, -1, -2, -2};

    private int[]y = {2, -2, 1, -1, 2, -2, 1, -1};

    public List<Point> getSteps(Point point, boolean[][] board) {
        this.min = 8;
        int weight = board[0].length;
        int height = board.length;
        List<Point> steps = new LinkedList<>();
        for (int i = 0; i < 8; i++) {
            int x = point.getX() + this.x[i];
            int y = point.getY() + this.y[i];
            if (x >= 0 && x < weight && y >= 0 && y < height && !board[y][x]) {
                steps.add(new Point(x, y, stepCount(x, y, height, weight, board)));
            }
        }
        steps.removeIf(a -> a.getStepCount() != min);
        return steps;
    }

    private int stepCount(int sx, int sy, int height, int weight, boolean[][] board) {
        int min = 0;
        for (int i = 0; i < 8; i++) {
            int x = sx + this.x[i];
            int y = sy + this.y[i];
            if (x >= 0 && x < weight && y >= 0 && y < height && !board[y][x]) {
                min++;
            }
        }
        if (this.min > min) {
            this.min = min;
        }
        return min;
    }
}
