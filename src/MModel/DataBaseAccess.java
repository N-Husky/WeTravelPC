package MModel;

import MModel.exeptions.MailExistException;
import MModel.exeptions.PasswordIncorectException;
import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.common.collect.Lists;
import com.google.firebase.database.core.Path;
import javafx.scene.image.Image;
import net.thegreshams.firebase4j.error.FirebaseException;
import net.thegreshams.firebase4j.error.JacksonUtilityException;
import net.thegreshams.firebase4j.model.FirebaseResponse;
import net.thegreshams.firebase4j.service.Firebase;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class DataBaseAccess {
    public String firebase_baseUrl = "https://wetravel-1591a.firebaseio.com/"; //
    public String firebase_apiKey = "AIzaSyCO06MSKvbYLnPGzBYPKpX8SlcPpiJupA8";
    private static DataBaseAccess instance;
    private User user;

    static {
        instance = new DataBaseAccess();
    }

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

    private DataBaseAccess() {

    }

    public static synchronized DataBaseAccess getInstance() {
        return instance;
    }

    public boolean instantiateUser(boolean auntification, String login, String password) throws FirebaseException, JacksonUtilityException, PasswordIncorectException, UnsupportedEncodingException, MailExistException {
        if (auntification)
            return logining(login, password);
        else return register(login, password);
    }

    private boolean logining(String login, String password) throws FirebaseException, UnsupportedEncodingException, PasswordIncorectException {
        if (!checkLoginPassword(login, password))
            throw new PasswordIncorectException();
        user = new User(login, password, getUserLink(login));
        return true;
        //Zashol
    }

    private boolean register(String login, String password) throws FirebaseException, JacksonUtilityException, MailExistException, UnsupportedEncodingException {
        if (checkMailExistence(login)){
            throw new MailExistException();
        }
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
            if (dataMap2.containsValue(login)) {
                return states;
            }
        }
        return null;
    }

    private boolean checkLoginPassword(String email, String password) throws FirebaseException, UnsupportedEncodingException { //for logining
        //вместе с проверкой пароля в метод checkEverything

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
        dataMap.put("Email", login);
        dataMap.put("Password", password);
        response = firebase.post("users", dataMap);
    }

    private void changePassword(String new_password, String old_password) throws FirebaseException, UnsupportedEncodingException, PasswordIncorectException, JacksonUtilityException {
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

    private void changeUserInfo(String new_info) throws FirebaseException, UnsupportedEncodingException, JacksonUtilityException {
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

    public void uploadVideo(String videoName, String pathToVideo, String coordinates) throws Exception {
        // FILE UPLOAD
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
        String objectName = user.getDataBaseReference() + "/" + videoName;
        // The path to your file to upload
        String filePath = pathToVideo;//
        BlobId blobId = BlobId.of(bucketName, objectName);
        Map<String, String> newMetaData = new HashMap<>();
        newMetaData.put("user_id", user.getDataBaseReference());
        newMetaData.put("position", coordinates);
        Calendar cal = new GregorianCalendar();
        newMetaData.put("uploadingTime", cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR) + " " + cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND));
        //System.out.println(cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR) + " " + cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND));
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setMetadata(newMetaData).build();
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
        String objectName = user.getDataBaseReference() + "/" + videoName;
        // The path to your file to upload
        storage.delete(bucketName, objectName);
//        String filePath = pathToVideo;//
//        BlobId blobId = BlobId.of(bucketName, objectName);
    }

    public void uploadUserPhoto(String pathToPhoto) throws IOException {//Пользователь выберает фото// , после чего фото хагружается на сервер, все фото пользователей
        //имеют одинаковое название - profile_img
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
        if (!checkPhotoExistence())
        {
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
        BlobId blobId = BlobId.of(bucketName,userReference + "/profile_img");
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

    public boolean checkCredentials() {//проверяет есть ли файл с логином паролем у клиента
        return new File(".//credentials.tmp").exists();
    }

    public void createCredentialsTmp() { //создать файл с логином паролем
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (FileOutputStream fos = new FileOutputStream(".//credentials.tmp")) {
            // перевод строки в байты
            byte[] buffer = user.getUserLogin().getBytes();
            fos.write(buffer, 0, buffer.length);
            buffer = "\n".getBytes();
            fos.write(buffer, 0, buffer.length);
            buffer = user.getUserLogin().getBytes();
            fos.write(buffer, 0, buffer.length);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
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

    public ArrayList<VideoMarker> markdersForMap() {
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

    public User getUserInfo (String userReference) throws FirebaseException, IOException {
        Firebase firebase = new Firebase("https://wetravel-1591a.firebaseio.com/");
        FirebaseResponse response = firebase.get();
        Map<String, Object> dataMap = response.getBody();
        dataMap = (Map) dataMap.get("user_data");
        Map<String, Object> dataMap2 = (Map) dataMap.get(userReference);
        return new User(userReference+"/profile_img", dataMap2.get("user_name").toString(), dataMap2.get("user_info").toString(), userReference);
    }
}
