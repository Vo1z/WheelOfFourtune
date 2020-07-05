package View;

import Model.HiddenWordModel;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class LettersView extends GridPane
{
    private HiddenWordModel hiddenWord;

    public LettersView(HiddenWordModel hiddenWord)
    {
        this.hiddenWord = hiddenWord;
        refresh();

        //Configuration
        this.setHgap(10);
        this.setAlignment(Pos.CENTER);
    }

    public void refresh()
    {
        for (int i = 0; i < hiddenWord.length(); i++)
        {
            if (hiddenWord.getHiddenWord().get(i).isHidden())
            {
                Rectangle rectangle = new Rectangle(50, 40);
                rectangle.setFill(Color.LIGHTGRAY);

                this.add(rectangle, i, 1);
            }
            else
            {
                Text text = new Text(hiddenWord.getHiddenWord().get(i).getDisplayedChar()+"");
                text.setFont(new Font("System", 32));
                this.add(text, i, 1);
            }
        }
    }
}
