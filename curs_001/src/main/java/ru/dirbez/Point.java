package ru.dirbez;

public class Point {
    private int x;

    private int y;

    private int stepCount = 8;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(int x, int y, int stepCount) {
        this.x = x;
        this.y = y;
        this.stepCount = stepCount;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }
}
