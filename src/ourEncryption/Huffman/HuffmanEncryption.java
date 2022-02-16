package ourEncryption.Huffman;

import java.io.*;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class HuffmanEncryption {
    static byte[] planeBytes;
    static int[] frequencyOfBytes;
    static int numberOfCharacter;
    private static int headLength = 0;
    private static PriorityQueue<HuffmanNode> minHeap;
    private static HuffmanNode huffmanTreeRoot;
    private static ArrayList<Node> huffmanCodes;
    private static String dataLength = "";
    private static String headerLength = "";
    private static String header = "";
    private static String extension = "";


    public static void encrypt(File planFile, File file) throws IOException {

        String nameOfFile = planFile.getName().split("\\.")[1];
        readFile(planFile);
        FileOutputStream outStream = new FileOutputStream(file);

        BinaryStreamOut binaryStream = new BinaryStreamOut(outStream);
        BitOutputStream bitStream = new BitOutputStream(outStream);

        initializePriorityQueue();
        buildHuffmanTree();
        getHeaderLength(huffmanTreeRoot);

        bitStream.writeFirstPartOfHeader(new StringBuilder(nameOfFile + ":" + headLength + ":"));
        buildHeader(huffmanTreeRoot, binaryStream);

        huffmanCodes = addCode(huffmanTreeRoot);

        writeCompressedData(bitStream, binaryStream);


        byte[] encryptedArrayOfBytes = new byte[(int) file.length()];
        try (FileInputStream inputStream = new FileInputStream(file)) {
            inputStream.read(encryptedArrayOfBytes);
        }
    }

    public static void decrypt(File encryptedFile, File decryptedFile) throws IOException {
        BinaryStreamIn binaryRead = new BinaryStreamIn(encryptedFile);
        splitHeader(binaryRead);
        decryptedFile.delete();
        decryptedFile = new File(decryptedFile.getAbsoluteFile()+"."+extension);


        if (decryptedFile != null) {
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(decryptedFile);
                BinaryStreamOut bout = new BinaryStreamOut(out);


                long datalength = Long.parseLong(dataLength);

                int length = Integer.parseInt(headerLength);

                StringBuilder header = new StringBuilder();

                for (int i = 0; i < length; i++) {
                    boolean b = binaryRead.readBoolean();
                    if (b)
                        header.append("1");
                    else
                        header.append("0");

                }

                HuffmanEncryption.header = header.toString();

                HuffmanNode root = decodeHeader();

                for (int i = 0; i < datalength; i++) {
                    HuffmanNode node = root;
                    while (true) {
                        assert node != null;
                        if (node.isLeaf()) break;
                        boolean bit = binaryRead.readBoolean();
                        if (bit)
                            node = node.getRight();
                        else
                            node = node.getLeft();
                    }
                    bout.write(node.getVal());
                }
                bout.close();
                out.close();
                binaryRead.close();

            } catch (FileNotFoundException e1) {
                System.out.println("File not found" + e1);
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException ioe) {
                    System.out.println("Error while closing stream: " + ioe);
                }

            }
        }

    }


    private static void readFile(File file) throws IOException {

        planeBytes = new byte[(int) file.length()];
        // read file as bytes
        try (FileInputStream inputStream = new FileInputStream(file)) {
            inputStream.read(planeBytes);
            inputStream.close();
        }
        frequencyOfBytes = new int[256];
        for (byte aByte : planeBytes)
            frequencyOfBytes[aByte + 128]++;

        for (int j : frequencyOfBytes)
            if (j > 0)
                numberOfCharacter++;
    }

    private static void initializePriorityQueue() {
        minHeap = new PriorityQueue<>();

        for (int i = 0; i < frequencyOfBytes.length; i++)
            if (frequencyOfBytes[i] > 0)
                minHeap.add(new HuffmanNode(frequencyOfBytes[i], (byte) i, true));

    }

    private static void buildHuffmanTree() {
        while (minHeap.size() > 1) {
            HuffmanNode node = new HuffmanNode(0);

            HuffmanNode left = minHeap.poll();
            HuffmanNode right = minHeap.poll();
            node.addLift(left);
            node.addRight(right);
            minHeap.add(node);
        }
        huffmanTreeRoot = minHeap.peek();
    }

    private static void getHeaderLength(HuffmanNode huffmanTreeRoot) {
        if (huffmanTreeRoot == null)
            return;
        if (huffmanTreeRoot.isLeaf()) {
            headLength += 9;
        } else {
            headLength++;
            getHeaderLength(huffmanTreeRoot.getLeft());
            getHeaderLength(huffmanTreeRoot.getRight());
        }

    }

    private static ArrayList<Node> addCode(HuffmanNode root) {
        ArrayList<Node> huffmanCodes = new ArrayList<>();
        String s = ("");
        addCode(root, s, huffmanCodes);

        return huffmanCodes;

    }

    private static void addCode(HuffmanNode root, String s, ArrayList<Node> huffmanCodes) {
        if (!(root.isLeaf())) {
            String sl = s + "0";
            String sr = s + "1";
            addCode(root.getLeft(), sl, huffmanCodes);
            addCode(root.getRight(), sr, huffmanCodes);
        } else {
            huffmanCodes.add(new Node(s, root));
        }
    }

    private static void buildHeader(HuffmanNode huffmanTreeRoot, BinaryStreamOut bos) {
        if (huffmanTreeRoot.isLeaf()) {
            bos.write(true);
            String s = getByteBinaryString(huffmanTreeRoot.getVal());
            for (int i = 0; i < s.length(); i++)
                bos.write(s.charAt(i) == '1');
            return;
        }

        bos.write(false);
        buildHeader(huffmanTreeRoot.getLeft(), bos);
        buildHeader(huffmanTreeRoot.getRight(), bos);
    }

    private static String getByteBinaryString(byte b) {

        StringBuilder sb = new StringBuilder();
        for (int i = 7; i >= 0; --i) {
            sb.append(b >>> i & 1);
        }
        return sb.toString();
    }

    private static void writeCompressedData(BitOutputStream bitStream, BinaryStreamOut binaryStream) throws IOException {

        StringBuilder s = new StringBuilder();

        for (byte aByte : planeBytes)
            for (Node huffmanCode : huffmanCodes)
                if (aByte == huffmanCode.getVal().getVal()) {
                    s.append(huffmanCode.gethCode());
                    break;
                }
        bitStream.writeFirstPartOfHeader(new StringBuilder(planeBytes.length + ":"));

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '1')
                binaryStream.write(true);
            else if (s.charAt(i) == '0')
                binaryStream.write(false);
        }

        binaryStream.close();
    }

    private static void splitHeader(BinaryStreamIn binaryRead) {
        char c;
        extension = "";
        while (true) {
            c = (char) binaryRead.readChar();
            if (c == ':')
                break;
            else
                extension += c;
        }
        while (true) {
            c = (char) binaryRead.readByte();
            if (c == ':')
                break;
            else
                headerLength += c;
        }

        while (true) {
            c = (char) binaryRead.readByte();
            if (c == ':')
                break;
            else
                dataLength += c;
        }

    }

    private static HuffmanNode decodeHeader() {

        String nextChar = getIndexAndRemoveIt(1);
        if (nextChar.isEmpty())
            return null;

        return nextChar.equals("1")
                ? new HuffmanNode((byte) ((Integer.parseInt(getIndexAndRemoveIt(8), 2)) - 128), -1, null, null, true)
                : new HuffmanNode(decodeHeader(), decodeHeader());
    }

    private static String getIndexAndRemoveIt(int index) {
        if (index > header.length())
            return "";
        String charAtIndex = header.substring(0, index);
        header = header.substring(index);
        return charAtIndex;
    }


}