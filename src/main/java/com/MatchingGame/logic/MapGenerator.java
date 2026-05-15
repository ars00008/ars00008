package com.MatchingGame.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MapGenerator {//generate a playable map
    private LinkLogic linkLogic = new LinkLogic();
    private Random random = new Random();

    public int[][] generateMapByDifficulty(String mode) {
        int rows, cols, typeCount;

        if ("EASY".equals(mode)) {
            rows = 10;
            cols = 10;
            typeCount = 5;
            return fillEasyPairs(rows, cols, typeCount);
        } else {
            rows = 10;
            cols = 10;
            typeCount = 12;
            return fillHardPairs(rows, cols, typeCount);
        }
    }

    private int[][] fillEasyPairs(int rows, int cols, int typeCount) {
        int[][] map = new int[rows + 2][cols + 2];
        List<Integer> list = new ArrayList<>();

        int totalCells = 4 * 4 * 2; // 2 of 4 by 4
        for (int i = 0; i < totalCells / 2; i++) {
            int type = (i % typeCount) + 1;// at least 5 types
            list.add((i % typeCount) + 1);
            list.add((i % typeCount) + 1);
        }
        Collections.shuffle(list);

        int index = 0;
        // up 4 by 4
        for (int r = 1; r <= 4; r++) {
            for (int c = 1; c <= 4; c++) {
                map[r][c] = list.get(index++);
            }
        }
        // down 4 by 4
        for (int r = 6; r <= 9; r++) {
            for (int c = 6; c <= 9; c++) {
                map[r][c] = list.get(index++);
            }
        }
        return map;
    }

    private int[][] fillHardPairs(int rows, int cols, int typeCount) {
        int[][] map = new int[rows + 2][cols + 2];
        List<Integer> list = new ArrayList<>();

        int totalCells = rows * cols;

        for (int i = 0; i < totalCells / 2; i++) {
            int type = (i % typeCount) + 1;// 12 types
            list.add(type);
            list.add(type);
        }
        Collections.shuffle(list);

        int index = 0;
        for (int r = 1; r <= rows; r++) {
            for (int c = 1; c <= cols; c++) {
                map[r][c] = list.get(index++);
            }
        }
        return map;
    }

    private void applyMagicSwap(int[][] workingMap, int[][] initialMap) {//fix the deadlock until there's at least one solution
        List<GridPoint> remaining = getRemainingPoints(workingMap);
        if (remaining.size() < 2) return;

        boolean solved = false;
        while (!solved) {
            //random swap
            int idx1 = random.nextInt(remaining.size());
            int idx2 = random.nextInt(remaining.size());
            while (idx1 == idx2) idx2 = random.nextInt(remaining.size());

            GridPoint p1 = remaining.get(idx1);
            GridPoint p2 = remaining.get(idx2);

            // pull the swap to the map
            swap(workingMap, p1, p2);
            swap(initialMap, p1, p2);

            // check validity
            if (!findAllAvailablePairs(workingMap).isEmpty()) {
                solved = true;
            }
        }
    }

    // tool methods

    private List<GridPoint[]> findAllAvailablePairs(int[][] map) {
        List<GridPoint[]> pairs = new ArrayList<>();
        for (int r1 = 1; r1 < map.length - 1; r1++) {
            for (int c1 = 1; c1 < map[0].length - 1; c1++) {
                if (map[r1][c1] == 0) continue;
                for (int r2 = r1; r2 < map.length - 1; r2++) {
                    for (int c2 = (r1 == r2 ? c1 + 1 : 1); c2 < map[0].length - 1; c2++) {
                        if (map[r1][c1] == map[r2][c2]) {
                            GridPoint p1 = new GridPoint(c1, r1);
                            GridPoint p2 = new GridPoint(c2, r2);
                            if (linkLogic.judge(p1, p2, map)) {
                                pairs.add(new GridPoint[]{p1, p2});
                            }// go for all points and all points for all pairs to check link logic
                        }
                    }
                }
            }
        }
        return pairs;
    }

    public void shuffleSpecificMode(int[][] map) {
        List<Integer> values = new ArrayList<>();
        List<GridPoint> positions = new ArrayList<>();

        for (int r = 1; r < map.length - 1; r++) {//scan and only record points with values
            for (int c = 1; c < map[0].length - 1; c++) {
                if (map[r][c] != 0) {
                    values.add(map[r][c]);
                    positions.add(new GridPoint(c, r)); // record
                }
            }
        }

        Collections.shuffle(values);//shuffle with valid points

        for (int i = 0; i < positions.size(); i++) {
            GridPoint pos = positions.get(i);
            map[pos.y()][pos.x()] = values.get(i);
        }
    }

    private List<GridPoint> getRemainingPoints(int[][] map) {
        List<GridPoint> list = new ArrayList<>();
        for (int r = 1; r < map.length - 1; r++) {
            for (int c = 1; c < map[0].length - 1; c++) {
                if (map[r][c] != 0) list.add(new GridPoint(c, r));
            }
        }// get all remain points after eliminate 我说万物一心有没有懂的
        return list;
    }

    private void swap(int[][] map, GridPoint p1, GridPoint p2) {
        int temp = map[p1.y()][p1.x()];
        map[p1.y()][p1.x()] = map[p2.y()][p2.x()];
        map[p2.y()][p2.x()] = temp;
    }//swap points

    /**
    private boolean isCleared(int[][] map) {
        for (int r = 1; r < map.length - 1; r++) {
            for (int c = 1; c < map[0].length - 1; c++) {
                if (map[r][c] != 0) return false;
            }
        }
        return true;
    }//check if is empty

    private int[][] copyMap(int[][] original) {
        int[][] copy = new int[original.length][original[0].length];
        for (int i = 0; i < original.length; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, original[i].length);
        }
        return copy;
    }//影分身の術
    */
}
