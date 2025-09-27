import java.io.*;
import java.util.List;
import java.util.Map;

public class ClientHandler {

    private final Client client;
    private final BufferedReader reader;
    private final BufferedWriter writer;

    Map<Client, BufferedWriter> writers;

    public ClientHandler(Client client, Map<Client,BufferedWriter> writers) throws IOException {
        this.client = client;
        this.writers = writers;
        InputStream in = client.getInputStream();
        OutputStream out = client.getOutputStream();
        reader = new BufferedReader(new InputStreamReader(in));
        writer = new BufferedWriter(new OutputStreamWriter(out));

        brodcast(client.getName()+" has joined the chat!");
    }

    public void handle() throws IOException {

            String message;
            while((message = reader.readLine() ) != null)
                brodcast(client.getName() +":"+message);

            logout();

    }

    private void logout() {

        brodcast(client.getName() + " left the chat!");
        writers.remove(this.client);

    }

    public void brodcast(String message) {

        for(Map.Entry<Client,BufferedWriter> entry : writers.entrySet() )
        {
            if(entry.getKey() == client) continue;

            try {
                    BufferedWriter writer = entry.getValue();
                    synchronized (writer) {
                        writer.write(message);
                        writer.newLine();
                        writer.flush();
                    }
                }
                catch(IOException e)
                {
                    writers.remove(entry.getKey());
                    System.out.println("Invalid client");

                }


        }
    }



}
