import java.io.BufferedReader;
import java.io.FileReader;

class Board {
    private int height;
    private int width;
    private int[][] backingArray;
    private Character[] characters;
    private int score;

    public static final int PAC_DELAY = 2;
    public static final int GHOST_DELAY = 3;
    public static final int CHOMP_DELAY = 2;
    public static final int NUM_FRAMES = PAC_DELAY * GHOST_DELAY * CHOMP_DELAY;

    public Board(String filename) {
        characters = new Character[4];
        score = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int currentLine = 0;
            while ((line = br.readLine()) != null) {
                if (currentLine == 0) {
                    String[] dimensions = line.split(" ");
                    try {
                        height = Integer.parseInt(dimensions[0]);
                        width = Integer.parseInt(dimensions[1]);
                    } catch (NumberFormatException e) {
                        Logger.log("Unable to parse number");
                        e.printStackTrace();
                        return;
                    } catch (ArrayIndexOutOfBoundsException e) {
                        Logger.log("Array index out of bounds");
                        e.printStackTrace();
                        return;
                    }
                    backingArray = new int[height][width];
                    currentLine++;
                    continue;
                }
                int currentIndex = 0;
                for (char c : line.toCharArray()) {
                    int code = c - 48;
                    if (code < -1 || code > Visible.DOT) {
                        Logger.log("Couldn't parse character: " + c);
                    } else {
                        if (code <= Visible.PACMAN && code >= Visible.GHOST0) {
                            characters[code - Visible.GHOST0] = new Character(currentLine - 1, currentIndex, code);
                            backingArray[currentLine - 1][currentIndex] = -1;
                        } else {
                            backingArray[currentLine - 1][currentIndex] = code;
                        }
                        currentIndex++;
                    }
                }
                currentLine++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Board(Board b) {
        height = b.getHeight();
        width = b.getWidth();
        backingArray = new int[height][width];
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                backingArray[r][c] = b.getBackingArray()[r][c];
            }
        }
        characters = new Character[4];
        for (int i = 0; i < characters.length; i++) {
            characters[i] = new Character(b.getCharacters()[i]);
        }
        score = b.getScore();
    }

    public Board after(int charIndex, int direction) {
        Board retVal = new Board(this);
        retVal.getCharacters()[charIndex].setDirection(direction);
        retVal.move(retVal.getCharacters()[charIndex]);
        return retVal;
    }

    /**
     * @return the score
     */
    public int getScore() {
        return score;
    }

    public boolean ghostAt(int r, int c) {
        for (int i = 0; i < characters.length - 1; i++) {
            if (characters[i].getRow() == r && characters[i].getCol() == c) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return the characters
     */
    public Character[] getCharacters() {
        return characters;
    }

    public int getAt(int row, int col) {
        if (row < 0 || col < 0 || row > height || col > width)
            return Visible.WALL;
        return backingArray[row][col];
    }

    public int setAt(int row, int col, int target) {
        int tmp = backingArray[row][col];
        if (tmp == Visible.WALL) {
            return -1;
        }
        backingArray[row][col] = target;
        return tmp;
    }

    public int[][] getBackingArray() {
        return backingArray;
    }

    public boolean isLost() {
        for (int i = 0; i < Visible.PACMAN - Visible.GHOST0; i++) {
            if (characters[i].getRow() == characters[Visible.PACMAN - Visible.GHOST0].getRow()
                    && characters[i].getCol() == characters[Visible.PACMAN - Visible.GHOST0].getCol()) {
                return true;
            }
        }
        return false;
    }

    public boolean isWon() {
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                if (backingArray[r][c] == Visible.DOT) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @param score the score to set
     */
    public void setScore(int score) {
        this.score = score;
    }

    public void tick() {
        for (Character c : characters) {
            move(c);
        }
    }

    public void move(Character c) {
        int newLocation;

        if (c.getId() == Visible.PACMAN) {
            if (backingArray[c.getRow()][c.getCol()] == Visible.DOT) {
                score++;
            }
            backingArray[c.getRow()][c.getCol()] = -1;
        }

        switch (c.getDirection()) {
        case Character.Moves.RIGHT:
            newLocation = (c.getCol() + 1) % width;
            if (getAt(c.getRow(), newLocation) != Visible.WALL) {
                c.setCol(newLocation);
            }
            break;
        case Character.Moves.UP:
            newLocation = Math.floorMod(c.getRow() - 1, height);
            if (getAt(newLocation, c.getCol()) != Visible.WALL) {
                c.setRow(newLocation);
            }
            break;
        case Character.Moves.DOWN:
            newLocation = (c.getRow() + 1) % height;
            if (getAt(newLocation, c.getCol()) != Visible.WALL) {
                c.setRow(newLocation);
            }
            break;
        case Character.Moves.LEFT:
            newLocation = Math.floorMod(c.getCol() - 1, width);
            if (getAt(c.getRow(), newLocation) != Visible.WALL) {
                c.setCol(newLocation);
            }
            break;
        }
    }
}