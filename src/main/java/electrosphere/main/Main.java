package electrosphere.main;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;

import javax.imageio.ImageIO;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import electrosphere.main.bmp.BMPWriter;
import electrosphere.main.dds.DDSDataParser;
import electrosphere.main.dds.DDSWriter;

import java.awt.image.BufferedImage;

public class Main {

    public static void main(String args[]){
        System.out.println("Hello, world!");

        String inputPathRaw = null;
        String outputPathRaw = null;
        String formatRaw = null;

        //parse command line arguments
        Options options = new Options();
        options.addOption("i","inputfile",true,"The full path to the input file");
        options.addOption("o","outputfile",true,"The full path to the output file");
        options.addOption("f","format",true,"The format to output in");
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine line = parser.parse(options, args);
            inputPathRaw = line.getOptionValue("i");
            outputPathRaw = line.getOptionValue("o");
            formatRaw = line.getOptionValue("f");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(inputPathRaw == null || outputPathRaw == null){
            System.err.println("Error! Input file or output file argument not provided.");
            System.exit(1);
        }

        String inputFormat = inputPathRaw.substring(inputPathRaw.length() - 3);
        String outputFormat = outputPathRaw.substring(outputPathRaw.length() - 3);

        System.out.println("Converting:");
        System.out.println(inputFormat + " => " + outputFormat);

        //read image in
        File file = new File(inputPathRaw);
        BufferedImage img = null;
        switch(inputFormat){
            case "tga":
            case "png": {
                try {
                    img = ImageIO.read(new File(inputPathRaw));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } break;
            case "dds": {
                try {
                    byte[] bytes = Files.readAllBytes(file.toPath());
                    ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
                    buffer.put(bytes);
                    buffer.flip();
                    img = DDSDataParser.parseDDSBytes(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } break;
        }
        
        //save image out
        if(img != null){
            switch(outputFormat){
                case "png": {
                    try {
                        ImageIO.write(img, "png", new File(outputPathRaw));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } break;
                case "tga": {
                    try {
                        ImageIO.write(img, "tga", new File(outputPathRaw));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } break;
                case "dds": {
                    DDSWriter.writeDDS(new File(outputPathRaw), img, formatRaw);
                } break;
                case "bmp": {
                    BMPWriter.writeBMP(new File(outputPathRaw), img, formatRaw);
                } break;
            }
        }
    }

}
