package grad.proj.utils.imaging;


import java.awt.*;
import java.awt.image.BufferedImage;

public class ArrayImage implements Image {
    int[][] imageArr;

    public ArrayImage(int rows, int cols) {
        imageArr = new int[rows][cols];
    }

    public ArrayImage(int[][] imageArr) {
        this.imageArr = imageArr;
    }

    public ArrayImage(ArrayImage image) {
        this(image.imageArr);
    }

    public ArrayImage(BufferedImage image) {
        imageArr = new int[image.getHeight()][image.getWidth()];

        for (int i = 0; i < getWidth(); ++i) {
            for (int j = 0; j < getHeight(); ++j) {
                imageArr[j][i] = image.getRGB(i, j);
            }
        }
    }

    public static BufferedImage rotate(BufferedImage image, double angle) {
        double sin = Math.abs(Math.sin(angle)), cos = Math.abs(Math.cos(angle));
        int w = image.getWidth(), h = image.getHeight();
        int neww = (int) Math.floor(w * cos + h * sin), newh = (int) Math.floor(h * cos + w * sin);
        GraphicsConfiguration gc = getDefaultConfiguration();
        BufferedImage result = gc.createCompatibleImage(neww, newh, Transparency.TRANSLUCENT);
        Graphics2D g = result.createGraphics();
        g.translate((neww - w) / 2, (newh - h) / 2);
        g.rotate(angle, w / 2, h / 2);
        g.drawRenderedImage(image, null);
        g.dispose();
        return result;
    }

    public static void transparentToWhite(BufferedImage image) {
        for (int i = 0; i < image.getWidth(); ++i) {
            for (int j = 0; j < image.getHeight(); ++j) {
                Color color = new Color(image.getRGB(i, j), true);
                if (color.getAlpha() == 0) {
                    image.setRGB(i, j, Color.WHITE.getRGB());
                }
            }
        }
    }


    private static GraphicsConfiguration getDefaultConfiguration() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        return gd.getDefaultConfiguration();
    }

    public BufferedImage cloneToBufferedImage(int image_type) {
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), image_type);
        for (int i = 0; i < getWidth(); ++i) {
            for (int j = 0; j < getHeight(); ++j) {
                image.setRGB(i, j, imageArr[j][i]);
            }
        }
        return image;
    }

    public void rotateImage(double angle) {
        BufferedImage image = cloneToBufferedImage(BufferedImage.TYPE_BYTE_GRAY);


        image = rotate(image, angle);
        transparentToWhite(image);
        image = ImageLoader.scaleImage(image);


        imageArr = new ArrayImage(image).imageArr;
    }

    @Override
    public int getPixelAt(int row, int col) {
        return imageArr[row][col];
    }

    @Override
    public void setPixelAt(int row, int col, int rgb) {
        imageArr[row][col] = rgb;
    }

    @Override
    public int getWidth() {
        return imageArr[0].length;
    }

    @Override
    public int getHeight() {
        return imageArr.length;
    }

}
