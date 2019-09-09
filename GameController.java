package FX.FXSudoku;

import com.jfoenix.controls.JFXButton;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    @FXML private GridPane boardPane;
    @FXML private JFXButton backButton,newGame,settings;
    @FXML private HBox optionPane;
    @FXML private AnchorPane navBackground;
    @FXML private Pane navBar;
    @FXML private Label diffLabel, timer;
    @FXML private Pane winner;
    private int seconds = 0;
    private Difficulty difficulty;
    private byte[][] board;
    public GameController (Difficulty diff) {
        difficulty = diff;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        board = SudokuBoard.getBoard(difficulty);
        Timeline clock = new Timeline(new KeyFrame(Duration.seconds(1), e -> timer.setText(getTime(seconds++))));
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
        diffLabel.setText(getDifficulty(difficulty));
        newGame.setOnAction(e -> {
            PopOver pop = new PopOver();
            pop.show(newGame);
        });
        for (Node n: optionPane.getChildren()) {
            JFXButton currentBtn = (JFXButton)n;
            currentBtn.setTextFill(Color.BLACK);
            if (currentBtn.getText().matches("[1-9]"))
                n.setStyle("-fx-background-radius: 30px; -fx-background-color: " + getColor(Integer.parseInt(currentBtn.getText())) + ";");
            else
                n.setStyle("-fx-background-radius: 30px; -fx-background-color: " + getColor(0) + ";");
            currentBtn.setOnAction(e -> {

                randomColor();
                ((JFXButton)((navBar.getScene()).focusOwnerProperty().get())).setText(currentBtn.getText());
                (((navBar.getScene()).focusOwnerProperty().get())).setStyle("-fx-background-radius: 23px; -fx-background-color: " + getColor(currentBtn.getText().equals(" ") ? 0 : Integer.parseInt(currentBtn.getText())) + ";");
            });
        }
        for (Node n: boardPane.getChildren()) {
            JFXButton currentBtn = (JFXButton)n;
            GridPane.setRowIndex(n,GridPane.getRowIndex(n) == null ? 0 : GridPane.getRowIndex(n));
            GridPane.setColumnIndex(n,GridPane.getColumnIndex(n) == null ? 0 : GridPane.getColumnIndex(n));
            currentBtn.setText(Integer.toString(board[GridPane.getRowIndex(n)][GridPane.getColumnIndex(n)]));
            if (!currentBtn.getText().equals("0"))
                currentBtn.setFocusTraversable(false);
            else
                currentBtn.setTextFill(Color.WHITE);
            update(currentBtn);
            currentBtn.setOnKeyPressed(e -> {
                if (e.getCode().isDigitKey())
                    currentBtn.setText(e.getCode().toString().substring(5));
                else if (e.getCode() == KeyCode.BACK_SPACE)
                    currentBtn.setText("0");
                update(currentBtn);
                if (boardFull())
                    checkWin();
            });
        }
    }
    Timeline clock;
    boolean playing;
    private void randomColor() {
        if (clock == null) {
            clock = new Timeline(new KeyFrame(Duration.seconds(0.05), e -> {
                for (Node n : boardPane.getChildren()) {
                    JFXButton b = (JFXButton) n;
                    //b.setText(Integer.toString(new java.util.Random().nextInt(9) + 1));
                    b.setStyle("-fx-background-radius: 23px; -fx-background-color: " + getColor((new java.util.Random()).nextInt(9) + 1) + ";");
                }
            }));
            clock.setCycleCount(30);
            clock.play();
            playing = true;
        } else {
            if (playing) {
                clock.pause();
                playing = false;
            }
            else {
                clock.play();
                playing = true;
            }
        }
        clock.setOnFinished(e -> {
            for (Node n: boardPane.getChildren())
                update((JFXButton)n);
        });


    }
    private String getDifficulty (Difficulty difficulty) {
        switch (difficulty) {
            case EASY: return "EASY";
            case MEDIUM: return "MEDIUM";
            case HARD: return "HARD";
            default: return "ERROR";
        }
    }
    private String getTime(int sec) {
        int minutes = sec/60;
        int seconds = sec%60;
        return String.format("%02d:%02d",minutes,seconds);
    }
    public void checkWin() {
        boolean gameWon = true;/*
        for (Node n: boardPane.getChildren()) {
            JFXButton btn = (JFXButton)n;
            if (!btn.getText().matches("[1-9]"))
                gameWon = false;
            else if (Integer.parseInt(btn.getText()) != board[GridPane.getRowIndex(n)][GridPane.getColumnIndex(n)])
                gameWon = false;
        }*/
        if (gameWon) {
            FadeTransition fade = new FadeTransition(Duration.seconds(0.7),navBackground);
            fade.setFromValue(0);
            fade.setToValue(1);
            fade.play();
            navBackground.setVisible(true);
            TranslateTransition translate = new TranslateTransition(Duration.seconds(0.5),winner);
            translate.setToY(-500);
            //translate.play();
        }
    }
    public void update (JFXButton currentBtn) {
        if (currentBtn.getText().equals(" "))
            currentBtn.setText("0");
        currentBtn.setStyle("-fx-background-radius: 23px; -fx-background-color: " + getColor(Integer.parseInt(currentBtn.getText())) + ";");
        if (currentBtn.getText().equals("0"))
            currentBtn.setText(" ");
    }
    public String getColor (int num) {
        switch (num) {
            case 1: return "rgb(232, 70, 41)";
            case 2: return "rgb(242, 130, 50)";
            case 3: return "rgb(214, 163, 68)";
            case 4: return "rgb(40, 229, 16)";
            case 5: return "rgb(21, 237, 147)";
            case 6: return "rgb(27, 229, 209)";
            case 7: return "rgb(24, 191, 224)";
            case 8: return "rgb(165, 91, 239)";
            case 9: return "rgb(213, 92, 232)";
            default: return "rgb(186, 184, 184)";
        }
    }
    public void clickBack() {
            Stage primaryStage = (Stage) backButton.getScene().getWindow();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
                AnchorPane pane = loader.load();
                primaryStage.setScene(new Scene(pane));
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
    public void openNavBar() {
        FadeTransition fade = new FadeTransition(Duration.seconds(0.5),navBackground);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
        navBackground.setVisible(true);
        TranslateTransition translate = new TranslateTransition(Duration.seconds(0.6),navBar);
        translate.setToX(-300);
        translate.play();
    }
    public void closeNavBar() {
        FadeTransition fade = new FadeTransition(Duration.seconds(0.5),navBackground);
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.play();
        fade.setOnFinished(e->navBackground.setVisible(false));
        TranslateTransition translate = new TranslateTransition(Duration.seconds(0.6),navBar);
        translate.setToX(300);
        translate.play();
    }
    public boolean boardFull() {
        for (int[] i: gameBoardToArr())
            for (int j = 0; j < 9; j++)
                if (i[j] == 0)
                    return false;
        return true;
    }
    public int[][] gameBoardToArr() {
        int[][] arr = new int[9][9];
        for (Node n: boardPane.getChildren())
            arr[GridPane.getRowIndex(n)][GridPane.getColumnIndex(n)] = Integer.parseInt(((JFXButton)n).getText().matches("[1-9]") ? ((JFXButton)n).getText() : "0");
        return arr;
    }
    public void quit() {
        Platform.exit();
    }
}
