import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics;

import javax.swing.ImageIcon;

public class Character {
    int row;
    int col;
    int id;
    int direction;
    int onTopOf;
    private static ImageIcon sprites = new ImageIcon("res/img/sprites.png");
    Image currentImg;
    Image[] directionalImages;

    public static class Moves {
        public static final int RIGHT = 0;
        public static final int LEFT = 1;
        public static final int UP = 2;
        public static final int DOWN = 3;
    }

    public Character(int row, int col, int id) {
        this.row = row;
        this.col = col;
        this.id = id;
        onTopOf = -1;
        directionalImages = new Image[4];
        BufferedImage baseSprites = new BufferedImage(sprites.getIconWidth(), sprites.getIconHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics g = baseSprites.createGraphics();
        // paint the Icon to the BufferedImage.
        sprites.paintIcon(null, g, 0, 0);
        g.dispose();
        if (id == Visible.PACMAN) {
            directionalImages = new Image[] { baseSprites.getSubimage(0, 0, 17, 17),
                    baseSprites.getSubimage(0, 16, 17, 17), baseSprites.getSubimage(0, 32, 17, 17),
                    baseSprites.getSubimage(0, 48, 17, 17) };
        } else {
            int base = 64 + 16 * id;
            for (int i = 0; i < 4; i++) {
                directionalImages[i] = baseSprites.getSubimage((int)(32.5 * i) + 1, base, 17, 17);
            }
        }
        currentImg = directionalImages[0];
        direction = 0;
    }

    /**
     * @return the onTopOf
     */
    public int getOnTopOf() {
        return onTopOf;
    }

    /**
     * @param onTopOf the onTopOf to set
     */
    public void setOnTopOf(int onTopOf) {
        if (onTopOf < Visible.GHOST0 || onTopOf > Visible.PACMAN){
            this.onTopOf = onTopOf;
        } else {
            this.onTopOf = -1;
        }
    }

    /**
     * @param currentImg the currentImg to set
     */
    public void setCurrentImg(Image currentImg) {
        this.currentImg = currentImg;
    }

    /**
     * @return the col
     */
    public int getCol() {
        return col;
    }

    /**
     * @param col the col to set
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the image
     */
    public Image getImage() {
        return currentImg;
    }

    /**
     * @return the row
     */
    public int getRow() {
        return row;
    }

    /**
     * @param row the row to set
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * @return the direction
     */
    public int getDirection() {
        return direction;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(int direction) {
        this.direction = direction;
        this.currentImg = directionalImages[direction];
    }
}