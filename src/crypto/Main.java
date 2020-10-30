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

		//--------------------------TEST CAESAR--------------------------
		//testCaesar(messageBytes, (byte) 1);

		//--------------------------TEST VIGENERE--------------------------
		testVigenere(messageBytes, keyBytes);
		/*String message = "i want"; //÷ ö i wanted
		System.out.println(message);
		String encrypted = Encrypt.encrypt(message, Helper.bytesToString(b), 1);
		System.out.println("Encrypted : " + encrypted);

		Decrypt.computeFrequencies(Helper.stringToBytes(encrypted));

		byte[] decrypted = Decrypt.decryptCBC(Helper.stringToBytes(encrypted), b);
		System.out.println("Decrypted : " + Helper.bytesToString(decrypted));

		//Decrypt.xorBruteForce(Helper.stringToBytes(Encrypt.encrypt(message, Helper.bytesToString(b), 2)));

		Fonction générale pour print le message avec n'importe quelle technique
		System.out.println(Encrypt.encrypt(message, Helper.bytesToString(Encrypt.generatePad(1)),0));
		System.out.println(Encrypt.encrypt(message, Helper.bytesToString(c), 1));
		System.out.println(Encrypt.encrypt(message, Helper.bytesToString(b), 2));
		System.out.println(Encrypt.encrypt(message, Helper.bytesToString(d), 3));
		System.out.println(Encrypt.encrypt(message, Helper.bytesToString(c), 4));*/

		// TODO: TO BE COMPLETED
		/*byte[] test = { (byte) 161 };
		System.out.println(Helper.bytesToString(test) + Helper.stringToBytes("¡")[0]);*/
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
		System.out.println("Decoded without knowing the key : " + sFD);
	}

	//Run the Encoding and Decoding using the caesar pattern
	public static void testVigenere(byte[] string , byte[] key) {
		System.out.println("------Vigenere------");

		//Encoding
		byte[] result = Encrypt.vigenere(string, key);
		String s = bytesToString(result);
		System.out.println("Encoded : " + s);

		/*//Decoding with key
		String sD = bytesToString(Encrypt.vigenere(result, (byte) (-key)));
		System.out.println("Decoded knowing the key : " + sD);*/

		//Decoding without key
		Decrypt.vigenereWithFrequencies(result);
		/*String sFD = bytesToString(Decrypt.vigenereWithFrequencies(result));
		System.out.println("Decoded without knowing the key : " + sFD);*/
	}
//TODO : TO BE COMPLETED
	
}
