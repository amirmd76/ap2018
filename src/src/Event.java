public class Event {

    public void printStatus(String status){
        System.out.println(status);
    }

    public void checkStatus(String[] status){
        printStatus(status[1]);
    }
}
