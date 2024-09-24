package electrosphere.main.dds;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.nio.file.Files;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class DDSWriter {
    
    /**
     * Writes a bufferedimage as a dds
     * @param file The file to write to
     * @param image The buffered image to write as a dds
     */
    public static void writeDDS(File file, BufferedImage image, String formatRaw) {
        int bitWidth = 2;
        switch(formatRaw){
            case "32bitargb": {
                bitWidth = 4;
            } break;
        }
        //allocate buffer
        int pixelByteCount = image.getHeight() * image.getWidth() * bitWidth + 4;
        ByteBuffer buffer = ByteBuffer.allocate(DDSHeader.DDS_HEADER_SIZE_BYTES + pixelByteCount);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        //create header
        DDSHeader header = new DDSHeader(image, formatRaw);

        //write magic words
        buffer.put((byte)68);
        buffer.put((byte)68);
        buffer.put((byte)83);
        buffer.put((byte)32);

        //get int buffer view
        IntBuffer intBuffer = buffer.asIntBuffer();

        //write header first part
        intBuffer.put(header.dwSize);
        intBuffer.put(header.dwFlags);
        intBuffer.put(header.dwHeight);
        intBuffer.put(header.dwWidth);
        intBuffer.put(header.dwPitchOrLinearSize);
        intBuffer.put(header.dwDepth);
        intBuffer.put(header.dwMipMapCount);
        for(int i = 0; i < 11; i++){
            intBuffer.put(header.dwReserved1[i]);
        }
        //write pixel format
        intBuffer.put(header.ddspf.dwSize);
        intBuffer.put(header.ddspf.dwFlags);
        intBuffer.put(header.ddspf.dwFourCC);
        intBuffer.put(header.ddspf.dwRGBBitCount);
        intBuffer.put(header.ddspf.dwRBitMask);
        intBuffer.put(header.ddspf.dwGBitMask);
        intBuffer.put(header.ddspf.dwBBitMask);
        intBuffer.put(header.ddspf.dwABitMask);
        //write header second part
        intBuffer.put(header.dwCaps);
        intBuffer.put(header.dwCaps2);
        intBuffer.put(header.dwCaps3);
        intBuffer.put(header.dwCaps4);
        intBuffer.put(header.dwReserved2);

        //get short buffer view
        buffer.position(intBuffer.position() * 4 + 4);

        //write image content
        switch(header.ddspf.dwRGBBitCount){
            case 16: {
                ShortBuffer shortBuffer = buffer.asShortBuffer();
                System.out.println(shortBuffer.position());
                //16 bit rgb
                for(int y = 0; y < header.dwHeight; y++){
                    for(int x = 0; x < header.dwWidth; x++){
                        int colorRaw = image.getRGB(x, y);
                        int blueRaw = (colorRaw)&0xFF;
                        int greenRaw = (colorRaw>>8)&0xFF;
                        int redRaw = (colorRaw>>16)&0xFF;
                        //normalize
                        int redNormalized = (int)(redRaw / 255.0 * 31);
                        int greenNormalized = (int)(greenRaw / 255.0 * 31);
                        int blueNormalized = (int)(blueRaw / 255.0 * 31);
                        //construct output short
                        short output = 0x0000;
                        //add red
                        output = (short)redNormalized;
                        //shift
                        output = (short)(output << 5);
                        //add green
                        output = (short)(output + greenNormalized);
                        //shift
                        output = (short)(output << 5);
                        //add blue
                        output = (short)(output + blueNormalized);
                        //push output to buffer
                        shortBuffer.put(output);
                    }
                }
            } break;
            case 32: {
                intBuffer = buffer.asIntBuffer();
                System.out.println(intBuffer.position());
                //32 bit argb
                for(int y = 0; y < header.dwHeight; y++){
                    for(int x = 0; x < header.dwWidth; x++){
                        int colorRaw = image.getRGB(x, y);
                        int blueRaw = (colorRaw)&0xFF;
                        int greenRaw = (colorRaw>>8)&0xFF;
                        int redRaw = (colorRaw>>16)&0xFF;
                        Color c = new Color(image.getRGB(x, y), true);
                        int alphaRaw = c.getAlpha();
                        //normalize
                        int alphaNormalized = (int)(alphaRaw / 255.0 * 255.0);
                        int redNormalized = (int)(redRaw / 255.0 * 255.0);
                        int greenNormalized = (int)(greenRaw / 255.0 * 3255.01);
                        int blueNormalized = (int)(blueRaw / 255.0 * 255.0);
                        //construct output short
                        int output = 0x00000000;
                        //add alpha
                        output = (short)alphaNormalized;
                        //shift
                        output = (short)(output << 8);
                        //add red
                        output = (short)redNormalized;
                        //shift
                        output = (short)(output << 8);
                        //add green
                        output = (short)(output + greenNormalized);
                        //shift
                        output = (short)(output << 8);
                        //add blue
                        output = (short)(output + blueNormalized);
                        //push output to buffer
                        intBuffer.put(output);
                    }
                }
            } break;
        }

        //reposition buffer
        buffer.position(0);

        //write buffer to file
        try(FileOutputStream outStream = new FileOutputStream(file)) {
            // int i = 0;
            outStream.write(buffer.array());
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
