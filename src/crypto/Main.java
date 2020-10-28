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
		//String key = "2cF%5";
		
		//String messageClean = cleanString(inputMessage);
		
		
		//byte[] messageBytes = stringToBytes(messageClean);
		//byte[] keyBytes = stringToBytes(key);
		
		
		//System.out.println("Original input sanitized : " + messageClean);
		//System.out.println();
		
		/*System.out.println("------Caesar------");
		testCaesar(messageBytes, (byte) 1);*/

		//System.out.println("------Vigenere------");
		//Encrypt.vigenere(messageBytes, keyBytes);

		/*System.out.println("------CBC------");
		testCbc(messageBytes, keyBytes);*/

		byte[] b = {50, 10};

		String message = "i want";
		System.out.println(message);
		String encrypted = Encrypt.encrypt(message, Helper.bytesToString(b), 4);
		System.out.println("Encrypted : " + encrypted);

		byte[] decrypted = Decrypt.decryptCBC(Helper.stringToBytes(encrypted), b);
		System.out.println("Decrypted : " + Helper.bytesToString(decrypted));

		//Decrypt.xorBruteForce(Helper.stringToBytes(Encrypt.encrypt(message, Helper.bytesToString(b), 2)));

		/*Fonction générale pour print le message avec n'importe quelle technique
		System.out.println(Encrypt.encrypt(message, Helper.bytesToString(Encrypt.generatePad(1)),0));
		System.out.println(Encrypt.encrypt(message, Helper.bytesToString(c), 1));
		System.out.println(Encrypt.encrypt(message, Helper.bytesToString(b), 2));
		System.out.println(Encrypt.encrypt(message, Helper.bytesToString(d), 3));
		System.out.println(Encrypt.encrypt(message, Helper.bytesToString(c), 4));*/

		// TODO: TO BE COMPLETED
		
	}

	//Run the Encoding and Decoding using the cbc pattern
	public static void testCbc(byte[] string , byte[] iv) {
		//Encoding
		byte[] result = Encrypt.cbc(string, iv);
		System.out.println(result);
		String s = bytesToString(result);
		System.out.println("Encoded : " + s);
	}

	//Run the Encoding and Decoding using the caesar pattern 
	public static void testCaesar(byte[] string , byte key) {
		//Encoding
		byte[] result = Encrypt.caesar(string, key);
		System.out.println(result);
		String s = bytesToString(result);
		System.out.println("Encoded : " + s);
		
		/*Decoding with key
		String sD = bytesToString(Encrypt.caesar(result, (byte) (-key)));
		System.out.println("Decoded knowing the key : " + sD);
		
		//Decoding without key
		byte[][] bruteForceResult = Decrypt.caesarBruteForce(result);
		String sDA = Decrypt.arrayToString(bruteForceResult);
		Helper.writeStringToFile(sDA, "bruteForceCaesar.txt");
		
		byte decodingKey = Decrypt.caesarWithFrequencies(result);
		String sFD = bytesToString(Encrypt.caesar(result, decodingKey));
		System.out.println("Decoded without knowing the key : " + sFD);*/
	}
	
//TODO : TO BE COMPLETED
	
}
