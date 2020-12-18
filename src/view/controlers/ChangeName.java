package view.controlers;

import MModel.DataBaseAccess;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.thegreshams.firebase4j.error.FirebaseException;
import net.thegreshams.firebase4j.error.JacksonUtilityException;

import java.io.IOException;

public class ChangeName {
    public TextField name;

    public void onYes() throws JacksonUtilityException, IOException, FirebaseException {

        DataBaseAccess.getInstance().changeUserName(name.getText().toString());
        onNo();
    }

    public void onNo() {
        Stage stage = (Stage) this.name.getScene().getWindow();
        stage.close();
    }

    public void initialize(String string) {
        name.setText(string);
    }
}
