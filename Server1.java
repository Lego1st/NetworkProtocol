import java.net.InetAddress;

public class Server1 extends Thread{
    private int port;
    private int portMD5;
    private int portSHA;

    public Server1(int port, int p1, int p2){
        this.port = port;
        this.portMD5 = p1;
        this.portSHA = p2;
    }

    @Override
    public void run() {
        super.run();
        try {
            Protocol p = new Protocol(InetAddress.getLocalHost().getHostAddress(), port);
            while(true) {
                Diagram payload = p.receive();
                if(payload != null) {
                    // System.out.println("Server 1 Received: " + payload.message);
                    String mess = payload.source_add + "-" + Integer.toString(payload.source_port) + "-" + payload.message;
                    //System.out.println("received from: " + payload.source_add);
                    Diagram payloadMD5 = new Diagram(this.port, this.portMD5, mess.length(), false, mess);
                    Protocol p1 = new Protocol(InetAddress.getLocalHost().getHostAddress(), this.port);
                    p1.send(InetAddress.getLocalHost(), payloadMD5);
                    Diagram payloadSHA = new Diagram(this.port, this.portSHA, mess.length(), false, mess);
                    Protocol p2 = new Protocol(InetAddress.getLocalHost().getHostAddress(), this.port);
                    p2.send(InetAddress.getLocalHost(), payloadSHA);

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

    //    Thread t = new Thread(() -> {
//        RawSocket socket = new RawSocket();
//        Protocol p = new Protocol(socket, portServer);
//        while(true) {
//            Diagram payload = p.receive();
//            if(payload != null)
//                System.out.println("Received: " + payload.message);
//        }
//    })
}
