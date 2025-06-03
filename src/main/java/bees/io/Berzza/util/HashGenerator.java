package bees.io.Berzza.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashGenerator {

    private static String salt = "100af1b49f5e9f87efc81f838bf9b1f5e38293e5b4cf6d0b366c004e0a8d9987";

    public static String generateHash(String seed) {
        try {
            // Create a SHA-256 message digest
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Update the digest with the seed's bytes
            byte[] hashBytes = digest.digest(seed.getBytes());

            // Convert the hash bytes to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    public static String getPrevGame(String hashCode) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(hashCode.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hash);
    }

    public static double getResult(String gameHash) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(gameHash.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKey);
        byte[] hmacResult = mac.doFinal(salt.getBytes(StandardCharsets.UTF_8));
        String hmacHex = bytesToHex(hmacResult);

        BigInteger h = new BigInteger(hmacHex, 16);
        BigInteger e = BigInteger.valueOf(2).pow(52);

        if (h.mod(BigInteger.valueOf(33)).equals(BigInteger.ZERO)) {
            return 1.0;
        }

        BigInteger numerator = e.multiply(BigInteger.valueOf(100)).subtract(h);
        double result = numerator.divide(numerator.add(e)).doubleValue();
        return Math.floor(result * 100) / 100; // Round to two decimal places
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }


    public static int crashPointFromHash(String serverSeed, String salt) throws Exception {
        // Calculate the HMAC-SHA256 hash of the serverSeed using the salt
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(serverSeed.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKey);
        byte[] hashBytes = mac.doFinal(salt.getBytes(StandardCharsets.UTF_8));
        String hash = bytesToHex(hashBytes);

        // Calculate the crash point
        int hs = 100 / 4;
        if (isDivisible(hash, hs)) {
            return 1;
        }

        long h = Long.parseLong(hash.substring(0, 52 / 4), 16);
        double e = Math.pow(2, 52);

        return (int) Math.floor((100 * e - h) / 100.0);
    }

    private static boolean isDivisible(String hash, int hs) {
        long hashValue = Long.parseLong(hash, 16);
        return hashValue % hs == 0;
    }
}
