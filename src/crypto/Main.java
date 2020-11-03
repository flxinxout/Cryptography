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
	public static void main(String args[]) {

		//----------------- DECLARATION DES MESSAGES ET CLES -----------------------
		String message1 = Helper.readStringFromFile("text_4.txt");
		String key1 = "2cF%5";

		/*String message2 = "Resonance describes the phenomenon of increased amplitude that occurs when the frequency of a periodically applied force (or a Fourier component of it) is equal or close to a natural frequency of the system on which it acts. When an oscillating force is applied at a resonant frequency of a dynamical system, the system will oscillate at a higher amplitude than when the same force is applied at other, non-resonant frequencies.[3]\n" +
				"\n" +
				"Frequencies at which the response amplitude is a relative maximum are also known as resonant frequencies or resonance frequencies of the system.[3] Small periodic forces that are near a resonant frequency of the system have the ability to produce large amplitude oscillations in the system due to the storage of vibrational energy.\n" +
				"\n" +
				"Resonance phenomena occur with all types of vibrations or waves: there is mechanical resonance, acoustic resonance, electromagnetic resonance, nuclear magnetic resonance (NMR), electron spin resonance (ESR) and resonance of quantum wave functions. Resonant systems can be used to generate vibrations of a specific frequency (e.g., musical instruments), or pick out specific frequencies from a complex vibration containing many frequencies (e.g., filters).\n" +
				"\n" +
				"The term resonance (from Latin resonantia, 'echo', from resonare, 'resound') originated from the field of acoustics, particularly the sympathetic resonance observed in musical instruments, e.g., when one string starts to vibrate and produce sound after a different one is struck. Another example, electrical resonance, occurs in a circuit with capacitors and inductors because the collapsing magnetic field of the inductor generates an electric current in its windings that charges the capacitor, and then the discharging capacitor provides an electric current that builds the magnetic field in the inductor. Once the circuit is charged, the oscillation is self-sustaining, and there is no external periodic driving action.[clarification needed] This is analogous to a mechanical pendulum, where mechanical energy is converted back and forth between kinetic and potential, and both systems are forms of simple harmonic oscillators.";
		String key2 = "1w47Z3e";*/

		//------------------------TESTS COMPLETS----------------------------
		System.out.println("-------------------- TEST NO 1 --------------------");
		overallGeneralTest(message1, key1);

		//System.out.println("-------------------- TEST NO 2 --------------------");
		//overallGeneralTest(message1, key1);
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
}
