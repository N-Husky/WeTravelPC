package view;

import MModel.VideoMarker;
import MModel.exeptions.MailExistException;
import MModel.exeptions.PasswordIncorectException;
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
import net.thegreshams.firebase4j.error.FirebaseException;
import net.thegreshams.firebase4j.error.JacksonUtilityException;
import view.controlers.*;
import view.fxml.MyVideo;

import java.io.IOException;
import java.util.function.Consumer;

public class StartPoint extends Application {

    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(final Stage primaryStage) throws Exception {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("fxml/registration.fxml"));
        Scene scene = new Scene((Parent) loader.load());
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());
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
        EnterControler controller = loader.getController();
        controller.initialize();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void startMainWindow() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("fxml/MainWindow.fxml"));
        Scene scene = new Scene((Parent) loader.load());
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
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

    public void loaderInfo(VideoMarker videoMarker) throws IOException, FirebaseException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("fxml/userWindow.fxml"));
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
        UserWindow controller = loader.getController();
        controller.initialize(videoMarker);
        stage.show();
    }

    public void settings() throws IOException, FirebaseException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("fxml/accountSettings.fxml"));
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
        AccountSettings controller = loader.getController();
        controller.initialize();
        stage.show();
    }

    public void biochange() throws IOException, FirebaseException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("fxml/changeBio.fxml"));
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
        ChangeBio controller = loader.getController();
        controller.initialize();
        stage.show();
    }

    public void namechange(String name) throws IOException, FirebaseException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("fxml/changeName.fxml"));
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
        ChangeName controller = loader.getController();
        controller.initialize(name);
        stage.show();
    }

    public void videoShow(Consumer consumer) throws IOException, FirebaseException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("fxml/MyVideo.fxml"));
        Scene scene = new Scene((Parent) loader.load());
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());
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
        MyVideo controller = loader.getController();
        try {
            controller.initialize(consumer);
        } catch (JacksonUtilityException e) {
            e.printStackTrace();
        } catch (FirebaseException e) {
            e.printStackTrace();
        } catch (PasswordIncorectException e) {
            e.printStackTrace();
        } catch (MailExistException e) {
            e.printStackTrace();
        }
        stage.show();
    }

}
