package electrosphere.main.dds;

import java.nio.ByteBuffer;

import electrosphere.main.Utils;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;

public class DDSDataParser {

    static ByteBuffer shortBuffer = ByteBuffer.allocate(2);
    static ByteBuffer intBuffer = ByteBuffer.allocate(4);

    /**
     * Parses a DDS image from an arbitrary buffer of bytes
     * @param buffer The buffer of bytes that contains a dds image
     * @return The bufferedimage representing the dds image
     */
    public static BufferedImage parseDDSBytes(ByteBuffer buffer){
        //read magic word
        for(int i = 0; i < 4; i++){
            buffer.get();
        }
        //parse header
        DDSHeader ddsHeader = new DDSHeader(buffer);

        System.out.println("Image size: " + ddsHeader.getWidth() + " x " + ddsHeader.getHeight());
        BufferedImage img = null;

        //based on format type, parse image pixel content
        switch(ddsHeader.getPixelFormat().getDWFlags()){
            case DDSPixelFormat.SURFACE_DATA_TYPE_DDPF_RGB: {
                img = DDSDataParser.parseUncompressedRGB(ddsHeader,buffer);
            } break;
            case DDSPixelFormat.SURFACE_DATA_TYPE_DDPF_RGBA: {
                img = DDSDataParser.parseUncompressedRGBA(ddsHeader,buffer);
            } break;
            default: {
                System.err.println("DDS Pixel format in unhandled format: " + ddsHeader.getPixelFormat().getDWFlags());
            } break;
        }

        return img;
    }
    
    /**
     * Creates a buffered image from pixel data stored in buffer based on information in a rgb dds header
     * @param header The header for the dds image
     * @param buffer The buffer containing pixel data
     * @return The bufferedimage object representing the pixel data in buffer
     */
    public static BufferedImage parseUncompressedRGB(DDSHeader header, ByteBuffer buffer){
        int width = header.getWidth();
        int height = header.getHeight();
        BufferedImage img = new BufferedImage(header.getWidth(), header.getHeight(), ColorSpace.TYPE_RGB);
        System.out.println("Size: " + header.ddspf.dwSize);
        System.out.println("RGB Bit Size: " + header.ddspf.dwRGBBitCount);
        //if 16 bit
        if(header.ddspf.dwRGBBitCount == 16){
            //get color bit shifts, masks, and maximum values
            short rbitMask = (short)header.ddspf.dwRBitMask;
            short gbitMask = (short)header.ddspf.dwGBitMask;
            short bbitMask = (short)header.ddspf.dwBBitMask;
            int redShifts = getNumberShifts(rbitMask);
            int greenShifts = getNumberShifts(gbitMask);
            int blueShifts = getNumberShifts(bbitMask);
            short redMax =   (short)(rbitMask >> redShifts);
            short greenMax = (short)(gbitMask >> greenShifts);
            short blueMax =  (short)(bbitMask >> blueShifts);

            System.out.println("Masks for r,g,b");
            Utils.printHex(rbitMask);
            System.out.println();
            Utils.printHex(gbitMask);
            System.out.println();
            Utils.printHex(bbitMask);
            System.out.println();
            System.out.println("Number of shifts to normalize each type: " + redShifts + " " + greenShifts + " " + blueShifts);
            System.out.println("Resulting in max values of: " + redMax + " " + greenMax + " " + blueMax);

            //for each pixel, get the color for the pixel and put it into the buffered image
            for(int y = 0; y < height; y++){
                for(int x = 0; x < width; x++){
                    shortBuffer.position(1);
                    shortBuffer.put(buffer.get());
                    shortBuffer.position(0);
                    shortBuffer.put(buffer.get());
                    shortBuffer.position(0);
                    short valueRaw = shortBuffer.asShortBuffer().get();
                    short redRaw =   (short)((valueRaw & rbitMask) >> redShifts);
                    short greenRaw = (short)((valueRaw & gbitMask) >> greenShifts);
                    short blueRaw =  (short)((valueRaw & bbitMask) >> blueShifts);
                    int red = (int)(((float)redRaw) / ((float)redMax) * 255);
                    int green = (int)(((float)greenRaw) / ((float)greenMax) * 255);
                    int blue = (int)(((float)blueRaw) / ((float)blueMax) * 255);
                    img.setRGB(x, y, Utils.getIntFromColor(red, green, blue));
                }
            }
        }
        return img;
    }

    /**
     * Creates a buffered image from pixel data stored in buffer based on information in a rgba dds header
     * @param header The header for the dds image
     * @param buffer The buffer containing pixel data
     * @return The bufferedimage object representing the pixel data in buffer
     */
    public static BufferedImage parseUncompressedRGBA(DDSHeader header, ByteBuffer buffer){
        int width = header.getWidth();
        int height = header.getHeight();
        BufferedImage img = new BufferedImage(header.getWidth(), header.getHeight(), ColorSpace.TYPE_RGB);
        System.out.println("Size: " + header.ddspf.dwSize);
        System.out.println("RGB Bit Size: " + header.ddspf.dwRGBBitCount);
        //if 16 bit
        if(header.ddspf.dwRGBBitCount == 32){
            //get color bit shifts, masks, and maximum values
            int rbitMask = header.ddspf.dwRBitMask;
            int gbitMask = header.ddspf.dwGBitMask;
            int bbitMask = header.ddspf.dwBBitMask;
            int redShifts = getNumberShifts(rbitMask);
            int greenShifts = getNumberShifts(gbitMask);
            int blueShifts = getNumberShifts(bbitMask);
            int redMax =   (rbitMask >> redShifts);
            int greenMax = (gbitMask >> greenShifts);
            int blueMax =  (bbitMask >> blueShifts);

            System.out.println("Masks for r,g,b");
            Utils.printHex(rbitMask);
            System.out.println();
            Utils.printHex(gbitMask);
            System.out.println();
            Utils.printHex(bbitMask);
            System.out.println();
            System.out.println("Number of shifts to normalize each type: " + redShifts + " " + greenShifts + " " + blueShifts);
            System.out.println("Resulting in max values of: " + redMax + " " + greenMax + " " + blueMax);

            //for each pixel, get the color for the pixel and put it into the buffered image
            for(int y = 0; y < height; y++){
                for(int x = 0; x < width; x++){
                    intBuffer.position(3);
                    intBuffer.put(buffer.get());
                    intBuffer.position(2);
                    intBuffer.put(buffer.get());
                    intBuffer.position(1);
                    intBuffer.put(buffer.get());
                    intBuffer.position(0);
                    intBuffer.put(buffer.get());
                    intBuffer.position(0);
                    int valueRaw = intBuffer.asIntBuffer().get();
                    int redRaw =   ((valueRaw & rbitMask) >> redShifts);
                    int greenRaw = ((valueRaw & gbitMask) >> greenShifts);
                    int blueRaw =  ((valueRaw & bbitMask) >> blueShifts);
                    int red = (int)(((float)redRaw) / ((float)redMax) * 255);
                    int green = (int)(((float)greenRaw) / ((float)greenMax) * 255);
                    int blue = (int)(((float)blueRaw) / ((float)blueMax) * 255);
                    img.setRGB(x, y, Utils.getIntFromColor(red, green, blue));
                }
            }
        }
        return img;
    }

    /**
     * Counts the number of right shifts until the mask loses a 1
     * @param inputMask The bit mask
     * @return The number of shifts
     */
    static int getNumberShifts(short inputMask){
        int numShifts = 0;
        short iterator = inputMask;
        int startingBits = Utils.numberOfBits(iterator);
        iterator = (short)(iterator >> 1);
        //while we haven't shifted valid bits..
        while(Utils.numberOfBits(iterator) == startingBits){
            iterator = (short)(iterator >> 1);
            numShifts++;
        }
        return numShifts;
    }

    /**
     * Counts the number of right shifts until the mask loses a 1
     * @param inputMask The bit mask
     * @return The number of shifts
     */
    static int getNumberShifts(int inputMask){
        int numShifts = 0;
        int iterator = inputMask;
        int startingBits = Utils.numberOfBits(iterator);
        iterator = (iterator >> 1);
        //while we haven't shifted valid bits..
        while(Utils.numberOfBits(iterator) == startingBits){
            iterator = (iterator >> 1);
            numShifts++;
        }
        return numShifts;
    }

}

