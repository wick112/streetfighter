import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player {
    private final double MOVE_AMT = 0.2;
    private BufferedImage right;
    private BufferedImage left;
    private BufferedImage punchMove;
    private boolean facingRight;
    private double xCoord;
    private double yCoord;
    private int score;
    private BufferedImage prevImage;

    public Player(String leftImg, String rightImg, String punchImg) {
        facingRight = true;
        xCoord = 50; // starting position is (50, 435), right on top of ground
        yCoord = 435;
        score = 0;
        try {
            left = ImageIO.read(new File(leftImg));
            right = ImageIO.read(new File(rightImg));
            punchMove = ImageIO.read(new File(punchImg));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        prevImage = right;
    }

    public int getxCoord() {
        return (int) xCoord;
    }

    public int getyCoord() {
        return (int) yCoord;
    }

    public int getScore() {
        return score;
    }

    public void faceRight() {
        facingRight = true;
    }

    public void faceLeft() {
        facingRight = false;
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

    public void moveUp() {
        if (yCoord - MOVE_AMT >= 0) {
            yCoord -= MOVE_AMT;
        }
    }

    public void moveDown() {
        if (yCoord + MOVE_AMT <= 435) {
            yCoord += MOVE_AMT;
        }
    }


    public void collectCoin() {
        score++;
    }

    public BufferedImage getPlayerImage() {
        if (facingRight) {
            return right;
        } else {
            return left;
        }
    }

    public void punch() {

        // Set punch image
        if (facingRight) {
            prevImage = right;
            right = punchMove;
        }

    }

    public void resetImage() {
        // Reset image to previous state
        if (facingRight) {
            right = prevImage;
        } else {
            left = prevImage;
        }
    }

    // we use a "bounding Rectangle" for detecting collision
    public Rectangle playerRect() {
        int imageHeight = getPlayerImage().getHeight();
        int imageWidth = getPlayerImage().getWidth();
        Rectangle rect = new Rectangle((int) xCoord, (int) yCoord, imageWidth, imageHeight);
        return rect;
    }
}
