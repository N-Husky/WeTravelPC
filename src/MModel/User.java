package MModel;

public class User {
    private String userName;
    private String profilePhotoReference;
    private String dataBaseReference;
    public User(String userName, String dataBaseReference, String profilePhotoReference){
        this.userName = userName;
        this.dataBaseReference = dataBaseReference;
        this.profilePhotoReference = profilePhotoReference;
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
