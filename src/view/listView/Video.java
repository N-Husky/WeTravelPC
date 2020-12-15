package view.listView;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Video {

    private final IntegerProperty idProperty = new SimpleIntegerProperty(0);
    private final StringProperty dateProperty = new SimpleStringProperty("13.13.1313");
    private final StringProperty nameProperty = new SimpleStringProperty("noname");

    public Video() {

    }

    public IntegerProperty idProperty() {
        return idProperty;
    }


    public Integer id() {
        return idProperty().get();
    }

    public Video id(Integer value) {
        idProperty().set(value);
        return this;
    }

    public StringProperty dateProperty() {
        return dateProperty;
    }

    public String date() {
        return dateProperty.get();
    }

    public Video date(String value) {
        dateProperty().set(value);
        return this;
    }

    public int getIdProperty() {
        return idProperty.get();
    }

    public IntegerProperty idPropertyProperty() {
        return idProperty;
    }

    public void setIdProperty(int idProperty) {
        this.idProperty.set(idProperty);
    }

    public String getDateProperty() {
        return dateProperty.get();
    }

    public StringProperty datePropertyProperty() {
        return dateProperty;
    }

    public void setDateProperty(String dateProperty) {
        this.dateProperty.set(dateProperty);
    }

    public String getNameProperty() {
        return nameProperty.get();
    }

    public StringProperty nameProperty() {
        return nameProperty;
    }

    public Video name(String nameProperty) {
        this.nameProperty.set(nameProperty);
        return this;
    }
}
