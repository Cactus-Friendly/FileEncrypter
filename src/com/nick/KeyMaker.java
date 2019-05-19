package com.nick;

import java.math.BigInteger;
import java.util.Random;

public final class KeyMaker {

    private static BigInteger[] privKey = new BigInteger[3];
    private static BigInteger[] pubKey = new BigInteger[3];
    private static final Random RAND = new Random();

    public static BigInteger[] getPubKey() {
        return pubKey;
    }

    public static BigInteger[] getPrivKey() {
        return privKey;
    }

    public static void makeKeys(int keySize) {

        BigInteger size = new BigInteger(keySize + "");

        //Creating the q and p values
        BigInteger p = BigInteger.probablePrime(keySize, RAND);
        BigInteger q = BigInteger.probablePrime(keySize, RAND);

        //Making sure that the values did not happen to be the same
        while (p.equals(q)) {
            p = BigInteger.probablePrime(keySize, RAND);
            q = BigInteger.probablePrime(keySize, RAND);
        }

        /**
         * e = Public key
         * d = private key
         */
        //Getting the n value used to encrypt data
        BigInteger n = p.multiply(q);
        BigInteger e, p1, q1, d;
        p1 = p.subtract(BigInteger.ONE);
        q1 = q.subtract(BigInteger.ONE);

        //Making the e value and making sure that the
        //biggest number that the public key and (p-1)*(q-1) can be divided by
        //is one
        while (true) {
            e = BigInteger.probablePrime(keySize, RAND);
            if (gcd(e, p1.multiply(q1)).equals(BigInteger.ONE)) {
                break;
            }
        }

        //Getting the private key from the inverse mod of the public key and (p-1)*(q-1)
        d = e.modInverse(p1.multiply(q1));

        pubKey[0] = size;
        pubKey[1] = n;
        pubKey[2] = e;
        privKey[0] = size;
        privKey[1] = n;
        privKey[2] = d;

    }

    private static BigInteger gcd(BigInteger a, BigInteger b) {

        //Finding the Greatest Common Denominator of
        //two BigIntegers
        while (!a.equals(BigInteger.ZERO)) {
            BigInteger temp = a;
            a = b.mod(a);
            b = temp;
        }

        return b;

    }


}
