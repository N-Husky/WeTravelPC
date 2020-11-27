package view.controlers;

import MModel.User;
import MModel.VideoMarker;
import com.dlsc.gmapsfx.GoogleMapView;
import com.dlsc.gmapsfx.MapComponentInitializedListener;
import com.dlsc.gmapsfx.javascript.event.UIEventHandler;
import com.dlsc.gmapsfx.javascript.event.UIEventType;
import com.dlsc.gmapsfx.javascript.object.*;
import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.common.collect.Lists;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import netscape.javascript.JSObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class MainWindow extends Window implements MapComponentInitializedListener {

    public GoogleMapView mapView;
    public VBox video_box;
    private GoogleMap map;
    private Map<String, VideoMarker> videos = new HashMap<>();
    public static String firebase_baseUrl = "https://wetravel-1591a.firebaseio.com/";
    public static String firebase_apiKey = "AIzaSyCO06MSKvbYLnPGzBYPKpX8SlcPpiJupA8";

    @Override
    public void mapInitialized() {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream("I:\\wetravel-1591a-1fa332112603.json");
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
        mapOptions.center(new LatLong(47.6097, -122.3331))
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
            VideoMarker vidM = new VideoMarker(blob.getName(), blob.getName().split("/")[1], blob.getMetadata().get("position"));
            videos.put(blob.getName(), vidM);
            markerOptions.position(new LatLong(vidM.getX(), vidM.getY()));                                                      //введення позиції маркера
            final Marker marker = new Marker(markerOptions);
            marker.setTitle(new LatLong(vidM.getX(), vidM.getY()).toString());

            map.addUIEventHandler(marker, UIEventType.click, new UIEventHandler() {
                @Override
                public void handle(JSObject jsObject) {
                    video_box.setVisible(true);
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
    }


    public void uploadVideo(String videoName, String pathToVideo, String coordinates) throws IOException {
        // FILE UPLOAD
        FileInputStream stream = new FileInputStream("I:\\wetravel-1591a-1fa332112603.json");
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
    public URL getTempStreamReference(BlobInfo blbInfo, Storage storage){
        return storage.signUrl(blbInfo,2, TimeUnit.SECONDS, Storage.SignUrlOption.withV4Signature());
    }


    public String getStringCoordinates(double latitude, double longitude) {
        return new StringBuilder().append(String.valueOf(latitude) + "/" + String.valueOf(longitude)).toString();
    }

    public void initialize() {
        mapView.addMapInitializedListener(this);
    }


    public void hideVideo() {
        video_box.setVisible(false);
    }

    public void onClose() {
        Stage stage = (Stage) this.video_box.getScene().getWindow();
        stage.close();
    }
}
