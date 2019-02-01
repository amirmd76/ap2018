import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class NewGame extends JPanel implements ActionListener {
    int screenw, screenh;
    private static Image buffer;
    private static Graphics bg;
    private String action = "NoAction";
    private JTextField field;
    private JTextArea textArea;
    private String nickname = null;

    public NewGame(int screenw, int screenh) {
        super(new GridBagLayout());
        this.screenw = screenw;
        this.screenh = screenh;
        setPreferredSize(new Dimension(screenw, screenh));
    }
    public NewGame() {
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        buffer = createImage(screenw, screenh);
        bg = buffer.getGraphics();

        bg.setColor(Color.WHITE);
        bg.fillRect(0, 0, screenw, screenh);

        BufferedImage image;
        try {
            image = ImageIO.read(new File("C://Users//Sina//Desktop//BackGround2.jpg"));
            Image scaled = image.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
            bg.drawImage(scaled, 0, 0, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        g.drawImage(buffer, 0, 0,  null);

        JLabel label = new JLabel("Enter your nickname");
        label.setBounds(90,0,250,100);
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        add(label);
        field = new JTextField(10);
        field.addActionListener(this);
        field.setBounds(50,100,300,100);
        field.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 20));
        add(field);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.nickname = field.getText();
        this.action = "NicknameEntered";
    }
}
