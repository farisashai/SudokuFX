package FX.FXSudoku;

import com.jfoenix.controls.JFXButton;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class StartController implements Initializable{
    @FXML private JFXButton loginButton;
    public void initialize(URL location, ResourceBundle resources) {
        TranslateTransition slideIn = new TranslateTransition(Duration.seconds(1.2),loginButton);
        slideIn.setFromX(-800);
        slideIn.setToX(0);
        slideIn.play();

    }
    public void clickPlay() {
        SudokuBoard.getBoard(Difficulty.EASY);
        Stage primaryStage = (Stage)loginButton.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GameScreen.fxml"));
            GameController control = new GameController(Difficulty.EASY);
            loader.setController(control);
            AnchorPane pane = loader.load();
            Scene scene = new Scene(pane);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
