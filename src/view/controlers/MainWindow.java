package view.controlers;

import MModel.User;
import com.dlsc.gmapsfx.GoogleMapView;
import com.dlsc.gmapsfx.MapComponentInitializedListener;
import com.dlsc.gmapsfx.javascript.event.*;
import com.dlsc.gmapsfx.javascript.object.*;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import netscape.javascript.JSObject;

import java.util.ArrayList;
import java.util.Map;

public class MainWindow extends Window implements MapComponentInitializedListener {

    public GoogleMapView mapView;
    public VBox video_box;
    private GoogleMap map;

    @Override
    public void mapInitialized() {

        ArrayList<LatLong> list = new ArrayList<LatLong>();
        list.add(new LatLong(47.6197, -122.3231));
        list.add(new LatLong(47.6297, -122.3431));
        list.add(new LatLong(47.6397, -122.3231));
        list.add(new LatLong(47.6497, -122.3431));

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

        for (LatLong u : list) {
            markerOptions.position(u);                                                      //введення позиції маркера
            final Marker marker = new Marker(markerOptions);
            marker.setTitle(u.toString());

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
                    map.addUIEventHandler(marker,UIEventType.mouseout, new UIEventHandler() {
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
