package view.controlers;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EnterControler {

    public TextField email;
    public TextField password;
    public TextField repeat_password;


    public void enter_btn() {

    }

    public void onClose() {
        Stage stage = (Stage) this.email.getScene().getWindow();
        stage.close();
    }

    public void logining_btn() {

    }

    public void registration_btn() {

    }
}
