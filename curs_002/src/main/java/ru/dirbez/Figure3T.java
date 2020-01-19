package ru.dirbez;

import javafx.scene.shape.Rectangle;

/**
 * Класс содержит информацию о клетке, что она содержит, крестик или нолик
 * @author Dmitriy Balandin (d89086362742@yandex.ru)
 * @version $Id$
 * @since 25.04.2018
 */
public class Figure3T extends Rectangle {
    private boolean markX = false;
    private boolean markO = false;

    public Figure3T() {
        markX = false;
        markO = false;
    }

    public Figure3T(boolean markX) {
        this.markX = markX;
        this.markO = !markX;
    }

    public void take(boolean markX) {
        this.markX = markX;
        this.markO = !markX;
    }

    public boolean hasMarkX() {
        return this.markX;
    }

    public boolean hasMarkO() {
        return this.markO;
    }

    public String toString() {
        return markX + " " + markO;
    }
}