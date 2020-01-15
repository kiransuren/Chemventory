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
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ItemPopUpController{

    @FXML private TableView<SingleItem>tableView;
    @FXML private TableColumn<SingleItem, String>propColm;
    @FXML private TableColumn<SingleItem, String>infoColm;
    public Button backButton;
    public ObservableList<SingleItem> data;
    public Item curr_item;
    ArrayList<Item> itemArr = new ArrayList<>();

    public void initialize(){
        parseFileToDataList();
        setItemArray(parseFileToArr());
        propColm.setCellValueFactory(new PropertyValueFactory<>("attribute"));
        infoColm.setCellValueFactory(new PropertyValueFactory<>("value"));
        tableView.setEditable(true);
        System.out.println("IS EDITABLE" + tableView.isEditable());
        tableView.setItems(data);
        tableView.getItems().get(0);

    }

    public static class SingleItem {

        public SimpleStringProperty attribute;
        public SimpleStringProperty value;

        public SingleItem(String attr, String val) {
            this.attribute= new SimpleStringProperty(attr);
            this.value= new SimpleStringProperty(val);
        }

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

    public void resetLocalArray(){
        itemArr.clear();
    }

    public void parseFileToDataList(){
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
                curr_item = new Item(id,type, title, description, notes, orderDate, expiryDate, acqDate, precautions, usage, departments, quantity);
                System.out.println("TEMP ITEM CREATED" + curr_item.title);
                data = FXCollections.observableArrayList(
                        new SingleItem("ID", curr_item.ID),
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

            System.out.println("DATA USING GET:");
            System.out.println(data.get(2).getValue());
            System.out.println("DATA USING PROP:");
            System.out.println(data.get(2).value);


        }catch (SAXException | ParserConfigurationException | IOException e1) {
            e1.printStackTrace();
        }

    }


    public void editButton(){
        System.out.println("EDIT BUTTON PRESSED");
    }

    public void removeButton(){
        for(int i=0; i<itemArr.size(); i++){
            System.out.println("Curr_item id" + curr_item.ID);
            System.out.println("Item array" + itemArr.get(i).ID);
            if(itemArr.get(i).ID.equals(curr_item.ID)){
                System.out.println(itemArr.get(i).title);
                itemArr.remove(i);
                break;
            }
        }
        updateXML();
        backToMain();

    }

    public void updateXML(){
        try {
            try{

                File file = new File("saved_data/inventory.xml");

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
            Element rootElement = doc.createElement("items");
            doc.appendChild(rootElement);

            for(int i=0; i<itemArr.size(); i++){
                Element item = doc.createElement("item");

                Element id = doc.createElement("id");
                id.setTextContent(itemArr.get(i).ID);
                item.appendChild(id);


                Element type = doc.createElement("type");
                type.setTextContent(itemArr.get(i).type);
                item.appendChild(type);

                Element title = doc.createElement("title");
                title.setTextContent(itemArr.get(i).title);
                item.appendChild(title);

                Element description = doc.createElement("description");
                description.setTextContent(itemArr.get(i).description);
                item.appendChild(description);

                Element notes = doc.createElement("notes");
                notes.setTextContent(itemArr.get(i).notes);
                item.appendChild(notes);

                Element orderDate = doc.createElement("orderDate");
                orderDate.setTextContent(itemArr.get(i).dateOrdered);
                item.appendChild(orderDate);

                Element expiryDate = doc.createElement("expiryDate");
                expiryDate.setTextContent(itemArr.get(i).expiryDate);
                item.appendChild(expiryDate);

                Element acqDate = doc.createElement("acqDate");
                acqDate.setTextContent(itemArr.get(i).dateAcq);
                item.appendChild(acqDate);

                Element precautions = doc.createElement("precautions");
                precautions.setTextContent(itemArr.get(i).prec);
                item.appendChild(precautions);

                Element usage = doc.createElement("usage");
                usage.setTextContent(itemArr.get(i).usage);
                item.appendChild(usage);

                Element departments = doc.createElement("departments");
                departments.setTextContent(itemArr.get(i).dept);
                item.appendChild(departments);

                Element quantity = doc.createElement("quantity");
                quantity.setTextContent(itemArr.get(i).quantity);
                item.appendChild(quantity);


                rootElement.appendChild(item);

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

}
