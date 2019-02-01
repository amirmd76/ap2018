import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.Semaphore;

public class Client implements Runnable {
    private String IP;
    private int port;
    private Socket socket;
    private Player thisPlayer;
    private Semaphore semaphore = new Semaphore(0);
    private File chatFile = new File("chat.txt");
    private ArrayList<String> textInputs = new ArrayList<>();
    private ArrayList<String> commands;
    private boolean run = true;
    private Shop shop;
    private HashMap<String, Long> boughtItems;
    private HashMap<String, Long> soldItems;
    private HashSet<String> friends = new HashSet<>();

    public Client(int port, String IP, Player player, ArrayList<String> commands, Shop shop, HashMap<String, Long> boughtItems, HashMap<String, Long> soldItems) {
        this.IP = IP;
        this.port = port;
        this.thisPlayer = player;
        this.commands = commands;
        this.shop = shop;
        this.boughtItems = boughtItems;
        this.soldItems = soldItems;
        try {
            socket = new Socket(IP, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Client(String IP, Player player, ArrayList<String> commands, Shop shop, HashMap<String, Long> boughtItems, HashMap<String, Long> soldItems) {
        this.IP = IP;
        this.port = 8060;
        this.thisPlayer = player;
        this.commands = commands;
        this.shop = shop;
        this.boughtItems = boughtItems;
        this.soldItems = soldItems;
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



    public Player getThisPlayer() {
        return thisPlayer;
    }

    @Override
    public void run() {
        sendInitialData();
        try {
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            Scanner scanner = new Scanner(inputStream);
            Formatter formatter = new Formatter(outputStream);
            Thread reader = new Thread(new Reader(socket, chatFile, semaphore, run));
            Thread writer = new Thread(new Writer(socket, chatFile, textInputs, run, semaphore));
            Iterator<String> iterator = commands.iterator();


            while (true) {
                if (iterator.hasNext()) {
                    String command = iterator.next();
                    synchronized (socket) {
                        switch (command) {
                            case "playerData": {
                                formatter.format("playerData");
                                formatter.flush();
                                run = false;
                                sendInitialData();
                                do { } while(!scanner.nextLine().equals("playerReceived"));
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
                                formatter.format("shop");
                                formatter.flush();
                                receiveShop();
                                formatter.format("shopReceived");
                                formatter.flush();
                            }
                            case "sendDeals":{
                                run = false;
                                formatter.format("deals");
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

                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
