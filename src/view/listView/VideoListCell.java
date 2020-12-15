package view.listView;

import MModel.VideoMarker;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VideoListCell extends ListCell<VideoMarker> implements Initializable {

    public AnchorPane root;
    public Label video_name;
    public Label video_date;
    private VideoMarker model;
    private static final Logger LOG = Logger.getLogger(VideoListCell.class.getName());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateSelected(false);
        root.getChildrenUnmodifiable().forEach(c -> {
            c.focusedProperty().addListener((obj, prev, curr) -> {
                if (!curr) {
                    commitEdit(model);
                }
            });
        });
        setGraphic(root);
    }

    public static VideoListCell newInstance() {
        FXMLLoader loader = new FXMLLoader(VideoListCell.class.getResource("/view/listView/video_cell.fxml"));
        try {
            loader.load();
            return loader.getController();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return null;
        }
    }

   // @Override
    protected void updateItem(Video item, boolean empty) {
     //   super.updateItem(item, empty);
        root.getChildrenUnmodifiable().forEach(c -> c.setVisible(!empty));
        if (!empty && item != null && !item.equals(this.model)) {
            video_name.textProperty().set(item.getNameProperty());
            video_date.textProperty().set(item.getDateProperty());
        }
      //  this.model = item;
    }

    @Override
    public void updateSelected(boolean selected) {
        super.updateSelected(selected);
        // update UI hints based on selected state
       root.getChildrenUnmodifiable().forEach(c -> {
            // setting mouse-transparent to false ensure that
            // the cell will get selected we click on a field in
            // a non-selected cell
            c.setMouseTransparent(!selected);
            // focus-traversable prevents users from "tabbing"
            // out of the currently selected cell
            c.setFocusTraversable(selected);
        });
        if (selected) {
            // start editing when the cell is selected
            startEdit();
        } else {
            if (model != null) {
                // commit edits if the cell becomes unselected
                // we're not keeping track of "dirty" state
                // so this will commit changes even to unmodified cells
                commitEdit(model);
            }
        }
    }

}
