package com.nick;

import java.math.BigInteger;
import java.util.ArrayList;

public final class Encryptor {

    private static final double LOG2 = Math.log(2.0);
    private static long blockSize;
    private static long symbolCount = 0x10ffff;
    public static int keySize;

    private Encryptor() { }

    public static String[] encryptMessage(String message, BigInteger[] key) {

        keySize = key[0].intValue();

        BigInteger bits = new BigInteger("2");

        bits = bits.pow(keySize);

        blockSize = (long) Math.floor(logBigInteger(bits) / Math.log(symbolCount));

        ArrayList<BigInteger> encryptedBlocks = new ArrayList<>();

        BigInteger n = key[1];
        BigInteger e = key[2];

        ArrayList<BigInteger> blocks = getBlocksFromText(message);

        for (BigInteger block : blocks) {
            BigInteger b = new BigInteger("" + block);
            encryptedBlocks.add(b.modPow(e, n));
        }

        String[] estring = new String[encryptedBlocks.size()];

        for (int i = 0; i < encryptedBlocks.size(); i++) {
            estring[i] = encryptedBlocks.get(i).toString();
        }

        String[] data = new String[2+encryptedBlocks.size()];

        data[0] = message.length() + "";
        data[1] = blockSize + "";

        for (int i = 0; i < estring.length; i++) {
            data[i + 2] = estring[i];
        }

        return data;

    }

    private static ArrayList<BigInteger> getBlocksFromText(String message) {

        ArrayList<BigInteger> blockInts = new ArrayList<>();
        BigInteger letter, symbol;

        for (int blockStart = 0; blockStart < message.length(); blockStart += blockSize) {

            BigInteger blockInt = BigInteger.ZERO;

            for (int i = blockStart; i < Math.min(blockStart + blockSize, message.length()); i++) {
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

    private static double logBigInteger(BigInteger val) {
        int blex = val.bitLength() - 1022;
        if (blex > 0)
            val = val.shiftRight(blex);
        double res = Math.log(val.doubleValue());
        return blex > 0 ? res + blex * LOG2 : res;
    }

}
