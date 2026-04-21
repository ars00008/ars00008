package com.MatchingGame.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MapGenerator {//generate a playable map
    private LinkLogic linkLogic = new LinkLogic();
    private Random random = new Random();

    public int[][] generatePerfectMap(int rows, int cols, int typeCount) {
        // basic random map
        int[][] initialMap = new int[rows + 2][cols + 2];
        fillPairs(initialMap, rows, cols, typeCount);

        // a copy for validation
        int[][] workingMap = copyMap(initialMap);

        // link start!!!
        while (!isCleared(workingMap)) {
            List<GridPoint[]> availablePairs = findAllAvailablePairs(workingMap);
            // get all solvable pairs

            if (availablePairs.isEmpty()) {
                // deadlock, 都给我回来，重审！
                applyMagicSwap(workingMap, initialMap);
                // swap and continue work on the workingMap
            } else {
                // eliminate the solvable pair
                GridPoint[] pair = availablePairs.get(random.nextInt(availablePairs.size()));
                workingMap[pair[0].y()][pair[0].x()] = 0;
                workingMap[pair[1].y()][pair[1].x()] = 0;
            }
        }

        return initialMap;//map without deadlock
    }

    private void fillPairs(int[][] map, int rows, int cols, int typeCount) {//create random map
        List<Integer> list = new ArrayList<>();
        int totalPairs = (rows * cols) / 2;
        for (int i = 0; i < totalPairs; i++) {
            list.add((i % typeCount) + 1);
            list.add((i % typeCount) + 1);
        }//for different patterns
        Collections.shuffle(list);

        int index = 0;
        for (int r = 1; r <= rows; r++) {
            for (int c = 1; c <= cols; c++) {
                map[r][c] = list.get(index++);
            }
        }
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
}
