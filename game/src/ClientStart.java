import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ClientStart extends JPanel implements ActionListener {
    int screenw, screenh;
    private static Image buffer;
    private static Graphics bg;
    private String action = "NoAction", IP = null;
    private JTextField field, field2;
    private JTextArea textArea;
    private String nickname = null;
    private int port;

    public ClientStart(int screenw, int screenh) {
        super(new GridBagLayout());
        this.screenw = screenw;
        this.screenh = screenh;
        setPreferredSize(new Dimension(screenw, screenh));
    }
    public ClientStart() {
        super(new GridBagLayout());
        this.screenw = 0;
        this.screenh = 0;
        setPreferredSize(new Dimension(screenw, screenh));
    }

    public String getAction() {
        return action;
    }

    public String getNickname() {
        return nickname;
    }

    public int getPort() { return port; }

    public String getIP() { return IP; }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        buffer = createImage(screenw, screenh);
        bg = buffer.getGraphics();

        bg.setColor(Color.WHITE);
        bg.fillRect(0, 0, screenw, screenh);

        BufferedImage image;
        try {
            image = ImageIO.read(new File("game/static/BackGround2.jpg"));
            Image scaled = image.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
            bg.drawImage(scaled, 0, 0, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        g.drawImage(buffer, 0, 0,  null);

        JLabel label = new JLabel("Enter Server IP Number");
        label.setBounds(90,0,250,100);
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        add(label);
        field = new JTextField(10);
        field.addActionListener(this);
        field.setBounds(50,100,300,100);
        field.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 20));
        add(field);

        JLabel label2 = new JLabel("Enter Server Port Number");
        label2.setBounds(90,200,250,100);
        label2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        add(label2);
        field2 = new JTextField(10);
        field2.addActionListener(this);
        field2.setBounds(50,300,300,100);
        field2.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 20));
        add(field2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.IP = field.getText();
        this.nickname = field2.getText();
        this.port = Integer.parseInt(nickname);
        this.action = "PortEntered";
    }
}
