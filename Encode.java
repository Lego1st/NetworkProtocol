import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by root on 27/05/2017.
 */
public class Encode {
    private static String message;

    public Encode(String m) {
        message = m;
    }

    public static String getCode(String message, String code) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] byte_of_message = message.getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance(code);
        BigInteger bigInt = new BigInteger(1, md.digest(byte_of_message));
        String hash_text = bigInt.toString(16);
        return hash_text;
    }
}
