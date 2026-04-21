package com.MatchingGame.logic;

public class LinkLogic {
    public boolean judge(GridPoint p1, GridPoint p2, int[][] map) {
        if (p1.equals(p2) || map[p1.x()][p1.y()] != map[p2.x()][p2.y()]) return false;
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

        int startY = Math.min(p1.x(), p2.x());
        int startX = Math.min(p1.y(), p2.y());
        int endY = Math.max(p1.x(), p2.x());
        int endX = Math.max(p1.y(), p2.y());
        //get the positions for a line judge

        if (startY==endY){
            for (int i = startX+1; i < endX; i++) {
                if (map[startY][i] != 0) return false;
            }//line link by same row
        }
        if (startX==endX){
            for (int i = startY+1; i < endY; i++) {
                if (map[i][startX] != 0) return false;
            }//line by same column
        }

        return true;
    }


    private boolean isOneTurnLink(GridPoint p1, GridPoint p2, int[][] map) {
        GridPoint c1 = new GridPoint(p1.x(), p2.y());
        GridPoint c2 = new GridPoint(p2.x(), p1.y());
        //possible turn point for a one-turn route

        if (map[c1.x()][c1.y()] == 0) {
            if (isLineLink(p1, c1, map) && isLineLink(c1, p2, map)) {
                return true;
            }
        }//check the validity of c1

        if (map[c2.x()][c2.y()] == 0) {
            if (isLineLink(p1, c2, map) && isLineLink(c2, p2, map)) {
                return true;
            }
        }//check the validity of c2

        return false;
    }

    private boolean isTwoTurnLink(GridPoint p1, GridPoint p2, int[][] map) {
        int[] dx = {0, 0, 1, -1};
        int[] dy = {1, -1, 0, 0};
        //look for possible turn point on the four directions(up, down, right, left)
        for (int i = 0; i < 4; i++) {// scan from p1

            int nextX = p1.x() + dx[i];
            int nextY = p1.y() + dy[i];

            while (nextX >= 0 && nextX < map.length && nextY >= 0 && nextY < map[0].length
                    && map[nextX][nextY] == 0) {// scan if the grid is in the map and is empty

                GridPoint tempPoint = new GridPoint(nextX, nextY);

                if (isOneTurnLink(tempPoint, p2, map)) {
                    return true; // find
                }// if the turn point can link to p2 with a one-turn route

                nextX += dx[i];
                nextY += dy[i];
                // do not find a valid link, continue to find
            }
        }
        return false;
    }
}
