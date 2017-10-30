import java.util.PriorityQueue;
import java.util.Arrays;

class BotAI {
    private static final int SEARCH_DEPTH = 5;

    public static int getMove(Character c, Board b) {
        return shortestPath(c, b.getCharacters()[Visible.PACMAN - Visible.GHOST0], b);
    }

    public static int getMove(Character c, Board b, int frame) {
        return alphaBeta(c, b, frame);
    }

    private static int alphaBeta(Character c, Board b, int frame) {
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        int numGhosts = 0;
        Character[] chars = b.getCharacters();
        //pacman is last in array
        for (int i = 0; i < chars.length - 1; i++) {
            if (chars[i] != null)
                numGhosts++;
        }

        return alphaBetaHelper(chars[c.getId() - Visible.GHOST0], 0, alpha, beta, b, numGhosts, frame,
                c.getId() - Visible.GHOST0);
    }

    private static int alphaBetaHelper(Character c, int depth, int alpha, int beta, Board b, int numGhosts, int frame,
            int currGhost) {
        if (depth == SEARCH_DEPTH) {
            return AIHelpers.evaluation(b);
        }

        boolean isMinNode = false;
        if (currGhost < numGhosts) {
            isMinNode = true;
        } else {
            frame = AIHelpers.getNextFrame(frame);
            if (frame % Board.GHOST_DELAY == 0) {
                currGhost = -1;
            }
        }

        //get and sort moves
        int charIndex = c.getId() - Visible.GHOST0;
        Board[] moves = new Board[] { b.after(charIndex, 0), b.after(charIndex, 1), b.after(charIndex, 2),
                b.after(charIndex, 3) };
        for (Board tmp : moves) {
            tmp.setScore(AIHelpers.evaluation(tmp));
        }
        Arrays.sort(moves, (Board one, Board two) -> two.getScore() - one.getScore());

        int bestValue = isMinNode ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        int bestIndex = -1;
        for (int i = 0; i < moves.length; i++) {
            int value = alphaBetaHelper(c, depth + 1, alpha, beta, moves[i], numGhosts, frame, currGhost + 1);
            if (value > beta || value < alpha)
                break;
            if (isMinNode) {
                if (value < beta) {
                    beta = value;
                }
                if (value < bestValue) {
                    bestValue = value;
                    bestIndex = i;
                }
            } else {
                if (value > alpha) {
                    alpha = value;
                }
                if (value > bestValue) {
                    bestValue = value;
                    bestIndex = i;
                }
            }
        }
        if (depth == 0) {
            return bestIndex;
        }
        return bestValue;
    }

    private static int shortestPath(Character c1, Character c2, Board b) {
        boolean[][] marked = new boolean[b.getHeight()][b.getWidth()];
        marked[c1.getRow()][c1.getCol()] = true;

        PriorityQueue<Move> validMoves = new PriorityQueue<>((Move one, Move two) -> one.getDisplacement()
                + one.getScore() - two.getDisplacement() - two.getScore());
        for (int i = -1; i < 2; i += 2) {
            if (b.getAt(Math.floorMod(c1.getRow() + i, b.getHeight()), c1.getCol()) != Visible.WALL
                    && !b.ghostAt(Math.floorMod(c1.getRow() + i, b.getHeight()), c1.getCol())) {
                int dir = i == -1 ? Character.Moves.UP : Character.Moves.DOWN;
                validMoves.add(new Move(c1.getRow(), c1.getCol(), dir,
                        AIHelpers.distance(c1.getRow() + i, c1.getCol(), c2.getRow(), c2.getCol()), dir, b, 0));
            }
            if (b.getAt(c1.getRow(), Math.floorMod(c1.getCol() + i, b.getWidth())) != Visible.WALL
                    && !b.ghostAt(c1.getRow(), Math.floorMod(c1.getCol() + i, b.getWidth()))) {
                int dir = i == -1 ? Character.Moves.LEFT : Character.Moves.RIGHT;
                validMoves.add(new Move(c1.getRow(), c1.getCol(), dir,
                        AIHelpers.distance(c1.getRow(), c1.getCol() + i, c2.getRow(), c2.getCol()), dir, b, 0));
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
                if (b.getAt(Math.floorMod(endRow + i, b.getHeight()), endCol) != Visible.WALL
                        && !b.ghostAt(Math.floorMod(endRow + i, b.getHeight()), endCol)) {
                    int dir = i == -1 ? Character.Moves.UP : Character.Moves.DOWN;
                    validMoves.add(new Move(endRow, endCol, dir,
                            AIHelpers.distance(endRow + i, endCol, c2.getRow(), c2.getCol()),
                            next.getOriginatingDirection(), b, next.getDisplacement()));
                }
                if (b.getAt(endRow, Math.floorMod(endCol + i, b.getWidth())) != Visible.WALL
                        && !b.ghostAt(endRow, Math.floorMod(endCol + i, b.getWidth()))) {
                    int dir = i == -1 ? Character.Moves.LEFT : Character.Moves.RIGHT;
                    validMoves.add(new Move(endRow, endCol, dir,
                            AIHelpers.distance(endRow, endCol + i, c2.getRow(), c2.getCol()),
                            next.getOriginatingDirection(), b, next.getDisplacement()));
                }
            }
        }
        Logger.log("A* failed for bot ID " + c1.getId() + ", defaulting to random move");
        return (int) (Math.random() * 4);
    }
}