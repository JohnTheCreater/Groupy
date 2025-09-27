import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {

    private final String id;
    private final Socket socket;
    private final InputStream in;
    private final OutputStream out;
    private final String name;
    public Client(String id,String name,Socket socket) throws IOException
    {
        this.id = id;
        this.name = name;
        this.socket = socket;
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
    }

    public String getId(){return this.id;}
    public String getName(){return this.name;}
    public InputStream getInputStream(){return this.in;}
    public OutputStream getOutputStream(){return this.out;}


}
