package view.controlers;

import net.thegreshams.firebase4j.error.FirebaseException;
import net.thegreshams.firebase4j.model.FirebaseResponse;
import net.thegreshams.firebase4j.service.Firebase;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Set;

public class TestLogin {
    public static void main(String[] args) throws FirebaseException, UnsupportedEncodingException {
         String firebase_baseUrl = "https://wetravel-1591a.firebaseio.com/";
         String firebase_apiKey = "AIzaSyCO06MSKvbYLnPGzBYPKpX8SlcPpiJupA8";
        Firebase firebase = new Firebase(firebase_baseUrl);
        FirebaseResponse response = firebase.get();
        Map<String, Object> dataMap = response.getBody();
        dataMap = (Map) dataMap.get("users");
        System.out.println(dataMap.toString());
        Set<String> codeKeys = dataMap.keySet();
        for (String states : codeKeys) {
            Map<String, Object> dataMap2 = (Map) dataMap.get(states);
            System.out.println(dataMap2.toString() + " " + states);
            if (dataMap2.containsValue("depresion@cry.hell")) {
                System.out.println("Found successfully");
                if (dataMap2.containsValue("amicrying")) {
                    System.out.println("Password accepted");
                } else break;
            }
        }
    }
}
