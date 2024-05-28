import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class EndPanelRyu extends JPanel implements ActionListener {

    private JTextField textField;
    private JButton startButton;
    private JFrame enclosingFrame;
    private BufferedImage background;
    private Clip songClip;

    public EndPanelRyu(JFrame frame) {
        enclosingFrame = frame;
        try {
            background = ImageIO.read(new File("src/Assets/ryuWin.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        playMusic();

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Courier ITALIC", Font.BOLD, 45));
        g.setColor(Color.RED);
        g.drawImage(background, 0, 0, null);
    }

    // ACTIONLISTENER INTERFACE METHODS
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            if (button == startButton) {
                MainFrame f = new MainFrame();
                enclosingFrame.setVisible(false);
                songClip.stop();
                songClip.close();

            }
        }
    }

    private void playMusic() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/Assets/win.wav").getAbsoluteFile());
            songClip = AudioSystem.getClip();
            songClip.open(audioInputStream);
            songClip.loop(Clip.LOOP_CONTINUOUSLY);  // song repeats when finished
            songClip.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}

