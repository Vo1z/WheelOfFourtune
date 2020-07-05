package View;

import Additional.Converter;
import Model.HiddenWordModel;
import Model.SettingsModel;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InitialStage extends Application
{
    private Stage mainStage;

    private ExecutorService executorService;
    private int lastScored;
    private Task<Integer> task;
    private boolean isRotating;

    private Scene rootScene; //Scene with wheel
    private Scene settingsScene; //Scene with settings
    private HiddenWordModel hiddenWord; //Model representation
    private LettersView hiddenWordView; //Visual representation of HiddenWord

    //Root scene
    private GridPane controlInformationPanel;
    private WheelPaneView wheel;
    private int attempts;
    private Text attemptsText;
    private int score;
    private Text scoreText;
    private Text previousBadChars;
    private Button startButton;

    //Settings scene
    private SettingsModel settingsModel;
    private ListView<HiddenWordModel> totalWordsPool;
    private ListView<HiddenWordModel> gameWordsPool;
    private ObservableList<HiddenWordModel> totalWordsObsList;
    private ObservableList<HiddenWordModel> gameWordsObsList;
    private Button settingsButton;

    public static void main(String[] args)
    {
        launch();
    }

    @Override
    public void init() throws Exception
    {
        this.lastScored = 0;
        this.hiddenWord = new HiddenWordModel("nothing");
        this.hiddenWordView = new LettersView(hiddenWord);
        //Root scene
        this.executorService = Executors.newSingleThreadExecutor();
        this.isRotating = false;
        this.wheel = new WheelPaneView(480, 480, 1);
        this.attempts = 3;
        this.attemptsText = new Text("Attempts: " + attempts);
        this.score = 0;
        this.scoreText = new Text("Score: " + score);
        this.previousBadChars = new Text("Previous wrong chars: ");
        this.startButton = new Button("Start");
        this.rootScene = createRootScene();

        //Settings scene
        this.totalWordsPool = new ListView<>();
        this.gameWordsPool = new ListView<>();
        this.settingsButton = new Button("Settings");
        this.settingsScene = creteSettingsScene();
        this.settingsModel = new SettingsModel(totalWordsObsList, gameWordsObsList);
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        this.mainStage = stage;
        this.rootScene.setFill(Color.DARKGRAY);
        this.settingsScene.setFill(Color.DARKGRAY);
        this.mainStage.setTitle("Wheel of fortune");
        this.mainStage.setWidth(500);
        this.mainStage.setHeight(800);
        this.mainStage.setMinWidth(500);
        this.mainStage.setMinHeight(800);
        this.mainStage.setScene(settingsScene);
        this.mainStage.setOnCloseRequest(windowEvent -> System.exit(1));
        this.mainStage.show();
    }

    private Scene createRootScene()
    {
        StackPane stackPane = new StackPane();
        AnchorPane anchorPaneForWheel = new AnchorPane();
        FlowPane wheelSection = createWheelSection();

        stackPane.setPrefSize(500, 500);

        AnchorPane.setBottomAnchor(wheelSection, 10D);
        AnchorPane.setLeftAnchor(wheelSection, 10D);
        anchorPaneForWheel.getChildren().add(wheelSection);

        controlInformationPanel = createControlInformationPanelSection();
        stackPane.getChildren().add(controlInformationPanel);
        stackPane.getChildren().add(anchorPaneForWheel);

        return new Scene(stackPane);
    }

    private GridPane createControlInformationPanelSection()
    {
        GridPane createdGridPane = new GridPane();
        FlowPane flowPaneForTextAreas = new FlowPane();
        FlowPane flowPaneForButtons = new FlowPane();

        createdGridPane.setAlignment(Pos.BASELINE_CENTER);
        createdGridPane.setVgap(10);

        flowPaneForButtons.setAlignment(Pos.CENTER);
        flowPaneForButtons.setHgap(5);

        flowPaneForTextAreas.setAlignment(Pos.CENTER);
        flowPaneForTextAreas.setHgap(50);

        Button startButton = new Button("Start/Stop");
        startButton.setFont(Font.font("System", FontWeight.BOLD, 16));
        startButton.setPrefSize(600, 50);
        startButton.setOnAction(
                actionEvent ->
                {
                    if (isRotating == false)
                    {
                        this.isRotating = !isRotating;
                        rotateWheel();
                    }
                    else
                    {
                        String guessedChar;
                        this.isRotating = !isRotating;

                        while (true)
                        {
                            TextInputDialog textInputDialog = new TextInputDialog("");
                            textInputDialog.setTitle("Input window");
                            textInputDialog.setHeaderText("Enter character");
                            textInputDialog.showAndWait();

                            guessedChar = textInputDialog.getResult();

                            if (guessedChar != null && guessedChar.length() != 1)
                            {
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setTitle("Alert");
                                alert.setHeaderText("You are allowed to enter one char only");
                                alert.showAndWait();
                            }
                            else
                            {
                                try
                                {
                                    if (hiddenWord.isCharacterGuessed(guessedChar.charAt(0)) && guessedChar != null)
                                    {
                                        hiddenWord.displayChar(guessedChar.charAt(0));
                                        hiddenWordView.refresh();

                                        this.score += this.wheel.getScore();
                                        this.scoreText.setText("Score:" + score);

                                        if (hiddenWord.isWordGuessed())
                                        {
                                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                            alert.setTitle("Win");
                                            alert.setHeaderText("Congratulations! You've won with score " + score);

                                            alert.show();

                                            this.mainStage.setScene(settingsScene);
                                        }
                                    } else
                                    {
                                        if (attempts <= 0)
                                        {
                                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                            alert.setTitle("Defeat");
                                            alert.setHeaderText("You've lost :(");

                                            alert.show();

                                            this.mainStage.setScene(settingsScene);
                                        }

                                        this.previousBadChars.setText(previousBadChars.getText() + guessedChar + ", ");
                                        this.attempts -= 1;
                                        this.attemptsText.setText("Attempts: " + attempts);
                                    }
                                }
                                catch (NullPointerException ex)
                                {
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("Alert");
                                    alert.setHeaderText("Character must be selected");
                                    alert.showAndWait();
                                }
                                break;
                            }
                        }
                    }
                }
        );

        Button settingsButton = new Button("Settings");
        settingsButton.setFont(Font.font("System", FontWeight.BOLD, 16));
        settingsButton.setPrefSize(600,50);
        settingsButton.setOnAction(
                actionEvent ->
                {
                    this.mainStage.setScene(settingsScene);
                }
        );

        flowPaneForTextAreas.getChildren().add(attemptsText);
        flowPaneForTextAreas.getChildren().add(previousBadChars);

        flowPaneForButtons.getChildren().add(startButton);
        flowPaneForButtons.getChildren().add(settingsButton);

        createdGridPane.add(flowPaneForTextAreas, 1, 1, 1, 1);
        createdGridPane.add(flowPaneForButtons, 1, 2, 1, 1);
        createdGridPane.add(hiddenWordView, 1, 3, 1, 1);

        return createdGridPane;
    }

    private FlowPane createWheelSection()
    {
        FlowPane createdFlowPane = new FlowPane();

        scoreText.setFont(Font.font("System", FontWeight.BOLD, 20));
        createdFlowPane.setOrientation(Orientation.HORIZONTAL);
        createdFlowPane.setHgap(10);
        createdFlowPane.setVgap(10);

        createdFlowPane.getChildren().add(wheel);
        createdFlowPane.getChildren().add(scoreText);

        return createdFlowPane;
    }

    private void rotateWheel()
    {
        task = new Task<Integer>()
        {
            @Override
            protected Integer call() throws Exception
            {
                while (isRotating)
                {
                    wheel.rotate();

                    try {Thread.sleep(30);}
                    catch (InterruptedException ex) {ex.printStackTrace();}
                }

                return wheel.getScore();
            }
        };

        task.setOnFailed(event -> task = null);
        task.setOnSucceeded(event ->
                            {
                                System.out.println(task.getValue());
                            }
        );

        //Starts game
        executorService.execute(task);
    }

    private Scene creteSettingsScene()
    {
        GridPane settingsRoot = new GridPane();
        Button goBackButton = new Button("Back");
        Button addNewWordButton = new Button("Add new");
        Button startNewGameButton = new Button("Start new game");

        addNewWordButton.setPrefSize(500, 100);
        addNewWordButton.setOnAction(actionEvent -> addWord());

        goBackButton.setPrefSize(500, 100);
        goBackButton.setOnAction(actionEvent -> mainStage.setScene(rootScene));

        startNewGameButton.setPrefSize(500, 100);
        startNewGameButton.setOnAction(actionEvent ->
                                       {
                                           startNewGame();
                                       }
        );


        this.totalWordsObsList = FXCollections.observableArrayList(Converter.extractHiddenWords("res/words"));
        this.gameWordsObsList = FXCollections.observableArrayList(new HiddenWordModel("nothing"));
        this.totalWordsPool = new ListView<>(totalWordsObsList);
        this.totalWordsPool.setPrefHeight(500);
        this.gameWordsPool = new ListView<>(gameWordsObsList);
        this.gameWordsPool.setPrefHeight(500);

        this.totalWordsPool.setOnMouseClicked(event ->
                                              {
                                                  if (event.getButton() == MouseButton.PRIMARY)
                                                  {
                                                      if (event.getTarget() instanceof Labeled)
                                                      {
                                                          HiddenWordModel item = totalWordsPool.getSelectionModel().getSelectedItem();
                                                          totalWordsPool.getItems().remove(item);
                                                          settingsModel.addWordToGameWordsPool(item);
                                                      }
                                                  }
                                              }
        );

        this.gameWordsPool.setOnMouseClicked(event ->
                                             {
                                                 if (event.getButton() == MouseButton.PRIMARY)
                                                 {
                                                     if (event.getTarget() instanceof Labeled)
                                                     {
                                                         HiddenWordModel item = gameWordsPool.getSelectionModel().getSelectedItem();
                                                         gameWordsPool.getItems().remove(item);
                                                         settingsModel.addWordToTotalWordsPool(item);
                                                     }
                                                 }
                                             }
        );

        settingsRoot.add(gameWordsPool, 1, 1);
        settingsRoot.add(totalWordsPool, 2, 1);
        settingsRoot.add(startNewGameButton, 1, 2, 2, 1);
        settingsRoot.add(addNewWordButton, 1, 4, 2, 1);
        settingsRoot.add(goBackButton, 1, 6, 2, 1);
        return new Scene(settingsRoot);
    }

    private void addWord()
    {
        TextInputDialog textInputDialog = new TextInputDialog("");
        textInputDialog.setTitle("Input window");
        textInputDialog.setHeaderText("Enter new word");
        textInputDialog.showAndWait();

        this.settingsModel.addWordToTotalWordsPool(textInputDialog.getResult());
    }

    private void startNewGame()
    {
        this.attempts = 3;
        this.attemptsText.setText("Attempts :" + attempts);
        this.score = 0;
        this.scoreText.setText("Score: " + score);
        this.previousBadChars.setText("Previous chars: ");

        this.controlInformationPanel.getChildren().remove(hiddenWordView);

        this.hiddenWord = this.gameWordsPool.getItems().get((int) (Math.random() * gameWordsPool.getItems().size()));
        this.hiddenWordView = new LettersView(hiddenWord);
        this.controlInformationPanel.add(hiddenWordView, 1, 3, 1, 1);

        this.mainStage.setScene(rootScene);

        System.out.println(hiddenWord + " is chosen");
    }
}