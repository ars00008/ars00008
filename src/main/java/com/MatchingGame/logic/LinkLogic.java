package com.MatchingGame.logic;

public class LinkLogic {
    public boolean judge(GridPoint p1, GridPoint p2, int[][] map) {
        if (p1.equals(p2) || map[p1.y()][p1.x()] != map[p2.y()][p2.x()]) return false;
        //cant be the same point, and the two point must have same paterns
        if (isLineLink(p1, p2, map)) return true;
        //with 0 time of turn
        if (isOneTurnLink(p1, p2, map)) return true;
        //with 1 time of turn
        if (isTwoTurnLink(p1, p2, map)) return true;
        //with 2 times of turn
        return false;//cant link
    }

    private boolean isLineLink(GridPoint p1, GridPoint p2, int[][] map) {
        if (p1.x() == p2.x() && p1.y() == p2.y()) return false;//if same point
        if (p1.x() != p2.x() && p1.y() != p2.y()) return false;//if not on same row or same column, return false directly

        int x1 = p1.x(), y1 = p1.y();
        int x2 = p2.x(), y2 = p2.y();
        //get the positions for a line judge

        if (y1 == y2) {
            int minX = Math.min(x1, x2);
            int maxX = Math.max(x1, x2);
            for (int x = minX + 1; x < maxX; x++) {
                if (map[y1][x] != 0) return false;
            }
            return true;
        }//link by same row
        if (x1 == x2) {
            int minY = Math.min(y1, y2);
            int maxY = Math.max(y1, y2);
            for (int y = minY + 1; y < maxY; y++) {
                if (map[y][x1] != 0) return false;
            }
            return true;
        }//link by same column

        return false;
    }


    private boolean isOneTurnLink(GridPoint p1, GridPoint p2, int[][] map) {
        GridPoint c1 = new GridPoint(p1.x(), p2.y());
        GridPoint c2 = new GridPoint(p2.x(), p1.y());
        //possible turn point for a one-turn route

        if (map[c1.y()][c1.x()] == 0) {
            if (isLineLink(p1, c1, map) && isLineLink(c1, p2, map)) {
                return true;
            }
        }//check the validity of c1

        if (map[c2.y()][c2.x()] == 0) {
            if (isLineLink(p1, c2, map) && isLineLink(c2, p2, map)) {
                return true;
            }
        }//check the validity of c2

        return false;
    }

    private boolean isTwoTurnLink(GridPoint p1, GridPoint p2, int[][] map) {
        int rows = map.length;
        int cols = map[0].length;

        int[] dx = {0, 0, 1, -1};
        int[] dy = {1, -1, 0, 0};
        //look for possible turn point on the four directions(up, down, right, left)
        for (int i = 0; i < 4; i++) {// scan from p1

            int nextX = p1.x() + dx[i];
            int nextY = p1.y() + dy[i];

            while (nextX >= 0 && nextX < cols && nextY >= 0 && nextY < rows) {// scan if the grid is in the map and is empty
                if (map[nextY][nextX] != 0) {//p1 and p2 are connected by a straight line
                    if (nextX == p2.x() && nextY == p2.y()) {
                        if (isLineLink(p1, p2, map)) return true;
                    }
                    break;//meet other point, stop scanning
                }

                GridPoint tempPoint = new GridPoint(nextX, nextY);//first turn point

                if (isOneTurnLink(tempPoint, p2, map)) {
                    return true;
                }//one turn from the turning point

                nextX += dx[i];
                nextY += dy[i];
            }
        }
        return false;
    }
}
