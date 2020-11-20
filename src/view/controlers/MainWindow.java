package view.controlers;

import com.dlsc.gmapsfx.GoogleMapView;
import com.dlsc.gmapsfx.MapComponentInitializedListener;
import com.dlsc.gmapsfx.javascript.object.*;
import java.util.ArrayList;
import java.util.Map;

public class MainWindow implements MapComponentInitializedListener{

    public GoogleMapView mapView;
    private GoogleMap map;
    private Animation animation;


    @Override
    public void mapInitialized() {

        ArrayList<LatLong> list = new ArrayList<LatLong>();
        list.add(new LatLong(47.6197, -122.3231));
        list.add(new LatLong(47.6297, -122.3431));
        list.add(new LatLong(47.6397, -122.3231));
        list.add(new LatLong(47.6497, -122.3431));

        MapOptions mapOptions = new MapOptions();

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

        for (LatLong u : list){
            markerOptions.position(u);
            markerOptions.animation(Animation.DROP);
            Marker marker = new Marker(markerOptions);
            InfoWindowOptions infoOptions = new InfoWindowOptions();
            infoOptions.content(u.toString());


            InfoWindow window = new InfoWindow(infoOptions);
            window.open(map, marker);
            map.addMarker(marker);
        }

    }

    public void initialize() {
        mapView.addMapInitializedListener(this);

    }


}
