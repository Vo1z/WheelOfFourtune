package Model;

public class HiddenCharacterModel
{
    private boolean isHidden;
    private char displayedChar;

    public HiddenCharacterModel(char displayedChar)
    {
        this.displayedChar = displayedChar;
        this.isHidden = true;
    }

    public HiddenCharacterModel(boolean isHidden, char displayedChar)
    {
        this.displayedChar = displayedChar;
        this.isHidden = isHidden;
    }

    public boolean isHidden()
    {
        return isHidden;
    }

    public void setHidden(boolean hidden)
    {
        isHidden = hidden;
    }

    public char getDisplayedChar()
    {
        return displayedChar;
    }
}
