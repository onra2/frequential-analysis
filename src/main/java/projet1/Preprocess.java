package projet1;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


public class Preprocess 
{
    public static final String DIACRITICS = "àâäéèêëîïôùûç";
    public static final String DIACRITICS_REPLACE = "aaaeeeeiiouuc";
    public static final String TO_REMOVE = " \n\t\"+-*/=&|'()[]{},;.!?#~_";
    
    public static int indexOf(char c, String str)
    {
        for(int i = 0; i < str.length(); i++)
        if(str.charAt(i) == c)
            return i;
    return -1;
    }
    
    public static String preprocessLine(String line)
    {
        line = line.toLowerCase();
        String out = "";
        for(int i = 0; i < line.length(); i++)
        {
            char c = line.charAt(i);            
            
            int index = indexOf(c, DIACRITICS);
            if(index != -1) //char is not a diacritic
                out += DIACRITICS_REPLACE.charAt(index);
            else if(indexOf(c, TO_REMOVE) != -1) //char has to be removed
            {}
            else if(c == 'æ')
                out += "ae";
            else if(c == 'œ')
                out += "oe";
            else
                out += c;
        }
        
        return out;
    }
    
    public static void preprocessFile(String input, String output, boolean ignoreLines)
            throws FileNotFoundException, IOException
    {
        Scanner in = new Scanner(new FileReader(input, StandardCharsets.UTF_8));
        PrintStream out = new PrintStream(new FileOutputStream(output));
        
        while(in.hasNextLine())
        {
            if(!ignoreLines)
                out.println(preprocessLine(in.nextLine()));
            else
                out.print(preprocessLine(in.nextLine()));
        }
        
    }
}