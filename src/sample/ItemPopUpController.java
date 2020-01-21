/**
 * ItemPopUp Controller:
 * Used with the ItemPopUp.fxml, when an item is clicked for full view in mainScreen
 */


package sample;

//Imports
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
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
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class ItemPopUpController{


    //FXML References
    @FXML private TableView<SingleItem>tableView;
    @FXML private TableColumn<SingleItem, String>propColm;
    @FXML private TableColumn<SingleItem, String>infoColm;
    public Button backButton;
    public Label errorLabel;

    //Data Variables
    public ObservableList<SingleItem> data;                 //Data to output on TableView
    public Item curr_item;                                  //Currently viewing item
    ArrayList<Item> itemArr = new ArrayList<>();            //Array that holds all items

    public void initialize(){
        parseFileToDataList();
        setItemArray(parseFileToArr());
        propColm.setCellValueFactory(new PropertyValueFactory<>("attribute"));      //Instantiate property column
        infoColm.setCellValueFactory(new PropertyValueFactory<>("value"));          //Instantiate property column
        tableView.setItems(data);                                                   //Set tableview to show data
        //tableView.getItems().get(0);
        tableView.setEditable(true);                                                 //Set tableview to show data
        infoColm.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    public void onEditChanged(TableColumn.CellEditEvent<SingleItem, String> singleItemStringCellEditEvent) {
        /**
         * Handles when TableView is edited
         */
        SingleItem singleItem = tableView.getSelectionModel().getSelectedItem();            //Gets reference to currently edited item
        singleItem.setValue(singleItemStringCellEditEvent.getNewValue());                   //Changes value of cell to edited input
    }

    public static class SingleItem {
        /**
         * Class used in the tableview as the reference for that cell
         */

        //Properties
        public SimpleStringProperty attribute;
        public SimpleStringProperty value;

        public SingleItem(String attr, String val) {
            //Class Initializer
            this.attribute = new SimpleStringProperty(attr);
            this.value = new SimpleStringProperty(val);
        }
            //Getters and Setters
            public String getAttribute() {
                return attribute.get();
            }
            public void setAttribute(String attr) {
                attribute.set(attr);
            }
            public String getValue() {
                return value.get();
            }
            public void setValue(String val) {
                value.set(val);
            }
    }

    public Item[] parseFileToArr(){
        /**
         * Parses inventory.xml file to an Item array format
         */

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
                //Fetch Entered Item Data from textfields
                int id = Integer.parseInt(tempElement.getElementsByTagName("id").item(0).getTextContent());
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
        /**
         * Copies values from passed in Item[] array to itemArr ArrayList
         */
        resetLocalArray();
        for(int i=0; i<arr.length; i++){
            itemArr.add(arr[i]);
        }
    }

    public void resetLocalArray(){
        /**
         * Clears itemArr ArrayList
         */
        itemArr.clear();
    }

    public void parseFileToDataList(){
        /**
         * Parses saved_data.xml file to an Item format
         */

        //Creating new document for DOM manipulation
        String filePath = "saved_data/current_item.xml";
        File xmlFile = new File(filePath);                                              //Reference to file at path
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;

        try {
            dBuilder = dbFactory.newDocumentBuilder();      //Making new document builder
            Document doc = dBuilder.parse(xmlFile);         //Assigning doc to file at path
            doc.getDocumentElement().normalize();           //Normalize doc

            NodeList items = doc.getElementsByTagName("current");      //Get all course elements and store in nodelist

            //loop for each course in courses
            for(int i=0; i<items.getLength();i++){
                Element tempElement = (Element)items.item(i);
                //Fetch Entered Item Data from textfields
                int id = Integer.parseInt(tempElement.getElementsByTagName("id").item(0).getTextContent());
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
                curr_item = new Item(id,type, title, description, notes, orderDate, expiryDate, acqDate, precautions, usage, departments, quantity);
                //Sets data to take properties of current item
                data = FXCollections.observableArrayList(
                        new SingleItem("ID", Integer.toString(curr_item.ID)),
                        new SingleItem("Type", curr_item.type),
                        new SingleItem("Title", curr_item.title),
                        new SingleItem("Description", curr_item.description),
                        new SingleItem("Notes", curr_item.notes),
                        new SingleItem("Date Ordered", curr_item.dateOrdered),
                        new SingleItem("Expiry Date(s)", curr_item.expiryDate),
                        new SingleItem("Date Acquired", curr_item.dateAcq),
                        new SingleItem("Precautions/Safety", curr_item.prec),
                        new SingleItem("Usage/Labs", curr_item.usage),
                        new SingleItem("Departments", curr_item.dept),
                        new SingleItem("Quantity", curr_item.quantity)
                );
            }
        }catch (SAXException | ParserConfigurationException | IOException e1) {
            e1.printStackTrace();
        }

    }

    public void removeButton(){
        /**
         * Removes item from itemArr and updates XML file to remove item
         */

        //Loop through itemArr
        for(int i=0; i<itemArr.size(); i++){
            //If the current item id is the same as the item in itemArr
            if(itemArr.get(i).ID == curr_item.ID){
                //Remove from itemArr
                itemArr.remove(i);
                break;
            }
        }
        updateXML();
        backToMain();
    }

    public void updateXML(){
        /**
         * Updates XML file by deleted file then adding data from itemArr ArrayList
         */
        try {
            try{
                File file = new File("saved_data/inventory.xml");
                //Delete xml file
                if(file.delete()){
                }else{
                }
            }catch(Exception e){
                e.printStackTrace();
            }

            //Create a new XML document with items root
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            Element rootElement = doc.createElement("items");
            doc.appendChild(rootElement);

            //For each item in itemArr, create an XML object and include properties
            for(int i=0; i<itemArr.size(); i++){
                //Create item tag
                Element item = doc.createElement("item");

                //Add ID property to item element
                Element id = doc.createElement("id");
                id.setTextContent(Integer.toString(itemArr.get(i).ID));
                item.appendChild(id);

                //Add type property to item element
                Element type = doc.createElement("type");
                type.setTextContent(itemArr.get(i).type);
                item.appendChild(type);

                //Add title property to item element
                Element title = doc.createElement("title");
                title.setTextContent(itemArr.get(i).title);
                item.appendChild(title);

                //Add description property to item element
                Element description = doc.createElement("description");
                description.setTextContent(itemArr.get(i).description);
                item.appendChild(description);

                //Add notes property to item element
                Element notes = doc.createElement("notes");
                notes.setTextContent(itemArr.get(i).notes);
                item.appendChild(notes);

                //Add orderDate property to item element
                Element orderDate = doc.createElement("orderDate");
                orderDate.setTextContent(itemArr.get(i).dateOrdered);
                item.appendChild(orderDate);

                //Add expiryDate property to item element
                Element expiryDate = doc.createElement("expiryDate");
                expiryDate.setTextContent(itemArr.get(i).expiryDate);
                item.appendChild(expiryDate);

                //Add acqDate property to item element
                Element acqDate = doc.createElement("acqDate");
                acqDate.setTextContent(itemArr.get(i).dateAcq);
                item.appendChild(acqDate);

                //Add precautions property to item element
                Element precautions = doc.createElement("precautions");
                precautions.setTextContent(itemArr.get(i).prec);
                item.appendChild(precautions);

                //Add usage property to item element
                Element usage = doc.createElement("usage");
                usage.setTextContent(itemArr.get(i).usage);
                item.appendChild(usage);

                //Add departments property to item element
                Element departments = doc.createElement("departments");
                departments.setTextContent(itemArr.get(i).dept);
                item.appendChild(departments);

                //Add quantity property to item element
                Element quantity = doc.createElement("quantity");
                quantity.setTextContent(itemArr.get(i).quantity);
                item.appendChild(quantity);


                //Add item to the root object
                rootElement.appendChild(item);

                //Write data to XML file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new File("saved_data/inventory.xml"));
                transformer.transform(source, result);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void backToMain(){
        /**
         * Saves changes and return to mainScreen
         */

        boolean success = createItem();         //Create new Item from cells, return success
        if(success) {
            updateXML();                        //Update XML file with new data
            try {
                Stage stage = (Stage) backButton.getScene().getWindow();                          //Get current scene and window
                Parent root = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));      //Set root to newItem.fxml
                //Set scene and show new scene
                Scene scene = new Scene(root, 1200, 800);           //Create new scene with root
                stage.setScene(scene);                                            //Set stage with new scene
                stage.show();                                                     //Show stage

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public boolean createItem(){
        /**
          Fetch item data from cells and add new item to itemArr array
        */

        //Fetch Entered Item Data from cells
        String id = tableView.getItems().get(0).getValue();                             //YES
        String type = tableView.getItems().get(1).getValue();
        String title = tableView.getItems().get(2).getValue();
        String description = tableView.getItems().get(3).getValue();
        String notes = tableView.getItems().get(4).getValue();
        String orderDate = tableView.getItems().get(5).getValue();
        String expiryDate = tableView.getItems().get(6).getValue();
        String acqDate = tableView.getItems().get(7).getValue();
        String precautions = tableView.getItems().get(8).getValue();
        String usage = tableView.getItems().get(9).getValue();
        String departments = tableView.getItems().get(10).getValue();
        String quantity = tableView.getItems().get(11).getValue();

        if(!title.isEmpty()){
            //Goes through id, expiry and quantity validity checks
            if((isValidID(id) && isValidExpiry(expiryDate)) && isValidQuantity(quantity)){
                //Create temporary Item object and initialize properties
                for(int i=0; i<itemArr.size(); i++){
                    if(itemArr.get(i).ID == curr_item.ID){
                        itemArr.remove(i);
                    }
                }
                Item temp = new Item(Integer.parseInt(id),type, title, description, notes, orderDate, expiryDate, acqDate, precautions, usage, departments, quantity);
                itemArr.add(temp);
                return true;
            }else if(!isValidID(id)){
                //Invalid id
                errorLabel.setText("Please enter a unique integer for the ID value");
            }else if(!isValidExpiry(expiryDate)){
                //Invalid expiry date
                errorLabel.setText("Please enter the expiry date in the following format: dd/MM/yyyy | For no date type: none");
            }else if(!isValidQuantity(quantity)){
                //Invalid quantity
                errorLabel.setText("Please enter a positive integer quantity | For no date type: none");
            }
            else{
                //Invalid for all
                errorLabel.setText("Please enter valid data");
            }
            return false;
        }else{
            //No title present
            errorLabel.setText("Please enter a title for the item");
            return false;
        }
    }

    public boolean isValidID(String id){
        /**
         * Checks if ID is a positive integer and has not already been taken
         */
        try {
            int newId = Integer.parseInt(id);
            //Postive integer
            if(newId >=0){
                for (int i = 0; i < itemArr.size(); i++) {
                    //If ID has already been taken
                    if (itemArr.get(i).ID == newId) {
                        //If id is the same as the current item being viewed (it is ok to take this, as it is the original value)
                        if(itemArr.get(i).ID == curr_item.ID){
                            return true;
                        }else{
                            return false;
                        }
                    }
                }
            }else{
                return false;
            }
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public boolean isValidExpiry(String expiryDate){
        /**
         * Checks if expiry date is either in the "dd//MM/yyy" format or has a "none" string value
         */
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");            //format for strings
        //If expiry date is "none" string
        if(expiryDate.equals("none")){
            return true;
        }else{
            try{
                //Try to format string
                Date expiry = formatter.parse(expiryDate);
                return true;
            }catch (Exception e){
                //when there is an error (catch when parsing the formatted string)
                return false;
            }
        }

    }

    public boolean isValidQuantity(String quan){
        /**
         * Checks if quantity is a positive integer or "none" string
         */
        try{
            if(quan.equals("none")){
                //If quantity is "none" string
                return true;
            }else{
                //Try to parse quantity as string
                Integer.parseInt(quan);
                return true;
            }
        }catch (Exception e){
            //when there is an error (catch when parsing string to integer)
            return false;
        }
    }

}
