package MModel;

import MModel.exeptions.MailExistException;
import MModel.exeptions.PasswordIncorectException;
import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.common.collect.Lists;
import com.google.common.net.MediaType;
import com.google.firebase.database.core.Path;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import net.thegreshams.firebase4j.error.FirebaseException;
import net.thegreshams.firebase4j.error.JacksonUtilityException;
import net.thegreshams.firebase4j.model.FirebaseResponse;
import net.thegreshams.firebase4j.service.Firebase;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.xml.crypto.Data;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class DataBaseAccess {
    public String firebase_baseUrl = "https://wetravel-1591a.firebaseio.com/"; //
    public String firebase_apiKey = "AIzaSyCO06MSKvbYLnPGzBYPKpX8SlcPpiJupA8";
    private User user;

    private static DataBaseAccess instance;
    private DataBaseAccess() {}
    static {instance = new DataBaseAccess();}
    public static synchronized DataBaseAccess getInstance() {return instance;}

    public String getVideoPlayerLink(VideoMarker vm) throws IOException {//retrieve link for media player from videoMarker
        FileInputStream stream = new FileInputStream("./src/resources/wetravel-1591a-1fa332112603.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(stream)
                .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
        stream.close();
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        String bucketName = "wetravel-1591a.appspot.com";

        BlobId blobId = BlobId.of(bucketName, vm.getVideoReference());
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        return storage.signUrl(blobInfo, 1, TimeUnit.MINUTES, Storage.SignUrlOption.withV4Signature()).toString();
    }

    public boolean instantiateUser(boolean auntification, String login, String password) throws FirebaseException, JacksonUtilityException, PasswordIncorectException, IOException, MailExistException {
        if (auntification)
            return logining(login, password);
        else return register(login, password);
    }

    private boolean logining(String login, String password) throws FirebaseException, IOException, PasswordIncorectException {
        if (!checkLoginPassword(login, password))
            throw new PasswordIncorectException();
        user = new User(login, password, getUserLink(login));
        User tempUser = this.getUserInfo(user.getDataBaseReference());
        user = new User(tempUser.getUserName(), tempUser.getProfilePhotoReference(), tempUser.getDataBaseReference(),
                password, login, tempUser.getUserInfo());
        return true;
    }

    private boolean register(String login, String password) throws FirebaseException, JacksonUtilityException,
            MailExistException, UnsupportedEncodingException {
        if (checkMailExistence(login)) {
            throw new MailExistException();
        }
        registration(login, password);
        user = new User(login, password, getUserLink(login));
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
            if (dataMap2.containsValue(login)) {
                return states;
            }
        }
        return null;
    }

    private boolean checkLoginPassword(String email, String password) throws FirebaseException, UnsupportedEncodingException { //for logining
        Firebase firebase = new Firebase(firebase_baseUrl);
        FirebaseResponse response = firebase.get();
        Map<String, Object> dataMap = response.getBody();
        dataMap = (Map) dataMap.get("users");
        Set<String> codeKeys = dataMap.keySet();
        for (String states : codeKeys) {
            Map<String, Object> dataMap2 = (Map) dataMap.get(states);
            if (dataMap2.containsValue(email)) {
                if (dataMap2.containsValue(password)) {
                    return true;
                } else break;
            }
        }
        return false;
        //THROWS EXCEPTION!!!
        //System.out.println("ERROR Password or login is invalid");
    }

    private boolean checkMailExistence(String email) throws FirebaseException, UnsupportedEncodingException {//for registration
        Firebase firebase = new Firebase(firebase_baseUrl);
        FirebaseResponse response = firebase.get();
        Map<String, Object> dataMap = response.getBody();
        dataMap = (Map) dataMap.get("users");
        Set<String> codeKeys = dataMap.keySet();
        for (String states : codeKeys) {
            Map<String, Object> dataMap2 = (Map) dataMap.get(states);

            if (dataMap2.containsValue(email)) {//THROWS EXCEPTION!!!
                return true;
            }
        }
        return false;
    }

    private void registration(String login, String password) throws FirebaseException, UnsupportedEncodingException, JacksonUtilityException {
        Firebase firebase = new Firebase(firebase_baseUrl);
        FirebaseResponse response = firebase.get();
        Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
        dataMap.put("email", login);
        dataMap.put("password", password);
        response = firebase.post("users", dataMap);
        dataMap.clear();
        response = firebase.get();
        dataMap = response.getBody();
        dataMap = (Map) dataMap.get("users");
        Set<String> codeKeys = dataMap.keySet();
        Map<String, Object> dataMap2 = null;
        String key = null;
        for (String states : codeKeys) {
            dataMap2 = (Map) dataMap.get(states);
            if (dataMap2.containsValue(login)) {
                key = states;
                break;
            }
        }
        dataMap2.clear();
        dataMap2.put("user_info", "Hello world");
        dataMap2.put("user_name", login.split("@")[0]);
        response = firebase.put("user_data/" + key, dataMap2);
    }

    public void changePassword(String new_password, String old_password) throws FirebaseException, UnsupportedEncodingException, PasswordIncorectException, JacksonUtilityException {
        Firebase firebase = new Firebase(firebase_baseUrl);
        FirebaseResponse response = firebase.get();
        Map<String, Object> dataMap = response.getBody();
        dataMap = (Map) dataMap.get("users");
        Set<String> codeKeys = dataMap.keySet();
        Map<String, Object> dataMap2 = null;
        String key = null;
        for (String states : codeKeys) {
            dataMap2 = (Map) dataMap.get(states);
            if (dataMap2.containsValue(user.getUserLogin())) {
                if (!dataMap2.containsValue(old_password))
                    throw new PasswordIncorectException();
                key = states;
                break;
            }
        }
        dataMap2.put("Password", new_password);
        response = firebase.put("users/" + key, dataMap2);
        user.setPassword(new_password);
    }

    public void changeUserInfo(String new_info) throws FirebaseException, IOException, JacksonUtilityException {
        Firebase firebase = new Firebase(firebase_baseUrl);
        FirebaseResponse response = firebase.get();
        Map<String, Object> dataMap = response.getBody();
        dataMap = (Map) dataMap.get("user_data");
        Set<String> codeKeys = dataMap.keySet();
        Map<String, Object> dataMap2 = null;
        String key = null;
        for (String states : codeKeys) {
            dataMap2 = (Map) dataMap.get(states);
            if (dataMap2.containsValue(user.getUserName())) {
                key = states;
                break;
            }
        }
        dataMap2.put("user_info", new_info);
        response = firebase.put("user_data/" + key, dataMap2);
        user.setUserInfo(new_info);
    }

    public VideoMarker getVideoMarkerByName(ArrayList<VideoMarker> vmL, String videoName) {
        for (VideoMarker vm : vmL) {
            if (vm.getVideoName().equals(videoName))
                return vm;
        }
        return null;
    }

    public ArrayList<VideoMarker> getUserVideos() throws IOException {
        //instantiateUser(true,"maksymenko1111@gmail.com","maksymenko1111@gmail.com");
        ArrayList<VideoMarker> lVm = new ArrayList<VideoMarker>();
        FileInputStream stream = new FileInputStream("./src/resources/wetravel-1591a-1fa332112603.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(stream)
                .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
        stream.close();
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        String bucketName = "wetravel-1591a.appspot.com";
        Bucket bucket = storage.get(bucketName);
        Page<Blob> pblob = bucket.list(Storage.BlobListOption.prefix(user.getDataBaseReference()));
        for (Blob blob : pblob.iterateAll()) {
            if (blob.getName().equals(user.getDataBaseReference() + "/profile_img"))
                continue;
            else {
                lVm.add(new VideoMarker(blob.getName(), blob.getName().split("/")[1], blob.getMetadata().get("position")));
            }
        }
        return lVm;
    }

    public ObservableList<String> getUserVideoNames(ArrayList<VideoMarker> lvm) {
        ObservableList<String> temp = FXCollections.observableArrayList();
        for (VideoMarker vm : lvm) {
            temp.add(vm.getVideoName());
        }
        return temp;
    }

    public void changeUserName(String new_info) throws FirebaseException, IOException, JacksonUtilityException {
        Firebase firebase = new Firebase(firebase_baseUrl);
        FirebaseResponse response = firebase.get();
        Map<String, Object> dataMap = response.getBody();
        dataMap = (Map) dataMap.get("user_data");
        Set<String> codeKeys = dataMap.keySet();
        Map<String, Object> dataMap2 = null;
        String key = null;
        for (String states : codeKeys) {
            dataMap2 = (Map) dataMap.get(states);
            if (dataMap2.containsValue(user.getUserName())) {
                key = states;
                break;
            }
        }
        dataMap2.put("user_name", new_info);
        response = firebase.put("user_data/" + key, dataMap2);
        user.setUserInfo(new_info);
    }

    public void uploadVideo(String videoName, String pathToVideo, String coordinates) throws Exception {
        if (user == null) throw new NullPointerException("User is not initialized");
        FileInputStream stream = new FileInputStream("./src/resources/wetravel-1591a-1fa332112603.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(stream).createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
        stream.close();
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        String bucketName = "wetravel-1591a.appspot.com";
        String objectName = user.getDataBaseReference() + "/" + videoName;
        String filePath = pathToVideo;
        BlobId blobId = BlobId.of(bucketName, objectName);
        Map<String, String> newMetaData = new HashMap<>();
        newMetaData.put("user_id", user.getDataBaseReference());
        newMetaData.put("position", coordinates);
        Calendar cal = new GregorianCalendar();
        newMetaData.put("uploadingTime", cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR) + " " + cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND));
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setMetadata(newMetaData).setContentType(String.valueOf(MediaType.MP4_VIDEO)).build();
        storage.create(blobInfo, Files.readAllBytes(Paths.get(filePath)));
    }

    public void deleteVideo(String videoName) throws IOException {
        if (user == null)
            throw new NullPointerException("User is not initialized");
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
        //String objectName = user.getDataBaseReference() + "/" + videoName;
        // The path to your file to upload
        storage.delete(bucketName, videoName);
      //  String filePath = pathToVideo;
      //  BlobId blobId = BlobId.of(bucketName, objectName);
    }

    public void uploadUserPhoto(String pathToPhoto) throws IOException {//Пользователь выберает фото// , после чего фото хагружается на сервер, все фото пользователей

        if (user == null)
            throw new NullPointerException("User is not initialized");
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
        String objectName = user.getDataBaseReference() + "/" + "profile_img";
        // The path to your file to upload
        String filePath = pathToPhoto;
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        storage.create(blobInfo, Files.readAllBytes(Paths.get(filePath)));
        user.setProfilePhotoReference(objectName);
        user.setPhotoPath(pathToPhoto);
    }

    public boolean checkPhotoExistence() throws IOException {//проверяет есть ли фото пользователя на сервере
        if (user == null)
            throw new NullPointerException("User is not initialized");
        FileInputStream stream = new FileInputStream("./src/resources/wetravel-1591a-1fa332112603.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(stream)
                .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
        stream.close();
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        String bucketName = "wetravel-1591a.appspot.com";
        Bucket bucket = storage.get(bucketName);
        Page<Blob> pblob = bucket.list(Storage.BlobListOption.prefix(user.getDataBaseReference()));
        for (Blob blob : pblob.iterateAll()) {
            if (blob.getName().equals(user.getDataBaseReference() + "/profile_img"))
                return true;
        }
        return false;
    }

    public String getPhotoLink(String userReference) throws IOException {
        if (!checkPhotoExistence()) {
            System.out.println("1");
            return "view/css/default_profile_img.jpg";
        }

        FileInputStream stream = new FileInputStream("./src/resources/wetravel-1591a-1fa332112603.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(stream)
                .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
        stream.close();
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        String bucketName = "wetravel-1591a.appspot.com";
        Bucket bucket = storage.get(bucketName);
        Page<Blob> blobs = bucket.list();
        BlobId blobId = BlobId.of(bucketName, userReference + "/profile_img");
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

        return storage.signUrl(blobInfo, 1, TimeUnit.MINUTES, Storage.SignUrlOption.withV4Signature()).toString();
    }

    public void loadPhoto() throws IOException {//загружает фотографию с сервера на клент, что бы не приходилось каждый раз загружать с сервера
        FileInputStream stream = new FileInputStream("./src/resources/wetravel-1591a-1fa332112603.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(stream)
                .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
        stream.close();
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        String bucketName = "wetravel-1591a.appspot.com";
        Bucket bucket = storage.get(bucketName);
        Blob blob = storage.get(BlobId.of(bucketName, "profile_img"));
        blob.downloadTo(Paths.get(".//"));
    }

    public boolean checkCredentials() {
        return new File(".//credentials.tmp").exists();
    }
    public void createCredentialsTmp() { //создать файл с логином паролем
        try (FileOutputStream fos = new FileOutputStream(".//credentials.tmp")) {
            // перевод строки в байты
            byte[] buffer = user.getUserLogin().getBytes();
            fos.write(buffer, 0, buffer.length);
            buffer = "/".getBytes();
            fos.write(buffer, 0, buffer.length);
            buffer = user.getPassword().getBytes();
            fos.write(buffer, 0, buffer.length);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    public void deleteCredentials() {
        new File(".//credentials.tmp").delete();
    }
    public String getCredentialsFromFile() { //возвращает логин и пароль в строке типа: "login/password"
        String credentials;
        try (FileInputStream fin = new FileInputStream(".//credentials.tmp")) {
            byte[] buffer = new byte[fin.available()];
            fin.read(buffer, 0, buffer.length);
            credentials = new String(buffer);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            credentials = "";
        }
        return credentials;
    }

    public ObservableList<VideoMarker> markdersForMap() {
        ObservableList<VideoMarker> markers = FXCollections.observableArrayList();

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
        String bucketName = "wetravel-1591a.appspot.com";
        Bucket bucket = storage.get(bucketName);
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

    public User getUserInfo(String userReference) throws FirebaseException, IOException {
        Firebase firebase = new Firebase("https://wetravel-1591a.firebaseio.com/");
        FirebaseResponse response = firebase.get();
        Map<String, Object> dataMap = response.getBody();
        dataMap = (Map) dataMap.get("user_data");
        Map<String, Object> dataMap2 = (Map) dataMap.get(userReference);
        return new User(userReference + "/profile_img", dataMap2.get("user_name").toString(), dataMap2.get("user_info").toString(), userReference);
    }

    public String getCountryCoordinates(String countryName) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONParser parserCity = new JSONParser();
        Object obj = parser.parse(new FileReader("./src/resources/countries.json"));
        Object objCities = parser.parse(new FileReader("./src/resources/cities.json"));
        JSONArray jsonArr = (JSONArray) obj;
        for (int i = 0; i < jsonArr.size(); i++) {
            if (((JSONObject) jsonArr.get(i)).get("name").equals(countryName)) {
                String latlng = ((JSONObject) jsonArr.get(i)).get("latlng").toString();
                latlng = latlng.substring(1, latlng.length() - 1);
                return new StringBuilder(latlng.split(",")[0] + "/" + latlng.split(",")[1]).toString();
            }}
        jsonArr = (JSONArray) objCities;
        for (int i = 0; i < jsonArr.size(); i++) {
            if (((JSONObject) jsonArr.get(i)).get("name").equals(countryName)) {
                String lat = ((JSONObject) jsonArr.get(i)).get("lat").toString();
                String lng = ((JSONObject) jsonArr.get(i)).get("lng").toString();
                return new StringBuilder(lat + "/" + lng).toString();}}
        return null;
    }

}
