package compressor.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import compressor.FileHandler;
import compressor.Lzw;

class CompressorTest {
	Lzw algorithm = new Lzw();
	Random random = new Random();
	List<File> filesToCleanup = new ArrayList<File>();

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
		while (filesToCleanup.size() > 0) {
			File file = filesToCleanup.get(0);
			try {
				file.delete();
			} catch (Exception ex) {
				System.out.println(String.format("Failed to delete file %s after test. Exception: %s", file.getName(), ex.getMessage()));
			}
			filesToCleanup.remove(0);
		}
	}

	@Test
	void compressionDecompressionTest_StaticData() {
		String inputText = "How much wood could a woodchuck chuck if a woodchuck could chuck wood?";
		String[] repeatedWords = new String[] { "wood", "could", "chuck", "a" };
		
		String compressedString = algorithm.compress(inputText);
		String compressedStringAsText = algorithm.numberStringToText(compressedString);
		String decompressedText = algorithm.numberStringToText(algorithm.decompress(compressedString));
		
		// Validate that compressed data is both different than input, and smaller.
		Assert.assertFalse(String.format("Compression of string failed to result in any change. Both input and compressed string both were: '%s'.", inputText), inputText.equals(compressedStringAsText));
		Assert.assertTrue(String.format("Compressed text failed to be smaller than input text. Input Length: %d, Compressed Length: %d.", inputText.length(), compressedStringAsText.length()), compressedStringAsText.length() < inputText.length());
		
		// De-compressed data must be exactly identical to input data.
		Assert.assertTrue(String.format("Decompressed string failed to be the same as input text.\n\tExpected: %s\n\tGot:%s",  inputText, decompressedText), inputText.contentEquals(decompressedText));
	
		// Validate that our sentence repetitions do not show up more than once.
		System.out.println(decompressedText);
		for (String word : repeatedWords) {
			Assert.assertEquals(compressedString.indexOf(word), compressedString.lastIndexOf(word));
		}
	}
	
	@Test
	void compressionDecompressionTest_RandomData() {
		List<String> wordSelection = Arrays.asList("sparrow", "pigeon", "dove", "chicken", "swift", "arrow", "wheelbarrow", "smidgeon", "love", "move", "thicken", "lift");
		String alphabet = "abcdefghijklmnopqrstuvwxyz";
		List<String> addedWords = new ArrayList<>();
		
		String testInputText = "";
		
		// Assemble random input text.
		for (int wave = 0; wave < 10; wave++) {
			if (random.nextBoolean()) {
				// Add a random word.
				String randomWord = wordSelection.get(random.nextInt(wordSelection.size()));
				testInputText += randomWord;
				addedWords.add(randomWord);
			} else {
				// Add random characters.
				int numberOfCharsToAdd = random.nextInt(10) + 1;
				for (int i = 0; i < numberOfCharsToAdd; i++) {
					testInputText += alphabet.charAt(random.nextInt(alphabet.length()));
				}
			}
			
			boolean addPeriod = random.nextBoolean();
			boolean addComma = random.nextBoolean();
			boolean addSpace = random.nextBoolean();
			
			if (addPeriod && addComma) {
				testInputText += random.nextBoolean() ? ',' : '.';
			} else if (addPeriod) {
				testInputText += ". ";
			} else if (addComma) {
				testInputText += ", ";
			} else if (addSpace) {
				testInputText += " ";
			}
		}
		
		String inputText = testInputText;
		String compressedString = algorithm.compress(inputText);
		String compressedStringAsText = algorithm.numberStringToText(compressedString);
		String decompressedText = algorithm.numberStringToText(algorithm.decompress(compressedString));
		
		// Validate that compressed data is both different than input, and smaller.
		Assert.assertFalse(String.format("Compression of string failed to result in any change. Both input and compressed string both were: '%s'.", inputText), inputText.equals(compressedStringAsText));
		Assert.assertTrue(String.format("Compressed text failed to be smaller than input text. Input Length: %d, Compressed Length: %d.", inputText.length(), compressedStringAsText.length()), compressedStringAsText.length() < inputText.length());
		
		// De-compressed data must be exactly identical to input data.
		Assert.assertTrue(String.format("Decompressed string failed to be the same as input text.\n\tExpected: %s\n\tGot:%s",  inputText, decompressedText), inputText.contentEquals(decompressedText));
	
		// Validate, if any, all selected words only appear at max once.
		System.out.println(decompressedText);
		for (String word : addedWords) {
			Assert.assertEquals(compressedString.indexOf(word), compressedString.lastIndexOf(word));
		}
		
		System.out.println("Random Text Input:" + inputText);
		System.out.println("Random Text Compressed:"+compressedStringAsText);
		System.out.println("Random Test Decompressed:"+decompressedText + "\n\n");
	}
	
	@Test
	void compressionDecompressionTest_UniqueData() {
		// Note: No repetition of data to identify and compress.
		String inputText = "abcdefghijklmnopqrstuvwxyz1234567890!@#$%^&*()";
		
		String compressedString = algorithm.compress(inputText);
		String compressedStringAsText = algorithm.numberStringToText(compressedString);
		String decompressedText = algorithm.numberStringToText(algorithm.decompress(compressedString));
		
		// Data that is entirely unique should not be compressed at all.
		// Validate that the data is identical at all stages.
		Assert.assertTrue(String.format("Compressed text failed to be identical to input data.\n\tExpected: '%s'\n\tGot: '%s'", inputText, compressedStringAsText), inputText.contentEquals(compressedStringAsText));
		Assert.assertTrue(String.format("Compressed text failed to be identical to decompressed data.\n\tExpected: '%s'\n\tGot: '%s'", inputText, compressedStringAsText), decompressedText.contentEquals(compressedStringAsText));	
		Assert.assertTrue(String.format("Decompressed text failed to be identical to input data.\n\tExpected: '%s'\n\tGot: '%s'", inputText, decompressedText), inputText.contentEquals(decompressedText));
	}
	
	@Test
	void compressionDecompressionTest_NoData() {
		String inputText = "";
		
		String compressedString = algorithm.compress(inputText);
		String compressedStringAsText = algorithm.numberStringToText(compressedString);
		String decompressedText = algorithm.numberStringToText(algorithm.decompress(compressedString));
		
		// The compress and de-compress functions should be able to return successfully
		// when given an empty string, to assure that this does not break our program.
		Assert.assertTrue(String.format("Compressed text failed to be identical to input data.\n\tExpected: '%s'\n\tGot: '%s'", inputText, compressedStringAsText), inputText.contentEquals(compressedStringAsText));
		Assert.assertTrue(String.format("Compressed text failed to be identical to decompressed data.\n\tExpected: '%s'\n\tGot: '%s'", inputText, compressedStringAsText), decompressedText.contentEquals(compressedStringAsText));	
		Assert.assertTrue(String.format("Decompressed text failed to be identical to input data.\n\tExpected: '%s'\n\tGot: '%s'", inputText, decompressedText), inputText.contentEquals(decompressedText));
	}
	
	@Test
	void overtwoCharacterMatchRegressionTest() {
		// A defect was found where matches greater than two characters being added to the dictionary, the third character
		// was not selected correctly, causing lost/damaged data in the compression process. This validates that this
		// issue is no longer occurring and does not return.
		
		// Due to the inaccessibility to the dictionary during compression, we can't validate the corruption of the
		// dictionary that was experienced, but we can validate that this edge case is functioning normally.
		String inputText = "thisisthethethe";
		String compressedString = algorithm.compress(inputText);
		String compressedStringAsText = algorithm.numberStringToText(compressedString);
		String decompressedText = algorithm.numberStringToText(algorithm.decompress(compressedString));
		
		// Validate that compressed data is both different than input, and smaller.
		Assert.assertFalse(String.format("Compression of string failed to result in any change. Both input and compressed string both were: '%s'.", inputText), inputText.equals(compressedStringAsText));
		Assert.assertTrue(String.format("Compressed text failed to be smaller than input text. Input Length: %d, Compressed Length: %d.", inputText.length(), compressedStringAsText.length()), compressedStringAsText.length() < inputText.length());
		
		// De-compressed data must be exactly identical to input data.
		Assert.assertTrue(String.format("Decompressed string failed to be the same as input text.\n\tExpected: %s\n\tGot:%s",  inputText, decompressedText), inputText.contentEquals(decompressedText));
	}
	
	@Test
	void fileIOTest() {
		String testData = "Thisdatashouldbereadbackin correctly, not incorrectly.";
		String filePath = "test-file.txt";
		FileHandler.saveFile(filePath, testData);
		
		// Test reading from file path.
		byte[] fileDataFromPath = FileHandler.openFile(filePath);
		String fileDataFromPathString = "";
		 for (byte character : fileDataFromPath) {
			 fileDataFromPathString += (char)character;
		 }
		
		Assert.assertArrayEquals(testData.getBytes(), fileDataFromPath);
		Assert.assertTrue(String.format("File read from path failed to match input data. \nExpected: %s\nGot: %s", testData, fileDataFromPathString), testData.contentEquals(fileDataFromPathString));
		
		// Test reading from File object.
		File savedFile = new File(filePath);
		filesToCleanup.add(savedFile);
		byte[] fileDataFromFileObj = FileHandler.openFile(savedFile);
		String fileDataFromFileObjString = "";
		 for (byte character : fileDataFromFileObj) {
			 fileDataFromFileObjString += (char)character;
		 }
		Assert.assertArrayEquals(testData.getBytes(), fileDataFromFileObj);
		Assert.assertTrue(String.format("File read from file obj failed to match input data. \nExpected: %s\nGot: %s", testData, fileDataFromFileObjString), testData.contentEquals(fileDataFromFileObjString));
	}
}
