package sample;
/**
 * mainScreenController, for mainScreen.fxml
 */

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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

enum Filter{
    /**
     * Filter enum, that is used to hold the state of the currently selected filter in filter box
     */
    ID, TYPE, TITLE, USAGE, DEPTS
}

enum ListOutput{
    /**
     * ListOutput enum, used to pick what output type to use for displayItems method
     */
    normal, expiry, lowstock
}

/*
 *  TODO:
 *  - Search item by Id, no item found needs label
 *
 */

public class mainScreenController {

    //Constants
    final int EXPIRY_VALUE = 183;                                  //DAYS
    final int LOWSTOCK_VALUE = 5;                                  //DAYS

    //References
    public ListView invListView, expListView, lowListView;
    public TextField searchBar;
    public Button newItem;
    public ComboBox filterComboBox;

    //Arrays holding items
    ArrayList<Item> itemArr = new ArrayList<>();                    //Holds all items
    ArrayList<Item> filterArr = new ArrayList<>();                  //Holds items based on filter
    ArrayList<Item> expiryArr = new ArrayList<>();                  //Holds item meeting expiry criteria
    ArrayList<Item> lowStockArr = new ArrayList<>();                //Holds items meeting lowStock criteria


    public void initialize(){
        setItemArray(parseFileToArr());
        itemArr = quickSort(itemArr, 0, itemArr.size()-1);       //Sort array by increasing ID value
        ArrayList<String> clist = new ArrayList<>();                        //Create ArrayList with all filters
        Collections.addAll(clist,"Filter by ID", "Filter by TYPE", "Filter by TITLE", "Filter by USAGE/LABS", "Filter by DEPARTMENT");
        //Add filters to combo box
        for(int i=0; i <clist.size(); i++){
            filterComboBox.getItems().add(clist.get(i));
        }
        updateExpiryArray();
        updateLowStockArray();
        displayItems(expiryArr, expListView, ListOutput.expiry);            //Display expired items
        displayItems(lowStockArr, lowListView, ListOutput.lowstock);        //Display low stock items
    }

    public void updateExpiryArray(){
        /**
         * Update the expiry date by looking at data from itemArr and checking if it meets the criteria
         */
        try{

            for(int i=0; i<itemArr.size(); i++){
                Item temp = itemArr.get(i);
                //Check if item date is not "none" and expiry time in days is less than default
                if(!(temp.expiryDate.toLowerCase().equals("none"))){
                    if(temp.isExpired() < EXPIRY_VALUE){
                        //Add item to expiry array
                        expiryArr.add(itemArr.get(i));
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateLowStockArray(){
        /**
         * Update the low stock by looking at data from itemArr and checking if it meets the criteria
         */
        for(int i=0; i<itemArr.size(); i++){
            Item temp = itemArr.get(i);
            try {
                if (!(temp.quantity.toLowerCase().equals("none"))) {
                    //Check if item quantity is not "none" and stock is less than default
                    if (Integer.parseInt(temp.quantity) < LOWSTOCK_VALUE) {
                        //Add item to low stock array
                        lowStockArr.add(itemArr.get(i));
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void onNewItem(){
        /**
         * Switches screen to newItem.fxml
         * Called when new item button is pressed
         */
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
        /**
         * Clears itemArr ArrayList
         */
        itemArr.clear();
    }

    public void onSearchItem(){
        /**
         * Filters array and displays new filtered array
         * Called on search button pressed
         */
        setItemArray(parseFileToArr());
        itemArr = quickSort(itemArr, 0, itemArr.size()-1);      //Sort array by ID
        filterArr = filterArray(searchBar.getText().toLowerCase(), getFilter());    //Filter array by searched keyword
        displayItems(filterArr, invListView, ListOutput.normal);            //Display new filtered array
    }

    public Item[] parseFileToArr(){
        /**
         * Parses inventory.xml file to an Item array format
         */

        //Creating new document for DOM manipulation
        String filePath = "./saved_data/inventory.xml";
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
                //System.out.println(tempElement.getTextContent());
                //System.out.println(i);
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

    public Filter getFilter(){
        /**
         * Finds the correct filter enum based on String value selected in ComboBox
         */
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
        /**
         * Returns filtered ArrayList by search keyword and filter type
         */
        ArrayList<Item> arr = new ArrayList<>();        //Create output array
        switch (filter){
            //Filter by ID
            case ID:
                try{
                    Item it = binarySearch(Integer.parseInt(search), itemArr);  //Binary Search for object with ID
                    if(it != null){
                        //If the item was found add to array
                        arr.add(it);
                    }else{
                        System.out.println("No item found, none in array");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    System.out.println("No item found, exceptional error");
                }
                break;
            //Filter by type
            case TYPE:
                for(int i=0; i < itemArr.size(); i++ ){
                    if(itemArr.get(i).type.toLowerCase().contains(search)){
                        //Check if search keyword (lowercase) is in item type
                        arr.add(itemArr.get(i));
                    }
                }
                break;
            //Filter by title
            case TITLE:
                for(int i=0; i < itemArr.size(); i++ ){
                    if(itemArr.get(i).title.toLowerCase().contains(search)){
                        //Check if search keyword (lowercase) is in item title
                        arr.add(itemArr.get(i));
                    }
                }
                break;
            //Filter by usage
            case USAGE:
                for(int i=0; i < itemArr.size(); i++ ){
                    if(itemArr.get(i).usage.toLowerCase().contains(search)){
                        //Check if search keyword (lowercase) is in item usage
                        arr.add(itemArr.get(i));
                    }
                }
                break;
            //Filter by departments
            case DEPTS:
                for(int i=0; i < itemArr.size(); i++ ){
                    if(itemArr.get(i).dept.toLowerCase().contains(search)){
                        //Check if search keyword (lowercase) is in item departments
                        arr.add(itemArr.get(i));
                    }
                }
                break;
        }
        return arr;
    }

    public void displayItems(ArrayList<Item> arr, ListView listview, ListOutput lo ){
        /**
         *  Display parsed ArrayList onto listview, depending on which list view
         */
        if(lo == ListOutput.normal) {
            //For main output
            listview.getItems().clear();                                     //Clear items from ListView
            for (int i = 0; i < arr.size(); i++) {                                  //Loops through each entry in the array and creates a string with data from the i-th entry (concatenation)
                String itemString = arr.get(i).getViewString();                 //Gets string of class data
                listview.getItems().add(itemString);
            }
        }else if(lo == ListOutput.expiry){
            //For expiry date output
            listview.getItems().clear();                                     //Clear items from ListView
            for (int i = 0; i < arr.size(); i++) {                                  //Loops through each entry in the array and creates a string with data from the i-th entry (concatenation)
                String itemString = arr.get(i).getExpiryString();                 //Gets string of class data
                listview.getItems().add(itemString);
            }
        }else if(lo == ListOutput.lowstock){
            //For low stock output
            listview.getItems().clear();                                     //Clear items from ListView
            for (int i = 0; i < arr.size(); i++) {                                  //Loops through each entry in the array and creates a string with data from the i-th entry (concatenation)
                String itemString = arr.get(i).getLowStockString();               //Gets string of class data
                listview.getItems().add(itemString);
            }
        }
    }

    public void handleClickListView(){
        /**
         * Opens new full view window (itemPopUp.xml) with selected item
         * Handle when list view is clicked
         */

        if(invListView.getSelectionModel().getSelectedItem() != null) {
            int indexOfItem = invListView.getSelectionModel().getSelectedIndex();       //Get index of item
            saveCurrentItemXML(filterArr.get(indexOfItem));                             //Save data of selected item to current_item.xm;
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
        /**
         * Saves the data of parsed Item to current_item xml
         * Deletes file then re creates with new data
         */

        try {
            try{
                File file = new File("saved_data/current_item.xml");
                if(file.delete()){
                    //Delete file
                    System.out.println(file.getName() + " is deleted!");
                }
            }catch(Exception e){
                e.printStackTrace();
            }

            //Create new FXML document with current tag
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            Element curr = doc.createElement("current");
            doc.appendChild(curr);

            //Add ID property to current element
            Element id = doc.createElement("id");
            id.setTextContent(Integer.toString(cItem.ID));
            curr.appendChild(id);

            //Add type property to current element
            Element type = doc.createElement("type");
            type.setTextContent(cItem.type);
            curr.appendChild(type);

            //Add title property to current element
            Element title = doc.createElement("title");
            title.setTextContent(cItem.title);
            curr.appendChild(title);

            //Add description property to current element
            Element description = doc.createElement("description");
            description.setTextContent(cItem.description);
            curr.appendChild(description);

            //Add notes property to current element
            Element notes = doc.createElement("notes");
            notes.setTextContent(cItem.notes);
            curr.appendChild(notes);

            //Add orderDate property to current element
            Element orderDate = doc.createElement("orderDate");
            orderDate.setTextContent(cItem.dateOrdered);
            curr.appendChild(orderDate);

            //Add expiryDate property to current element
            Element expiryDate = doc.createElement("expiryDate");
            expiryDate.setTextContent(cItem.expiryDate);
            curr.appendChild(expiryDate);

            //Add acqDate property to current element
            Element acqDate = doc.createElement("acqDate");
            acqDate.setTextContent(cItem.dateAcq);
            curr.appendChild(acqDate);

            //Add precautions property to current element
            Element precautions = doc.createElement("precautions");
            precautions.setTextContent(cItem.prec);
            curr.appendChild(precautions);

            //Add usage property to current element
            Element usage = doc.createElement("usage");
            usage.setTextContent(cItem.usage);
            curr.appendChild(usage);

            //Add departments property to current element
            Element departments = doc.createElement("departments");
            departments.setTextContent(cItem.dept);
            curr.appendChild(departments);

            //Add quantity property to current element
            Element quantity = doc.createElement("quantity");
            quantity.setTextContent(cItem.quantity);
            curr.appendChild(quantity);

            //Write data to XML file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("saved_data/current_item.xml"));
            transformer.transform(source, result);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<Item> quickSort(ArrayList<Item> itemInp, int start, int end){//start = 0. end = item.length-1
        ArrayList<Item>sortedArr=itemInp;
        if (start<end){
            int p=partition(sortedArr,start,end);
            quickSort(sortedArr,start,p-1);//Before (left)partition
            quickSort(sortedArr,p+1,end);//After (right)partition
        }
        return sortedArr;

    }

    private static int partition(ArrayList<Item> itemInp, int start, int end){
        int pivot = itemInp.get(end).ID;//picks final value in array to make pivot
        int i= start-1;//lower index then first
        for(int j=start; j<end; j++){//goes through every element in array except for last (pivot)
            if(itemInp.get(j).ID<pivot){//while element is smaller than pivot, swaps lower numbers with pivot
                i++;
                Item temp= itemInp.get(i);
                itemInp.set(i,itemInp.get(j));
                itemInp.set(j,temp);
            }
        }
        Item temp = itemInp.get(i+1);//Swaps higher numbers with pivot
        itemInp.set(i+1,itemInp.get(end));
        itemInp.set(end,temp);
        return i+1;
    }

    public Item binarySearch(int value, ArrayList<Item> d){
        int half =d.size()/2;
        System.out.print(half);

        if(d.size()==1 && d.get(half).ID != value){
            //When item cant be found
            System.out.print("Item cannot be found");
            return null;
        }
        if(d.get(half).ID == value ){
            //When the ID is the input value
            return d.get(half);
        }else if(d.get(half).ID < value){
            //When the ID is less than the input value, repeat process with array starting from checked entry to end of current array
            List s = d.subList(half,d.size());
            ArrayList<Item> listOfItems = new ArrayList<>(s.size());
            listOfItems.addAll(s);
            return binarySearch(value, listOfItems);
        }else if(d.get(half).ID> value){
            //When the ID is more than the input value, repeat process with array starting from start of current array to checked entry
            List s = d.subList(0,half);
            ArrayList<Item> listOfItems = new ArrayList<>(s.size());
            listOfItems.addAll(s);
            return binarySearch(value, listOfItems);
        }else{
            //For any other issue with the algorithm
            System.out.print("other issue");
            return null;
        }
    }


}
