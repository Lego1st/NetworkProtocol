import java.nio.ByteBuffer;
import java.util.Arrays;

public class Diagram {
    public int source_port;
    public int destination_port;
    public String checksum;
    public int length;
    public boolean isACK;
    public String message;
    public String source_add;

    private byte[] hexStringToByte(String s) {
        byte[] res = new byte[2];
        for(int i = 0; i < s.length(); i+=2)
            res[i/2] = (byte) (Character.digit(s.charAt(i), 16) * 16
                    + Character.digit(s.charAt(i+1), 16));
        return res;
    }

    private String byteToHexString(byte[] b) {
        StringBuilder res = new StringBuilder();
        for(int i = 0; i < b.length; i++) {
            int x = getByte((int)b[i]);
            res.append(Character.forDigit(x / 16, 16));
            res.append(Character.forDigit(x % 16, 16));
        }
        return res.toString();
    }

    private String addressFromBytes(byte[] bytes) {
        StringBuilder res = new StringBuilder("");
        for (byte b : bytes) {
            res.append(Integer.toString(getByte((int)b)) + ".");
        }
        return res.substring(0, res.length()-1);
    }

    private int getByte(int i) {
        return i < 0 ? 256 + i : i;
    }

    public Diagram(byte[] data) {
        source_add = addressFromBytes(Arrays.copyOfRange(data, 12, 16));
        source_port = ByteBuffer.wrap(Arrays.copyOfRange(data, 20, 24)).getInt();
        destination_port = ByteBuffer.wrap(Arrays.copyOfRange(data, 24, 28)).getInt();
        checksum = byteToHexString(Arrays.copyOfRange(data, 30, 32));
        length = ByteBuffer.wrap(Arrays.copyOfRange(data, 32, 36)).getInt();
        isACK = ByteBuffer.wrap(Arrays.copyOfRange(data, 36, 40)).getInt() == 1;
        message = new String(Arrays.copyOfRange(data, 40, 40+length));
//        sh(intToByteArray(computeChecksum(toByte())));
    }

    private void sh(byte[] b) {
        for (int i = 0; i < b.length; i++)
            System.out.print(b[i] + " ");
        System.out.println();
    }

    public Diagram(int s, int d, int l, boolean a, String m) {
        source_port = s;
        destination_port = d;
        checksum = "0000";
        length = l;
        isACK = a;
        message = m;
        checksum = byteToHexString(Arrays.copyOfRange(intToByteArray(computeChecksum(toByte())), 2, 4));
    }


    private int getOneComplement(int i) {
        return (int)Math.pow(2, 16) - 1 - i;
    }

    private int getSum(int a, int b) {
        a += b;
        b = a/(int)Math.pow(2, 16);
        a = a%(int)Math.pow(2, 16) + b;
        return a;
    }

    public int computeChecksum(byte[] a) {
        int n = a.length;
        int res = 0;
        for(int i = 0; i < n/2; i++) {
            int x = getByte(a[2*i]);
            int y = getByte(a[2*i+1]);
            res = getSum(res, x * 256 + y);
        }
        if(n % 2 != 0)
            res = getSum(res, getByte(a[n-1]));
        return getOneComplement(res);
    }

    public boolean checkChecksum() {
        return computeChecksum(toByte()) == 0;
    }

    private byte[] intToByteArray(int value) {
        ByteBuffer b = ByteBuffer.allocate(4);
        b.putInt(value);
        return b.array();
    }

    private byte[] concat(byte[] a, byte[] b) {
        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

    public byte[] toByte() {
        byte[] res = new byte[0];
        byte[] source_byte = intToByteArray(source_port);
        res = concat(res, source_byte);
        byte[] destination_byte = intToByteArray(destination_port);
        res = concat(res, destination_byte);
        byte[] cs_byte = hexStringToByte(checksum);
        cs_byte = concat(hexStringToByte("0000"), cs_byte);
        res = concat(res, cs_byte);
        byte[] len_byte = intToByteArray(length);
        res = concat(res, len_byte);
        byte[] isACK_byte = intToByteArray(isACK ? 1 : 2);
        res = concat(res, isACK_byte);
        byte[] mess_byte = message.getBytes();
        res = concat(res, mess_byte);
        return res;
    }

    void show(){
        System.out.println("Whatssup");
        System.out.println(source_port + "-" + destination_port + "-" + length + "-" + isACK + "-" + checksum + "-" + message);
    }
}
