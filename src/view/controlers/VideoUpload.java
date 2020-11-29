package view.controlers;

import com.sun.jndi.toolkit.url.Uri;
import com.sun.org.apache.xml.internal.utils.URI;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class VideoUpload {

    public MediaView video;
    public TextField name_area;
    public TextArea additional;
    private File file;
    private String cordinates;

    public void initialize(Window window, String cordinates){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Виберіть необхідний файл");
        file = fileChooser.showOpenDialog(window);
        this.cordinates = cordinates;
    }

    public void onUpload() {
        if (name_area.getText()!=null) {
            try {
                (new MainWindow()).uploadVideo("-MN5NE8UAFRPTR52M6JJ/" + name_area.getText(), file.getAbsolutePath(), cordinates);
            } catch (IOException e) {
            }
        }
    }
}
