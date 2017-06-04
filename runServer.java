import com.savarese.rocksaw.net.RawSocket;

import java.io.IOException;


public class runServer {
    public static void main(String[] args) throws IOException {

        int portServer = Integer.parseInt(args[0]);
        int portMD5 = Integer.parseInt(args[1]);
        int portSHA256 = Integer.parseInt(args[2]);

        Server1 server1 = new Server1(portServer, portMD5, portSHA256);
        server1.start();

        ServerMD5 serverMD5 = new ServerMD5(portMD5);
        serverMD5.start();

        Server256 server256 = new Server256(portSHA256);
        server256.start();
    }
}