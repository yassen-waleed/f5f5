package ourEncryption.RC4;

import sun.net.www.content.text.plain;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class RC4 {
    private final static int LENGTH = 256;
    static int s[] = new int[LENGTH];
    static int k[] = new int[LENGTH];


    public static void encryption_decryption(File f1, File output) throws IOException {

        int temp;
        final String key = "MoathJardat";

        byte[] planeBytes = new byte[(int) f1.length()];
        readFile(f1, planeBytes);
        String plain = fillString(planeBytes);

        int cipher[] = new int[plain.length()];
        int plaini[] = stringToInt(plain);
        int keyi[] = stringToInt(key);
        for (int i = 0; i < LENGTH; i++) {
            s[i] = i;
            k[i] = keyi[i % key.length()];
        }

        int j = 0;
        for (int i = 0; i < LENGTH; i++) {
            j = (j + s[i] + k[i]) % LENGTH;
            temp = s[i];
            s[i] = s[j];
            s[j] = temp;
        }
        int i, k1;
        j = 0;
        for (int l = 0; l < plain.length(); l++) {
            i = (l + 1) % LENGTH;
            j = (j + s[i]) % LENGTH;
            temp = s[i];
            s[i] = s[j];
            s[j] = temp;
            k1 = s[(s[i] + s[j]) % LENGTH];
            cipher[l] = k1 ^ plaini[l];


        }
        String out = intToString(cipher, plain.length());
        byte[] encryptedBytes = new byte[out.length()];
        for ( i = 0 ; i < encryptedBytes.length ; i++)
            encryptedBytes[i] = (byte) out.charAt(i);
        Methods.write(encryptedBytes, output);

    }

    private static String intToString(int[] output, int length) {
        char t[] = new char[output.length];
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            t[i] = (char) output[i];
            builder.append(t[i]);

        }
        return builder.toString();

    }

    private static int[] stringToInt(String plain) {
        char t[] = plain.toCharArray();
        int arrayInt[] = new int[plain.length()];
        for (int i = 0; i < plain.length(); i++) {
            arrayInt[i] = (int) t[i];
        }
        return arrayInt;
    }

    private static void readFile(File file, byte[] planeBytes) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            inputStream.read(planeBytes);
            inputStream.close();
        }
    }

    private static String fillString(byte[] planeBytes) throws IOException {
        StringBuilder planeText = new StringBuilder();
        for (int i = 0; i < planeBytes.length; i++)
            planeText.append((char) planeBytes[i]);

        return planeText.toString();

    }
}

