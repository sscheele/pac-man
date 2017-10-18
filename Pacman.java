import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Pacman extends JFrame {
    Window win;
    public Pacman() {
        initUI();
    }
    
    private void initUI() {
        win = new Window();
        add(win);
        setTitle("Pacman");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(null);
        setVisible(true);
        addKeyListener(new PacAdapter());
        requestFocusInWindow();
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            Pacman ex = new Pacman();
            ex.setVisible(true);
        });
    }

    private class PacAdapter extends KeyAdapter { 
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            Character pac = win.getGameBoard().getCharacters()[Visible.PACMAN - Visible.GHOST0];
            if (key == KeyEvent.VK_LEFT) {
                pac.setDirection(Character.Moves.LEFT);
            } else if (key == KeyEvent.VK_RIGHT) {
                pac.setDirection(Character.Moves.RIGHT);
            } else if (key == KeyEvent.VK_UP) {
                pac.setDirection(Character.Moves.UP);
            } else if (key == KeyEvent.VK_DOWN) {
                pac.setDirection(Character.Moves.DOWN);
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }
}