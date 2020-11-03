package crypto;

import java.util.function.DoubleToIntFunction;

import static crypto.Helper.cleanString;
import static crypto.Helper.stringToBytes;
import static crypto.Helper.bytesToString;

/*
 * Part 1: Encode (with note that one can reuse the functions to decode)
 * Part 2: bruteForceDecode (caesar, xor) and CBCDecode
 * Part 3: frequency analysis and key-length search
 * Bonus: CBC with encryption, shell
 */
public class Main {
	
	
	//---------------------------MAIN---------------------------
	public static void main(String args[]) {

		String inputMessage = Helper.readStringFromFile("text_one.txt");
		String key = "2cF%5";
		
		String messageClean = cleanString(inputMessage);

		byte[] messageBytes = stringToBytes(messageClean);
		byte[] keyBytes = stringToBytes(key);
		
		System.out.println("Original input sanitized : " + messageClean);
		System.out.println();

		//------------------------TEST COMPLET----------------------------
		/*String inputMessage = "i want some cheese motherfucker because I'm going to kick your ass";
		String messageClean = cleanString(inputMessage);
		byte[] messageBytes = stringToBytes(messageClean);
		byte[] keyBytes = stringToBytes(key);*/

		// CBC
		byte[] c = Encrypt.cbc(messageBytes, keyBytes);
		System.out.println(Helper.bytesToString(Encrypt.cbc(messageBytes, keyBytes)));
		System.out.println(Helper.bytesToString(Decrypt.decryptCBC(c, keyBytes)));
		testCBC(messageBytes, keyBytes);
		System.out.println();

		// CAESAR
		testCaesar(messageBytes, keyBytes[0]);
		System.out.println();

		// VIGENERE
		testVigenere(messageBytes, keyBytes);
		System.out.println();

		// XOR
		/*testXOR(messageBytes, keyBytes[0]);
		System.out.println();*/

		// ONE TIME PAD
		testOneTime(messageBytes);
		System.out.println();

		// CBC
		testCBC(messageBytes, keyBytes);
		System.out.println();

		//BREAK CIPHER
		testBreakCipher(Helper.bytesToString(Encrypt.caesar(messageBytes, keyBytes[0])), 0);
		testBreakCipher(Helper.bytesToString(Encrypt.vigenere(messageBytes, keyBytes)), 1);
		//testBreakCipher(Helper.bytesToString(Encrypt.xor(messageBytes, keyBytes[0])), 2);
		testBreakCipher(inputMessage, 3);

		// TODO: TO BE COMPLETED
	}

	//Run the Encoding and Decoding using the caesar pattern
	public static void testBreakCipher(String cipher, int type) {
		String plainText = Decrypt.breakCipher(cipher, type);

		if (type == 0){
			System.out.println("------Decrypted using Caesar and frequencies method ------ ");
			System.out.println(plainText);
			System.out.println();
		}
		else if(type == 1){
			System.out.println("------ Decrypted using Vigenere and frequencies method ------ ");
			System.out.println(plainText);
			System.out.println();
		}
		else if(type == 2){
			System.out.println("------ Decrypted using XOR and brute force method ------");
			System.out.println(plainText);
			System.out.println();
		}
		else{
			System.out.println("------ The desired type isn't recognised: 0 = Caesar, 1 = Vigenere, 2 = XOR ------");
			System.out.println();
		}
	}

	//Run the Encoding and Decoding using the caesar pattern
	public static void testCaesar(byte[] string , byte key) {
		System.out.println("------Caesar------");

		//Encoding
		byte[] result = Encrypt.caesar(string, key);
		String s = bytesToString(result);
		System.out.println("Encoded : " + s);

		//Decoding with key
		String sD = bytesToString(Encrypt.caesar(result, (byte) (-key)));
		System.out.println("Decoded knowing the key : " + sD);

		/*//Decoding with brute force
		byte[][] bruteForceResult = Decrypt.caesarBruteForce(result);
		String sDA = Decrypt.arrayToString(bruteForceResult);
		System.out.println("Caesar brute force:");
		System.out.println(sDA);
		Helper.writeStringToFile(sDA, "bruteForceCaesar.txt");*/

		//Decoding with frequencies
		byte decodingKey = Decrypt.caesarWithFrequencies(result);
		String sFD = bytesToString(Encrypt.caesar(result, decodingKey));
		System.out.println("Decoded without knowing the key : " + sFD);
	}


	//Run the Encoding and Decoding using the caesar pattern
	public static void testVigenere(byte[] string, byte[] key) {
		System.out.println("------Vigenere------");

		//Encoding
		byte[] result = Encrypt.vigenere(string, key);
		String s = bytesToString(result);
		System.out.println("Encoded : " + s);

		//Decoding without key
		byte[] plainText = Decrypt.vigenereWithFrequencies(result);
		String sFD = bytesToString(plainText);
		System.out.println("Decoded without knowing the key : " + sFD);
	}


	//Run the Encoding and Decoding using the caesar pattern
	public static void testXOR(byte[] string, byte key) {
		System.out.println("------XOR------");

		//Encoding
		byte[] result = Encrypt.xor(string, key);
		String s = bytesToString(result);
		System.out.println("Encoded : " + s);

		//Decoding with brute force
		byte[][] bruteForceResult = Decrypt.xorBruteForce(result);
		String sDA = Decrypt.arrayToString(bruteForceResult);
		System.out.println("XOR brute force :");
		System.out.println(sDA);
	}

	//Run the Encoding and Decoding using the caesar pattern
	public static void testOneTime(byte[] string) {
		System.out.println("------One Time Pad------");

		byte[] pad = Encrypt.generatePad(string.length);

		//Encoding
		byte[] result = Encrypt.oneTimePad(string, pad);
		String s = bytesToString(result);
		System.out.print("Encoded : ");
		System.out.println(s);
	}

	//Run the Encoding and Decoding using the caesar pattern
	public static void testCBC(byte[] string, byte[] iv) {
		System.out.println("------CBC------");

		//Encoding
		byte[] result = Encrypt.cbc(string, iv);
		String s = bytesToString(result);
		System.out.println("Encoded : " + s);

		//Decoding with the key
		byte[] plainText = Decrypt.decryptCBC(result, iv);
		String sFD = bytesToString(plainText);
		System.out.println("Decoded knowing the key: " + sFD);
	}
//TODO : TO BE COMPLETED
	
}
