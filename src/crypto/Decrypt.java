package crypto;

import static crypto.Helper.bytesToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Decrypt {

	public static final int ALPHABETSIZE = Byte.MAX_VALUE - Byte.MIN_VALUE + 1 ; //256
	public static final int CAESAR = 0;
	public static final int VIGENERE = 1;
	public static final int XOR = 2;
	public static final byte SPACE = 32;

	//source : https://en.wikipedia.org/wiki/Letter_frequency
	public static final double[] ENGLISHFREQUENCIES = {0.08497,0.01492,0.02202,0.04253,0.11162,0.02228,0.02015,0.06094,0.07546,0.00153,0.01292,0.04025,0.02406,0.06749,0.07507,0.01929,0.00095,0.07587,0.06327,0.09356,0.02758,0.00978,0.0256,0.0015,0.01994,0.00077};



	//-----------------------General-------------------------
	/**
	 * Method to break a string encoded with different types of cryptosystems
	 * @param type the integer representing the method to break : 0 = Caesar, 1 = Vigenere, 2 = XOR
	 * @return the decoded string or the original encoded message if type is not in the list above.
	 */
	public static String breakCipher(String cipher, int type) {
		byte[] cipherByte = Helper.stringToBytes(cipher);
		String result;

		if (type == CAESAR) {
			byte key = caesarWithFrequencies(cipherByte);
			byte[] byteResult = Encrypt.caesar(cipherByte, key);
			result = bytesToString(byteResult);
		}
		else if (type == VIGENERE){
			byte[] byteResult = vigenereWithFrequencies(cipherByte);
			result = bytesToString(byteResult);
		}
		else if (type == XOR){
			byte[][] bruteForceResult = xorBruteForce(cipherByte);
			result = Decrypt.arrayToString(bruteForceResult);
		}
		else {
			result = cipher;
		}

		return result;
	}
	
	
	/**
	 * Converts a 2D byte array to a String
	 * @param bruteForceResult a 2D byte array containing the result of a brute force method
	 */
	public static String arrayToString(byte[][] bruteForceResult) {
		String message = "";

		for(int row = 0; row < bruteForceResult.length; ++row) {
			byte[] byteRow = new byte[bruteForceResult[row].length];

			for(int letter = 0; letter < bruteForceResult[row].length; ++letter) {
				byteRow[letter] = bruteForceResult[row][letter];
			}
			message += Helper.bytesToString(byteRow);
			message += System.lineSeparator();
		}
		
		return message;
	}


	
	//-----------------------Caesar-------------------------
	/**
	 *  Method to decode a byte array  encoded using the Caesar scheme
	 * This is done by the brute force generation of all the possible options
	 * @param cipher the byte array representing the encoded text
	 * @return a 2D byte array containing all the possibilities
	 */
	public static byte[][] caesarBruteForce(byte[] cipher) {
		byte[][] decodedText = new byte[ALPHABETSIZE][cipher.length];

		for(int row = 1; row < ALPHABETSIZE; ++row) {
			for(int col = 0; col < cipher.length; ++col) {
				decodedText[row-1][col] = Encrypt.caesar(cipher, (byte) row)[col];
			}
		}

		return decodedText;
	}	
	
	
	/**
	 * Method that finds the key to decode a Caesar encoding by comparing frequencies
	 * @param cipherText the byte array representing the encoded text
	 * @return the decoding key
	 */
	public static byte caesarWithFrequencies(byte[] cipherText) {
		float[] frequencies = computeFrequencies(cipherText);
		return caesarFindKey(frequencies);
	}


	/**
	 * Method that computes the frequencies of letters inside a byte array corresponding to a String
	 * @param cipherText the byte array 
	 * @return the character frequencies as an array of float
	 */
	public static float[] computeFrequencies(byte[] cipherText) {
		int charactersButSpace = 0;
		float[] charactersFrequencies = new float[ALPHABETSIZE];

		//Check how many times each character (except space) does appear in the cipher text
		for(int i = 0; i < cipherText.length; ++i) {
			byte character = cipherText[i];

			if(character != SPACE) {
				charactersButSpace += 1;

				if (character >= 0 && character <= 127){
					charactersFrequencies[character] += 1.0;
				} else{
					charactersFrequencies[character + 255] += 1.0;
				}
			}
		}

		//Divide by the total number of characters (except space)
		for(int i = 0; i < charactersFrequencies.length; ++i) {
			if(charactersFrequencies[i] != 0.0) {
				charactersFrequencies[i] /= charactersButSpace;
			}
		}

		return charactersFrequencies;
	}


	/**
	 * Method that finds the key used by a  Caesar encoding from an array of character frequencies
	 * @param charFrequencies the array of character frequencies
	 * @return the decoding key
	 */
	public static byte caesarFindKey(float[] charFrequencies) {
		double maxScalarProduct = 0.0;
		double scalarProd;
		int maxIndex = 0;

		//Compute the scalar products
		for(int cipher = 0; cipher < charFrequencies.length; ++cipher) {
			scalarProd = 0;

			for(int english = 0; english < ENGLISHFREQUENCIES.length; ++english) {
				//Last index (in cipher) of the current comparison
				int sum = (cipher + english);
				if(sum > 255) {
					sum -= 256;
				}

				scalarProd += ENGLISHFREQUENCIES[english] * charFrequencies[sum];
			}

			//Keep track of the maximum scalar product and its index
			if(scalarProd > maxScalarProduct) {
				maxScalarProduct = scalarProd;
				maxIndex = cipher;
			}
		}

		// The key which should be used to decrypt the message
		return (byte) -(maxIndex - 96);
	}


	
	//-----------------------XOR-------------------------
	/**
	 * Method to decode a byte array encoded using a XOR 
	 * This is done by the brute force generation of all the possible options
	 * @param cipher the byte array representing the encoded text
	 * @return the array of possibilities for the clear text
	 */
	public static byte[][] xorBruteForce(byte[] cipher) {
		byte[][] decodedText = new byte[ALPHABETSIZE][cipher.length];

		for(int i = 1; i < ALPHABETSIZE; ++i) {
			for(int j = 0; j < cipher.length; ++j) {
				decodedText[i-1][j] = Encrypt.xor(cipher, (byte) i)[j];
			}
		}

		return decodedText;
	}



	//-----------------------Vigenere-------------------------
	// Algorithm : see  https://www.youtube.com/watch?v=LaWp_Kq0cKs	
	/**
	 * Method to decode a byte array encoded following the Vigenere pattern, but in a clever way, 
	 * saving up on large amounts of computations
	 * @param cipher the byte array representing the encoded text
	 * @return the byte encoding of the clear text
	 */
	public static byte[] vigenereWithFrequencies(byte[] cipher) {
		List<Byte> cleanedText = removeSpaces(cipher);

		int keyLength = vigenereFindKeyLength(cleanedText);

		byte[] key = vigenereFindKey(cleanedText, keyLength);

		return Encrypt.vigenere(cipher, key);
	}
	

	/**
	 * Helper Method used to remove the space character in a byte array for the clever Vigenere decoding
	 * @param array the array to clean
	 * @return a List of bytes without spaces
	 */
	public static List<Byte> removeSpaces(byte[] array){
		List<Byte> list = new ArrayList<>();

		for (int i = 0; i < array.length; ++i) {
			if (array[i] != SPACE){
				list.add(array[i]);
			}
		}

		return list;
	}


	/**
	 * Method that computes the key length for a Vigenere cipher text.
	 * @param cipher the byte array representing the encoded text without space
	 * @return the length of the key
	 */
	public static int vigenereFindKeyLength(List<Byte> cipher) {
		int[] coincidences = findCoincidences(cipher);

		List<Integer> potentialKeyLengths = findPotentialKeyLength(coincidences);

		return getKeyLength(potentialKeyLengths);
	}


	/**
	 * Sub-Method (Part 1) to "vigenereFindKeyLength" that computes the
	 * coincidences between the characters for each shift in the text.
	 * @param cipher the byte array representing the encoded text without space
	 * @return an array of the coincidences in the encoded text
	 */
	public static int[] findCoincidences(List<Byte> cipher) {
		/* Array that stores the number of coincidences for each shift. Its indexes
		 * represent the number of shifts to the right (0 = 1 shift, 1 = 2 shift, ...)
		 */
		int[] coincidences = new int[cipher.size()];

		//Iterates through the cipher array and add the coincidences to their array
		for (int shift = 1; shift < cipher.size(); ++shift) {
			for (int i = shift; i < cipher.size() - shift; ++i) {
				if (cipher.get(i) == cipher.get(i - shift)){
					coincidences[shift-1] += 1;
				}
			}
		}

		return coincidences;
	}


	/**
	 * Sub-Method (Part 2) to "vigenereFindKeyLength" that computes the
	 * different potential key lengths from the coincidences array.
	 * @param coincidences the array representing the coincidences in the encoded text
	 * @return the potential lengths of the key
	 */
	public static List<Integer> findPotentialKeyLength(int[] coincidences) {
		//List containing the indexes (of coincidences array) of the maxima
		List<Integer> maximaIndex = new ArrayList<>();

		//Check the two first elements of the list
		if (coincidences[0] > coincidences[1] && coincidences[0] > coincidences[2]){
			maximaIndex.add(0);
		}
		if (coincidences[1] > coincidences[0] &&
				coincidences[1] > coincidences[2] && coincidences[1] > coincidences[3]){
			maximaIndex.add(1);
		}

		//Add all the maxima indexes in the list
		for (int i = 2; i < Math.ceil(coincidences.length / 2); ++i) {
			if (coincidences[i] > coincidences[i+1] && coincidences[i] > coincidences[i+2] &&
					coincidences[i] > coincidences[i-1] && coincidences[i] > coincidences[i-2]){
				maximaIndex.add(i);
			}
		}

		return maximaIndex;
	}


	/**
	 * Sub-Method (Part 3) to "vigenereFindKeyLength" that gets the most probable
	 * key length from the potential key lengths array
	 * @param maximaIndex the array representing the maximaIndex of coincidences
	 * @return the length of the key
	 */
	public static int getKeyLength(List<Integer> maximaIndex) {
		//Map: Key: distances, Values: occurrences
		Map<Integer, Integer> distances = new HashMap<>();

		//Add the distances to the map and map them to their value (number of occurences)
		for (int i = 0; i < maximaIndex.size() - 1; ++i) {
			int thisDistance = maximaIndex.get(i + 1) - maximaIndex.get(i);

			if(!distances.containsKey(thisDistance)){
				distances.put(thisDistance, 1);
			} else {
				distances.replace(thisDistance, (distances.get(thisDistance) + 1));
			}
		}

		//Set the key size to the greatest number of occurrences
		int keyLength = 0;
		int maxOccurrence = 0;

		for (int key : distances.keySet()) {
			if (distances.get(key) > maxOccurrence ||
					(distances.get(key) == maxOccurrence && key > keyLength)) {
				maxOccurrence = distances.get(key);
				keyLength = key;
			}
		}

		return keyLength;
	}


	/**
	 * Takes the cipher without space, and the key length, and uses the dot product with the English language frequencies 
	 * to compute the shifting for each letter of the key
	 * @param cipher the byte array representing the encoded text without space
	 * @param keyLength the length of the key we want to find
	 * @return the inverse key to decode the Vigenere cipher text
	 */
	public static byte[] vigenereFindKey(List<Byte> cipher, int keyLength) {
		byte[] key = new byte[keyLength];

		for (int i = 0; i < keyLength; ++i) {

			List<Byte> part = new ArrayList<>();
			for (int j = i; j < cipher.size(); j += keyLength) {
				part.add(cipher.get(j));
			}

			//Convert from list to array
			byte[] arrayPart = new byte[part.size()];
			for (int j = 0; j < part.size(); ++j) {
				arrayPart[j] = part.get(j);
			}

			byte thisKey = caesarWithFrequencies(arrayPart);
			key[i] = thisKey;
		}

		return key;
	}
	


	//-----------------------Basic CBC-------------------------
	/**
	 * Method used to decode a String encoded following the CBC pattern
	 * @param cipher the byte array representing the encoded text
	 * @param iv the pad of size BLOCKSIZE we use to start the chain encoding
	 * @return the clear text
	 */
	public static byte[] decryptCBC(byte[] cipher, byte[] iv) {
		int textSize = cipher.length;
		int blockSize = iv.length;
		int lastBlockSize = blockSize;
		int blocksNumber;

		// Fill the new pad to have a new adress
		byte[] copyPad = new byte[blockSize];
		for (int i = 0; i < blockSize; ++i) {
			copyPad[i] = iv[i];
		}

		//Compute the number of blocks
		blocksNumber = textSize / blockSize +
				((textSize % blockSize == 0) ? 0 : 1);

		//Fill the plain text block by block
		byte[] plainText = new byte[textSize];
		for (int currentBlock = 0; currentBlock < blocksNumber; ++currentBlock) {
			if (textSize % blockSize != 0 && (currentBlock + 1) * blockSize > textSize) {
				lastBlockSize = textSize - currentBlock * blockSize;
			}

			for (int i = currentBlock * blockSize; i < (currentBlock + 1) * blockSize - (blockSize - lastBlockSize); ++i) {
				plainText[i] = (byte) (cipher[i] ^ copyPad[i - (currentBlock * blockSize)]);

				copyPad[i - (currentBlock * blockSize)] = cipher[i];
			}
		}

		return plainText;
	}
}
