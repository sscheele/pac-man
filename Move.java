public class Move {
    int startRow;
    int startCol;
    int direction;
    int score;
    int originatingDirection;
    int displacement;
    Board on;

    public Move(int startRow, int startCol, int direction, int score, int originatingDirection, Board b, int displacement) {
        this.startRow = startRow;
        this.startCol = startCol;
        this.direction = direction;
        this.score = score;
        this.originatingDirection = originatingDirection;
        this.displacement = displacement + 1;
        on = b;
    }

    /**
     * @return the displacement
     */
    public int getDisplacement() {
        return displacement;
    }

    /**
     * @return the originatingDirection
     */
    public int getOriginatingDirection() {
        return originatingDirection;
    }

    /**
     * @return the direction
     */
    public int getDirection() {
        return direction;
    }

    /**
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * @return the startCol
     */
    public int getStartCol() {
        return startCol;
    }

    /**
     * @return the startRow
     */
    public int getStartRow() {
        return startRow;
    }

    public int getEndRow() {
        if (direction == Character.Moves.UP) {
            return Math.floorMod(startRow - 1, on.getHeight());
        }
        if (direction == Character.Moves.DOWN) {
            return (startRow + 1) % on.getHeight();
        }
        return startRow;
    }

    public int getEndCol() {
        if (direction == Character.Moves.LEFT) {
            return Math.floorMod(startCol - 1, on.getWidth());
        }
        if (direction == Character.Moves.RIGHT) {
            return (startCol + 1) % on.getWidth();
        }
        return startCol;
    }
}