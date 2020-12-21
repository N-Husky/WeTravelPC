package view.fxml;

import MModel.DataBaseAccess;
import MModel.VideoMarker;
import MModel.exeptions.MailExistException;
import MModel.exeptions.PasswordIncorectException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import net.thegreshams.firebase4j.error.FirebaseException;
import net.thegreshams.firebase4j.error.JacksonUtilityException;
import view.listView.Video;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;

public class MyVideo {
    public ListView listView;
    public Button close;
    public Button delete;
    private ArrayList<VideoMarker> help;
    Consumer consumer;

    public void initialize(Consumer consumer) throws IOException, JacksonUtilityException, PasswordIncorectException, FirebaseException, MailExistException {
        this.consumer = consumer;
        help = DataBaseAccess.getInstance().getUserVideos();
        ObservableList<String> videos = DataBaseAccess.getInstance().getUserVideoNames(help);
        listView.setOnMouseClicked(new ClickHandler());
        close.setOnAction(new ButtonHandler());
        delete.setOnAction(new ButtonHandler());
        listView.setItems(videos);
    }


    public void onClose() {
        Stage stage = (Stage) this.listView.getScene().getWindow();
        stage.close();
    }

    private class ButtonHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            if (event.getSource().equals(delete)) {
                try {
                    System.out.println(DataBaseAccess.getInstance().getVideoMarkerByName(help, (String) listView.getSelectionModel().getSelectedItem()).getVideoReference());
                    DataBaseAccess.getInstance().deleteVideo(DataBaseAccess.getInstance().getVideoMarkerByName(help, (String) listView.getSelectionModel().getSelectedItem()).getVideoReference());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (event.getSource().equals(close)) {
                onClose();
            }
        }
    }

    private class ClickHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            if (event.getSource().equals(listView)) {
                consumer.accept(DataBaseAccess.getInstance().getVideoMarkerByName(help, (String) listView.getSelectionModel().getSelectedItem()).getLatLong());
            }
        }
    }
}
