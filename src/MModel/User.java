package MModel;

import net.thegreshams.firebase4j.error.FirebaseException;

import java.io.File;
import java.io.IOException;

public class User {
//    private String profilePhotoReference;//in google store database
    private String userLogin;
    private String password;
    private String userName;
    private String userInfo;
    private String dataBaseReference;

//    public String getUserInfo() throws IOException, FirebaseException {
//        return  DataBaseAccess.getInstance().getUserInfo(dataBaseReference).userInfo;
//    }
//    public String getUserName() throws IOException, FirebaseException {
//        return DataBaseAccess.getInstance().getUserInfo(dataBaseReference).userName;
//    }


    public User(String userLogin, String password,  String userName, String userInfo, String dataBaseReference) {
        this.userLogin = userLogin;
        this.password = password;
        this.userName = userName;
        this.userInfo = userInfo;
        this.dataBaseReference = dataBaseReference;
    }
//    public User(String userLogin, String password, String dataBaseReference){
//        this.userLogin = userLogin;
//        this.password = password;
//        this.dataBaseReference = dataBaseReference;
//    }
//    public User(String profilePhotoReference, String userName, String userInfo, String dataBaseReference){
//        this.userName = userName;
//        this.userInfo = userInfo;
//        this.dataBaseReference = dataBaseReference;
//    }
    //GETTERS
    public String getUserLogin() {
        return userLogin;
    }
    public String getPassword() { return password; }
    public String getDataBaseReference(){
        return dataBaseReference;
    }
    public String getProfilePhotoReference() throws IOException {//in order to observe photo
        return DataBaseAccess.getInstance().getPhotoLink(dataBaseReference);
    }
    public String getUserName() { return userName; }
    public String getUserInfo() { return userInfo; }
    //END OF GETTERS
    //SETTERS
    public void setPassword(String new_password) {
        this.password = new_password;
    }
    public void setUserInfo(String new_userInfo) {
        this.userInfo = new_userInfo;
    }
    //END OF SETTERS
    //photo methods (to delete later)
//    public boolean loadPhoto() throws IOException { //возвращает false если нет фото в базе даных и в локальком хранилище
//        //Если нет файла в локальном хранилище то он будет загружен, если нет и в базе данных нет, то false
//        if(!checkLocalTempPhoto()){
//            if(!DataBaseAccess.getInstance().checkPhotoExistence())
//                return false;
//            DataBaseAccess.getInstance().loadPhoto();
//            return true;
//        }
//        return true;
//    }
//    public boolean checkLocalTempPhoto(){
//        return new File(".//profile_photo").exists();
//    }//проверяет наличие локальеной копии фото
    //END OF PHOTO
}
