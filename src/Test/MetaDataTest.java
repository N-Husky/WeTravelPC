package Test;

import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.common.collect.Lists;
import com.google.firebase.cloud.FirestoreClient;
import com.sun.org.apache.xerces.internal.impl.xs.util.XSNamedMap4Types;
import net.thegreshams.firebase4j.error.FirebaseException;
import org.apache.http.entity.ContentType;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MetaDataTest {
    public static String firebase_baseUrl = "https://wetravel-1591a.firebaseio.com/";
    public static String firebase_apiKey = "AIzaSyCO06MSKvbYLnPGzBYPKpX8SlcPpiJupA8";

    public static void main(String[] args) throws FirebaseException, IOException {
//        Firebase firebase = new Firebase(firebase_baseUrl);
//        FirebaseResponse response = firebase.get();
//        Map<String, Object> dataMap = response.getBody();
//        dataMap = (Map) dataMap.get("users");
//        Set<String> codeKeys = dataMap.keySet();
//        for (String states : codeKeys) {
//            Map<String, Object> dataMap2 = (Map) dataMap.get(states);
//            for (Map.Entry<String, Object> item : dataMap2.entrySet()) {
//                System.out.println(item.getValue());
//            }
//        }
//        FileInputStream stream = new FileInputStream("I:\\wetravel-1591a-1fa332112603.json");
//        GoogleCredentials credentials = GoogleCredentials.fromStream(stream)
//                .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
//        stream.close();
//        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
//        // The ID of your GCP project
//        //String projectId = "wetravel-1591a";
//        // The ID of your GCS bucket
//        String bucketName = "wetravel-1591a.appspot.com";
//        // The ID of your GCS object
//        Bucket bucket = storage.get(bucketName);
//        System.out.println(bucket.get("-MM7aIc-jFBHl_Qr33S8/ghhv").getMetadata().get("position"));
//        Page<Blob> pblob = bucket.list(Storage.BlobListOption.prefix("-MM7aIc-jFBHl_Qr33S8"));
//        Blob blob = pblob.iterateAll().iterator().next();
//        System.out.println(blob.getName());
//        System.out.println(pblob.getValues().iterator().next().getName());
//        System.out.println(blob.toString());
//        Page<Blob> blobs = bucket.list();
//        BlobId blobId = BlobId.of(bucketName, "-MM7aIc-jFBHl_Qr33S8/ghhv");
//        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
//        System.out.println(blobInfo.getName());
//        for (Blob blobT : blobs.iterateAll()) {
        //System.out.println(storage.signUrl();
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
        String objectName = "-MM7aIc-jFBHl_Qr33S8/PVID2";
        // The path to your file to upload
        String filePath = "I:\\PVID.mp4";
        Bucket bucket = storage.get(bucketName);
        Page<Blob> pblob = bucket.list(Storage.BlobListOption.prefix("-MM7aIc-jFBHl_Qr33S8"));
        Blob blob = pblob.iterateAll().iterator().next();
        System.out.println(blob.signUrl(100, TimeUnit.SECONDS).toString());
        BlobId blobId = BlobId.of(bucketName, objectName);
        Map<String, String> newMetadata = new HashMap<>();
        newMetadata.put("position", "47/47");
        Calendar cal = new GregorianCalendar();
        newMetadata.put("uploadingTime", cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR) + " " + cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND));
        //System.out.println(cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR) + " " + cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND));
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setMetadata(newMetadata).setContentType("video/mp4").build();
        //storage.create(blobInfo, Files.readAllBytes(Paths.get(filePath)));
        System.out.println(
                "File " + filePath + " uploaded to bucket " + bucketName + " as " + objectName);
        //Calendar cal = new GregorianCalendar();
        //System.out.println(cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR) + " " + cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND));
    }
//            Map<String, String> newMetadata =n ew HashMap<>();
//            newMetadata.put("position", "47.6197/-122.3231");
//            blob.toBuilder().setMetadata(newMetadata).build().update();


//            for (Blob blob : pblob.iterateAll()) {
//            System.out.println(blob.getName());
//            }
//            for (Map.Entry<String, String> userMetadata : blobInfo.getMetadata().entrySet()) {
//                System.out.println(userMetadata.getKey() + "=" + userMetadata.getValue());
//            }
}

