package view;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import view.controlers.AcceptWindow;
import view.controlers.MainWindow;
import view.controlers.VideoUpload;

import java.io.IOException;
import java.util.function.Consumer;

public class StartPoint extends Application {

    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(final Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/registration.fxml"));
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.getScene().setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = primaryStage.getX() - event.getScreenX();
                yOffset = primaryStage.getY() - event.getScreenY();
            }
        });
        primaryStage.getScene().setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX() + xOffset);
                primaryStage.setY(event.getScreenY() + yOffset);
            }
        });
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void startMainWindow() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("fxml/MainWindow.fxml"));
        stage.setScene(new Scene((Parent) loader.load()));
        stage.setResizable(false);
        stage.setTitle("WeTravel");
        MainWindow controller = loader.getController();
        controller.initialize();
        stage.show();
    }

    public void startVideoUpload(Window window, String marker) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("fxml/videoUpload.fxml"));
        Scene scene = new Scene((Parent) loader.load());
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        VideoUpload controller = loader.getController();
        controller.initialize(window, marker);
        stage.show();
    }

    public void acceptor(Window window, String str, Consumer consumer) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("fxml/AcceptWindow.fxml"));
        Scene scene = new Scene((Parent) loader.load());
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.getScene().setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = stage.getX() - event.getScreenX();
                yOffset = stage.getY() - event.getScreenY();
            }
        });

        stage.getScene().setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() + xOffset);
                stage.setY(event.getScreenY() + yOffset);
            }
        });
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        AcceptWindow controller = loader.getController();
        controller.initialize(window, str, consumer);
        stage.show();
    }
}
