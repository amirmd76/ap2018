public class ClickCommand {
    private int lx, rx, ly, ry;
    private String command;
    private String type = "";
    public ClickCommand(int lx, int ly, int w, int h, String  cmd) {
        this.lx = lx;
        this.ly = ly;
        this.rx = lx + w;
        this.ry = ly + h;
        this.command = cmd;
    }
    public ClickCommand(int lx, int ly, int w, int h, String  cmd, String type) {
        this.lx = lx;
        this.ly = ly;
        this.rx = lx + w;
        this.ry = ly + h;
        this.command = cmd;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getCommand() {
        return command;
    }

    public boolean wasClicked(int x, int y) {
        return lx <= x && x <= rx && ly <= y && y <= ry;
    }
}
