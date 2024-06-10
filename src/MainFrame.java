import javax.swing.*;

public class MainFrame implements Runnable {

    private GraphicsPanel panel;
    private JFrame frame;

    public MainFrame() {
        frame = new JFrame("Street Fighter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(960, 575); // 540 height of image + 40 for window menu bar
        frame.setLocationRelativeTo(null); // auto-centers frame in screen

        // create and add panel
        panel = new GraphicsPanel(this);
        frame.add(panel);

        // display the frame
        frame.setVisible(true);

        // start thread, required for animation
        Thread thread = new Thread(this);
        thread.start();
    }

    public void run() {
        while (true) {
            panel.repaint();  // we don't ever call "paintComponent" directly, but call this to refresh the panel
        }
    }

    public void disposeFrame() {
        frame.setVisible(false);
        frame.dispose();
    }

    public void reviveFrame() {
        frame.setVisible(true);
    }
}
