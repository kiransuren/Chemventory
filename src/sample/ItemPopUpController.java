package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ItemPopUpController{

        @FXML private TableView<SingleItem>tableView;
        @FXML private TableColumn<SingleItem, String>propColm;
        @FXML private TableColumn<SingleItem, String>infoColm;
        public Button backButton;

        public void initialize(){
            propColm.setCellValueFactory(new PropertyValueFactory<SingleItem,String>("property"));
            infoColm.setCellValueFactory(new PropertyValueFactory<SingleItem,String>("information"));
            tableView.setItems(data);
        }

        public void editButton(){
            System.out.println("EDIT BUTTON PRESSED");
        }

        public void removeButton(){
            System.out.println("REMOVE BUTTON PRESSED");
        }

        public void backToMain(){
            try {
                Stage stage = (Stage) backButton.getScene().getWindow();                          //Get current scene and window
                Parent root = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));      //Set root to newItem.fxml
                //Set scene and show new scene
                Scene scene = new Scene(root, 1200, 800);           //Create new scene with root
                stage.setScene(scene);                                            //Set stage with new scene
                stage.show();                                                     //Show stage

            }catch (Exception e){
                e.printStackTrace();
            }

        }



        private final ObservableList<SingleItem> data =
                FXCollections.observableArrayList(
                        new SingleItem("ID", "15"),
                        new SingleItem("Type", "Material")
                );

        public static class SingleItem {

            private final SimpleStringProperty property;
            private final SimpleStringProperty information;

            private SingleItem(String prop, String info) {
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
        }
}
