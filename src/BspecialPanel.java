import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BspecialPanel extends JPanel implements ActionListener {

    private JTextField textField;
    private JButton startButton;
    private BufferedImage background;
    private ImageIcon img;
    private JLabel l;
    private Clip songClip;

    private BspecialFrame frame;

    public BspecialPanel(BspecialFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        try {
            img = new ImageIcon("src/Assets/bisonSpecial.gif");
            l = new JLabel(img);
            add(l, BorderLayout.CENTER);
            background = ImageIO.read(new File("src/Assets/bisonSpecial.gif"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
//        playMusic();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Courier ITALIC", Font.BOLD, 45));
        g.setColor(Color.RED);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
//        if (e.getSource() instanceof JButton) {
//            JButton button = (JButton) e.getSource();
//            if (button == startButton) {
//                ChunliFrame f = new ChunliFrame();
//                frame.setVisible(false);
//                songClip.stop();
//                songClip.close();
//            }
//        }
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

//    public static void main(String[] args) {
//        ChunliFrame frame = new ChunliFrame();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(800, 600);
//        frame.add(new ChunliPanel(frame));
//        frame.setVisible(true);
//    }
}

