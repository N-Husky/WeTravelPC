package view.controlers;

import MModel.DataBaseAccess;
import com.jfoenix.controls.JFXTextArea;
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
    private File file;
    private String cordinates;
    private MyFileChooser myFileChooser = new MyFileChooser();

    public void initialize(Window window, String cordinates) throws FileNotFoundException {
        this.cordinates = cordinates;
        loadWindow();
        if (file == null) return;
        name.setText(file.getName());
    }

    public void loadWindow() throws FileNotFoundException {
        file = new File(myFileChooser.forVideoChoose(this, "Video upload"));
    }

    public void onUpload() {
        if(name_area.getText().length()<=0)return;
        if (name_area.getText() != null) {
            try {
                DataBaseAccess.getInstance().uploadVideo(name_area.getText(), file.getAbsolutePath(), cordinates);
            } catch (IOException e ) {

            } catch (Exception e) {
                return;
            }
        }
        onClose();
    }


    public void onReload() throws FileNotFoundException {
        loadWindow();

        if (file == null) return;
        name.setText(file.getName());
    }

    public void onClose() {
        Stage stage = (Stage) this.name_area.getScene().getWindow();
        stage.close();
    }
}
