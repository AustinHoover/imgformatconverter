package electrosphere.main.dds;

import java.nio.ByteBuffer;

import electrosphere.main.Utils;

public class DDSPixelFormat {
    public static final int DDS_PIXEL_FORMAT_SIZE_BYTES = 8 * 4;

    static final int SIZE_MAGIC_NUMBER = 32;

    public static final int SURFACE_DATA_TYPE_DDPF_RGB = 64;
    //this is not a valid value for dwFlags according to Microsoft's documentation, but paradox supports it so we support it
    public static final int SURFACE_DATA_TYPE_DDPF_RGBA = 65;


    static final int RGB_16_BIT_MASK_R = 31744;
    static final int RGB_16_BIT_MASK_G = 992;
    static final int RGB_16_BIT_MASK_B = 31;
    static final int RGB_16_BIT_MASK_A = 0;
    static final int RGB_16_BIT_COUNT = 16;

    static final int ARGB_32_BIT_MASK_A = 0xFF000000;
    static final int ARGB_32_BIT_MASK_R = 0x00FF0000;
    static final int ARGB_32_BIT_MASK_G = 0x0000FF00;
    static final int ARGB_32_BIT_MASK_B = 0x000000FF;
    static final int ARGB_32_BIT_COUNT = 32;
    

    //Refer to https://learn.microsoft.com/en-us/windows/win32/direct3ddds/dds-pixelformat
    //for detailed documentation on what all of these fields are
    
    int dwSize = SIZE_MAGIC_NUMBER;
    int dwFlags = SURFACE_DATA_TYPE_DDPF_RGB;
    int dwFourCC = 0;
    int dwRGBBitCount = RGB_16_BIT_COUNT;
    int dwRBitMask = RGB_16_BIT_MASK_R;
    int dwGBitMask = RGB_16_BIT_MASK_G;
    int dwBBitMask = RGB_16_BIT_MASK_B;
    int dwABitMask = RGB_16_BIT_MASK_A;

    public DDSPixelFormat(ByteBuffer buffer){
        dwSize = Utils.readDWord(buffer);
        dwFlags = Utils.readDWord(buffer);
        dwFourCC = Utils.readDWord(buffer);
        dwRGBBitCount = Utils.readDWord(buffer);
        dwRBitMask = Utils.readDWord(buffer);
        dwGBitMask = Utils.readDWord(buffer);
        dwBBitMask = Utils.readDWord(buffer);
        dwABitMask = Utils.readDWord(buffer);
    }

    public DDSPixelFormat(String formatRaw){
        switch(formatRaw){
            case "32bitargb": {
                dwABitMask = ARGB_32_BIT_MASK_A;
                dwRBitMask = ARGB_32_BIT_MASK_R;
                dwGBitMask = ARGB_32_BIT_MASK_G;
                dwBBitMask = ARGB_32_BIT_MASK_B;
                dwRGBBitCount = ARGB_32_BIT_COUNT;
                dwFlags = SURFACE_DATA_TYPE_DDPF_RGBA;
            } break;
            default: {

            } break;
        }
    }

    public int getDWFlags(){
        return dwFlags;
    }



}
