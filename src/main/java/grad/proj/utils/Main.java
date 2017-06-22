package grad.proj.utils;

import grad.proj.localization.ObjectsLocalizer;
import grad.proj.server.Server;
import grad.proj.utils.imaging.ArrayImage;
import grad.proj.utils.imaging.ImageLoader;
import org.opencv.core.Core;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;


/**
 * Created by mohammad on 13/06/17.
 */
public class Main {
    private static ObjectsLocalizer localizer = ObjectsLocalizer.getObjectsLocalizer();

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void NormalizeImage(ArrayImage image) {
        ImageLoader.saveImage(image, "jpg", new File("datasets/Yasser/test.jpg"), 700, 700);
        image = (ArrayImage) ImageLoader.loadImage("datasets/Yasser/test.jpg");
    }

    public static void test() throws IOException {
        ArrayImage test = (ArrayImage) ImageLoader.loadImage("datasets/Yasser/test.jpg");
        NormalizeImage(test);


        Map<String, Rectangle> objectsBounds = localizer.getObjectsBounds(test);


        BufferedImage bufferedImageTest = test.cloneToBufferedImage(BufferedImage.TYPE_INT_RGB);

        for (Map.Entry entry : objectsBounds.entrySet()) {
            TestsHelper.drawRectangle((String) entry.getKey(), (Rectangle) entry.getValue(),
                    bufferedImageTest);
        }


        File resultImageFile = new File("Output/test.jpg");

        ImageIO.write(bufferedImageTest, "jpg", resultImageFile);

    }

    public static void main(String[] args) throws IOException, ClassNotFoundException,
            SQLException {
        System.out.println("Loaded classifier");
        Server.initializeDatabase();
//        test();
    }
}
