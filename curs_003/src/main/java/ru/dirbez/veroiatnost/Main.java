package ru.dirbez.veroiatnost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    private ArrayList<Integer> maxChain = new ArrayList<>();
    private ArrayList<int[]> countChain = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> dataAll = new ArrayList<>();
    private int countBlackLine = 0;
    private int maxBlackLine = 0;
    private int maxLengthBlackLine = 0;

    public static void main(String[] args) {
        Main main = new Main();
//        main.experimentOne();
        int minim = 100;
        int maxim = 0;
        for (int i = 0; i < 1; i++) {
            main.dataAll.add(main.createRandomData(10000000, 0.45
                    , false));
            main.countChain.add(main.countChainAll(main.dataAll.get(i), main.maxChain.get(i)));
            main.distanceOneZeroIncludingAndCountChain(main.dataAll.get(i));
        }
//        main.experimentOne(0.92, 10000);
//        System.out.println(main.shlopData(main.dataAll.get(0)));
//        System.out.println(main.dataAll.get(0));
//        main.delimiterNumber(main.dataAll.get(0), 7);
//        for (int[] index : main.countChain) {
//            for (int ind : index) {
//                System.out.print(ind + " : ");
//            }
//            System.out.println();
//        }
//        System.out.println(main.maxChain);
//        main.distanceChain();

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

    /**
     * создает список случайных чисел
     * @param size размер списка
     * @param veroiatnost вероятность победы
     * @param sleep нужно ли усыплять систему
     * @return список с данными
     */
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

    /**
     * наблюдает есть ли в последовательности 2 нуля
     * @param data
     */
    private void distanceOneZeroIncludingAndCountChain(ArrayList<Integer> data) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < data.size() - 1; i++) {
            if (data.get(i) != 0) {
                while (true) {
                    if (i + 1 > data.size()-1 || i + 2 > data.size() - 1) {
                        break;
                    }
                    if (data.get(++i) == 0) {
                        list.add(data.get(i - 1));
                        list.add(0);
                        if (data.get(++i) == 0) {
                            this.analizeBlackLine(list);
                            list = new ArrayList<>();
                            break;
                        }
                    }
                }
            }
        }
        System.out.println("countBlackLine = " + this.countBlackLine);
        System.out.println("maxBlackLine = " + this.maxBlackLine);
        System.out.println("maxLengthBlackLine = " + this.maxLengthBlackLine);
    }

    private void analizeBlackLine(ArrayList<Integer> list) {
        int oneChain = 3;
        int twoChain = 2;
        int threeChain = 2;
        int fourChain = 2;
        int fiveChain = 2;
        int sixChain = 2;
        int sevenChain = 2;
        int eitChain = 2;
        int nineChain = 2;
        int tenChain = 2;
        int ellevenChain = 2;
        int twalweChain = 2;
        int treetyChain = 2;


        if (list.get(0) >= oneChain
                && list.size() > 2 && list.get(2) >= twoChain
                && list.size() > 4 && list.get(4) >= threeChain
                && list.size() > 6 && list.get(6) >= fourChain
                && list.size() > 8 && list.get(8) >= fiveChain
                && list.size() > 10 && list.get(10) >= sixChain
                && list.size() > 12 && list.get(12) >= sevenChain
                && list.size() > 14 && list.get(14) >= eitChain
                && list.size() > 16 && list.get(16) >= nineChain
                && list.size() > 18 && list.get(18) >= tenChain
            && list.size() > 20 && list.get(20) >= ellevenChain
            && list.size() > 22 && list.get(22) >= twalweChain
            && list.size() > 24 && list.get(24) >= treetyChain
        ) {
            System.out.println(list);
            if (list.size()/2 > this.maxBlackLine) {
                this.maxBlackLine = list.size()/2;
            }
            System.out.println(list.size()/2);
            int maxLine = list.stream().reduce((x,y)->x+y).get();
            System.out.println(maxLine);
            if (maxLine > this.maxLengthBlackLine) {
                this.maxLengthBlackLine = maxLine;
            }
            this.countBlackLine++;
        }
    }


    /**
     * Делит исходную последовательность на
     * @param data последовательность
     * @param delim делитель
     */
    private void delimiterNumber(ArrayList<Integer> data, int delim) {
        int count = 0;
        int num = 0;
        int prev = 0;
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < data.size() - 1; i++) {
            if(i % delim == 0) {
                count++;
                if (data.get(i) != 0) {
                    num++;
                    System.out.println(num + ". count = " + count + "num = " + data.get(i) + (prev == i - 1 ? "повтор" : "-"));
                    prev = i;
                } else {
                    System.out.println("count = " + count +"num = "+ data.get(i));
                }
            }
        }
        System.out.println("result = " + result);
    }

    private void experimentOne(double ver, int size) {
        List<Integer> list = this.shlopData(this.createRandomData(size, ver, true));
        System.out.println(list);
        System.out.println(this.countResults(list));
    }

    private ArrayList<Integer> shlopData(ArrayList<Integer> data) {
        ArrayList<Integer> result = new ArrayList<>();
        int posCount = 0;
        int negCount = 0;
        for (Integer i : data) {
            if (i == 0) {
                posCount++;
                if (negCount != 0) {
                    result.add(negCount);
                    negCount = 0;
                }
            } else {
                negCount--;
                if (posCount != 0) {
                    result.add(posCount);
                    posCount = 0;
                }
            }
        }
        return result;
    }

    private Map<Integer, Integer> countResults(List<Integer> data) {
        Map<Integer, Integer> result = new HashMap<>();
        for (Integer i : data) {
            result.put(i, result.get(i) == null ? 1 : result.get(i) + 1);
        }
        return result;
    }

    private Map<Integer, Integer> countGoes(List<Integer> data) {
        Map<Integer, Integer> result = new HashMap<>();
        for (Integer i : data) {

            result.put(i, result.get(i) == null ? 1 : result.get(i) + 1);
        }
        return result;
    }
}
