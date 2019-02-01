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

class RoundedCornerButtonUI extends BasicButtonUI {
    private static final float arcwidth  = 16.0f;
    private static final float archeight = 16.0f;
    protected static final int focusstroke = 2;
    protected final Color fc = new Color(100,150,255,200);
    protected final Color ac = new Color(230,230,230);
    protected final Color rc = Color.ORANGE;
    protected Shape shape;
    protected Shape border;
    protected Shape base;

    @Override protected void installDefaults(AbstractButton b) {
        super.installDefaults(b);
        b.setContentAreaFilled(false);
        b.setOpaque(false);
        b.setBackground(new Color(250, 250, 250));
        initShape(b);
    }
    @Override protected void installListeners(AbstractButton b) {
        BasicButtonListener listener = new BasicButtonListener(b) {
            @Override public void mousePressed(MouseEvent e) {
                AbstractButton b = (AbstractButton) e.getSource();
                initShape(b);
                if(shape.contains(e.getX(), e.getY())) {
                    super.mousePressed(e);
                }
            }
            @Override public void mouseEntered(MouseEvent e) {
                if(shape.contains(e.getX(), e.getY())) {
                    super.mouseEntered(e);
                }
            }
            @Override public void mouseMoved(MouseEvent e) {
                if(shape.contains(e.getX(), e.getY())) {
                    super.mouseEntered(e);
                }else{
                    super.mouseExited(e);
                }
            }
        };
        if(listener != null) {
            b.addMouseListener(listener);
            b.addMouseMotionListener(listener);
            b.addFocusListener(listener);
            b.addPropertyChangeListener(listener);
            b.addChangeListener(listener);
        }
    }
    @Override public void paint(Graphics g, JComponent c) {
        Graphics2D g2 = (Graphics2D)g;
        AbstractButton b = (AbstractButton) c;
        ButtonModel model = b.getModel();
        initShape(b);
        //ContentArea
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        if(model.isArmed()) {
            g2.setColor(ac);
            g2.fill(shape);
        }else if(b.isRolloverEnabled() && model.isRollover()) {
            paintFocusAndRollover(g2, c, rc);
        }else if(b.hasFocus()) {
            paintFocusAndRollover(g2, c, fc);
        }else{
            g2.setColor(c.getBackground());
            g2.fill(shape);
        }
        //Border
        g2.setPaint(c.getForeground());
        g2.draw(shape);

        g2.setColor(c.getBackground());
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);
        super.paint(g2, c);
    }
    private void initShape(JComponent c) {
        if(!c.getBounds().equals(base)) {
            base = c.getBounds();
            shape = new RoundRectangle2D.Float(0, 0, c.getWidth()-1, c.getHeight()-1,
                    arcwidth, archeight);
            border = new RoundRectangle2D.Float(focusstroke, focusstroke,
                    c.getWidth()-1-focusstroke*2,
                    c.getHeight()-1-focusstroke*2,
                    arcwidth, archeight);
        }
    }
    private void paintFocusAndRollover(Graphics2D g2, JComponent c, Color color) {
        g2.setPaint(new GradientPaint(0, 0, color, c.getWidth()-1, c.getHeight()-1,
                color.brighter(), true));
        g2.fill(shape);
        g2.setColor(c.getBackground());
        g2.fill(border);
    }
}

public class Menu extends JPanel {
    int screenw, screenh;
    private static Image buffer;
    private static Graphics bg;
    private String action = "NoAction";

    public Menu(int screenw, int screenh) {
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
            image = ImageIO.read(new File("C://Users//Sina//Desktop//BackGround1.jpg"));
            Image scaledImage = image.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
            bg.drawImage(scaledImage, 0, 0, null);

        } catch (IOException e) {
            e.printStackTrace();
        }

        JButton button1 = new JButton("New Game");
        button1.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        button1.setBounds(300,75,200,50);
        button1.setVisible(true);
        button1.setBackground(Color.pink);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action = "New Game";
            }
        });
        this.add(button1);

        JButton button2 = new JButton("Load Game");
        button2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        button2.setBounds(300,175,200,50);
        button2.setVisible(true);
        button2.setBackground(Color.pink);
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action = "Load Game";
            }
        });
        this.add(button2);

        JButton button3 = new JButton("Create Costume");
        button3.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        button3.setBounds(300,275,200,50);
        button3.setVisible(true);
        button3.setBackground(Color.pink);
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action = "Create Costume";
            }
        });
        this.add(button3);

        JButton button4 = new JButton("Options");
        button4.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        button4.setBounds(300,375,200,50);
        button4.setVisible(true);
        button4.setBackground(Color.pink);
        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action = "Options";
            }
        });
        this.add(button4);

        JButton button5 = new JButton("Exit");
        button5.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        button5.setBounds(300,475,200,50);
        button5.setVisible(true);
        button5.setBackground(Color.pink);
        this.add(button5);
        button5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //what do we want to happen when we
                //click the button
                int dialogButton = JOptionPane.YES_NO_OPTION;
                JOptionPane.showConfirmDialog(null, "Do you want to  close", "Warning", dialogButton);
                if (dialogButton == JOptionPane.YES_OPTION) { //The ISSUE is here
                    System.exit(0);
                }
            }
        });

        g.drawImage(buffer, 0, 0,  null);

    }
}
