import com.savarese.rocksaw.net.RawSocket;

import java.net.InetAddress;

/**
 * Created by root on 27/05/2017.
 */
public class Server256 extends Thread{
    private int port;

    public Server256(int port){
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
                    // System.out.println("SHA received");
                    String[] info = payload.message.split("-", 3);
                    String mess = "SHA 256: " + Encode.getCode(info[2], "SHA-256");
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
