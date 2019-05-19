package com.nick;

import java.math.BigInteger;

public final class Decryptor {

    //Used to determine the characters that can be used
    //in the message wanting to be decrypted
    private static long symbolCount = 0x10ffff;

    public static String decryptMessage(String[] message, BigInteger[] key) {

        //Grabbing the n value and the private key from the key set handed to the method.
        BigInteger n = key[1];
        BigInteger d = key[2];

        String[] content = message;

        //Pulling the necessary info from the message.
        int messageLength = Integer.parseInt(content[0]);
        int blockSize = Integer.parseInt(content[1]);
        String[] blocks = new String[content.length-2];

        //Separating the message from the other info.
        for (int i = 0; i < blocks.length; i++) {
            blocks[i] = content[i+2];
        }

        //Getting the number of data blocks
        int blockNums = blocks.length;

        BigInteger[] intBlocks = new BigInteger[blockNums];

        //Turning the blocks into numbers
        for (int i = 0; i < blockNums; i++) {
            intBlocks[i] = new BigInteger(blocks[i]);
        }

        BigInteger[] decryptedBlocks = new BigInteger[blockNums];

        //Decrypting the data
        for (int i = 0; i < blockNums; i++) {
            decryptedBlocks[i] = intBlocks[i].modPow(d, n);
        }

        return getTextFromBlock(decryptedBlocks, messageLength, blockSize);

    }

    private static String getTextFromBlock(BigInteger[] blockInts, int messageLength, int blockSize) {

        String message = "";

        for (BigInteger blockInt : blockInts) {

            String blockMessage = "";

            for (int i = blockSize - 1; i > -1; i--) {
                //Turning the number that is the text into readable text
                if (message.length() + i < messageLength) {
                    BigInteger t = new BigInteger(symbolCount + "");
                    t = t.pow(i);
                    //Grabbing the character value from the number
                    long ascii = blockInt.divide(t).longValue();
                    blockInt = blockInt.mod(t);
                    //Turning the character value into a char
                    blockMessage += ((char)ascii) + "";
                }
            }

            //Building the final message from the encoded message
            blockMessage = new StringBuilder(blockMessage).reverse().toString();
            message += blockMessage;

        }

        return message;

    }

}
