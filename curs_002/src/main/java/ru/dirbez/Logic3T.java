package ru.dirbez;

/**
 * Проверка пустых клеток + проверки на победителя.
 * @author Dmitriy Balandin (d89086362742@yandex.ru)
 * @version $Id$
 * @since 25.04.2018
 */

public class Logic3T {
    private final Figure3T[][] table;
    private boolean[][] cellX = new boolean[3][3];
    private boolean[][] cellO = new boolean[3][3];

    /**
     * конструктор передает в класс готовую таблицу
     * @param table таблица с данными по клеткам
     */
    public Logic3T(Figure3T[][] table) {
        this.table = table;
    }

    /**
     * Проверяет наличие победителя среди ноликов или крестиков
     * сначала проверка опорной клетки, если она заполнена,
     * проверяет соседние клетки на наличие победителя.
     * Всего 3 опорные клетки [0][0], [1][1], [2][2]
     * @param cell таблица клеток крестиков или ноликов
     * @return
     */
    public boolean isWinner(boolean[][] cell) {
        boolean winner = false;
        if (cell[1][1]) {
            winner = cell[0][1] && cell[2][1] || cell[1][0] && cell[1][2] || cell[0][0] && cell[2][2] || cell[2][0] && cell[0][2];
        }
        if (cell[0][0] && !winner) {
            winner = cell[0][1] && cell[0][2] || cell[1][0] && cell[2][0];
        }
        if (cell[2][2] && !winner) {
            winner = cell[2][0] && cell[2][1] || cell[0][2] && cell[0][1];
        }
        return winner;
    }

    /**
     * Создает двумерные массивы ходов + отправляет массив крестиков на проверку
     * @return boolean true - есть победитель среди Х.
     */
    public boolean isWinnerX() {
        for (int line = 0; line < 3; line++) {
            for (int cell = 0; cell < 3; cell++) {
                this.cellX[line][cell] = this.table[line][cell].hasMarkX();
                this.cellO[line][cell] = this.table[line][cell].hasMarkO();
            }
        }
        return this.isWinner(this.cellX);
    }

    /**
     * Oтправляет массив ноликов на проверку
     *
     * @return boolean true - есть победитель среди O.
     */
    public boolean isWinnerO() {
        return this.isWinner(this.cellO);
    }

    /**
     * поиск незанятых ячеек, если не находит, выводится диалоговое окно с предложением начать
     * игру заново
     * @return boolean true - если есть свободные ячейки для хода.
     */
    public boolean hasGap() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!table[i][j].hasMarkO() && !table[i][j].hasMarkX()) {
                    return true;
                }
            }
        }
        return false;
    }
}