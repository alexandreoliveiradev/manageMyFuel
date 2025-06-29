package assemblers;

import java.awt.*;
import java.util.Arrays;
import java.util.Base64;

public class Decripter {

    public static String decryptText(String text){
        if (text.equals("") || text == null){
            return "Nothing to decrypt.";
        }

        String input = reverse(text);

        input = replaceEverySix(input);

        String output = "";


        for (char c: input.toCharArray()) {
            output = output.concat((String.valueOf(replaceChar(c))));
        }

        return output;
    }

    private static String replaceEverySix(String text) {
        return text.replaceAll("»", "").replaceAll("«", "").
                replaceAll("=", "").replaceAll("£", "").
                replaceAll("º", "").replaceAll("#", "").
                replaceAll("!", "").replaceAll(";", "");

    }

    private static String reverse(String toReverse) {
        char [] string = toReverse.toCharArray();

        char [] result = new char [string.length];

        for (int i = 0; i<string.length; i++){
            result[i] = string[string.length-i-1];
        }

        return Arrays.toString(result).replace("[", "").replace("]", "").
                replaceAll(", ","");
    }

    private static char replaceChar(char character){

        char option = '_';
        switch(character){
            case  '9':
                option = 'a';
                break;
            case 'l':
            case 'L':
                option = 'b';
                break;
            case 'q':
            case 'Q':
                option = 'c';
                break;
            case '8':
                option = 'd';
                break;
            case 'c':
            case 'C':
                option = 'e';
                break;
            case '2':
                option = 'f';
                break;
            case '7':
                option = 'g';
                break;
            case 'x':
            case 'X':
                option = 'h';
                break;
            case  '6':
                option = 'i';
                break;
            case '~':
                option = 'j';
                break;
            case '&':
                option = 'k';
                break;
            case '5':
                option = 'l';
                break;
            case 'p':
            case 'P':
                option = 'm';
                break;
            case 'b':
            case 'B':
                option = 'n';
                break;
            case 'i':
            case 'I':
                option = 'o';
                break;
            case '4':
                option = 'p';
                break;
            case  'm':
            case 'M':
                option = 'q';
                break;
            case '3':
                option = 'r';
                break;
            case 'd':
            case 'D':
                option = 's';
                break;
            case 'z':
            case 'Z':
                option = 't';
                break;
            case 'j':
            case 'J':
                option = 'u';
                break;
            case 'e':
            case 'E':
                option = 'v';
                break;
            case 'n':
            case 'N':
                option = 'x';
                break;
            case '1':
                option = 'y';
                break;
            case  '0':
                option = 'w';
                break;
            case 'r':
            case 'R':
                option = 'z';
                break;
            case '(':
                option = '0';
                break;
            case 'a':
            case 'A':
                option = '1';
                break;
            case 'g':
            case 'G':
                option = '2';
                break;
            case '*':
                option = '3';
                break;
            case '}':
                option = '4';
                break;
            case '%':
                option = '5';
                break;
            case  '$':
                option = '6';
                break;
            case ':':
                option = '7';
                break;
            case 'u':
            case 'U':
                option = '8';
                break;
            case 'f':
            case 'F':
                option = '9';
                break;
            case 's':
            case 'S':
                option = '@';
                break;
            case 't':
            case 'T':
                option = '@';
                break;
            case 'v':
            case 'V':
                option = '.';
                break;
            case '>':
                option = '.';
                break;
            case '_':
                option = ' ';
                break;
            default:
                option = character;
                break;
        }
        return option;
    }

    public static String decodeB64(String input) {
        byte[] decodedBytes = null;
        // Decode the Base64 input
        try {
            decodedBytes = Base64.getDecoder().decode(input);
        }
        catch (Exception e){
            Messenger.Popup(new Frame("Error"), "Error decripting file", "E");
        }
        // Convert the decoded bytes to a string
        return new String(decodedBytes);
    }



}