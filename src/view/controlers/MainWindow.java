package view.controlers;

import MModel.DataBaseAccess;
import MModel.VideoMarker;
import MModel.exeptions.MailExistException;
import MModel.exeptions.PasswordIncorectException;
import com.dlsc.gmapsfx.GoogleMapView;
import com.dlsc.gmapsfx.MapComponentInitializedListener;
import com.dlsc.gmapsfx.javascript.event.GMapMouseEvent;
import com.dlsc.gmapsfx.javascript.event.MouseEventHandler;
import com.dlsc.gmapsfx.javascript.event.UIEventHandler;
import com.dlsc.gmapsfx.javascript.event.UIEventType;
import com.dlsc.gmapsfx.javascript.object.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import net.thegreshams.firebase4j.error.FirebaseException;
import net.thegreshams.firebase4j.error.JacksonUtilityException;
import netscape.javascript.JSObject;
import org.json.simple.parser.ParseException;
import view.StartPoint;


import java.io.*;
import java.util.ArrayList;
import java.util.function.Consumer;

public class MainWindow extends Window implements MapComponentInitializedListener {

    public GoogleMapView mapView;
    public AnchorPane anchorPane;
    public WebView videoView;
    public TextField search_field;
    public Button upload_btn;
    public Circle image_circle;
    public Rectangle logout;
    public Rectangle setings;
    public Label poster_name;
    public Circle poster_photo;
    public AnchorPane pain;
    public Rectangle hiderPain;
    private GoogleMap map;
    private Marker marker_load;
    private boolean firstin = true;
    LatLong markerForUpload;
    private double xOffset;
    private double yOffset;

    //done
    public void hideVideo() {
        System.out.println("++++++");
        anchorPane.setVisible(false);
        videoView.getEngine().load("");
    }

    public void onUserPhotoChange() throws IOException {
        MyFileChooser fileChooser = new MyFileChooser();
        final String filePath;
        filePath = fileChooser.forPhotoChoose(this, "User's photo changing");
        (new StartPoint()).acceptor(this, "You want to upload new profile image?", new Consumer() {
            @Override
            public void accept(Object o) {
                try {
                    DataBaseAccess.getInstance().uploadUserPhoto(filePath);
                    Image img = new Image(DataBaseAccess.getInstance().getPhotoLink(DataBaseAccess.getInstance().getUser().getDataBaseReference()));
                    image_circle.setFill(new ImagePattern(img));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //TO PUSH
    public void onAdd() {
        upload_btn.setVisible(true);
        hideVideo();
        final MapOptions mapOptions = new MapOptions();
        mapOptions.center(new LatLong(52.888, 51.556))
                .overviewMapControl(false).panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(false)
                .zoom(5)
                .minZoom(3);
        map = mapView.createMap(mapOptions);

        map.addMouseEventHandler(UIEventType.click, new MouseEventHandler() {

            @Override
            public void handle(GMapMouseEvent mouseEvent) {
                if (markerForUpload != null) {
                    marker_load.setPosition(mouseEvent.getLatLong());
                    markerForUpload = mouseEvent.getLatLong();
                    return;
                }
                markerForUpload = mouseEvent.getLatLong();
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(mouseEvent.getLatLong());
                marker_load = new Marker(markerOptions);
                map.addMarker(marker_load);
            }
        });
    }

    public void onClose() {
        Stage stage = (Stage) this.anchorPane.getScene().getWindow();
        onHide();
        stage.close();
    }
    //not done

    @Override
    public void mapInitialized() {


        Stage stage = (Stage) this.anchorPane.getScene().getWindow();
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
        if (firstin) {
            onShow();
            firstin = false;
        }
    }


    public void initialize() throws IOException {

        upload_btn.setVisible(false);
        mapView.addMapInitializedListener(this);
        Image img;
        try {
            img = new Image(DataBaseAccess.getInstance().getPhotoLink(DataBaseAccess.getInstance().getUser().getDataBaseReference()));
        } catch (NullPointerException e) {
            img = new Image("view/css/default_profile_img.jpg");
        }
        image_circle.setFill(new ImagePattern(img));
        Image logout = new Image("view/css/log-out.png");
        Image settings = new Image("view/css/settings.png");
        Image left = new Image("view/css/left-arrow-line-symbol.png");
        hiderPain.setFill(new ImagePattern(left));
        this.logout.setFill(new ImagePattern(logout));
        this.setings.setFill(new ImagePattern(settings));
    }

    public void onShow() {
        upload_btn.setVisible(false);
        if (marker_load != null)
            marker_load.setVisible(false);
        markerForUpload = null;
        final MapOptions mapOptions = new MapOptions();
        mapOptions.center(new LatLong(52.888, 51.556))
                .overviewMapControl(false)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(false)
                .zoom(5)
                .minZoom(3);

        map = mapView.createMap(mapOptions);

        MarkerOptions markerOptions = new MarkerOptions();
        for (VideoMarker vid : DataBaseAccess.getInstance().markdersForMap()) {
            final VideoMarker vidM = vid;
            markerOptions.position(new LatLong(vidM.getX(), vidM.getY()));
            final Marker marker = new Marker(markerOptions);
            marker.setTitle(vid.getVideoName());

            map.addUIEventHandler(marker, UIEventType.click, jsObject -> {

                WebEngine webEngine = videoView.getEngine();
                webEngine.setJavaScriptEnabled(true);
                try {
                    videoView.getEngine().load(DataBaseAccess.getInstance().getVideoPlayerLink(vidM));
                    System.out.println(DataBaseAccess.getInstance().getVideoPlayerLink(vidM));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Image img = null;
                try {
                    img = new Image(DataBaseAccess.getInstance().getPhotoLink(vid.getVideoReference().split("/")[0]));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    poster_name.setText(DataBaseAccess.getInstance().getUserInfo(vid.getVideoReference().split("/")[0]).getUserName());
                } catch (FirebaseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                poster_photo.setFill(new ImagePattern(img));
                poster_name.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        try {
                            (new StartPoint()).loaderInfo(vidM);
                        } catch (IOException | FirebaseException e) {
                            e.printStackTrace();
                        }
                    }
                });
                anchorPane.setVisible(true);
            });
            map.addUIEventHandler(marker, UIEventType.mouseover, new UIEventHandler() {
                @Override
                public void handle(JSObject jsObject) {
                    final InfoWindowOptions infoOptions = new InfoWindowOptions();
                    infoOptions.content(marker.getTitle());
                    final InfoWindow window = new InfoWindow(infoOptions);
                    window.open(map, marker);
                    map.addUIEventHandler(marker, UIEventType.mouseout, new UIEventHandler() {
                        @Override
                        public void handle(JSObject jsObject) {
                            window.close();
                        }
                    });
                }
            });
            map.addMarker(marker);
        }
    }

    public void uploadVideo() throws IOException {
        if (markerForUpload != null)
            (new StartPoint()).startVideoUpload(videoView.getScene().getWindow(), VideoMarker.getStringCoordinates(markerForUpload.getLatitude(), markerForUpload.getLongitude()));
        marker_load.setVisible(false);
        marker_load = null;
        markerForUpload = null;

    }

    public void onSearch() throws IOException, ParseException {
        map.setZoom(5);
        map.setCenter(new LatLong(Double.parseDouble(DataBaseAccess.getInstance().getCountryCoordinates(search_field.getText()).split("/")[0]),
                Double.parseDouble(DataBaseAccess.getInstance().getCountryCoordinates(search_field.getText()).split("/")[1])));

    }

    public void onLogOut() throws IOException {
        (new StartPoint()).acceptor(this, "Do you want to log-out?", new Consumer() {
            @Override
            public void accept(Object o) {
                DataBaseAccess.getInstance().deleteCredentials();
                onClose();
            }
        });
    }

    public void onSetings() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    (new StartPoint()).settings();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (FirebaseException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    ArrayList<VideoMarker> help;
    public void onMyVideos() throws IOException, JacksonUtilityException, PasswordIncorectException, FirebaseException, MailExistException {
        upload_btn.setVisible(false);

        help = DataBaseAccess.getInstance().getUserVideos();

        if (marker_load != null)
            marker_load.setVisible(false);
        markerForUpload = null;

        final MapOptions mapOptions = new MapOptions();
        mapOptions.center(new LatLong(52.888, 51.556))
                .overviewMapControl(false)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(false)
                .zoom(5)
                .minZoom(3);

        map = mapView.createMap(mapOptions);

        MarkerOptions markerOptions = new MarkerOptions();
        for (VideoMarker vid : help) {
            final VideoMarker vidM = vid;
            markerOptions.position(new LatLong(vidM.getX(), vidM.getY()));
            final Marker marker = new Marker(markerOptions);
            marker.setTitle(vid.getVideoName());

            map.addUIEventHandler(marker, UIEventType.click, jsObject -> {

                Image img = null;
                try {
                    img = new Image(DataBaseAccess.getInstance().getPhotoLink(vid.getVideoReference().split("/")[0]));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    poster_name.setText(DataBaseAccess.getInstance().getUserInfo(vid.getVideoReference().split("/")[0]).getUserName());//will wait for dataBase
                } catch (FirebaseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                poster_photo.setFill(new ImagePattern(img));
                poster_name.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        try {
                            (new StartPoint()).loaderInfo(vidM);
                        } catch (IOException | FirebaseException e) {
                            e.printStackTrace();
                        }
                    }
                });
                WebEngine webEngine = videoView.getEngine();
                webEngine.setJavaScriptEnabled(true);
                try {
                    videoView.getEngine().load(DataBaseAccess.getInstance().getVideoPlayerLink(vidM));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                anchorPane.setVisible(true);
            });

            map.addUIEventHandler(marker, UIEventType.mouseover, new UIEventHandler() {
                @Override
                public void handle(JSObject jsObject) {
                    final InfoWindowOptions infoOptions = new InfoWindowOptions();
                    infoOptions.content(marker.getTitle());
                    final InfoWindow window = new InfoWindow(infoOptions);
                    window.open(map, marker);
                    map.addUIEventHandler(marker, UIEventType.mouseout, new UIEventHandler() {
                        @Override
                        public void handle(JSObject jsObject) {
                            window.close();
                        }
                    });
                }
            });
            map.addMarker(marker);
        }
        (new StartPoint()).videoShow(new Consumer<LatLong>() {
            @Override
            public void accept(LatLong latLong) {
        if (latLong.equals(new LatLong(666,666))) {

                onShow();

        }
            else map.setCenter(latLong);
            }
        });
    }

    public void onHide(){
        Stage stage = (Stage) this.anchorPane.getScene().getWindow();
        stage.setIconified(true);
    }


    boolean hide = true;
    public void hidePain() {
        if (hide) {
            Image settings = new Image("view/css/right-arrow-line-symbol.png");
            hiderPain.setFill(new ImagePattern(settings));
            pain.setTranslateX(-290);
            hide= false;
        } else {
            Image settings = new Image("view/css/left-arrow-line-symbol.png");
            hiderPain.setFill(new ImagePattern(settings));
            pain.setTranslateX(0);
            hide = true;
        }
    }
}
