package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class ItemPopUpController{

@FXML private TableView<Item>tableView;
@FXML private TableColumn<Item, String>propColm;
@FXML private TableColumn<Item, String>infoColm;

   // @Override
    public void initialize(URL location, ResourceBundle resources){
        propColm.setCellValueFactory(new PropertyValueFactory<Item,String>("property"));
        infoColm.setCellValueFactory(new PropertyValueFactory<Item,String>("information"));
        tableView.setItems(data);
    }



    private final ObservableList<Item> data =
            FXCollections.observableArrayList(
                    new Item("ID", "15"),
                    new Item("Type", "Material")
    );

    public static class Item {

    private final SimpleStringProperty property;
    private final SimpleStringProperty information;

    private Item(String prop, String info) {
        this.property = new SimpleStringProperty(prop);
        this.information = new SimpleStringProperty(info);
    }

    public String getProperty() {
        return property.get();
    }

    public void setProperty(String prop) {
        property.set(prop);
    }

    public String getInformation() {
        return information.get();
    }

    public void setInformation(String info) {
        information.set(info);
    }
}}
