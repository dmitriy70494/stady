package ru.dirbez;

import java.util.ArrayList;
import java.util.List;

public class PathNode {
    private Point point;

    private int level;

    private int childNum;

    private PathNode parent;

    private List<PathNode> children;

    public PathNode(Point point, int level, int childNum, PathNode parent) {
        this.point = point;
        this.level = level;
        this.childNum = childNum;
        this.parent = parent;
    }

    public Point getPoint() {
        return this.point;
    }

    public int getLevel() {
        return level;
    }

    public int getChildNum() {
        return childNum;
    }

    public PathNode getParent() {
        return parent;
    }

    public PathNode getChildren(int childNum) {
        return children.get(childNum);
    }

    public void setChildren(List<PathNode> children) {
        this.children = children;
    }

    public int getCountChildren() {
        return this.children.size();
    }

    public void setChildNum() {
        this.childNum++;
    }
}
