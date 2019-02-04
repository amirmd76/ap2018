import javafx.print.PageLayout;
import org.omg.CORBA.ARG_IN;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Client implements Runnable {
    private String IP;
    private int port;
    private Socket socket;
    private Player thisPlayer;
    private Semaphore semaphore = new Semaphore(0);
    private ArrayList<String> textInputs;
    private ArrayList<String> privateTextInput;
    private ArrayList<String> commands;
    private boolean run = true, runClient = true;
    private Shop shop;
    private HashMap<String, Long> boughtItems;
    private HashMap<String, Long> soldItems;
    private HashSet<String> friends = new HashSet<>();
    private Thread privateChatRoom;
    private String error;
    private String notif;
    private HashMap<String, Player> connectedPlayers;
    private ArrayList<String> chatRoom, privateChat;


    public Client(int port, String IP, Player player, ArrayList<String> commands, Shop shop, HashMap<String, Long> boughtItems, HashMap<String, Long> soldItems, String err, String notif, HashMap<String, Player> connectedPlayers, ArrayList<String> chatRoom, ArrayList<String> privateChat, ArrayList<String> textInputs, ArrayList<String> privateTextInput) {
        this.IP = IP;
        this.port = port;
        this.privateTextInput = privateTextInput;
        this.thisPlayer = player;
        this.privateChat = privateChat;
        this.commands = commands;
        this.error = err;
        this.shop = shop;
        this.boughtItems = boughtItems;
        this.textInputs = textInputs;
        this.soldItems = soldItems;
        this.connectedPlayers = connectedPlayers;
        this.notif = notif;
        this.chatRoom = chatRoom;
        try {
            socket = new Socket(IP, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Client(String IP, Player player, ArrayList<String> commands, Shop shop, HashMap<String, Long> boughtItems, HashMap<String, Long> soldItems, String err, String notif, HashMap<String, Player> connectedPlayers, ArrayList<String> chatRoom, ArrayList<String> privateChat, ArrayList<String> textInputs, ArrayList<String> privateTextInput) {
        this.IP = IP;
        this.port = 8060;
        this.thisPlayer = player;
        this.privateChat = privateChat;
        this.privateTextInput = privateTextInput;
        this.connectedPlayers = connectedPlayers;
        this.chatRoom = chatRoom;
        this.error = err;
        this.textInputs = textInputs;
        this.commands = commands;
        this.shop = shop;
        this.boughtItems = boughtItems;
        this.soldItems = soldItems;
        this.notif = notif;
        try {
            socket = new Socket(IP, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendInitialData() {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeChars(String.format("Player data of %s", thisPlayer.getNameID()));
            objectOutputStream.flush();
            objectOutputStream.writeObject(thisPlayer);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendText(String txt) {
        String text = thisPlayer.getNameID() + ": " + txt;
        try {
            OutputStream outputStream = socket.getOutputStream();
            Formatter formatter = new Formatter(outputStream);
            formatter.format(text);
            formatter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receivePlayerData() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            thisPlayer = (Player) objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void receiveShop() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            shop = (Shop) objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void sendBoughtItems(){
        synchronized (socket) {
            try {
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(boughtItems);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void receiveConnectedPlayers(){
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            connectedPlayers = (HashMap<String, Player>) objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void sendSoldItems(){
        synchronized (socket) {
            try {
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(soldItems);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean checkID(String ID, InputStream inputStream){
        return true;
    }



    public Player getThisPlayer() {
        return thisPlayer;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            Scanner scanner = new Scanner(inputStream);
            Formatter formatter = new Formatter(outputStream);
            Thread reader = new Thread(new Reader(socket, chatRoom, semaphore, run));
            Thread writer = new Thread(new Writer(socket, chatRoom, textInputs, run, semaphore));
            Iterator<String> iterator = commands.iterator();
            sendInitialData();
            if (scanner.nextLine().equals("enter new ID")){
                error = "ID error";
                runClient = false;
            }

            while (runClient) {
                sendInitialData();
                if (iterator.hasNext()) {
                    String command = iterator.next();
                    synchronized (socket) {
                        switch (command) {
                            case "playerData": {
                                formatter.format("playerData" + "\n");
                                formatter.flush();
                                run = false;
                                sendInitialData();
                                do {
                                    try {
                                        TimeUnit.MILLISECONDS.sleep(10);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                } while(!scanner.nextLine().equals("playerReceived"));
                                receiveConnectedPlayers();
                                formatter.format("received" + "\n");
                                formatter.flush();
                                break;
                            }
                            case "chat": {
                                formatter.format("chat");
                                formatter.flush();
                                run = true;
                                reader.start();
                                writer.start();
                                semaphore.release();
                                semaphore.release();
                                break;
                            }
                            case "shop": {
                                run = false;
                                formatter.format("shop" + "\n");
                                formatter.flush();
                                receiveShop();
                                formatter.format("shopReceived" + "\n");
                                formatter.flush();
                            }
                            case "sendDeals":{
                                run = false;
                                formatter.format("deals" + "\n");
                                formatter.flush();
                                sendBoughtItems();
                                do { } while(!scanner.nextLine().equals("boughtItemsReceived"));
                                sendSoldItems();
                                do { } while(!scanner.nextLine().equals("soldItemsReceived"));
                                break;
                            }
                            case "getAnotherPlayerData":{
                                String ID = iterator.next();
                            }
                            case "privateChat":{

                                semaphore.release();
                                semaphore.release();
                                String name = iterator.next();
                                formatter.format(name + "\n");
                                formatter.flush();
                                privateChatRoom = new Thread(new PrivateChatRoom(socket, privateTextInput, run, semaphore, name, privateChat));
                                privateChatRoom.start();
                                break;
                            }
                            case "makeFriend": {
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
                            }
                        }
                    }
                }
            }
            socket.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}