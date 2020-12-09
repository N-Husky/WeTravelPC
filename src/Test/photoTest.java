package Test;

import MModel.DataBaseAccess;
import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.common.collect.Lists;
import net.thegreshams.firebase4j.error.FirebaseException;
import org.apache.http.entity.ContentType;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class photoTest {
    public static void main(String[] args) throws FirebaseException, IOException {
//        FileInputStream stream = new FileInputStream("./src/resources/wetravel-1591a-1fa332112603.json");
//        GoogleCredentials credentials = GoogleCredentials.fromStream(stream)
//                .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
//        stream.close();
//        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
//        // The ID of your GCP project
//        //String projectId = "wetravel-1591a";
//        // The ID of your GCS bucket
//        String bucketName = "wetravel-1591a.appspot.com";
//        // The ID of your GCS object
//        String objectName =  "-MN5NE8UAFRPTR52M6JJ"+ "/" + "profile_img";
//        // The path to your file to upload
//        String filePath = "C:\\Users\\NorthernHusky\\Pictures\\Dalida.png";
//        BlobId blobId = BlobId.of(bucketName, objectName);
//        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/png").build();
//        storage.create(blobInfo, Files.readAllBytes(Paths.get(filePath)));
//        Bucket bucket = storage.get(bucketName);
//        Page<Blob> pblob = bucket.list(Storage.BlobListOption.prefix("-MN5NE8UAFRPTR52M6JJ"));
        FileInputStream stream = new FileInputStream("./src/resources/wetravel-1591a-1fa332112603.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(stream)
                .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
        stream.close();
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        String bucketName = "wetravel-1591a.appspot.com";
        Bucket bucket = storage.get(bucketName);
        Page<Blob> pblob = bucket.list(Storage.BlobListOption.prefix("-MN5NE8UAFRPTR52M6JJ"));
        for (Blob blob : pblob.iterateAll()) {
            if(blob.getName().equals("-MN5NE8UAFRPTR52M6JJ/profile_img")){
                System.out.println("There is a photo");
                break;
            }
        }
        System.out.println("There is no such a photo");

    }
}
