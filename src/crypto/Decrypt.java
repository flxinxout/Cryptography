package crypto;

import static crypto.Helper.bytesToString;
import static crypto.Helper.stringToBytes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Decrypt {
	
	
	public static final int ALPHABETSIZE = Byte.MAX_VALUE - Byte.MIN_VALUE + 1 ; //256
	public static final int APOSITION = 97 + ALPHABETSIZE/2; 
	
	//source : https://en.wikipedia.org/wiki/Letter_frequency
	public static final double[] ENGLISHFREQUENCIES = {0.08497,0.01492,0.02202,0.04253,0.11162,0.02228,0.02015,0.06094,0.07546,0.00153,0.01292,0.04025,0.02406,0.06749,0.07507,0.01929,0.00095,0.07587,0.06327,0.09356,0.02758,0.00978,0.0256,0.0015,0.01994,0.00077};
	
	/**
	 * Method to break a string encoded with different types of cryptosystems
	 * @param type the integer representing the method to break : 0 = Caesar, 1 = Vigenere, 2 = XOR
	 * @return the decoded string or the original encoded message if type is not in the list above.
	 */
	public static String breakCipher(String cipher, int type) {
		//TODO : COMPLETE THIS METHOD
		
		return null; //TODO: to be modified
	}
	
	
	/**
	 * Converts a 2D byte array to a String
	 * @param bruteForceResult a 2D byte array containing the result of a brute force method
	 */
	public static String arrayToString(byte[][] bruteForceResult) {
		//TODO : COMPLETE THIS METHOD
		String message = "";
		for(int row = 0; row < bruteForceResult.length; ++row) {
			byte[] byteRow = new byte[bruteForceResult[row].length];
			for(int letter = 0; letter < bruteForceResult[row].length; ++letter) {
				byteRow[letter] = bruteForceResult[row][letter];
			}
			message += Helper.bytesToString(byteRow);
			message += System.lineSeparator();
		}
		
		return message; //TODO: to be modified
	}
	
	
	//-----------------------Caesar-------------------------
	
	/**
	 *  Method to decode a byte array  encoded using the Caesar scheme
	 * This is done by the brute force generation of all the possible options
	 * @param cipher the byte array representing the encoded text
	 * @return a 2D byte array containing all the possibilities
	 */
	public static byte[][] caesarBruteForce(byte[] cipher) {
		//TODO : COMPLETE THIS METHOD
		byte[][] decodedText = new byte[ALPHABETSIZE][cipher.length];

		for(int i = 1; i < ALPHABETSIZE; i++) {
			for(int j = 0; j < cipher.length; j++) {
				decodedText[i-1][j] = Encrypt.caesar(cipher, (byte) i)[j];
			}
		}

		return decodedText; //TODO: to be modified
	}	
	
	
	/**
	 * Method that finds the key to decode a Caesar encoding by comparing frequencies
	 * @param cipherText the byte array representing the encoded text
	 * @return the encoding key
	 */
	public static byte caesarWithFrequencies(byte[] cipherText) {
		//TODO : COMPLETE THIS METHOD

		return -1; //TODO: to be modified
	}
	
	/**
	 * Method that computes the frequencies of letters inside a byte array corresponding to a String
	 * @param cipherText the byte array 
	 * @return the character frequencies as an array of float
	 */
	public static float[] computeFrequencies(byte[] cipherText) {
		//TODO : COMPLETE THIS METHOD

		// Pas du tout sur de cette méthode car je suis pas sur d'avoir compris comment rempli le tableau

		// entier provisoir
		int n = 0;

		// Integer pour voir le nombre de caractères différents de SPACE (32)
		int caracterWithoutSpace = 0;

		// le tableau
		float[] caractersFrequencies = new float[ALPHABETSIZE];

		// Boucle des bytes du tableau
		for(int i = 0; i < cipherText.length; i++) {
			// caractère à la i-ème position
			byte caractere = cipherText[i];

			if(caractere != 32) {
				caracterWithoutSpace += 1;
				// affectation += 1.0 à l'indice du tableau se rapportant au nombre byte qui lui est attribué par la convention
				// + 128 car sinon indice négatif
				caractersFrequencies[caractere + 128] += 1.0;
			}
		}

		for(int i = 0; i < caractersFrequencies.length; i++) {
			if(caractersFrequencies[i] != 0.0) {
				caractersFrequencies[i] /= caracterWithoutSpace;
			}
		}

		// A enlever après
		for (int i = 0; i < caractersFrequencies.length; i++) {
			System.out.println(caractersFrequencies[i]);
			if(caractersFrequencies[i] != 0.0) {
				n++;
			}
		}

		System.out.println(n);
		System.out.println(caracterWithoutSpace);

		return caractersFrequencies; //TODO: to be modified
	}
	
	
	/**
	 * Method that finds the key used by a  Caesar encoding from an array of character frequencies
	 * @param charFrequencies the array of character frequencies
	 * @return the key
	 */
	public static byte caesarFindKey(float[] charFrequencies) {
		//TODO : COMPLETE THIS METHOD

		// Le produit scalaire qui sera pris à la fin
		double biggerProduitScalaire = 0.0;
		// Le produit scalaire qui est comparé avec le plus grand
		double temp = 0.0;
		// Valeur de i + j pour les itérations
		int k;
		// Indice à recupérer quand le produit scalaire est le plus grand
		int l = 0;

		for(int i = 0; i < charFrequencies.length; i++) {
			for(int j = 0; j < ENGLISHFREQUENCIES.length; j++) {
				k = (i + j);
				if(k > 255) {
					k -= 256;
				}
				temp += ENGLISHFREQUENCIES[j] * charFrequencies[k];
			}
			if(temp > biggerProduitScalaire) { // supérieur ou égal ???
				biggerProduitScalaire = temp;
				l = i;
			}
		}

		byte caractereDuDecalage = (byte) l;

		/*
		TODO JARRIVE PAS A FAIRE LA DISTANCE COMME IL LE DISE DANS LE PDF
		 */

		//byte distance = l - 97;

		return -1; //TODO: to be modified
	}
	
	
	
	//-----------------------XOR-------------------------
	
	/**
	 * Method to decode a byte array encoded using a XOR 
	 * This is done by the brute force generation of all the possible options
	 * @param cipher the byte array representing the encoded text
	 * @return the array of possibilities for the clear text
	 */
	public static byte[][] xorBruteForce(byte[] cipher) {
		//TODO : COMPLETE THIS METHOD
		byte[][] decodedText = new byte[ALPHABETSIZE][cipher.length];

		for(int i = 1; i < ALPHABETSIZE; i++) {
			for(int j = 0; j < cipher.length; j++) {
				decodedText[i-1][j] = Encrypt.xor(cipher, (byte) i)[j];
			}
		}

		return decodedText; //TODO: to be modified
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
		//TODO : COMPLETE THIS METHOD
		return null; //TODO: to be modified
	}
	
	
	
	/**
	 * Helper Method used to remove the space character in a byte array for the clever Vigenere decoding
	 * @param array the array to clean
	 * @return a List of bytes without spaces
	 */
	public static List<Byte> removeSpaces(byte[] array){
		//TODO : COMPLETE THIS METHOD
		return null;
	}
	
	
	/**
	 * Method that computes the key length for a Vigenere cipher text.
	 * @param cipher the byte array representing the encoded text without space
	 * @return the length of the key
	 */
	public static int vigenereFindKeyLength(List<Byte> cipher) {
		//TODO : COMPLETE THIS METHOD
		return -1; //TODO: to be modified
	}

	
	
	/**
	 * Takes the cipher without space, and the key length, and uses the dot product with the English language frequencies 
	 * to compute the shifting for each letter of the key
	 * @param cipher the byte array representing the encoded text without space
	 * @param keyLength the length of the key we want to find
	 * @return the inverse key to decode the Vigenere cipher text
	 */
	public static byte[] vigenereFindKey(List<Byte> cipher, int keyLength) {
		//TODO : COMPLETE THIS METHOD
		return null; //TODO: to be modified
	}
	
	
	//-----------------------Basic CBC-------------------------
	
	/**
	 * Method used to decode a String encoded following the CBC pattern
	 * @param cipher the byte array representing the encoded text
	 * @param iv the pad of size BLOCKSIZE we use to start the chain encoding
	 * @return the clear text
	 */
	public static byte[] decryptCBC(byte[] cipher, byte[] iv) {
		//TODO : COMPLETE THIS METHOD

		int blockSize = iv.length;
		int blocksNumber;
		if(cipher.length % blockSize != 0) {
			blocksNumber = cipher.length / blockSize + 1;
		} else {
			blocksNumber = cipher.length / blockSize;
		}
		byte[] plainText = new byte[cipher.length];

		for (int i = 0; i < blocksNumber; ++i) {
			if ((i + 1) * blockSize > cipher.length) {
				blockSize = cipher.length - i * blockSize;
			}

			for (int j = i * blockSize; j < ((i + 1) * blockSize); ++j) {
				plainText[j] = (byte) (cipher[j] ^ iv[j - (i * blockSize)]);

				//Faut-il créer un pad temporaire ou c'est ok de modifier le iv?
				iv[j - (i * blockSize)] = cipher[j];
			}
		}

		return plainText; //TODO: to be modified
	}
	
	
	

		
		
		
		
		
}
