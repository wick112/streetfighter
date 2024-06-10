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
public class ChunliFrame {
    private ChunliPanel panel;
    private JFrame frame;

    public ChunliFrame() {
        frame = new JFrame("Street Fighter");
        // Other code remains the same
        frame.setSize(960, 575);
        frame.setLocationRelativeTo(null); // auto-centers frame in screen

        // create and add panel
        if (panel == null) {
            // create and add panel
            panel = new ChunliPanel(this); // Pass reference to ChunliFrame instance
            frame.add(panel);
        }

        frame.setVisible(true);

    }

    // Method to dispose of the frame
    public void disposeFrame() {
        frame.setVisible(false);
        frame.dispose();
    }

    // Method to get the panel
    public ChunliPanel getPanel() {
        return panel;
    }
}
