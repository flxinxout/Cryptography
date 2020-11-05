package crypto;

import static crypto.Helper.*;
import static crypto.Helper.enterString;

public class Bonus {

    //-------------BONUS----------------

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
        // TODO: COMPLETE THIS METHOD

        byte[] cipherText = new byte[plainText.length];

        for(int i = 0; i < plainText.length; ++i) {
            cipherText[i] = (byte) (plainText[i] + key);
        }

        return cipherText; // TODO: to be modified
    }

    /**
     * Method to encode a byte array (AND THE SPACE) using a XOR with a single byte long key
     * @param plainText the byte array representing the string to encode
     * @param key the byte we will use to XOR
     * @return an encoded byte array
     */
    public static byte[] xorWithoutSpace(byte[] plainText, byte key) {
        // TODO: COMPLETE THIS METHOD

        byte[] cipherText = new byte[plainText.length];

        for(int i = 0; i < plainText.length; ++i) {
            cipherText[i] = (byte) (plainText[i] ^ key);
        }

        return cipherText; // TODO: to be modified
    }

    /**
     * Method used to decode a String encoded following the CBC pattern and the CAESAR encryption
     * @param cipher the byte array representing the encoded text
     * @param iv the pad of size BLOCKSIZE we use to start the chain encoding
     * @return the clear text
     */
    public static byte[] decryptCBCBonusWithCaesar(byte[] cipher, byte[] iv, byte key) {
        //TODO : COMPLETE THIS METHOD

        int blockSize = iv.length;
        int lastBlockSize = blockSize;
        int blocksNumber;

        // Cipher Text without caesar encryption
        byte[] cbcWithoutCaesar;

        // Fill the new pad to have a new adress
        byte[] copyPad = new byte[blockSize];
        for (int i = 0; i < blockSize; ++i) {
            copyPad[i] = iv[i];
        }

        // Decrypt the caesar encryption
        cbcWithoutCaesar = caesarWithoutSpace(cipher, (byte) (-key));

        //Compute the number of blocks
        if(cipher.length % blockSize != 0) {
            blocksNumber = cbcWithoutCaesar.length / blockSize + 1;
        } else {
            blocksNumber = cbcWithoutCaesar.length / blockSize;
        }

        //Fill the plain text block by block
        byte[] plainText = new byte[cipher.length];
        for (int currentBlock = 0; currentBlock < blocksNumber; ++currentBlock) {
            if (cbcWithoutCaesar.length % blockSize != 0 && (currentBlock + 1) * blockSize > cbcWithoutCaesar.length) {
                lastBlockSize = cbcWithoutCaesar.length - currentBlock * blockSize;
            }

            for (int i = currentBlock * blockSize; i < (currentBlock + 1) * blockSize - (blockSize - lastBlockSize); ++i) {
                plainText[i] = (byte) (cbcWithoutCaesar[i] ^ copyPad[i - (currentBlock * blockSize)]);

                copyPad[i - (currentBlock * blockSize)] = cbcWithoutCaesar[i];
            }
        }

        return plainText; //TODO: to be modified
    }

    /**
     * Method used to decode a String encoded following the CBC pattern and the CAESAR encryption
     * @param cipher the byte array representing the encoded text
     * @param iv the pad of size BLOCKSIZE we use to start the chain encoding
     * @return the clear text
     */
    public static byte[][] decryptCBCBonusWithXor(byte[] cipher, byte[] iv) {
        //TODO : COMPLETE THIS METHOD

        int blockSize = iv.length;
        int lastBlockSize = blockSize;
        int blocksNumber;

        // Cipher Text without caesar encryption
        byte[][] cbcWithoutXor;

        // Plain Text decrypted
        byte[][] decrypted = new byte[Decrypt.ALPHABETSIZE][cipher.length];

        // Fill the new pad to have a new adress
        byte[] copyPad = new byte[blockSize];
        for (int i = 0; i < blockSize; ++i) {
            copyPad[i] = iv[i];
        }

        // Decrypt the caesar encryption
        cbcWithoutXor = Decrypt.xorBruteForce(cipher);

        //Compute the number of blocks
        if(cipher.length % blockSize != 0) {
            blocksNumber = cbcWithoutXor.length / blockSize + 1;
        } else {
            blocksNumber = cbcWithoutXor.length / blockSize;
        }

        //For the different lines of the 2 dimensions array
        //Decode each line
        for(int row = 0; row < cbcWithoutXor.length; ++row) {

            byte[] lineToDecrypt = new byte[cbcWithoutXor[row].length];

            for (int currentBlock = 0; currentBlock < blocksNumber; ++currentBlock) {
                if (lineToDecrypt.length % blockSize != 0 && (currentBlock + 1) * blockSize > lineToDecrypt.length) {
                    lastBlockSize = lineToDecrypt.length - currentBlock * blockSize;
                }

                for (int i = currentBlock * blockSize; i < (currentBlock + 1) * blockSize - (blockSize - lastBlockSize); ++i) {
                    decrypted[row][i] = (byte) (lineToDecrypt[i] ^ copyPad[i - (currentBlock * blockSize)]);

                    copyPad[i - (currentBlock * blockSize)] = lineToDecrypt[i];
                }
            }

        }

        return decrypted; //TODO: to be modified
    }

    /**
     * Method applying a basic chain block counter of XOR WITH encryption method, here CAESAR. Encodes spaces.
     * @param plainText the byte array representing the string to encode
     * @param iv the pad of size BLOCKSIZE we use to start the chain encoding
     * @param key is the key for the caesar encryption
     * @return an encoded byte array
     */
    public static byte[] cbcBonusWithCaesar(byte[] plainText, byte[] iv, byte key) {
        int blockSize = iv.length;
        int lastBlockSize = blockSize;
        int blocksNumber;

        // Final byte Array with caesar encryption
        byte[] finalArray;

        // Fill new pad to have a new address
        byte[] copyPad = new byte[blockSize];
        for (int i = 0; i < blockSize; i++) {
            copyPad[i] = iv[i];
        }

        //Compute blocks number
        if(plainText.length % blockSize != 0) {
            blocksNumber = plainText.length / blockSize + 1;
        } else {
            blocksNumber = plainText.length / blockSize;
        }

        //Fill the cipher text block by block
        byte[] cipherText = new byte[plainText.length];
        for (int currentBlock = 0; currentBlock < blocksNumber; ++currentBlock) {
            if ((currentBlock + 1) * blockSize > plainText.length) {
                lastBlockSize = plainText.length - currentBlock * blockSize;
            }

            for (int i = currentBlock * blockSize; i < (currentBlock + 1) * blockSize - (blockSize - lastBlockSize); ++i) {
                cipherText[i] = (byte) (plainText[i] ^ copyPad[i - (currentBlock * blockSize)]);

                copyPad[i - (currentBlock * blockSize)] = cipherText[i];
            }
        }

        System.out.println();
        finalArray = caesarWithoutSpace(cipherText, key);
        System.out.println(cipherText.length);

        return finalArray;
    }

    /**
     * Method applying a basic chain block counter of XOR WITH encryption method, here XOR. Encodes spaces.
     * @param plainText the byte array representing the string to encode
     * @param iv the pad of size BLOCKSIZE we use to start the chain encoding
     * @param key is the key for the caesar encryption
     * @return an encoded byte array
     */
    public static byte[] cbcBonusWithXor(byte[] plainText, byte[] iv, byte key) {
        int blockSize = iv.length;
        int lastBlockSize = blockSize;
        int blocksNumber;

        // Final byte Array with caesar encryption
        byte[] finalArray;

        // Fill new pad to have a new address
        byte[] copyPad = new byte[blockSize];
        for (int i = 0; i < blockSize; i++) {
            copyPad[i] = iv[i];
        }

        //Compute blocks number
        if(plainText.length % blockSize != 0) {
            blocksNumber = plainText.length / blockSize + 1;
        } else {
            blocksNumber = plainText.length / blockSize;
        }

        //Fill the cipher text block by block
        byte[] cipherText = new byte[plainText.length];
        for (int currentBlock = 0; currentBlock < blocksNumber; ++currentBlock) {
            if ((currentBlock + 1) * blockSize > plainText.length) {
                lastBlockSize = plainText.length - currentBlock * blockSize;
            }

            for (int i = currentBlock * blockSize; i < (currentBlock + 1) * blockSize - (blockSize - lastBlockSize); ++i) {
                cipherText[i] = (byte) (plainText[i] ^ copyPad[i - (currentBlock * blockSize)]);

                copyPad[i - (currentBlock * blockSize)] = cipherText[i];
            }
        }

        finalArray = xorWithoutSpace(cipherText, key);

        return finalArray;
    }

    //--------------- BONUS 2 - SHELL -----------------

    /**
     * Method that create a shell program to implement encryption and decryption
     */
    public static void shell() {
        boolean isFinished = false;
        int nbUtilisation = 0;

        System.out.println("====================================================================================================");
        System.out.println("Bienvenue dans le programme d'encryptage / décryptage " +
                "de Giovanni Ranieri et Dylan Vairoli !");
        System.out.println("====================================================================================================");

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
            if (repeat.equals("Non".toLowerCase())) {
                System.out.println("Merci d'avoir utilisé notre programme.");
                isFinished = true;
            }
        } while (!isFinished);
    }

}
