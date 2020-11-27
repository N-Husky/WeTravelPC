package view.controlers;

import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.common.collect.Lists;
import javafx.fxml.FXML;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;


public class TestMedia {
    @FXML
    private MediaView videoView;

    public void initialize() throws IOException {
        FileInputStream stream = new FileInputStream("I:\\wetravel-1591a-1fa332112603.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(stream)
                .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
        stream.close();
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

        String bucketName = "wetravel-1591a.appspot.com";

        Bucket bucket = storage.get(bucketName);
        Page<Blob> blobs = bucket.list();
        BlobId blobId = BlobId.of(bucketName, "-MM7aIc-jFBHl_Qr33S8/PVID2");
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        URL url = storage.signUrl(blobInfo, 2, TimeUnit.SECONDS, Storage.SignUrlOption.withV4Signature());
        System.out.println(url.toExternalForm() + "My Message");
        System.out.println("My Message again woof");
        synchronized (url) {
            Media media = new Media(url.toExternalForm());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setAutoPlay(true);
            videoView.setMediaPlayer(mediaPlayer);
        }
    }
}
