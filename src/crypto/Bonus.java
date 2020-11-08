package crypto;

import java.util.Scanner;
import static crypto.Helper.*;

public class Bonus {
    private static Scanner input = new Scanner (System.in);

    //-------------HELPER METHODS----------------
    /**
     * Method that asks the user to enter an integer with a specific message
     * @param message the message before entering the integer
     * @param infBound the inferior bound
     * @param supBound the superior bound
     * @return the entered integer
     */
    public static int enterInt(String message, int infBound, int supBound) {
        int number;
        boolean correct;

        do {
            System.out.println(message);

            number = input.nextInt();
            input.nextLine();
            correct = (number >= infBound && number <= supBound);

            if (!correct) {
                System.out.println("\nERROR : Veuillez entrer un nombre entre "
                        + infBound + " et " + supBound);
            }
        } while (!correct);

        return number;
    }


    /**
     * Method that asks the user to enter a string with a specific message
     * @param message the message before entering the integer
     * @return the entered string
     */
    public static String enterString(String message) {
        String answer;
        System.out.println(message);
        answer = input.nextLine();

        return answer;
    }

    /**
     * Method that asks the user to enter an string with a specific message
     * @param message the message before entering the integer
     * @param answers possible answers
     * @return the entered string
     */
    public static String enterString(String message, String... answers) {
        String answer;
        boolean correct = false;

        do {
            System.out.println(message);

            answer = input.nextLine().toLowerCase();

            //Check if the answer is correct
            for (int i = 0; i < answers.length; i++) {
                answers[i] = answers[i].toLowerCase();
                if (answer.equals(answers[i])) {
                    correct = true;
                }
            }

            if (!correct) {
                System.out.print("\nERROR : Veuillez entrer une des réponses suivantes : ");
                for (int i = 0; i < answers.length; i++) {
                    System.out.print(answers[i]);
                    if (i != answers.length - 1) {
                        System.out.print(", ");
                    }
                }
            }
        } while (!correct);

        return answer;
    }


    /**
     * Method that asks the user to enter an string with a specific message
     * @param message the message before entering the integer
     * @param minSize minimum size of the string
     * @return the entered string
     */
    public static String enterString(String message, int minSize) {
        String answer;
        do {
            answer = enterString(message);

            if (answer.length() < minSize) {
                System.out.println("\nERROR : Veuillez entrer un texte d'au moins " +
                        minSize + " caractères");
            }

        } while (answer.length() < minSize);

        return answer;
    }


    //-------------EXTENDED CBC----------------
    /**
     * Method to encode a byte array message using a single character key
     * the key is simply added to each byte of the original message
     * The method ###ENCODES THE SPACES###
     * @param plainText The byte array representing the string to encode
     * @param key the byte corresponding to the char we use to shift
     * @return an encoded byte array
     */
    public static byte[] caesarWithoutSpace(byte[] plainText, byte key) {
        assert(plainText != null);

        byte[] cipherText = new byte[plainText.length];

        for(int i = 0; i < plainText.length; ++i) {
            cipherText[i] = (byte) (plainText[i] + key);
        }

        return cipherText;
    }


    /**
     * Method applying a basic chain block counter of XOR WITH encryption method, here CAESAR. Encodes spaces.
     * @param plainText the byte array representing the string to encode
     * @param iv the pad of size BLOCKSIZE we use to start the chain encoding
     * @param key is the key for the caesar encryption
     * @return an encoded byte array
     */
    public static byte[] cbcBonusWithCaesar(byte[] plainText, byte[] iv, byte key) {
        byte[] cbc = Encrypt.cbc(plainText, iv);
        return caesarWithoutSpace(cbc, key);
    }


    /**
     * Method used to decode a String encoded following the CBC pattern and the CAESAR encryption
     * @param cipher the byte array representing the encoded text
     * @param iv the pad of size BLOCKSIZE we use to start the chain encoding
     * @return the clear text
     */
    public static byte[] decryptCBCBonusWithCaesar(byte[] cipher, byte[] iv, byte key) {
        byte[] cbc = caesarWithoutSpace(cipher, (byte) -key);
        return Decrypt.decryptCBC(cbc, iv);
    }


    /**
     * Method applying a basic propagating chain block counter of XOR. Encodes spaces.
     * @param plainText the byte array representing the string to encode
     * @param iv the pad of size BLOCKSIZE we use to start the chain encoding
     * @return an encoded byte array
     */
    public static byte[] PCBC(byte[] plainText, byte[] iv) {
        int textSize = plainText.length;
        int blockSize = iv.length;
        int lastBlockSize = blockSize;
        int blocksNumber;

        // Fill new pad to have a new address
        byte[] copyPad = new byte[blockSize];
        for (int i = 0; i < blockSize; i++) {
            copyPad[i] = iv[i];
        }

        //Compute blocks number
        blocksNumber = textSize / blockSize +
                ((textSize % blockSize == 0) ? 0 : 1);

        //Fill the cipher text block by block
        byte[] cipherText = new byte[textSize];
        for (int currentBlock = 0; currentBlock < blocksNumber; ++currentBlock) {
            if ((currentBlock + 1) * blockSize > textSize) {
                lastBlockSize = textSize - currentBlock * blockSize;
            }

            for (int i = currentBlock * blockSize; i < (currentBlock + 1) * blockSize -
                    (blockSize - lastBlockSize); ++i) {
                cipherText[i] = (byte) (plainText[i] ^ copyPad[i - (currentBlock * blockSize)]);

                int index = i - (currentBlock * blockSize);
                byte[] key = new byte[blockSize];
                key[index] = (byte) (cipherText[i] ^ plainText[i]);
                copyPad[index] = key[index];
            }
        }

        return cipherText;
    }


    /**
     * Method used to decode a String encoded following the PCBC (propagating CBC) pattern
     * @param cipher the byte array representing the encoded text
     * @param iv the pad of size BLOCKSIZE we use to start the chain encoding
     * @return the clear text
     */
    public static byte[] decryptPCBC(byte[] cipher, byte[] iv) {
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

                int index = i - (currentBlock * blockSize);
                byte[] key = new byte[blockSize];
                key[index] = (byte) (plainText[i] ^ cipher[i]);
                copyPad[index] = key[index];
            }
        }

        return plainText;
    }



    //--------------- BONUS 2 - SHELL -----------------
    /**
     * Method that create a shell program to implement encryption and decryption
     */
    public static void shell() {
        boolean isFinished = false;
        int nbUtilisation = 0;

        System.out.println("====================================================================================================");
        System.out.println("️Bienvenue dans le programme d'encryptage / décryptage " +
                "de Giovanni Ranieri et Dylan Vairoli !");
        System.out.println("====================================================================================================");

        String aide = enterString("\nSi vous avez besoin d'assistance, tapez 'aide', sinon cliquez sur enter.");

        if (aide.equals("aide")){
            System.out.println("\n----- Aide -----\n" +
                    "    Dans ce programme vous avez la possibilité de crypter ou décrypter un message de votre choix.\n" +
                    "\n" +
                    "    Avant tout il faut savoir que ce programme est adapté au jeu de caractères ISO-8859-1. Dans\n" +
                    "    cette norme, 256 caractères sont représentés.\n" +
                    "\n" +
                    "    Voici un descriptif des différentes étapes du programme:\n" +
                    "       1. Entrer le message à crypter ou décrypter. Il doit être composé d'AU MOINS 6 CARACTERES\n" +
                    "       2. Choisir si vous souhaitez crypter ou décrypter le message (0 = crypter, 1 = décrypter)\n" +
                    "\n" +
                    "----- Voie d'encryptage -----\n" +
                    "    Supposons que vous ayiez choisi d'encrypter le message:\n" +
                    "        E3. Choisir votre méthode d'encryptage :\n" +
                    "            0 = Caesar\n" +
                    "            1 = Vigenere\n" +
                    "            2 = XOR\n" +
                    "            3 = One Time Pad\n" +
                    "            4 = CBC\n" +
                    "\n" +
                    "        E4. Entrer votre clé de cryptage (Sous forme textuelle, sa conversion en byte se fera automatiquement)\n" +
                    "            !ATTENTION!\n" +
                    "                 - Les méthodes Caesar et XOR ne prendront comme clé effective uniquement le premier caractère\n" +
                    "                   que vous entrez.\n" +
                    "                 - La méthode One Time Pad requiert un pad (clé) d'au moins la même taille que le texte à crypter.\n" +
                    "\n" +
                    "\n" +
                    "----- Voie de décryptage -----\n" +
                    "    Supposons que vous ayiez choisi de décrypter le message:\n" +
                    "        D3. Choisir votre méthode de décryptage :\n" +
                    "            0 = Caesar\n" +
                    "            1 = Vigenere\n" +
                    "            2 = XOR (Méthode force brute : vous devrez chercher manuellement le résultat)\n" +
                    "            3 = CBC (Décryptage possible uniquement en connaissant la clé utilisée lors du cryptage)\n" +
                    "\n" +
                    "        (CBC) Entrer la clé de cryptage (Sous forme textuelle, sa conversion en byte se fera automatiquement)   \n");
        }

        do {
            String modifiedMessage;
            String key;

            ++nbUtilisation;
            System.out.println("\n\n---------- Essai n°" + nbUtilisation + " ----------");
            String inputMessage = enterString("Veuillez entrer le texte à crypter / décrypter " +
                    "(taille minimale de 6 caractères) : ", 6);

            String messageClean = cleanString(inputMessage);

            //Ask encryption or decrpytion
            int crypt = enterInt("\nEntrez 0 si vous souhaitez crypter le texte, " +
                    "entrez 1 si vous souhaitez décrypter le texte", 0, 1);

            //----- Encryption way -----
            if (crypt == 0) {

                //Choice of the encryption method
                int method = enterInt("\nQuelle méthode de cryptage souhaitez vous utiliser?\n " +
                        "0 := Caesar, 1 := Vigenere, 2 := XOR, 3 := One Time Pad, " +
                        "4 := CBC ", 0, 4);

                //Enter key for every method except One Time Pad
                if (method != 3) {
                    key = enterString("\nVeuillez entrer la clé de cryptage : ", 1);
                }
                //Enter key for One Time Pad
                else {
                    key = enterString("\nVeuillez entrer le pad de cryptage " +
                            "(sa taille doit être au moins aussi grande " +
                            "que le texte à crypter) : ", inputMessage.length());
                }

                //Encrypt the message
                modifiedMessage = Encrypt.encrypt(messageClean, key, method);
                System.out.println("\nVotre texte crypté est : " + modifiedMessage);
            }

            //Decryption way
            if (crypt == 1) {

                //Choice of the decryption method
                int method = enterInt("\nQuelle méthode de décryptage " +
                                "souhaitez-vous utiliser? 0 := Caesar, 1 := Vigenere, " +
                                "2 := XOR (force brute), 3 := CBC (avec clé connue uniquement)",
                        0 ,3);

                //Decrypt all but CBC
                if (method != 3) {
                    modifiedMessage = Decrypt.breakCipher(inputMessage, method);
                }

                //Decrypt CBC
                else {
                    key = enterString("\nVeuillez entrer la clé utilisée lors du " +
                            "cryptage : ", 1);

                    byte[] byteKey = stringToBytes(key);

                    byte[] byteMessage = stringToBytes(messageClean);
                    modifiedMessage = bytesToString(Decrypt.decryptCBC(byteMessage, byteKey));
                }
                System.out.println("\nVotre texte décrypté est : " + modifiedMessage);
            }

            //Ask for repeat
            String repeat = enterString("\nSouhaitez-vous recommencer ? " +
                    "[Oui / Non] ", "Oui", "Non");

            //If it's no (repeat toLowerCase())
            if (repeat.equals("non")) {
                System.out.println("\nMerci d'avoir utilisé notre programme.");
                isFinished = true;
            }
        } while (!isFinished);
    }

}
