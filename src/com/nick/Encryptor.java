package com.nick;

import java.math.BigInteger;
import java.util.ArrayList;

public final class Encryptor {

    private static final double LOG2 = Math.log(2.0);
    private static long blockSize;
    //Used to determine the characters that can be used
    //in the message wanting to be encrypted
    private static long symbolCount = 0x10ffff;
    public static int keySize;

    //Making it so the class cannot be used to make an object
    private Encryptor() { }

    public static String[] encryptMessage(String message, BigInteger[] key) {

        //Grabbing the bit size of the key
        keySize = key[0].intValue();

        BigInteger bits = new BigInteger("2");

        //Getting bit length of the key
        bits = bits.pow(keySize);

        //Figuring out the size that each block of encrypted
        //data is going to be.
        blockSize = (long) Math.floor(logBigInteger(bits) / Math.log(symbolCount));

        ArrayList<BigInteger> encryptedBlocks = new ArrayList<>();

        //Getting the n value and the public key from the key
        BigInteger n = key[1];
        BigInteger e = key[2];

        ArrayList<BigInteger> blocks = getBlocksFromText(message);

        //Taking the blocks of "text" and encrypting them
        for (BigInteger block : blocks) {
            BigInteger b = new BigInteger("" + block);
            encryptedBlocks.add(b.modPow(e, n));
        }

        String[] estring = new String[2 + encryptedBlocks.size()];

        estring[0] = "" + message.length();
        estring[1] = "" + blockSize;

        //turning the array of BigIntegers into an array of Strings
        for (int i = 0; i < encryptedBlocks.size(); i++) {
            estring[i+2] = encryptedBlocks.get(i).toString();
        }

        return estring;

    }

    private static ArrayList<BigInteger> getBlocksFromText(String message) {

        ArrayList<BigInteger> blockInts = new ArrayList<>();
        BigInteger letter, symbol;

        //Turning the message into blocks of numbers
        for (int blockStart = 0; blockStart < message.length(); blockStart += blockSize) {

            BigInteger blockInt = BigInteger.ZERO;

            for (int i = blockStart; i < Math.min(blockStart + blockSize, message.length()); i++) {
                //Grabbing the number value of the character of the message for each letter
                int ascii = message.charAt(i);
                letter = new BigInteger(ascii + "");
                symbol = new BigInteger(symbolCount + "");
                int modi = (int) (i % blockSize);
                symbol = symbol.pow(modi);
                letter = letter.multiply(symbol);
                blockInt = blockInt.add(letter);
            }

            blockInts.add(blockInt);

        }

        return blockInts;

    }

    //Used to get the block size for the message
    private static double logBigInteger(BigInteger val) {
        int blex = val.bitLength() - 1022;
        if (blex > 0)
            val = val.shiftRight(blex);
        double res = Math.log(val.doubleValue());
        return blex > 0 ? res + blex * LOG2 : res;
    }

}
