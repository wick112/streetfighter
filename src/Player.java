import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Player {
    private final double MOVE_AMT = 0.2;
    private BufferedImage right;
    private BufferedImage left;
    private BufferedImage punchrightMove;
    private BufferedImage punchleftMove;
    private boolean facingRight;
    private double xCoord;
    private double yCoord;
    private int score;
    private int health;
    private BufferedImage currentImage;

    private boolean isPunching;

    private Animation run;
    private Animation punch;

    public Player(String rightImg, String punchrightImg, String punchleftImg) {
        facingRight = true;
        xCoord = 50; // starting position is (50, 435), right on top of ground
        yCoord = 360;
        score = 0;
        health = 5;
        try {
//            left = ImageIO.read(new File(leftImg));
            right = ImageIO.read(new File(rightImg));
            punchleftMove = ImageIO.read(new File(punchleftImg));
            punchrightMove = ImageIO.read(new File(punchrightImg));

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        currentImage = right;

        ArrayList<BufferedImage> punch_animation = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            String filename = "src/RyuAniPunch/punch_" + i + ".png";
            try {
                punch_animation.add(ImageIO.read(new File(filename)));
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        punch = new Animation(punch_animation,200);


        ArrayList<BufferedImage> run_animation = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            String filename = "src/RyuAni/run_" + i + ".png";
            try {
                run_animation.add(ImageIO.read(new File(filename)));
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        run = new Animation(run_animation,200);


        isPunching = false;
    }

    public int getxCoord() {
        if (facingRight) {
            return (int) xCoord;
        } else {
            return (int) (xCoord + (getPlayerImage().getWidth()));
        }
    }

    public int getyCoord() {
        return (int) yCoord;
    }

    public int getScore() {
        return score;
    }

    public void faceRight() {
        facingRight = true;
//        currentImage = right;
    }

    public void faceLeft() {
        facingRight = false;
//        currentImage = left;
    }

    public boolean ifFacingRight(){
        return facingRight;
    }

    public void moveRight() {
        if (xCoord + MOVE_AMT <= 920) {
            xCoord += MOVE_AMT;
        }
    }

    public void moveLeft() {
        if (xCoord - MOVE_AMT >= 0) {
            xCoord -= MOVE_AMT;
        }
    }

    public void turn() {
        if (facingRight) {
            faceLeft();
        } else {
            faceRight();
        }
    }

    public void setHealth(int newH){
        health -= newH;
    }

    public int getHealth(){
        return health;
    }

    public void moveUp() {
        if (yCoord - MOVE_AMT >= 0) {
            yCoord -= 10;
        }
    }

    public void moveDown() {
        if (yCoord + MOVE_AMT <= 435) {
            yCoord += 10;
        }
    }

    public void collectCoin() {
        score++;
    }


    public void punch() {
        isPunching = true;
    }

    public void resetPunch() {
        isPunching = false;
    }

    public boolean isPunching(){
        return isPunching;
    }


    public BufferedImage getPlayerImage() {
        if (isPunching) {
//            if (!facingRight) {
//                return punchleftMove;
//            } else{
//                return punchrightMove;
//            }
            return punch.getActiveFrame();
        } else {
            return run.getActiveFrame();
        }
    }

    //These functions are newly added to let the player turn left and right
    //These functions when combined with the updated getxCoord()
    //Allow the player to turn without needing separate images for left and right
    public int getHeight() {
        return getPlayerImage().getHeight();
    }

//    public void resetPunch() {
//        currentImage = facingRight ? right : left;
//    }

    // we use a "bounding Rectangle" for detecting collision

    public int getWidth() {
        if (facingRight) {
            return getPlayerImage().getWidth();
        } else {
            return getPlayerImage().getWidth() * -1;
        }
    }
    public Rectangle playerRect() {
        int imageHeight = getPlayerImage().getHeight();
        int imageWidth = getPlayerImage().getWidth();
        return new Rectangle((int) xCoord, (int) yCoord, imageWidth, imageHeight);
    }
}
