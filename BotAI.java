import java.util.PriorityQueue;

class BotAI {
    public static int getMove(Character c, Board b) {
        return shortestPath(c, b.getCharacters()[Visible.PACMAN - Visible.GHOST0], b);
    }

    private static int shortestPath(Character c1, Character c2, Board b) {
        boolean[][] marked = new boolean[b.getHeight()][b.getWidth()];
        marked[c1.getRow()][c1.getCol()] = true;

        PriorityQueue<Move> validMoves = new PriorityQueue<>((Move one, Move two) -> one.getScore() - two.getScore());
        for (int i = -1; i < 2; i += 2) {
            if (b.getAt(Math.floorMod(c1.getRow() + i, b.getHeight()), c1.getCol()) != Visible.WALL) {
                int dir = i == -1 ? Character.Moves.UP : Character.Moves.DOWN;
                validMoves.add(new Move(c1.getRow(), c1.getCol(), dir,
                        AIHelpers.distance(c1.getRow() + i, c1.getCol(), c2.getRow(), c2.getCol()), dir, b));
            }
            if (b.getAt(c1.getRow(), Math.floorMod(c1.getCol() + i, b.getHeight())) != Visible.WALL) {
                int dir = i == -1 ? Character.Moves.LEFT : Character.Moves.RIGHT;
                validMoves.add(new Move(c1.getRow(), c1.getCol(), dir,
                        AIHelpers.distance(c1.getRow(), c1.getCol() + i, c2.getRow(), c2.getCol()), dir, b));
            }
        }
        while (!validMoves.isEmpty()) {
            Move next = validMoves.poll();
            int endRow = next.getEndRow();
            int endCol = next.getEndCol();

            if (marked[endRow][endCol])
                continue;
            marked[endRow][endCol] = true;
            if (endRow == c2.getRow() && endCol == c2.getCol()) {
                return next.getOriginatingDirection();
            }
            for (int i = -1; i < 2; i += 2) {
                if (b.getAt(Math.floorMod(endRow + i, b.getHeight()), endCol) != Visible.WALL) {
                    int dir = i == -1 ? Character.Moves.UP : Character.Moves.DOWN;
                    validMoves.add(new Move(endRow, endCol, dir,
                            AIHelpers.distance(endRow + i, endCol, c2.getRow(), c2.getCol()),
                            next.getOriginatingDirection(), b));
                }
                if (b.getAt(endRow, Math.floorMod(endCol + i, b.getWidth())) != Visible.WALL) {
                    int dir = i == -1 ? Character.Moves.LEFT : Character.Moves.RIGHT;
                    validMoves.add(new Move(endRow, endCol, dir,
                            AIHelpers.distance(endRow, endCol + i, c2.getRow(), c2.getCol()),
                            next.getOriginatingDirection(), b));
                }
            }
        }
        return Integer.MIN_VALUE;
    }
}