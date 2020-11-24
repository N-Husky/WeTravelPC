package MModel;

import com.dlsc.gmapsfx.javascript.object.LatLong;

public class VideoMarker {
    String videoReference;
    String videoName;
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
