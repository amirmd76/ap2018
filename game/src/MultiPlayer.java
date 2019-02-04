import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonListener;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

public class MultiPlayer extends JPanel {
    int screenw, screenh;
    private static Image buffer;
    private static Graphics bg;
    private String action = "NoAction";

    public MultiPlayer(int screenw, int screenh) {
        this.screenw = screenw;
        this.screenh = screenh;
        setPreferredSize(new Dimension(screenw, screenh));
    }
    public String getAction() {
        return action;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        buffer = createImage(screenw, screenh);
        bg = buffer.getGraphics();

        bg.setColor(Color.WHITE);
        bg.fillRect(0, 0, screenw, screenh);

        BufferedImage image;
        try {
            image = ImageIO.read(new File("game/static/BackGround1.jpg"));
            Image scaledImage = image.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
            bg.drawImage(scaledImage, 0, 0, null);

        } catch (IOException e) {
            e.printStackTrace();
        }

        g.drawImage(buffer, 0, 0,  null);

        JButton button1 = new JButton("Host");
        button1.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        button1.setBounds(300,225,200,50);
        button1.setVisible(true);
        button1.setBackground(Color.pink);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action = "Host";
            }
        });
        this.add(button1);

        JButton button2 = new JButton("Client");
        button2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        button2.setBounds(300,325,200,50);
        button2.setVisible(true);
        button2.setBackground(Color.pink);
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action = "Client";
            }
        });
        this.add(button2);



    }
}
