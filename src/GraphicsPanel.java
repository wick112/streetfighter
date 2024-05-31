import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class GraphicsPanel extends JPanel implements KeyListener, MouseListener {
    private BufferedImage background;
    private Player player;
    private Player2 player2;
    private boolean[] pressedKeys;
    private Timer punchTimer;
    private Timer punchTimer2;

    private Clip songClip;



    public GraphicsPanel() {
        try {
            background = ImageIO.read(new File("src/Assets/fightbackground.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        player = new Player("src/RyuAni/run_1.png", "src/RyuAni.RyuAniPunch/punch_1.png", "src/RyuAni.RyuAniPunch/punch_1.png");
        player2 = new Player2("src/Bison/Ani/bisonRun_1.png", "src/BisonAni/bisonPunch_1.png", "src/BisonAni/bisonPunch_1.png");
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

        punchTimer2 = new Timer(600, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player2.resetPunch();
                punchTimer2.stop();
                repaint();
            }
        });
        punchTimer2.setRepeats(false);

        playMusic();

    }

    @Override
    public void paintComponent(Graphics g) {
        boolean player2Action = false;

        super.paintComponent(g);  // just do this
        g.drawImage(background, 0, 0, null);  // the order that things get "painted" matter; we put background down first
        g.drawImage(player.getPlayerImage(), player.getxCoord(), player.getyCoord(), player.getWidth(), player.getHeight(), null);
        g.drawImage(player2.getPlayerImage(), player2.getxCoord(), player2.getyCoord(), player2.getWidth(), player2.getHeight(), null);

        // draw score
        g.setFont(new Font("Courier New", Font.BOLD, 24));
        g.drawString("Player Health: " + player.getHealth(), 20, 40);
        g.drawString("Player2 Health: " + player2.getHealth(), 400, 40);

        if (pressedKeys[17]) { // E key
            if (!player2.isPunching()) {
                player2.punch();
                punchTimer2.start();
                if (player2.playerRect().intersects(player.playerRect())) { // check for collision
                    player.setHealth(5);
                    if(player.getHealth() == 0){
                        songClip.stop();
                        songClip.close();
                        bisonWin();
                    }
                }
            }
            player2Action = true;
        } else {
            if (pressedKeys[37]) {
                player2.faceLeft();
                player2.moveLeft();
                player2Action = true;
            }
            if (pressedKeys[39]) {
                player2.faceRight();
                player2.moveRight();
                player2Action = true;
            }
        }

        if (!player2Action) {
            player2.setIdle(true);
        } else {
            player2.setIdle(false);
        }



        // player2 moves up (W)
//            if (pressedKeys[38]) {
//                player2.moveUp();
//            }

            // player2 moves down (S)
//            if (pressedKeys[40]) {
//                player2.moveDown();
//            }


        if (pressedKeys[69]) { // E key
            if (!player.isPunching()) {
                player.punch();
                punchTimer.start();
                if (player.playerRect().intersects(player2.playerRect())) { // check for collision
                    player2.setHealth(5);
                    if(player2.getHealth() == 0){
                        songClip.stop();
                        songClip.close();
                        ryuWin();
                    }
                }
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

            Point mouseClickLocation = e.getPoint();
            if (player.playerRect().contains(mouseClickLocation)) {
                player.turn();
            }
            if (player2.playerRect().contains(mouseClickLocation)) {
                player2.turn();
            }

    }

    public void mouseEntered(MouseEvent e) { } // unimplemented

    public void mouseExited(MouseEvent e) { } // unimplemented

    public void ryuWin(){
        EndFrameRyu r = new EndFrameRyu();
    }
    public void bisonWin(){
        EndFrameBison r = new EndFrameBison();
    }

    private void playMusic() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/Assets/humannature.wav").getAbsoluteFile());
            songClip = AudioSystem.getClip();
            songClip.open(audioInputStream);
            songClip.loop(Clip.LOOP_CONTINUOUSLY);  // song repeats when finished
            songClip.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


}
