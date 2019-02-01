import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class InGameMenu extends JPanel implements ActionListener {
    int screenw, screenh;
    private static Image buffer;
    private static Graphics bg;
    private String action = "NoAction";
    private JTextField field;
    private JTextArea textArea;
    private String nickname = null;

    public InGameMenu() {
        super(new GridBagLayout());
        this.screenw = 250;
        this.screenh = 405;
        setPreferredSize(new Dimension(screenw, screenh));
    }

    public String getAction() {
        return action;
    }

    public String getNickname() {
        return nickname;
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
            image = ImageIO.read(new File("C://Users//Sina//Desktop//BackGround1.jpg"));
            bg.drawImage(image, 0, 0, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        g.drawImage(buffer, 0, 0,  null);

        JButton button1 = new JButton("Continue");
        button1.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        button1.setBounds(50,50,150,50);
        button1.setVisible(true);
        button1.setBackground(Color.pink);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action = "Continue";
            }
        });
        this.add(button1);

        JButton button2 = new JButton("Restart this level");
        button2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        button2.setBounds(50,135,150,50);
        button2.setVisible(true);
        button2.setBackground(Color.pink);
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action = "Restart";
            }
        });
        this.add(button2);

        JButton button3 = new JButton("Return to menu");
        button3.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        button3.setBounds(50,220,150,50);
        button3.setVisible(true);
        button3.setBackground(Color.pink);
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action = "Return";
            }
        });
        this.add(button3);

        JButton button4 = new JButton("Exit");
        button4.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        button4.setBounds(50,305,150,50);
        button4.setVisible(true);
        button4.setBackground(Color.pink);
        this.add(button4);
        button4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //what do we want to happen when we
                //click the button
                int dialogButton = JOptionPane.YES_NO_OPTION;
                JOptionPane.showConfirmDialog(null, "Do you want to  close", "Warning", dialogButton);
                if (!(dialogButton == JOptionPane.NO_OPTION)) { //The ISSUE is here
                    System.exit(0);
                }
            }
        });

        g.drawImage(buffer, 0, 0,  null);



    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.nickname = field.getText();
        this.action = "NicknameEntered";
    }
}
