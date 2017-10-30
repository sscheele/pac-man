public class AIHelpers {
    public static int distance(int r1, int c1, int r2, int c2) {
        return Math.abs(r1 - r2) + Math.abs(c1 - c2);
    }

    public static int distance(Character c1, Character c2) {
        return Math.abs(c1.getRow() - c2.getRow()) + Math.abs(c1.getCol() - c2.getCol());
    }

    public static int evaluation(Board b) {
        final double distanceWeight = .7;
        final double scoreWeight = 1;
        final double winWeight = 500;
        final double loseWeight = -500;
        final double rayWeight = .4;
        final double minDistanceWeight = 25;

        int distanceSum = 0;
        int minDistance = Integer.MAX_VALUE;
        Character pacman = b.getCharacters()[Visible.PACMAN - Visible.GHOST0];
        for (int i = Visible.GHOST0; i < Visible.PACMAN; i++) {
            Character c = b.getCharacters()[i - Visible.GHOST0];
            int currDistance = AIHelpers.distance(c, pacman);
            distanceSum += currDistance;
            if (currDistance < minDistance) {
                minDistance = currDistance;
            }
        }

        int hasWon = b.isWon() ? 1 : 0;
        int hasLost = b.isLost() ? 1 : 0;

        return (int) (distanceWeight * distanceSum) + (int) (scoreWeight * b.getScore()) + (int) (winWeight * hasWon)
                + (int) (loseWeight * hasLost) + (int) (rayWeight * AIHelpers.rayTotal(pacman, b)) + (int)(minDistanceWeight * minDistance);
    }

    public static int rayTotal(Character c, Board b) {
        int retVal = 0;
        int[][] board = b.getBackingArray();
        for (int i = -1; i < 2; i += 2) {
            for (int a = 0; a < b.getHeight(); a++) {
                int newRow = Math.floorMod(c.getRow() + (i * a), b.getHeight());
                if (board[newRow][c.getCol()] == Visible.WALL || (board[newRow][c.getCol()] >= Visible.GHOST0
                        && board[newRow][c.getCol()] <= Visible.PACMAN)) {
                    break;
                }
                retVal++;
            }
            for (int a = 0; a < b.getWidth(); a++) {
                int newCol = Math.floorMod(c.getCol() + (i * a), b.getWidth());
                if (board[c.getRow()][newCol] == Visible.WALL || (board[c.getRow()][newCol] >= Visible.GHOST0
                        && board[c.getRow()][newCol] <= Visible.PACMAN)) {
                    break;
                }
                retVal++;
            }
        }
        return retVal;
    }

    public static int getNextFrame(int frame) {
        int retVal = (frame + 1) % Board.NUM_FRAMES;
        while (retVal % Board.GHOST_DELAY != 0 && retVal % Board.PAC_DELAY != 0) {
            retVal = (retVal + 1) % Board.NUM_FRAMES;
        }
        return retVal;
    }
}