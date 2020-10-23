package crypto;

import javax.swing.*;
import java.util.Random;
import static crypto.Helper.*;

public class Encrypt {
	
	public static final int CAESAR = 0;
	public static final int VIGENERE = 1;
	public static final int XOR = 2;
	public static final int ONETIME = 3;
	public static final int CBC = 4; 
	
	public static final byte SPACE = 32;
	
	final static Random rand = new Random();
	
	//-----------------------General-------------------------
	
	/**
	 * General method to encode a message using a key, you can choose the method you want to use to encode.
	 * @param message the message to encode already cleaned
	 * @param key the key used to encode
	 * @param type the method used to encode : 0 = Caesar, 1 = Vigenere, 2 = XOR, 3 = One time pad, 4 = CBC
	 * 
	 * @return an encoded String
	 * if the method is called with an unknown type of algorithm, it returns the original message
	 */
	public static String encrypt(String message, String key, int type) {
		// TODO: COMPLETE THIS METHOD
		
		return null; // TODO: to be modified
	}
	
	
	//-----------------------Caesar-------------------------
	
	/**
	 * Method to encode a byte array message using a single character key
	 * the key is simply added to each byte of the original message
	 * @param plainText The byte array representing the string to encode
	 * @param key the byte corresponding to the char we use to shift
	 * @param spaceEncoding if false, then spaces are not encoded
	 * @return an encoded byte array
	 */
	public static byte[] caesar(byte[] plainText, byte key, boolean spaceEncoding) {
		assert(plainText != null);
		// TODO: COMPLETE THIS METHOD

		byte[] cipherText = new byte[plainText.length];

		for(int i = 0; i < plainText.length; ++i) {
			if(plainText[i] == 32) {
				cipherText[i] = 32;
				System.out.println(cipherText[i]);
			} else {
				cipherText[i] = (byte) (plainText[i] + key);
				System.out.println(cipherText[i]);
			}
		}
		
		return cipherText; // TODO: to be modified
	}
	
	/**
	 * Method to encode a byte array message  using a single character key
	 * the key is simply added  to each byte of the original message
	 * spaces are not encoded
	 * @param plainText The byte array representing the string to encode
	 * @param key the byte corresponding to the char we use to shift
	 * @return an encoded byte array
	 */
	public static byte[] caesar(byte[] plainText, byte key) {
		// TODO: COMPLETE THIS METHOD
		return caesar(plainText, key, false); // TODO: to be modified
	}
	
	//-----------------------XOR-------------------------
	
	/**
	 * Method to encode a byte array using a XOR with a single byte long key
	 * @param plainText the byte array representing the string to encode
	 * @param key the byte we will use to XOR
	 * @param spaceEncoding if false, then spaces are not encoded
	 * @return an encoded byte array
	 */
	public static byte[] xor(byte[] plainText, byte key, boolean spaceEncoding) {
		// TODO: COMPLETE THIS METHOD

		byte[] cipherText = new byte[plainText.length];

		for(int i = 0; i < plainText.length; ++i) {
			if(plainText[i] == 32) {
				cipherText[i] = 32;
				System.out.println(cipherText[i]);
			} else {
				cipherText[i] = (byte) (plainText[i] ^ key);
				System.out.println(cipherText[i]);
			}
		}

		return cipherText; // TODO: to be modified
	}
	/**
	 * Method to encode a byte array using a XOR with a single byte long key
	 * spaces are not encoded
	 * @param key the byte we will use to XOR
	 * @return an encoded byte array
	 */
	public static byte[] xor(byte[] plainText, byte key) {
		// TODO: COMPLETE THIS METHOD
		return xor(plainText, key, false); // TODO: to be modified
	}
	//-----------------------Vigenere-------------------------
	
	/**
	 * Method to encode a byte array using a byte array keyword
	 * The keyword is repeated along the message to encode
	 * The bytes of the keyword are added to those of the message to encode
	 * @param plainText the byte array representing the message to encode
	 * @param keyword the byte array representing the key used to perform the shift
	 * @param spaceEncoding if false, then spaces are not encoded
	 * @return an encoded byte array 
	 */
	public static byte[] vigenere(byte[] plainText, byte[] keyword, boolean spaceEncoding) {
		// TODO: COMPLETE THIS METHOD

		byte[] cipherText = new byte[plainText.length];
		int a = 0;
		for(int i = 0; i < plainText.length; ++i) {
			if(plainText[i] == 32) {
				System.out.println(plainText[i] + " " + keyword[a] + " " + a);
				cipherText[i] = 32;
				System.out.println(cipherText[i]);
			} else {
				if(a >= keyword.length) a = 0;
				System.out.println(plainText[i] + " " + keyword[a] + " " + a);
				cipherText[i] = (byte) (plainText[i] + keyword[a]);
				System.out.println(cipherText[i]);
				a++;
			}
		}

		return cipherText; // TODO: to be modified
	}
	
	/**
	 * Method to encode a byte array using a byte array keyword
	 * The keyword is repeated along the message to encode
	 * spaces are not encoded
	 * The bytes of the keyword are added to those of the message to encode
	 * @param plainText the byte array representing the message to encode
	 * @param keyword the byte array representing the key used to perform the shift
	 * @return an encoded byte array 
	 */
	public static byte[] vigenere(byte[] plainText, byte[] keyword) {
		// TODO: COMPLETE THIS METHOD
		return vigenere(plainText, keyword, false); // TODO: to be modified
	}
	
	
	
	//-----------------------One Time Pad-------------------------
	
	/**
	 * Method to encode a byte array using a one time pad of the same length.
	 *  The method  XOR them together.
	 * @param plainText the byte array representing the string to encode
	 * @param pad the one time pad
	 * @return an encoded byte array
	 */
	public static byte[] oneTimePad(byte[] plainText, byte[] pad) {
		// TODO: COMPLETE THIS METHOD
		assert (pad.length >= plainText.length);

		byte[] cipherText = new byte[plainText.length];

		for(int i = 0; i < plainText.length; ++i) {
			cipherText[i] = (byte) (plainText[i] ^ pad[i]);
		}

		return cipherText; // TODO: to be modified
	}
	
	
	
	
	//-----------------------Basic CBC-------------------------
	
	/**
	 * Method applying a basic chain block counter of XOR without encryption method. Encodes spaces.
	 * @param plainText the byte array representing the string to encode
	 * @param iv the pad of size BLOCKSIZE we use to start the chain encoding
	 * @return an encoded byte array
	 */
	public static byte[] cbc(byte[] plainText, byte[] iv) {
		// TODO: COMPLETE THIS METHOD
		/**
		 * PAS ENCORE FAITE
		 */
		byte[] cipherText = new byte[plainText.length];
		byte[] newPad = new byte[iv.length];
		int a = 0;

		for(int i = 0; i < plainText.length; i++) {
			if(a <= iv.length) {
				cipherText[i] = (byte) (plainText[i] ^ iv[a]);
				newPad[i] = cipherText[i];
				a++;
				if(a == iv.length) a = 0;
			} else {
				cipherText[i] = (byte) (plainText[i] ^ newPad[a]);
				iv[i] = cipherText[i];
				a++;
			}
		}
		
		return cipherText; // TODO: to be modified
	}
	
	
	/**
	 * Generate a random pad/IV of bytes to be used for encoding
	 * @param size the size of the pad
	 * @return random bytes in an array
	 */
	public static byte[] generatePad(int size) {
		// TODO: COMPLETE THIS METHOD

		return null; // TODO: to be modified

	}
	
	
	
}