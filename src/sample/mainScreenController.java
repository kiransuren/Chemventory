package sample;


//IMPORTS
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
import java.util.Collections;

/*
    TODO:

 */

enum Filter{
    ID, TYPE, TITLE, USAGE, DEPTS
}

public class mainScreenController {

    final int ITEM_PROPS = 11;

    public ListView invListView;
    public TextField searchBar;
    public Button newItem;
    public ComboBox filterComboBox;
    ArrayList<Item> itemArr = new ArrayList<>();
    ArrayList<Item> filterArr = new ArrayList<>();


    public void initialize(){
        ArrayList<String> clist = new ArrayList<>();                                                          //Create ArrayList with all filters
        Collections.addAll(clist,"Filter by ID", "Filter by TYPE", "Filter by TITLE", "Filter by USAGE/LABS", "Filter by DEPARTMENT");
        //Add filters to combo box
        for(int i=0; i <clist.size(); i++){
            filterComboBox.getItems().add(clist.get(i));
        }
    }

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
        filterArr = filterArray(searchBar.getText().toLowerCase(), getFilter());
        displayItems(filterArr);
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

    public Filter getFilter(){
        String f = filterComboBox.getValue().toString();
        switch (f){
            case "Filter by ID":
                return Filter.ID;
            case "Filter by TYPE":
                return Filter.TYPE;
            case "Filter by TITLE":
                return Filter.TITLE;
            case "Filter by USAGE/LABS":
                return Filter.USAGE;
            case "Filter by DEPARTMENT":
                return Filter.DEPTS;
            default:
                return Filter.ID;
        }

    }

    public ArrayList<Item> filterArray(String search, Filter filter){
        ArrayList<Item> arr = new ArrayList<>();
        switch (filter){
            case ID:
                for(int i=0; i < itemArr.size(); i++ ){
                    if(itemArr.get(i).ID.toLowerCase().contains(search)){
                        arr.add(itemArr.get(i));
                    }
                }
                break;
            case TYPE:
                for(int i=0; i < itemArr.size(); i++ ){
                    if(itemArr.get(i).type.toLowerCase().contains(search)){
                        arr.add(itemArr.get(i));
                    }
                }
                break;
            case TITLE:
                for(int i=0; i < itemArr.size(); i++ ){
                    if(itemArr.get(i).title.toLowerCase().contains(search)){
                        arr.add(itemArr.get(i));
                    }
                }
                break;
            case USAGE:
                for(int i=0; i < itemArr.size(); i++ ){
                    if(itemArr.get(i).usage.toLowerCase().contains(search)){
                        arr.add(itemArr.get(i));
                    }
                }
                break;
            case DEPTS:
                for(int i=0; i < itemArr.size(); i++ ){
                    if(itemArr.get(i).dept.toLowerCase().contains(search)){
                        arr.add(itemArr.get(i));
                    }
                }
                break;
        }

        return arr;
    }

    public void displayItems(ArrayList<Item> arr){
        /*
           Current usage: Show all Items in parsed arr on TextArea
         */
        invListView.getItems().clear();                                     //Clear items from ListView
        for(int i=0; i < arr.size(); i++){                                  //Loops through each entry in the array and creates a string with data from the i-th entry (concatenation)
            String itemString = arr.get(i).getViewString();                 //Gets string of class data
            invListView.getItems().add(itemString);
        }

    }

    @FXML
    public void handleClickListView(){
        System.out.println("POOPS");
        if(invListView.getSelectionModel().getSelectedItem() != null) {
            System.out.println(invListView.getSelectionModel().getSelectedItem().toString());
            int indexOfItem = invListView.getSelectionModel().getSelectedIndex();
            saveCurrentItemXML(filterArr.get(indexOfItem));
            try {
                Stage stage = (Stage) newItem.getScene().getWindow();                          //Get current scene and window
                Parent root = FXMLLoader.load(getClass().getResource("itemPopUp.fxml"));      //Set root to newItem.fxml
                //Set scene and show new scene
                Scene scene = new Scene(root, 1200, 800);           //Create new scene with root
                stage.setScene(scene);                                            //Set stage with new scene
                stage.show();                                                     //Show stage

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public void saveCurrentItemXML(Item cItem){
        try {
            try{

                File file = new File("saved_data/current_item.xml");

                if(file.delete()){
                    System.out.println(file.getName() + " is deleted!");
                }else{
                    System.out.println("Delete operation is failed.");
                }

            }catch(Exception e){

                e.printStackTrace();

            }

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            Element curr = doc.createElement("current");
            doc.appendChild(curr);


            Element id = doc.createElement("id");
            id.setTextContent(cItem.ID);
            curr.appendChild(id);


            Element type = doc.createElement("type");
            type.setTextContent(cItem.type);
            curr.appendChild(type);

            Element title = doc.createElement("title");
            title.setTextContent(cItem.title);
            curr.appendChild(title);

            Element description = doc.createElement("description");
            description.setTextContent(cItem.description);
            curr.appendChild(description);

            Element notes = doc.createElement("notes");
            notes.setTextContent(cItem.notes);
            curr.appendChild(notes);

            Element orderDate = doc.createElement("orderDate");
            orderDate.setTextContent(cItem.dateOrdered);
            curr.appendChild(orderDate);

            Element expiryDate = doc.createElement("expiryDate");
            expiryDate.setTextContent(cItem.expiryDate);
            curr.appendChild(expiryDate);

            Element acqDate = doc.createElement("acqDate");
            acqDate.setTextContent(cItem.dateAcq);
            curr.appendChild(acqDate);

            Element precautions = doc.createElement("precautions");
            precautions.setTextContent(cItem.prec);
            curr.appendChild(precautions);

            Element usage = doc.createElement("usage");
            usage.setTextContent(cItem.usage);
            curr.appendChild(usage);

            Element departments = doc.createElement("departments");
            departments.setTextContent(cItem.dept);
            curr.appendChild(departments);

            Element quantity = doc.createElement("quantity");
            quantity.setTextContent(cItem.quantity);
            curr.appendChild(quantity);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("saved_data/current_item.xml"));
            transformer.transform(source, result);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}