package compressor;

import java.util.Collection;

abstract class CompressionAlgorithm {
	abstract Collection<Character> compress(Collection<Character> fileData);
	abstract Collection<Character> decompress(Collection<Character> fileData);
}