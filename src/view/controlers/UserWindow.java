package view.controlers;

import MModel.DataBaseAccess;
import MModel.OtherUserInfo;
import MModel.User;
import MModel.VideoMarker;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.Window;
import net.thegreshams.firebase4j.error.FirebaseException;

import java.io.IOException;

public class UserWindow extends Window {

    public Circle profile_img;
    @FXML
    TextArea info_user;
    public Label user_info;
    private String bio;

    public void initialize(VideoMarker vidM) throws IOException, FirebaseException {

        Image img = new Image(DataBaseAccess.getInstance().getPhotoLink(vidM.getVideoReference().split("/")[0]));
        profile_img.setFill(new ImagePattern(img));
        User otherU = DataBaseAccess.getInstance().getUserInfo(vidM.getVideoReference().split("/")[0]);
        user_info.setText(DataBaseAccess.getInstance().getUserInfo(vidM.getVideoReference().split("/")[0]).getUserName());
        bio = DataBaseAccess.getInstance().getUserInfo(vidM.getVideoReference().split("/")[0]).getUserInfo();
        user_info.setText(otherU.getUserName());
        bio = otherU.getUserInfo();
        info_user.setText(bio);
        info_user.setEditable(false);
    }

    public void onClose() {
        Stage stage = (Stage) this.profile_img.getScene().getWindow();
        stage.close();
    }
}

