package compressor;

import java.io.*;
import java.util.List;

public class FileHandler {
	public static byte[] openFile(String filePath) {
		try {
			FileInputStream inputFile = new FileInputStream(filePath);
			byte[] fileData = inputFile.readAllBytes();
			inputFile.close();
			return fileData;	
		} catch (IOException e) {
			System.out.println(String.format("Failed to find or read file at: \"%s\". Exception: %s", filePath, e.getMessage()));
		}
		return null;

	}
	
	public static byte[] openFile(File file) {
		try {
			FileInputStream inputFile = new FileInputStream(file.getAbsolutePath());
			byte[] fileData = inputFile.readAllBytes();
			inputFile.close();
			return fileData;	
		} catch (IOException e) {
			System.out.println(String.format("Failed to find or read file at: \"%s\". Exception: %s", file.getAbsoluteFile(), e.getMessage()));
		}
		return null;
	}
	
	public static void saveFile(String filePath, String fileData) {
		try {
			FileOutputStream outputFile = new FileOutputStream(filePath);
			byte[] fileDataArray = fileData.getBytes();
			
			outputFile.write(fileDataArray);
			outputFile.close();
		} catch (IOException e) {
			System.out.println(String.format("Failed to save file to: \"%s\". Exception: %s", filePath, e.getMessage()));
		}
	}
}
