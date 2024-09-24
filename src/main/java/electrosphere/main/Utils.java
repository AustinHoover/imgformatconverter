package electrosphere.main;

import java.nio.ByteBuffer;

public class Utils {

    static ByteBuffer intConverterBuffer = ByteBuffer.allocate(4);

    public static void printByte(byte toPrint){
        System.out.print(String.format("%02X",toPrint));
    }

    /**
     * Reads a DWORD (int) from a buffer
     * @param buffer The buffer to read from
     * @return The DWORD as an int
     */
    public static int readDWord(ByteBuffer buffer){
        intConverterBuffer.position(3);
        intConverterBuffer.put(buffer.get());
        intConverterBuffer.position(2);
        intConverterBuffer.put(buffer.get());
        intConverterBuffer.position(1);
        intConverterBuffer.put(buffer.get());
        intConverterBuffer.position(0);
        intConverterBuffer.put(buffer.get());
        intConverterBuffer.position(0);
        return intConverterBuffer.asIntBuffer().get();
    }

    public static void printIntHex(int input){
        intConverterBuffer.asIntBuffer().put(input);
        intConverterBuffer.position(0);
        for(int i = 0; i < 4; i++){
            printByte(intConverterBuffer.get());
        }
        intConverterBuffer.position(0);
    }

    public static void printHex(short input){
        intConverterBuffer.asShortBuffer().put(input);
        intConverterBuffer.position(0);
        for(int i = 0; i < 2; i++){
            printByte(intConverterBuffer.get());
        }
        intConverterBuffer.position(0);
    }

    public static void printHex(int input){
        intConverterBuffer.asIntBuffer().put(input);
        intConverterBuffer.position(0);
        for(int i = 0; i < 4; i++){
            printByte(intConverterBuffer.get());
        }
        intConverterBuffer.position(0);
    }

    /**
     * Counts the number of 1 bits in an int
     * @param input The int to sum
     * @return The number of 1 bits in the int
     */
    public static int numberOfBits(int input){
        int rVal = 0;
        rVal += (input & 0x00000001) > 0 ? 1 : 0;
        rVal += (input & 0x00000002) > 0 ? 1 : 0;
        rVal += (input & 0x00000004) > 0 ? 1 : 0;
        rVal += (input & 0x00000008) > 0 ? 1 : 0;
        rVal += (input & 0x00000010) > 0 ? 1 : 0;
        rVal += (input & 0x00000020) > 0 ? 1 : 0;
        rVal += (input & 0x00000040) > 0 ? 1 : 0;
        rVal += (input & 0x00000080) > 0 ? 1 : 0;
        rVal += (input & 0x00000100) > 0 ? 1 : 0;
        rVal += (input & 0x00000200) > 0 ? 1 : 0;
        rVal += (input & 0x00000400) > 0 ? 1 : 0;
        rVal += (input & 0x00000800) > 0 ? 1 : 0;
        rVal += (input & 0x00001000) > 0 ? 1 : 0;
        rVal += (input & 0x00002000) > 0 ? 1 : 0;
        rVal += (input & 0x00004000) > 0 ? 1 : 0;
        rVal += (input & 0x00008000) > 0 ? 1 : 0;
        
        rVal += (input & 0x00010000) > 0 ? 1 : 0;
        rVal += (input & 0x00020000) > 0 ? 1 : 0;
        rVal += (input & 0x00040000) > 0 ? 1 : 0;
        rVal += (input & 0x00080000) > 0 ? 1 : 0;
        rVal += (input & 0x00100000) > 0 ? 1 : 0;
        rVal += (input & 0x00200000) > 0 ? 1 : 0;
        rVal += (input & 0x00400000) > 0 ? 1 : 0;
        rVal += (input & 0x00800000) > 0 ? 1 : 0;
        rVal += (input & 0x01000000) > 0 ? 1 : 0;
        rVal += (input & 0x02000000) > 0 ? 1 : 0;
        rVal += (input & 0x04000000) > 0 ? 1 : 0;
        rVal += (input & 0x08000000) > 0 ? 1 : 0;
        rVal += (input & 0x10000000) > 0 ? 1 : 0;
        rVal += (input & 0x20000000) > 0 ? 1 : 0;
        rVal += (input & 0x40000000) > 0 ? 1 : 0;
        rVal += (input & 0x80000000) > 0 ? 1 : 0;
        return rVal;
    }

    /**
     * Counts the number of 1 bits in a short
     * @param input The short to sum
     * @return The number of 1 bits in the short
     */
    public static int numberOfBits(short input){
        int rVal = 0;
        rVal += (input & 0x0001) > 0 ? 1 : 0;
        rVal += (input & 0x0002) > 0 ? 1 : 0;
        rVal += (input & 0x0004) > 0 ? 1 : 0;
        rVal += (input & 0x0008) > 0 ? 1 : 0;
        rVal += (input & 0x0010) > 0 ? 1 : 0;
        rVal += (input & 0x0020) > 0 ? 1 : 0;
        rVal += (input & 0x0040) > 0 ? 1 : 0;
        rVal += (input & 0x0080) > 0 ? 1 : 0;
        rVal += (input & 0x0100) > 0 ? 1 : 0;
        rVal += (input & 0x0200) > 0 ? 1 : 0;
        rVal += (input & 0x0400) > 0 ? 1 : 0;
        rVal += (input & 0x0800) > 0 ? 1 : 0;
        rVal += (input & 0x1000) > 0 ? 1 : 0;
        rVal += (input & 0x2000) > 0 ? 1 : 0;
        rVal += (input & 0x4000) > 0 ? 1 : 0;
        rVal += (input & 0x8000) > 0 ? 1 : 0;
        return rVal;
    }

    /**
     * Counts the number of 1 bits in a byte
     * @param input The byte to sum
     * @return The number of 1 bits in the byte
     */
    public static int numberOfBits(byte input){
        int rVal = 0;
        rVal += (input & 0x01) > 0 ? 1 : 0;
        rVal += (input & 0x02) > 0 ? 1 : 0;
        rVal += (input & 0x04) > 0 ? 1 : 0;
        rVal += (input & 0x08) > 0 ? 1 : 0;
        rVal += (input & 0x10) > 0 ? 1 : 0;
        rVal += (input & 0x20) > 0 ? 1 : 0;
        rVal += (input & 0x40) > 0 ? 1 : 0;
        rVal += (input & 0x80) > 0 ? 1 : 0;
        return rVal;
    }

    //from https://stackoverflow.com/questions/18022364/how-to-convert-rgb-color-to-int-in-java
    public static int getIntFromColor(int Red, int Green, int Blue){
        Red = (Red << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
        Green = (Green << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
        Blue = Blue & 0x000000FF; //Mask out anything not blue.
    
        return 0xFF000000 | Red | Green | Blue; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
    }

}
