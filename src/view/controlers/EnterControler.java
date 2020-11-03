package view.controlers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import net.thegreshams.firebase4j.error.FirebaseException;
import net.thegreshams.firebase4j.error.JacksonUtilityException;
import net.thegreshams.firebase4j.model.FirebaseResponse;
import net.thegreshams.firebase4j.service.Firebase;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class EnterControler {

    public TextField email;
    public TextField password;
    public TextField repeat_password;
    public Label label;
    public AnchorPane pane2;
    public AnchorPane pane1;
    public AnchorPane back1;
    private double xOffset;
    private double yOffset;
    private boolean point = true;             // true - register    false - login
    public String firebase_baseUrl = "https://wetravel-1591a.firebaseio.com/";
    public String firebase_apiKey = "AIzaSyCO06MSKvbYLnPGzBYPKpX8SlcPpiJupA8";


    public void enter_btn() throws JacksonUtilityException, UnsupportedEncodingException, FirebaseException {
        if (point) {
            registration();
        } else {
            logining();
        }
    }

    public void onClose() {
        Stage stage = (Stage) this.email.getScene().getWindow();
        stage.close();
    }

    public void logining_btn()  {
        forLogin();
    }

    public void registration_btn()  {
        forRegister();
    }

    private void forLogin() {
        if(!point)
            return;
        repeat_password.setTranslateX(1000000);
        password.setTranslateY(30);
        email.setTranslateY(30);
        point = false;
        label.setText("Sing-up");
    }

    private void forRegister() {
        if(point)
            return;
        repeat_password.setTranslateX(0);
        password.setTranslateY(0);
        email.setTranslateY(0);
        point = true;
        label.setText("Registration");
    }
    private void logining() throws FirebaseException, UnsupportedEncodingException{
        Firebase firebase = new Firebase( firebase_baseUrl );
        FirebaseResponse response = firebase.get();
        Map<String, Object> dataMap = response.getBody();
        dataMap = (Map)dataMap.get("Users");
        Set<String> codeKeys = dataMap.keySet();
        for(String states : codeKeys){
            Map<String, Object> dataMap2 = (Map)dataMap.get(states);
            for(Map.Entry<String,Object> item : dataMap2.entrySet()){
                System.out.println(item.getValue());
            }
        }
    }
    private void registration()throws FirebaseException, UnsupportedEncodingException, JacksonUtilityException{
        Firebase firebase = new Firebase( firebase_baseUrl );
        FirebaseResponse response = firebase.get();
        Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
        dataMap.put("Email", email.getText());
        dataMap.put("Password", password.getText());
        response = firebase.post("Users",dataMap);
    }

}
