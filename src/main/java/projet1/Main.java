package projet1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Scanner;

public class Main {
    private static char alphabet[] = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    
    public static String Encrypt(String text, String key) {
        StringBuffer sb = new StringBuffer(text);
        for (int i = 0; i < text.length(); i++)
        {
            int decalage = (int)key.charAt(i % key.length()) - 97;
            int newCharCode = ((int) text.charAt(i) - 97 + decalage) % 26 + 97;
            sb.setCharAt(i, (char) newCharCode);
        }

        text = sb.toString();
        return text;
    }

    public static String Decrypt(String text, String key) {
        StringBuffer sb = new StringBuffer(text);

        for (int i = 0; i < text.length(); i++)
        {
            int decalage = (int) key.charAt(i % key.length()) - 97;

            int currentLetter = (int) text.charAt(i);
            if (currentLetter - decalage < 97) {
                currentLetter += 26;
            }

            int newCharCode = (currentLetter - 97 - decalage) % 26 + 97;
            sb.setCharAt(i, (char) newCharCode);
        }

        text = sb.toString();
        return text;
    }
    
    private static Hashtable<Character, Double> getFreqFromText(StringBuilder colonne) {
        Hashtable<Character, Double> freq = new Hashtable<Character, Double>();
        char charDeColonne[] = colonne.toString().toCharArray();
        double frequency = 0;
        double counter = 0;
        double Total = charDeColonne.length;

        for (char c : alphabet) {
            for (char cipher : charDeColonne) {
                if (cipher == c) {
                    counter++;
                }
            }
            frequency = counter / Total;
            freq.put(c, frequency);
            frequency = 0;
            counter = 0;
        }
        return freq;
    }
    
    private static Hashtable<Character, Double> getFreq() {
        Hashtable<Character, Double> freqdeLettres = new Hashtable<Character, Double>();
        //wé jabuze, é alor
        freqdeLettres.put('a', .08167);
        freqdeLettres.put('b', .01492);
        freqdeLettres.put('c', .02782);
        freqdeLettres.put('d', .04253);
        freqdeLettres.put('e', .12702);
        freqdeLettres.put('f', .0228);
        freqdeLettres.put('g', .02015);
        freqdeLettres.put('h', .06094);
        freqdeLettres.put('i', .06966);
        freqdeLettres.put('j', .00153);
        freqdeLettres.put('k', .00772);
        freqdeLettres.put('l', .04025);
        freqdeLettres.put('m', .02406);
        freqdeLettres.put('n', .06749);
        freqdeLettres.put('o', .07507);
        freqdeLettres.put('p', .01929);
        freqdeLettres.put('q', .00095);
        freqdeLettres.put('r', .05987);
        freqdeLettres.put('s', .06327);
        freqdeLettres.put('t', .09056);
        freqdeLettres.put('u', .02758);
        freqdeLettres.put('v', .00978);
        freqdeLettres.put('w', .02361);
        freqdeLettres.put('x', .00150);
        freqdeLettres.put('y', .01974);
        freqdeLettres.put('z', .00074);

        return freqdeLettres;
    }
    
    private static char trouveDeplacement(StringBuilder colonne) {
        Hashtable<Character, Double> letterFrequency = getFreq();
        Hashtable<Character, Double> cipherFrequency = getFreqFromText(colonne);
        char finalCharKey;

        double allowedError = Double.MAX_VALUE;
        int shiftValue = -1;
        for (int deplacement = 0; deplacement < alphabet.length; deplacement++) {
            double Error = 0;
            for (char c : alphabet) {
                char shifted = (char) (c + deplacement);
                //ASCII FTW nohomo
                if (shifted > 122) {
                    shifted = (char) (shifted - 122);
                    shifted = (char) (shifted + 97);
                }
                //diff entre erreur cécar et english
                double diff = Math.abs(letterFrequency.get(c) - cipherFrequency.get(shifted));
                Error += diff;
            }
            //Shoppe celui avec le moins d'erreurs
            if (Error < allowedError) {
                shiftValue = deplacement;
                allowedError = Error;
            }
        }

        finalCharKey = (char) ('a' + shiftValue);
        return finalCharKey;//sah quel plaisir
    }
    
    private static String findKey(String txt, int keyLength) {
        Hashtable<Integer, StringBuilder> colonneMots = new Hashtable<Integer, StringBuilder>();
        char originalText[] = txt.toCharArray();

        //split original text into x columns
        //where x is the key length
        for (int col = 0; col < keyLength; col++) {
            StringBuilder column = new StringBuilder();
            for (int i = col; i < originalText.length; i += keyLength) {
                column.append(originalText[i]);
            }
            colonneMots.put(col, column);
        }

        StringBuilder key = new StringBuilder();
        for (int i = 0; i < keyLength; i++) {
            char keyChar = trouveDeplacement(colonneMots.get(i));//get each letter for the key
            key.append(keyChar);
        }

        return key.toString();
    }
    
    private static int findKeyLenght(String txt) {
        char originalToChar[] = txt.toCharArray();
        int amountSame[] = new int[15];//15 max key lenght

        //find key lenght from 2 to 15
        for (int shift = 2; shift < 15; shift++) {
            for (int i = 0; i < originalToChar.length; i++) {
                if (i + shift + 2 >= originalToChar.length) {
                    break;
                }
                //compare shifted message with original message
                if (originalToChar[i] == originalToChar[i + shift]) {
                    amountSame[shift] += 1;
                }
            }
        }
        int keyLength = 0;
        int same = 0;
        for (int j = 2; j < amountSame.length; j++) {
            //find key length if most are equal
            if (same < amountSame[j]) {
                keyLength = j;
                same = amountSame[j];
            }
        }
        return keyLength;
    }
    
    public static String Analyse(String txt) throws IOException {
        //try to find the lenght of the key
        int keyLength = findKeyLenght(txt);
        //get the key and return it
        String key = findKey(txt, keyLength);
	return key;
    }
        
    @SuppressWarnings("fallthrough")
    public static void main(String[] args) throws IOException {
        Scanner myScan = new Scanner(System.in);
        System.out.println("Enter the name of the file you want to process without the extension (example: plain):");
        String FileName = myScan.nextLine();
        Preprocess.preprocessFile(FileName + ".txt", FileName + "-preprocessed.txt", true);
        FileName = FileName + "-preprocessed.txt";
        
        File input = new File(FileName);
        BufferedReader in = new BufferedReader(new FileReader(input));
        String text = in.readLine();
        System.out.println("Enter the number of the option you want to use\n1 : Encrypt\n2 : Decrypt\n3 : Decrypt without key\nAnything else : quit\n");
        
        int option = myScan.nextInt();
        String result = "";
        String key;
        switch (option) {
            case 1:
                System.out.println("Enter a key :");
                key = myScan.next();
                result = Encrypt(text, key);
                break;
            case 2:
                System.out.println("Enter the key :");
                key = myScan.next();
                result = Decrypt(text, key);
                break;
            case 3:
                System.out.println("Computing...");
                String finalKey = Analyse(text);
                System.out.println("The key seems to be: '" + finalKey + "'");
                result = Decrypt(text, finalKey);
                break;
            default:
                System.out.println("Quitting.\n");
                System.exit(0);
        }
        System.out.println("Enter the output file name (example: encrypted):");
        String outputfile = myScan.next() + ".txt";
        try (FileWriter out = new FileWriter(outputfile, false)) {
            out.write(result);
            input.delete();
        }
    }
}