package view.controlers;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EnterControler {

    public TextField email;
    public TextField password;
    public TextField repeat_password;
    private boolean point = true;             // true - register    false - login

    public void enter_btn() {
        if (point) {

        } else {

        }
    }

    public void onClose() {
        Stage stage = (Stage) this.email.getScene().getWindow();
        stage.close();
    }

    public void logining_btn() {
        forLogin();
    }

    public void registration_btn() {
        forRegister();
    }

    private void forLogin() {
        repeat_password.setTranslateX(1000000);
        password.setTranslateY(40);
        email.setTranslateY(40);
        point = false;
    }

    private void forRegister() {
        repeat_password.setTranslateX(0);
        password.setTranslateY(0);
        email.setTranslateY(0);
        point = true;
    }
}
