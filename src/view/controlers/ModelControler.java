package view.controlers;

import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import net.thegreshams.firebase4j.error.FirebaseException;
import view.StartPoint;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ModelControler {

    int onBioClick = 0;
    int onNameClick = 0;

    public interface Listener{
        void onChange(ModelControler modelControler);
    }

    private List<Listener> listeners = new LinkedList<>();

    public void addListener(Listener listener){
        listeners.add(listener);
    }

    public void onBioClick(Circle circle) throws IOException, FirebaseException {
        onBioClick++;
        if(onBioClick%2==0){
            (new StartPoint()).biochange();
            Stage stage = (Stage) circle.getScene().getWindow();
            stage.close();
        }
        notifyObservers();
    }

    public void onLoginClick(Circle circle, String str) throws IOException, FirebaseException {
        onNameClick++;
        if(onNameClick%2==0){
            (new StartPoint()).namechange(str);
            Stage stage = (Stage) circle.getScene().getWindow();
            stage.close();
        }
        notifyObservers();
    }

    private void notifyObservers(){
        listeners.stream().forEach(listener -> listener.onChange(this));
    }

}
