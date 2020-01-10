package sample;

import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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

public class Controller {

    final int ITEM_PROPS = 11;

    public TextField idLabel, typeLabel, titleLabel, descLabel, notesLabel, ordDateLabel, expDateLabel, acqDateLabel, precLabel, useLabel, depsLabel, quanLabel;
    public ListView invListView;
    ArrayList<Item> itemArr = new ArrayList<>();

    public void onEnterData(){
        createItem();
        System.out.println(itemArr.get(0));
        updateXML();
        resetLocalArray();
        setItemArray(parseFileToArr());

    }

    public void resetLocalArray(){
        itemArr.clear();
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


    public void onSearchItem(){
        setItemArray(parseFileToArr());
        displayItems();

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
        String precautions = precLabel.getText();
        String usage = useLabel.getText();
        String departments = depsLabel.getText();
        String quantity = quanLabel.getText();

        //Create temporary Item object and initialize properties
        Item temp = new Item(id,type, title, description, notes, orderDate, expiryDate, acqDate, precautions, usage, departments, quantity);
        //Add Item object to itemArr array
        itemArr.add(temp);

    }
}
