package MModel;

public class OtherUserInfo {
    private String userName;
    private String userInfo;
    private String dataBaseReference;
    public OtherUserInfo(String userName, String userInfo, String dataBaseReference) {
        this.userName = userName;
        this.userInfo = userInfo;
        this.dataBaseReference = dataBaseReference;
    }
    //GETTERS
    public String getUserName() {
        return userName;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public String getDataBaseReference() {
        return dataBaseReference;
    }
    //END OF GETTERS
    //SETTERS
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public void setDataBaseReference(String dataBaseReference) {
        this.dataBaseReference = dataBaseReference;
    }
    //END OF SETTERS
}
