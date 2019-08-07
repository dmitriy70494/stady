package ru.dirbez.veroiatnost;


import java.util.ArrayList;

public class Main {

    private ArrayList<Integer> maxChain = new ArrayList<>();
    private ArrayList<int[]> countChain = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> dataAll = new ArrayList<>();

    public static void main(String[] args) {
        Main main = new Main();
        int minim = 100;
        int maxim = 0;
        for (int i = 0; i < 10; i++) {
            main.dataAll.add(main.createRandomData(100000, 0.538235294, false));
            main.countChain.add(main.countChainAll(main.dataAll.get(i), main.maxChain.get(i)));
        }
        for (int[] index : main.countChain) {
            for (int ind : index) {
                System.out.print(ind + " : ");
            }
            System.out.println();
        }
        System.out.println(main.maxChain);
        main.distanceChain();
    }

    private int[] countChainAll(ArrayList<Integer> data, int maxChain) {
        int[] index = new int[maxChain + 1];
        int chain = 0;
        for (Integer res : data) {
            if (res == 0) {
                if (chain != 0) {
                    index[chain]++;
                    chain = 0;
                }
                index[0]++;
            } else {
                chain = res;
            }
        }
        return index;
    }

    private int[] countOneZeros(ArrayList<Integer> data, int maxChain) {
        int [] oneZeros = new int[maxChain];
        for (Integer inter : data) {
            oneZeros[inter]++;
        }
        return oneZeros;
    }

    public void distanceChain() {
        for (ArrayList<Integer> list : this.dataAll) {
            boolean twoZero = false;
            boolean chain = false;
            int twoZeros = 0;
            ArrayList<Integer> lengthTwoChain = new ArrayList<>();
            for (int i = 0; i < list.size()-1; i++) {
                if (twoZero && list.get(i) == 0) {
                    twoZeros++;
                    twoZero = false;
                } else if(twoZero && list.get(i) != 0) {
                    while(true) {
                        if (list.get(++i) == 0) {
                            lengthTwoChain.add(list.get(i - 1));
                            break;
                        }
                    }
                    twoZero = false;
                } else if (chain && list.get(i) == 0) {
                    if (list.get(i-1) > 11) {
                        twoZero = true;
                    }
                    chain = false;
                } else if (!chain && list.get(i) != 0) {
                    chain = true;
                }
            }
            System.out.println("count TwoZeros = " + twoZeros);
            System.out.println("chainLength = " + lengthTwoChain);
            System.out.println(lengthTwoChain.size());
            for (int ind : this.countOneZeros(lengthTwoChain, 20)) {
                System.out.print(ind + " : ");
            }
            System.out.println();
        }
    }

    private ArrayList<Integer> createRandomData(int size, double veroiatnost, boolean sleep) {
        ArrayList<Integer> list = new ArrayList<>(size);
        int count = 0;
        int max = 0;
        for (int i = 0; i < size; i++) {
            if (Math.random() > veroiatnost) {
                list.add(++count);
                if (max < count) {
                    max = count;
                }
            } else {
                count = 0;
                list.add(count);
            }
            if (sleep) {
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        maxChain.add(max);
        return list;
    }

}
