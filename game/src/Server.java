import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.Semaphore;


public class Server implements Runnable, Serializable {
    private String IP;
    private int port;
    private ServerSocket serverSocket;
    private Socket socket;
    private HashMap<String, Player> connectedPlayers = new HashMap<>();
    private File chatFile;
    private Shop shop;
    private boolean run = true;
    private File saveFile = new File("save");
    HashMap<String, Thread> clients = new HashMap<>();
    HashMap<String, ArrayList<String>> commands = new HashMap<>();



    public Server(int port) {
        this.port = port;
        this.chatFile = new File("chat.txt");
        shop = new Shop();
        try {
            serverSocket = new ServerSocket(port);
            this.IP = serverSocket.getInetAddress().getHostAddress();
        } catch (IOException e) {
            e.printStackTrace();        //Handle this exception by asking for a new port number
        }
    }

    public Server(Shop shop) {
        this.port = 8050;
        this.chatFile = new File("chat.txt");
        this.shop = shop;
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

    public boolean admitPlayerName(String ID){
        Set<String> names = connectedPlayers.keySet();
        Iterator<String> iterator = names.iterator();
        while(iterator.hasNext()){
            String temp = iterator.next();
            if (temp.equals(ID))
                return false;
        }
        return true;
    }

    public String getIP() { return IP; }

    public int getPort() { return port; }

    public HashMap<String, Player> getConnectedPlayers() { return connectedPlayers; }

    @Override
    public void run() {
        String comment = null;
        String ID = null;
        synchronized (socket) {
            while (true) {
                try {
                    socket = serverSocket.accept();
                    OutputStream outputStream = socket.getOutputStream();
                    Formatter formatter = new Formatter(outputStream);
                    ID = getID();
                    if (!admitPlayerName(ID)){
                        comment = "error: this nameID is already taken";
                        formatter.format("enter new ID" + "\n");
                        formatter.flush();
                        run = false;
                    }
                    else
                        run = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (run) {
                    Thread client = new Thread(new ServerHandler(port, socket, shop, IP, chatFile, connectedPlayers, connectedPlayers.get(ID), commands.get(ID)));
                    clients.put(ID, client);
                    client.start();
                }
                saveServer();
            }
        }
    }
}
