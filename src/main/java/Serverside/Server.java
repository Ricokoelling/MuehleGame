package Serverside;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private static final ArrayList<Clienthandler> clients = new ArrayList<>();
    private static final ExecutorService pool = Executors.newFixedThreadPool(10);
    static ServerSocket servs;

    public Server() {
        try {
            servs = new ServerSocket(1337);
        } catch (IOException e) {
            System.err.println("Failed to create Server\nError:");
            e.printStackTrace();
        }
    }

    public Server(int port) {
        try {
            servs = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Failed to create Server\nError:");
            e.printStackTrace();
        }
    }

    /**
     * starts client search and if founded will start clienthandler to process every upcoming stream
     *
     * @throws IOException yee
     */
    public static void start() throws IOException {
        while (true) {
            System.out.println("[SERVER] Waiting for client connection....");
            Socket client = servs.accept();
            System.out.println("[SERVER] Connected to " + client.getLocalPort());
            Clienthandler clienthandler = new Clienthandler(client, clients);
            clients.add(clienthandler);


            pool.execute(clienthandler);
        }
    }

    public static void main(String[] args) throws IOException, SQLException {
        Server s = new Server();
        start();
    }

}
