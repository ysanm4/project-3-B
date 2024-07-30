import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class TaskTwoHash {

    public static void main(String[] args) throws NoSuchAlgorithmException {

        byte[] data = "Initial data".getBytes();
        String birthdateHex = "04272003";


        byte[] zeroBits = new byte[32];
        byte[] dataPlusZeroBits = new byte[data.length + zeroBits.length];
        System.arraycopy(data, 0, dataPlusZeroBits, 0, data.length);
        System.arraycopy(zeroBits, 0, dataPlusZeroBits, data.length, zeroBits.length);


        findVanityHash(dataPlusZeroBits, birthdateHex);
    }

    private static void findVanityHash(byte[] data, String birthdateHex) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        long counter = 0;
        int matchLength = 1;

        while (true) {

            byte[] counterBytes = longToBytes(counter);
            System.arraycopy(counterBytes, 0, data, data.length - counterBytes.length, counterBytes.length);


            byte[] hash = digest.digest(data);


            if (hashMatchesPrefix(hash, birthdateHex, matchLength)) {
                System.out.printf("Match found after %d iterations: %s%n", counter, bytesToHex(hash));
                matchLength++;
                if (matchLength > birthdateHex.length()) break; // Found complete birthdate
            }

            counter++;
            if (counter % 1000000 == 0) {
                System.out.printf("Checking %d hash...%n", counter);
            }
        }
    }

    private static boolean hashMatchesPrefix(byte[] hash, String prefix, int length) {
        String hashHex = bytesToHex(hash);
        return hashHex.startsWith(prefix.substring(0, length));
    }

    private static byte[] longToBytes(long value) {
        byte[] result = new byte[8];
        for (int i = 7; i >= 0; i--) {
            result[i] = (byte) (value & 0xFF);
            value >>= 8;
        }
        return result;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
