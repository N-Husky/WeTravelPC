package view.controlers;

import MModel.DataBaseAccess;
import MModel.User;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.thegreshams.firebase4j.error.FirebaseException;
import net.thegreshams.firebase4j.error.JacksonUtilityException;
import net.thegreshams.firebase4j.model.FirebaseResponse;
import net.thegreshams.firebase4j.service.Firebase;
import view.StartPoint;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EnterControler {

    public TextField email;
    public TextField password;
    public TextField repeat_password;
    public Label label;
    public AnchorPane pane2;
    public AnchorPane pane1;
    public AnchorPane back1;
    public Label eror2;
    public Label eror1;
    public Label eror3;
    private boolean point = true;             // true - register    false - login
    public String firebase_baseUrl = "https://wetravel-1591a.firebaseio.com/";
    public String firebase_apiKey = "AIzaSyCO06MSKvbYLnPGzBYPKpX8SlcPpiJupA8";

    private Pattern p = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{1,})$");
    private Stage stage = new Stage();


    public void enter_btn() throws JacksonUtilityException, IOException, FirebaseException {
        if (point) {
            if (!checkEverything()) return;
            if(DataBaseAccess.getInstance().instantiateUser(true ,email.getText().toString(), password.getText().toString()))
                (new StartPoint()).startMainWindow();
            System.out.println(DataBaseAccess.getInstance().getUser().getUserLogin());
            onClose();
        } else {
            if (!checkEverything()) return;
            if(DataBaseAccess.getInstance().instantiateUser(false ,email.getText().toString(), password.getText().toString()))
                (new StartPoint()).startMainWindow();
            System.out.println(DataBaseAccess.getInstance().getUser().getUserLogin());
            onClose();
        }
    }

    public void onClose() {
        Stage stage = (Stage) this.email.getScene().getWindow();
        stage.close();
    }

    public void logining_btn() throws IOException, InterruptedException {
            //email.setText("zmejka0@gmail.com");
            //password.setText("qqqqqqqq");
            forLogin();
    }

    public void registration_btn() throws IOException {

        forRegister();
    }

    private void forLogin() {

        if (!point)
            return;
        eror1.setVisible(false);
        eror2.setVisible(false);
        eror3.setVisible(false);
        password.setText("");
        email.setText("");
        repeat_password.setText("");
        repeat_password.setTranslateX(1000000);
        password.setTranslateY(30);
        email.setTranslateY(30);
        eror2.setTranslateY(30);
        eror1.setTranslateY(30);
        point = false;
        label.setText("Sing-up");
    }

    private void forRegister() {
        if (point)
            return;
        eror2.setTranslateY(0);
        eror1.setTranslateY(0);
        eror1.setVisible(false);
        eror2.setVisible(false);
        eror3.setVisible(false);
        password.setText("");
        email.setText("");
        repeat_password.setText("");
        repeat_password.setTranslateX(0);
        password.setTranslateY(0);
        email.setTranslateY(0);
        point = true;
        label.setText("Registration");
    }



    private boolean checkEverything() {
        boolean check = true;
        Matcher m = p.matcher(email.getText());

        if (password.getText().length() < 8) {
            eror2.setVisible(true);
            eror2.setText("Пароль занадто короткий");
            check = false;
        }

        if (point) {
            if (password.getText().compareTo(repeat_password.getText()) != 0) {
                eror3.setVisible(true);
                eror2.setVisible(true);
                eror3.setText("Паролі різні");
                eror2.setText("Паролі різні");
                check = false;
            }
        }

        if (password.getText().isEmpty()) {
            eror2.setVisible(true);
            eror2.setText("Введіть пароль");
            check = false;
        }

        if (!m.find()) {
            eror1.setVisible(true);
            eror1.setText("Неправильно введений email");
            check = false;
        }

        if (email.getText().isEmpty()) {
            eror1.setVisible(true);
            eror1.setText("Введіть email");
            check = false;
        }

        return check;
    }


    public void hide() {
        eror1.setVisible(false);
        eror2.setVisible(false);
        eror3.setVisible(false);
    }
}
