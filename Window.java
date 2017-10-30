import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

class Window extends JPanel implements ActionListener {
    private Board gameBoard;
    private int height;
    private int width;
    private Image wallImg = new ImageIcon("res/img/wall.png").getImage();
    private Image dotImg = new ImageIcon("res/img/dot.png").getImage();
    private Image pacmanSolid = new ImageIcon("res/img/pacman_solid.png").getImage();
    private final int TILE_SIZE = 30;
    private final int ANIM_DELAY = 75;

    private int anim_parity = 0;

    public Window() {
        gameBoard = new Board("testboard");
        height = gameBoard.getHeight();
        width = gameBoard.getWidth();
        new Timer(ANIM_DELAY, new TimerListener()).start();
    }

    private class TimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Character pac = gameBoard.getCharacters()[Visible.PACMAN - Visible.GHOST0];
            if (anim_parity % 2 == 0) {
                pac.setCurrentImg(pacmanSolid);
            } else {
                //triggers an image reset
                pac.setDirection(pac.getDirection());
            }
            if (anim_parity % Board.PAC_DELAY == 0) {
                gameBoard.move(pac);
            }
            if (anim_parity % Board.GHOST_DELAY == 0) {
                for (int i = Visible.GHOST0; i < Visible.PACMAN; i++) {
                    Character currGhost = gameBoard.getCharacters()[i - Visible.GHOST0];
                    currGhost.setDirection(BotAI.getMove(currGhost, gameBoard));
                    //currGhost.setDirection(BotAI.getMove(currGhost, gameBoard, anim_parity));
                    gameBoard.move(currGhost);
                }
            }
            checkWinLose();
            anim_parity = (anim_parity + 1) % Board.NUM_FRAMES;
            repaint();
        }
    }

    private void checkWinLose() {
        if (gameBoard.isLost()) {
            Logger.log("You lost");
        }
        if (gameBoard.isWon()) {
            Logger.log("You win!");
        }
    }

    /**
     * @return the gameBoard
     */
    public Board getGameBoard() {
        return gameBoard;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    public void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, TILE_SIZE * width, TILE_SIZE * height);

        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                int currItem = gameBoard.getAt(r, c);
                if (currItem == -1)
                    continue;
                if (currItem == Visible.WALL) {
                    g2d.drawImage(wallImg, TILE_SIZE * c, TILE_SIZE * r, TILE_SIZE, TILE_SIZE, Color.black, null);
                } else if (currItem == Visible.DOT) {
                    g2d.drawImage(dotImg, TILE_SIZE * c, TILE_SIZE * r, TILE_SIZE, TILE_SIZE, Color.black, null);
                }
            }
        }
        for (Character c : gameBoard.getCharacters()) {
            if (c != null) {
                g2d.drawImage(c.getImage(), TILE_SIZE * c.getCol(), TILE_SIZE * c.getRow(), TILE_SIZE, TILE_SIZE, Color.black, null);                
            }
        }

        g2d.setFont(new Font("Helvetica", Font.BOLD, 14));
        g2d.setColor(new Color(96, 128, 255));
        g2d.drawString("Score: " + gameBoard.getScore(), 27, 54);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}