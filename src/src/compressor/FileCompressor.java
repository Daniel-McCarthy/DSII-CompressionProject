package compressor;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import compressor.FileHandler;
import compressor.Lzw;

public class FileCompressor {
////		Input Example:
////		byte[] fileData = FileHandler.openFile("../samples/peterpiper.txt");
////		for (byte byteValue : fileData)
////			System.out.print(String.format("%c ", byteValue));
//		
////		Output Example:
////		List<Byte> fileDataAsList = new ArrayList<Byte>();
////		for (Byte byteValue : fileData)
////			fileDataAsList.add(byteValue);
////		FileHandler.saveFile("../samples/peterpiper-output.txt", fileDataAsList);

	 public static void main(String[] args) {
		 Scanner scanner = new Scanner(System.in);
		 System.out.println("Welcome to our compression program."
				 + "\n\t(1. Text input."
				 + "\n\t(2. File input."
				 + "\n\nPlease enter a number to select your input method:");
		 
		 String input = scanner.nextLine();
		 // Default to text input if input is not explicitly for File input.
		 if (input.equals("2") || input.contains("File") || input.contains("file")) {
			// Ask for a filename to open.
			boolean retry = false;
			String fileName = "";
			while (fileName.equals("") || retry) {
				System.out.println("\n\nPlease enter the name of the file you wish to open:");
				fileName = scanner.nextLine();
				File file = new File(fileName);
				
				if (file.exists()) {
					System.out.println("\n"); // Extra line to add room before next output.
					compressFileAndPrintResult(file);
					retry = false;
					
				} else {
					retry = true;
					System.out.println("\nFile not found. Please try again.\n");
				}
			}
			 
		 } else {
			 // Ask for text input to compress.
			 System.out.println("\nPlease enter the text you would like to compress:\n");
			 String textToCompress = scanner.nextLine();
			 compressStringAndPrintResult(textToCompress);
		 }
		 scanner.close();
	 }
	 
	 static void compressFileAndPrintResult(File fileToCompress) {
		 String fileName = fileToCompress.getName();
		 String fileNameWithoutExtension = fileToCompress.getName().substring(0, fileName.lastIndexOf('.'));
		 String fileExtension = fileName.substring(fileName.lastIndexOf('.'));
		 
		 byte[] fileData = FileHandler.openFile(fileToCompress);
		 String textToCompress = "";
		 for (byte character : fileData) {
			 textToCompress += (char)character;
		 }
		 
		 String compressedData = Lzw.compress(textToCompress);
		 String compressedDataAsText = Lzw.numberStringToText(compressedData);
		 String decompressedText = Lzw.decompress(compressedData);
		 String fileNameForCompressedData = String.format("%s.c%s", fileNameWithoutExtension, fileExtension);
		 
		 // Print input/output text, their sizes, and the percentage difference between the two string sizes.
		 System.out.println(String.format("\n\nInput Text:\n\n'%s'", textToCompress));
		 System.out.println(String.format("Input Size: %d", textToCompress.length()));
		 System.out.println(String.format("\n\nCompressed Text Output:\n\n'%s'", compressedDataAsText));
		 System.out.println(String.format("Compressed Text Size: %d", compressedDataAsText.length()));
		 
		 float inputLength = textToCompress.length();
		 float compressedLength = compressedDataAsText.length();
		 float differencePercentage = (Math.abs(inputLength - compressedLength) / ((inputLength + compressedLength) / 2)) * 100;
		 System.out.println(String.format("\n\nPercentage size difference: %s%.2f%%", inputLength > compressedLength ? '-' : '+', differencePercentage));
	 
		 FileHandler.saveFile(fileNameWithoutExtension + ".c" + fileExtension, compressedDataAsText);
		 System.out.println("Wrote compressed file to: " + fileNameForCompressedData);
	 }
	 
	 static void compressStringAndPrintResult(String textToCompress) {
		 String compressedData = Lzw.compress(textToCompress);
		 String decompressedText = Lzw.decompress(compressedData);	
		 
		 // Print input/output text, their sizes, and the percentage difference between the two string sizes.
		 String compressedDataAsText = Lzw.numberStringToText(compressedData);
		 System.out.println(String.format("\n\nInput Text:\n\n'%s'", textToCompress));
		 System.out.println(String.format("Input Size: %d", textToCompress.length()));
		 System.out.println(String.format("\n\nCompressed Text Output:\n\n'%s'", compressedDataAsText));
		 System.out.println(String.format("Compressed Text Size: %d", compressedDataAsText.length()));
		 
		 float inputLength = textToCompress.length();
		 float compressedLength = compressedDataAsText.length();
		 float differencePercentage = (Math.abs(inputLength - compressedLength) / ((inputLength + compressedLength) / 2)) * 100;
		 System.out.println(String.format("\n\nPercentage size difference: %s%.2f%%", inputLength > compressedLength ? '-' : '+', differencePercentage));
	 }

}