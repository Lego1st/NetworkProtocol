import java.net.InetAddress;

public class ServerMD5 extends Thread{
    private int port;

    public ServerMD5(int port){
        this.port = port;
    }

    @Override
    public void run() {
        super.run();
        try {
            Protocol p = new Protocol(InetAddress.getLocalHost().getHostAddress(), port);
            while(true) {
                Diagram payload = p.receive();
                if(payload != null) {
                    // System.out.println("MD5 received");
                    String[] info = payload.message.split("-", 3);
                    String mess = "MD5: " + Encode.getCode(info[2],"MD5");
                    Diagram payload_to_send = new Diagram(this.port, Integer.parseInt(info[1]), mess.length(), false, mess);
                    Protocol p1 = new Protocol(InetAddress.getLocalHost().getHostAddress(), this.port);
                    p1.send(InetAddress.getByName(info[0]), payload_to_send);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void start() {
        super.start();
    }
}
