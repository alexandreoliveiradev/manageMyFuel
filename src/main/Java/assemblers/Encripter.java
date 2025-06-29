package assemblers;

import java.util.Arrays;
import java.util.Base64;

public class Encripter {

    public static String encryptText(String text){
        if (text.isEmpty()){
            return "Nothing to encrypt.";
        }

        String output = "";

        for (char c: text.toCharArray()) {
            output = output.concat((String.valueOf(replaceChar(c))));
        }

        output = everySix(output);

        return reverse(output);

    }

    private static String everySix(String text) {

        String output = "";

        char [] c = text.toCharArray();

        for (int i = 0; i< c.length; i++){
            String chr = String.valueOf(c[i]);
            if (i > 4 && i%6==0) {
                output = output.concat(every());
            } if(i==0){
                output = output.concat(begOrEng());
            }
            output = output.concat(chr);
        }
        return output.concat(begOrEng());
    }

    private static String begOrEng() {
        int x = (int) (Math.random()*10);
        if( x < 2) {
            return "»";
        } else if( x < 4){
            return "«";
        } else if( x < 6){
            return "=";
        } else if( x < 8){
            return "£";
        } else {
            return "º";
        }
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
            case  'a':
                option = '9';
                break;
            case 'b':
                option = 'l';
                break;
            case 'c':
                option = 'q';
                break;
            case 'd':
                option = '8';
                break;
            case  'e':
                option = 'c';
                break;
            case 'f':
                option = '2';
                break;
            case 'g':
                option = '7';
                break;
            case 'h':
                option = 'x';
                break;
            case  'i':
                option = '6';
                break;
            case 'j':
                option = '~';
                break;
            case 'k':
                option = '&';
                break;
            case 'l':
                option = '5';
                break;
            case  'm':
                option = 'p';
                break;
            case 'n':
                option = 'b';
                break;
            case 'o':
                option = 'i';
                break;
            case 'p':
                option = '4';
                break;
            case  'q':
                option = 'm';
                break;
            case 'r':
                option = '3';
                break;
            case 's':
                option = 'd';
                break;
            case 't':
                option = 'z';
                break;
            case  'u':
                option = 'j';
                break;
            case 'v':
                option = 'e';
                break;
            case 'x':
                option = 'n';
                break;
            case 'y':
                option = '1';
                break;
            case  'w':
                option = '0';
                break;
            case 'z':
                option = 'r';
                break;
            case '0':
                option = '(';
                break;
            case '1':
                option = 'a';
                break;
            case  '2':
                option = 'g';
                break;
            case '3':
                option = '*';
                break;
            case '4':
                option = '}';
                break;
            case '5':
                option = '%';
                break;
            case  '6':
                option = '$';
                break;
            case '7':
                option = ':';
                break;
            case '8':
                option = 'u';
                break;
            case '9':
                option = 'f';
                break;
            case  '@':
                option = at();
                break;
            case '.':
                option = point();
                break;
            case ' ':
                break;
            default:
                option = character;
                break;
        }
        return option;
    }

    private static char point(){
        if(Math.random()*10 > 4) {
            return 'v';
        } else return '>';
    }

    private static char at(){
        if(Math.random()*10 > 4) {
            return 's';
        } else return 't';
    }


    private static String every(){
        int x = (int) (Math.random()*10);
        if( x <= 2) {
            return "#";
        } else if( x <= 6){
            return "!";
        } else {
            return ";";
        }
    }

    public static String encodeToB64(String input) {
        // Encode the input text to Base64
        byte[] encodedBytes = Base64.getEncoder().encode(input.getBytes());

        // Convert the encoded bytes to a string
        return new String(encodedBytes);
    }

   /* public static void main(String[] args) {
        String futurJson = "W3sicGFzc3dvcmQiOiLCq30qZ2HCqyIsImNhcnMiOltdLCJ1c2VyTmFtZSI6ImFsZXgiLCJjYXJGdWVscyI6IiIsImNhckJyYW5kcyI6W10sIm51bUNhcnMiOjAsImNhck5hbWVzIjpbXSwiZGVmYXVsdENhciI6Ik5vIGNhciBhZGRlZCB5ZXQuIn1d";
        System.out.println(Encripter.encodeToB64(futurJson));
        System.out.println(Decripter.decodeB64(futurJson));
    }*/
}
