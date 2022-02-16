package ourEncryption.Huffman;

import java.io.FileOutputStream;
import java.io.IOException;

class BitOutputStream {

    private FileOutputStream out;
    private boolean[] buffer = new boolean[8];
    private int count = 0;

    public BitOutputStream(FileOutputStream is) {
        this.out = is;
    }

    public void write(boolean x) throws IOException {
        this.count++;
        this.buffer[8-this.count] = x;
        if (this.count == 8){
            int num = 0;
            for (int index = 0; index < 8; index++){
                num = 2*num + (this.buffer[index] ? 1 : 0);
            }

            this.out.write(num - 128);

            this.count = 0;
        }
    }
    
    
    public void write(byte x) throws IOException {
       
            this.out.write(x - 128);

        
    }

    public void writeFirstPartOfHeader(StringBuilder header) throws IOException {
      for (int i =0 ;i<header.length() ; i++)
      {
    	  out.write((byte)header.charAt(i));
      }
    }
    public void close() throws IOException {
        int num = 0;
        for (int index = 0; index < 8; index++){
            num = 2*num + (this.buffer[index] ? 1 : 0);
        }

        this.out.write(num - 128);

        this.out.close();
    }
    
    public void clean() throws IOException {
        int num = 0;
        for (int index = 0; index < 8; index++){
            num = 2*num + (this.buffer[index] ? 1 : 0);
        }

        this.out.write(num - 128);
    }

}