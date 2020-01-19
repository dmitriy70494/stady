package ru.dirbez;



public class Main{

    private int weight;

    private int height;

    public Main (int weight, int height) {
        this.weight = weight;
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public int getHeight() {
        return height;
    }

    public static void main(String[] args) {
        long total = 0;
        Main main = new Main(8, 8);
        Сhessboard chessboard = new Сhessboard(main.getWeight(), main.getHeight(), new Knight());
        for (int y = 0; y < main.getHeight(); y++) {
            for (int x = 0; x < main.getHeight(); x++) {
                total += chessboard.countPath(new Point(x, y));
                System.out.println(total);
            }
        }
    }
}