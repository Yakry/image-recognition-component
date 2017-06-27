package grad.proj.server;

/**
 * Created by mohammad on 18/06/17.
 */

import grad.proj.database_manager.DatabaseManager;
import grad.proj.database_manager.UserController;
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

    public static ArrayImage normalizeImage(ArrayImage image) {
        ImageLoader.saveImage(image, "jpg", new File("test.jpg"), 700, 700);
        return (ArrayImage) ImageLoader.loadImage("test.jpg");
    }

    public static LinkedList<String> getObjects(BufferedImage bufferedImage) {
        LinkedList<String> list = new LinkedList<>();

        ArrayImage image = new ArrayImage(bufferedImage);
        image = normalizeImage(image);

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

    public static BufferedImage pixelsToImage(int[][] data) throws IOException {
        BufferedImage image = new BufferedImage(data.length, data[0].length, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < data.length; ++i) {
            for (int j = 0; j < data[i].length; j++) {
                image.setRGB(i, j, data[i][j]);
            }
        }
        return image;
    }

    public static void main(String args[]) throws IOException, ClassNotFoundException, SQLException {
        initializeDatabase();

        server = new ServerSocket(port);

        System.out.println("Waiting for client request");
        Socket socket = server.accept();
        System.out.println("Accepted client request");

        ObjectInputStream inputStream = null;
        ObjectOutputStream outputStream = null;
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            //e.printStackTrace();
        }

        boolean running = true;
        while(running){
            System.out.println("Waiting request...");
            String requestType = (String)inputStream.readObject();

            switch (requestType) {
                case "recognize":
                    doRecognizeRequest(inputStream, outputStream);
                    break;
                case "signup":
                    doSignupRequest(inputStream, outputStream);
                    break;
                case "login":
                    doLoginRequest(inputStream, outputStream);
                    break;
                case "exit":
                    running = false;
                default:
                    continue;
            }

        }

        server.close();
    }

    private static void doLoginRequest(ObjectInputStream inputStream, ObjectOutputStream outputStream) throws IOException, ClassNotFoundException, SQLException {
        System.out.println("Waiting Credentials");
        String[] credentials = (String[])inputStream.readObject();
        System.out.println("Credentials Received");

        Boolean success = UserController.login(credentials[0], credentials[1]);

        outputStream.writeBoolean(success);
        outputStream.flush();
    }

    private static void doSignupRequest(ObjectInputStream inputStream, ObjectOutputStream outputStream) throws IOException, ClassNotFoundException, SQLException {
        System.out.println("Waiting Credentials");
        String[] credentials = (String[])inputStream.readObject();
        System.out.println("Credentials Received");

        Boolean success = UserController.signup(credentials[0], credentials[1]);

        outputStream.writeBoolean(success);
        outputStream.flush();
    }

    private static void doRecognizeRequest(ObjectInputStream inputStream, ObjectOutputStream outputStream) throws IOException, ClassNotFoundException {
        System.out.println("Waiting Image");
        int[][] pixels = (int[][]) inputStream.readObject();
        System.out.println("Image Received");

        BufferedImage image = pixelsToImage(pixels);
        LinkedList<String> list = getObjects(image);

        System.out.println("Objects detected: " + list);

        outputStream.writeObject(list);
        outputStream.flush();
    }


}
