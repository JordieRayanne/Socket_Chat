import java.lang.ClassNotFoundException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ServerTest extends Socket {
    static ServerSocket server;
    static int port = 9559;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        server = new ServerSocket(port);
        System.out.println("Waiting for the client message");
        while (true) {
            Socket socket = server.accept();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            String message = (String) ois.readObject();
            System.out.println("Message:" + message);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject("Hi client " + message);
            ois.close();
            oos.close();
            socket.close();
            if (message.equalsIgnoreCase("exit"))
                break;
        }
    }

}