package view.controlers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.function.Consumer;

public class AcceptWindow extends Window {
    @FXML
    public Label string;
    public Consumer consumer;
    public void onOk() {
        consumer.accept(string);
        onNot();
    }
    public void onNot() {
        Stage stage = (Stage) this.string.getScene().getWindow();
        stage.close();
    }
    public void initialize(Window window, String str, Consumer consumer){
        this.string.setText(str);
        this.consumer = consumer;
    }
}
