package ourEncryption.hillCipher;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Hill {

    public static void encrypt(File file, File file2) throws IOException {
        List<Byte> plainText = readFile(file);
        StringBuilder cipherText = HillCalculation.hillCipher(plainText, HillType.ENCRYPT);



        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file2))) {
            bw.write(String.valueOf(cipherText));//Internally it does aSB.toString();
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void decrypt(File file, File file2) throws IOException {
        List<Byte> cipherText = readFile(file);
        StringBuilder plainText = HillCalculation.hillCipher(cipherText, HillType.DECRYPT);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file2))) {
            bw.write(String.valueOf(plainText));//Internally it does aSB.toString();
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<Byte> readFile(File file) throws IOException {
        byte[] planeBytes = new byte[(int) file.length()];
        // read file as bytes
        try (FileInputStream inputStream = new FileInputStream(file)) {
            inputStream.read(planeBytes);
        }

        ArrayList<Byte> result = new ArrayList<>();
        for (int i = 0; i < planeBytes.length; i++) {
            result.add(planeBytes[i]);
        }
        return result;
    }


}

