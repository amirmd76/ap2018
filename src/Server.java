import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.Semaphore;


public class Server implements Runnable, Serializable {
    private String IP;
    private int port;
    private ServerSocket serverSocket;
    private Socket socket;
    private HashMap<String, Player> connectedPlayers = new HashMap<>();
    private File chatFile;
    private Shop shop;
    private File saveFile = new File("save");
    HashMap<String, Thread> clients = new HashMap<>();



    public Server(int port) {
        this.port = port;
        this.chatFile = new File("chat.txt");
        shop = new Shop();
        try {
            serverSocket = new ServerSocket(port);
            //socket = serverSocket.accept();
            this.IP = serverSocket.getInetAddress().getHostAddress();
        } catch (IOException e) {
            e.printStackTrace();        //Handle this exception by asking for a new port number
        }
    }

    public Server() {
        this.port = 8050;
        this.chatFile = new File("chat.txt");
        shop = new Shop();
        try {
            serverSocket = new ServerSocket(port);
            //socket = serverSocket.accept();
            this.IP = serverSocket.getInetAddress().getHostAddress();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPlayer(){
        String nameID = null;
        synchronized (socket) {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                Player currentPlayer = (Player) objectInputStream.readObject();
                nameID = currentPlayer.getNameID();
                connectedPlayers.put(nameID, currentPlayer);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return nameID;
    }

    public void sendSpecificPlayer(String nameID){
        synchronized (socket) {
            try {
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                Player specificPlayer = connectedPlayers.get(nameID);
                outputStream.writeObject(specificPlayer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public void saveServer(){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(saveFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getID(){
        String ID = null;
        try {
            InputStream inputStream = socket.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            ID = scanner.nextLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ID;
    }

    public String getIP() { return IP; }

    public int getPort() { return port; }

    public HashMap<String, Player> getConnectedPlayers() { return connectedPlayers; }

    @Override
    public void run() {
        synchronized (socket) {
            while (true) {
                try {
                    socket = serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String ID = getID();
                Thread client = new Thread(new ServerHandler(port, socket, shop, IP, chatFile, connectedPlayers, connectedPlayers.get(ID)));
                clients.put("ID", client);
                client.start();

                saveServer();
            }
        }
    }
}
