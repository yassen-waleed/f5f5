package ourEncryption.RC4;

import java.io.*;

public class Methods {
    public static String readFile(File file) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(file));
        String st = "";
        String line;
        while ((line = br.readLine()) != null) {
            st += line + "\n";

        }
        return st;
    }

    public static void WriteString(File f, String output) throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter(f));
        writer.write(output);
        writer.close();
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


