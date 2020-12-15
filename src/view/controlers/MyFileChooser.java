package view.controlers;

import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.FileNotFoundException;

public class MyFileChooser {

    public String forPhotoChoose(Window window, String title) throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        File file;
        fileChooser.setTitle(title);
        FileChooser.ExtensionFilter extFilter = new FileChooser
                .ExtensionFilter("All photos", "*.jpeg", "*.png", "*.jpg");
        fileChooser.getExtensionFilters().add(extFilter);
        extFilter = new FileChooser
                .ExtensionFilter("PNG files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);
        extFilter = new FileChooser
                .ExtensionFilter("JPG files (*.jpg)", "*.jpg");
        fileChooser.getExtensionFilters().add(extFilter);
        extFilter = new FileChooser
                .ExtensionFilter("JPEG files (*.jpeg)", "*.jpeg");
        fileChooser.getExtensionFilters().add(extFilter);
        file = fileChooser.showOpenDialog(window);

        System.out.println(file.getAbsolutePath());
        if (file == null) throw new NullPointerException();
        if (!file.exists()) throw new FileNotFoundException();
        return file.getAbsolutePath();
    }

    public String forVideoChoose(Window window, String title) throws FileNotFoundException {

        FileChooser fileChooser = new FileChooser();
        File file;
        fileChooser.setTitle(title);
        FileChooser.ExtensionFilter extFilter = new FileChooser
                .ExtensionFilter("All videos", "*.mp4","*.wmv","*.m4v","*.mov","*.avi","*.mp4");
        fileChooser.getExtensionFilters().add(extFilter);

        extFilter = new FileChooser
                .ExtensionFilter("WMV files (*.wmv)", "*.wmv");
        fileChooser.getExtensionFilters().add(extFilter);
        extFilter = new FileChooser
                .ExtensionFilter("M4V files (*.wmv)", "*.m4v");
        fileChooser.getExtensionFilters().add(extFilter);
        extFilter = new FileChooser
                .ExtensionFilter("MOV files (*.mov)", "*.mov");
        fileChooser.getExtensionFilters().add(extFilter);
        extFilter = new FileChooser
                .ExtensionFilter("AVi files (*.avi)", "*.avi");
        fileChooser.getExtensionFilters().add(extFilter);
        extFilter = new FileChooser
                .ExtensionFilter("MP4 files (*.mp4)", "*.mp4");
        fileChooser.getExtensionFilters().add(extFilter);

        file = fileChooser.showOpenDialog(window);
        System.out.println(file.getAbsolutePath());
        if (file == null) throw new NullPointerException();
        if (!file.exists()) throw new FileNotFoundException();
        return file.getAbsolutePath();
    }

}
