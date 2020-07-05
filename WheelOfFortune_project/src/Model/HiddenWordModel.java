package Model;

import java.util.*;
import Additional.Converter;

public class HiddenWordModel implements Iterable<HiddenCharacterModel>, Iterator<HiddenCharacterModel>
{
    private String hiddenWordStr;
    private ArrayList<HiddenCharacterModel> hiddenWordChars;

    private int iter;

    public HiddenWordModel(String hiddenWord)
    {
        this.hiddenWordStr = hiddenWord;
        this.iter = 0;
        hiddenWordChars = Converter.stringToHiddenCharArray(hiddenWord);
    }

    public void displayChar(char characterToDisplay)
    {
        hiddenWordChars.stream().forEach(
                hiddenCharacter ->
                {
                    if (hiddenCharacter.getDisplayedChar() == characterToDisplay)
                    {
                        hiddenCharacter.setHidden(false);
                    }
                }
        );
    }

    public boolean isCharacterGuessed(char character)
    {
        return hiddenWordChars.stream()
                .anyMatch(hiddenCharacter -> hiddenCharacter.getDisplayedChar() == character);
    }

    public boolean isWordGuessed()
    {
        return hiddenWordChars.stream()
                .allMatch(hiddenCharacter -> hiddenCharacter.isHidden() == false);
    }

    public ArrayList<HiddenCharacterModel> getHiddenWord()
    {
        return this.hiddenWordChars;
    }

    public int length()
    {
        return hiddenWordChars.size();
    }
    @Override
    public String toString()
    {
        return this.hiddenWordStr;
    }

    @Override
    public Iterator iterator()
    {
        return this;
    }

    @Override
    public boolean hasNext()
    {
        if (iter < hiddenWordChars.size())
        {
            return true;
        }
        else
        {
            iter = 0;
            return false;
        }
    }

    @Override
    public HiddenCharacterModel next()
    {
        iter++;
        return hiddenWordChars.get(iter);
    }
}
