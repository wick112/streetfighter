import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GraphicsPanel extends JPanel implements KeyListener, MouseListener {
    private BufferedImage background;
    private Player player;
    private boolean[] pressedKeys;
    private ArrayList<Coin> coins;
    private Timer punchTimer;



    public GraphicsPanel() {
        try {
            background = ImageIO.read(new File("src/fightbackground.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        player = new Player("src/ryuRight.png", "src/ryuPunchRight.png", "src/ryuPunchLeft.png");
        coins = new ArrayList<>();
        pressedKeys = new boolean[128]; // 128 keys on keyboard, max keycode is 127
        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true); // this line of code + one below makes this panel active for keylistener events
        requestFocusInWindow(); // see comment above

        punchTimer = new Timer(600, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.resetPunch();
                punchTimer.stop();
                repaint();
            }
        });
        punchTimer.setRepeats(false);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);  // just do this
        g.drawImage(background, 0, 0, null);  // the order that things get "painted" matter; we put background down first
        g.drawImage(player.getPlayerImage(), player.getxCoord(), player.getyCoord(), player.getWidth(), player.getHeight(), null);

        // this loop does two things:  it draws each Coin that gets placed with mouse clicks,
        // and it also checks if the player has "intersected" (collided with) the Coin, and if so,
        // the score goes up and the Coin is removed from the arraylist
        for (int i = 0; i < coins.size(); i++) {
            Coin coin = coins.get(i);
            g.drawImage(coin.getImage(), coin.getxCoord(), coin.getyCoord(), null); // draw Coin
            if (player.playerRect().intersects(coin.coinRect())) { // check for collision
                player.collectCoin();
                coins.remove(i);
                i--;
            }
        }

        // draw score
        g.setFont(new Font("Courier New", Font.BOLD, 24));
        g.drawString("Score: " + player.getScore(), 20, 40);

        if (pressedKeys[69]) { // E key
            if (!player.isPunching()) {
                player.punch();
                punchTimer.start();
            }
        } else {
            // player moves left (A)
            if (pressedKeys[65]) {
                player.faceLeft();
                player.moveLeft();
            }

            // player moves right (D)
            if (pressedKeys[68]) {
                player.faceRight();
                player.moveRight();
            }

            // player moves up (W)
//            if (pressedKeys[87]) {
//                player.moveUp();
//            }

            // player moves down (S)
//            if (pressedKeys[83]) {
//                player.moveDown();
//            }
        }
        repaint();
    }

    // ----- KeyListener interface methods -----
    public void keyTyped(KeyEvent e) { } // unimplemented

    public void keyPressed(KeyEvent e) {
        // see this for all keycodes: https://stackoverflow.com/questions/15313469/java-keyboard-keycodes-list
        // A = 65, D = 68, S = 83, W = 87, left = 37, up = 38, right = 39, down = 40, space = 32, enter = 10
        int key = e.getKeyCode();
        pressedKeys[key] = true;
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        pressedKeys[key] = false;
    }

    // ----- MouseListener interface methods -----
    public void mouseClicked(MouseEvent e) { }  // unimplemented; if you move your mouse while clicking,
    // this method isn't called, so mouseReleased is best

    public void mousePressed(MouseEvent e) { } // unimplemented

    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {  // left mouse click
            Point mouseClickLocation = e.getPoint();
            Coin coin = new Coin(mouseClickLocation.x, mouseClickLocation.y);
            coins.add(coin);

        }else {
            Point mouseClickLocation = e.getPoint();
            if (player.playerRect().contains(mouseClickLocation)) {
                player.turn();
            }
        }
    }

    public void mouseEntered(MouseEvent e) { } // unimplemented

    public void mouseExited(MouseEvent e) { } // unimplemented


}
