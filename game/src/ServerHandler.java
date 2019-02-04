import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.Semaphore;


public class ServerHandler implements Runnable, Serializable {
    private String IP;
    private int port;
    private Socket socket;
    private HashMap<String, Player> connectedPlayers;
    private Player currentPlayer;
    private Semaphore semaphore;
    private File chatFile;
    private Shop shop;
    private ArrayList<String> command;
    ArrayList<String> textInputs = new ArrayList<>();
    private File saveFile = new File("save");


    public ServerHandler(int port, Socket socket, Shop shop, String IP, File file, HashMap<String, Player> connectedPlayers, Player currentPlayer, ArrayList<String> cmd) {
        this.port = port;
        this.IP = IP;
        this.socket = socket;
        this.semaphore = new Semaphore(0);
        this.chatFile = file;
        this.shop = shop;
        this.connectedPlayers = connectedPlayers;
        this.currentPlayer = currentPlayer;
        this.command = cmd;
    }

    public String getPlayer(){
        String nameID = null;
        synchronized (socket) {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                currentPlayer = (Player) objectInputStream.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return nameID;
    }

    public void sendSpecificPlayer(){
        synchronized (socket) {
            try {
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(currentPlayer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void sendShop(){
        synchronized (socket) {
            try {
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(shop);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void receiveSells(){
        synchronized (socket) {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                HashMap<String, Long> map = (HashMap<String, Long>) objectInputStream.readObject();
                for (String i : map.keySet()) {
                    shop.bought(i, map.get(i));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void receiveBuys(){
        synchronized (socket) {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                HashMap<String, Long> map = (HashMap<String, Long>) objectInputStream.readObject();
                for (String i : map.keySet()) {
                    shop.sell(i, map.get(i));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public String getIP() { return IP; }

    public int getPort() { return port; }

    public HashMap<String, Player> getConnectedPlayers() { return connectedPlayers; }

    @Override
    public void run() {
        synchronized (socket) {
            Thread reader = new Thread(new Reader(socket, chatFile, semaphore, true));
            Thread writer = new Thread(new Writer(socket, chatFile, textInputs, true, semaphore));
            try {
                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();
                Scanner scanner = new Scanner(inputStream);
                Formatter formatter = new Formatter(outputStream);
                while (true) {
                    semaphore.acquire();
                    String txt = scanner.nextLine(), nameID = null;
                    switch (txt) {
                        case "playerData": {
                            nameID = getPlayer();
                            formatter.format("playerReceived");
                            formatter.flush();
                            break;
                        }
                        case "chat": {
                            reader.start();
                            writer.start();
                            semaphore.release();
                            semaphore.release();
                            break;
                        }
                        case "shop": {
                            sendShop();
                            do {
                            } while (!scanner.nextLine().equals("shopReceived"));
                            break;
                        }
                        case "deals": {
                            receiveBuys();
                            formatter.format("boughtItemsReceived");
                            formatter.flush();
                            receiveSells();
                            formatter.format("soldItemsReceived");
                            formatter.flush();
                            break;
                        }
                        case "friendNotification": {
                            formatter.format("friendNotification");
                            formatter.flush();
                            break;
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
