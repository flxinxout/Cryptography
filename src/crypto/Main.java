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

		//String inputMessage = Helper.readStringFromFile("text_one.txt");
		String key = "2cF%5";
		
		/*String messageClean = cleanString(inputMessage);

		byte[] messageBytes = stringToBytes(messageClean);
		byte[] keyBytes = stringToBytes(key);
		
		
		System.out.println("Original input sanitized : " + messageClean);
		System.out.println();*/

		//------------------------TEST COMPLET----------------------------
		String inputMessage = "i want some cheese motherfucker because I'm going to kick your ass";

		// CAESAR
		testCaesar(Helper.stringToBytes(inputMessage), (byte) 10);

		// VIGENERE
		testVigenere(Helper.stringToBytes(inputMessage), stringToBytes(key));

		// XOR
		System.out.println("------XOR------");
		String codedXor = Encrypt.encrypt(inputMessage, key, 2);
		System.out.println("Encrypted : " + codedXor);
		System.out.println("Decrypted : " + Decrypt.breakCipher(codedXor, 2));

		// CBC
		System.out.println("------CBC------");
		String codedCBC = Encrypt.encrypt(inputMessage, key, 4);
		System.out.println("Encrypted : " + codedCBC);
		System.out.println("Decrypted : " + Decrypt.decryptCBC(Helper.stringToBytes(codedCBC), Helper.stringToBytes(key)));

		// ONE TIME PAD
		System.out.println("------ONE TIME PAD------");
		byte[] otp = {1, 2 , 3, 4, 5, 6};
		String codedOneTimePad = Encrypt.encrypt("i want", Helper.bytesToString(otp), 3);
		System.out.println("Encrypted : " + codedOneTimePad);
		System.out.println("Decrypted : On l'a pas non ???");



		/*Fonction générale pour print le message avec n'importe quelle technique
		System.out.println(Encrypt.encrypt(message, Helper.bytesToString(Encrypt.generatePad(1)),0));
		System.out.println(Encrypt.encrypt(message, Helper.bytesToString(c), 1));
		System.out.println(Encrypt.encrypt(message, Helper.bytesToString(b), 2));
		System.out.println(Encrypt.encrypt(message, Helper.bytesToString(d), 3));
		System.out.println(Encrypt.encrypt(message, Helper.bytesToString(c), 4));*/

		// TODO: TO BE COMPLETED
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
		
		//Decoding without key
		byte[][] bruteForceResult = Decrypt.caesarBruteForce(result);
		String sDA = Decrypt.arrayToString(bruteForceResult);
		Helper.writeStringToFile(sDA, "bruteForceCaesar.txt");
		
		byte decodingKey = Decrypt.caesarWithFrequencies(result);
		String sFD = bytesToString(Encrypt.caesar(result, decodingKey));
		/**
		 * Ici, le without key donne une réponse qui est pas le message d'origine mais dans fichier .txt on retrouve bien la phrase
		 * a un endroit
		 */
		System.out.println("Decoded without knowing the key : " + sFD);
	}

	//Run the Encoding and Decoding using the caesar pattern
	public static void testVigenere(byte[] string, byte[] key) {
		System.out.println("------Vigenere------");

		//Encoding
		byte[] result = Encrypt.vigenere(string, key);
		String s = bytesToString(result);
		System.out.println("Encoded : " + s);

		/*//Decoding with key
		String sD = bytesToString(Encrypt.vigenere(result, (byte) (-key)));
		System.out.println("Decoded knowing the key : " + sD);*/

		//Decoding without key
		byte[] temp = Decrypt.vigenereWithFrequencies(result);
		String sFD = bytesToString(temp);
		System.out.println("Decoded without knowing the key : " + sFD);
	}
//TODO : TO BE COMPLETED
	
}
