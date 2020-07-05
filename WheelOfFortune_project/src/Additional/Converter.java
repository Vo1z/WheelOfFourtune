package Additional;

import Model.HiddenCharacterModel;
import Model.HiddenWordModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Converter
{
    public static ArrayList<HiddenCharacterModel> stringToHiddenCharArray(String word)
    {
        ArrayList<HiddenCharacterModel> createdHiddenWordChars = new ArrayList<>();

        word.chars().forEach(
                value ->
                {
                    createdHiddenWordChars.add(new HiddenCharacterModel((char)value));
                }
        );

        return createdHiddenWordChars;
    }

    public static ArrayList<HiddenWordModel> extractHiddenWords(String path)
    {
        //Assign
        ArrayList<HiddenWordModel> hiddenWords = new ArrayList<>();
        String fileContent = "";
        String wordRegex;
        Pattern wordPattern;
        Matcher wordMatcher;

        //Reads file
        try
        {
            File wordsFile = new File(path);
            Scanner sc = new Scanner(wordsFile);

            while (sc.hasNextLine())
            {
                fileContent += sc.nextLine();
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        //Finds and adds words to the collection
        wordRegex = "\\w+";
        wordPattern = Pattern.compile(wordRegex);
        wordMatcher = wordPattern.matcher(fileContent);

        while(wordMatcher.find())
        {
            HiddenWordModel hw = new HiddenWordModel(wordMatcher.group());
            hiddenWords.add(hw);
        }

        //fixme debug
//        System.out.println("All words pool {");
//        hiddenWords.stream().forEach(hiddenWord -> System.out.println(hiddenWord));
//        System.out.println("}");

        return hiddenWords;
    }
}