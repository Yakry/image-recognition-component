package grad.proj.server;

/**
 * Created by mohammad on 18/06/17.
 */

import grad.proj.database_manager.DatabaseManager;
import grad.proj.localization.ObjectsLocalizer;
import grad.proj.utils.imaging.ArrayImage;
import grad.proj.utils.imaging.ImageLoader;
import org.opencv.core.Core;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;


public class Server {
    private static ObjectsLocalizer localizer = ObjectsLocalizer.getObjectsLocalizer();
    //static ServerSocket variable
    private static ServerSocket server;
    //socket server port on which it will listen
    private static int port = 9876;

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void normalizeImage(ArrayImage image) {
        ImageLoader.saveImage(image, "jpg", new File("test.jpg"), 700, 700);
        image = (ArrayImage) ImageLoader.loadImage("test.jpg");
    }

    public static LinkedList<String> getObjects(BufferedImage bufferedImage) {
        LinkedList<String> list = new LinkedList<>();

        ArrayImage image = new ArrayImage(bufferedImage);
        normalizeImage(image);

        Map<String, Rectangle> objectsBounds = localizer.getObjectsBounds(image);

        for (Map.Entry<String, Rectangle> entry : objectsBounds.entrySet()) {
            list.add(entry.getKey());
        }

        return list;
    }

    public static void initializeDatabase() throws SQLException {
        DatabaseManager.deleteAllRows("Items");
        Set<String> items = localizer.getClassifier().getClasses();
        for (String item : items) {
            DatabaseManager.executeInsert("Items", new String[]{item});
        }
    }


    public static void main(String args[]) throws IOException, ClassNotFoundException {
        //create the socket server object
        server = new ServerSocket(port);
        //keep listens indefinitely until receives 'exit' call or program terminates
        while (true) {
            System.out.println("Waiting for client request");
            //creating socket and waiting for client connection
            Socket socket = server.accept();
            //read from socket to ObjectInputStream object
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            //convert ObjectInputStream object to String
            BufferedImage image = (BufferedImage) inputStream.readObject();
            System.out.println("Image Received");

            LinkedList<String> list = getObjects(image);

            System.out.println("Objects detected");

            //create ObjectOutputStream object
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            //write object to Socket
            outputStream.writeObject(list);
            //close resources
            inputStream.close();
            outputStream.close();
            socket.close();
            //terminate the server if client sends exit request
        }
        //close the ServerSocket object
//        server.close();
    }

}
