import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TestClient {
    static volatile boolean running = true;
    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("localhost",5500);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        Scanner scan = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String name = scan.nextLine();

        writer.write(name);
        writer.newLine();
        writer.flush();

        new Thread(()->{
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String message;
                while (running && (message = reader.readLine()) != null )
                {
                    System.out.println(message);
                }

            } catch (IOException e) {
                if (running) {
                    System.out.println("Connection lost: " + e.getMessage());
                }
            }

        }).start();



        while (true)
        {

            String message = scan.nextLine();
            if (message.equalsIgnoreCase("/quit")) {
                running = false;
                writer.close();
                socket.close();
                break;
            }

            writer.write(message);
            writer.newLine();
            writer.flush();


        }

    }
}
