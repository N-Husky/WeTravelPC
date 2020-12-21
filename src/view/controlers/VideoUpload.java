package view.controlers;

import MModel.DataBaseAccess;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class VideoUpload extends Window {

    public TextField name_area;
    public Label name;
    public JFXButton reload;
    public JFXButton close;
    public JFXButton upload;
    private File file;
    private String cordinates;
    private MyFileChooser myFileChooser = new MyFileChooser();

    public void initialize(Window window, String cordinates) throws FileNotFoundException {
        this.cordinates = cordinates;
        loadWindow();
        if (file == null) return;
        name.setText(file.getName());
        reload.setOnAction(new ButtonHandler());
        close.setOnAction(new ButtonHandler());
        upload.setOnAction(new ButtonHandler());
    }

    public void loadWindow() throws FileNotFoundException {
        file = new File(myFileChooser.forVideoChoose(this, "Video upload"));
    }

    private class ButtonHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            if (event.getSource().equals(reload)) {
                try {
                    loadWindow();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (file == null) return;
                name.setText(file.getName());
            } else if (event.getSource().equals(close)) {
                Stage stage = (Stage) name_area.getScene().getWindow();
                stage.close();
            }else if (event.getSource().equals(upload)){
                if (name_area.getText().length() <= 0) return;
                if (name_area.getText() != null) {
                    try {
                        DataBaseAccess.getInstance().uploadVideo(name_area.getText(), file.getAbsolutePath(), cordinates);
                    } catch (IOException e) {

                    } catch (Exception e) {
                        return;
                    }
                }
                Stage stage = (Stage) name_area.getScene().getWindow();
                stage.close();
            }
        }
    }
}
