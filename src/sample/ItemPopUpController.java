package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ItemPopUpController<User> {
    @FXML private TableView<User> tableView;
    @FXML private TableColumn<User, String>propColm ;
    @FXML private TableColumn<User, String> infoColm;
    public void initialize(){
        propColm.setCellValueFactory(new PropertyValueFactory<User,String>("ID"));
        //infoColm.setCellValueFactory(new PropertyValueFactory<User, String>());


    }
}
