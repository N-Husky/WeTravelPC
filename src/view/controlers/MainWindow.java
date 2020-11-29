package view.controlers;

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
    private Map<String, VideoMarker> videos = new HashMap<>();
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

    public void uploadVideo(String videoName, String pathToVideo, String coordinates) throws IOException {
        // FILE UPLOAD
        FileInputStream stream = new FileInputStream("./src/resources/wetravel-1591a-1fa332112603.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(stream)
                .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
        stream.close();
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        // The ID of your GCP project
        //String projectId = "wetravel-1591a";
        // The ID of your GCS bucket
        String bucketName = "wetravel-1591a.appspot.com";
        // The ID of your GCS object
        String objectName = videoName;
        // The path to your file to upload
        String filePath = pathToVideo;//
        BlobId blobId = BlobId.of(bucketName, objectName);
        Map<String, String> newMetaData = new HashMap<>();
        newMetaData.put("position", coordinates);
        Calendar cal = new GregorianCalendar();
        newMetaData.put("uploadingTime", cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR) + " " + cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND));
        //System.out.println(cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR) + " " + cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND));
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setMetadata(newMetaData).build();
        storage.create(blobInfo, Files.readAllBytes(Paths.get(filePath)));
        System.out.println(
                "File " + filePath + " uploaded to bucket " + bucketName + " as " + objectName);
    }

    //Put this URL into MediaView
    public URL getTempStreamReference(BlobInfo blbInfo, Storage storage) {
        return storage.signUrl(blbInfo, 2, TimeUnit.SECONDS, Storage.SignUrlOption.withV4Signature());
    }


    public String getStringCoordinates(double latitude, double longitude) {
        return new StringBuilder().append(String.valueOf(latitude) + "/" + String.valueOf(longitude)).toString();
    }

    //TODO Чому це тут? а не у відеомаркері?
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
        FileInputStream stream = null;
        try {
            stream = new FileInputStream("./src/resources/wetravel-1591a-1fa332112603.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        GoogleCredentials credentials = null;
        try {
            credentials = GoogleCredentials.fromStream(stream)
                    .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        // The ID of your GCP project
        //String projectId = "wetravel-1591a";
        // The ID of your GCS bucket
        String bucketName = "wetravel-1591a.appspot.com";
        // The ID of your GCS object
        Bucket bucket = storage.get(bucketName);
        //System.out.println(bucket.get("-MM7aIc-jFBHl_Qr33S8/ghhv").getMetadata().get("position"));
        Page<Blob> pblob = bucket.list();
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
        for (Blob blob : pblob.iterateAll()) {
            if (blob.getName().split("/")[1].equals("profile_img"))
                continue;
            final VideoMarker vidM = new VideoMarker(blob.getName(), blob.getName().split("/")[1], blob.getMetadata().get("position"));
            videos.put(blob.getName(), vidM);
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
            (new StartPoint()).startVideoUpload(videoView.getScene().getWindow(), getStringCoordinates(markerForUpload.getLatitude(), markerForUpload.getLongitude()));
        markerForUpload = null;
        marker = null;
    }
}
