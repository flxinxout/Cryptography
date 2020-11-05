package crypto;

import static crypto.Helper.*;

/*
 * Part 1: Encode (with note that one can reuse the functions to decode)
 * Part 2: bruteForceDecode (caesar, xor) and CBCDecode
 * Part 3: frequency analysis and key-length search
 * Bonus: CBC with encryption, shell
 */
public class Main {
	public static void main(String args[]) {

		//----------------- MESSAGES AND KEYS DECLARATION -----------------------
		String message1 = Helper.readStringFromFile("text_one.txt");
		String message2 = Helper.readStringFromFile("text_two.txt");
		String message3 = Helper.readStringFromFile("text_three.txt");
		String message4 = Helper.readStringFromFile("text_4.txt");

		String key1 = "2cF%5";
		String key2 = "abcdefghij";
		String key3 = "f4[%&ji!è";

		//------------------------TESTS COMPLETS----------------------------
		//shell();

		/*System.out.println("-------------------- TEST NO 1 --------------------");
		overallGeneralTest(message1, key1);*/

		System.out.println("-------------------- TEST NO 2 --------------------");
		overallGeneralTest(message1, key2);

		/*System.out.println("-------------------- TEST NO 3 --------------------");
		overallGeneralTest(message1, key3);

		System.out.println("-------------------- TEST NO 4 --------------------");
		overallGeneralTest(message2, key1);

		System.out.println("-------------------- TEST NO 5 --------------------");
		overallGeneralTest(message2, key2);

		System.out.println("-------------------- TEST NO 6 --------------------");
		overallGeneralTest(message2, key3);

		System.out.println("-------------------- TEST NO 7 --------------------");
		overallGeneralTest(message3, key1);

		System.out.println("-------------------- TEST NO 8 --------------------");
		overallGeneralTest(message3, key2);

		System.out.println("-------------------- TEST NO 9 --------------------");
		overallGeneralTest(message3, key3);*/

		// TODO: TO BE COMPLETED
	}

	public static void overallGeneralTest(String message, String key){
		String messageClean = cleanString(message);

		byte[] messageBytes = stringToBytes(messageClean);
		byte[] keyBytes = stringToBytes(key);

		System.out.println("Original message sanitized : " + messageClean);
		System.out.println("Original key : " + key);
		System.out.println();

		// CAESAR
		testCaesar(messageBytes, keyBytes[0]);
		System.out.println();

		// VIGENERE
		testVigenere(messageBytes, keyBytes);
		System.out.println();

		/*// XOR
		testXOR(messageBytes, keyBytes[0]);
		System.out.println();*/

		// ONE TIME PAD
		testOneTime(messageBytes);
		System.out.println();

		// CBC
		testCBC(messageBytes, keyBytes);
		System.out.println();

		// CBC BONUS WITH CAESAR
		testCBCBonusWithCaesar(messageBytes, keyBytes, (byte) 1);
		System.out.println();

		// CBC BONUS WITH XOR
		testCBCBonusWithXor(messageBytes, keyBytes, keyBytes[0]);
		System.out.println();

		//BREAK CIPHER
		testBreakCipher(Helper.bytesToString(Encrypt.caesar(messageBytes, keyBytes[0])), 0);
		testBreakCipher(Helper.bytesToString(Encrypt.vigenere(messageBytes, keyBytes)), 1);
		//testBreakCipher(Helper.bytesToString(Encrypt.xor(messageBytes, keyBytes[0])), 2);
		testBreakCipher(Helper.bytesToString(Encrypt.vigenere(messageBytes, keyBytes)), 3);

		System.out.println();
		System.out.println();

	}

	//Run the Encoding and Decoding using the caesar, vigenere or XOR pattern
	public static void testBreakCipher(String cipher, int type) {
		String plainText = Decrypt.breakCipher(cipher, type);

		System.out.println("------ Break cipher method ------ ");

		if (type == 0){
			System.out.println("CAESAR WITH FREQUENCIES");
			System.out.println(plainText);
			System.out.println();
		}
		else if(type == 1){
			System.out.println("VIGENERE WITH FREQUENCIES");
			System.out.println(plainText);
			System.out.println();
		}
		else if(type == 2){
			System.out.println("XOR BRUTE FORCE");
			System.out.println(plainText);
			System.out.println();
		}
		else{
			System.out.println("ERROR: the desired type isn't recognised: 0 = Caesar, 1 = Vigenere, 2 = XOR");
			System.out.println(plainText);
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


	//Run the Encoding and Decoding using the vigenere pattern
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


	//Run the Encoding and Decoding using the XOR pattern
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

	//Run the Encoding and Decoding using the One Time Pad pattern
	public static void testOneTime(byte[] string) {

		/**
		 * Le pad généré est plus grand que le résultat de l'encodage... est-ce normal ?
		 * Sinon, impossible de trouver des exemples pour voir si c'est juste
		 */

		System.out.println("------One Time Pad------");

		byte[] pad = Encrypt.generatePad(string.length);
		System.out.println(bytesToString(pad));
		System.out.println("--------");

		//Encoding
		byte[] result = Encrypt.oneTimePad(string, pad);
		String s = bytesToString(result);
		System.out.print("Encoded : ");
		System.out.println(s);
	}

	//Run the Encoding and Decoding using the CBC pattern
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

	//Run the Encoding and Decoding using CBC BONUS pattern and CAESAR
	public static void testCBCBonusWithCaesar(byte[] string, byte[] iv, byte key) {
		System.out.println("------CBC BONUS CAESAR------");

		//Encoding
		byte[] result = Bonus.cbcBonusWithCaesar(string, iv, key);
		String s = bytesToString(result);
		System.out.println("Encoded : " + s);

		//Decoding with the key
		byte[] plainText = Bonus.decryptCBCBonusWithCaesar(result, iv, key);
		String sFD = bytesToString(plainText);
		System.out.println("Decoded knowing the key: " + sFD);
	}

	//Run the Encoding and Decoding using CBC BONUS pattern and XOR
	public static void testCBCBonusWithXor(byte[] string, byte[] iv, byte key) {
		System.out.println("------CBC BONUS XOR------");

		//Encoding
		byte[] result = Bonus.cbcBonusWithXor(string, iv, key);
		String s = bytesToString(result);
		System.out.println("Encoded : " + s);
		System.out.println();

		//Decoding with the key
		byte[][] plainText = Bonus.decryptCBCBonusWithXor(result, iv);
		String sFD = Decrypt.arrayToString(plainText);
		System.out.println("Decoded knowing the key: " + sFD);
	}
}
