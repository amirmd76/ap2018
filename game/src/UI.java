import com.sun.scenario.effect.ColorAdjust;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.text.html.ImageView;

public class UI extends JPanel {
    int screenw, screenh;
    private static Image buffer;
    private static Graphics bg;
    public UI(int screenw, int screenh) {
        this.screenw = screenw;
        this.screenh = screenh;
        setPreferredSize(new Dimension(screenw, screenh));
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        buffer = createImage(screenw, screenh);
        bg = buffer.getGraphics();

        bg.setColor(Color.WHITE);
        bg.fillRect(0, 0, screenw, screenh); //black background
//        g.drawImage(buffer, 0, 0, null);

        BufferedImage image;
        try {
            image = ImageIO.read(new File("/home/amirmd76/Codes/projects/ap2018/game/static/back.png"));
            Image scaledImage = image.getScaledInstance(getWidth(), getHeight(),Image.SCALE_SMOOTH);
            bg.drawImage(scaledImage, 0, 0, null);

        } catch (IOException e) {
            e.printStackTrace();
        }
        bg.setColor(Color.BLACK);
       // bg.drawRect(336, 264, 528, 432);
        try {
            image = ImageIO.read(new File("/home/amirmd76/Codes/projects/ap2018/game/static/Grass/grass1.png"));
            Image img = image.getSubimage(48*3, 48*3, 48, 48);

            for(int i = 0; i < 11; ++ i)
                for(int j = 0; j < 9; ++ j) {
                    bg.drawImage(img, 336 + 48 * i, 264 + 48 * j, null);
                }

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            image = ImageIO.read(new File("/home/amirmd76/Codes/projects/ap2018/game/static/Service/Well/04.png"));
            Image img = image.getSubimage(0, 0, 150, 136);
            Image scaledImage = img.getScaledInstance(195, 176,Image.SCALE_SMOOTH);
            bg.drawImage(scaledImage, 550, 70, null);

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            image = ImageIO.read(new File("/home/amirmd76/Codes/projects/ap2018/game/static/Service/Truck/03.png"));
            Image scaledImage = image.getScaledInstance(180, 180,Image.SCALE_SMOOTH);

            bg.drawImage(colorImage(scaledImage), 250, 715, null);

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            image = ImageIO.read(new File("/home/amirmd76/Codes/projects/ap2018/game/static/Service/Helicopter/02.png"));
            Image scaledImage = image.getScaledInstance(200, 200,Image.SCALE_SMOOTH);
            bg.drawImage(scaledImage, 750, 700, null);

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            image = ImageIO.read(new File("/home/amirmd76/Codes/projects/ap2018/game/static/Service/Depot/02.png"));
            Image scaledImage = image.getScaledInstance(200, 200,Image.SCALE_SMOOTH);
            bg.drawImage(colorImage(scaledImage), 500, 700, null);

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            image = ImageIO.read(new File("/home/amirmd76/Codes/projects/ap2018/game/static/Workshops/Spinnery/01.png"));
            int W = image.getWidth();
            int H = image.getHeight();
            W /= 4;
            H /= 4;
            Image img = image.getSubimage(0, 0, W, H);
            int maxw = 134 * 3 / 2;
            int maxh = 142 * 3 / 2;
            double factor = Math.min((double)maxw/W, (double)maxh/H);
            int w = (int)Math.round(W * factor), h = (int)Math.round(H * factor);

            Image scaledImage = img.getScaledInstance(w, h,Image.SCALE_SMOOTH);
            bg.drawImage(scaledImage, 100, 175, null);
            bg.drawImage(scaledImage, 70, 350, null);
            bg.drawImage(scaledImage, 70, 550, null);

            bg.drawImage(scaledImage, 900, 150, null);
            bg.drawImage(scaledImage, 930, 320, null);
            bg.drawImage(scaledImage, 930, 480, null);


        } catch (IOException e) {
            e.printStackTrace();
        }
        g.drawImage(buffer, 0, 0,  null);


    }
}
