package com.MatchingGame.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MapValid {
    private LinkLogic linkLogic = new LinkLogic();//get the link logic method only for this class

    public int[][] generateAvailableMap(int rows, int cols, int typeCount) {
        int[][] map = new int[rows + 2][cols + 2]; // one more grid on the edges
        boolean isAvailable = false; // default as false

        while (!isAvailable) {
            fillPairs(map, rows, cols, typeCount);
            //generate a map
            if (hasSolution(map)) {
                isAvailable = true;
            }//check the validity of the map

        }//if not valid, regenerate

        return map;
    }

    private void fillPairs(int[][] map, int rows, int cols, int typeCount) {//create random map
        List<Integer> list = new ArrayList<Integer>();
        int totalPairs = (rows * cols) / 2;
        for (int i = 0; i < totalPairs; i++) {
            int pattern = (i % typeCount) + 1; //for different patterns
            list.add(pattern);
            list.add(pattern);
        }
        Collections.shuffle(list);

        int index = 0;
        for (int r = 1; r <= rows; r++) {
            for (int c = 1; c <= cols; c++) {
                map[r][c] = list.get(index++);
            }
        }// fill in numbers in the map
    }

    public boolean hasSolution(int[][] map) {//check for solutions
        for (int r1 = 1; r1 < map.length - 1; r1++) {//go for all grid and find the same-pattern grid
            for (int c1 = 1; c1 < map[0].length - 1; c1++) {
                if (map[r1][c1] == 0) continue;

                for (int r2 = r1; r2 < map.length - 1; r2++) {//find the other point in the pair
                    for (int c2 = (r1 == r2 ? c1 + 1 : 1); c2 < map[0].length - 1; c2++) {
                        if (map[r1][c1] == map[r2][c2]) {
                            if (linkLogic.judge(new GridPoint(c1, r1), new GridPoint(c2, r2), map)) {
                                return true; //at least one pair can be found as linkable
                            }
                        }
                    }
                }
            }
        }
        return false;//残念,no solution
    }

}
