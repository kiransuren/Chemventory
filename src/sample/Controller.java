package sample;

import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/*
    TODO:

 */

public class Controller {

    final int ITEM_PROPS = 11;

    public TextField idLabel, typeLabel, titleLabel, descLabel, notesLabel, ordDateLabel, expDateLabel, acqDateLabel, precLabel, useLabel, depsLabel, quanLabel;
    public ListView invListView;
    ArrayList<Item> itemArr = new ArrayList<>();

    public void onEnterData(){
        createItem();
        System.out.println(itemArr.get(0));

    }

    public void parseFileToArr(){
        //Creating new document for DOM manipulation
        String filePath = "saved_data/inventory.xml";
        File xmlFile = new File(filePath);                                              //Reference to file at path
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;

        try {
            dBuilder = dbFactory.newDocumentBuilder();      //Making new document builder
            Document doc = dBuilder.parse(xmlFile);         //Assigning doc to file at path
            doc.getDocumentElement().normalize();           //Normalize doc

            NodeList items = doc.getElementsByTagName("items");      //Get all course elements and store in nodelist
            Element tempElement;
            String[] temp = new String[ITEM_PROPS];
            //loop for each course in courses
            for(int i=0; i<items.getLength();i++){
                tempElement = (Element) items.item(i);                        //Get each course
            }

        }catch (SAXException | ParserConfigurationException | IOException e1){
            e1.printStackTrace();
        }


    }


    public void onSearchItem(){
        displayItems();
        parseFileToArr();
    }

    public void displayItems(){
        /*
           Current usage: Show all Items in itemArr on TextArea
           TODO:
           -Add filter feature
         */

        invListView.getItems().removeAll();                                     //Clear items from ListView
        for(int i=0; i < itemArr.size(); i++){                                  //Loops through each entry in the array and creates a string with data from the i-th entry (concatenation)
            String itemString = itemArr.get(i).getViewString();                 //Gets string of class data
            invListView.getItems().add(itemString);
        }

    }

    public void createItem(){
        /*
          Fetch item data from textfields and add new item to itemArr array
        */

        //Fetch Entered Item Data from textfields
        String id = Integer.toString(Integer.parseInt(idLabel.getText()));
        String type = typeLabel.getText();
        String title = titleLabel.getText();
        String description = descLabel.getText();
        String notes = notesLabel.getText();
        String orderDate = ordDateLabel.getText();
        String expiryDate = expDateLabel.getText();
        String acqDate = acqDateLabel.getText();
        String precDate = precLabel.getText();
        String usage = useLabel.getText();
        String departments = depsLabel.getText();
        String quantity = quanLabel.getText();

        //Create temporary Item object and initialize properties
        Item temp = new Item(id,type, title, description, notes, orderDate, expiryDate, acqDate, precDate, usage, departments, quantity);
        //Add Item object to itemArr array
        itemArr.add(temp);

    }
}
