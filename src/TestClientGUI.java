
import javax.swing.*;
        import java.awt.*;
        import java.io.*;
        import java.net.Socket;

public class TestClientGUI extends JFrame {
    private Socket socket;
    private BufferedWriter writer;
    private BufferedReader reader;
    private JTextArea chatArea;
    private JTextField inputField;

    public TestClientGUI () {

        setTitle("Groupy Chat");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                try {
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                dispose();
                System.exit(0);
            }
        });


        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);

        inputField = new JTextField();
        JButton sendButton = new JButton("Send");

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);



        try {
            socket = new Socket("groupy-dvq0.onrender.com", 5500);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));


            String name = JOptionPane.showInputDialog(this, "Enter your name:");
            writer.write(name);
            writer.newLine();
            writer.flush();


            new Thread(() -> {
                try {
                    String msg;
                    while ((msg = reader.readLine()) != null) {
                        chatArea.append(msg + "\n");
                    }
                } catch (IOException e) {
                    chatArea.append("Connection closed.\n");
                }
            }).start();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Unable to connect to server: " + e.getMessage());
            System.exit(1);
        }


        sendButton.addActionListener(e -> sendMessage());
        inputField.addActionListener(e -> sendMessage());
    }

    private void sendMessage() {
        try {
            String message = inputField.getText().trim();
            if (!message.isEmpty()) {
                writer.write(message);
                chatArea.append("you: "+message + "\n");
                writer.newLine();
                writer.flush();
                inputField.setText("");
            }
        } catch (IOException e) {
            chatArea.append("Error sending message.\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TestClientGUI().setVisible(true));
    }
}
