package Model;

import javafx.collections.ObservableList;

public class SettingsModel
{
    private ObservableList<HiddenWordModel> totalWordsPool;
    private ObservableList<HiddenWordModel> gameWordsPool;

    public SettingsModel(ObservableList<HiddenWordModel> totalWordsPool, ObservableList<HiddenWordModel> gameWordsPool)
    {
        this.totalWordsPool = totalWordsPool;
        this.gameWordsPool = gameWordsPool;
    }

    public void addWordToTotalWordsPool(String word)
    {
        totalWordsPool.add(totalWordsPool.size(), new HiddenWordModel(word));
    }

    public void addWordToTotalWordsPool(HiddenWordModel hiddenWord)
    {
        totalWordsPool.add(hiddenWord);
    }

    public void addWordToGameWordsPool(String word)
    {
        gameWordsPool.add(gameWordsPool.size(), new HiddenWordModel(word));
    }

    public void addWordToGameWordsPool(HiddenWordModel hiddenWord)
    {
        gameWordsPool.add(gameWordsPool.size(), hiddenWord);
    }
}