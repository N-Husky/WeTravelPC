package view.controlers;

import MModel.DataBaseAccess;
import MModel.VideoMarker;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.Window;
import net.thegreshams.firebase4j.error.FirebaseException;

import java.io.IOException;

public class UserWindow extends Window {

    public Circle profile_img;
    public Label label1;
    public Label label2;
    public Label label3;
    public Label label4;
    public Label label5;
    public Label user_info;
    private String bio;

    public void initialize(VideoMarker vidM) throws IOException, FirebaseException {

        Image img = new Image(DataBaseAccess.getInstance().getPhotoLink(vidM.getVideoReference().split("/")[0]));
        profile_img.setFill(new ImagePattern(img));
        user_info.setText(DataBaseAccess.getInstance().getUserInfo(vidM.getVideoReference().split("/")[0]).getUserName());
        bio = DataBaseAccess.getInstance().getUserInfo(vidM.getVideoReference().split("/")[0]).getUserInfo();

        //Всьо ясно, автор junior
        if (bio.length() < 37) {
            label3.setText(bio);
            onClose();
            return;
        }
        if (bio.length() < 73) {
            label2.setText(bio.substring(0, 36));
            label3.setText(bio.substring(37));
            onClose();
            return;
        }
        if (bio.length() < 109) {
            label2.setText(bio.substring(0, 36));
            label3.setText(bio.substring(37, 73));
            label3.setText(bio.substring(74));
            onClose();
            return;
        }
        if (bio.length() < 146) {
            label1.setText(bio.substring(0, 36));
            label2.setText(bio.substring(37, 73));
            label3.setText(bio.substring(74, 109));
            label4.setText(bio.substring(110));
            onClose();
            return;
        }
        if (bio.length() < 181) {
            label1.setText(bio.substring(0, 36));
            label2.setText(bio.substring(37, 73));
            label3.setText(bio.substring(74, 109));
            label4.setText(bio.substring(110, 146));
            label4.setText(bio.substring(147));
            onClose();
            return;
        }
    }

    public void onClose() {
        Stage stage = (Stage) this.profile_img.getScene().getWindow();
        stage.close();
    }
}

