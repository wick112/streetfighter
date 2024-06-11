import javax.swing.*;

public class BspecialFrame {
    private BspecialPanel panel;
    private JFrame frame;

    public BspecialFrame() {
        frame = new JFrame("Street Fighter");
        // Other code remains the same
        frame.setSize(960, 575);
        frame.setLocationRelativeTo(null); // auto-centers frame in screen

        // create and add panel
        if (panel == null) {
            // create and add panel
            panel = new BspecialPanel(this); // Pass reference to ChunliFrame instance
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
    public BspecialPanel getPanel() {
        return panel;
    }
}
