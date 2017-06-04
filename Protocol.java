import com.savarese.rocksaw.net.RawSocket;

import java.io.IOException;
import java.net.InetAddress;

import static com.savarese.rocksaw.net.RawSocket.PF_INET;


public class Protocol {
    private RawSocket sendSocket;
    private RawSocket recSocket;
    private int port;

    public Protocol(String host, int port) throws IOException {
        sendSocket = new RawSocket();
        recSocket = new RawSocket();
        sendSocket.open(PF_INET, RawSocket.getProtocolByName("ip"));
        recSocket.open(PF_INET, RawSocket.getProtocolByName("ip"));
        sendSocket.bind(InetAddress.getByName(host));
        recSocket.bind(InetAddress.getByName(host));
        this.port = port;
    }

    public void send(InetAddress dest_address, Diagram data_diagram) {
        Thread timer = new Thread(() -> {
            byte[] data = data_diagram.toByte();
            byte[] received_bytes = new byte[2400];
            Diagram received_data;
            while(true) {
                try {
                    // System.out.println("Sending...");
                    sendSocket.write(dest_address, data);
                    // System.out.println("Checking...");
                    sendSocket.read(received_bytes);
                    received_data = new Diagram(received_bytes);
					if(received_data.destination_port == this.port && received_data.checkChecksum())
                    	if(received_data != null && received_data.isACK)
                        	break;
					Thread.sleep(10000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                System.out.println("received ACK");
            }
        });
        timer.start();
    }

    public Diagram receive() throws IOException{
        byte[] data = new byte[2400];
        recSocket.read(data);
        // System.out.println("Received something");
        Diagram received_data = new Diagram(data);
        if(received_data.isACK) return null;
        if(received_data.destination_port != this.port) return null;
        if (received_data.checkChecksum()) { //check sum here
            Diagram ack_diagram = new Diagram(received_data.destination_port, received_data.source_port,
                    0, true, "");
            recSocket.write(InetAddress.getByName(received_data.source_add), ack_diagram.toByte());
            return received_data;
        }
//        else {
//            System.out.print("Invalid checksum");
//        }
        return null;
    }
}
