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
    private HashMap<String, Player> connectedPlayers;
    private Semaphore semaphore = new Semaphore(0);
    private ArrayList<String> command;
    private ArrayList<String> textInputs;
    private ArrayList<String> privateTextInput;
    private ArrayList<String> chatRoom;
    private ArrayList<String> privateChat;
    private Thread privateChatRoom;
    private Shop shop;
    private boolean run = true;
    private File saveFile = new File("save");
    HashMap<String, Thread> clients = new HashMap<>();
    HashMap<String, ArrayList<String>> commands = new HashMap<>();



    public Server(int port, Shop shop, HashMap<String, Player> connectedPlayers, ArrayList<String> chatRoom, ArrayList<String> commands, ArrayList<String> privateChat, ArrayList<String> textInputs, ArrayList<String> privateTextInput) {
        this.port = port;
        this.chatRoom = chatRoom;
        this.command = commands;
        this.textInputs = textInputs;
        this.privateChat = privateChat;
        this.privateTextInput = privateTextInput;
        this.shop = shop;
        this.connectedPlayers = connectedPlayers;
        try {
            serverSocket = new ServerSocket(port);
            this.IP = serverSocket.getInetAddress().getHostAddress();
        } catch (IOException e) {
            e.printStackTrace();        //Handle this exception by asking for a new port number
        }
    }

    public Server(Shop shop, HashMap<String, Player> connectedPlayers, ArrayList<String> chatRoom, ArrayList<String> commands, ArrayList<String> privateChat, ArrayList<String> textInputs, ArrayList<String> privateTextInput) {
        this.port = 8050;
        this.chatRoom = chatRoom;
        this.textInputs = textInputs;
        this.privateTextInput = privateTextInput;
        this.command = commands;
        this.privateChat = privateChat;
        this.connectedPlayers = connectedPlayers;
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
        Iterator<String> iterator = command.iterator();
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
                    Thread client = new Thread(new ServerHandler(port, socket, shop, IP, connectedPlayers, connectedPlayers.get(ID), commands.get(ID), chatRoom));
                    clients.put(ID, client);
                    client.start();
                }
                if (iterator.hasNext()){
                    String command = iterator.next();
                    switch (command){
                        case "privateChat":{
                            semaphore.release();
                            semaphore.release();
                            String name = iterator.next();
                            privateChatRoom = new Thread(new PrivateChatRoom(socket, privateTextInput, run, semaphore, name, privateChat));
                            privateChatRoom.start();
                            break;
                        }
                        /*case "makeFriend": {
                            String name = iterator.next();
                            formatter.format("friendNotification" + "\n");
                            formatter.flush();
                            formatter.format(name);
                            formatter.flush();
                            if (scanner.nextLine().equals("approved")) {
                                friends.add(name);
                            }
                            break;
                        }
                        case "friendNotification":{
                            notif = "makeFriend";
                            do { } while (notif.equals("makeFriend"));
                            if (notif.equals("approved"))
                                formatter.format("approved" + "\n");
                            else
                                formatter.format("declined" + "\n");
                            formatter.flush();
                        }*/
                    }
                }
                saveServer();
            }
        }
    }
}