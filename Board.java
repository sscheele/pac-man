import java.io.BufferedReader;
import java.io.FileReader;

class Board {
    private int height;
    private int width;
    private int[][] backingArray;
    private Character[] characters;
    private int score;

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
                        backingArray[currentLine - 1][currentIndex] = code;
                        if (code <= Visible.PACMAN && code >= Visible.GHOST0) {
                            characters[code - Visible.GHOST0] = new Character(currentLine - 1, currentIndex, code);
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

    /**
     * @return the score
     */
    public int getScore() {
        return score;
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
            if (characters[i].getRow() == characters[Visible.PACMAN- Visible.GHOST0].getRow() &&
                characters[i].getCol() == characters[Visible.PACMAN- Visible.GHOST0].getCol()){
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

    public void tick() {
        for (Character c : characters) {
            int newLocation;
            int oldData = Integer.MAX_VALUE;
            switch (c.getDirection()) {
            case Character.Moves.RIGHT:
                newLocation = (c.getCol() + 1) % width;
                if (getAt(c.getRow(), newLocation) != Visible.WALL) {
                    if (c.getId() == Visible.PACMAN) {
                        backingArray[c.getRow()][c.getCol()] = -1;
                    } else {
                        backingArray[c.getRow()][c.getCol()] = c.getOnTopOf();
                    }
                    c.setCol(newLocation);
                    oldData = setAt(c.getRow(), newLocation, c.getId());
                }
                break;
            case Character.Moves.UP:
                newLocation = Math.floorMod(c.getRow() - 1, height);
                if (getAt(newLocation, c.getCol()) != Visible.WALL) {
                    if (c.getId() == Visible.PACMAN) {
                        backingArray[c.getRow()][c.getCol()] = -1;
                    } else {
                        backingArray[c.getRow()][c.getCol()] = c.getOnTopOf();
                    }
                    c.setRow(newLocation);
                    oldData = setAt(newLocation, c.getCol(), c.getId());
                }
                break;
            case Character.Moves.DOWN:
                newLocation = (c.getRow() + 1) % height;
                if (getAt(newLocation, c.getCol()) != Visible.WALL) {
                    if (c.getId() == Visible.PACMAN) {
                        backingArray[c.getRow()][c.getCol()] = -1;
                    } else {
                        backingArray[c.getRow()][c.getCol()] = c.getOnTopOf();
                    }
                    c.setRow(newLocation);
                    oldData = setAt(newLocation, c.getCol(), c.getId());
                }
                break;
            case Character.Moves.LEFT:
                newLocation = Math.floorMod(c.getCol() - 1, width);
                if (getAt(c.getRow(), newLocation) != Visible.WALL) {
                    if (c.getId() == Visible.PACMAN) {
                        backingArray[c.getRow()][c.getCol()] = -1;
                    } else {
                        backingArray[c.getRow()][c.getCol()] = c.getOnTopOf();
                    }
                    c.setCol(newLocation);
                    oldData = setAt(c.getRow(), newLocation, c.getId());
                }
                break;
            }
            if (c.getId() == Visible.PACMAN && oldData == Visible.DOT) {
                score++;
            }
            if (oldData != Integer.MAX_VALUE) c.setOnTopOf(oldData);
        }
    }
}