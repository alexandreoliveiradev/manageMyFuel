package utils;

import assemblers.Decripter;
import assemblers.Encripter;

public class Crypter {

    public static String decrypt(String text){
        return Decripter.decryptText(text);
    }

    public static String encrypt (String text){
        return Encripter.encryptText(text);
    }
}
