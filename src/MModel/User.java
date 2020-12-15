package MModel;

import java.io.File;
import java.io.IOException;

public class User {
    private String userName;

    private String profilePhotoReference;//in google store database
    private String dataBaseReference;
    private String password;
    private String userLogin;

    public String getUserInfo() {
        return userInfo;
    }
    public String getUserName(){
        return userName;
    }
    private String userInfo;

    private String photoPath;//temp link to photo in order not to download it again
    public String getUserLogin() {
        return userLogin;
    }
    public User(String userLogin, String password, String dataBaseReference){
        this.userLogin = userLogin;
        this.password = password;
        this.dataBaseReference = dataBaseReference;
    }
    public User(String profilePhotoReference, String userName, String userInfo, String dataBaseReference){
        this.profilePhotoReference = profilePhotoReference;
        this.userName = userName;
        this.userInfo = userInfo;
        this.dataBaseReference = dataBaseReference;
    }

    public String getDataBaseReference(){
        return dataBaseReference;
    }
    public String getProfilePhotoReference(){
        return profilePhotoReference;
    }
    public void setProfilePhotoReference(String profilePhotoReference) {
        this.profilePhotoReference = profilePhotoReference;
    }
    public void setPassword(String new_password) {
        this.password = new_password;
    }
    public void setUserInfo(String new_userInfo) {
        this.userInfo = new_userInfo;
    }
    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
    public boolean loadPhoto() throws IOException { //возвращает false если нет фото в базе даных и в локальком хранилище
        //Если нет файла в локальном хранилище то он будет загружен, если нет и в базе данных нет, то false
        if(!checkLocalTempPhoto()){
            if(!DataBaseAccess.getInstance().checkPhotoExistence())
                return false;
            DataBaseAccess.getInstance().loadPhoto();
            return true;
        }
        return true;
    }
    public boolean checkLocalTempPhoto(){
        return new File(".//profile_photo").exists();
    }//проверяет наличие локальеной копии фото
}
