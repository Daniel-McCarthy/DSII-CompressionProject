package compressor.test;

import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import compressor.Lzw;

class CompressorTest {
	Lzw algorithm = new Lzw();

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
}
