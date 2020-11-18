package view.controlers;

import com.google.api.core.ApiFuture;
import com.google.api.gax.paging.Page;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.storage.*;
import com.google.common.collect.Lists;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.cloud.StorageClient;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.thegreshams.firebase4j.error.FirebaseException;
import net.thegreshams.firebase4j.error.JacksonUtilityException;
import net.thegreshams.firebase4j.model.FirebaseResponse;
import net.thegreshams.firebase4j.service.Firebase;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
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
            //if (!checkEverything()) return;
            checkMailExistence(email.getText());//Тоже будет бросать exception если такой мейл есть в базе
            //registration();
        } else {
            //if (!checkEverything()) return;
            checkLoginPassword(email.getText(),password.getText());
            //logining();
        }
    }

    private void checkLoginPassword(String email, String password) throws FirebaseException, UnsupportedEncodingException {
        //вместе с проверкой пароля в метод checkEverything
        System.out.println(email);
        Firebase firebase = new Firebase(firebase_baseUrl);
        FirebaseResponse response = firebase.get();
        Map<String, Object> dataMap = response.getBody();
        dataMap = (Map) dataMap.get("users");
        Set<String> codeKeys = dataMap.keySet();
        for (String states : codeKeys) {
            Map<String, Object> dataMap2 = (Map) dataMap.get(states);
            System.out.println(dataMap2.toString());
            if(dataMap2.containsValue(email)){
                System.out.println("Found successfully");
                if(dataMap2.containsValue(password)){
                    System.out.println("Password accepted");
                    return;
                }
                else break;
            }
        }
        //THROWS EXCEPTION!!!
        System.out.println("ERROR Password or login is invalid");
    }

    private void checkMailExistence(String email) throws FirebaseException, UnsupportedEncodingException {//Думаю лучше будет инкапсулировать этот метод
        //вместе с проверкой пароля в метод checkEverything
        System.out.println(email);
        Firebase firebase = new Firebase(firebase_baseUrl);
        FirebaseResponse response = firebase.get();
        Map<String, Object> dataMap = response.getBody();
        dataMap = (Map) dataMap.get("users");
        Set<String> codeKeys = dataMap.keySet();
        for (String states : codeKeys) {
            Map<String, Object> dataMap2 = (Map) dataMap.get(states);
            System.out.println(dataMap2.toString());
            if(dataMap2.containsValue(email)){//THROWS EXCEPTION!!!
                System.out.println("The email \"" + email + "\" already registered");
                return;
            }

        }
    }

    public void onClose() {
        Stage stage = (Stage) this.email.getScene().getWindow();
        stage.close();
    }

    public void logining_btn() throws IOException, ExecutionException, InterruptedException {


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
