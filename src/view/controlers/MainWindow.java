package view.controlers;

import MModel.DataBaseAccess;
import MModel.User;
import MModel.VideoMarker;
import com.dlsc.gmapsfx.GoogleMapView;
import com.dlsc.gmapsfx.MapComponentInitializedListener;
import com.dlsc.gmapsfx.javascript.event.GMapMouseEvent;
import com.dlsc.gmapsfx.javascript.event.MouseEventHandler;
import com.dlsc.gmapsfx.javascript.event.UIEventHandler;
import com.dlsc.gmapsfx.javascript.event.UIEventType;
import com.dlsc.gmapsfx.javascript.object.*;
import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.common.collect.Lists;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import netscape.javascript.JSObject;
import view.StartPoint;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class MainWindow extends Window implements MapComponentInitializedListener {

    public GoogleMapView mapView;
    public AnchorPane anchorPane;
    public MediaView videoView;
    private GoogleMap map;
    public static String firebase_baseUrl = "https://wetravel-1591a.firebaseio.com/";
    public static String firebase_apiKey = "AIzaSyCO06MSKvbYLnPGzBYPKpX8SlcPpiJupA8";
    private Marker marker;
    private Marker marker_load;
    private File file;
    LatLong markerForUpload;
    @Override
    public void mapInitialized() {
        onShow();
    }

    public void onAdd() {
        hideVideo();
        final MapOptions mapOptions = new MapOptions();
        mapOptions.center(new LatLong(52.888, 51.556))
                .overviewMapControl(false).panControl(false).rotateControl(false).scaleControl(false).streetViewControl(false).zoomControl(false).zoom(12).minZoom(3);

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



    //Put this URL into MediaView
    public URL getTempStreamReference(BlobInfo blbInfo, Storage storage) {
        return storage.signUrl(blbInfo, 2, TimeUnit.SECONDS, Storage.SignUrlOption.withV4Signature());
    }




    //TODO Чому це тут? а не у відеомаркері?
    //Потому что
    public String getVideoPlayerLink(VideoMarker vm) throws IOException {//retrieve link for media player from videoMarker
        FileInputStream stream = new FileInputStream("./src/resources/wetravel-1591a-1fa332112603.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(stream)
                .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
        stream.close();
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        String bucketName = "wetravel-1591a.appspot.com";

        Bucket bucket = storage.get(bucketName);
        Page<Blob> blobs = bucket.list();
        BlobId blobId = BlobId.of(bucketName, vm.getVideoReference());
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        return storage.signUrl(blobInfo, 1, TimeUnit.MINUTES, Storage.SignUrlOption.withV4Signature()).toString();
    }

    public void initialize() {
        mapView.addMapInitializedListener(this);
    }

    public void hideVideo() {
        anchorPane.setVisible(false);
    }

    public void onClose() {
        Stage stage = (Stage) this.anchorPane.getScene().getWindow();
        stage.close();
    }

    public void onShow() {
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
                .zoom(12)
                .minZoom(3);

        map = mapView.createMap(mapOptions);

        MarkerOptions markerOptions = new MarkerOptions();
        for (VideoMarker vid : DataBaseAccess.getInstance().markdersForMap()) {
            final VideoMarker vidM = vid;
            markerOptions.position(new LatLong(vidM.getX(), vidM.getY()));                                                      //введення позиції маркера
            final Marker marker = new Marker(markerOptions);
            marker.setTitle(new LatLong(vidM.getX(), vidM.getY()).toString());

            map.addUIEventHandler(marker, UIEventType.click, new UIEventHandler() {
                @Override
                public void handle(JSObject jsObject) {
                    anchorPane.setVisible(true);
                    Media media = null;
                    try {
                        media = new Media(getVideoPlayerLink(vidM));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    final MediaPlayer mediaPlayer = new MediaPlayer(media);
                    mediaPlayer.setAutoPlay(true);

                    videoView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                                mediaPlayer.pause();
                                return;
                            } else if (mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
                                mediaPlayer.play();
                            } else {
                                //TODO  Після закінчення відео, повтор
                            }
                        }
                    });
                    videoView.setMediaPlayer(mediaPlayer);
                }
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
        marker = null;
    }

    public void uploadVideo() throws IOException {
        if (markerForUpload != null)
            (new StartPoint()).startVideoUpload(videoView.getScene().getWindow(), VideoMarker.getStringCoordinates(markerForUpload.getLatitude(), markerForUpload.getLongitude()));
        markerForUpload = null;
        marker = null;
    }
}
