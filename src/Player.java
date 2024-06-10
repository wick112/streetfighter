import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Player {
    private final double MOVE_AMT = 0.4;
    private final double JUMP_AMT = 2;
    private final double GRAVITY = 0.5;
    private BufferedImage right;
    private BufferedImage left;
    private BufferedImage punchrightMove;
    private BufferedImage punchleftMove;
    private boolean facingRight;
    private double xCoord;
    private double yCoord;
    private double groundYCoord;
    private int tagTeam;
    private int health;
    private BufferedImage currentImage;

    private boolean isPunching;
    private boolean isIdle;
    private boolean isJumping;
    private boolean isFalling;
    private boolean isJumpingRight;
    private boolean isJumpingLeft;


    private Animation run;
    private Animation punch;
    private Animation idle;
    private Animation jump;

    public Player(String rightImg, String punchrightImg, String punchleftImg) {
        tagTeam = 0;
        facingRight = true;
        xCoord = 50; // starting position is (50, 435), right on top of ground
        groundYCoord = 350;
        yCoord = groundYCoord;
        health = 20;
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
            String filename = "src/RyuAni/RyuAniPunch/punch_" + i + ".png";
            try {
                punch_animation.add(ImageIO.read(new File(filename)));
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        punch = new Animation(punch_animation,200);

        ArrayList<BufferedImage> jump_animation = new ArrayList<>();
        for (int i = 2; i <= 7; i++) {
            String filename = "src/RyuAniJump/jump_" + i + ".png";
            try {
                jump_animation.add(ImageIO.read(new File(filename)));
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        jump = new Animation(jump_animation,200);
        isJumping = false;
        isFalling = false;

        ArrayList<BufferedImage> idle_animation = new ArrayList<>();
        for (int i = 1; i <= 1; i++) {
            String filename = "src/RyuAni/ryuIdle.png";
            try {
                idle_animation.add(ImageIO.read(new File(filename)));
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        idle = new Animation(idle_animation,200);
        isIdle = true;


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
        isIdle = false;
    }

    public void moveLeft() {
        if (xCoord - MOVE_AMT >= 0) {
            xCoord -= MOVE_AMT;
        }
        isIdle = false;
    }

    public void turn() {
        if (facingRight) {
            faceLeft();
        } else {
            faceRight();
        }
    }

    public void minusHealth(int newH){
        health -= newH;
        if(health <= 0){
            health = 0;
        }
    }

    public int getHealth(){
        return health;
    }

    public void moveUp() {
        if (yCoord - MOVE_AMT >= 0) {
            yCoord -= 120;
        }
        isIdle = false;
    }

    public void moveDown() {
        if (yCoord + MOVE_AMT <= 435) {
            yCoord += 120;
        }
        isIdle = false;
    }


    public void jump() {
        isJumping = true;
        isFalling = false;
    }

    public void fall() {
        isJumping = false;
        isFalling = true;
    }


    public void resetJump() {
        isJumping = false;
    }

    public boolean isJumping() {
        return isJumping;
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

    public void setIdle(boolean idle) {
        this.isIdle = idle;
    }

    public boolean isIdle() {
        return isIdle;
    }

    public void updateJumpingState() {
        if (isJumping) {
            if (yCoord > groundYCoord - 100) { // move up until 100 units above the ground
                moveUp();
                if (isJumpingRight) {
                    moveRight();
                }
                if (isJumpingLeft) {
                    moveLeft();
                }
            } else {
                fall();
            }
        } else if (isFalling) {
            if (yCoord < groundYCoord) { // move down until back on the ground
                moveDown();
                if (isJumpingRight) {
                    moveRight();
                }
                if (isJumpingLeft) {
                    moveLeft();
                }
            } else {
                resetJump();
            }
        }
    }

    public void setJumpingDirection(boolean right, boolean left) {
        isJumpingRight = right;
        isJumpingLeft = left;
    }


    public BufferedImage getPlayerImage() {
        if (isPunching) {
            return punch.getActiveFrame();
        } else if (isJumping) {
            return jump.getActiveFrame();
        }else if (isIdle) {
            return idle.getActiveFrame();
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

    public int getTagTeam(){
        return tagTeam;
    }

    public void addTagTeam(){
        tagTeam += 5;
    }

    public void resetTagTeam(){
        tagTeam = 0;
    }
}
