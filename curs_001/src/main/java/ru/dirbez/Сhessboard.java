package ru.dirbez;

import java.util.ArrayList;
import java.util.List;

public class Сhessboard {

    private boolean[][] board;

    private int weight;

    private int height;

    private int lenght;

    private Knight knight;

    private PathNode pathNode;

    private Сhessboard() {
    }

    public Сhessboard(int weight, int height, Knight knight) {
        this.weight = weight;
        this.height = height;
        this.lenght = weight * height;
        this.knight = knight;
    }

    public long countPath(Point point) {
        long count = 0;
        this.board = new boolean[height][weight];
        this.pathNode = new PathNode(point, 1, 0, null);
        int childNum = 0;
        PathNode currentNode = pathNode;
        board[point.getY()][point.getX()] = true;
        currentNode.setChildren(this.findChildren(currentNode));
        while (true) {
            if (currentNode.getLevel() == 1 && currentNode.getChildNum() >= currentNode.getCountChildren()) {
                break;
            }
            currentNode.setChildren(this.findChildren(currentNode));
            if (currentNode.getChildNum() < currentNode.getCountChildren()) {
                currentNode.setChildNum();
                currentNode = currentNode.getChildren(currentNode.getChildNum() - 1);
                this.board[currentNode.getPoint().getY()][currentNode.getPoint().getX()] = true;
            } else {
                if (currentNode.getLevel() == this.lenght) {
                    count++;
                }
                while (currentNode.getChildNum() >= currentNode.getCountChildren() && currentNode.getLevel() != 1) {
                    this.board[currentNode.getPoint().getY()][currentNode.getPoint().getX()] = false;
                    currentNode = currentNode.getParent();
                }
            }
        }
        return count;
    }

    private ArrayList<PathNode> findChildren(PathNode parent) {
        List<Point> steps = knight.getSteps(parent.getPoint(), this.board);
        ArrayList<PathNode> result = new ArrayList<>(steps.size());
        for (Point point : steps) {
            result.add(new PathNode(point, parent.getLevel() + 1, 0, parent));
        }
        return result;
    }

}
