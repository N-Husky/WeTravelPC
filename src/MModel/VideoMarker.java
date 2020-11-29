package MModel;


public class VideoMarker {
    private String videoReference;
    private String videoName;
    private double x;
    private double y;
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
}
