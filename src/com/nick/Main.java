package com.nick;

import java.io.*;
import java.math.BigInteger;

public class Main {

    public static void main(String[] args) throws Exception{

        KeyMaker.makeKeys(1024);

        BigInteger[][] keys = new BigInteger[2][2];

        keys[0] = KeyMaker.getPubKey();
        keys[1] = KeyMaker.getPrivKey();

        File file = new File(args[0]);

        byte[] in = new byte[(int) file.length()];

        try (InputStream is = new FileInputStream(file)) {

            is.read(in);

        } catch (IOException ex) { ex.printStackTrace(); }

        BigInteger bi = new BigInteger(in);

        String text = bi.toString();

        String[] estring = Encryptor.encryptMessage(text, keys[0]);

        file = new File(args[0] + ".cry");

        SaveData.writeFile(file, estring);

        String[] data = ReadData.readFile(file);

        String dstring = Decryptor.decryptMessage(data, keys[1]);

        try (OutputStream os = new FileOutputStream("new " + args[0])) {
            BigInteger temp = new BigInteger(dstring + "");
            byte[] out = temp.toByteArray();
            os.write(out);
        } catch (Exception ex) { ex.printStackTrace(); }

    }

}
