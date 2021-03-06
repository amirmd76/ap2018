
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Writer implements Runnable {

    private Socket socket;
    private boolean run;
    private Semaphore semaphore;
    ArrayList<String> textInputs;
    ArrayList<String> chatRoom;


    public Writer(Socket socket, ArrayList<String> chatRoom, ArrayList<String> txt, boolean run, Semaphore semaphore) {
        this.socket = socket;
        this.chatRoom = chatRoom;
        this.textInputs = txt;
        this.run = run;
        this.semaphore = semaphore;
    }

    public void fileFormatter(File file, String chat){
        synchronized (file) {
            try {
                FileWriter fileWriter = new FileWriter(file, true);
                fileWriter.write("\n" + chat);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        synchronized (socket) {
            try {
                OutputStream outputStream = socket.getOutputStream();
                Formatter formatter = new Formatter(outputStream);
                while (run) {
                    semaphore.acquire();
                    String str = textInputs.get(0);
                    textInputs.remove(0);
                    formatter.format("chat" + "\n");
                    formatter.flush();
                    formatter.format(str);
                    synchronized (chatRoom) {
                        chatRoom.add(str);
                    }
                    formatter.flush();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}