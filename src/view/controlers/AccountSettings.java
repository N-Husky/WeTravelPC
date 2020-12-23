package view.controlers;

import MModel.DataBaseAccess;
import com.jfoenix.controls.JFXTextArea;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import net.thegreshams.firebase4j.error.FirebaseException;

import java.io.IOException;

public class AccountSettings {
    public Circle profile_img;
    public Label user_info;
    public JFXTextArea bio;
    private int onNameClick = 0;
    private int onBioClick = 0;
    public Model modelControler= new Model();
    public void initialize() throws IOException, FirebaseException {
        profile_img.setFill(new ImagePattern(new Image(DataBaseAccess.getInstance()
                .getUser().getProfilePhotoReference())));
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
        modelControler.onBioClick(profile_img);
    }
    public void onUserClick() throws IOException, FirebaseException {
        modelControler.onLoginClick(profile_img,user_info.getText());
    }
    public void setModelControler(Model modelControler) {
        this.modelControler = modelControler;
        modelControler.addListener(m->System.out.println("huy"));
    }
}
