package view.controlers;

import MModel.DataBaseAccess;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import net.thegreshams.firebase4j.error.FirebaseException;

import view.StartPoint;

import java.io.IOException;
import java.util.function.Consumer;

public class AccountSettings {

    public Circle profile_img;
    public Label user_info;
    public JFXTextArea bio;
    int onNameClick = 0;
    int onBioClick = 0;

    public void initialize() throws IOException, FirebaseException {
       profile_img.setFill(new ImagePattern(new Image(DataBaseAccess.getInstance().getUser().getProfilePhotoReference())));
        user_info.setText(DataBaseAccess.getInstance().getUser().getUserName());
        String bio =  DataBaseAccess.getInstance().getUser().getUserInfo();
        this.bio.setText(bio);
        this.bio.setEditable(false);
    }


    public void onClose() {
        Stage stage = (Stage) this.profile_img.getScene().getWindow();
        stage.close();
    }
    public void onBioClick() throws IOException, FirebaseException {
        onBioClick++;
        if(onBioClick%2==0){
            (new StartPoint()).biochange();
            onClose();
        }
    }

    public void onUserClick() throws IOException, FirebaseException {
        onNameClick++;
        if(onNameClick%2==0){
            (new StartPoint()).namechange(user_info.getText());
            onClose();
        }
    }
}
