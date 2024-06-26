import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class GraphicsPanel extends JPanel implements KeyListener, MouseListener {
    private BufferedImage background;
    private BufferedImage koBar;
    private BufferedImage fight;
    private BufferedImage freePlay;


    private Player player;
    private Player2 player2;
    private boolean[] pressedKeys;
    private Timer punchTimer;
    private Timer chunliTimer;
    private Timer zangeifTimer;
    private Timer ryuSpecialTimer;
    private Timer bisonSpecialTimer;



    private Timer punchTimer2;
    private Timer kickTimer;
    private Timer kickTimer2;
    private Timer jumpTimer;
    private Timer jump2Timer;
    private Timer timer;
    private Timer timer2;
    private Timer timer3;
    private Timer timer4;


    private Timer playerJumpUpdater;
    private Timer player2JumpUpdater;

    private MainFrame frame;
    private boolean isAnimating;
    private ChunliFrame f; // Reference to ChunliFrame instance
    private ZangeifFrame z; // Reference to ChunliFrame instance

    private RspecialFrame r;
    private BspecialFrame b;


    private Clip songClip;



    public GraphicsPanel(MainFrame frame) {
        this.frame = frame;
        try {
            background = ImageIO.read(new File("src/Assets/fightbackground.png"));
            koBar = ImageIO.read(new File("src/Assets/koBar.png"));
            freePlay = ImageIO.read(new File("src/Assets/freePlay.png"));
            fight = ImageIO.read(new File("src/Assets/fight.png"));
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

        jumpTimer = new Timer(600, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.resetJump();
                jumpTimer.stop();
                repaint();
            }
        });
        jumpTimer.setRepeats(false);

        playerJumpUpdater = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.updateJumpingState();
                repaint();
            }
        });
        playerJumpUpdater.start();

        jump2Timer = new Timer(600, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player2.resetJump();
                jump2Timer.stop();
                repaint();
            }
        });
        jump2Timer.setRepeats(false);

        player2JumpUpdater = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player2.updateJumpingState();
                repaint();
            }
        });
        player2JumpUpdater.start();

        chunliTimer = new Timer(8300, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.disposeFrame();
            }
        });
        chunliTimer.setRepeats(false); // Set to not repeat

        timer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f = new ChunliFrame();
                chunliTimer.start();

                timer.stop(); // stop this delay timer
                player.resetTagTeam();
            }
        });
        timer.setRepeats(false); // Set to not repeat

        zangeifTimer = new Timer(10800, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                z.disposeFrame();
            }
        });
        zangeifTimer.setRepeats(false); // Set to not repeat

        timer2 = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                z = new ZangeifFrame();
                zangeifTimer.start();

                timer2.stop(); // stop this delay timer
                player2.resetTagTeam();
            }
        });
        timer2.setRepeats(false); // Set to not repeat


        ryuSpecialTimer = new Timer(6900, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                r.disposeFrame();
            }
        });
        ryuSpecialTimer.setRepeats(false); // Set to not repeat

        timer3 = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                r = new RspecialFrame();
                ryuSpecialTimer.start();

                timer3.stop(); // stop this delay timer
                player.resetSpecial();
            }
        });
        timer3.setRepeats(false); // Set to not repeat

        bisonSpecialTimer = new Timer(9300, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                b.disposeFrame();
            }
        });
        bisonSpecialTimer.setRepeats(false); // Set to not repeat

        timer4 = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                b = new BspecialFrame();
                bisonSpecialTimer.start();

                timer4.stop(); // stop this delay timer
                player2.resetSpecial();
            }
        });
        timer4.setRepeats(false); // Set to not repeat

        kickTimer = new Timer(800, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.resetKick();
                kickTimer.stop();
                repaint();
            }
        });
        kickTimer.setRepeats(false);

        kickTimer2 = new Timer(600, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player2.resetKick();
                kickTimer2.stop();
                repaint();
            }
        });
        kickTimer2.setRepeats(false);

        playMusic();

    }

    @Override
    public void paintComponent(Graphics g) {
        boolean playerAction = false;
        boolean player2Action = false;

        super.paintComponent(g);  // just do this
        g.drawImage(background, 0, 0, null);  // the order that things get "painted" matter; we put background down first
        g.drawImage(koBar, 140, 30, null);  // the order that things get "painted" matter; we put background down first
        g.drawImage(freePlay, 0, 0, null);  // the order that things get "painted" matter; we put background down first
        g.drawImage(fight, 400, 65, null);  // the order that things get "painted" matter; we put background down first
        g.drawImage(player.getPlayerImage(), player.getxCoord(), player.getyCoord(), player.getWidth(), player.getHeight(), null);
        g.drawImage(player2.getPlayerImage(), player2.getxCoord(), player2.getyCoord(), player2.getWidth(), player2.getHeight(), null);

        // draw score
        g.setFont(new Font("Courier New", Font.BOLD, 24));
        g.drawString("Ryu Health: " + player.getHealth(), 175, 57);
        g.drawString("M. Bison Health: " + player2.getHealth(), 550, 57);
        if(player.getSpecial() >= 30){
            g.drawString("Ryu Special Ready", 100, 90);
        }else if(player.getSpecial() <= 30){
            g.drawString("Ryu Special Not Ready", 100, 90);
        }
        if(player2.getSpecial() >= 30){
            g.drawString("Bison Special Ready", 570, 90);
        }else if(player2.getSpecial() <= 30){
            g.drawString("Bison Special Not Ready", 570, 90);
        }

        if(player.getTagTeam() >= 30){
            g.drawString("Tag Team Ready", 100, 115);
        }else if(player.getTagTeam() <= 30){
            g.drawString("Tag Team Not Ready", 100, 115);
        }
        if(player2.getTagTeam() >= 30){
            g.drawString("Tag Team Ready", 570, 115);
        }else if(player2.getTagTeam() <= 30){
            g.drawString("Tag Team Not Ready", 570, 115);
        }

        if (pressedKeys[17]) { // E key for player 2 punch
            if (!player2.isPunching()) {
                player2.punch();
                punchTimer2.start();
                if (player2.playerRect().intersects(player.playerRect())) {
                    if (!player.isJumping()) {
                        if (player2.ifFacingRight() && player2.playerRect().x < player.playerRect().x) {
                            player2.addTagTeam();
                            player2.addSpecial();
                            player.minusHealth(5);
                        } else if (!player2.ifFacingRight() && player2.playerRect().x > player.playerRect().x) {
                            player2.addTagTeam();
                            player2.addSpecial();
                            player.minusHealth(5);
                        }
                        if (player.getHealth() <= 0) {
                            songClip.stop();
                            songClip.close();
                            bisonWin();
                        }
                    }
                }
            }
            player2Action = true;
        } else if (pressedKeys[40]) { // E key for punch
            if (!player2.isKicking()) {
                player2.kick();
                kickTimer2.start();
                if (player2.playerRect().intersects(player.playerRect())) {
                    if (!player.isJumping()) {
                        if ((player2.ifFacingRight() && player2.playerRect().x < player.playerRect().x)) {
                            player2.addTagTeam();
                            player2.addSpecial();
                            player.minusHealth(7);
                        }
                        else if (!player2.ifFacingRight() && player2.playerRect().x > player.playerRect().x) {
                            player2.addTagTeam();
                            player2.addSpecial();
                            player.minusHealth(7);
                        }
                        if (player.getHealth() <= 0) {
                            songClip.stop();
                            songClip.close();
                            bisonWin();

                        }
                    }
                }
            }
            player2Action = true;
        }else {
            if (pressedKeys[37]) { // Left arrow key for player 2
                player2.faceLeft();
                player2.moveLeft();
                player2Action = true;
            }
            if (pressedKeys[39]) { // Right arrow key for player 2
                player2.faceRight();
                player2.moveRight();
                player2Action = true;
            }
            if (pressedKeys[38]) { // W key for jump
                if (!player2.isJumping()) {
                    boolean right = pressedKeys[39]; // W + D for right jump
                    boolean left = pressedKeys[37]; // W + A for left jump
                    player2.setJumpingDirection(right, left);
                    player2.jump();
                }
                player2Action = true;
            }
        }


        if (!player2Action) {
            player2.setIdle(true);
        } else {
            player2.setIdle(false);
        }

        if (pressedKeys[69]) { // E key for punch
            if (!player.isPunching()) {
                player.punch();
                punchTimer.start();
                if (player.playerRect().intersects(player2.playerRect())) {
                    if (!player2.isJumping()) {
                        if ((player.ifFacingRight() && player.playerRect().x < player2.playerRect().x)) {
                            player.addTagTeam();
                            player.addSpecial();
                            player2.minusHealth(5);
                        }
                        else if (!player.ifFacingRight() && player.playerRect().x > player2.playerRect().x) {
                            player.addTagTeam();
                            player.addSpecial();
                            player2.minusHealth(5);
                        }
                        if (player2.getHealth() <= 0) {
                            songClip.stop();
                            songClip.close();
                            ryuWin();

                        }
                    }
                }
            }
            playerAction = true;
        }else if (pressedKeys[83]) { // E key for punch
            if (!player.isKicking()) {
                player.kick();
                kickTimer.start();
                if (player.playerRect().intersects(player2.playerRect())) {
                    if (!player2.isJumping()) {
                        if ((player.ifFacingRight() && player.playerRect().x < player2.playerRect().x)) {
                            player.addTagTeam();
                            player.addSpecial();
                            player2.minusHealth(7);
                        }
                        else if (!player.ifFacingRight() && player.playerRect().x > player2.playerRect().x) {
                            player.addTagTeam();
                            player.addSpecial();
                            player2.minusHealth(7);
                        }
                        if (player2.getHealth() <= 0) {
                            songClip.stop();
                            songClip.close();
                            ryuWin();

                        }
                    }
                }
            }
            playerAction = true;
        }else {
            if (pressedKeys[65]) { // A key for left
                player.faceLeft();
                player.moveLeft();
                playerAction = true;
            }
            if (pressedKeys[68]) { // D key for right
                player.faceRight();
                player.moveRight();
                playerAction = true;
            }
            if (pressedKeys[87]) { // W key for jump
                if (!player.isJumping()) {
                    boolean right = pressedKeys[68]; // W + D for right jump
                    boolean left = pressedKeys[65]; // W + A for left jump
                    player.setJumpingDirection(right, left);
                    player.jump();
                }
                playerAction = true;
            }

        }


        if (!playerAction) {
            player.setIdle(true);
        } else {
            player.setIdle(false);
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
        if (pressedKeys[70]) {
            if(player.getTagTeam() >= 30){
                timer.start();
                player2.minusHealth(15);
                if (player2.getHealth() == 0) {
                    songClip.stop();
                    songClip.close();
                    ryuWin();

                }
            }

        }else if (pressedKeys[16]) {
            if(player2.getTagTeam() >= 30){
                timer2.start();
                player.minusHealth(15);
                if (player.getHealth() <= 0) {
                    songClip.stop();
                    songClip.close();
                    bisonWin();
                }
            }
        }else if (pressedKeys[81]) {
            if(player.getSpecial() >= 30){
                timer3.start();
                player2.minusHealth(15);
                if (player2.getHealth() <= 0) {
                    songClip.stop();
                    songClip.close();
                    ryuWin();
                }
            }
        }else if (pressedKeys[18]) {
            if(player2.getSpecial() >= 30){
                timer4.start();
                player.minusHealth(15);
                if (player.getHealth() <= 0) {
                    songClip.stop();
                    songClip.close();
                    bisonWin();
                }
            }
        }
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
        frame.disposeFrame();
    }
    public void bisonWin(){
        EndFrameBison r = new EndFrameBison();
        frame.disposeFrame();
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
