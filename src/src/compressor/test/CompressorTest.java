package compressor.test;

import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import compressor.CompressionAlgorithm;

class CompressorTest {
	CompressionAlgorithm algorithm = new CompressionAlgorithm();

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
	void compressionDecompressionTest() {
		String inputText = "How much would could a woodchuck chuck if a woodchuck could chuck wood?";
		String compressedText = algorithm.compress(inputText);
		
		Assert.assertFalse(String.format("Compression of string failed to result in any change. Both input and compressed string both were: '%s'.", inputText), inputText.equals(compressedText));
		Assert.assertTrue(String.format("Compressed text failed to be smaller than input text. Input Length: %d, Compressed Length: %d.", inputText.length(), compressedText.length()), compressedText.length() < inputText.length());
		
		String decompressedText = algorithm.decompress(compressedText);
		Assert.assertTrue(String.format("Decompressed string failed to be the same as input text.\n\tExpected: %s\n\tGot:%s",  inputText, compressedText), inputText.contentEquals(decompressedText));
	}

}
