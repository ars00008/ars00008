package com.MatchingGame.logic;

import java.util.ArrayList;
import java.util.List;

public class AvailablePairsFounder {
    public static List<GridPoint[]> findAllAvailablePairs(int[][] map) {
        List<GridPoint> tiles = new ArrayList<>();
        LinkLogic logic = new LinkLogic();

        for (int r = 1; r < map.length - 1; r++) {//collect all non-0 points
            for (int c = 1; c < map[0].length - 1; c++) {
                if (map[r][c] != 0) {
                    tiles.add(new GridPoint(c, r));
                }
            }
        }

        List<GridPoint[]> available = new ArrayList<>();
        for (int i = 0; i < tiles.size(); i++) {
            for (int j = i + 1; j < tiles.size(); j++) {
                GridPoint p1 = tiles.get(i);
                GridPoint p2 = tiles.get(j);

                //connectable and same color
                if (map[p1.y()][p1.x()] == map[p2.y()][p2.x()] &&
                        logic.judge(p1, p2, map)) {
                    available.add(new GridPoint[]{p1, p2});
                }
            }
        }
        return available;
    }
}
