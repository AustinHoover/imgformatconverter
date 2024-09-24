package electrosphere.main.dds;

import java.nio.ByteBuffer;

import java.awt.image.BufferedImage;

import electrosphere.main.Utils;

public class DDSHeader {
    public static final int DDS_HEADER_SIZE_BYTES = 92 + DDSPixelFormat.DDS_PIXEL_FORMAT_SIZE_BYTES;

    static final int SIZE_STATIC_NUMBER = 124;

    //Refer to https://learn.microsoft.com/en-us/windows/win32/direct3ddds/dds-header
    //for detailed documentation on what all of these fields are

    int dwSize = SIZE_STATIC_NUMBER;
    int dwFlags = 659463;
    int dwHeight = 210;
    int dwWidth = 156;
    int dwPitchOrLinearSize = 65520;
    int dwDepth = 0;
    int dwMipMapCount = 0;
    int[] dwReserved1 = new int[]{0,0,0,0,0,0,0,0,0,0,0};
    DDSPixelFormat ddspf;
    int dwCaps = 4198408;
    int dwCaps2 = 0;
    int dwCaps3 = 0;
    int dwCaps4 = 0;
    int dwReserved2 = 0;
    
    //reads a dds header from the buffer passed in
    public DDSHeader(ByteBuffer buffer){
        dwSize = Utils.readDWord(buffer);
        dwFlags = Utils.readDWord(buffer);
        dwHeight = Utils.readDWord(buffer);
        dwWidth = Utils.readDWord(buffer);
        dwPitchOrLinearSize = Utils.readDWord(buffer);
        dwDepth = Utils.readDWord(buffer);
        dwMipMapCount = Utils.readDWord(buffer);
        for(int i = 0; i < 11; i++){
            dwReserved1[i] = Utils.readDWord(buffer);
        }
        ddspf = new DDSPixelFormat(buffer);
        dwCaps = Utils.readDWord(buffer);
        dwCaps2 = Utils.readDWord(buffer);
        dwCaps3 = Utils.readDWord(buffer);
        dwCaps4 = Utils.readDWord(buffer);
        dwReserved2 = Utils.readDWord(buffer);
    }

    public DDSHeader(BufferedImage img, String formatRaw){
        ddspf = new DDSPixelFormat(formatRaw);
        dwWidth = img.getWidth();
        dwHeight = img.getHeight();
        int bytesPerPixel = 2;
        switch(formatRaw){
            case "32bitargb": {
                bytesPerPixel = 4;
            } break;
        }
        dwPitchOrLinearSize = ( dwWidth * dwHeight * bytesPerPixel );
        System.out.println("Pitch or linear size: " + dwPitchOrLinearSize);
    }

    public int getWidth(){
        return dwWidth;
    }

    public int getHeight(){
        return dwHeight;
    }

    public DDSPixelFormat getPixelFormat(){
        return ddspf;
    }

}
