import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Server {

    private ServerSocket serverSocket;
    public static final int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "10000"));

    Map<Client, BufferedWriter> writers;

    public Server() throws IOException {


        serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress("0.0.0.0", port));

        writers = new ConcurrentHashMap<>();

    }

    public void start() {

        try {
            System.out.println("Server Running on port "+port);
            while (true) {

                Socket clientSocket = serverSocket.accept();
                String userId = UUID.randomUUID().toString();
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                String name = reader.readLine();
                Client client = new Client(userId, name, clientSocket);

                writers.put(client, writer);

                new Thread(() -> {
                    try {
                        ClientHandler handler = new ClientHandler(client, writers);
                        System.out.println(client.getName() + " JOINED!");
                        handler.handle();
                        System.out.println(client.getName() +" LEFT!");

                    } catch (Exception e) {

                        System.out.println("Unable to Join the Chat!");

                    } finally {
                        writers.remove(client);
                        try {
                            reader.close();
                        } catch (Exception ignore) {
                        }
                        try {
                            writer.close();
                        } catch (Exception ignore) {
                        }
                        try {
                            clientSocket.close();
                        } catch (Exception ignore) {
                        }
                    }

                }).start();


            }

        } catch (IOException e) {
            System.out.println("Exception at Top Layer (unable to setup connection) :" + e);
        }
    }
}
