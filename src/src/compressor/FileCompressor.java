package compressor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import compressor.FileHandler;
import compressor.CompressionAlgorithm;

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
				 + "\n\n Please enter a number to select your input method:");
		 
		 String input = scanner.nextLine();
		 // Default to text input if input is not explicitly for File input.
		 if (input == "2" || input.contains("File") || input.contains("file")) {
			// Ask for a filename to open.
			 
		 } else {
			 // Ask for text input to compress.
			 System.out.println("\nPlease enter the text you would like to compress:\n");
			 String textToCompress = scanner.nextLine();
			 compressStringAndPrintResult(textToCompress);
		 }
		 scanner.close();
	 }
	 
	 static void compressStringAndPrintResult(String textToCompress) {
		 String compressedData = CompressionAlgorithm.compress(textToCompress);
		 String decompressedText = CompressionAlgorithm.decompress(compressedData);	
		 
		 // Print input/output text, their sizes, and the percentage difference between the two string sizes.
		 String compressedDataAsText = CompressionAlgorithm.numberStringToText(compressedData);
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