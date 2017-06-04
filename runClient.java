import java.io.IOException;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;


public class runClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        int source_port = 1997;
        String message = args[2];

        if(message.length() > 80) {
            System.out.println("Opps, your message's length is over 80 characters!!!");
        } else {
            int dest_port = Integer.parseInt(args[1]);

            Diagram payload = new Diagram(source_port, dest_port, message.length(), false, message);
            Protocol p = new Protocol(InetAddress.getLocalHost().getHostAddress(), source_port);
            p.send(InetAddress.getByName(args[0]), payload);


            Set<String> hashValues = new HashSet<String>();

            while(true) {
                Diagram received_payload = p.receive();
                if(received_payload != null && received_payload.message != null) {
                    if(!hashValues.contains(received_payload.message)) {
                        hashValues.add(received_payload.message);
                    }
                }
                if(hashValues.size() == 2) {
                    System.out.println("Hash value of \"" + message +"\"");
                    String hash = hashValues.iterator().next();
                    System.out.println(hash);
                    hashValues.remove(hash);
                    System.out.println(hashValues.iterator().next());
                    break;
                }
            }
        }
    }
}
