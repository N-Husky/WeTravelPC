package MModel;


import com.dlsc.gmapsfx.javascript.object.LatLong;

public class VideoMarker {
    private String videoReference;
    private String videoName;
    private String uploadDate;
    private double x;
    private double y;

    public String getUploadDate() {
        return uploadDate;
    }

    public VideoMarker(String videoReference, String videoName, String coordinates) {
        this.videoReference = videoReference;
        this.videoName = videoName;
        x = Double.parseDouble(coordinates.split("/")[0]);
        y = Double.parseDouble(coordinates.split("/")[1]);
    }
    public String getVideoReference() {
        return videoReference;
    }
    public static String getStringCoordinates(double latitude, double longitude) {
        return new StringBuilder().append(String.valueOf(latitude) + "/" + String.valueOf(longitude)).toString();
    }
    public String getVideoName() {
        return videoName;
    }
    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public LatLong getLatLong(){
        return new LatLong(x,y);
    }

    public VideoMarker(String videoReference, String videoName, String coordinates, String uploadDate) {
        this.videoReference = videoReference;
        this.videoName = videoName;
        this.uploadDate = uploadDate;
        x = Double.parseDouble(coordinates.split("/")[0]);
        y = Double.parseDouble(coordinates.split("/")[1]);
    }
}
