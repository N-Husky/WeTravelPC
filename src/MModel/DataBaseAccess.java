package MModel;

import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.common.collect.Lists;
import net.thegreshams.firebase4j.error.FirebaseException;
import net.thegreshams.firebase4j.error.JacksonUtilityException;
import net.thegreshams.firebase4j.model.FirebaseResponse;
import net.thegreshams.firebase4j.service.Firebase;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class DataBaseAccess {
    public String firebase_baseUrl = "https://wetravel-1591a.firebaseio.com/";
    public String firebase_apiKey = "AIzaSyCO06MSKvbYLnPGzBYPKpX8SlcPpiJupA8";
    private static DataBaseAccess instance;
    private User user;

    static {
        instance = new DataBaseAccess();
    }

    private DataBaseAccess() {

    }

    public static synchronized DataBaseAccess getInstance() {
        return instance;
    }

    public boolean instantiateUser(boolean auntification, String login, String password) throws UnsupportedEncodingException, FirebaseException, JacksonUtilityException {
        if(auntification)
            return register(login,password);
        else return logining(login,password);
    }

    private boolean logining(String login, String password) throws FirebaseException, UnsupportedEncodingException {
        if (!checkLoginPassword(login, password))
            return false;//Ne zashol (!zashol)
        user = new User(login, password, getUserLink(login));
        return true;
        //Zashol
    }

    private boolean register(String login, String password) throws UnsupportedEncodingException, FirebaseException, JacksonUtilityException {
        if (checkMailExistence(login))//if mail exist
            return false;
        registration(login, password);
        return true;
    }

    private String getUserLink(String login) throws FirebaseException, UnsupportedEncodingException {
        Firebase firebase = new Firebase(firebase_baseUrl);
        FirebaseResponse response = firebase.get();
        Map<String, Object> dataMap = response.getBody();
        dataMap = (Map) dataMap.get("users");
        Set<String> codeKeys = dataMap.keySet();
        for (String states : codeKeys) {
            Map<String, Object> dataMap2 = (Map) dataMap.get(states);
            System.out.println(dataMap2.toString());
            if (dataMap2.containsValue(login)) {
                System.out.println("Found successfully");
                return states;
            }
        }
        return null;
    }

    private boolean checkLoginPassword(String email, String password) throws FirebaseException, UnsupportedEncodingException { //for logining
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
            if (dataMap2.containsValue(email)) {
                System.out.println("Found successfully");
                if (dataMap2.containsValue(password)) {
                    System.out.println("Password accepted");
                    return true;
                } else break;
            }
        }
        return false;
        //THROWS EXCEPTION!!!
        //System.out.println("ERROR Password or login is invalid");
    }

    private boolean checkMailExistence(String email) throws FirebaseException, UnsupportedEncodingException {//for registration
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
            if (dataMap2.containsValue(email)) {//THROWS EXCEPTION!!!
                System.out.println("The email \"" + email + "\" already registered");
                return true;
            }
        }
        return false;
    }

    private void registration(String login, String password) throws FirebaseException, UnsupportedEncodingException, JacksonUtilityException {
        Firebase firebase = new Firebase(firebase_baseUrl);
        FirebaseResponse response = firebase.get();
        Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
        dataMap.put("Email", login);
        dataMap.put("Password", password);
        response = firebase.post("users", dataMap);
    }
    public void uploadVideo(String videoName, String pathToVideo, String coordinates) throws Exception {
        // FILE UPLOAD
        if(user == null)
            throw  new NullPointerException("User is not initialized");
        FileInputStream stream = new FileInputStream("./src/resources/wetravel-1591a-1fa332112603.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(stream)
                .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
        stream.close();
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        // The ID of your GCP project
        //String projectId = "wetravel-1591a";
        // The ID of your GCS bucket
        String bucketName = "wetravel-1591a.appspot.com";
        // The ID of your GCS object
        String objectName = user.getDataBaseReference()+"/"+videoName;
        // The path to your file to upload
        String filePath = pathToVideo;//
        BlobId blobId = BlobId.of(bucketName, objectName);
        Map<String, String> newMetaData = new HashMap<>();
        newMetaData.put("position", coordinates);
        Calendar cal = new GregorianCalendar();
        newMetaData.put("uploadingTime", cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR) + " " + cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND));
        //System.out.println(cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR) + " " + cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND));
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setMetadata(newMetaData).build();
        storage.create(blobInfo, Files.readAllBytes(Paths.get(filePath)));
        System.out.println(
                "File " + filePath + " uploaded to bucket " + bucketName + " as " + objectName);
    }
    public ArrayList<VideoMarker> markdersForMap (){
        ArrayList<VideoMarker> markers = new ArrayList<>();
        FileInputStream stream = null;
        try {
            stream = new FileInputStream("./src/resources/wetravel-1591a-1fa332112603.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        GoogleCredentials credentials = null;
        try {
            credentials = GoogleCredentials.fromStream(stream)
                    .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        // The ID of your GCP project
        //String projectId = "wetravel-1591a";
        // The ID of your GCS bucket
        String bucketName = "wetravel-1591a.appspot.com";
        // The ID of your GCS object
        Bucket bucket = storage.get(bucketName);
        //System.out.println(bucket.get("-MM7aIc-jFBHl_Qr33S8/ghhv").getMetadata().get("position"));
        Page<Blob> pblob = bucket.list();
        for (Blob blob : pblob.iterateAll()) {
            if (blob.getName().split("/")[1].equals("profile_img"))
                continue;
            final VideoMarker vidM = new VideoMarker(blob.getName(), blob.getName().split("/")[1], blob.getMetadata().get("position"));
            markers.add(vidM);
        }
        return markers;
    }
    public User getUser() {
        return user;
    }
}
