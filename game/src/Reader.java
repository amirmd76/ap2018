import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Formatter;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Reader implements Runnable {

    private Socket socket;
    private File file;
    private Semaphore semaphore;
    private boolean run;

    public Reader(Socket socket, File file, Semaphore semaphore, boolean run) {
        this.socket = socket;
        this.file = file;
        this.semaphore = semaphore;
        this.run = run;
    }

    @Override
    public void run() {
        synchronized (socket) {
            try {
                InputStream inputStream = socket.getInputStream();
                Scanner scanner = new Scanner(inputStream);
                Formatter formatter = new Formatter(file);

                synchronized (file) {
                    semaphore.acquire();
                    while (run) {
                        String str = scanner.nextLine();
                        //System.out.println(str);
                        if (str.equals("endOfChat")) {            //server send "endOfChat" to stop the reader class and update player data
                            semaphore.release();
                            break;
                        }
                        synchronized (file) {
                            formatter.format(str);
                            formatter.flush();
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
