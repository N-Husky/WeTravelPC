package view.controlers;

import MModel.DataBaseAccess;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import net.thegreshams.firebase4j.error.FirebaseException;
import net.thegreshams.firebase4j.error.JacksonUtilityException;

import java.io.IOException;

public class ChangeBio {
    public TextArea bio;

    public void initialize() throws IOException, FirebaseException {
        bio.setText(DataBaseAccess.getInstance().getUser().getUserInfo());
    }


    public void onYes() throws JacksonUtilityException, IOException, FirebaseException {
        DataBaseAccess.getInstance().changeUserInfo(bio.getText().toString());
        onNo();
    }

    public void onNo() {
        Stage stage = (Stage) this.bio.getScene().getWindow();
        stage.close();
    }
}
