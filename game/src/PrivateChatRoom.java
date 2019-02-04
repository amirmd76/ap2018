import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class PrivateChatRoom implements Runnable {

    private Thread reader, writer;
    private File file = new File("privateCHatRoom");
    private ArrayList<String> privateChat;
    private FileWriter fileWriter;
    String exception = null;
    private Socket socket;
    private Semaphore semaphore;
    private boolean run;
    private ArrayList<String> textInputs;
    private String chatNameID;

    public PrivateChatRoom(Socket socket, ArrayList<String> textInputs, boolean run, Semaphore semaphore, String chatNameID, ArrayList<String> privateChat) {
        this.socket = socket;
        this.textInputs = textInputs;
        this.run = run;
        this.semaphore = semaphore;
        this.privateChat = privateChat;
        this.chatNameID = chatNameID;
        try {
            fileWriter = new FileWriter(file);
            reader = new Thread(new Reader(socket, privateChat, semaphore, run));
            writer = new Thread(new Writer(socket, privateChat, textInputs, run, semaphore));
        } catch (IOException e) {
            e.printStackTrace();
            exception = "FileException";
        }
    }

    @Override
    public void run() {
        reader.start();
        writer.start();

    }
}