import javax.swing.*;

public class EndFrameBison {
    private EndPanelBison panel;

    public EndFrameBison() {
        JFrame frame = new JFrame("Street Fighter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650, 650);
        frame.setLocationRelativeTo(null); // auto-centers frame in screen

        // create and add panel
        panel = new EndPanelBison(frame);
        frame.add(panel);

        // display the frame
        frame.setVisible(true);

        // no thread needed here since we aren't doing animation for this frame/panel
    }
}

