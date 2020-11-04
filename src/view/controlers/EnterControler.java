package view.controlers;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.thegreshams.firebase4j.error.FirebaseException;
import net.thegreshams.firebase4j.error.JacksonUtilityException;
import net.thegreshams.firebase4j.model.FirebaseResponse;
import net.thegreshams.firebase4j.service.Firebase;

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
    private double xOffset;
    private double yOffset;
    private boolean point = true;             // true - register    false - login
    public String firebase_baseUrl = "https://wetravel-1591a.firebaseio.com/";
    public String firebase_apiKey = "AIzaSyCO06MSKvbYLnPGzBYPKpX8SlcPpiJupA8";

    private Pattern p = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{1,})$");


    public void enter_btn() throws JacksonUtilityException, UnsupportedEncodingException, FirebaseException {
        if (point) {
            if (!checkEverything()) return;
            registration();
        } else {
            if (!checkEverything()) return;
            logining();
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
        if (!point)
            return;
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
        repeat_password.setTranslateX(0);
        password.setTranslateY(0);
        email.setTranslateY(0);
        point = true;
        label.setText("Registration");
    }

    private void logining() throws FirebaseException, UnsupportedEncodingException {
        Firebase firebase = new Firebase(firebase_baseUrl);
        FirebaseResponse response = firebase.get();
        Map<String, Object> dataMap = response.getBody();
        dataMap = (Map) dataMap.get("users");
        Set<String> codeKeys = dataMap.keySet();
        for (String states : codeKeys) {
            Map<String, Object> dataMap2 = (Map) dataMap.get(states);
            for (Map.Entry<String, Object> item : dataMap2.entrySet()) {
                System.out.println(item.getValue());
            }
        }
    }

    private void registration() throws FirebaseException, UnsupportedEncodingException, JacksonUtilityException {
        Firebase firebase = new Firebase(firebase_baseUrl);
        FirebaseResponse response = firebase.get();
        Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
        dataMap.put("Email", email.getText());
        dataMap.put("Password", password.getText());
        response = firebase.post("users", dataMap);
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
            eror1.setVisible(true);
            eror1.setText("Введіть пароль");
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
