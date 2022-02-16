package ourEncryption.Vigenere;


import java.io.*;

public class VigenereCipher {



    public static boolean isEmpty(byte [] dataInBytes) {
        return dataInBytes == null || dataInBytes.length < 1;
    }

    public static void encrypt(File file, File encryptedFile) throws IOException {

        final String key = "MoathJardat";
        byte[] dataInBytes;
        int counterForEncryption;
        dataInBytes = new byte[(int) file.length()];

        FileInputStream inputStream = new FileInputStream(file);
        inputStream.read(dataInBytes);
        inputStream.close();

        counterForEncryption = 0;

        if (isEmpty(dataInBytes))
            return;

        byte[] encryptedData = new byte[dataInBytes.length];
        for (int i = 0; i < dataInBytes.length; i++) {
            if (counterForEncryption == key.length())
                counterForEncryption = 0;

            byte byteBeforEncryption = dataInBytes[i];
            byte byteFromTheKey = (byte) key.charAt(counterForEncryption);
            byte byteAfterEncryption = (byte) ((byteBeforEncryption + byteFromTheKey) % 256);
            encryptedData[i] = byteAfterEncryption;
            counterForEncryption++;
        }
        write(encryptedData, encryptedFile);
    }

    public static void decrypt (File file, File decryptedFile) throws IOException {

        final String key = "MoathJardat";
        byte[] dataInBytes;
        int counterForDecryption;
        dataInBytes = new byte[(int) file.length()];

        FileInputStream inputStream = new FileInputStream(file);
        inputStream.read(dataInBytes);
        counterForDecryption = 0;


        if (isEmpty(dataInBytes))
            return;

        byte[] originalData = new byte[dataInBytes.length];

        for (int i = 0; i < dataInBytes.length; i++) {
            if (counterForDecryption == key.length())
                counterForDecryption = 0;

            byte byteBeforeDecryption = dataInBytes[i];
            byte byteFromKey = (byte) key.charAt(counterForDecryption);


            byte byteAfterDecryption = (byte) ((byteBeforeDecryption - byteFromKey) % 256);

            originalData[i] = byteAfterDecryption;
            counterForDecryption++;
        }
        write(originalData, decryptedFile);
    }

    public static void write(byte myByteArray[], File file) {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(myByteArray);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}