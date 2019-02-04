import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Reader implements Runnable {

    private Socket socket;
    private ArrayList<String> chatRoom;
    private Semaphore semaphore;
    private boolean run;

    public Reader(Socket socket, ArrayList<String> chatRoom, Semaphore semaphore, boolean run) {
        this.socket = socket;
        this.chatRoom = chatRoom;
        this.semaphore = semaphore;
        this.run = run;
    }

    @Override
    public void run() {
        synchronized (socket) {
            try {
                InputStream inputStream = socket.getInputStream();
                Scanner scanner = new Scanner(inputStream);

                synchronized (socket) {
                    semaphore.acquire();
                    while (run) {
                        String str = scanner.nextLine();
                        if (str.equals("endOfChat")) {            //server send "endOfChat" to stop the reader class and update player data
                            semaphore.release();
                            break;
                        }
                        synchronized (chatRoom) {
                            chatRoom.add(str);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}