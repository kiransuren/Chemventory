package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/*
    TODO:

 */

public class mainScreenController {

    final int ITEM_PROPS = 11;

    public TextField idLabel, typeLabel, titleLabel, descLabel, notesLabel, ordDateLabel, expDateLabel, acqDateLabel, precLabel, useLabel, depsLabel, quanLabel;
    public ListView invListView;
    public Button newItem;
    ArrayList<Item> itemArr = new ArrayList<>();

    public void onNewItem(){
        try {
            Stage stage = (Stage) newItem.getScene().getWindow();                          //Get current scene and window
            Parent root = FXMLLoader.load(getClass().getResource("newItem.fxml"));      //Set root to newItem.fxml
            //Set scene and show new scene
            Scene scene = new Scene(root, 1200, 800);           //Create new scene with root
            stage.setScene(scene);                                            //Set stage with new scene
            stage.show();                                                     //Show stage

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void resetLocalArray(){
        itemArr.clear();
    }

    public void onSearchItem(){
        setItemArray(parseFileToArr());
        displayItems();
    }

    public Item[] parseFileToArr(){
        //Creating new document for DOM manipulation
        String filePath = "saved_data/inventory.xml";
        File xmlFile = new File(filePath);                                              //Reference to file at path
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;

        try {
            dBuilder = dbFactory.newDocumentBuilder();      //Making new document builder
            Document doc = dBuilder.parse(xmlFile);         //Assigning doc to file at path
            doc.getDocumentElement().normalize();           //Normalize doc

            NodeList items = doc.getElementsByTagName("item");      //Get all course elements and store in nodelist
            Item[] outArr = new Item[items.getLength()];

            //loop for each course in courses
            for(int i=0; i<items.getLength();i++){
                Element tempElement = (Element)items.item(i);
                System.out.println(tempElement.getTextContent());
                System.out.println(i);
                //Fetch Entered Item Data from textfields
                String id = tempElement.getElementsByTagName("id").item(0).getTextContent();
                String type = tempElement.getElementsByTagName("type").item(0).getTextContent();
                String title = tempElement.getElementsByTagName("title").item(0).getTextContent();
                String description = tempElement.getElementsByTagName("description").item(0).getTextContent();
                String notes = tempElement.getElementsByTagName("notes").item(0).getTextContent();
                String orderDate = tempElement.getElementsByTagName("orderDate").item(0).getTextContent();
                String expiryDate = tempElement.getElementsByTagName("expiryDate").item(0).getTextContent();
                String acqDate = tempElement.getElementsByTagName("acqDate").item(0).getTextContent();
                String precautions = tempElement.getElementsByTagName("precautions").item(0).getTextContent();
                String usage = tempElement.getElementsByTagName("usage").item(0).getTextContent();
                String departments = tempElement.getElementsByTagName("departments").item(0).getTextContent();
                String quantity = tempElement.getElementsByTagName("quantity").item(0).getTextContent();

                //Create temporary Item object and initialize properties
                Item temp = new Item(id,type, title, description, notes, orderDate, expiryDate, acqDate, precautions, usage, departments, quantity);
                outArr[i] = temp;
            }
            return outArr;

        }catch (SAXException | ParserConfigurationException | IOException e1){
            e1.printStackTrace();
        }

        return null;


    }

    public void setItemArray(Item[] arr){
        resetLocalArray();
        for(int i=0; i<arr.length; i++){
            itemArr.add(arr[i]);
        }
    }

    public void displayItems(){
        /*
           Current usage: Show all Items in itemArr on TextArea
           TODO:
           -Add filter feature
         */

        invListView.getItems().clear();                                     //Clear items from ListView
        for(int i=0; i < itemArr.size(); i++){                                  //Loops through each entry in the array and creates a string with data from the i-th entry (concatenation)
            String itemString = itemArr.get(i).getViewString();                 //Gets string of class data
            invListView.getItems().add(itemString);
        }

    }

}
