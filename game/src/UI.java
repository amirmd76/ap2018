
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class UI extends JPanel {
    int screenw, screenh, gameWidth, gameHeight, sbMargin;
    private static Image buffer;
    private static final String baseFilesPath = "game/static/";
    private static Graphics bg;
    private Timer timer;
    private Image coin = null;
    private boolean isMultiPlayer = true;

    private  Game game;
    JButton sendButton, pvSendButton, closePVButton;
    JTextArea textArea, pvTextArea;

    private ArrayList<ClickCommand> commands = new ArrayList<>();


    private void click(int x, int y) {
        for(ClickCommand command: commands)
            if(command.wasClicked(x, y)) {
                String cmd = command.getCommand();
                if(command.getType().equals("prv")) {
                    String id = cmd;
                    openPV(id);
                }
                else {
                    game.runCommand(cmd);
                }
                break ;
            }
    }


    public UI(Game game, int screenw, int screenh) {
        this.isMultiPlayer = game.isMultiPlayer;
        this.game = game;
        this.gameWidth = screenw;
        this.gameHeight = screenh;
        this.sbMargin = screenh/2;
        if(isMultiPlayer)
            this.screenw = screenw + 500;
        else
            this.screenw = screenw;
        this.screenh = screenh;

        if(isMultiPlayer) {
            textArea = new JTextArea(1, 1);
            pvTextArea = new JTextArea(1, 1);
            sendButton = new JButton("Send");
            closePVButton = new JButton("X");
            pvSendButton = new JButton("Send private message");

            add(textArea);
            add(sendButton);

            add(pvTextArea);
            add(pvSendButton);
            add(closePVButton);
            pvSendButton.setVisible(false);
            pvTextArea.setVisible(false);
            closePVButton.setVisible(false);
            sendButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String text = textArea.getText().trim();
                    if(text.isEmpty())  return;
                    textArea.setText("");
                    textArea.grabFocus();
                    String msg = game.getCurrentController().player.getNameID() + ": " + text;
                    game.commands.add("chat");
                    game.textInput.add(msg);
                }
            });
            pvSendButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String text = pvTextArea.getText().trim();
                    if(text.isEmpty())  return;
                    pvTextArea.setText("");
                    pvTextArea.grabFocus();
                    String msg = game.getCurrentController().player.getNameID() + ": " + text;
                    game.commands.add("privateChat");
                    game.commands.add(inPV);
                    game.privateTextInput.add(msg);
                }
            });
            closePVButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    closePV();
                }
            });
        }
        try {
            image = ImageIO.read(new File(baseFilesPath + "/coin.png"));
            coin = image.getScaledInstance(15, 15, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastUpdate = System.nanoTime();
        setPreferredSize(new Dimension(this.screenw, this.screenh));
                addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                click(x, y);
            }

            public void mouseEntered(MouseEvent arg0) {
            }

            public void mouseExited(MouseEvent arg0) {
            }

            public void mousePressed(MouseEvent arg0) {
            }

            public void mouseReleased(MouseEvent arg0) {
            }
        });
        ActionListener animation = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                repaint();
                if((System.nanoTime() - lastUpdate) > (long)1e8) {
                    lastUpdate = System.nanoTime();
                    game.runCommand("turn 1");
                }
            }
        };
        timer = new Timer(1, animation);
        timer.start();
    }

//    private static BufferedImage colorImage(BufferedImage image) {
//        int width = image.getWidth();
//        int height = image.getHeight();
//        WritableRaster raster = image.getRaster();
//
//        for (int xx = 0; xx < width; xx++) {
//            for (int yy = 0; yy < height; yy++) {
//                int[] pixels = raster.getPixel(xx, yy, (int[]) null);
//                pixels[0] += 10;
//                pixels[1] += 10;
//                pixels[2] += 10;
//                raster.setPixel(xx, yy, pixels);
//            }
//        }
//        return image;
//    }
    private static BufferedImage colorImage(BufferedImage image) {
        BufferedImage imageAltered = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        int brightness = 50;//rand.nextInt(150 + 200 + 1) - 200; //values from 150 to 200
        double contrast = 1.0; //+ (5.0 - 1.5) * rand.nextDouble(); //values from 1.5 to 5.0

        for(int i = 0; i < image.getWidth(); i++) {
            for(int j = 0; j < image.getHeight(); j++) {
                int pixel = image.getRGB(i,j);
                int alpha = (pixel>>24) & 0xff;
                Color c = new Color(image.getRGB(i, j));
                int red = (int) contrast * c.getRed() + brightness;
                int green = (int) contrast * c.getGreen() + brightness;
                int blue = (int) contrast * c.getBlue() + brightness;

                if(red > 255) { // the values of the color components must be between 0-255
                    red = 255;
                } else if(red < 0) {
                    red = 0;
                }
                if(green > 255) {
                    green = 255;
                } else if(green < 0) {
                    green = 0;
                }
                if(blue > 255) {
                    blue = 255;
                } else if(blue < 0) {
                    blue = 0;
                }
                if(alpha < 0xff)
                    imageAltered.setRGB(i, j, pixel);
                else
                    imageAltered.setRGB(i, j, new Color(red, green, blue).getRGB());
            }
        }
        return imageAltered;
    }


    private static BufferedImage colorImage(Image image) {
        return colorImage(toBufferedImage(image));
    }

    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }
    BufferedImage image;

    private Pair<Integer, Integer> getCellLocation(int i, int j) {
        return new Pair<>(336 + 48 * j, 264 + 48 * i);
    }

    private BufferedImage horizontalFlip(BufferedImage image) {
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-image.getWidth(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(image, null);
    }
    private boolean terminate = false;
    private BufferedImage getAnimal(String type, int dx, int dy, int stage) {
        if(type.equals("Bear") || type.equals("Lion"))
            terminate = true;
        String dir = "";
        if(dx > 0)
            dir = "down";
        else if(dx < 0)
            dir = "up";
        boolean flip = false;

        if(dy < 0)
            dir = dir.isEmpty()? "left": dir + "_left";
        else if(dy > 0) {
            flip = true;
            dir = dir.isEmpty()? "left": dir + "_left";
        }
        if(dir.isEmpty())
            dir = "left";
        try {
            image = ImageIO.read(new File(baseFilesPath + "Animals/Africa/" + type + "/" + dir + "/" + String.valueOf(stage) + ".png"));
            if(flip)
                return horizontalFlip(toBufferedImage(image));
            return toBufferedImage(image);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void paintAnimal(Animal animal, Pair<Integer, Integer> location) {
        BufferedImage image = getAnimal(animal.getType(), Math.toIntExact(animal.direction.x), Math.toIntExact(animal.direction.y), animal.stage); // fix stage
        animal.stage = (animal.stage + 1) % 24;
        int w = image.getWidth();
        int h = image.getHeight();
        bg.drawImage(image, location.x - w/2, location.y - h/2, null);
    }

    long lastUpdate = 0;

    private void fillCircle(int cx, int cy, int rr) {
        bg.fillOval(cx - rr/2, cy - rr/2, rr, rr);

    }

    private void paintIcon(String animal, int x, int y) {
        try {
            image = ImageIO.read(new File(baseFilesPath + "UI/Icons/Products/" + animal + ".png"));
            int w = image.getWidth(), h = image.getHeight();
            int rr = Math.max(w, h), RR = 2 + rr;
            int cx = x + RR/2, cy = y + RR/2;
            int X = cx - w/2, Y = cy - h/2;
            bg.setColor(Color.LIGHT_GRAY);
            commands.add(new ClickCommand(cx - RR/2, cy - RR/2, RR, RR, "buy " + animal));
            fillCircle(cx, cy, RR);
            bg.setColor(Color.cyan);
            fillCircle(cx, cy, rr);
//            bg.fillOval(cx - rr/2, cy - rr/2, rr, rr);
            bg.drawImage(image, X, Y, null);
            bg.setColor(new Color(Integer.parseInt("1E90FF", 16)));
            int badgeX = x, badgeY = y + RR - 5, badgeW = RR, badgeH = 20;
            bg.fillRect(badgeX, badgeY, badgeW, badgeH);
            bg.setColor(Color.white);
            bg.drawString("10", badgeX + 2, badgeY+15);
            bg.drawImage(coin, badgeX + badgeW - 17, badgeY + badgeH - 17, null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void paintIcons() {
        int X = 50, Y = 30, DX = 70;
        String[] types = {"cat", "dog", "cow", "sheep", "turkey", "chicken"};
        for(int i = 0; i < types.length; ++ i)
            paintIcon(types[i], X + DX * i, Y);
    }


    private void drawNames() {
        if(inPV != null) {
            drawPrivateChat();
            return;
        }
        bg.setColor(Color.RED);
        Font font = new Font("Arial", Font.PLAIN, 15);
        FontMetrics fontMetrics = bg.getFontMetrics(font);
        bg.setFont(font);
        int mx = gameWidth + 10, imx = mx;
        int W = screenw - gameWidth;
        int my = 20, imy = my;
        int state = 0;
        for(String id: game.connectedPlayers.keySet()) {
            if(my >= sbMargin && state == 1)  break;
            if(my >= sbMargin) {
                state = 1;
                mx += W/2 + 3;
                my = imy;
            }
            String s = game.connectedPlayers.get(id).getName();
            int cw = fontMetrics.stringWidth(s), ch = fontMetrics.getHeight();
            commands.add(new ClickCommand(mx, my-ch, cw, ch, id, "prv" ));
            bg.drawString(s, mx, my);
            my += 20;
        }
        bg.setColor(Color.black);
        bg.drawLine(gameWidth + W/2, 0, gameWidth + W/2, sbMargin);
        bg.drawLine(gameWidth, sbMargin, screenw, sbMargin);
    }

    public String inPV = null;

    private void openPV(String id) {
        pvTextArea.setVisible(true);
        pvSendButton.setVisible(true);
        closePVButton.setVisible(true);
        inPV = id;
    }

    private void closePV() {
        pvSendButton.setVisible(false);
        pvTextArea.setVisible(false);
        closePVButton.setVisible(false);
        game.privateChat.clear();
        inPV = null;
    }

    private void drawPrivateChat() {
        int sz = 30;

        Font font = new Font("Arial", Font.PLAIN, 15);
        bg.setFont(font);
        FontMetrics fontMetrics = bg.getFontMetrics(font);
        int mx = gameWidth + 10;
        int my = sbMargin-2*sz-8;

        for(int i = game.privateChat.size(); i >= 0; -- i){
            if(my - fontMetrics.getHeight() <= 20)  break;
            String rawMsg = game.chatRoom.get(i);
            int colon = rawMsg.indexOf(':');
            String id = rawMsg.substring(0, colon).trim();
            String msg = rawMsg.substring(colon+1).trim();
            String name = game.connectedPlayers.get(id).getName();
            String x = name + ": ";
            String y = msg;
            int cw = fontMetrics.stringWidth(x);
            bg.setColor(Color.RED);
            bg.drawString(x, mx, my);
            bg.setColor(Color.black);
            bg.drawString(y, mx + cw, my);
            my -= 20;
        }

        bg.setColor(Color.black);
        bg.drawLine(gameWidth, sbMargin, screenw, sbMargin);
        pvTextArea.setSize(screenw-gameWidth, sz);
        bg.drawLine(gameWidth, sbMargin-2*sz-2, screenw, sbMargin-2*sz-2);
        pvTextArea.setLocation(gameWidth+2, sbMargin-2*sz);
        pvSendButton.setSize(screenw-gameWidth, sz);
        pvSendButton.setLocation(gameWidth, sbMargin-sz);
        closePVButton.setFont(new Font("Arial", Font.PLAIN, 10));
        closePVButton.setSize(40, 40);
        closePVButton.setLocation(screenw-50, 2);
    }


    private void drawGroupChat() {
        int sz = 30;

        Font font = new Font("Arial", Font.PLAIN, 15);
        bg.setFont(font);
        FontMetrics fontMetrics = bg.getFontMetrics(font);
        int mx = gameWidth + 10;
        int my = gameHeight-2*sz-8;

        for(int i = game.chatRoom.size() - 1; i >= 0; --i) {
            String rawMsg = game.chatRoom.get(i);
            int colon = rawMsg.indexOf(':');
            String id = rawMsg.substring(0, colon).trim();
            String msg = rawMsg.substring(colon+1).trim();
            String name = game.connectedPlayers.get(id).getName();
            if(my - fontMetrics.getHeight() <= sbMargin)  break;
            String x = name + ": ";
            String y = msg;
            int cw = fontMetrics.stringWidth(x);
            bg.setColor(Color.RED);
            bg.drawString(x, mx, my);
            bg.setColor(Color.black);
            bg.drawString(y, mx + cw, my);
            my -= 20;
        }

        bg.setColor(Color.black);
        bg.drawLine(gameWidth, gameHeight-2*sz-2, screenw, gameHeight-2*sz-2);
        textArea.setSize(screenw-gameWidth, sz);
        textArea.setLocation(gameWidth+2, gameHeight-2*sz);
        sendButton.setSize(screenw-gameWidth, sz);
        sendButton.setLocation(gameWidth, gameHeight-sz);
    }

    private void paintCells() {
        Map map = game.getCurrentController().player.getMap();
        Pair<Long, Long> dim = map.getDimensions();
        int w = Math.toIntExact(dim.x), h = Math.toIntExact(dim.y);
        for(int i = 0; i < w; ++ i)
            for(int j = 0; j < h; ++ j) {
                Cell cell = map.getCell(i, j);
                int grass = cell.getGrass() - 1;
                Pair<Integer, Integer> p = getCellLocation(i, j);
                commands.add(new ClickCommand(p.x, p.y, 48, 48, String.format("plant %d %d", i, j)));
                if(grass >= 0) {
                    try {
                        image = ImageIO.read(new File(baseFilesPath + String.format("Grass/grass1/%d.png", grass)));
                        // 11 x 9
                        bg.drawImage(image, p.x, p.y, null);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                ArrayList<Animal> animals = cell.getAnimals();
                for(Animal animal: animals)
                    paintAnimal(animal, p);
            }
    }

    BufferedImage background = null, chatBack = null;

    int iter = 0;

    @Override
    protected void paintComponent(Graphics g) {
        if(game.getCurrentController().player.getMap().updating) {
            System.err.println("UPDATING");
            return;
        }
        commands.clear();
        long beg = System.nanoTime();
        buffer = createImage(screenw, screenh);
        bg = buffer.getGraphics();

        bg.setColor(Color.WHITE);
        bg.fillRect(0, 0, screenw, screenh); //white background
//        g.drawImage(buffer, 0, 0, null);
        if(background == null) {
            try {
                image = ImageIO.read(new File(baseFilesPath + "back.png"));
                Image scaledImage = image.getScaledInstance(gameWidth, gameHeight, Image.SCALE_SMOOTH);
                background = toBufferedImage(scaledImage);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        bg.drawImage(background, 0, 0, null);
        if(isMultiPlayer) {
            if (chatBack == null) {
                try {
                    image = ImageIO.read(new File(baseFilesPath + "chatback2.jpg"));
                    Image img = image.getSubimage(0, 0, screenw - gameWidth, screenh);
                    chatBack = toBufferedImage(img);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            bg.drawImage(chatBack, gameWidth, 0, null);
        }


        paintCells();
        paintIcons();


       // bg.drawRect(336, 264, 528, 432);
        try {
            Well well = game.getCurrentController().player.getMap().getWell();
            String level = String.valueOf(well.getLevel());
            String stage = String.valueOf(well.stage);
            image = ImageIO.read(new File(baseFilesPath + "/Service/Well/" + level + "/" + stage + ".png"));
            if(well.working && iter % 2 == 0) {
                well.stage = (well.stage + 1) % 16;
                if(well.stage == 0)
                    well.working = false;
            }
            Image scaledImage = image.getScaledInstance(195, 176,Image.SCALE_SMOOTH);
            commands.add(new ClickCommand(550, 70, scaledImage.getWidth(null), scaledImage.getHeight(null), "well"));
            bg.drawImage(scaledImage, 550, 70, null);

        } catch (IOException e) {
            e.printStackTrace();
        }
//        try {
//            image = ImageIO.read(new File("/home/amirmd76/Codes/projects/ap2018/game/static/Service/Truck/03.png"));
//            Image scaledImage = image.getScaledInstance(180, 180,Image.SCALE_SMOOTH);
//
//            bg.drawImage(colorImage(scaledImage), 250, 715, null);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            image = ImageIO.read(new File("/home/amirmd76/Codes/projects/ap2018/game/static/Service/Helicopter/02.png"));
//            Image scaledImage = image.getScaledInstance(200, 200,Image.SCALE_SMOOTH);
//            bg.drawImage(scaledImage, 750, 700, null);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            image = ImageIO.read(new File("/home/amirmd76/Codes/projects/ap2018/game/static/Service/Depot/02.png"));
//            Image scaledImage = image.getScaledInstance(200, 200,Image.SCALE_SMOOTH);
//            bg.drawImage(colorImage(scaledImage) , 500, 700, null);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            image = ImageIO.read(new File("/home/amirmd76/Codes/projects/ap2018/game/static/Workshops/Spinnery/01.png"));
//            int W = image.getWidth();
//            int H = image.getHeight();
//            W /= 4;
//            H /= 4;
//            Image img = image.getSubimage(0, 0, W, H);
//            int maxw = 134 * 3 / 2;
//            int maxh = 142 * 3 / 2;
//            double factor = Math.min((double)maxw/W, (double)maxh/H);
//            int w = (int)Math.round(W * factor), h = (int)Math.round(H * factor);
//
//            Image scaledImage = img.getScaledInstance(w, h,Image.SCALE_SMOOTH);
//            bg.drawImage(scaledImage, 100, 175, null);
//            bg.drawImage(scaledImage, 70, 350, null);
//            bg.drawImage(scaledImage, 70, 550, null);
//
//            bg.drawImage(scaledImage, 900, 150, null);
//            bg.drawImage(scaledImage, 930, 320, null);
//            bg.drawImage(scaledImage, 930, 480, null);
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        if(isMultiPlayer) {
            drawNames();
            drawGroupChat();
        }
        g.drawImage(buffer, 0, 0,  null);
        long end = System.nanoTime();
        iter ++;
//        if(terminate)
//            System.exit(0);
//        System.err.println(String.format("Took %dms", (end-beg)/(long)1e6));

    }
}
