package com.nick;

import java.math.BigInteger;

public final class Decryptor {

    private static long symbolCount = 0x10ffff;

    public static String decryptMessage(String[] message, BigInteger[] key) {

        BigInteger n = key[1];
        BigInteger d = key[2];

        String[] content = message;

        int messageLength = Integer.parseInt(content[0]);
        int blockSize = Integer.parseInt(content[1]);
        String[] blocks = new String[content.length-2];

        for (int i = 0; i < blocks.length; i++) {
            blocks[i] = content[i+2];
        }

        int blockNums = blocks.length;

        BigInteger[] intBlocks = new BigInteger[blockNums];

        for (int i = 0; i < blockNums; i++) {
            intBlocks[i] = new BigInteger(blocks[i]);
        }

        BigInteger[] decryptedBlocks = new BigInteger[blockNums];

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
                if (message.length() + i < messageLength) {
                    BigInteger t = new BigInteger(symbolCount + "");
                    t = t.pow(i);
                    long ascii = blockInt.divide(t).longValue();
                    blockInt = blockInt.mod(t);
                    blockMessage += ((char)ascii) + "";
                }
            }

            blockMessage = new StringBuilder(blockMessage).reverse().toString();
            message += blockMessage;

        }

        return message;

    }

}
