package MModel;

public class User {
    private String userName;
    private String profilePhotoReference;
    private String dataBaseReference;
    private String password;
    private String userLogin;
    private String userInfo;

    public String getUserLogin() {
        return userLogin;
    }
    public User(String userLogin, String password, String dataBaseReference){
        this.userLogin = userLogin;
        this.password = password;
        this.dataBaseReference = dataBaseReference;
    }
    public String getUserName(){
        return userName;
    }
    public  String getDataBaseReference(){
        return dataBaseReference;
    }
    public String getProfilePhotoReference(){
        return profilePhotoReference;
    }
}
